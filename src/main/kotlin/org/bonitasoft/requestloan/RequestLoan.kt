package org.bonitasoft.requestloan

import bonita.connector.email.email
import org.bonitasoft.engine.dsl.process.DataType.Companion.boolean
import org.bonitasoft.engine.dsl.process.DataType.Companion.integer
import org.bonitasoft.engine.dsl.process.DataType.Companion.string
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.constant
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.contract
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.dataRef
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.groovy
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.parameter
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.startedBy
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.stringSubstitution
import org.bonitasoft.engine.dsl.process.Process
import org.bonitasoft.engine.dsl.process.ProcessConfiguration
import org.bonitasoft.engine.dsl.process.configuration
import org.bonitasoft.engine.dsl.process.process
import org.bonitasoft.engine.spring.BonitaProcessBuilder
import org.bonitasoft.engine.spring.annotations.BonitaProcess


@BonitaProcess
class RequestLoan : BonitaProcessBuilder {
    override fun build(): Process = process("Request Loan", "1.0") {
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
                boolean named "reason" withDescription "whether the load is accepted or not"
            }
            operations {
                update("accepted").with(contract("accept"))
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
                    smtpPort(parameter("smtpPrt"))
                    from(constant("no-reply@acme.com"))
                    to(groovy("startedBy.contactData.email") {
                        startedBy
                    })
                    subject(constant("Your loan was rejected"))
                    message(stringSubstitution("""
                        |
                        | We are sorry to inform you that your Loan was rejected because:
                        | $\{reason}
                        |
                        | Thank you
                        |
                    """.trimMargin()) {
                        dataRef("reason")
                    })
                }
            }
        }
        transitions {
            start to review
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
            "smtpPort" to "2525"
        }
    }
}