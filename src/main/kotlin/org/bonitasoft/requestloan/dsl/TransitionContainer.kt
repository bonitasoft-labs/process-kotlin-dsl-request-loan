package org.bonitasoft.requestloan.dsl

import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder
import org.bonitasoft.engine.expression.ExpressionBuilder

enum class TransitionType {
    NORMAL, CONDITIONAL, DEFAULT
}

class TransitionContainer(val builder: ProcessDefinitionBuilder) {

    val normal get() = normal()
    val default get() = default()

    fun normal(): TransitionBuilder = TransitionBuilder(builder, TransitionType.NORMAL)

    fun conditional(dataName: String): TransitionBuilder =
            TransitionBuilder(builder, TransitionType.CONDITIONAL, dataName)

    fun default(): TransitionBuilder = TransitionBuilder(builder, TransitionType.DEFAULT)

}

class TransitionBuilder(val builder: ProcessDefinitionBuilder, val type: TransitionType, val dataName: String? = null) {

    var source: String = ""

    infix fun from(source: String): TransitionBuilder {
        this.source = source
        return this
    }

    infix fun to(target: String) {
        when (type) {
            TransitionType.NORMAL ->
                builder.addTransition(source, target)
            TransitionType.CONDITIONAL ->
                builder.addTransition(source, target,ExpressionBuilder().createDataExpression(dataName, "java.lang.Boolean"))
            TransitionType.DEFAULT ->
                builder.addDefaultTransition(source, target)
        }
    }
}
