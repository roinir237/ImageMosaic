package uk.ac.uceenir.pyramidconfigbuilder;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String pathToPositions = "/Users/roinir/Projects/HadoopMosaicScaler/runnable/positions.csv";
        //String pathToPositions = "/Users/roinir/Projects/HadoopMosaicScaler/runnable/test_positions.csv";

        try {
            ImagePyramidBuilder builder = new ImagePyramidBuilder(pathToPositions);
            ImagePyramid pyramid = builder.build();
            pyramid.toMapFile("/Users/roinir/Projects/HadoopMosaicScaler/runnable/pyramid.conf");
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
