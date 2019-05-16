package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder

@ProcessDSLMarker
class DataContainer(val builder: ProcessDefinitionBuilder) {

    val text get() = DataDeclaration(builder, "java.lang.String")
    val boolean get() = DataDeclaration(builder, "java.lang.Boolean")
    val integer get() = DataDeclaration(builder, "java.lang.Integer")


    fun text(name: String) = builder.addData(name, "java.lang.String", null)
    fun integer(name: String) = builder.addData(name, "java.lang.Integer", null)
    fun boolean(name: String) = builder.addData(name, "java.lang.Boolean", null)

}

class DataDeclaration(val builder: ProcessDefinitionBuilder, val type: String){

    infix fun named(name: String) = builder.addData(name, type, null)
}
