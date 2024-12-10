
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatrixMultiplication {
    public static class MatrixMapper extends Mapper<Object, Text, Text, Text> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] elements = value.toString().split(",");
            String matrixName = elements[0];
            int row = Integer.parseInt(elements[1]);
            int col = Integer.parseInt(elements[2]);
            double val = Double.parseDouble(elements[3]);

            if (matrixName.equals("A")) {
                context.write(new Text(Integer.toString(col)), new Text("A," + row + "," + val));
            } else if (matrixName.equals("B")) {
                context.write(new Text(Integer.toString(row)), new Text("B," + col + "," + val));
            }
        }
    }

    public static class MatrixReducer extends Reducer<Text, Text, Text, DoubleWritable> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            List<String[]> matrixA = new ArrayList<>();
            List<String[]> matrixB = new ArrayList<>();

            for (Text val : values) {
                String[] valueParts = val.toString().split(",");
                if (valueParts[0].equals("A")) {
                    matrixA.add(new String[]{valueParts[1], valueParts[2]});
                } else if (valueParts[0].equals("B")) {
                    matrixB.add(new String[]{valueParts[1], valueParts[2]});
                }
            }

            for (String[] a : matrixA) {
                for (String[] b : matrixB) {
                    int row = Integer.parseInt(a[0]);
                    int col = Integer.parseInt(b[0]);
                    double product = Double.parseDouble(a[1]) * Double.parseDouble(b[1]);
                    context.write(new Text(row + "," + col), new DoubleWritable(product));
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Matrix Multiplication");

        job.setJarByClass(MatrixMultiplication.class);
        job.setMapperClass(MatrixMapper.class);
        job.setReducerClass(MatrixReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
