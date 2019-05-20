package k.s.yarlykov.data.provider

import android.content.Context
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.History
import k.s.yarlykov.extensions.daysAgo
import java.util.*

object HistoryProvider {

    private val listHistory = mutableListOf<History>()

    //

    /**
     * Генерит прогноз за последние daysAgo дней.
     *
     * @isNeedNew - определяет нужно ли генерить новый массив данных
     * или вернуть имеющийся. Нужен для случаев восстановления активити
     * с тем же городом.
     */
    fun build(context: Context, daysAgo: Int, isNeedNew: Boolean): List<History> {
        if(!isNeedNew) return listHistory

        listHistory.clear()
        val today = Date()
        val images = context.resources.obtainTypedArray(R.array.historyLogos)

        for (i in 1..daysAgo) {
            listHistory.add(History(today.daysAgo(i),
                    images.getResourceId(inRange(0, images.length() - 1), 0),
                    "$tDay℃ ~ $tNight℃"))
        }

        images.recycle()
        return listHistory
    }

    // Получить историю ещё за один день
    fun oneMoreDay(context: Context) {
        val images = context.resources.obtainTypedArray(R.array.historyLogos)
        listHistory.add(History(Date().daysAgo(listHistory.size + 1),
                images.getResourceId(inRange(0, images.length() - 1), 0),
                "$tDay℃ ~ $tNight℃"))
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