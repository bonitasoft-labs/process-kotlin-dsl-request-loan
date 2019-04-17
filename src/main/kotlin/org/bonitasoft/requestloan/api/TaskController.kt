package org.bonitasoft.requestloan.api

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.requestloan.api.APIHelper.convertContract
import org.springframework.web.bind.annotation.*

@RestController
class TaskController(val apiClient: APIClient) {

    @GetMapping("/tasks")
    fun list(): List<HumanTaskInstance>? {
        return apiClient.processAPI.searchMyAvailableHumanTasks(apiClient.loggedUserId(), allResults).result
    }

    @PostMapping("/task/{id}")
    fun execute(@PathVariable id: Long, @RequestBody contract: Map<String, String>) {
        apiClient.processAPI.apply {
            assignUserTask(id, apiClient.session.userId)
            executeUserTask(id, convertContract(contract, apiClient.processAPI.getUserTaskContract(id)))
        }
    }
}