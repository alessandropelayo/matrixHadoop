How to use MatrixMultiplication

Compile with:

``javac -classpath $(hadoop classpath) -d classes/ MatrixMultiplication.java``

``jar -cvf matmult.jar -C classes/ .``

Upload files in resources to hadoop hdfs

``hadoop fs -mkdir /input``

``hadoop fs -put resources/matrix_A.csv /input``

``hadoop fs -put resources/matrix_B.csv /input``

``hadoop fs -put resources/matrix_A23.csv /input``

``hadoop fs -put resources/matrix_B34.csv /input``

The output directory must not exist prior to running the command. Delete it if needed:

``hadoop fs -rm -r /output/matrix3x3``

To use the program:

``hadoop jar matmult.jar MatrixMultiplication /input/matrix_A.csv /input/matrix_B.csv /output/matrix3x3 3x3 3``

or

``hadoop jar matmult.jar MatrixMultiplication /input/matrix_A23.csv /input/matrix_B34.csv /output/matrix2x3x4 2x3 4``


