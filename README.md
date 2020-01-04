# JNIDemo 

Here are the steps I took to create and implement a JNI on a Mac. 

以下是我在 Mac 电脑上创建 JNI 的过程。

## Steps

### 1. Create a class that has JNI.

Create a method with `native` keyword, `JniWrapper`.
Note that it should be defined exactly the same as you define other Java methods. The only difference is the extra `native` keyword.

```java
public native int add(int a, int b);
```

### 2. Create a class that calls it.

Create a class that calls it, `JniDemo`. This class must have a static block that loads this JNI library. You can name it anything, the name will be used in following steps. It ends with `.jnilib` on Mac, `.dll` on Windows, and `.so` on Linux. Here I named the library `jniwrapper`:

```java
static {
    System.loadLibrary("jniwrapper");
}
```

### 3. Compile these classes.

Nothing special here. You can either use IDE or use `javac`, command directly:

```bash
$ javac <your_package_file_path>JniWrapper.java
$ javac <your_package_file_path>/JniDemo.java
```

`<your_package_file_path>` is the path of your `.java` file, for example for this repo:

```bash
$ javac src/net/lilifei/learning/jni/JniWrapper.java
$ javac src/net/lilifei/learning/jni/JniDemo.java
```

### 4. Generate the header file of this JNI:

Run this command against the `.class` file in the corresponding path:

```bash
$ javah -jni <package>.JniWrapper
```

`<package>` is the package name of yours:

```bash
$ javah -jni net.lilifei.learning.jni.JniWrapper
```

Note that this command only works for JDK 8 and below. I haven't tried with higher JDK versions.

After executing this command, you will get a `.h` file.

### 5. Implement this header file.

You can use either C or C++. In my example, I implemented a method that adds two numbers:

```c
#include <jni.h>
#include "net_lilifei_learning_jni_JniWrapper.h"

JNIEXPORT jint JNICALL Java_net_lilifei_learning_jni_JniWrapper_add
  (JNIEnv *env, jobject object, jint a, jint b) {
    return a + b;
  }
```

### 6. Compile the C/C++ file in this path:

```bash
$ gcc -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin/" -o lib<library_name>.jnilib -shared <your_c_file_name>.c
```

1. `$JAVA_HOME` is where the JDK installed in your computer. You don't have to manually type it if you have it set.
2. `<your_c_file_name>` is the file name you used to implement the header file.
3. `<library_name>` is the name you used in step 2.

```bash
$ gcc -I"/Library/Java/JavaVirtualMachines/jdk1.8.0_31.jdk/Contents/Home/include" -I"/Library/Java/JavaVirtualMachines/jdk1.8.0_31.jdk/Contents/Home/include/darwin/" -o libjniwrapper.jnilib -shared net_lilifei_learning_jni_JniWrapper.c
```

### 7. Run

If you use command line, here the command:

```bash
java -Djava.library.path="<class_path>" <class_name>
```

`<class_path>` is the path of the `.class` file in step 2, and `<class_name>` is the file name.

If you use an IDE, e.g. IntelliJ IDEA, you should be able to set it in VM options in Edit Configurations:

```bash
-Djava.library.path="<class_path>"
```

## 步骤

JNI 是 Java Native Interface 的缩写，它是 Java 提供的一套使开发者可以通过 Java 调用本地操作系统功能的框架。

JNI 通过定义一个以`native`关键词修饰的方法并由某种本地语言（例如 C、C++）实现之后，调用该方法来达到使用本地操作系统的目的。

此处记录我尝试实现一个 JNI 的过程：

### 1. 创建有 JNI 的类。

创建一个有`native`方法的类，`JniWrapper`。
注意该方法定义方式与其他 Java 方法相同，唯一的区别是有`native`关键词并且没有方法体。

```java
public native int add(int a, int b);
```

### 2. 创建调用它的类。

创建一个调用该类的类，`JniDemo`，该类必须有这样一个静态块，加载你的本地库，名字可以随便起，但要记下该名字，一会儿编译库的时候用。Mac 上以`.jnilib`结尾，Windows 上以`.dll`结尾，Linux 上以`.so`结尾。此处，我的库名为`jniwrapper`：

```java
static {
    System.loadLibrary("jniwrapper");
}
```

### 3. 编译这两个类。

这一步没有任何特殊之处，使用 IDE 的功能或者用`javac`，得到`.class`文件：

```bash
$ javac <your_package_file_path>JniWrapper.java
$ javac <your_package_file_path>/JniDemo.java
```

`<your_package_file_path>`是包含了你的包路径的`.java`文件位置，例如对于本 repo 就是：

```bash
$ javac src/net/lilifei/learning/jni/JniWrapper.java
$ javac src/net/lilifei/learning/jni/JniDemo.java
```

### 4. 生成 JNI 的头文件。

在创建`.class`的路径中，对包含了`native`的类运行如下命令：

```bash
$ javah -jni <package>.JniWrapper
```

其中`<package>`是你创建的包名，即包含了命名空间的类名，例如：

```bash
$ javah -jni net.lilifei.learning.jni.JniWrapper
```

注意该命令仅在 JDK 8 及以下环境中有效，我尚未尝试更高版本环境中的命令。

完成后，会生成一个`.h`文件。

### 5. 实现该`.h`文件。

用 C 或 C++ 实现该头文件。例如在我的例子中，我实现了两数相加：

```c
#include <jni.h>
#include "net_lilifei_learning_jni_JniWrapper.h"

JNIEXPORT jint JNICALL Java_net_lilifei_learning_jni_JniWrapper_add
  (JNIEnv *env, jobject object, jint a, jint b) {
    return a + b;
  }
```

### 6. 在该路径中，编译该 C 或 C++ 文件：

```bash
$ gcc -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/darwin/" -o lib<library_name>.jnilib -shared <your_c_file_name>.c
```
其中：
1. `$JAVA_HOME`是电脑 JDK 安装的位置，如果设置了环境变量，就不用手动输入。
2. `<your_c_file_name>`是你刚才实现头文件时使用的文件名。
3. `<library_name>`则是你在第二步中使用的名字。

```bash
$ gcc -I"/Library/Java/JavaVirtualMachines/jdk1.8.0_31.jdk/Contents/Home/include" -I"/Library/Java/JavaVirtualMachines/jdk1.8.0_31.jdk/Contents/Home/include/darwin/" -o libjniwrapper.jnilib -shared net_lilifei_learning_jni_JniWrapper.c
```

### 7. 最后运行即可。

使用命令行，输入的命令应该是：

```bash
java -Djava.library.path="<class_path>" <class_name>
```

其中`<class_path>`就是第二步中`.class`文件所在的位置，而`<class_name>`则是该文件名。

如果使用的是 IDE，例如 IntelliJ IDEA，在 Edit Configuration 中，设置 VM options：

```bash
-Djava.library.path="<class_path>"
```