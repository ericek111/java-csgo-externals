# java-csgo-externals
External Linux Java cheats for CS:GO with OpenGL overlay.

## Dependencies:
- [Java Memory Manipulation](https://github.com/ericek111/Java-Memory-Manipulation) - Memory library for reading, parsing and writing to the process memory. Make sure that [*libnative_mem.so*](native/libnative_mem.so) is in **java.library.path**.
  - [fastutil](http://fastutil.di.unimi.it/) extends the Java™ Collections Framework by providing type-specific maps, sets, lists and queues. [Download JAR.](http://repo1.maven.org/maven2/it/unimi/dsi/fastutil/7.1.0/fastutil-7.1.0.jar)
  - [Zero-allocation Hashing](https://github.com/OpenHFT/Zero-Allocation-Hashing) for Java - [download JAR.](http://repo1.maven.org/maven2/net/openhft/zero-allocation-hashing/0.8/zero-allocation-hashing-0.8.jar)
- [Java Native Access (JNA)](https://github.com/java-native-access/jna) as the backbone for interfacing with native libraries. You can download precompiled files from [their GitHub](https://github.com/java-native-access/jna#download) - you need both *jna-4.x.0.jar* and *jna-platform-4.x.0.jar*.
- [JOGL](https://github.com/sgothel/jogl) - Java™ Binding for the OpenGL® API.  
  - Download the base library from [their website](https://jogamp.org/).
  - Native X11 wrapper needs to be **patched** by jogl_patch.txt [patched precompiled JAR here.](native)
- [Gson](https://github.com/google/gson) - A Java serialization/deserialization library that can convert Java Objects into JSON and back. [Precompiled JAR here.](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar)