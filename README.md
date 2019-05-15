# ICogniCrypt

ICogniCrypt is an IntelliJ IDEA plugin that supports Java developers in using cryptographic APIs. It supports developers in two ways. First, it may generate code snippets for a number of programming tasks that involve cryptography, e.g., communication over a secure channel, data encryption, and long-term archiving. Second, it continuously runs a suite of static analyses in the background that check the developer's code for misuses of cryptographic APIs.

## Setting Up ICogniCrpyt
Import the project using either of the following methods:
##### Cloning Project from the Repository
1) Select the *File>Project from Version Control>Git* option, enter repository’s URL and then select *Clone* to import the project.
2) Go to *File>Project Structure* to edit the project settings. 
    3) For the project's SDK, select the *IntelliJ IDEA IU-** option.
    4) Select *Modules* from the left panel and use the *Add* button to add a new project module. In the window that appears, select *IntelliJ Platform Plugin* from the left panel and select *OK*. Select a name for the module and ensure that the *Content Root* and *Module File Location* point to the project's root folder and select *Finish*. If a default module was generated while importing the project, you can remove it.
    5) Select Libraries from the left panel, select the *Add* button and select Java. Select the ``/libs`` folder in the window that appears and select *Open*.

##### Downloading and Importing Project
1) Download the project from Github and the use the *File>Project from Existing Resources* from the menu to import the project. Select the downloaded project's root folder and select *Open*.
2) Select the option to *Create Project from existing sources* and then proceed.
3) At the step to select the project's source files, deselect the ``test-project/src`` entry, if it was automatically selected. The project libraries will be automatically detected and a module will also be created.
4) Validate that the project was imported correctly and the module was created correctly. If there are issues, follow the steps in step 2 in the above section.
 
## Running ICogniCrpyt
To run the plugin, select the *Run Configuration* drop down menu and select *Edit Configurations*. Ensure that the module that was created previously is selected and press Ok. You should now be able to run the project.

A separate instance of IntelliJ will be launched. Use the open option to select the project found in ``/test-project`` directory. You may need to setup a project SDK for the project if one isn’t automatically configured. You should then be able to run the test project.

Logs for the plugin will appear in the initial instance of IntelliJ.


## Building ICogniCrpyt
To build the plugin, select the  "Prepare Plugin Module '...' For Deployment" option from the Build menu. This will generate the a zip file that contains the plugin's jars and resources in the project's root directory.

## Installing ICogniCrpyt Plugin
To install the plugin, go Preferences and select "Plugins" from the sidebar. Select the "Install Plugin from disk" button, locate the plugin file and select it. You will need to restart IntelliJ for the plugin to work. 