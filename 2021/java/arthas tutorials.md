**Arthas启动命令**

java -jar arthas-boot.jar --target-ip 0.0.0.0

1

The dashboard command can view the real-time data panel of the current system.

**sc:**
搜索loaded classes.
基础用法：
sc javax.servlet.Filter
其他参数
-d: 详细

**sm:**

搜索具体method

基础用法:

sm java.math.RoundginMode



**Jad**

decompile code

基础用法:

jad --source-only com.example.demo.arthas.user.UserController



**Ognl**

动态执行代码



#### 排查函数调用异常现象

watch com.example.demo.arthas.user.UserController * '{params, throwExp}'

第一个参数是lei