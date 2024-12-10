
#!/bin/bash

# Compile the Java program
javac -classpath $(hadoop classpath) -d build/classes src/MatrixMultiplication.java

# Create JAR file
jar -cvf build/matmult.jar -C build/classes/ .

# Run Hadoop job
hadoop fs -mkdir -p /input
hadoop fs -put resources/matrix_A.csv /input
hadoop fs -put resources/matrix_B.csv /input
hadoop jar build/matmult.jar MatrixMultiplication /input /output

# Fetch output from HDFS
hadoop fs -get /output/part-r-00000 results/matrix_output.txt
