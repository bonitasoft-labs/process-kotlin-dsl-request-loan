package org.bonitasoft.requestloan.api

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.bpm.contract.ContractDefinition
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.requestloan.api.APIHelper.convertContract
import java.io.Serializable
import java.lang.Boolean


fun Map<String, String>.typed(contractDefinition: ContractDefinition): Map<String, Serializable> {
    return convertContract(this, contractDefinition)
}

fun APIClient.loggedUserId(): Long = this.session.userId

val allResults: SearchOptions
    get() = SearchOptionsBuilder(0, 100).done()

object APIHelper {

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