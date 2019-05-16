package k.s.yarlykov.extensions

import java.text.SimpleDateFormat
import java.util.*

// Получить дату в формате "dd/MM" на days дней назад
fun Date.daysAgo(days: Int): String {
    return this.daysThrough(days)
}

// Получить дату в формате "dd/MM" на days дней вперед
fun Date.daysAhead(days: Int): String {
    return this.daysThrough(days, false)
}

// Дата в формате "dd/MM" на shift дней вперед/назад
private fun Date.daysThrough(shift: Int, isAgo: Boolean = true): String {
    val format = "dd/MM"
    val days = Math.abs(shift) * (if (isAgo) -1 else 1)

    val calendar = Calendar.getInstance().apply {
        time = this@daysThrough
        add(Calendar.DAY_OF_YEAR, days)
    }
    return SimpleDateFormat(format, Locale.getDefault()).format(calendar.time)
}