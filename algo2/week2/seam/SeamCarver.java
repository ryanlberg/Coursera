/* *****************************************************************************
 *  Name: Ryan Berg
 *  Date: 01/01/21
 *  Description: Seam Carver as described at https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture seamcarved;
    private int[][] toBePicture;
    private double[][] energies;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.toBePicture = new int[picture.height()][picture.width()];
        this.energies = new double[picture.height()][picture.width()];
        this.seamcarved = new Picture(picture);
        for (int i = 0; i < this.seamcarved.height(); i++) {
            for (int j = 0; j < this.seamcarved.width(); j++) {
                toBePicture[i][j] = picture.getRGB(j, i);
                energies[i][j] = energy(j, i);
            }
        }
    }

    // current picture
    public Picture picture() {
        this.seamcarved = new Picture(width(), height());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                this.seamcarved.setRGB(j, i, this.toBePicture[i][j]);
            }
        }
        return this.seamcarved;
    }

    // width of current picture
    public int width() {
        if (this.energies == null) {
            throw new NullPointerException();
        }
        return this.energies[0].length;
    }

    // height of current picture
    public int height() {
        if (this.energies == null) {
            throw new NullPointerException();
        }
        return this.energies.length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        int tempx = y;
        y = x;
        x = tempx;
        if (x < 0 || y < 0 || x > height() - 1 || y > width() - 1) {
            throw new IllegalArgumentException();
        }
        if (this.energies[x][y] > 0) {
            return this.energies[x][y];
        }
        if (x == 0 || x == height() - 1 || y == 0 || y == width() - 1) {
            return 1000;
        }
        int l = seamcarved.getRGB(y - 1, x);
        int r = seamcarved.getRGB(y + 1, x);
        int u = seamcarved.getRGB(y, x - 1);
        int d = seamcarved.getRGB(y, x + 1);
        int[] left = getRGB(l);
        int[] right = getRGB(r);
        int[] up = getRGB(u);
        int[] down = getRGB(d);
        double upDown = getDelta(down, up);
        double leftRight = getDelta(right, left);
        return Math.sqrt(upDown + leftRight);


    }

    private int[] getRGB(int c) {
        return new int[]{(c >> 16) & 0xFF, (c >> 8) & 0xFF, c & 0xFF};
    }

    private double getDelta(int[] from, int[] to) {
        double redDif = Math.pow(from[0] - to[0], 2);
        double greenDif = Math.pow(from[1] - to[1], 2);
        double blueDif = Math.pow(from[2] - to[2], 2);
        return redDif + greenDif + blueDif;
    }

    private static class SeamNode {
        private final double total;
        private final SeamNode from;
        private final int id;

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
        return i >= 0 && i < height() && j >= 0 && j < width();
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        SeamNode[][] seam = new SeamNode[height()][width()];
        for (int i = 0; i < height(); i++) {
            seam[i][0] = new SeamNode(1000, null, i);
        }

        for (int j = 1; j < width(); j++) {
            for (int i = 0; i < height(); i++) {
                double curmin = Double.MAX_VALUE;
                double curEnergy = this.energies[i][j];
                SeamNode from = null;
                int row = 0;
                for (int x = -1; x < 2; x += 1) {
                    if (isValid(i + x, j - 1)) {
                        double curvalue = curEnergy + seam[i + x][j - 1].getTotal();
                        if (curvalue < curmin) {
                            curmin = curvalue;
                            from = seam[i + x][j - 1];
                            row = i;

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
        SeamNode[][] seam = new SeamNode[height()][width()];
        for (int i = 0; i < width(); i++) {
            seam[0][i] = new SeamNode(1000, null, i);
        }
        for (int i = 1; i < seam.length; i++) {
            for (int j = 0; j < seam[0].length; j++) {
                double curmin = Double.MAX_VALUE;
                double curEnergy = this.energies[i][j];
                SeamNode from = null;
                int row = 0;
                for (int x = -1; x < 2; x += 1) {
                    if (isValid(i - 1, j + x)) {
                        double curvalue = curEnergy + seam[i - 1][j + x].getTotal();
                        if (curvalue < curmin) {
                            curmin = curvalue;
                            from = seam[i - 1][j + x];
                            row = j;

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
                bottom -= 1;
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
                right -= 1;
            }
        }
        return out;
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width()) {
            throw new IllegalArgumentException();
        }
        int seamToSee = 0;
        int newi = 0;
        int newj = 0;
        double[][] newEnergies = new double[height() - 1][width()];
        int[][] newToBePicture = new int[height() - 1][width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                if (j != seam[seamToSee]) {
                    newEnergies[newj][newi] = this.energies[j][i];
                    newToBePicture[newj][newi] = this.toBePicture[j][i];
                    newj++;
                }
            }
            newi++;
            newj = 0;
            seamToSee++;


        }
        this.toBePicture = newToBePicture;
        this.energies = newEnergies;

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height()) {
            throw new IllegalArgumentException();
        }
        int seamToSee = 0;
        int newi = 0;
        int newj = 0;
        double[][] newEnergies = new double[height()][width() - 1];
        int[][] newToBePicture = new int[height()][width() - 1];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                if (j != seam[seamToSee]) {
                    newEnergies[newi][newj] = this.energies[i][j];
                    newToBePicture[newi][newj] = this.toBePicture[i][j];
                    newj++;
                }
            }
            newi++;
            newj = 0;
            seamToSee++;

        }
        this.energies = newEnergies;
        this.toBePicture = newToBePicture;

    }

    public static void main(String[] args) {

    }
}
