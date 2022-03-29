package model

import java.lang.Float.max
import java.lang.Float.min
import kotlin.random.Random.Default.nextFloat

const val SIZE_OF_BOARD = 1080f
class ParticlePosition(x: Float, y: Float) {
    private var position: FloatArray
    private var fitness: Float = 0.0f

    fun getPosition() = position
    fun getFitness() = fitness
    fun setFitness(value: Float): ParticlePosition {
        fitness = value
        return this
    }

    fun updatePosition(velocity: FloatArray) {
        position = position.zip(velocity) { p, v -> min(max(p + v,0f), SIZE_OF_BOARD) }.toFloatArray()
    }

    companion object {
        fun randomPosition(): ParticlePosition = ParticlePosition(nextFloat() * SIZE_OF_BOARD, nextFloat() * SIZE_OF_BOARD)
    }

    init {
        position = floatArrayOf(x, y)
    }
}