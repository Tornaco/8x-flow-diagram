package architecture.dsl.inter_process

import architecture.models.Element
import common.DSL
import common.ParentContainer

class service(override val element: Element) : DSL<service>, ParentContainer {
    private val childComponents: MutableList<String> = mutableListOf()
    val processes: MutableList<process> = mutableListOf()

    override val backgroundColor: String?
        get() = element.color

    fun process(name: String, color: String? = null, function: (process.() -> Unit)? = null): process =
        process(Element(name, "rectangle", color), this).apply {
            processes.add(this)
            function?.let { it() }
        }

    override fun invoke(function: service.() -> Unit): service = apply { function() }

    override fun addElement(element: Element) {
        childComponents.add("${element.type} ${element.name} ${element.color ?: this.element.color ?: ""}")
    }

    override fun toString(): String = buildString {
        addElements(childComponents, element)
    }
}