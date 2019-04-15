package org.bonitasoft.requestloan

import org.bonitasoft.engine.dsl.process.DataType.Companion.boolean
import org.bonitasoft.engine.dsl.process.DataType.Companion.integer
import org.bonitasoft.engine.dsl.process.DataType.Companion.string
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.contract
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.dataRef
import org.bonitasoft.engine.dsl.process.Process
import org.bonitasoft.engine.dsl.process.process
import org.bonitasoft.engine.spring.BonitaProcessBuilder
import org.bonitasoft.engine.spring.annotations.BonitaProcess


@BonitaProcess
class RequestLoan : BonitaProcessBuilder {
    override fun build(): Process {
        return process("Request Loan", "1.0") {
            val requester = initiator("requester")
            val validator = actor("validator")

            data {
                type = string()
                name = "type"
            }
            data {
                type = integer()
                name = "amount"
            }
            data {
                type = boolean()
                name = "accepted"
            }
            contract {
                text named "type" withDescription "type of the loan"
                integer named "amount" withDescription "amount of the loan"
            }

            val start = start("Start request")
            val review = userTask("Review request") {
                actor = validator
                contract {
                    boolean named "accept" withDescription "whether the load is accepted or not"
                }
                operations {
                    update("accepted").with(contract("accept"))
                }
            }
            val gate = exclusiveGateway("isAccepted")
            val sign = userTask("Sign contract"){
                actor = requester
            }
            val notify = automaticTask("Notify reject"){
            }
            transitions {
                start to review
                review to gate
                gate to sign withCondition dataRef("accepted")
                default from gate to notify
            }

        }
    }


}