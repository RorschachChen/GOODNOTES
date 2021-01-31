support for Scala 2.11 was removed in Spark 3.0.0.



RDD 的操作算子包括两 类， 一类是 transformation，用来将 RDD 进行转换，构建 RDD 的依赖关系;另 一类称为 action, 用来触发 RDD 的计算 ， 得到 RDD 的相关计算结果或将 RDD 保存到文件系统中 。

只有遇到 action，才会真正地执行 RDD 的计算



当 RDD 的某个分区数据计 算失 败或丢 失时，可以通过 Lineage信息重建

但是Lineage信息会越来越长，重建代价大。改为checkpoint



Spark在 RDD （基本底层）基础上，提供了 DataFrame 和 Dataset 用户编程接口



RDD不包含任何结构信息，DataFrame 中的数据集类似于关系数据库中的表， 按列名存储DataFrame具有Schema信息。

DataFrame 本质上是一种特殊的 Dataset (Dataset[Row]类型)

Dataset引入了编码器( Encoder)的概念。在映射的过程中， Encoder首先检查定义的 Person 类的类型是否与数据相符



SparkSession逐步取代 SparkContext成为 Spark 应用程序的入口



逻辑计划阶段会将用户所写的 SQL语句转换成树型数据结构(逻辑算子树)，

物理计划阶段将上一步逻辑计划阶段生成的逻辑算子树进行进一步转换，生成物理算子树 。物理计划阶段也包含 3 个子阶段:首 先，根据逻辑算子树，生成物理算子树的列表 Iterator[PhysicalPlan] (同样的逻辑算子树可能对 应多个物理算子树);然后，从列表中按照一定的策略选取最优的物理算子树( SparkPlan);最 后，对选取的物理算子树进行提交前的准备工作，例如，确保分区操作正确、物理算子树节点 重用、执行代码生成等，得到“准备后”的物理算子树(PreparedSparkPlan)。物理算子树生成的 RDD 执行 action操作(如例子中的 show)，即可提交执行 。



在 SparkSQL 内部实现中， InternalRow就是用来表示一行行数据的类

**InternalRow作为一个抽象类，包含 numFields和 update方法，以及各列数 据对应的 get与 set方法**，但具体的实现逻辑体现在不同的子类中。InternalRow 中都是根据下标来访问和操作列元素的。



**访问者模式把结构和行为分离**



**文法？词法？**





**LimitPushDown 优化规则，能够将 LocalLimit 算子下推到 Union All 和 Outer Join 操作算子的下 方，减少这两种算子在实际计算过程中需要处理的数据量 。**



如果聚合查询中涉及浮点数的精度处理，性能就会受到很大的影响。

**PruneFileSourcePartitions 优化规则会尽可能地将过滤算子下推到存储层，这样**

**可以避免读入无关的数据分区 。**

Batch User Provided Optimizers：该 Batch 用于支持用户自定义的优化规则。用户只需要继承 Rule[LogicalPlan]虚类，实现相应的转换逻辑就可以注册到优化规则队列中应用 执行。





具体来看， SparkPlan 的主要功能可以划分为 3 大块 。 首先，每个 SparkPlan 节点必不可少地 会记录其元数据( Metadata)与指标( Metric)信息，这些信息以 Key-Value 的形式保存在 Map 数 据结构中，统称为 SparkPlan 的 Metadata与 Metric体系 。 其次，在对 RDD 进行 Transformation操 作时，会涉及数据分区( Partitioning)与排序( Ordering)的处理，称为 SparkPlan 的 Partitioning与 Ordering体系;最后， SparkPlan作为物理计划，支持提交到 SparkCore去执行，即 SparkPlan 的执行操作部分，以 execute 和 executeBroadcast 方法为主。



UnaryExecNode节点的作用主要是对 RDD进行转换操作。 ProjectExec 和 FilterExec 分别对子节点产生的 RDD 进行列剪裁与行过滤操作 。