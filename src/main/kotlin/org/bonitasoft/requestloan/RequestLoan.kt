package org.bonitasoft.requestloan

import org.bonitasoft.engine.bpm.bar.BusinessArchive
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder
import org.bonitasoft.engine.bpm.bar.actorMapping.Actor
import org.bonitasoft.engine.bpm.bar.actorMapping.ActorMapping
import org.bonitasoft.engine.bpm.contract.Type
import org.bonitasoft.engine.bpm.flownode.GatewayType
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder
import org.bonitasoft.engine.expression.ExpressionBuilder
import org.bonitasoft.engine.operation.OperationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RequestLoan {

    @Bean
    fun requestLoanProcess(): BusinessArchive {

        val builder = ProcessDefinitionBuilder().createNewInstance("Request Loan", "1.0")

        builder.setActorInitiator("requester")
        builder.addActor("validator")
        builder.addData("type", "java.lang.Integer", null)
        builder.addData("amount", "java.lang.Integer", null)
        builder.addData("accepted", "java.lang.Boolean", null)
        builder.addData("reason", "java.lang.String", null)

        val contractBuilder = builder.addContract()
        contractBuilder.addInput("type", Type.TEXT, "type of the loan")
        contractBuilder.addInput("amount", Type.INTEGER, "amount of the loan")

        val reviewRequest = builder.addUserTask("Review request", "validator")
        val reviewRequestContract = reviewRequest.addContract()
        reviewRequestContract.addInput("accept", Type.BOOLEAN, "whether the loan is accepted or not")
        reviewRequestContract.addInput("reason", Type.TEXT, "why the loan was accepted/rejected")
        reviewRequest.addOperation(OperationBuilder().createSetDataOperation("accepted",
                ExpressionBuilder().createContractInputExpression("accept", "java.lang.Boolean")))
        reviewRequest.addOperation(OperationBuilder().createSetDataOperation("reason",
                ExpressionBuilder().createContractInputExpression("reason", "java.lang.String")))

        builder.addGateway("isAccepted", GatewayType.EXCLUSIVE)

        builder.addUserTask("Sign contract", "requester")

        builder.addAutomaticTask("Notify reject")


        builder.addTransition("Review request", "isAccepted")
        builder.addTransition("isAccepted", "Sign contract", ExpressionBuilder().createDataExpression("accepted", "java.lang.Boolean"))
        builder.addDefaultTransition("isAccepted", "Notify reject")


        val designProcessDefinition = builder.done()

        val businessArchiveBuilder = BusinessArchiveBuilder().createNewBusinessArchive()
        businessArchiveBuilder.setProcessDefinition(designProcessDefinition)
        businessArchiveBuilder.actorMapping = ActorMapping().apply {
            addActor(Actor("requester").apply {
                addUser("john")
            })
            addActor(Actor("validator").apply {
                addUser("jack")
            })
        }

        return businessArchiveBuilder.done()
    }

}