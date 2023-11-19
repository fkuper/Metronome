package com.fkuper.metronome.service

interface TickListener {
    /**
     * Called when the engine starts.
     */
    fun onStartTicks()

    /**
     * Called whenever the engine performs one tick.
     * Starts counting at 0 and goes up to NoteValue.upper - 1 before going back to 0.
     */
    fun onTick(tickCount: Int)

    /**
     * Called when the engine stops. This should reset the metronome engine back to 0.
     */
    fun onStopTicks()
}