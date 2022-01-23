import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.graphics.use
import model.PSO
import kotlin.math.*

const val WIDTH = 1920
const val HEIGHT = 1080
const val FPS = 60

fun main() {
    val config = LwjglApplicationConfiguration().apply {
        width = WIDTH
        height = HEIGHT
        foregroundFPS = FPS
    }
    LwjglApplication(TiminingApplication(), config)
}

class TiminingApplication : KtxApplicationAdapter {
    private lateinit var spriteBatch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var plane: Texture
    private lateinit var fps: FPSLogger
    private val squaresPerRow = 264
    private val size: Float = WIDTH.toFloat() / squaresPerRow
    var ticker: Long = 0
    var t0 = System.currentTimeMillis()
    var centerX: Float = 0.0f
    var centerY: Float = 0.0f
    var algorithm = PSO()

    override fun create() {
        fps = FPSLogger()
        spriteBatch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        plane = Texture(Gdx.files.internal("textures/fly.png"))
        algorithm.init { (x, y) -> valueFunctionPso(x, y) }
    }

    override fun render() {
        val a = algorithm.getGlobalBest().getPosition().get(0).toString() +","+ algorithm.getGlobalBest().getPosition().get(1) +", " + algorithm.getGlobalBest().getFitness()
        println(a)
        algorithm.run {(x, y) -> valueFunctionPso(x, y) }

        centerX =  WIDTH.toFloat()/2
        centerY = HEIGHT.toFloat() / 2
        val t1 = System.currentTimeMillis()
        ticker += (t1 - t0)
        t0 = t1
        clearScreen(0.01f, 0.1f, 0.1f, 1f)

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            var offsetX = 0f;
            var offsetY = 0f;

            for (y in 0..squaresPerRow) {
                var blueValueTopLeft = valueFunction(0, y)
                var blueValueBottomLeft = valueFunction(0, y + 1)
                for (x in 0..squaresPerRow) {
                    val blueValueTopRigth = valueFunction(x + 1, y)
                    val blueValueBottomRigth = valueFunction(x + 1, y + 1)
                    it.rect(
                        offsetX,
                        offsetY,
                        size,
                        size,
                        Color(1 - blueValueTopLeft, 0.5f, blueValueTopLeft, 1f),
                        Color(1 - blueValueTopRigth, 0.5f, blueValueTopRigth, 1f),
                        Color(1 - blueValueBottomRigth, 0.5f, blueValueBottomRigth, 1f),
                        Color(1 - blueValueBottomLeft, 0.5f, blueValueBottomLeft, 1f),
                    )
                    blueValueTopLeft = blueValueTopRigth
                    blueValueBottomLeft = blueValueBottomRigth
                    offsetX += size
                }
                offsetX = 0f
                offsetY += size
            }
        }
        spriteBatch.use {
            algorithm.getPopulation().iterator().forEach {
                spriteBatch.draw(plane, it.getPosition()[0]-28, it.getPosition()[1]-28, 56f, 56f)
            }
        }
    }

    private fun valueFunction(posX: Int, posY: Int): Float {
        return valueFunctionPso(posX * size, posY * size)
    }

    private fun valueFunctionPso(posX: Float, posY: Float): Float {
        val dist = distFromCenter(posX, posY)
        return 1 - dist / WIDTH * 9
    }

    private fun distFromCenter(x: Float, y: Float): Float {
        return sqrt((x - centerX).pow(2) + (y - centerY).pow(2))
    }
}