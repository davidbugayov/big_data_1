package hw1;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
* Enum line
* */
enum line {
    VALID,
    INVALID
}


/**
* Program which count average bytes per request by IP and total bytes by IP
* set separator symbol ',' for csv format
* */
public class main {

    public static void main(String[] args) throws Exception {
        if(args.length != 2) {
            System.out.println("Usage: hadoop jar 'jarfile.jar' <input_path> <outputh_path>");
            System.exit(-1);
        }


        Configuration conf = new Configuration();
        conf.set("mapred.textoutputformat.separator", ",");
        Job job = Job.getInstance(conf, "apachelog");
        job.setJarByClass(main.class);
        job.setMapperClass(custommapper.class);
        job.setReducerClass(customreducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);


        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}
