package example.micronaut

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import javax.script.Bindings
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptEngine

@Suppress("JAVA_CLASS_ON_COMPANION")
class ScriptEngineWrapper {
    companion object {
        private val classLoader = javaClass.classLoader
        private val commonScript = classLoader.getResourceAsStream("scripts/common_script.js")!!.reader().readText()
        private val initialScript =
            classLoader.getResourceAsStream("scripts/initial_script.js")!!.reader().readText()
        var engine: ScriptEngine = GraalJSScriptEngine.create(null,
            Context.newBuilder("js")
                .allowHostAccess(HostAccess.NONE)
                .allowHostClassLookup { false }
                .option("js.ecmascript-version", "2021"))
        private val compiledScript: CompiledScript = (engine as Compilable).compile(
            commonScript +
                    "\n" +
                    initialScript +
                    "\n" +
                    "if (method == 'validate') {" +
                    "   validate(JSON.parse(params)) " +
                    "} " +
                    "else {" +
                    "   navigate(JSON.parse(params)) " +
                    "}"
        )
    }

    fun executeScript(method: String, script: String): String {
        val scriptParams: Bindings = engine.createBindings()
        scriptParams["method"] = method
        scriptParams["params"] = script
        return compiledScript.eval(scriptParams).toString()
    }

}