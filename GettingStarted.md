## Introduction ##

Horkizein source code is wrapped in a Maven project. The quickest way to start working on the project is to use the Eclipse IDE and some of its plugins.
You can obviously compile and execute Horkizein using Maven from the command line, but you have to know what you are doing.

## Prerequisites ##

In order to compile the Horkizein library you need the following software:

  * **JDK** 1.5+
  * **Eclipse** Helios or later
  * **Android** **SDK** (`r07` or later), with API level 7 (2.1 Platform), see http://developer.android.com/sdk/installing.html
  * **An** **Eclipse** **Maven** **Plugin** ([comparison](http://docs.codehaus.org/display/MAVENUSER/Eclipse+Integration)):
    * Eclipse m2e (or m2eclipse), stable ergo my choice, see http://www.eclipse.org/m2e
    * Eclipse IAM/q4e, see the project page at http://code.google.com/p/q4e
  * (optional) Maven 2.2.1+ installed (Maven 3.0+ recommended), see http://maven.apache.org/download.html


## Environment ##

First of all, it's mandatory to set some environment variables in your operative system, so that Eclipse can use the Android tools.<br>
You have to include the Android <code>tools</code> folder in your <code>PATH</code> environment variable as well as set the <code>ANDROID_HOME</code> variable.<br><br>
If you are on Linux/Unix-based operative systems (MacOS included), add those few lines to your <code>.bashrc</code> or create a simple launch script for Eclipse:<br>
<table><thead><th> <code>export</code> <code>PATH=$PATH:/path-to-folder/android-sdk-linux_86/tools</code> </th></thead><tbody>
<tr><td> <code>export</code> <code>ANDROID_HOME=/path-to-folder/android-sdk-linux_86</code> </td></tr></tbody></table>

If you develop on Windows, add those instead:<br>
<table><thead><th> <code>set</code> <code>PATH=%PATH%;path-to-folder\android-sdk\tools</code> </th></thead><tbody>
<tr><td> <code>set</code> <code>ANDROID_HOME=path-to-folder\android-sdk</code> </td></tr></tbody></table>

Besides, if you want to compile from the command line you need to include the Maven binaries folder to your <code>PATH</code> as well.<br><br>
Linux:<br>
<table><thead><th> <code>export</code> <code>PATH=$PATH:/path-to-folder/apache-maven\bin</code> </th></thead><tbody></tbody></table>

Windows:<br>
<table><thead><th> <code>set</code> <code>PATH=%PATH%;path-to-folder\apache-maven\bin</code> </th></thead><tbody></tbody></table>

<h2>Import</h2>

Before contributing some code, you first have to clone the git repository on your local machine. There is plenty of articles about that and you can even find out how to clone under this project's Source section.<br>
After cloning, there are two ways to import files into Eclise: you can either materialize the project from a remote git repository or directly import it from your brand new cloned folder. The first way is easier to accomplish and requires the project to have an <a href='http://maven.apache.org/pom.html#SCM'>SCM</a> tag in the pom.xml. There is a very useful tutorial <a href='https://docs.sonatype.org/display/M2ECLIPSE/Importing+Maven+projects'>here</a> so I will stick to the latter.<br><br>
Go to <i>File</i> <i>-></i> <i>Import</i> and when the Import Wizard comes out select <i>Existing Maven Projects</i>. Use the git clone folder as <i>Root</i> <i>Directory</i> and wait. After processing, the window will display three pom.xml files: the root one is for the parent module, <code>\horkizein\pom.xml</code> is for the library, <code>\horkizein-it\pom.xml</code> is for the test suite. Be sure to tick <i>Resolve</i> <i>Workspace</i> <i>Projects</i> in order to resolve dependencies without using mvn install every time. I have also run up against a little problem while importing Horkizein: multi-module Maven projects are not always mirrored correctly by the M2E Eclipse plugin. To get around this, you have to specify a <i>Name</i> <i>Template</i>, <code>[</code>artifactId<code>]</code>-<code>[</code>version<code>]</code> works for me.<br><br>
<img src='http://wiki.horkizein.googlecode.com/git/img/import_step0.png' />
<br><br>
Press <i>Next</i> and a new window will show the missing dependencies. You can safely resolve the dependencies later, Maven will handle them. Select <i>Resolve</i> <i>Later</i> on each artefact and then the <i>Finish</i> button.<br><br>
On completion, you should be able to see the following structure in your <i>Navigator</i>: <br><br>
<img src='http://wiki.horkizein.googlecode.com/git/img/import_complete.png' />
<br><br>