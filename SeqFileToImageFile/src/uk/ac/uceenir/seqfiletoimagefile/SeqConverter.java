package uk.ac.uceenir.seqfiletoimagefile;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import uk.ac.ucl.devignette.NamedBytesWritable;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by roinir on 26/06/2014.
 */
public class SeqConverter extends MapReduceBase implements Reducer<IntWritable, NamedBytesWritable,Text,MatWritable> {

    @Override
    public void configure(JobConf job) {
        super.configure(job);
        try {
            File libFile = new File(Utils.getPathToCachedFile(job, new Path(Main.OPENCV_LIB).getName()).toString());
            System.load(libFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } catch (UnsatisfiedLinkError e){
            throw e;
        }

    }
    @Override
    public void reduce(IntWritable intWritable, Iterator<NamedBytesWritable> namedBytesWritableIterator, OutputCollector<Text, MatWritable> output, Reporter reporter) throws IOException {
        while (namedBytesWritableIterator.hasNext()){
            NamedBytesWritable val = namedBytesWritableIterator.next();
            String name = val.mText.toString();
            MatWritable mat = new MatWritable();
            mat.setMat(val.copyBytes());
            output.collect(new Text(name),mat);
        }
    }
}
