import model.Answer
import model.Record

fun Answer.toMoodleAnswer(percent: Float?): String {
    return "\n${
        percent?.let {
            when (it) {
                100f -> "~%100%"
                -100f -> "~%-100%"
                else -> "~%$it%"
            }
        }?: run {
            if (isTrue.value) "="
            else "~"
        }
    }${title.value}"
}

fun List<Answer>.toMoodleAnswerList(isOneAnswerTrue: Boolean): String {

    val answers = StringBuilder()

    if (!isOneAnswerTrue) {
        val countTrue = count { it.isTrue.value }

        val truePercent = 100f / if (countTrue == 0) 1 else countTrue
        val falsePercent = -100f / (size - countTrue).let { if (it == 0) 1 else it }

        forEach {
            answers.append(
                if (it.isTrue.value) it.toMoodleAnswer(truePercent)
                else it.toMoodleAnswer(falsePercent)
            )
        }
    } else {
        forEach {
            answers.append(
                it.toMoodleAnswer(null)
            )
        }
    }

    return answers.toString()
}

fun Record.toMoodleRecord(): String {
    return "::${name.value}::${title.value}{${answers.toMoodleAnswerList(oneTrueAnswer.value)}\n}\n\n"
}

fun List<Record>.toMoodleRecordList(): String {
    val moodleFormat = StringBuilder()

    this.forEach {
        moodleFormat.append(
            it.toMoodleRecord()
        )
    }

    return moodleFormat.toString()
}