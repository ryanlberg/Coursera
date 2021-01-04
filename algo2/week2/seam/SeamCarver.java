/* *****************************************************************************
 *  Name: Ryan Berg
 *  Date: 01/01/21
 *  Description: Seam Carver as described at https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {

    private Picture seamcarved;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.seamcarved = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return this.seamcarved;
    }

    // width of current picture
    public int width() {
        return this.seamcarved.width();
    }

    // height of current picture
    public int height() {
        return this.seamcarved.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || y >= this.seamcarved.width() || x >= this.seamcarved.height()) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || x == this.height() - 1 || y == 0 || y == this.height() - 1) {
            return 1000;
        }
        Color l = seamcarved.get(x, y - 1);
        Color r = seamcarved.get(x, y + 1);
        Color u = seamcarved.get(x - 1, y);
        Color d = seamcarved.get(x + 1, y);
        int[] left = getRGB(l);
        int[] right = getRGB(r);
        int[] up = getRGB(u);
        int[] down = getRGB(d);
        double upDown = getDelta(down, up);
        double leftRight = getDelta(right, left);
        return Math.sqrt(upDown + leftRight);


    }

    private int[] getRGB(Color c) {
        return new int[]{c.getRed(), c.getGreen(), c.getBlue()};
    }

    private double getDelta(int[] from, int[] to) {
        double redDif = Math.pow(from[0] - to[0], 2);
        double greenDif = Math.pow(from[1] - to[1], 2);
        double blueDif = Math.pow(from[2] - to[2], 2);
        return redDif + greenDif + blueDif;
    }

    private static class SeamNode {
        private final double total;
        private SeamNode from;
        private int id;

        public SeamNode(double total, SeamNode from, int row) {
            this.total = total;
            this.from = from;
            this.id = row;
        }

        public SeamNode getFrom() {
            return this.from;
        }

        public double getTotal() {
            return this.total;
        }

        private int getId() {
            return this.id;
        }
    }

    private boolean isValid(int i, int j) {
        return i >= 0 && i < seamcarved.height() && j >= 0 && j < seamcarved.width();
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        SeamNode[][] seam = new SeamNode[seamcarved.height()][seamcarved.width()];
        for (int i = 0; i < height(); i++) {
            seam[i][0] = new SeamNode(1000, null, i);
        }

        for (int j = 1; j < width(); j++) {
            for (int i = 0; i < height(); i++) {
                double curmin = Double.MAX_VALUE;
                double curEnergy = energy(i, j);
                SeamNode from = null;
                int row = 0;
                for (int x = -1; x < 2; x += 1) {
                    if (isValid(i + x, j - 1)) {
                        double curvalue = curEnergy + seam[i + x][j - x].getTotal();
                        if (curvalue < curmin) {
                            curmin = curvalue;
                            from = seam[i + x][j - 1];
                            row = i - x;

                        }
                    }
                    seam[i][j] = new SeamNode(curmin, from, row);
                }
            }

        }
        return getSeam(seam, 0);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        SeamNode[][] seam = new SeamNode[seamcarved.height()][seamcarved.width()];
        for (int i = 0; i < width(); i++) {
            seam[0][i] = new SeamNode(1000, null, i);
        }
        for (int i = 1; i < seam.length; i++) {
            for (int j = 0; j < seam[0].length; j++) {
                double curmin = Double.MAX_VALUE;
                double curEnergy = energy(i, j);
                SeamNode from = null;
                int row = 0;
                for (int x = -1; x < 2; x += 1) {
                    if (isValid(i - 1, j + x)) {
                        double curvalue = curEnergy + seam[i - 1][j + x].getTotal();
                        if (curvalue < curmin) {
                            curmin = curvalue;
                            from = seam[i - 1][j + x];
                            row = j - x;

                        }
                    }
                    seam[i][j] = new SeamNode(curmin, from, row);
                }
            }
        }
        return getSeam(seam, 1);
    }

    private int[] getSeam(SeamNode[][] seamNodes, int type) {
        int[] out;
        SeamNode minseen = new SeamNode(Double.MAX_VALUE, null, 0);
        if (type == 1) {
            out = new int[height()];
            int bottom = height() - 1;
            for (int i = 0; i < width(); i++) {
                SeamNode curseen = seamNodes[bottom][i];
                if (curseen.getTotal() < minseen.getTotal()) {
                    minseen = curseen;
                }
            }

            while (minseen != null) {
                out[bottom] = minseen.getId();
                minseen = minseen.getFrom();
            }
        } else {
            out = new int[width()];
            int right = width() - 1;
            for (int i = 0; i < height(); i++) {
                SeamNode curseen = seamNodes[i][right];
                if (curseen.getTotal() < minseen.getTotal()) {
                    minseen = curseen;
                }
            }
            while (minseen != null) {
                out[right] = minseen.getId();
                minseen = minseen.getFrom();
            }
        }
        return out;
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != this.seamcarved.width()) {
            throw new IllegalArgumentException();
        }

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != this.seamcarved.height()) {
            throw new IllegalArgumentException();
        }

    }

    public static void main(String[] args) {

    }
}
