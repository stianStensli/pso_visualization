package model

class PSO : Algorithm {
    private val swarmSize = 15 // Should be between 10-50
    private val importanceOfPersonalBest = .003f // C1
    private val importanceOfNeighbourhoodBest = .002f // C2
    private val maxVelocity = 10f
    private val population = Array(swarmSize) { Particle()}
    private var globalBest: ParticlePosition = ParticlePosition.randomPosition()

    fun init(valueFunction: (position: FloatArray) -> Float) {
        population.forEach {
            it.calculateFitness(valueFunction)
        }
    }
    override fun run(valueFunction: (position: FloatArray) -> Float) {
        // Update position and velocity
        population.forEach {
            it.updateVelocity(globalBest, importanceOfPersonalBest, importanceOfNeighbourhoodBest, maxVelocity)
            it.updatePosition(valueFunction)
        }

        // Update Global if necessary
        population.forEach {
            updateGlobal(it)
        }
    }

    private fun updateGlobal(indv: Particle) {
        if (indv.getBestFitness() > globalBest.getFitness()) {
            globalBest = indv.getPersonalBest()
        }
    }

    fun getPopulation() = population

    fun getGlobalBest() = globalBest
}