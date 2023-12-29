package witekformanski.plugin.dotfilesettings

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds

class SettingsFileWatcher {

    private val settingsApplier = SettingsApplier()

    fun watchSettingsFile(inputFilePath : String)  {
        val filePath = Paths.get(inputFilePath)

        val watcher = FileSystems.getDefault().newWatchService()

        val directory = filePath.parent
        directory.register(
            watcher,
            StandardWatchEventKinds.ENTRY_MODIFY
        )

        println("Watching file changes for: $filePath")

        while (true) {
            val key = watcher.take()
            for (event in key.pollEvents()) {
                val kind = event.kind()
                val eventPath = event.context() as Path

                if (eventPath == filePath.fileName && kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    println("The settings have been modified.")
                    settingsApplier.applyChanges(inputFilePath)
                }
            }
            key.reset()
        }
    }
}
