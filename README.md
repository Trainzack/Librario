# Librario

Librario is a band library management tool designed to minimize your interaction with a photocopier. Librario is still under alpha development, so there are no public releases yet. [Click here for more information.](https://www.elizupke.com/projects/librario)

## How to Compile from Source

### Setup the Project

1. [Clone this repository](https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository-from-github/cloning-a-repository).
2. Install the [Java 15 JDK](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html) (or higher).
3. Install an IDE that has Maven (I use [Eclipse](https://www.eclipse.org/ide/), and the rest of this guide is written assuming that's what you're using).
4. Add the JDK you downloaded to Eclipse
5. Import the project
    * In Eclipse, select `File > Open Projects from File System...`.
    * Click the `Show other specialized import wizards` link near the bottom right.
    * Click on `Maven > Existing Maven Projects` from the tree view.
    * Select the root directory of the repository you downloaded. When done right, `/pom.xml` should show up in the Projects list. Make sure it is checked.
    * Hit the "Finish" button, and wait for the project to be imported.
11. Setup the Maven Goals
    * Running Locally: `clean compile exec:java`
    * Packaging: `clean compile jlink:jlink package` (note: currently not working)


