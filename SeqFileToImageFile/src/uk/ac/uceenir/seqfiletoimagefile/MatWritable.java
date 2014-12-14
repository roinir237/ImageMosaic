package uk.ac.uceenir.seqfiletoimagefile;

import org.apache.hadoop.io.Writable;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class MatWritable implements Writable {

    private MatOfByte matOfByte;

    public MatWritable(){

    }

    public MatWritable(Mat mat) throws IOException{
        setMat(mat);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        byte [] buff = matOfByte.toArray();
        dataOutput.writeInt(buff.length);
        dataOutput.write(buff);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        int buffSize = dataInput.readInt();
        if (buffSize <= 0) throw new IOException("buffer size: "+buffSize);
        byte [] buff = new byte [buffSize];
        for(int i = 0; i < buffSize; i++){
            buff[i] = dataInput.readByte();
        }
        matOfByte = new MatOfByte(buff);
    }

    public byte [] toByte() {
        return matOfByte.toArray();
    }

    public Mat toMat() {
        return Highgui.imdecode(matOfByte,Highgui.CV_LOAD_IMAGE_COLOR);
    }

    public void setMat(Mat mat) throws IOException{
        if (mat.empty()) throw new IOException("Cannot set empty Mat");
        matOfByte = new MatOfByte();
        Highgui.imencode(".jpg",mat,matOfByte);
    }

    public void setMat(byte[] contents){
        matOfByte = new MatOfByte(contents);
    }
}
