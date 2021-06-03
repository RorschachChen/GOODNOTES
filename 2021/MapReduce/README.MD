The MapReduce whereby a map phase can do arbitrary local computations, followed by a barrier in the form of an all-toall
shuffle, after which the reduce phase can proceed with each reducer reading the output of relevant mappers (often
all of them).
