// ConfettiUtils.kt
package graduation.plantcare.ui.home
import android.graphics.Color
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

object ConfettiUtils {
    @JvmStatic
    fun showConfetti(view: KonfettiView) {
        val party = Party(
            angle = Angle.TOP,
            speed = 10f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(Color.YELLOW, Color.GREEN, Color.MAGENTA),
            emitter = Emitter(duration = 5, TimeUnit.SECONDS).max(200),
            position = Position.Relative(0.5, 0.5)
        )
        view.start(party)
    }
}
