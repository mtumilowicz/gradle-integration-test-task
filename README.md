# gradle-integration-test-task
The main goal of this project is to provide simple implementation of
integration test task for Gradle.

_Reference_: https://docs.gradle.org/4.10-rc-2/userguide/java_testing.html  
_Reference_: https://docs.gradle.org/4.10-rc-2/userguide/building_java_projects.html

# preface
Please refer to my recent project about tasks in general: 
https://github.com/mtumilowicz/gradle-tasks-example

Assumption: this project is **java** based 
(and all information below are under that assumption):
```
plugins {
    id 'java'
}

sourceCompatibility = '1.8'
targetCompatibility = '1.8'
```

* **sourceCompatibility** - Defines which language version of Java your source files should be treated as.
* **targetCompatibility** - Defines the minimum JVM version your code should run on, i.e. it determines the version of byte code the compiler generates.

# introduction
Gradle uses the same default directory structure for source files 
and resources, and it works with Maven-compatible repositories
* **compileJava** task that compiles all the Java source files 
under `src/main/java`
* **compileTestJava** task for source files under `src/test/java`
* **test** task that runs the tests from `src/test/java`
* **jar** task that packages the main compiled classes and 
resources from `src/main/resources` into a single JAR 
named `<project>-<version>.jar`

# source sets
Source files and resources are often logically grouped by type 
(application code, unit tests, integration tests etc...) - 
each logical group typically has its own sets of file dependencies, 
classpaths, and more. Significantly, the files that form a source 
set don’t have to be located in the same directory!

Source sets are the way of defining:
* the source files and where they’re located
* the compilation classpath, including any required dependencies
* where the compiled class files are placed

Naming:
* *sourceSetName*CompileOnly
* *sourceSetName*Implementation
* compile*sourceSetName*Java

_Remark_: for `main` (by convention) - we use `compileJava`

In addition to the `main` source set, the Java Plugin defines a 
`test` source set (typically use this source set for unit tests) 
that represents the project’s tests. This source set is used 
by the `test` task, which runs the tests. For other tests (integration, 
acceptance, functional) we should define other source set, mainly
for both of the following reasons:
* tests should be separated from one another for 
aesthetics and manageability
* the different test types require different compilation or 
runtime classpaths or some other difference in setup



# dependency configurations (maven scopes)
* compileOnly — for dependencies that are necessary to compile your production code but shouldn’t be part of the runtime classpath
* implementation (supersedes compile) — used for compilation and runtime
* runtimeOnly (supersedes runtime) — only used at runtime, not for compilation
* testCompileOnly — same as compileOnly except it’s for the tests
* testImplementation — test equivalent of implementation
* testRuntimeOnly — test equivalent of runtimeOnly