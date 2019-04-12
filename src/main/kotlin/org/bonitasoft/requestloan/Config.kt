package org.bonitasoft.requestloan

import com.bonitasoft.engine.dsl.process.DataType.Companion.boolean
import com.bonitasoft.engine.dsl.process.DataType.Companion.integer
import com.bonitasoft.engine.dsl.process.DataType.Companion.string
import com.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.contract
import com.bonitasoft.engine.dsl.process.process
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {

    @Bean
    fun requestLoanProcess() {
        process("Request Loan", "1.0") {
            val requestor = initiator("requestor")
            val validator = actor("requestor")

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
                actor = requestor
            }
            val notify = automaticTask("Notify reject"){

            }
            transitions {
                start to review
                review to gate
                (gate to sign).condition { dataRef("accepted") }
                (gate to notify).isDefault()
            }

        }
    }
}