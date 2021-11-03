package architecture

import architecture.dsl.inter_process.service
import common.DSL
import common.Diagram
import common.Diagram.Color.TRANSPARENT
import common.Element

object diagram_inter_process : DSL<diagram_inter_process>, Diagram {
    var services: MutableList<service> = mutableListOf()

    fun service(name: String, color: String = TRANSPARENT, function: service.() -> Unit): service =
        service(Element(name, "rectangle", color)).apply {
            services.add(this)
            function()
        }

    override fun invoke(function: diagram_inter_process.() -> Unit): diagram_inter_process = apply { function() }

    override fun buildPlantUmlString(): String = """
        |@startuml
        |skinparam rectangleFontColor black
        ${buildPlantUmlContent()}
        |@enduml
        """.trimMargin()

    override fun exportResult(isSuccess: Boolean) {
        if (isSuccess) services.clear()
    }

    private fun buildPlantUmlContent(): String = buildString {
        services.forEach { appendLine(it.toString()) }
        services.forEach { appendLine(it.element.generateRelationships()) }
    }
}