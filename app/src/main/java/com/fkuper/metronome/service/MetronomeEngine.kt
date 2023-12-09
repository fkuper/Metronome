package com.fkuper.metronome.service

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.fkuper.metronome.utils.MetronomeConfig
import com.fkuper.metronome.R
import com.fkuper.metronome.utils.SoundLoader
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

private const val SAMPLE_RATE_IN_HZ = 48_000
private const val SILENCE_CHUNK_SIZE = 8_000

/**
 * This implementation is heavily inspired by the following source:
 * https://github.com/Kr0oked/Metronome/blob/master/app/src/main/java/com/bobek/metronome/domain/Metronome.kt
 */
class MetronomeEngine(
    private val context: Context,
    override val lifecycle: Lifecycle
) : LifecycleOwner {

    private var metronomeConfig = MetronomeConfig()
    private var metronomeJob: Job? = null
    private var numberOfSoundTicks: Int = calculateNumberOfSoundTicks()
    private var listener: TickListener? = null

    private val silence = FloatArray(SILENCE_CHUNK_SIZE)
    private val clickSound = loadSound(R.raw.metronome_click_normal)
    private val emphasisSound = loadSound(R.raw.metronome_click_emphasis)

    fun start(config: MetronomeConfig) {
        metronomeConfig = config
        numberOfSoundTicks = calculateNumberOfSoundTicks()
        metronomeJob = lifecycleScope.launch(Dispatchers.IO) {
            metronomeLoop()
        }
        listener?.onStartTicks()
    }

    fun reset() {
        metronomeJob?.cancel()
        metronomeJob = null
        listener?.onStopTicks()
    }

    fun setListener(listener: TickListener) {
        this.listener = listener
    }

    private suspend fun metronomeLoop() {
        val track = getNewAudioTrack()
        track.play()

        try {
            var tickCount = 0
            while (true) {
                writeTickPeriod(track, tickCount)
                tickCount = (tickCount + 1) % numberOfSoundTicks
                if (shouldAdvanceOneVisualTick(tickCount)) {
                    listener?.onTick(tickCount / metronomeConfig.noteValue.delayDivisor)
                }
            }
        } catch(e: CancellationException) {
            track.pause()
        } finally {
            track.release()
        }
    }

    private suspend fun writeTickPeriod(track: AudioTrack, tickCount: Int) {
        var sizeWritten = 0
        val periodSize = calculatePeriodSize()
        val sound = if (tickCount == 0) emphasisSound else clickSound

        sizeWritten += writeNextAudioData(track, sound, periodSize, sizeWritten)
        yield()

        writeSilenceUntilPeriodFinished(track, sizeWritten)
    }

    private suspend fun writeSilenceUntilPeriodFinished(track: AudioTrack, previousSizeWritten: Int) {
        var sizeWritten = previousSizeWritten
        while (true) {
            val periodSize = calculatePeriodSize()
            if (sizeWritten >= periodSize) {
                break
            }

            sizeWritten += writeNextAudioData(track, silence, periodSize, sizeWritten)
            yield()
        }
    }

    private fun writeNextAudioData(
        track: AudioTrack,
        data: FloatArray,
        periodSize: Int,
        sizeWritten: Int
    ): Int {
        val size = calculateAudioSizeToWriteNext(data, periodSize, sizeWritten)
        writeAudio(track, data, size)
        return size
    }

    private fun writeAudio(track: AudioTrack, data: FloatArray, size: Int) {
        val result = track.write(data, 0, size, AudioTrack.WRITE_BLOCKING)
        if (result < 0) {
            throw IllegalStateException("Failed to play audio data. Error code: $result")
        }
    }

    private fun calculateAudioSizeToWriteNext(data: FloatArray, periodSize: Int, sizeWritten: Int): Int {
        val sizeLeft = periodSize - sizeWritten
        return if (data.size > sizeLeft) sizeLeft else data.size
    }

    private fun calculatePeriodSize(): Int =
        60 * SAMPLE_RATE_IN_HZ / metronomeConfig.bpm / metronomeConfig.noteValue.delayDivisor

    private fun calculateNumberOfSoundTicks(): Int =
        metronomeConfig.timeSignature.upper * metronomeConfig.noteValue.delayDivisor

    private fun shouldAdvanceOneVisualTick(tickCount: Int): Boolean =
        (tickCount % metronomeConfig.noteValue.delayDivisor) == 0

    private fun getNewAudioTrack(): AudioTrack {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
            .setSampleRate(48000)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()

        val minBufferSize = AudioTrack.getMinBufferSize(
            audioFormat.sampleRate,
            audioFormat.channelMask,
            audioFormat.encoding
        )

        return AudioTrack.Builder()
            .setAudioAttributes(audioAttributes)
            .setAudioFormat(audioFormat)
            .setBufferSizeInBytes(minBufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }

    private fun loadSound(resourceId: Int) = context.resources
        .openRawResource(resourceId)
        .use(SoundLoader::readDataFromWavPcmFloat)

}