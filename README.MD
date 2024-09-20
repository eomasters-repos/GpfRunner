GPF Runner
=============================
-----------------------------

[![ESA SNAP](https://img.shields.io/badge/FOR%20ESA%20SNAP-Version%2011-00AA89?labelColor=5A5A5A)](https://step.esa.int/main/)

This little project aims to help you to run processing with ESA SNAP faster.
In some cases the gpt tool does not execute with the best possible performance.
This issue has already been [reported in the STEP Forum](https://forum.step.esa.int/t/odd-processing-performance/43172)
to the development team. Till this can be fixed in SNAP this tool can help you workaround this issue.
Unfortunately, also this tool can't provide a one-for-all solution. The performance of a process depends in some cases
on the write function used. And in some cases this tool is slower than gpt. But it can help you to evaluate the best
option.
In the shown examples below, GPF Runner is in one case 2.2x faster and in another one it is 5.7x faster.
In the case of S1 it is 0.7x slower.<br>
After finding the best configuration for your process you can do the mass production and save a lot of time.

Examples:

| Process                      | GPT                         | GPF Runner + GPF-Write      | GPF Runner + PIO-Write      |
|------------------------------|-----------------------------|-----------------------------|-----------------------------|
| Resampling S2 product to 20m | 00:05:58                    | 00:07:59                    | $${\color{green}00:02:43}$$ | 
| C2RCC on S3 OLCI data        | 00:40:38                    | $${\color{green}00:07:05}$$ | 01:47:10                    |               
| S1 Terrain-Correction        | $${\color{green}00:03:03}$$ | 00:04:20                    | 00:04:18                    |               

This tool is not as generic as the gpt tool, and you have to implement your processing chain in Java, but it can serve
as a template for your further development. It is not very complicated when you have this project as your starting
point.

## Requirements

* **Java** - To use maven and to run the tool, you need to have Java installed. As SNAP is currently based on Java 11
  this should be [downloaded and installed](https://bell-sw.com/pages/downloads/#jdk-11-lts).
* **maven** - To build this tool you need to have maven installed. Get it from
  the [install page](https://maven.apache.org/download.cgi) and follow
  these [installation instructions](https://maven.apache.org/install.html).
* **git** - It is also advisable to [download](https://git-scm.com/downloads) and install git to obtain the source code
  of this repository. But you can also download
  a [zip file](https://github.com/eomasters-repos/GpfRunner/archive/refs/heads/master.zip).

## Usage

After you have obtained the source code you can run the tool by invoking it from the command line using maven. Navigate
into the base directory of the project where the `pom.xml` files is located. And then call:
`mvn package exec:exec`
This will do two things:

1. Compile and build the source code. This includes downloading and placing all necessary dependencies into the
   `target/dependencies` directory
2. Executes the built result.

This will not do anything useful as long you haven't changed the code. But at least you will see a "**** Starting
Processing" message followed by an error message.

In a production environment you would use the built output `gpf-runner-<version>.jar` and the `dependency` directory to
set up the execution environment and specify the parameters for each run on the commandline, e.g., the path to the
product to process.

## Configuration

The execution can be configured within the maven pom file. In the configuration section of the `exec-maven-plugin`
arguments can be added. By default, the memory allowed to use is set to 15G. Also, the classpath is set and the main
class which shall be executed. Here further parameters can be set.

```xml

<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>exec-maven-plugin</artifactId>
  <executions>
    <execution>
      <id>exec-runner</id>
      <goals>
        <goal>exec</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <executable>${java.executable}</executable>
    <arguments>
      <argument>-Xmx15G</argument>
      <argument>-cp</argument>
      <argument>
        ${project.build.directory}\${project.build.finalName}.jar;${project.build.directory}\dependencies\*
      </argument>
      <argument>org.eomasters.GpfRunner</argument>
    </arguments>
  </configuration>
</plugin>
```

In the properties section you can specify the SNAP version, and you can specify the path to the java executable which
shall be used.
The properties `maven.compiler.source` and `maven.compiler.target` shall match the used java version and this should be
the same which is required by SNAP.

```xml

<properties>
  <snap.version>11.0.0</snap.version>
  <!-- modify the path to the java executable here -->
  <java.executable>java</java.executable>

  <maven.compiler.source>11</maven.compiler.source>
  <maven.compiler.target>11</maven.compiler.target>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

## Implementation

The default `GpfRunner` class implements a generic way and shows an example usage.
The implementation does the necessary initialisation of SNAP and GPF, and it prints some useful messages at the
beginning and the end of the processing. You need to specify the input and output path of your data before using it.
To implement your processing chain you need to call the method `GPF.createProduct()` and pass the result to the next
call of `GPF.createProduct()` and finally write the result.<br>
As mentioned earlier the writing is important for the performance you can easily switch between the two modes. The
`WriteTools.writeResult()` method takes as last parameter the write function which shall be used. This lets you quickly
test the best function for your process.<br>
There are also variations of this GpfRunner implementation for the three examples given at the beginning of this readme
file. Exchange the paths with paths to your file system and see how the performance is on your system.

## Support

You can support me by following me on social-media<br>
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/company/eomasters)
[![X Follow](https://img.shields.io/badge/Twitter-000000?style=for-the-badge&logo=x&logoColor=white)](https://twitter.com/eomasters)
[![Mastodon Follow](https://img.shields.io/badge/Mastodon-6364FF?style=for-the-badge&logo=Mastodon&logoColor=white)](https://mastodon.green/@EOMasters)

or buying me a coffee at ko-fi<br>
[![ko-fi](https://img.shields.io/badge/Ko--fi-F16061?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/eomasters)

Maybe you might also like other tools which enhance SNAP and ease the work with it. In this case you should check out
the [EOMasters Toolbox Pro](https://www.eomasters.org/eomtbx-pro).

