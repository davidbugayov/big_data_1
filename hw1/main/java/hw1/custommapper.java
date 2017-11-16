package hw1;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;


public class custommapper extends Mapper<Object, Text, Text, IntWritable>{

    /**
    * Regex patter for parse log file
    * */
    Pattern regex = Pattern.compile("^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+)");


    /**
    *  @param key line number
     * @param value line content
     * @param context mr context
     * */
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        StringTokenizer itr = new StringTokenizer(value.toString(),"\r\n");

        while (itr.hasMoreTokens()) {

            String next = itr.nextToken();

            if(is_valid(next))
            {
                context.getCounter(line.VALID).increment(1);

                Matcher mregex = regex.matcher(next);
                mregex.find();

                context.write(new Text(mregex.group(1)),new IntWritable(Integer.parseInt(mregex.group(7))));

            }else
            {
                context.getCounter(line.INVALID).increment(1);
            }
        }
    }


    /**
     * Line validation
     * @param line - line to parse
     * @return true if line parse true else false
     */
    public Boolean is_valid(String line)
    {
        Matcher mregex = regex.matcher(line);
        if(mregex.find())
        {
            return true;
        }
        return false;
    }

}