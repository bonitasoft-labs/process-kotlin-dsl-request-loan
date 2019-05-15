package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.flownode.GatewayType
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder


@DslMarker
annotation class ProcessDSLMarker

fun process(name: String, version: String, init: BonitaProcess.() -> Unit): DesignProcessDefinition =
        BonitaProcess(name, version).apply(init).build()

@ProcessDSLMarker
class BonitaProcess(val name: String, val version: String) {

    val builder = ProcessDefinitionBuilder().createNewInstance(name, version)

    fun build(): DesignProcessDefinition = builder.done()

    fun initiator(name: String): ActorRef {
        builder.addActor(name, true)
        return ActorRef(name)
    }

    fun actor(name: String): ActorRef {
        builder.addActor(name)
        return ActorRef(name)
    }

    fun data(init: DataContainer.() -> Unit) = DataContainer(builder).init()
    fun contract(init: ContractContainer.() -> Unit) = ContractContainer(builder.addContract()).init()
    fun userTask(name: String, actor: ActorRef, init: UserTaskContainer.() -> Unit = {}) =
            UserTaskContainer(builder.addUserTask(name, actor.name)).init()
    fun exclusiveGateway(name: String, init: FlowNodeContainer.() -> Unit = {}) =
            FlowNodeContainer(builder.addGateway(name, GatewayType.EXCLUSIVE)).init()
    fun automaticTask(name: String, init: FlowNodeContainer.() -> Unit = {}) =
            FlowNodeContainer(builder.addAutomaticTask(name)).init()
    fun transitions(init: TransitionContainer.() -> Unit) = TransitionContainer(builder).init()



}

data class ActorRef(val name: String)