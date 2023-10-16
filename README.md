# direnv integration for JetBrains IDEs

![Build](https://github.com/fehnomenal/intellij-direnv/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/15285.svg)](https://plugins.jetbrains.com/plugin/15285)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/15285.svg)](https://plugins.jetbrains.com/plugin/15285)


<!-- Plugin description -->
This plugin provides an action to import environment variables from [direnv](https://github.com/direnv/direnv) into the Java process that is running the IDE.

To automatically load the `.envrc` file in the root of a project when you open it, visit <kbd>Settings</kbd> > <kbd>Tools</kbd> > <kbd>Direnv Settings</kbd> and tick the relevant checkbox.

If you don't choose the automatic loading option, a popup notification will appear whenever a project with a `.envrc` file in the root is opened. You can load the `.envrc` file by clicking on the link in the notification. 

To manually load an `.envrc` file: 
- If you have the main toolbar enabled (<kbd>View</kbd> > <kbd>Appearance</kbd> > <kbd>Main Toolbar</kbd>), a button next to the <kbd>Reload All from Disk</kbd> action will start the process.

![action-icon](https://user-images.githubusercontent.com/9959940/98688979-b6c88700-236b-11eb-8e27-319f23376212.png)

- You can also right-click on any `.envrc` file and click <kbd>Import Direnv</kbd> to load its contents.   

**Note**: This plugin handles only `.envrc` files in the project root automatically, but you can use the right click method to manually import any `.envrc` file that is in the project directory.

**Note**: You need `direnv` in your path, or you can specify the location of your direnv executable in <kbd>Settings</kbd> > <kbd>Tools</kbd> > <kbd>Direnv Settings</kbd>

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "intellij-direnv"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/fehnomenal/intellij-direnv/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


## Building from source

```shell script
git clone https://github.com/fehnomenal/intellij-direnv
cd intellij-direnv
./gradlew buildPlugin
```

The plugin is now in the folder `build/distributions/` and can be installed manually.


##### On NixOS

The gradle plugins downloads JetBrains' JRE and fails to execute it.
Add the following in `build.gradle.kts`:
```kotlin
tasks.withType<org.jetbrains.intellij.tasks.RunIdeBase> {
    projectExecutable.set(org.gradle.internal.jvm.Jvm.current().javaExecutable.absolutePath)
}
```

---

Logo and icon source: https://github.com/direnv/direnv-logo/tree/0949c12bafa532da0b23482a1bb042cf41b654fc

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
