package com.uceenir.mosaicscaler;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MosaicScaler{
    static String pathToPositions;
    static String pathToTiles;
    static String pathToResult;
    static int centerX;
    static int centerY;
    static int width;
    static int height;
    static double scale;

    // TO-DO: unhandled exceptions...
    public static void main (String [] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        parseInput(args);

        try {
            Mat scaledImage = new ImageScaler(pathToPositions, pathToTiles).getImage(centerX, centerY, width, height, scale);
            Highgui.imwrite(pathToResult,scaledImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TO-DO: make this actually parse the inputs and not just serve static data?
    private static void parseInput(String [] args) {
        pathToPositions = "/Users/roinir/Projects/sheykhData/positions.csv";
        pathToTiles = "/Users/roinir/Projects/sheykhData/100x120-sheykh_pic/";
        pathToResult = "/Users/roinir/Projects/MosaicScaler/data/sheik.jpg";
        centerX = 63200;
        centerY = 34400;
        width = 300;
        height = 300;
        scale = 10;
    }

}
