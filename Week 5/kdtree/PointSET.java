/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> bst;

    // construct an empty set of points
    public PointSET() {
        bst = new TreeSet<Point2D>();

    }

    // is the set empty?
    public boolean isEmpty() {
        return bst.isEmpty();
    }

    // number of points in the set
    public int size() {
        return bst.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        bst.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return bst.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenRadius(0.01);
        for (Point2D point : bst) {
            StdDraw.point(point.x(), point.y());
        }

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> points = new LinkedList<Point2D>();
        for (Point2D point : bst) {
            if (rect.contains(point)) points.add(point);
        }

        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (bst.isEmpty()) return null;
        Point2D minPoint = bst.first();
        double minDistance = p.distanceTo(minPoint);
        for (Point2D point : bst) {
            if (p.distanceTo(point) < minDistance) {
                minPoint = point;
                minDistance = p.distanceTo(minPoint);
            }
        }
        return minPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();
        System.out.println("1 (x) (y) -> Insert new point");
        System.out.println("2 (x) (y) -> Contains");
        System.out.println("3 (minX) (minY) (maxX) (maxY) -> Range");
        System.out.println("4 (x) (y) -> Nearest");
        System.out.println("5 -> Quit");
        System.out.println("6 -> Draw");
        while (true) {
            int input;
            input = StdIn.readInt();
            if (input == 1) pointSET.insert(new Point2D(StdIn.readDouble(), StdIn.readDouble()));
            if (input == 2) System.out.println(
                    pointSET.contains(new Point2D(StdIn.readDouble(), StdIn.readDouble())));
            if (input == 3) System.out.println(pointSET.range(
                    new RectHV(StdIn.readDouble(), StdIn.readDouble(), StdIn.readDouble(),
                               StdIn.readDouble())));
            if (input == 4) System.out.println(
                    pointSET.nearest(new Point2D(StdIn.readDouble(), StdIn.readDouble())));
            if (input == 5) break;
            if (input == 6) pointSET.draw();
        }
    }
}
