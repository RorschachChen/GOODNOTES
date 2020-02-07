with tf.Graph().as_default() as g

TFRecords是一种tensorflow的内定标准文件格式，其实质是二进制文件

使用tf.train.string_input_producer函数把我们需要的全部文件打包为一个tf内部的queue类型，之后tf开文件就从这个queue中取目录了，要注意一点的是这个函数的shuffle参数默认是True，

import的时候把从库中的放一起，从旁边文件中的放一起

if __name__ == '__main__':
有时候你写了一个模块（文件），你调试的时候可以直接运行这个文件，当它被导入(import)其他模块时，调试的信息就不会被调用到。

tf.global_variables_initializer()添加节点用于初始化所有的变量(GraphKeys.VARIABLES)。返回一个初始化所有全局变量的操作（Op）。在你构建完整个模型并在会话中加载模型后，运行这个节点。

Supervisorn能
（1）自动去checkpoint加载数据或初始化数据
（2）自身有一个Saver，可以用来保存checkpoint
（3）有一个summary_computed用来保存Summary

No need：
（1）手动初始化或从checkpoint中加载数据
（2）不需要创建Saver，使用sv内部的就可以
（3）不需要创建summary writer

class Resnet(Model):
    def __init__(self, num_classes=40, is_training=True, update_batch_stats=True, stochastic=True, seed=1234):
        super(Resnet, self).__init__()

        input_shape = (448, 448, 3)
        resnet50 = ResNet50(input_shape=input_shape, weights='imagenet', include_top=True)
        print(resnet50.summary())
        exit()
        self.base = resnet50
        self.base.trainable = is_training
        self.fc = tf.layers.Dense(40, activation=None, trainable=is_training)

    def call(self, x):
        x2 = self.base(x)
        x2 = tf.squeeze(x2, axis=[1,2])
        x = self.fc(x2)
        print(self.base.summary())
        exit()
        return x, x2

tf.cast: Casts a tensor to a new type.
tf.cast(
    x,
    dtype,
    name=None
)

tf.math.greater: Returns the truth value of (x > y) element-wise.
tf.math.greater(
    x,
    y,
    name=None
)

