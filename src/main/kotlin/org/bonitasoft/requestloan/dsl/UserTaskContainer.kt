package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.process.impl.UserTaskDefinitionBuilder


@ProcessDSLMarker
class UserTaskContainer(val userTaskBuilder: UserTaskDefinitionBuilder) : FlowNodeContainer(userTaskBuilder) {


    fun contract(init: ContractContainer.() -> Unit) = ContractContainer(userTaskBuilder.addContract()).init()

    fun operations(init: OperationContainer.() -> Unit) = OperationContainer(userTaskBuilder).init()
}
