package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.process.DesignProcessDefinition
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder



@DslMarker
annotation class ProcessDSLMarker

fun process(name: String, version: String, init: BonitaProcess.() -> Unit): DesignProcessDefinition {
    val process = BonitaProcess(name, version)
    process.init()
    return process.build()
}

@ProcessDSLMarker
class BonitaProcess(val name: String, val version: String) {

    val builder = ProcessDefinitionBuilder().createNewInstance(name, version)

    fun build(): DesignProcessDefinition {
        return builder.done()
    }

    fun initiator(name: String) {
        builder.addActor(name, true)
    }

    fun actor(name: String) {
        builder.addActor(name)
    }


    fun data(init: DataContainer.() -> Unit) {
        DataContainer(builder).init()
    }


}