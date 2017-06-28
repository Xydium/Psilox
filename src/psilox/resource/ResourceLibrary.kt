package psilox.resource

import java.util.*

object ResourceLibrary {

    private val resources = ArrayList<Resource>()

    internal fun addReference(res: Resource) {
        if(!res.registered) {
            resources += res
            res.registered = true
        }
        res.references++
    }

    internal fun remReference(res: Resource) {
        res.references--
    }

    fun clearDereferenced() {
        var i: Int = 0
        while(i < resources.size) {
            if(resources[i].references <= 0) {
                resources[i].destroy()
                resources.removeAt(i)
            } else {
                i++
            }
        }
    }

    fun clearAll() {
        for(r in resources) {
            r.destroy()
        }
        resources.clear()
    }

}