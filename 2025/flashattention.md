如果您使用的是安培架构（如英伟达 A10G 或 RTX 4090/3090）或更新的 GPU，则可以使用闪存注意力。
Flash Attention 是一种对注意力计算进行重新排序的方法，它利用经典技术（平铺、重新计算）大大加快了计算速度，并将内存使用量从序列长度的二次方降低到线性。
简而言之，就是将训练速度提高 3 倍。了解更多信息，请访问 FlashAttention。
