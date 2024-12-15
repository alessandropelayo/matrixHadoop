#!/bin/bash

# Define variables
HADOOP_CLASSPATH=$(hadoop classpath)
PROJECT_DIR=$(pwd)
HDFS_DIR="/user/matrix_multiplication"
OUTPUT_DIR="/user/output"
OUTPUT_LARGE_DIR="/user/output_large"
JAR_FILE="MatrixMultiplication.jar"
MAIN_CLASS="MatrixMultiplication"

# Compilation
echo "Compiling Java files..."
javac -classpath $HADOOP_CLASSPATH -d . MatrixMultiplication.java
if [ $? -ne 0 ]; then
    echo "Compilation failed. Exiting."
    exit 1
fi

# Create JAR file
echo "Creating JAR file..."
jar -cvf $JAR_FILE *.class
if [ $? -ne 0 ]; then
    echo "Failed to create JAR file. Exiting."
    exit 1
fi

# Create HDFS directories and upload files
echo "Setting up HDFS..."
hdfs dfs -mkdir -p $HDFS_DIR
hdfs dfs -put -f matrix_A.csv $HDFS_DIR/
hdfs dfs -put -f matrix_B.csv $HDFS_DIR/
hdfs dfs -put -f dataset.txt $HDFS_DIR/

# Run Hadoop jobs
echo "Running Hadoop jobs..."
# Multiply 4x4 matrices
hdfs dfs -rm -r -f $OUTPUT_DIR
hadoop jar $JAR_FILE $MAIN_CLASS $HDFS_DIR/matrix_A.csv $HDFS_DIR/matrix_B.csv $OUTPUT_DIR 4x4 4
if [ $? -ne 0 ]; then
    echo "Hadoop job for 4x4 matrices failed. Exiting."
    exit 1
fi

# Multiply 100x100 matrix
hdfs dfs -rm -r -f $OUTPUT_LARGE_DIR
hadoop jar $JAR_FILE $MAIN_CLASS $HDFS_DIR/dataset.txt $HDFS_DIR/dataset.txt $OUTPUT_LARGE_DIR 100x100 100
if [ $? -ne 0 ]; then
    echo "Hadoop job for 100x100 matrix failed. Exiting."
    exit 1
fi

# Fetch results
echo "Fetching results from HDFS..."
echo "4x4 Matrix Multiplication Results:"
hdfs dfs -cat $OUTPUT_DIR/part-00000

echo "100x100 Matrix Multiplication Results:"
hdfs dfs -cat $OUTPUT_LARGE_DIR/part-00000

echo "Hadoop jobs completed successfully."

