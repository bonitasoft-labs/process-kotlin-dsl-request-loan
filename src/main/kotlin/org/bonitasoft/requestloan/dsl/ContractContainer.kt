package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.contract.Type
import org.bonitasoft.engine.bpm.process.impl.ContractDefinitionBuilder

@ProcessDSLMarker
class ContractContainer(val builder: ContractDefinitionBuilder) {

    val text get() = ContractInputDeclaration(builder, Type.TEXT)
    val boolean get() = ContractInputDeclaration(builder, Type.BOOLEAN)
    val integer get() = ContractInputDeclaration(builder, Type.INTEGER)

    fun text(name: String): ContractInputContainer = ContractInputContainer(builder, name, Type.TEXT)
    fun boolean(name: String): ContractInputContainer = ContractInputContainer(builder, name, Type.BOOLEAN)
    fun integer(name: String): ContractInputContainer = ContractInputContainer(builder, name, Type.INTEGER)
}

class ContractInputDeclaration(val builder: ContractDefinitionBuilder, val type: Type) {
    infix fun named(name:String) = ContractInputContainer(builder, name, type)
}



class ContractInputContainer(val builder: ContractDefinitionBuilder, val name: String, val type: Type) {

    infix fun describedAs(description: String) {
        builder.addInput(name, type, description)
    }

}
