# IntelliJ Platform Plugin Template

## About

Here is a draft implementation of an IntelliJ Platform plugin that configuration options from a dotfile and applies them to the IntelliJ IDEA IDE. As for now only KeymapBindings and EditorFontSize are supported.

## How to use

1. Clone this repository.
2. Copy file [.intellij-idea-settings](.intellij-idea-settings) to your home directory.
3. Open this project in IntelliJ IDEA.
4. Set your Java language level to 17 or higher.
5. Don't forget to select Settings > Advanced Settings > Download Resources option.
6. Run Plugin.
7. Edit .intellij-idea-settings dotfile in your root directory and see how IDE reacts.