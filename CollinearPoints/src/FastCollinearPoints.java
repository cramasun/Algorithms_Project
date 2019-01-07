import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> ls;
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        Point[] inPoints = validateInput(points);
        Point[] sp = inPoints.clone();
  
        ls = new ArrayList<LineSegment>();
        
        for (int i = 0; i < inPoints.length; i++) {
            int collinearPoints = 1;
            Point min = inPoints[i];
            /* Create Slope order comparator wrt ith point */
            Comparator<Point> soComp = inPoints[i].slopeOrder();
            /* Sort the cloned array based on slope order.
             * Since slope of a point with itself is negative infinity, 
             * inPoints[i] will be first element in sorted array.
             */
            Arrays.sort(sp, soComp);
            for (int j = 1; j < sp.length; j++) {
                /* When the slope (wrt ith point) of jth point is different from 
                 * the suceeding point, there are no more collinear points
                 */
                if (soComp.compare(sp[j], sp[j-1]) != 0) {                
                    if (collinearPoints >= 3 && (inPoints[i].compareTo(min) == 0)) {
                        Arrays.sort(sp, (j - collinearPoints), j);
                        ls.add(new LineSegment(inPoints[i], sp[j-1]));
                    }
                    collinearPoints = 1;
                    min = inPoints[i];
                }
                else {
                    collinearPoints++;
                }
                if (sp[j].compareTo(min) < 0) {
                    min = sp[j];
                }
            }
            /* When 3 or more points adjacent points have equal slope 
             * wrt to ith point and ith point is the smallest in the set of 
             * collinear points, then add the line segment.
             */
            if (collinearPoints >= 3 && (inPoints[i].compareTo(min) == 0)) {
                Arrays.sort(sp, (sp.length - collinearPoints), sp.length);
                ls.add(new LineSegment(inPoints[i], sp[sp.length -1]));
            }
        }
    }
    // the number of line segments
    public int numberOfSegments() {
        return ls.size();
    }
    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[ls.size()];
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