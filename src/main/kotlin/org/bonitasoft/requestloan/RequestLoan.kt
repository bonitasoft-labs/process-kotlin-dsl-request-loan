package org.bonitasoft.requestloan

import bonita.connector.email.email
import org.bonitasoft.engine.dsl.process.*
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.constant
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.contract
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.dataRef
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.groovy
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.parameter
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.stringSubstitution
import org.bonitasoft.engine.expression.ExpressionConstants
import org.bonitasoft.engine.spring.BonitaProcessBuilder
import org.bonitasoft.engine.spring.annotations.BonitaProcess


@BonitaProcess
class RequestLoan : BonitaProcessBuilder {
    override fun build(): Process = process("Request Loan", "1.0") {
        val requester = initiator("requester")
        val validator = actor("validator")
        data {
            text named "type"
            integer named "amount"
            boolean named "accepted"
            text named "reason"
        }
        contract {
            text named "type" withDescription "type of the loan"
            integer named "amount" withDescription "amount of the loan"
        }
        val review = userTask("Review request") {
            actor = validator
            contract {
                boolean named "accept" withDescription "whether the load is accepted or not"
                text named "reason" withDescription "why the loan was accepted/rejected"
            }
            operations {
                update("accepted").with(contract("accept"))
                update("reason").with(contract("reason"))
            }
        }
        val gate = exclusiveGateway("isAccepted")
        val sign = userTask("Sign contract") {
            actor = requester
        }
        val notify = automaticTask("Notify reject") {
            connector {
                email {
                    smtpHost(parameter("smtpHost"))
                    smtpPort(constant(2525))
                    from(constant("no-reply@acme.com"))
                    to(groovy("""
                                |def userId = apiAccessor.getProcessAPI().getProcessInstance(processInstanceId).getStartedBy()
                                |return apiAccessor.getIdentityAPI().getUserWithProfessionalDetails(userId).contactData.email
                            """.trimMargin()) {
                        dependency(ExpressionDSLBuilder().apply { engineConstant(ExpressionConstants.PROCESS_INSTANCE_ID) })
                        dependency(ExpressionDSLBuilder().apply { engineConstant(ExpressionConstants.API_ACCESSOR) })
                    })
                    subject(constant("Your loan was rejected"))
                    message(stringSubstitution("""
                        |
                        | We are sorry to inform you that your Loan was rejected because:
                        | ${'$'}{reason}
                        |
                        | Thank you
                        |
                    """.trimMargin()) {
                        dataRef("reason")
                    })
                    sslSupport(constant(false))
                }
            }
        }
        transitions {
            review to gate
            gate to sign withCondition dataRef("accepted")
            (gate to notify).isDefault()
        }
    }

    override fun configuration(): ProcessConfiguration = configuration {
        actorMapping {
            "requester" to {
                group("requesters")
            }
            "validator" to {
                group("validators")
            }
        }
        parameters {
            "smtpHost" to "localhost"
        }
    }
}