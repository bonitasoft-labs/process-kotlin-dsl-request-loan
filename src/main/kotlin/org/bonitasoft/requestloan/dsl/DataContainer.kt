package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder

@ProcessDSLMarker
class DataContainer(val builder: ProcessDefinitionBuilder) {

    fun text(name: String) = builder.addData(name, "java.lang.String", null)
    fun integer(name: String) = builder.addData(name, "java.lang.Integer", null)
    fun boolean(name: String) = builder.addData(name, "java.lang.Boolean", null)

}
