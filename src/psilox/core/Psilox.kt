package psilox.core

import psilox.utility.Time
import psilox.utility.Time.now
import psilox.utility.Time.per
import psilox.utility.Time.elapsed
import psilox.utility.Time.sinceF

object Psilox {

    var running = false

    private var tickInterval = 60 per Time.SECOND
    private var frameInterval = 60 per Time.SECOND

    var dt = 0F; private set
    var ticks = 0L; private set

    fun start() {
        if (running) return
        running = true
        System.setProperty("java.awt.headless", "true");

        loop()
    }

    fun quit() {
        running = false
    }

    private fun update() {
        if(ticks == 10L) quit()
        //window.pollEvents()
        //root.updateChildren()
        //Node.applyChanges()
    }

    private fun render() {
        //window.renderTree(root);
        //window.logLastError();
        //window.swapBuffers();
    }

    private fun loop() {
        var lastTick = 0L
        var lastFrame = 0L

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
        }
    }

}