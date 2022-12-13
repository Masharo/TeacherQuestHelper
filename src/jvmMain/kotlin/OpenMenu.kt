import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope


@Composable
fun StartMenu(appScope: ApplicationScope, nav: Navigation) {

    var isSaveAction by remember { mutableStateOf(false) }

    if (isSaveAction) {
        FileDialog(
            title = "Открыть",
            isLoad = true,
            onResult = {
                nav.answers = it.toFile().fromDocToAnswers()
            },
            disposeAction = {
                isSaveAction = false
                if (nav.answers != null) nav.state.value = nav.stateMain
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 5.dp,
                    start = 5.dp,
                    end = 5.dp
                ),
            onClick = {
                nav.state.value = nav.stateMain
            }
        ) {
            Text(
                text = "Создать новый вариант"
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            onClick = {
                isSaveAction = true
            }
        ) {
            Text(
                text = "Загрузить вопросы из файла"
            )
        }
    }
}