package witekformanski.plugin.dotfilesettings

import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.wm.IdeFrame

internal class OnActivationListener : ApplicationActivationListener {

    override fun applicationActivated(ideFrame: IdeFrame) {
        thisLogger().info("Application activated.")
        val myWatcher = SettingsFileWatcher()
        Thread {
            myWatcher.watchSettingsFile(System.getProperty("user.home") + "\\.intellij-idea-settings")
        }.start()
    }
}
