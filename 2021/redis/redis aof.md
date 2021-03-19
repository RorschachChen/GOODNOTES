### redis aof

The way it works is extremely simple: every time a write operation that modifies the dataset in memory is performed, the operation gets logged. 

Redis re-plays all the operations to reconstruct the dataset.只会记录对db有修改的操作

AOF文件不断增长的问题？

When an AOF is too big Redis will simply rewrite it from scratch in a temporary file. The rewrite is NOT performed by reading the old one, but directly accessing data in memory, so that Redis can create the shortest AOF that is possible to generate。Once the rewrite is terminated, the temporary file is synched on disk with fsync and is used to overwrite the old AOF file.

新aof复写过程的数据log同时写到旧的aof和 in-memory buffer，最后从 in-memory buffer又写到新aof中