import androidx.compose.runtime.mutableStateOf
import model.Answer
import model.Record
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File

fun File.fromDocToAnswers(): Array<Record> {
    val text = XWPFWordExtractor(XWPFDocument(this.inputStream())).text
    val listLines = text.split("\n")
    val arrayRecords = mutableListOf<Record>()
    var countTrue = 0
    var id = 1

    listLines.forEach { line ->

        val lineTrim = line.trim()

        run {
            if (lineTrim.isNotBlank()) {
                if (lineTrim.length > 2) {

                    val first = lineTrim.substring(0..1)

                    if (first == "Q:") {
                        countTrue = 0

                        arrayRecords.add(
                            Record(
                                name = mutableStateOf("Вопрос ${id}"),
                                title = mutableStateOf(line.substring(2).trim()),
                                oneTrueAnswer = mutableStateOf(true)
                            )
                        )

                        id++

                        return@run
                    }
                }

                var title = lineTrim.replace("A:", "").trimStart()
                val first = title.first()

                var isTrue = false

                if (first == '+') {
                    isTrue = true
                    title = title.substring(1).trim()

                    countTrue++

                    if (countTrue > 1) {
                        arrayRecords.last().oneTrueAnswer.value = false
                    }
                }

                arrayRecords.last().answers.add(
                    Answer(
                        title = mutableStateOf(title),
                        isTrue = mutableStateOf(isTrue)
                    )
                )

            }
        }
    }

    return arrayRecords.toTypedArray()
}