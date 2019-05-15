package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.contract.Type
import org.bonitasoft.engine.bpm.process.impl.ContractDefinitionBuilder

@ProcessDSLMarker
class ContractContainer(val builder: ContractDefinitionBuilder) {

    fun text(name: String): ContractInputContainer = ContractInputContainer(builder, name, Type.TEXT)
    fun boolean(name: String): ContractInputContainer = ContractInputContainer(builder, name, Type.BOOLEAN)
    fun integer(name: String): ContractInputContainer = ContractInputContainer(builder, name, Type.INTEGER)
}

class ContractInputContainer(val builder: ContractDefinitionBuilder, val name: String, val type: Type) {

    fun describedAs(description: String) {
        builder.addInput(name, type, description)
    }

}
