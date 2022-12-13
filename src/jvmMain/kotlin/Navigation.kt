import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.ApplicationScope
import model.Record

class Navigation(private val appScope: ApplicationScope) {
    val stateStart = "START"
    val stateMain = "MAIN"

    var answers: Array<Record>? = null

    @Composable
    fun toScreen(state: String) {
        when(state) {
            stateStart -> {
                StartMenu(appScope, this)
            } else -> {
                App(this, answers ?:run { emptyArray() })
            }
        }
    }

    val state = mutableStateOf(stateStart)
}