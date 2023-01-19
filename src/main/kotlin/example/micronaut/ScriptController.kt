package example.micronaut

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.serde.annotation.Serdeable

@Controller("/script")
class ScriptController() {

    @Post("/run")
    fun run(@Body scriptInput: ScriptInput): String {
        return ScriptEngineWrapper().executeScript(
            scriptInput.method,
            jacksonObjectMapper().writeValueAsString(scriptInput.script)
        )
    }
}

@Serdeable
class ScriptInput(
    val method: String,
    val script: Any

)