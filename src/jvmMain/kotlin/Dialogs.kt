import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.io.File
import java.nio.file.Path

@Composable
fun FileDialog(
    title: String,
    isLoad: Boolean,
    onResult: (result: Path) -> Unit,
    disposeAction: () -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(ComposeWindow(), "Choose a file", if (isLoad) LOAD else SAVE) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    file?.let {
                        onResult(File(directory).resolve(it).toPath())
                    } ?: run {
//                        onResult(null)
                    }
                }
                disposeAction()
            }



        }.apply {
            this.title = title
        }
    },
    dispose = FileDialog::dispose
)