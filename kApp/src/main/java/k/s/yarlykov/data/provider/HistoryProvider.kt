package k.s.yarlykov.data.provider

import android.content.Context
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.History
import k.s.yarlykov.extensions.daysAgo
import java.util.*

object HistoryProvider {

    // Генерит прогноз за последние daysAgo дней
    fun build(context: Context, daysAgo: Int): List<History> {
        val list = mutableListOf<History>()
        val today = Date()

        val images = context.resources.obtainTypedArray(R.array.historyLogos)

        for(i in 1..daysAgo) {
            list.add(History(today.daysAgo(i),
                    images.getResourceId(inRange(0, images.length() - 1), 0),
                    "$tDay℃ ~ $tNight℃"))
        }

        images.recycle()
        return list
    }

    // Дневная температура
    val tDay: Int
    get() = inRange(15, 10)

    // Ночная температура
    val tNight: Int
    get() = inRange(8, 6)

    // Генератор чисел в диапазоне min ~ (min + range)
    private fun inRange(min: Int, range: Int) = min + Random().nextInt(range)
}