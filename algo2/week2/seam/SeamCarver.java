/* *****************************************************************************
 *  Name: Ryan Berg
 *  Date: 01/01/21
 *  Description: Seam Carver as described at https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture seamcarved;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.seamcarved = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return this.seamcarved;
    }

    // width of current picture
    public int width() {
    }

    // height of current picture
    public int height() {

    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    public static void main(String[] args) {

    }
}
