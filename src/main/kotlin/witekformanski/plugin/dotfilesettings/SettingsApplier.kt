package witekformanski.plugin.dotfilesettings

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.colors.EditorColorsManager
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.keymap.KeymapManager
import com.intellij.openapi.actionSystem.KeyboardShortcut

class SettingsApplier {

    private fun isValid(json: String): Boolean {
        return try {
            JSONObject(json)
            true
        } catch (e: JSONException) {
            false
        }
    }

    private fun getJson(inputFilePath: String): JSONObject? {
        val jsonData = File(inputFilePath).readText()
        println("readText")
        return if (isValid(jsonData)) {
            JSONObject(jsonData)
        } else {
            null
        }
    }

    private fun getEditorFontSize(jsonSettings: JSONObject?): Double? {
        println("getEditorFontSize")
        return try {
            val editorFont = jsonSettings?.getJSONObject("Editor")?.getJSONObject("Font")
            editorFont?.getDouble("Size")
        } catch (e: JSONException) {
            println("Invalid key in JSON: $e")
            null
        }
    }

    private fun applyEditorFontSize(editorFontSize: Double?) {
        if (editorFontSize == null) return
        EditorColorsManager.getInstance().globalScheme.editorFontSize = editorFontSize.toInt()
    }

    private fun getKeymapBindings(jsonSettings: JSONObject?): Map<String, String> {
        val keymapBindings = mutableMapOf<String, String>()

        jsonSettings?.let {
            val keymapJSON = it.optJSONObject("KeymapBindings")
            keymapJSON?.let { bindings ->
                bindings.keys().forEach { key ->
                    val value = bindings.optString(key)
                    keymapBindings[key] = value
                }
            }
        }

        return keymapBindings
    }

    private fun applyKeymapBindings(keymapBindings: Map<String, String>) {
        val keymap = KeymapManager.getInstance().activeKeymap

        for (keymapBinding in keymapBindings) {
            val shortcut = KeyboardShortcut.fromString(keymapBinding.value)
            if (shortcut != null) {
                keymap.addShortcut(keymapBinding.key, shortcut)
            }
        }
    }


    fun applyChanges(inputFilePath: String) {
        println("apply")
        val jsonSettings = getJson(inputFilePath) ?: return
        println("getJsonSettings")
        println(getEditorFontSize(jsonSettings)?.toInt())
        ApplicationManager.getApplication().invokeLater {
            applyEditorFontSize(getEditorFontSize(jsonSettings))
            applyKeymapBindings(getKeymapBindings(jsonSettings))

            EditorFactory.getInstance().refreshAllEditors()
        }
    }
}
