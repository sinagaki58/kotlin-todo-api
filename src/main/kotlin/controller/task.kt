package todolist

import com.fasterxml.jackson.databind.ObjectMapper
import spark.Request
import spark.Route
import spark.Spark.halt

class TaskController(
    private val objectMapper: ObjectMapper,
    private val taskRepositry: TaskRepositry
) {
    fun index(): Route = Route { req, res ->
        taskRepositry.findAll()
    }

    fun create(): Route = Route { req, res ->
        val request: TaskCreateRequest =
            objectMapper.readValue(req.bodyAsBytes()) ?: throw halt(400)
        val task = taskRepositry.create(request.content)
        res.status(201)
        task
    }

    fun show(): Route = Route { req, res ->
        req.task ?: throw halt(404)
    }

    fun destroy(): Route = Route { req, res ->
        val task = req.task ?: throw halt(404)
        taskRepositry.delete(task)
        res.status(204)
    }

    fun update(): Route = Route { req, res ->
        val requet: TaskUpdateRequest =
            objectMapper.readValue(req.bodyAsBytes()) ?: throw halt(400)
        val task = req.task ?: throw halt(400)
        val newTask = task.copy(
            content = requet.content ?: task.content,
            done = requet.done ?: task.done
        )

        taskRepositry.update(newTask)
        res.status(204)
    }

    private val Request.task: Task?
        get() {
            val id = params("id").toLongOrNull()
            return id?.let(taskRepositry::findById)
        }

}

