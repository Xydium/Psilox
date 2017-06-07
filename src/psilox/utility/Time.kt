package psilox.utility

object Time {

    val NANO = 1L
    val MICRO = NANO * 1000
    val MILLI = MICRO * 1000
    val SECOND = MILLI * 1000
    val MINUTE = SECOND * 60
    val HOUR = MINUTE * 60
    val DAY = HOUR * 24
    val YEAR = DAY * 365

    var now = 0L
        get() = System.nanoTime()
        private set

    private var profile = now

    fun since(time: Long = profile): Long = now - time
    fun sinceF(time: Long = profile, unit: Long = 1): Float = since(time) / unit.toFloat()

    fun profile() {
        profile = now
    }

    infix fun Number.per(x: Long): Long = x / this.toLong()
    infix fun Number.elapsed(x: Long): Boolean = since(this.toLong()) >= x

}