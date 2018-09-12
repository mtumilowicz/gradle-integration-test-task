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
Some useful tasks:
* **compileJava** task that compiles all the Java source files 
under `src/main/java`
* **compileTestJava** task for source files under `src/test/java`
* **test** task that runs the tests from `src/test/java`
* **jar** task that packages the main compiled classes and 
resources from `src/main/resources` into a single JAR 
named `<project>-<version>.jar`

# source sets
## basics
Source files and resources are often logically grouped by type 
(application code, unit tests, integration tests etc...) - 
each logical group typically has its own sets of file dependencies, 
classpaths, and more. Significantly, the files that form a source 
set don’t have to be located in the same directory!

Source sets are the way of defining:
* the source files and where they’re located
* the compilation classpath, including any required dependencies
* where the compiled class files are placed

# convention
Naming:
* *sourceSetName*CompileOnly
* *sourceSetName*Implementation
* compile*sourceSetName*Java

_Remark_: for `main` (by convention) - we use `compileJava`

## additional source sets
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

# how gradle detects tests
For JUnit, Gradle scans for both JUnit 3 and 4 test classes. 
A class is considered to be a JUnit test if it:
* Ultimately inherits from `TestCase` or `GroovyTestCase`
* Is annotated with `@RunWith`
* Contains a method annotated with `@Test` or a super class does

# project description
We provide tasks for running integration tests under assumptions:
1. integration tests should be under `src/integrationTest`

    defining dedicated source set:
    ```
    sourceSets {
        integrationTest {
            compileClasspath += sourceSets.main.output
            runtimeClasspath += sourceSets.main.output
        }
    }
    ``` 
    
1. integration tests task should be in `verification` group
    ```
    task integrationTest(type: Test) {
        ...
        group = 'verification'
        ...
    }
    ```
    
1. integration tests should be excluded from incremental build mechanism
    ```
    task integrationTest(type: Test) {
        ...
        outputs.upToDateWhen { false } // other way: inputs.upToDateWhen { false }
        ...
    }    
    ```    
1. integration tests should have their own classpath & dependency 
configuration
    * `compileClasspath`, `runtimeClasspath` is defined with source set
    * dependencies
        ```
        configurations {
            integrationTestImplementation.extendsFrom implementation
            integrationTestRuntimeOnly.extendsFrom runtimeOnly
        }
        ```
        extendsFrom informs us that all dependencies from 
        `implementation / runtimeOnly` are included for our source set
1. integration tests should run after unit tests
    ```
    task integrationTest(type: Test) {
        ...
        mustRunAfter test
        ...
    }
    ```
1. integration tests should be run before `check` task
    ```
    check.dependsOn integrationTest
    ```
1. integrationTest task should be of type Test
    ```
    task integrationTest(type: Test) {...}
    ```
1. integrationTest task should use classpath and compiled test classes
    based on the content of `integrationTest` directory
    ```
    task integrationTest(type: Test) {
        ...
        testClassesDirs = sourceSets.integrationTest.output.classesDirs
        classpath = sourceSets.integrationTest.runtimeClasspath
        ...
    }
    ```