# README #

### What is this repository for? ###

A utility to create an 'image pyramid' out of a set of initial overlapping image tiles and a positions file using Hadoop. 

### Dependencies ###

* openCV

### Input specifications ###

* Image tiles in a generic image format (e.g 000001.png,000002.png etc) all contained in a single dir.
* CSV file containing the positions and dimension of the each image tile in the format: 'tile name, x coord, y coord, width, height'. 


### Usage ###

```
#!bash
$ hadoop jar {path to HadoopMosaicScaler.jar} \ 
-libjars {path to openCV jar (e.g opencv-***.jar)} \
-positions {path to positions (default: positions.csv)} \
-outdir {path to HDFS out dir} \
-opencv {path to opencv lib (e.g: libopencv_java***.so)} \
-tilesDir {path to dir containing the initial image tiles} \
-imageFormat {(optional) the format of the final image pyramid tiles (default: jpg)} \
-config {(optional) output path for pyramid config file (default: pyramid.conf)}  \
-tilesSize {(optional) comma seperated width,height of tiles (default: 600,600)} \
-scalingStep {(optional) step between scalings (default: 2)} \
-scaleBounds {(optional) min,max scale bounds. If max = -1 the program will automatically calculate the best max scale (default: 1,-1)} \
-centered {(optional) boolean value indicating whether the tiles should be centered (default: true)} \
{(optional) other mapred options}
```

### Example usage on AWB ###

```
#!bash
hadoop jar HadoopMosaicScaler.jar \
-libjars /home/ewillman/lib/opencv-246.jar \
-Dmapreduce.map.memory.mb=8000 \
-Dmapreduce.map.java.opts=-Xmx3000M \
-Dmapreduce.reduce.memory.mb=8000 \
-Dmapreduce.reduce.java.opts=-Xmx3000M \
-Dmapreduce.input.fileinputformat.split.maxsize=80000000 \
-imageFormat jpg \
-config pyramid.conf \
-positions /home/rnir/mosaic/positions.csv \
-outdir out600x600 \
-opencv /home/ewillman/lib/libopencv_java246.so \
-tilesDir /user/rnir/converter/out \
-tilesSize 600,600 \
-scalingStep 2 \
-scaleBounds 16,-1 \
-centered true
```