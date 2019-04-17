package org.bonitasoft.requestloan.api

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class ProcessController(val apiClient: APIClient) {

    @GetMapping("/processes")
    fun list(): List<ProcessDeploymentInfo> {
        return apiClient.processAPI.searchProcessDeploymentInfosCanBeStartedBy(apiClient.loggedUserId(), allResults).result
    }

}