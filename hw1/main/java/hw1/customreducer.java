package hw1;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import java.util.ArrayList;
import java.util.List;



public class customreducer extends Reducer<Text,IntWritable,Text,IntWritable> {

    /**
     * Writes ip on average request size and total request size to context on each ip address
     * @param key ip address
     * @param values request sizes
     * @param context mr context
     */
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        int sum = 0;
        int count = 0;


        for(IntWritable val : values)
        {
            sum += val.get();
            count++;

        }

        double sc = (double) sum/count;

        context.write(new Text(key.toString()+","+String.format("%.2f",sc)),new IntWritable(sum));

    }



}