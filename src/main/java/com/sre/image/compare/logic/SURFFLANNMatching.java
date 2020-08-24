package com.sre.image.compare.logic;

import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SURFFLANNMatching {

    public void SURFFLANNMatching() {

    }

    public double ImageCompare(String fileName1, String fileName2) throws IOException {
        Mat img1 = Imgcodecs.imread(fileName1, Imgcodecs.IMREAD_GRAYSCALE);
        Mat img2 = Imgcodecs.imread(fileName2, Imgcodecs.IMREAD_GRAYSCALE);
        if (img1.empty() || img2.empty()) {
            System.err.println("Cannot read images!");
            System.exit(0);
        }
        ORB orb = ORB.create();
        double similarity = 0.0;
        int matched_points = 0;
        int tot_points = 0;
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
        orb.detect(img1, keypoints1);
        orb.detect(img2, keypoints2);

        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();
        orb.compute(img1, keypoints1, descriptors1);
        orb.compute(img2, keypoints2, descriptors2);

        if (descriptors1.cols() == descriptors2.cols()) {
            MatOfDMatch matchMatrix = new MatOfDMatch();
            DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
            matcher.match(descriptors1, descriptors2, matchMatrix);
            DMatch[] matches = matchMatrix.toArray();

            for (DMatch match : matches)
                if (match.distance <= 50)
                    matched_points++;//from   w ww  . j av a  2  s.  c om
            tot_points++;
        }
        similarity = matched_points / tot_points * 100;
        return similarity;

    }
}
