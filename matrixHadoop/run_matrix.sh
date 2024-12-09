#!/bin/bash

# Set variables for file paths
INPUT_DIR="/input"
OUTPUT_DIR="/output"
JAR_FILE="matmult.jar"
CLASS_NAME="MatrixMultiplication"
SRC_FILE="resources/MatrixMultiplication.java"

# Step 1: Compile Java program
echo "Compiling Java program..."
javac -classpath $(hadoop classpath) -d classes/ $SRC_FILE
if [ $? -ne 0 ]; then
  echo "Error during compilation. Exiting."
  exit 1
fi

# Step 2: Create JAR file
echo "Creating JAR file..."
jar -cvf $JAR_FILE -C classes/ .
if [ $? -ne 0 ]; then
  echo "Error creating JAR file. Exiting."
  exit 1
fi

# Step 3: Upload input files to HDFS
echo "Uploading input files to HDFS..."
hadoop fs -mkdir -p $INPUT_DIR
hadoop fs -put -f resources/matrix_A.csv $INPUT_DIR
hadoop fs -put -f resources/matrix_B.csv $INPUT_DIR
hadoop fs -put -f resources/matrix_A23.csv $INPUT_DIR
hadoop fs -put -f resources/matrix_B34.csv $INPUT_DIR

# Step 4: Run Hadoop job for matrix multiplication
echo "Running Hadoop job for a 3x3 matrix multiplication..."
hadoop jar $JAR_FILE $CLASS_NAME $INPUT_DIR/matrix_A.csv $INPUT_DIR/matrix_B.csv $OUTPUT_DIR/matrix3x3 3x3 3

echo "Running Hadoop job for a 2x3 x 3x4 matrix multiplication..."
hadoop jar $JAR_FILE $CLASS_NAME $INPUT_DIR/matrix_A23.csv $INPUT_DIR/matrix_B34.csv $OUTPUT_DIR/matrix2x3x4 2x3 4

# Step 5: Retrieve output from HDFS
echo "Retrieving output from HDFS..."
mkdir -p results
hadoop fs -get $OUTPUT_DIR/matrix3x3/part-r-00000 results/matrix3x3_output.txt
hadoop fs -get $OUTPUT_DIR/matrix2x3x4/part-r-00000 results/matrix2x3x4_output.txt

echo "Output files saved in the 'results' directory."

# Step 6: Clean up HDFS output directories (Optional)
echo "Cleaning up HDFS output directories..."
hadoop fs -rm -r $OUTPUT_DIR/matrix3x3
hadoop fs -rm -r $OUTPUT_DIR/matrix2x3x4

echo "Script execution completed successfully."
