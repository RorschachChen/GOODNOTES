Thus, the computation is a directed acyclic graph (DAG) of operators and is partitioned into different stages with a barrier between each of them.

The existing Spark scheduler implementation uses two threads: one to compute the stage dependencies and locality preferences, and the other to manage task
queuing, serializing, and launching tasks on executors.
