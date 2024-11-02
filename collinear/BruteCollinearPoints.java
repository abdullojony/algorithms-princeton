import java.util.List;
import java.util.ArrayList;

public class BruteCollinearPoints {
    private List<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument to BruteCollinearPoints is null");
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

            // find 4 collinear points
            for (int j = i + 1; j <= points.length - 3; j++) {
                Point start = points[i], end = points[i];
                if (points[j].compareTo(start) < 0) {
                    start = points[j];
                } else {
                    end = points[j];
                }
                
                double slope = start.slopeTo(end);
                int n = 2; // # of collinear points
                for (int k = j + 1; k < points.length; k++) {
                    if (points[j].slopeTo(points[k]) == slope) {
                        n++;
                        if (points[k].compareTo(start) < 0) {
                            start = points[k];
                        } else if (points[k].compareTo(end) > 0) {
                            end = points[k];
                        }
                        if (n == 4) {
                            segments.add(new LineSegment(start, end));
                            break;
                        }
                    }
                }
            }
        }
    }

    // returns number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // returns array of line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }
}
