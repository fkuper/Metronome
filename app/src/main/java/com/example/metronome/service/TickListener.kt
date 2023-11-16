package com.example.metronome.service

interface TickListener {
    fun onStartTicks()
    fun onTick()
    fun onStopTicks()
}