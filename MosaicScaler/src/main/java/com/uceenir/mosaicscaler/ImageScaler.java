package com.uceenir.mosaicscaler;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class ImageScaler {
    private int type;
    ArrayList<ImageTile> tiles;

    /**
     * ImageScaler constructor.
     *
     * @param srcFile   The path to the data file. The file must be of the format: "nameOfImageTileFile, topLeftX, topLeftY"
     *                  where topLeftX and topLeftY are the (integer) x,y coordinates of the top left corner of the tile in the
     *                  frame of the mosaic, and nameOfImageTileFile is the name of the image file (e.g nameOfImageTileFile = "0001.jpg")
     * @param pathToTiles  The absolute path of to the folder containing the tiles.
     * @throws FileNotFoundException    In the case the data file is not found (srcFile is wrong)
     * @throws IOException  In case there's a problem reading one of the lines in the file, or if one of the lines doesn't
     *                      have the right number of CSVs.
     */
    public ImageScaler(String srcFile, String pathToTiles) throws FileNotFoundException, IOException {
        parseDataFile(srcFile, pathToTiles);
        type = getTilesImageType();
    }

    /**
     * Returns the image by assembling the set of tiles. The size of the final image is defined by the width and height params.
     * The "magnification" is set by the scale params (e.g scale = 2 zooms out by a factor of 2 and scale = 0.5 zooms in
     * by a factor of 2). The image is centered on (centerX,centerY) relative to the whole image.
     * @param centerX
     * @param centerY
     * @param width
     * @param height
     * @param scale
     * @return
     */
    public Mat getImage(int centerX, int centerY, int width, int height, double scale) {
        Rectangle rawRange = new Rectangle((int) (centerX-width*scale/2),(int) (centerY-height*scale/2),(int) (width*scale),(int) (height*scale));
        Mat image = new Mat(height,width, this.type);
        // To shrink an image, it will generally look best with CV_INTER_AREA interpolation, whereas to enlarge an image,
        // it will generally look best with CV_INTER_CUBIC (slow) or CV_INTER_LINEAR (faster but still looks OK) [openCV docs]
        int inter = scale > 1 ? Imgproc.INTER_AREA:Imgproc.INTER_LINEAR;

        for (ImageTile tile : tiles) {
            if (tile.isInRange(rawRange)) {
                // get the segment of the tile in the region
                Rectangle roi = new Rectangle();
                Mat rawSeg = tile.getSegmentContainedWithin(rawRange, roi);
                // rescale the segment
                Mat scaledSeg = new Mat();
                Imgproc.resize(rawSeg, scaledSeg, scaledSeg.size(), 1.0 / scale, 1.0 / scale, inter);
                // place it in the right location in the final image

                roi.setSize((int) Math.round(roi.width * 1.0 / scale), (int) Math.round(roi.height * 1.0 / scale));
                roi.setLocation((int) Math.floor(roi.x * 1.0 / scale), (int) Math.floor(roi.y * 1.0 / scale));
                System.out.printf("x = %d, x2 = %d, y = %d, y2 = %d \n",roi.x, roi.x+scaledSeg.cols(),roi.y, roi.y +scaledSeg.rows());
                System.out.printf("width = %d, height = %d \n",scaledSeg.cols(), scaledSeg.rows());

                scaledSeg.copyTo(image.submat(roi.y, roi.y + scaledSeg.rows(), roi.x, roi.x + scaledSeg.cols()));
            }
        }

        return image;
    }

    /**
     * Parses the data files and populates the list of ImageTiles.
     *
     * @param srcFile   The path to the data file. The file must be of the format: "nameOfImageTileFile, topLeftX, topLeftY"
     *                  where topLeftX and topLeftY are the (integer) x,y coordinates of the top left corner of the tile in the
     *                  frame of the mosaic, and nameOfImageTileFile is the name of the image file (e.g nameOfImageTileFile = "0001.jpg")
     * @param tileRoot  The absolute path of to the folder containing the tiles.
     * @throws FileNotFoundException    In the case the data file is not found (srcFile is wrong)
     * @throws IOException  In case there's a problem reading one of the lines in the file, or if one of the lines doesn't
     *                      have the right number of CSVs.
     */
    private void parseDataFile (String srcFile, String tileRoot) throws FileNotFoundException, IOException{
        tiles = new ArrayList<ImageTile>();

        FileInputStream fstream = new FileInputStream(srcFile);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        final String delimiter = ",";
        String strLine;
        while ((strLine = br.readLine()) != null)   {
            String [] data = strLine.split(delimiter);
            tiles.add(new ImageTile((int) Float.parseFloat(data[1]), (int) Float.parseFloat(data[2]),tileRoot + data[0]));
        }

        in.close();
    }

    /**
     * Assuming all of the tiles have the same type, this function returns the type of the first tile image.
     * @return the type of the image tile or -1 if no tiles are loaded.
     */
    private int getTilesImageType() {
        if(tiles != null && tiles.size() > 0) return tiles.get(0).getImage().type();
        else return -1;
    }
}
