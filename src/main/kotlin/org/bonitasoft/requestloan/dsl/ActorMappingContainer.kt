package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.bar.actorMapping.Actor
import org.bonitasoft.engine.bpm.bar.actorMapping.ActorMapping

class ActorMappingContainer(private val actorMapping: ActorMapping) {

    infix fun String.mappedToUser(userName: String) = actor(this).mappedToUser(userName)

    fun actor(name: String): ActorMappingBuilder = ActorMappingBuilder(actorMapping, name)
}

class ActorMappingBuilder(var actorMapping: ActorMapping, var name: String) {
    infix fun mappedToUser(userName: String) = actorMapping.addActor(Actor(name).apply { addUser(userName) })
}
