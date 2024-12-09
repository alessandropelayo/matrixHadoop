package main.java.com.projHadoop.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MatrixMultiplication {

  public static class MatrixMapper extends Mapper<Object, Text, Text, Text> {
    private Text outputKey = new Text();
    private Text outputValue = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      // Parse input line: matrixName,rowIndex,colIndex,value
      String[] parts = value.toString().split(",");
      if (parts.length == 4) {
        String matrixName = parts[0];
        int rowIndex = Integer.parseInt(parts[1]);
        int colIndex = Integer.parseInt(parts[2]);
        double matrixValue = Double.parseDouble(parts[3]);


        // This builds the key such that an N x M and Y x Z 
        // builds towards the resulting M x Y matrix
        // Handle first matrix
        if (matrixName.equals("A")) {
          // Emit for all columns of B
          // i.e. 0 to (B columns - 1)= due to 0 indexing
          for (int i = 0; i < context.getConfiguration().getInt("matrixB.columns", 0); i++) {
            outputKey.set(rowIndex + "," + i); // Key: row of A, column of B
            outputValue.set("A," + colIndex + "," + matrixValue); // Value: matrix A's data
            context.write(outputKey, outputValue);
          }
        } // Handle second matrix
        else if (matrixName.equals("B")) {
          // Emit for all rows of A
          // i.e. 0 to (A rowss - 1) due to 0 indexing
          for (int i = 0; i < context.getConfiguration().getInt("matrixA.rows", 0); i++) {
            outputKey.set(i + "," + colIndex); // Key: row of A, column of B
            outputValue.set("B," + rowIndex + "," + matrixValue); // Value: matrix B's data
            context.write(outputKey, outputValue);
          }
        }
      }
    }
  }

  public static class MatrixReducer extends Reducer<Text, Text, Text, DoubleWritable> {
    private DoubleWritable result = new DoubleWritable();

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      Map<Integer, Double> aValues = new HashMap<>();
      Map<Integer, Double> bValues = new HashMap<>();

      // Parse values into separate maps for A and B
      for (Text val : values) {
        String[] parts = val.toString().split(",");
        String matrixName = parts[0];
        int index = Integer.parseInt(parts[1]);
        double value = Double.parseDouble(parts[2]); // Value in matrix data

        // Place all values into their respective map
        // Index is unique for each matrix
        if (matrixName.equals("A")) {
          aValues.put(index, value);
        } else if (matrixName.equals("B")) {
          bValues.put(index, value);
        }
      }

      // Compute the dot product
      double sum = 0.0;
      // For each entry in A check if the same entry exists in B
      // If so, multiply both into sum
      // Continue until dot product is complete
      for (Map.Entry<Integer, Double> aEntry : aValues.entrySet()) {
        int index = aEntry.getKey();
        if (bValues.containsKey(index)) {
          sum += aEntry.getValue() * bValues.get(index);
        }
      }

      // Emit the computed value for this matrix cell
      result.set(sum);
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 5) {
      System.err.println(
          "Usage: matrixmultiplication <matrix_a_input> <matrix_b_input> <output> <matrix_a_rows_columns> <matrix_b_columns>");
      System.exit(2);
    }

    // Parse matrix dimensions from command-line arguments
    String[] matrixADimensions = args[3].split("x");
    if (matrixADimensions.length != 2) {
      System.err.println("Matrix A dimensions must be in the format rowsxcolumns (e.g., 2x3).");
      System.exit(2);
    }
    int matrixARows = Integer.parseInt(matrixADimensions[0]);
    int matrixAColumns = Integer.parseInt(matrixADimensions[1]);

    int matrixBColumns = Integer.parseInt(args[4]);

    // Matrix rows and columns must be known
    // Also assumes matrixs are compatible
    // i.e. 3x4 & 4x4 or 3x3 & 3x3
    Configuration conf = new Configuration();
    conf.setInt("matrixA.rows", matrixARows);
    conf.setInt("matrixA.columns", matrixAColumns);
    conf.setInt("matrixB.columns", matrixBColumns);

    Job job = Job.getInstance(conf, "Matrix Multiplication");
    job.setJarByClass(MatrixMultiplication.class);

    // Set Mapper and Reducer classes
    job.setMapperClass(MatrixMapper.class);
    job.setReducerClass(MatrixReducer.class);

    // Set output key and value types
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    // Set input and output paths
    FileInputFormat.addInputPath(job, new Path(args[0])); // Matrix A input
    FileInputFormat.addInputPath(job, new Path(args[1])); // Matrix B input
    FileOutputFormat.setOutputPath(job, new Path(args[2]));

    // Configure number of reducers
    job.setNumReduceTasks(2);

    // Exit based on job completion
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}