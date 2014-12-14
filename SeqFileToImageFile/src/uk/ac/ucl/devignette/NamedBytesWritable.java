package uk.ac.ucl.devignette;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**A class with binary data and a text field*/
public class NamedBytesWritable extends BytesWritable { //implements Writable{

    //public BytesWritable mData;
    public Text mText = null;

    public NamedBytesWritable() {
        super();
        mText = new Text();
    }

    public NamedBytesWritable(BytesWritable bw) {
        super();
        mText = new Text();
        this.set(bw);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        super.readFields(in);
        mText.readFields(in);
        //mData.readFields(in);
    }
    @Override
    public void write(DataOutput out) throws IOException {
        super.write(out);
        mText.write(out);
        //mData.write(out);
    }
}