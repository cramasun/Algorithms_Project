/******************************************************************************
 *  Compilation:  javac SeamCarver.java
 *  Execution:    java SeamCarver input.png
 *  Dependencies: Picture.java Color.java Arrays.java
 *                
 *
 *  Takes a picture as an input. Calculates veritical and horizontal seams for
 *  the given picture. Also removes the horizontal and vetical seams from the
 *  current picture. 
 * 
 ******************************************************************************/
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private int[][] picColor;     // Save the picture in terms of color per pixel
    private int picHeight;          // Height of current picture
    private int picWidth;           // Width of current picture

    /* 
     * Create a seam carver object based on the given picture
     */
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Picture is NULL");
        picWidth = picture.width();
        picHeight = picture.height();
        picColor = new int[picHeight][picWidth];
        // Save the picture in terms of the color of all the pixels to avoid repeated
        // calls to the get() method of Picture
        for (int y = 0; y < picHeight; y++) {
            for (int x = 0; x < picWidth; x++) {
                picColor[y][x] = picture.getRGB(x, y);
            }
        }
    }

    /* 
     * Construct the current picture from the colors array and return it
     */
    public Picture picture() {
        Picture picture = new Picture(width(), height());
        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                picture.setRGB(x, y, picColor[y][x]);
            }
        }
        return picture;
    }

    /* 
     * Width of current picture
     */
    public int width() {
        return picWidth;
    }

    /* 
     * Height of current picture
     */
    public int height() {
        return picHeight;
    }

    /* 
     * Given two colors, calculate the color gradient between the two color
     * Gradient is the sum of the squares of the differences in the red, green, 
     * and blue components between the two colors
     * I.E: gradient = (C2.Red - C1.Red) ^ 2 + (C2.Green - C1.Green) ^ 2 + 
     *                 (C2.Blue - C1.Blue) ^ 2
     */
    private double calcColorGradient(int rgb1, int rgb2) {
        Color c1 = new Color(rgb1);
        Color c2 = new Color(rgb2);
        return Math.pow((c2.getRed() - c1.getRed()), 2) + 
               Math.pow((c2.getGreen() - c1.getGreen()), 2) + 
               Math.pow((c2.getBlue() - c1.getBlue()), 2);
    }

    /* 
     * Check if the co-ordinate passed is within the range of 
     * the current picture
     */ 
    private boolean isValidCoordinate(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) return false;
        return true;
    }

    /*
     * Energy of pixel at column x and row y
     */
    public double energy(int x, int y) {
        // Validate if the column and row passed are within width and 
        // height ranges of the current picture. 
        if (!isValidCoordinate(x, y)) {
            throw new IllegalArgumentException("Col or Row not within range");
        } 
        // Return fixed energy of 1000 for boundary rows and columns
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) return 1000;
        // Calculate the energy which is the square root of the sum of the 
        // X-gradient and Y-gradient of the pixel
        return Math.sqrt(calcColorGradient(picColor[y][x + 1], picColor[y][x - 1]) + 
                         calcColorGradient(picColor[y + 1][x], picColor[y - 1][x]));
    }

    /*
     * Transpose the matrix of colors. The method is used to
     * while finding or removing Hortizontal
     */
    private void transposeColorMatrix() {
        int[][] transposed = new int[width()][height()];
        for (int r = 0; r < width(); r++) {
            for (int c = 0; c < height(); c++) {
                transposed[r][c] = picColor[c][r];
            }
        }
        picColor = transposed;
        picHeight = transposed.length;
        picWidth = transposed[0].length;
    }

    /*
     * Relax the edge specified by (fromRow, fromCol) -> (toRow, toCol)
     */
    private void relax(double[][] energy, int fromRow, int fromCol, 
        int toRow, int toCol, double[][] distTo, int[][] edgeTo) 
    {
        if (!isValidCoordinate(toCol, toRow)) return;
        if (distTo[toRow][toCol] > (energy[toRow][toCol] + distTo[fromRow][fromCol])) {
            distTo[toRow][toCol] = (energy[toRow][toCol] + distTo[fromRow][fromCol]);
            edgeTo[toRow][toCol] = fromCol;
        }
    }

    /* Computes the shortest path from a sentinal source vertex above the 
     * first row and a sentinal destination vertex below the last row.
     *
     *******************
     * Graph Defination
     *******************
     * Vertices: Each pixel in the Energy 2D array is a vertex
     * Edges:  
     * 1. Sentinal Source vertex will have an edge to all pixels in first row
     * 2. Cells in the last row will all have an edge to sentinal dest vertex
     * 3. Boundary Cells will have 2 edges to pixel below and diagonally below it
     * 4. Non-Boundary Cells will have 3 edges to the pixel below and 2 pixels 
     *    diagonally below it
     */ 
    private int[] findShortestPath(double[][] energy) {
        // Shortest distance from sentinal source to each vertex
        double[][] distTo = new double[height()][width()];
        // Last edge along the shorest path from source to the vertex
        // Index is the vertex represented as row number and the edgeTO is 
        // represented as column number of the previous row
        int[][]edgeTo = new int[height()][width()];

        // For the first row, distTo is the energy of the pixel
        for (int c = 0; c < width(); c++) {
            distTo[0][c] = energy[0][c];
            edgeTo[0][c] = -1;
        }

        // Initialize distTo for Pixels in other rows to Infinity
        for (int r = 1; r < height(); r++) {
            Arrays.fill(distTo[r], Double.POSITIVE_INFINITY);
        }

        // Traversal Graph vertices in Toplogical Order and relax 
        // all the outgoing edges.
        // For every row R, pixels (vertices) have directed edges to 
        // pixels (vertices) in row R + 1. Hence traversal in row by row 
        // fashion is equivalent to traversing vertices in toplogical order
        for (int r = 0; r < height() - 1; r++) {
            for (int c = 0; c < width(); c++) {
                relax(energy, r, c, r + 1, c - 1, distTo, edgeTo);
                relax(energy, r, c, r + 1, c, distTo, edgeTo);
                relax(energy, r, c, r + 1, c + 1, distTo, edgeTo);
            }
        }

        // Relex the edges from pixels on the last row to the sentinal dest
        int edgeToDest = -1;
        double distToDest = Double.POSITIVE_INFINITY;
        for (int c = 0; c < width(); c++) {
            if (distToDest > distTo[height() - 1][c]) {
                distToDest = distTo[height() - 1][c];
                edgeToDest = c;
            }
        }

        int[] shortestPath = new int[height()];
        shortestPath[height() - 1] = edgeToDest;
        for (int r = height() - 1; r > 0; r--) {
            shortestPath[r - 1] = edgeTo[r][shortestPath[r]];
        }
        return shortestPath;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[height()][width()];
        // Compute the energy of all the Pixels in the 
        // picture
        for (int r = 0; r < energy.length; r++) {
            for (int c = 0; c < energy[r].length; c++) {
                energy[r][c] = energy(c, r);
            }
        }

        // The vertical seam is the shortest path from a sentinal source vertex 
        // above the first row and a sentinal destination vertex below the last row
        return findShortestPath(energy);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposeColorMatrix();
        int[] seam = findVerticalSeam();
        transposeColorMatrix();
        return seam;
    }

    private boolean isValidSeam(int[] seam, int dimension) {
        boolean valid = true;
        int last = seam[0];
        for (int s : seam) {
            if (s < 0 || s >= dimension || Math.abs(s - last) > 1) {
                valid = false;
                break;
            }
            last = s;
        }
        return valid;
    }

    /* 
     * Remove horizontal seam from current picture
     */
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Seam is NULL");
        if (height() <= 1) throw new IllegalArgumentException("Height of picture is <= 1");
        // System.out.println("Hor Seam Length = " + seam.length);
        if (seam.length != width() || !isValidSeam(seam, height())) {
            throw new IllegalArgumentException("Invalid Seam");
        }
        transposeColorMatrix();
        removeVerticalSeam(seam);
        transposeColorMatrix();
    }

    /*
     * Remove vertical seam from current picture
     */
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Seam is NULL");
        if (width() <= 1) throw new IllegalArgumentException("Width of picture is <= 1");
        // System.out.println("Vert Seam Length = " + seam.length);
        if (seam.length != height() || !isValidSeam(seam, width())) {
            throw new IllegalArgumentException("Invalid Seam");
        }
        int[][] newPicColor = new int[height()][width() - 1];
        for (int r = 0; r < height(); r++) {
            // Copy the color for pixels up to column to be deleted
            for (int c = 0; c < seam[r]; c++) {
                newPicColor[r][c] = picColor[r][c];
            }
            // Copy the color for the remianing pixels after 
            // the column to be deleted
            for (int c = seam[r] + 1; c < width(); c++) {
                newPicColor[r][c - 1] = picColor[r][c];
            }
        }
        picColor = newPicColor;
        picWidth--;
    }
}
