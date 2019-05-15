package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.process.impl.UserTaskDefinitionBuilder
import org.bonitasoft.engine.expression.ExpressionBuilder

@ProcessDSLMarker
class OperationContainer(val userTaskBuilder: UserTaskDefinitionBuilder) {
    fun update(dataName: String): OperationBuilder = OperationBuilder(userTaskBuilder, dataName)
}

class OperationBuilder(val userTaskBuilder: UserTaskDefinitionBuilder, val dataName: String) {

    fun withBooleanContractValue(contractName: String) = withContractValue(contractName, "java.lang.Boolean")
    fun withStringContractValue(contractName: String) = withContractValue(contractName, "java.lang.String")
    fun withContractValue(contractName: String, type: String) {
        userTaskBuilder.addOperation(
                org.bonitasoft.engine.operation.OperationBuilder()
                        .createSetDataOperation(dataName,
                                ExpressionBuilder().createContractInputExpression(contractName, type)))
    }
}