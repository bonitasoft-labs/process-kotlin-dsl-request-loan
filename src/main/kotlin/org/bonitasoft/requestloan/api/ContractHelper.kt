package org.bonitasoft.requestloan.api

import org.bonitasoft.engine.bpm.contract.ContractDefinition
import java.io.Serializable
import java.lang.Boolean

object ContractHelper {

    fun convertContract(contract: Map<String, String>, contractDefinition: ContractDefinition): Map<String, Serializable> {
        val inputsTypes= contractDefinition.inputs.map { input -> input.name to input.type.name }.toMap()
        val pair = contract.map { (k, v) ->
            val requestedType = inputsTypes[k] ?: "java,lang.String"
            val newValue: Serializable = when (requestedType) {
                "BOOLEAN" -> Boolean.valueOf(v)
                "INTEGER" -> Integer.valueOf(v)
                else -> v
            }
            k to newValue
        }

        val map = pair.toMap()
        return map
    }
}