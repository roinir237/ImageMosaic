package com.uceenir.mosaicscaler;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import java.awt.*;

public class ImageTile {
    final private int cornerX;
    final private int cornerY;
    final private String fileRef;
    final private int width = 2448;
    final private int height = 2050;

    /**
     * ImageTile constructor
     * @param x the x coordinate of the top left corner of the tile in the frame of the mosaic
     * @param y the y coordinate of the top left corner of the tile in the frame of the mosaic.
     * @param fileRef the absolute path to the image file.
     */
    public ImageTile(int x, int y, String fileRef) {
        this.cornerX = x;
        this.cornerY = y;
        this.fileRef = fileRef;
    }

    /**
     *
     * @return the y coordinate of the top left corner of the tile in the mosaic's frame
     */
    public int getY() {
        return cornerY;
    }

    /**
     *
     * @return the x coordinate of the top left corner of the tile in the mosaic's frame
     */
    public int getX() {
        return cornerX;
    }

    /**
     *
     * @return the absolute path to the image file on disk.
     */
    public String getFileRef() {
        return fileRef;
    }

    /**
     * Checks if this segment of the image is in an area in coordinate space defined by range.
     * @param range defines the part of the whole image of interest
     * @return true if range and the area defined by the segment intersect. False otherwise.
     */
    public boolean isInRange(Rectangle range) {
        return range.intersects(this.toRectangle());
    }

    /**
     * returns the portion of the image contained within the viewport
     * @param viewport
     * @return the portion of the whole image contained within viewport. Returns null if the image is not contained
     * within the viewport.
     */
    public Mat getSegmentContainedWithin (Rectangle viewport, Rectangle destRect) {
        Rectangle imageRect = this.toRectangle();
        Rectangle roi = viewport.intersection(imageRect);

        if (!roi.isEmpty()) {
            destRect.setBounds(roi);
            destRect.setLocation(roi.x - viewport.x, roi.y - viewport.y);
            // re-express the roi such that (0,0) is the image segments top left corner.
            roi.setLocation(roi.x - imageRect.x, roi.y - imageRect.y);

            Mat m = getImage();
            return m.submat(roi.y, roi.y + roi.height, roi.x, roi.x + roi.width);
        }

        return null;
    }

    public Mat getImage() {

        return Highgui.imread(this.fileRef);
    }

    private Rectangle toRectangle() {
        return new Rectangle(cornerX,cornerY,width,height);
    }
}
