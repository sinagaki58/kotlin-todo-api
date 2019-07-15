package todolist

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import spark.Spark.*

fun main() {
    val objectMapper = ObjectMapper().registerKotlinModule()
    val jsonTransformer = JsonTransformer(objectMapper)
    val taskRepositry = TaskRepositry()
    val taskController = TaskController(objectMapper, taskRepositry)

    path("/tasks") {


    get("", taskController.index(), jsonTransformer)
    post("", taskController.create(), jsonTransformer)
    get("/:id", taskController.show(), jsonTransformer)
    patch("/:id", taskController.update(), jsonTransformer)
    delete("/:id", taskController.destroy(), jsonTransformer)
    }
}

