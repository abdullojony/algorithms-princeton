import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private List<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument to FastCollinearPoints is null");
        }

        // check for null points
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Input contains null points");
            }
        }

        segments = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            // check for duplicates
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Input contains duplicate points");
                }
            }

            // make a copy of points
            Point[] copy = Arrays.copyOf(points, points.length);

            // swap i'th element with 0'th element
            Point tmp = copy[i];
            copy[i] = copy[0];
            copy[0] = tmp;

            // sort elements from 1 -> end according to 0'th element slopeOrder
            Arrays.sort(copy, 1, copy.length, copy[0].slopeOrder());

            Point start = copy[0], end = copy[0];
            double slope = Double.MAX_VALUE;
            int n = 1; // # of collinear points
            for (int j = 1; j < copy.length; j++) {
                double sl = start.slopeTo(copy[j]);
                if (slope == Double.MAX_VALUE) {
                    slope = sl;
                    n++;
                } else if (slope == sl) {
                    n++;
                } else {
                    if (n > 3) {
                        segments.add(new LineSegment(start, end));
                    }
                    slope = sl;
                    end = start;
                    n = 2;
                }

                if (copy[j].compareTo(start) < 0) {
                    j++;
                    while (j < copy.length && start.slopeTo(copy[j]) == slope) {
                        j++;
                    }
                    j--;
                    slope = Double.MAX_VALUE;
                    end = start;
                    n = 1;
                } else if (copy[j].compareTo(end) > 0) {
                    end = copy[j];
                }
            }

            if (n > 3) {
                segments.add(new LineSegment(start, end));
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }
}
