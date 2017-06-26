package psilox.resource

abstract internal class Resource(val glReference: Long) {

    internal var references = 0

    abstract fun destroy()

}