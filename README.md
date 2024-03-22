# Macana

Macana is a testbed for combining Obsidian with SPORT (WIP)

## How to build Macana from source

1. Install a Java Development Kit (JDK),
   if you don't already have one.
2. Point the `JAVA_HOME` environment variable to your JDK installation:
   (In other words, set it to the path of a directory/folder
   containing a "bin" that contains a Java executable.
   That path might look something like
   "C:\Program Files\Eclipse Adoptium\jdk-17.0.3.7-hotspot"
   or "/usr/lib/jvm/java-17-openjdk-amd64/" or
   "/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home" .)
  + using Bash or Zsh: `export JAVA_HOME="` *path to installation* `"`
  + using Fish: `set -g JAVA_HOME "` *path to installation* `"`
  + using Windows Command Prompt: `set JAVA_HOME="` *path to installation* `"`
  + using PowerShell: `$env:JAVA_HOME = '` *path to installation* `'`
3. Download and extract the Macana source code from GitHub:
  + using Git:
    + `git clone https://github.com/stephengold/Macana.git`
    + `cd Macana`
4. Run the Gradle wrapper:
  + using Bash or Fish or PowerShell or Zsh: `./gradlew build`
  + using Windows Command Prompt: `.\gradlew build`

You can run the local build using the Gradle wrapper:
+ using Bash or Fish or Zsh or PowerShell: `./gradlew run`
+ using Windows Command Prompt: `.\gradlew run`

You can restore the project to a pristine state:
+ using Bash or Fish or Zsh or PowerShell: `./gradlew clean`
+ using Windows Command Prompt: `.\gradlew clean`
