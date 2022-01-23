package model

interface Algorithm {
    fun run(valueFunction: (position: FloatArray) -> Float)
}