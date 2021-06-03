A centralized driver schedules all the map tasks to take turns running on free resources in the cluster. Each map task
then outputs records for each reducer based on some partition strategy, such as hashing or sorting. Each task then informs
the centralized driver of the allocation of output records to the different reducers. The driver can then schedule the reduce
tasks on available cluster resources, and pass this metadata to each reduce task, which then fetches the relevant records
from all the different map outputs. Thus, each barrier in a micro-batch requires communicating back and forth with the
driver.


In existing BSP systems like Spark or Hadoop, upstream tasks typically write their output to local disk and
notify the centralized driver of the allocation of output records to the different reducers.
