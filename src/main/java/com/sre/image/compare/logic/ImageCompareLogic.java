package com.sre.image.compare.logic;

import nu.pattern.OpenCV;

import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static org.opencv.core.Core.norm;
import static org.opencv.core.Core.NORM_L2;

public class ImageCompareLogic
{
    public void ImageCompareLogic()
    {

    }
    public double ImageCompare(String fileName1, String fileName2) throws IOException
    {
        nu.pattern.OpenCV.loadLocally();
        // Load images to compare
        Mat img1 = Imgcodecs.imread(fileName1);
        Mat img2 = Imgcodecs.imread(fileName2);
        // Declare key point of images
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();
        // Definition of ORB key point detector and descriptor extractors
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        // Detect key points
        detector.detect(img1, keypoints1);
        detector.detect(img2, keypoints2);
        // Extract descriptors
        extractor.compute(img1, keypoints1, descriptors1);
        extractor.compute(img2, keypoints2, descriptors2);
        // Definition of descriptor matcher
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        MatOfDMatch good_matches = new MatOfDMatch();
        MatOfDMatch total_matches = new MatOfDMatch();
        LinkedList<MatOfDMatch> dmatchesListOfMat = new LinkedList<>();
        matcher.knnMatch(descriptors1, descriptors2, dmatchesListOfMat, DescriptorMatcher.BRUTEFORCE);

        LinkedList<DMatch> total_matchesList = new LinkedList<>();
        LinkedList<DMatch> good_matchesList = new LinkedList<>();
        for (int matchIndx = 0; matchIndx < dmatchesListOfMat.size() ; matchIndx++) {
            double ratio = 0.8;
            if (dmatchesListOfMat.get(matchIndx).toArray()[0].distance  < ratio * dmatchesListOfMat.get(matchIndx).toArray()[1].distance)
            {
                good_matchesList.addLast(dmatchesListOfMat.get(matchIndx).toArray()[0]);
            }
            total_matchesList.addLast(dmatchesListOfMat.get(matchIndx).toArray()[0]);
            System.out.println(dmatchesListOfMat.get(matchIndx).toArray()[0].distance);
            System.out.println(dmatchesListOfMat.get(matchIndx).toArray()[1].distance);
        }
        good_matches.fromList(good_matchesList);
        total_matches.fromList(total_matchesList);
        double dist_percentage = 100*good_matchesList.size()/total_matchesList.size();
        // Match points of two images
        /*MatOfDMatch matches = new MatOfDMatch();
        //  System.out.println("Type of Image1= " + descriptors1.type() + ", Type of Image2= " + descriptors2.type());
        //  System.out.println("Cols of Image1= " + descriptors1.cols() + ", Cols of Image2= " + descriptors2.cols());
        int dist_changes = 0;
        int dist_size = 0;
        int retVal = 0;
        // Avoid to assertion failed
        // Assertion failed (type == src2.type() && src1.cols == src2.cols && (type == CV_32F || type == CV_8U)
        if (descriptors2.cols() == descriptors1.cols()) {
            matcher.match(descriptors1, descriptors2 ,matches);

            // Check matches of key points
            DMatch[] match = matches.toArray();
            double max_dist = 0; double min_dist = 100;

            for (int i = 0; i < descriptors1.rows(); i++) {
                double dist = match[i].distance;
                if( dist < min_dist ) min_dist = dist;
                if( dist > max_dist ) max_dist = dist;
            }
            System.out.println("max_dist=" + max_dist + ", min_dist=" + min_dist);

            // Extract good images (distances are under 10)
            for (int i = 0; i < descriptors1.rows(); i++) {
                if (match[i].distance <= 10) {
                    retVal++;
                }
            }
            System.out.println("matching count=" + retVal);
        }
        //double dist_percentage = dist_changes*100/dist_size;*/

        return dist_percentage;
    }
}
