运行即可。这里要注意max_seq_length和train_batch_size这两个参数，设置过大是很容易爆掉显存的，一般来说运行bert需要11G左右的显存。

备注
max_seq_length是指词的数量而不是指字符的数量。参考代码中的注释：

The maximum total input sequence length after WordPiece tokenization. Sequences longer than this will be truncated, and sequences shorter than this will be padded.

对于sequence的理解，网上很多博客都把这个翻译为句子，我个人认为是不准确的，序列是可以包含多个句子的，而不只是单独一个句子。
