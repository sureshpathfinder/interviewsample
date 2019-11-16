These instructions will help you get started with developing a library for Processing Android using Gradle and Android Studio. The steps walk you through three main tasks:

Configuring the build properties so that your library can be built and packaged by Gradle.
Finding the library files produced by Gradle for use in Processing and redistribution.
Setting up the debug project in Android Studio, which allows you to develop and debug your library using Android Studio as the programming environment.
NOTE: This template should be used as the starting point for a new library that uses Android-specific functions in the Android mode, or API from the Android platform. If your library only uses the regular Processing API and does not depend on any desktop-specific functionality or packages, you could use the regular Processing library template.

Prerequisites
The following components must be installed in order to go through the Usage Instructions.

Gradle Build Tool.
Latest release of the Java JDK version 8.
Git client.
Android Studio version 3.1 or later.
Android SDK with the API level 29.
Processing 3.1 or later.
Android mode for Processing, installed into the PDE using the Contributions Manager.
Set Up and Compile
Download and install Gradle following these instructions.
Fork the template repository to use as a starting point.
Navigate to https://sureshlti@bitbucket.org/sureshlti/testsample.git in your browser.
Click the "Fork" button in the top-right of the page.
Once your fork is ready, open the new repository's "Settings" by clicking the link in the menu bar on the right.
Change the repository name to the name of your library and save your changes.
NOTE: GitHub only allows you to fork a project once. If you need to create multiple forks, you can follow these instructions.
Checkout your fork of the template repository using your preferred git client.
You can test if the template project can be built with Gradle by open a command line terminal, changing the directory to the repository location, and calling gradle dist. This should generarate a distribution folder with several files in it, including YourLibrary.zip. If this is the case, then you are ready to start working with the template to create your own library.
Edit the resources/build.properties file to ensure the following fields are set correctly.
sketchbook.location is a quasi-optional field that should be set to the path of your Processing sketchbook folder. This field tells the Gradle build to deploy the library to the sketchbook/libraries path defined here (in addition to building a .zip distribution).
android_sdk.location should contain the path to the Android SDK in your computer. If you have used the Android mode before and let it to automatically download the SDK for you, then it will be inside the sketchbook/android/sdk folder, but here you can set any other location.
All of the fields in sections (4) and on are for metadata about your library.
Also, make sure to edit the library medatata in resources/library.properties file.
After having compiled and built your project successfully, you should be able to find your library in Processing's sketchbook folder, examples will be listed in Processing's sketchbook menu. The Gradle build process also generates a zip package of your library for distribution.

Import to Android Studio
Open Android Studio and select the "Import project" menu item in the welcome screen.
Navigate to the location where you checked out the repository in your computer, and select the debug folder.
Android Studio should be able to import the debug project automatically, which includes the library itself and a minimal test app.