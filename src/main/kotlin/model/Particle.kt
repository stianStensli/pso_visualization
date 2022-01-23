package model

import model.ParticlePosition.Companion.randomPosition
import java.lang.Float.max
import java.lang.Float.min
import kotlin.random.Random.Default.nextFloat

class Particle {
    private var position: ParticlePosition = randomPosition()
    private var personalBest = position
    private var velocity = floatArrayOf(0f, 0f)

    fun updateVelocity(globalBest: ParticlePosition, c1: Float, c2: Float, maxVelocity: Float) {
        val r1 = FloatArray(globalBest.getPosition().size) { nextFloat() }
        val r2 = FloatArray(globalBest.getPosition().size) { nextFloat() }

        val personalWeighting = position.getPosition().zip(personalBest.getPosition()) { p, pb ->  pb - p }.toFloatArray()
            .zip(r1) { res, r -> res * r * c1}.toFloatArray()
        val globalWeighting = position.getPosition().zip(globalBest.getPosition()) { p, gb ->  gb - p }.toFloatArray()
            .zip(r2) { res, r -> res * r * c2}.toFloatArray()

        velocity = personalWeighting.zip(globalWeighting) { p, g -> p + g }.toFloatArray()
            .zip(velocity){ res, v -> max(min(maxVelocity, res + v), -maxVelocity)}.toFloatArray()
    }

    fun updatePosition(valueFunction: (position: FloatArray) -> Float) {
        position.updatePosition(velocity)
        calculateFitness(valueFunction)
    }

    fun getPosition() = position.getPosition()
    fun getPersonalBest() = personalBest
    fun getBestFitness() = personalBest.getFitness()

    fun calculateFitness(valueFunction: (position: FloatArray) -> Float) {
        val currentFitness = valueFunction(position.getPosition())
        position.setFitness(currentFitness)

        if (currentFitness > personalBest.getFitness()) {
            personalBest = position
        }
    }
}
