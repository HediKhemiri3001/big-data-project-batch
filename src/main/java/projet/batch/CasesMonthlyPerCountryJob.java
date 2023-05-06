package projet.batch;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class CasesMonthlyPerCountryJob  {


    public static void main(String[] args) throws Exception {
            Configuration conf = new Configuration();
            conf.set("mapreduce.map.log.level", "DEBUG");
            Job job = Job.getInstance(conf, "Projet");
            job.setJarByClass(CasesMonthlyPerCountryJob.class);
            job.setMapperClass(CasesMonthlyPerCountryMapper.class);
            job.setReducerClass(CasesMonthlyPerCountryReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
            FileInputFormat.addInputPath(job, new Path(args[1]));
            FileOutputFormat.setOutputPath(job, new Path(args[2]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}