package k.s.yarlykov.data.domain

import java.io.Serializable

open class Forecast (
        val imgId: Int,
        internal var temperature: Int,
        internal var wind: Int,
        internal var humidity: Int,
        internal var pressureMm: Int) : Serializable {

    companion object {
        val EMPTY_VAL: Int = -1
    }

    fun mmToMb(mm: Int) = (mm * 1.333f).toInt()
    fun mbToMm(mb: Int) = (mb * 0.75006f).toInt()
    open fun getPressure(isMm: Boolean) = if (isMm) pressureMm else mmToMb(pressureMm)
}