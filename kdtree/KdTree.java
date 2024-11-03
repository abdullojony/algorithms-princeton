import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private Point2D point;
        private Node left;
        private Node right;

        public Node(Point2D point) {
            this.point = point;
        }

        public boolean equals(Object that) {
            if (that == this) return true;
            if (that == null) return false;
            if (that.getClass() != this.getClass()) return false;
            Node thatNode = (Node) that;
            return this.point.equals(thatNode.point);
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public Point2D getPoint() {
            return point;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public void setPoint(Point2D point) {
            this.point = point;
        }
    }

    // construct an empty set of points 
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty? 
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insertHelper(root, p, true);
    }

    private Node insertHelper(Node node, Point2D p, boolean isVertical) {
        if (node == null) {
            size++;
            return new Node(p);
        }

        if (node.getPoint().equals(p)) return node;

        int cmp = comparePoints(p, node.getPoint(), isVertical);
        if (cmp < 0) {
            node.setLeft(insertHelper(node.getLeft(), p, !isVertical));
        } else {
            node.setRight(insertHelper(node.getRight(), p, !isVertical));
        }

        return node;
    }

    private int comparePoints(Point2D p1, Point2D p2, boolean isVertical) {
        if (isVertical) {
            return Double.compare(p1.x(), p2.x());
        } else {
            return Double.compare(p1.y(), p2.y());
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return containsHelper(root, p, true);
    }

    private boolean containsHelper(Node node, Point2D p, boolean isVertical) {
        if (node == null) return false;
        if (node.getPoint().equals(p)) return true;

        int cmp = comparePoints(p, node.getPoint(), isVertical);
        if (cmp < 0) {
            return containsHelper(node.getLeft(), p, !isVertical);
        } else {
            return containsHelper(node.getRight(), p, !isVertical);
        }
    }

    // draw all points to standard draw
    public void draw() {
        drawHelper(root, 0, 0, 1, 1, true);
    }

    private void drawHelper(Node node, double minx, double miny, double maxx, double maxy, boolean isVertical) {
        if (node == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.getPoint().draw();
        StdDraw.setPenRadius();


        // Draw the line
        if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.getPoint().x(), miny, node.getPoint().x(), maxy);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(minx, node.getPoint().y(), maxx, node.getPoint().y());
        }

        if (isVertical) {
            drawHelper(node.getLeft(), minx, miny, node.getPoint().x(), maxy, !isVertical);
            drawHelper(node.getRight(), node.getPoint().x(), miny, maxx, maxy, !isVertical);
        } else {
            drawHelper(node.getLeft(), minx, miny, maxx, node.getPoint().y(), !isVertical);
            drawHelper(node.getRight(), minx, node.getPoint().y(), maxx, maxy, !isVertical);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> points = new ArrayList<>();
        rangeHelper(root, rect, points, true);
        return points;
    }

    private void rangeHelper(Node node, RectHV rect, List<Point2D> points, boolean isVertical) {
        if (node == null) return;

        if (rect.contains(node.getPoint())) {
            points.add(node.getPoint());
        }

        if (isVertical) {
            if (node.getPoint().x() > rect.xmax()) {
                rangeHelper(node.getLeft(), rect, points, !isVertical);
            } else if (node.getPoint().x() < rect.xmin()) {
                rangeHelper(node.getRight(), rect, points, !isVertical);
            } else {
                rangeHelper(node.getLeft(), rect, points, !isVertical);
                rangeHelper(node.getRight(), rect, points, !isVertical);
            }
        } else {
            if (node.getPoint().y() > rect.ymax()) {
                rangeHelper(node.getLeft(), rect, points, !isVertical);
            } else if (node.getPoint().y() < rect.ymin()) {
                rangeHelper(node.getRight(), rect, points, !isVertical);
            } else {
                rangeHelper(node.getLeft(), rect, points, !isVertical);
                rangeHelper(node.getRight(), rect, points, !isVertical);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        // StdDraw.setPenColor(StdDraw.GREEN);
        Point2D nearest = nearestHelper(root, p, root.getPoint(), true);
        // StdDraw.setPenRadius(0.01);
        // p.draw();
        // StdDraw.setPenColor(StdDraw.YELLOW);
        // StdDraw.setPenRadius();
        // p.drawTo(nearest);
        return nearest;
    }

    private Point2D nearestHelper(Node node, Point2D queryPoint, Point2D nearestPoint, boolean isVertical) {
        if (node == null) return nearestPoint;

        double nearestDistance = nearestPoint.distanceSquaredTo(queryPoint);
        double currentDistance = node.getPoint().distanceSquaredTo(queryPoint);
        if (currentDistance < nearestDistance) {
            nearestPoint = node.getPoint();
            nearestDistance = currentDistance;
        }

        // node.getPoint().drawTo(queryPoint);

        Point2D leftNearest = null;
        Point2D rightNearest = null;
        int cmp = comparePoints(queryPoint, node.getPoint(), isVertical);
        if (cmp < 0) {
            leftNearest = nearestHelper(node.getLeft(), queryPoint, nearestPoint, !isVertical);
            double leftDistance = leftNearest.distanceSquaredTo(queryPoint);
            double x = node.getPoint().x();
            double y = node.getPoint().y();
            double dx = x + (queryPoint.x() - x);
            double dy = y + (queryPoint.y() - y);
            Point2D minPoint = new Point2D(isVertical ? x : dx, isVertical ? dy : y);
            double minDistance = minPoint.distanceSquaredTo(queryPoint);
            if (minDistance < leftDistance) {
                rightNearest = nearestHelper(node.getRight(), queryPoint, nearestPoint, !isVertical);
                double rightDistance = rightNearest.distanceSquaredTo(queryPoint);
                if (rightDistance < leftDistance) {
                    if (rightDistance < nearestDistance) nearestPoint = rightNearest;
                } else {
                    if (leftDistance < nearestDistance) nearestPoint = leftNearest;
                }
            }
        } else {
            rightNearest = nearestHelper(node.getRight(), queryPoint, nearestPoint, !isVertical);
            double rightDistance = rightNearest.distanceSquaredTo(queryPoint);
            double x = node.getPoint().x();
            double y = node.getPoint().y();
            double dx = x + (queryPoint.x() - x);
            double dy = y + (queryPoint.y() - y);
            Point2D minPoint = new Point2D(isVertical ? x : dx, isVertical ? dy : y);
            double minDistance = minPoint.distanceSquaredTo(queryPoint);
            if (minDistance < rightDistance) {
                leftNearest = nearestHelper(node.getLeft(), queryPoint, nearestPoint, !isVertical);
                double leftDistance = leftNearest.distanceSquaredTo(queryPoint);
                if (leftDistance < rightDistance) {
                    if (leftDistance < nearestDistance) nearestPoint = leftNearest;
                } else {
                    if (rightDistance < nearestDistance) nearestPoint = rightNearest;
                }
            }
        }

        return nearestPoint;
    }
 
    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree pointSet = new KdTree();
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
