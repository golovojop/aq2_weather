package k.s.yarlykov.extensions

import java.text.SimpleDateFormat
import java.util.*

// Получить дату в формате "dd/MM" на days дней назад
fun Date.daysAgo(days: Int): String {
    val format = "dd/MM"
    val calendar = Calendar.getInstance().apply {
        time = this@daysAgo
        add(Calendar.DAY_OF_YEAR, -Math.abs(days))
    }

    return SimpleDateFormat(format, Locale.getDefault()).format(calendar.time)
}

// Получить дату в формате "dd/MM" на days дней вперед
fun Date.daysAhead(days: Int): String {
    val format = "dd/MM"
    val calendar = Calendar.getInstance().apply {
        time = this@daysAhead
        add(Calendar.DAY_OF_YEAR, Math.abs(days))
    }

    return SimpleDateFormat(format, Locale.getDefault()).format(calendar.time)
}