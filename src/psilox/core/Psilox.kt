package psilox.core

import psilox.graphics.RED
import psilox.resource.ResourceLibrary
import psilox.utility.Time
import psilox.utility.Time.elapsed
import psilox.utility.Time.now
import psilox.utility.Time.per
import psilox.utility.Time.sinceF

object Psilox {

    lateinit var window: Window
    var running = false

    private var tickInterval = 60 per Time.SECOND
    private var frameInterval = 60 per Time.SECOND

    var dt = 0F; private set
    var ticks = 0L; private set

    fun start(window: Window) {
        if (running) return
        running = true
        this.window = window
        System.setProperty("java.awt.headless", "true")

        loop()
    }

    fun quit() {
        running = false
    }

    private fun update() {
        window.pollEvents()
        //root.updateChildren()
        //Node.applyChanges()
    }

    private fun render() {
        window.clear()
        //window.renderTree(root);
        window.logLastError();
        window.swapBuffers();
    }

    private fun loop() {
        var lastTick = 0L
        var lastFrame = 0L

        window.initialize()

        while(running) {
            if(lastTick elapsed tickInterval) {
                dt = sinceF(lastTick, Time.SECOND)
                lastTick = now
                update()
                ticks++
            }

            if(lastFrame elapsed frameInterval) {
                lastFrame = now
                render()
            }

            ResourceLibrary.clearDereferenced()
            running = running and !window.shouldClose()
        }

        ResourceLibrary.clearAll()
        window.terminate()
    }

}

fun main(args: Array<String>) {
    Psilox.start(Window(clear = RED))
}