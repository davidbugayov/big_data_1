package hw1;


import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.io.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import org.mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class test {

    /**
     * Declare harnesses that let you test a mapper, a reducer, and
     * a mapper and a reducer working together.
     */
    MapDriver<Object, Text, Text, IntWritable> mapDriver;
    ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
    MapReduceDriver<Object, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

    /**
     * Set up the test. This method will be called before every test.
     */
    @Before
    public void setUp() {
        /**
        * Set up the mapper test harness.
        */
        custommapper mapper = new custommapper();
        mapDriver = new MapDriver<>();
        mapDriver.setMapper(mapper);

        /**
        * Set up the reducer test harness.
        */
        customreducer reducer = new customreducer();
        reduceDriver = new ReduceDriver<>();
        reduceDriver.setReducer(reducer);

        /**
        * Set up the mapper/reducer test harness.
        */
        mapReduceDriver = new MapReduceDriver<>();
        mapReduceDriver.setMapper(mapper);
       mapReduceDriver.setReducer(reducer);
    }


    /**
    * Test the mapper.
    */
    @Test
    public void testMapper()  throws IOException {
        mapDriver.withInput(NullWritable.get(), new Text("192.168.1.243 - - [2/1/2017:11:49:37 -0700] \"PUT /wp-includes HTTP/1.1\" 503 48303 https://yandex.ru/ Mozilla/5.0 (X11; Linux x86_64; rv:6.0a1) Gecko/20110421 Firefox/6.0a1 "));
        mapDriver.withOutput(new Text("192.168.1.243"), new IntWritable(48303));
        mapDriver.runTest();

    }

    /**
     * Test the reducer
     */
    @Test
    public void testReducer() throws IOException{
        List<IntWritable> list = new ArrayList<>();
        list.add(new IntWritable(48303));
        reduceDriver.withInput(new Text("192.168.1.243"), list);
        reduceDriver.withOutput(new Text("192.168.1.243,48303.00"), new IntWritable(48303));
        reduceDriver.runTest();
    }

    /**
     * Test the mapreducer
     */
    @Test
    public void testMapReduce() {
        mapReduceDriver.withInput(NullWritable.get(), new Text("192.168.1.243 - - [2/1/2017:11:49:37 -0700] \"PUT /wp-includes HTTP/1.1\" 503 48303 https://yandex.ru/ Mozilla/5.0 (X11; Linux x86_64; rv:6.0a1) Gecko/20110421 Firefox/6.0a1 "));
        mapReduceDriver.withOutput(new Text("192.168.1.243,48303.00"), new IntWritable(48303));
        try {
            mapReduceDriver.runTest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
