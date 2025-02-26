Our choice is for the paged_adamw_32bit optimizer, a variant of the AdamW optimizer designed to be more efficient on 32-bit GPUs. 
It does this by breaking the model parameters into smaller pages and optimizing each page separately. 
This can reduce the memory usage of the optimizer and improve its performance on 32-bit GPUs.
