package com.matrixmulti;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
Text - Matrix position input from Mapper (e.g. "(0,0)")
Text - (Matrix filename, value) input from mapper
Text - "(A*B = )"
Text - Multiplied matrix
 */
public class MatrixReducer extends Reducer<Text, Text, Text, Text>{
    @Override
    public void reduce(Text key, Iterable<Text> value, Context context) throws IOException, InterruptedException {
        
    }
}
