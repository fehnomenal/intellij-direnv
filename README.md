# direnv integration for JetBrains IDEs

![Build](https://github.com/fehnomenal/intellij-direnv/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/15285.svg)](https://plugins.jetbrains.com/plugin/15285)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/15285.svg)](https://plugins.jetbrains.com/plugin/15285)


<!-- Plugin description -->
This plugin provides an action to import environment variables from [direnv](https://github.com/direnv/direnv) into the Java process that is running the IDE.

A button next to the `Reload All from Disk` action will start the process.

![action-icon](https://user-images.githubusercontent.com/9959940/98688979-b6c88700-236b-11eb-8e27-319f23376212.png)

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "intellij-direnv"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/fehnomenal/intellij-direnv/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---

Logo and icon source: https://github.com/direnv/direnv-logo/tree/0949c12bafa532da0b23482a1bb042cf41b654fc

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
