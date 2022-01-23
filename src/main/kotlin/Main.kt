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
    private val squares_per_row = 64
    private val size: Float = WIDTH.toFloat() / squares_per_row
    var ticker: Long = 0
    var t0 = System.currentTimeMillis()
    var centerX: Float = 0.0f
    var centerY: Float = 0.0f

    override fun create() {
        fps = FPSLogger()
        spriteBatch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        plane = Texture(Gdx.files.internal("textures/fly.png"))
    }

    override fun render() {
        fps.log()
        centerX = ((sin(ticker.toDouble() / 1100) * WIDTH).toFloat() + WIDTH) / 2
        centerY = ((cos(ticker.toDouble() / 650) * HEIGHT).toFloat() + HEIGHT) / 2
        val t1 = System.currentTimeMillis()
        ticker += (t1 - t0)
        t0 = t1
        clearScreen(0.01f, 0.1f, 0.1f, 1f)

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            var offsetX = 0f;
            var offsetY = 0f;

            for (y in 0..squares_per_row) {
                var blueValueTopLeft = blueValue(0, y)
                var blueValueBottomLeft = blueValue(0, y + 1)
                for (x in 0..squares_per_row) {
                    val blueValueTopRigth = blueValue(x + 1, y)
                    val blueValueBottomRigth = blueValue(x + 1, y + 1)
                    it.rect(
                        offsetX,
                        offsetY,
                        size,
                        size,
                        Color(1 - blueValueTopLeft, 0.2f, blueValueTopLeft, 1f),
                        Color(1 - blueValueTopRigth, 0.2f, blueValueTopRigth, 1f),
                        Color(1 - blueValueBottomRigth, 0.2f, blueValueBottomRigth, 1f),
                        Color(1 - blueValueBottomLeft, 0.2f, blueValueBottomLeft, 1f),
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
            it.draw(plane, centerX / 2, HEIGHT.toFloat() / 2, 56f, 056f)
        }
    }

    fun blueValue(posX: Int, posY: Int): Float {
        return 1 - sqrt((posX * size - centerX).pow(2) + (posY * size - centerY).pow(2)) / WIDTH * 9
    }
}