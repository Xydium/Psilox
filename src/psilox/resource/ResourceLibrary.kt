package psilox.resource

import java.util.*

object ResourceLibrary {

    private val resources = ArrayList<Resource>()
    private val dereferenced = ArrayList<Resource>()

    private fun addReference(res: Resource) {
        res.references++
    }

    private fun remReference(res: Resource) {
        res.references--
        if(res.references == 0) {
            dereferenced += res
        }
    }

    fun clearDereferenced() {
        for(r in dereferenced) {
            resources -= r
            r.destroy()
        }
        dereferenced.clear()
    }

    fun clearAll() {
        for(r in resources) {
            r.destroy()
        }
        dereferenced.clear()
        resources.clear()
    }

}