import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.StringTokenizer;

// >>> Don't Change
public class OrphanPages extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new OrphanPages(), args);
        System.exit(res);
    }
// <<< Don't Change

    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(this.getConf(), "Orphan Pages");
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setMapperClass(OrphanPagesMap.class);
        job.setReducerClass(OrphanPagesReduce.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        job.setJarByClass(OrphanPages.class);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static class OrphanPagesMap extends Mapper<Object, Text, IntWritable, IntWritable> {
        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            String line = value.toString();
            String delims = ":" + " ";
            StringTokenizer st = new StringTokenizer(line, delims);
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                String newTok = tok.trim();
                int newTokInt = Integer.parseInt(newTok);
                context.write(new IntWritable(newTokInt), new IntWritable(1));
            }
        }
    }

    public static class OrphanPagesReduce extends Reducer<IntWritable, IntWritable, IntWritable, NullWritable> {
        @Override
        public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }

            if (1 == sum) { 
                context.write(key, null);
            }
        }
    }
}