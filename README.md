ShieldBreaker
=============
This is a POC of a Brute Force attack framework.

**USE ONLY FOR GRANTED TESTS !**

Current project version: Beta_1.0.

How it works
------------
ShieldBreaker provides a set of abstract classes and interfaces to easily build a multi-threaded Brute Force attack bot.

How to build and add a plugin
---------------------
This process is currently evolving.

Firstly, build the sources. Add the ShieldBreaker's `.jar` as a library in your plugin project.

Then, create:
 - a class extending `com.shieldbreaker.bot.Bot`. It will be the Bot launched by the plugin's bot manager ;
 - a class extending `com.shieldbreaker.bot.BotManager`. It will be your plugin's bot manager ;
 - a class extending `com.shieldbreaker.cli.BaseParametersManager`. It will tell the ShieldBreaker what options the plugin needs.
 - a class implementing `com.shieldbreaker.kernel.BasePlugin` and its methods according to the plugin's previous classes.
 
Build your plugin's `.jar`, and add it in ShieldBreaker plugins' directory (specified in `config.yaml`).

Changelogs
----------

### Alpha
There were no changelog file for this version. The followings are mains changes in the project (historically ordered).
 - Built bash script to Brute Force a Symfony's login webpage using a passlist.

### Beta
There were no changelog file for this version. The followings are mains changes in the project (historically ordered).
 - Translated the Alpha_1.0 to Java.
 - Built the multi-threaded bot manager for the Symfony's Bot.
 - Built the CLI.
 - Built a basic UI to start the Bot.
 - Built abstract Bot and BotManager classes to simplify next Bot builds.
 - Built an abstract BaseParametersManager class to permit the Bot to ask for CLI parameters out of the kernel.
 - Separated the kernel and the Symfony's Bot (first milestone to the plugin evolution).
 - Rebuilt the UI according to previous milestone.
 - Built the plugin interface.
 - Updated the Symfony's Bot to a plugin.
 - Built the PluginLoader.
 - Removed the Symfony's plugin.
 - Enhanced the UI with a terminal, dialog and errors popups.
 
TODO
---- 
TODO for the current version are in the `Main` class.

Gamma version will permit the ShieldBreaker to work in a distributed system, and will be generalisable (not restrained to Brute Force and IDOR attacks).

Known plugins
-------------

 - [SymfonyPlugin][1]
 
 [1]: https://github.com/PandatiX/ShieldBreaker-SymfonyPlugin