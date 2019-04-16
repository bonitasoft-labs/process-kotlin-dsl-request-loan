package org.bonitasoft.requestloan.api

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.bonitasoft.requestloan.api.ContractHelper.convertContract
import org.springframework.web.bind.annotation.*

@RestController
class TaskController(val apiClient: APIClient) {

    @GetMapping("/tasks")
    fun list(): SearchResult<HumanTaskInstance>? {
        return apiClient.processAPI.searchMyAvailableHumanTasks(apiClient.session.userId, SearchOptionsBuilder(0, 100).done())
    }

    @PostMapping("/task/{id}")
    fun execute(@PathVariable id: Long, @RequestBody contract: Map<String, String>) {
        apiClient.processAPI.assignUserTask(id, apiClient.session.userId)
        apiClient.processAPI.executeUserTask(id, convertContract(contract, apiClient.processAPI.getUserTaskContract(id)))
    }
}