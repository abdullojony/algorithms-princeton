import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points 
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty? 
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        // StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.01);
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> q = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                q.add(p);
            }
        }
        return q;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (points.isEmpty()) return null;
        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D q : points) {
            double distance = p.distanceSquaredTo(q);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = q;
            }
        }
        // StdDraw.setPenColor(StdDraw.GREEN);
        // StdDraw.setPenRadius(0.01);
        // p.draw();
        // StdDraw.setPenRadius();
        // p.drawTo(nearest);
        return nearest;
    }
 
    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSet = new PointSET();
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D point = new Point2D(x, y);
            pointSet.insert(point);
        }

        System.out.println("Number of points: " + pointSet.size());
        System.out.println("Is the set empty? " + pointSet.isEmpty());

        // Example usage of range and nearest methods
        RectHV rect = new RectHV(0.2, 0.2, 0.5, 0.5);
        System.out.println("Points in range " + rect + ":");
        for (Point2D p : pointSet.range(rect)) {
            System.out.println(p);
        }

        Point2D queryPoint = new Point2D(0.3, 0.3);
        Point2D nearestPoint = pointSet.nearest(queryPoint);
        System.out.println("Nearest point to " + queryPoint + ": " + nearestPoint);

        pointSet.draw();
    }
}
