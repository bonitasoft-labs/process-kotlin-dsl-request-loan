package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.bar.BusinessArchive
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder
import org.bonitasoft.engine.bpm.bar.actorMapping.ActorMapping
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition

fun businessArchive(init: BonitaProcessAndConfiguration.() -> Unit): BusinessArchive =
        BonitaProcessAndConfiguration(BusinessArchiveBuilder().createNewBusinessArchive()).apply(init).build()

@ProcessDSLMarker
class BonitaProcessAndConfiguration(private val businessArchiveBuilder: BusinessArchiveBuilder) {
    var process: DesignProcessDefinition? = null
        set(value) {
            businessArchiveBuilder.setProcessDefinition(value)
        }

    fun build(): BusinessArchive = businessArchiveBuilder.done()

    fun actorMapping(init: ActorMappingContainer.() -> Unit) {
        val actorMapping = ActorMapping()
        businessArchiveBuilder.actorMapping = actorMapping
        ActorMappingContainer(actorMapping).init()
    }

}