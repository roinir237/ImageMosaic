package uk.ac.uceenir.seqfiletoimagefile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.HashPartitioner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import uk.ac.ucl.devignette.NamedBytesWritable;

public class Main extends Configured implements Tool {

    public static final String JOB_NAME             = "SequanceFileToImageFile";

    public static final String OPENCV_LIB           = "libopencv_java246.so";

    String inPath;
    String outPath;
    String openCvLib;

    @Override
    public int run(String[] args) throws Exception {
        parseInput(args);

        Job job = Job.getInstance(getConf());
        Utils.cacheLocalFile(job, openCvLib, OPENCV_LIB);

        JobConf conf = new JobConf(job.getConfiguration(),this.getClass());

        conf.setJobName(JOB_NAME);

        conf.setReducerClass(SeqConverter.class);
        conf.setMapOutputKeyClass(IntWritable.class);
        conf.setMapOutputValueClass(NamedBytesWritable.class);
        conf.setInputFormat(SequenceFileInputFormat.class);
        conf.setOutputFormat(MultipleMatsOutputFormat.class);
        FileInputFormat.setInputPaths(conf, new Path(inPath));
        FileOutputFormat.setOutputPath(conf, new Path(outPath));

        JobClient.runJob(conf);

        return 0;
    }

    private void parseInput(String [] args) {
        inPath = args[0];
        outPath = args[1];
        openCvLib = args[2];
    }

    public static void main(String[] args) throws Exception{
        ToolRunner.run(new Configuration(), new Main(), args);
        System.out.println("EXIT OK");
    }
}
