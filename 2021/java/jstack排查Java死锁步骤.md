### jstack排查Java死锁步骤

- 在终端中输入jps查看当前运行的java程序
- 使用 jstack -l pid 查看线程堆栈信息
- 分析堆栈信息

### jstack 分析CPU过高步骤

- 1. top：查看各个进程的cpu使用情况
- 1. top -Hp pid：查看该进程下，各个线程的cpu使用情况
- 1. jstack pid：查看当前java进程的堆栈状态
- 1. jstack -l [PID] >/tmp/log.txt：把这些堆栈信息打到一个文件里
- 1. 分析堆栈信息。我们把占用cpu资源较高的线程pid（本例子是21350），将该pid转成16进制的值。在thread dump中，每个线程都有一个nid，我们找到对应的nid（5366），发现一直在跑（24行）。
  2. printf '%x\n' pid。一般输入这个指令就可以快速转换成16进制了





```sh
Usage:
    jmap [option] <pid>
        (to connect to running process)
    jmap [option] <executable <core>
        (to connect to a core file)
    jmap [option] [server_id@]<remote server IP or hostname>
        (to connect to remote debug server)

where <option> is one of:
    <none>               to print same info as Solaris pmap
    -heap                to print java heap summary
    -histo[:live]        to print histogram of java object heap; if the "live"
                         suboption is specified, only count live objects
    -permstat            to print permanent generation statistics
    -finalizerinfo       to print information on objects awaiting finalization
    -dump:<dump-options> to dump java heap in hprof binary format
                         dump-options:
                           live         dump only live objects; if not specified,
                                        all objects in the heap are dumped.
                           format=b     binary format
                           file=<file>  dump heap to <file>
                         Example: jmap -dump:live,format=b,file=heap.bin <pid>
    -F                   force. Use with -dump:<dump-options> <pid> or -histo
                         to force a heap dump or histogram when <pid> does not
                         respond. The "live" suboption is not supported
                         in this mode.
    -h | -help           to print this help message
    -J<flag>             to pass <flag> directly to the runtime system
```





jstack -l pid | grep -A 15 TIMED_WAITING

获取包含TIMED_WAITING的之后15行。





spark app name / spark executor tag





top

进程CPU 100%相当于1 core





Stat -gcutil 15 2000 20 ?????