package org.bonitasoft.requestloan.api

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.springframework.web.bind.annotation.*


@RestController
class CaseController(val apiClient: APIClient) {

    @GetMapping("/cases")
    fun list(): List<ProcessInstance> {
        return apiClient.processAPI
                .searchOpenProcessInstancesInvolvingUser(apiClient.loggedUserId(), allResults)
                .result
    }

    @PostMapping("/case/start/{name}/{version}")
    fun start(@PathVariable name: String,
              @PathVariable version: String,
              @RequestBody contract: Map<String, String>) {
        apiClient.processAPI.apply {
            val processDefinitionId = getProcessDefinitionId(name, version)
            val processContract = getProcessContract(processDefinitionId)
            startProcessWithInputs(processDefinitionId, contract.typed(processContract))
        }

    }




}