package com.sre.image.compare.domains;


import com.opencsv.bean.CsvBindByName;

public class CompareProp {
    @CsvBindByName
    private String image1;
    @CsvBindByName
    private String image2;
    @CsvBindByName
    private double similarity;
    @CsvBindByName
    private double elapsed;

    private boolean isrowerror;

    public CompareProp() {
    }

    public CompareProp(String image1, String image2)
    {//, double similarity, int elapsed) {
        this.image1 = image1;
        this.image2 = image2;
        //this.similarity = similarity;
        //this.elapsed = elapsed;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public double getElapsed() {
        return elapsed;
    }

    public void setElapsed(double elapsed) {
        this.elapsed = elapsed;
    }

    public boolean getIsRowError() {
        return isrowerror;
    }

    public void setIsRowError(boolean isrowerror) {
        this.isrowerror = isrowerror;
    }
}