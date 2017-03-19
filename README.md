# java-csgo-externals
External Linux Java cheats for CS:GO with OpenGL overlay.

## Download:
Create a directory and run this inside:
```
mkdir java-csgo-externals
cd java-csgo-externals
wget -q https://raw.githubusercontent.com/ericek111/java-csgo-externals/master/download.sh
chmod +x download.sh
./download.sh
```

## Dependencies
- [**Java Memory Manipulation**](https://github.com/ericek111/Java-Memory-Manipulation) - Memory library for reading, parsing and writing to the process memory. [Download JAR and dependencies.](https://github.com/ericek111/Java-Memory-Manipulation/releases/tag/2.0)
  - [fastutil](http://fastutil.di.unimi.it/) extends the Java™ Collections Framework by providing type-specific maps, sets, lists and queues. [Download JAR.](http://repo1.maven.org/maven2/it/unimi/dsi/fastutil/7.1.0/fastutil-7.1.0.jar)
  - [Zero-allocation Hashing](https://github.com/OpenHFT/Zero-Allocation-Hashing) for Java. [Download JAR.](http://repo1.maven.org/maven2/net/openhft/zero-allocation-hashing/0.8/zero-allocation-hashing-0.8.jar)
- [Java Native Access (JNA)](https://github.com/java-native-access/jna) as the backbone for interfacing with native libraries. You can download precompiled files from [their GitHub](https://github.com/java-native-access/jna#download) - you need both *jna-4.x.0.jar* and *jna-platform-4.x.0.jar*.
- [JOGL](https://github.com/sgothel/jogl) - Java™ Binding for the OpenGL® API.  
  - Native X11 wrapper needs to be **patched** by jogl_patch.txt. You need to download both [jogl-all.jar](https://github.com/ericek111/java-csgo-externals/releases/download/1.0/jogl-all.jar) and >[jogl-all-natives-linux-amd64.jar](https://github.com/ericek111/java-csgo-externals/releases/download/1.0/jogl-all-natives-linux-amd64.jar)<.
  [Patched precompiled JAR here.](https://github.com/ericek111/java-csgo-externals/releases/download/1.0/jogl-all-natives-linux-amd64.jar)
- [Gson](https://github.com/google/gson) - A Java serialization/deserialization library that can convert Java Objects into JSON and back. [Download JAR.](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar)
