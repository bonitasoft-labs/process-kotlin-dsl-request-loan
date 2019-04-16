package org.bonitasoft.requestloan.api

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.requestloan.api.ContractHelper.convertContract
import org.springframework.web.bind.annotation.*


@RestController
class CaseController(val apiClient: APIClient) {

    @GetMapping("/cases")
    fun list(): List<ProcessInstance> {
        return apiClient.processAPI.searchOpenProcessInstancesInvolvingUser(apiClient.session.userId, SearchOptionsBuilder(0, 100).done()).result
    }

    @PostMapping("/case/start/{name}/{version}")
    fun start(@PathVariable name: String,
              @PathVariable version: String,
              @RequestBody contract: Map<String, String>) {
        val processDefinitionId = apiClient.processAPI.getProcessDefinitionId(name, version)
        apiClient.processAPI.startProcessWithInputs(processDefinitionId, convertContract(contract, apiClient.processAPI.getProcessContract(processDefinitionId)))
    }




}