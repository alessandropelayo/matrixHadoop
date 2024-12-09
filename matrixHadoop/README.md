How to use MatrixMultiplication

Compile with:

``javac -classpath $(hadoop classpath) -d classes/ MatrixMultiplication.java``

``jar -cvf matmult.jar -C classes/ .``

Upload files in resources to hadoop hdfs

To use the program:

``hadoop jar matmult.jar MatrixMultiplication /input/matrix_A.csv /input/matrix_B.csv /output/matrix3x3 3x3 3``

or

``hadoop jar matmult.jar MatrixMultiplication /input/matrix_A23.csv /input/matrix_B34.csv /output/matrix2x3x4 2x3 4``
