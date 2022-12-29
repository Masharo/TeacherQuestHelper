import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import model.Answer
import model.Record
import values.bg
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText

@Composable
fun App(nav: Navigation, answerList: Array<Record>) {
    val answerList = remember { mutableStateListOf(*answerList) }
    var selectItem by remember { mutableStateOf<Record?>(null) }
    var isSaveAction by remember { mutableStateOf(false) }

    if (isSaveAction) {
        FileDialog(
            title = "Сохранить",
            isLoad = false,
            onResult = {
                it.deleteIfExists()
                it.createFile()

                it.writeText(
                    answerList.toMoodleRecordList()
                )
            },
            disposeAction = {
                isSaveAction = false
            }
        )
    }

    MaterialTheme {
        Scaffold(
            modifier = Modifier.padding(10.dp),
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(bottom = 35.dp),
                    onClick = { answerList.add(Record()) }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(resourcePath = "/img/icon-plus.png"),
                        contentDescription = null
                    )
                }
            }
        ) {
            Row(

            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(
                            items = answerList
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 4.dp)
                                        .clip(shape = RoundedCornerShape(10.dp))
                                        .clickable {
                                            selectItem = it
                                        },
                                    backgroundColor = bg
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(10.dp),
                                        text = it.name.value,
                                        fontSize = 30.sp
                                    )
                                }

                                Button(
                                    shape = RoundedCornerShape(percent = 50),
                                    contentPadding = PaddingValues(0.dp),
                                    onClick = {
                                        answerList.remove(element = it)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.White
                                    ),
                                    elevation = ButtonDefaults.elevation(0.dp)
                                ) {
                                    Text(
                                        text = "❌",
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            isSaveAction = true
                        }
                    ) {
                        Text(
                            text = "Сохранить"
                        )
                    }

                }

                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                        .fillMaxHeight()
                        .clip(
                            shape = RoundedCornerShape(10.dp)
                        ),
                    elevation = 2.dp,
                    backgroundColor = bg
                ) {
                    selectItem?.let {
                        Item(it)
                    }
                }

                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                        .fillMaxHeight()
                        .clip(
                            shape = RoundedCornerShape(10.dp)
                        ),
                    elevation = 2.dp,
                    backgroundColor = bg
                ) {
                    selectItem?.let {
                        Answers(it)
                    }
                }

            }
        }
    }
}

@Composable
fun Item(record: Record) {
    Column {
        TextField(
            value = record.name.value,
            singleLine = true,
            modifier = Modifier
                .padding(
                    vertical = 20.dp,
                    horizontal = 10.dp
                )
                .background(color = Color.White)
                .fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 25.sp
            ),
            onValueChange = {
                record.name.value = it
            },
            label = {
                Text(
                    text = "Название вопроса",
                    fontSize = 20.sp
                )
            }
        )

        TextField(
            value = record.title.value,
            singleLine = false,
            modifier = Modifier
                .padding(
                    vertical = 20.dp,
                    horizontal = 10.dp
                )
                .background(color = Color.White)
                .fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 25.sp
            ),
            onValueChange = {
                record.title.value = it
            },
            label = {
                Text(
                    text = "Текст вопроса",
                    fontSize = 20.sp
                )
            }
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            text = "Количество ответов"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .align(
                        alignment = Alignment.CenterVertically
                    ),
                textAlign = TextAlign.Left,
                fontSize = 25.sp,
                text = "Несколько"
            )

            Switch(
                checked = record.oneTrueAnswer.value,
                onCheckedChange = {
                    record.oneTrueAnswer.value = it

                    if (it) {
                        record.answers.forEach { answer ->
                            answer.isTrue.value = false
                        }
                    }
                }
            )

            Text(
                modifier = Modifier
                    .align(
                        alignment = Alignment.CenterVertically
                    ),
                textAlign = TextAlign.Right,
                fontSize = 25.sp,
                text = "Один"
            )
        }

    }
}

@Composable
fun Answers(record: Record) {
    Column {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(items = record.answers) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .clickable {

                        },
                    backgroundColor = bg
                ) {
                    Column {
                        TextField(
                            value = it.title.value,
                            singleLine = false,
                            modifier = Modifier
                                .padding(
                                    vertical = 20.dp,
                                    horizontal = 10.dp
                                )
                                .background(color = Color.White)
                                .fillMaxWidth()
                                .clip(
                                    shape = RoundedCornerShape(5.dp)
                                ),
                            textStyle = TextStyle(
                                fontSize = 25.sp
                            ),
                            onValueChange = { newValue ->
                                it.title.value = newValue
                            },
                            label = {
                                Text(
                                    text = "ответ",
                                    fontSize = 20.sp
                                )
                            }
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Checkbox(
                                checked = it.isTrue.value,
                                onCheckedChange = { value ->
                                    if (record.oneTrueAnswer.value) {
                                        record.answers.forEach {
                                            it.isTrue.value = false
                                        }
                                    }

                                    it.isTrue.value = value
                                }
                            )

                            Text(
                                text = "Ответ верен"
                            )

                            Spacer(
                                Modifier
                                    .weight(1f)
                            )

                            Button(
                                shape = RoundedCornerShape(percent = 50),
                                contentPadding = PaddingValues(0.dp),
                                onClick = {
                                    record.answers.remove(it)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = bg
                                ),
                                elevation = ButtonDefaults.elevation(0.dp)
                            ) {
                                Text(
                                    text = "❌",
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }


        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 15.dp,
                    vertical = 8.dp
                ),
            onClick = {
                record.answers.add(
                    Answer()
                )
            }
        ) {
            Text(
                text = "Добавить ответ"
            )
        }
    }
}

@Composable
fun NavigationContainer(appScope: ApplicationScope) {
    val navigation = Navigation(appScope)
    val navigationState = remember {
        navigation.state
    }

    Window(
        state = WindowState(
            size = DpSize(1300.dp, 800.dp)
        ),
        onCloseRequest = appScope::exitApplication,
        title = "Конструктор тестов"
    ) {
        navigation.toScreen(navigationState.value)
    }

}

fun main() = application {
    NavigationContainer(this)
}
