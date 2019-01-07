import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> ls;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        int numPoints;
        double slopeQ, slopeR, slopeS; 
              
        Point[] ps = validateInput(points);
        
        ls = new ArrayList<LineSegment>();
        numPoints = ps.length;
        
        for (int p = 0; p < (numPoints - 3); p++) {
            for (int q = p+1; q < (numPoints - 2); q++) {
                slopeQ = ps[p].slopeTo(ps[q]);
                for (int r = q+1; r < (numPoints - 1); r++) {
                    slopeR = ps[p].slopeTo(ps[r]);
                    /* Slope of 1st point w.r.t 2nd is not equal to the slope
                     * w.r.t 3rd point. Hence, first 3 points are not co-linear
                     * and there is no point checking the 4th point. 
                     */
                    if (Double.compare(slopeQ, slopeR) != 0) {
                        continue;
                    }
                    for (int s = r+1; s < numPoints; s++) {
                        slopeS = ps[p].slopeTo(ps[s]);
                        /* Slope of 1st point wrt 2nd, 3rd and 4th points 
                         * are the same. Hence the 4 points are collienar.
                         * Add the points to the line segment array
                         */
                        if (Double.compare(slopeS, slopeR) == 0) {
                             ls.add(new LineSegment(ps[p], ps[s]));
                        }
                    }
                }
            }
        }       
    }
    
    // the number of line segments
    public int numberOfSegments() {
        return ls.size();
    }
    
    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segments =  new LineSegment[ls.size()];
        segments = ls.toArray(segments);
        return segments;
    }
    
    /* Helper function to validate the input point array
     * Ensure that points array and all points in the array are not null.
     * Also, clone the input array and retun the sorted and validated array.
     */
    private Point[] validateInput(Point[] points) {
        Point[] ps;
        
        if (points == null) {
            throw new java.lang.IllegalArgumentException("Point Array is NULL");
        }
        ps = points.clone();
        for (Point point : ps) {
            if (point == null) {
                throw new java.lang.IllegalArgumentException("Point NULL");
            }
        }
        /* Sort the input array according to natural order. Sorting makes 
         * it easier to identify duplicate poinst as well as to find 
         * start and end points in the line segment associated with 
         * collienar points.
         */
        Arrays.sort(ps);
        for (int i = 0; i < (ps.length - 1); i++) {
            if (ps[i].compareTo(ps[i+1]) == 0) {
                throw new java.lang.IllegalArgumentException("Duplicate points");
            }
        }
        
        return ps;
    }
}