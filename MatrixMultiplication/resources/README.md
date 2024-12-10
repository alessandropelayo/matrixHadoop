
# Matrix Multiplication with Hadoop MapReduce

## Description
This project implements distributed matrix multiplication using Hadoop's MapReduce framework.

### Features
- Processes large matrices stored in HDFS.
- Scalable for distributed computation.

## File Structure
- `src/MatrixMultiplication.java`: Source code for MapReduce.
- `resources/matrix_A.csv`, `resources/matrix_B.csv`: Example input matrices.
- `run_matrix.sh`: Script to automate compilation and execution.

## Commands to Run
1. Compile the code:
   ```bash
   javac -classpath $(hadoop classpath) -d build/classes src/MatrixMultiplication.java
   ```

2. Create the JAR file:
   ```bash
   jar -cvf build/matmult.jar -C build/classes/ .
   ```

3. Upload input files to HDFS:
   ```bash
   hadoop fs -mkdir /input
   hadoop fs -put resources/matrix_A.csv /input
   hadoop fs -put resources/matrix_B.csv /input
   ```

4. Run the Hadoop job:
   ```bash
   hadoop jar build/matmult.jar MatrixMultiplication /input /output
   ```

5. Retrieve the output:
   ```bash
   hadoop fs -get /output/part-r-00000 results/matrix_output.txt
   ```
