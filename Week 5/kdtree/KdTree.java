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

public class KdTree {

    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private static final boolean VERTICAL = true; // level % 2 = 0
    private static final boolean HORIZONTAL = false; // level % 2 = 1

    private Node root;

    private class Node {
        private Point2D point;
        private boolean colour, direction;
        private Node left, right;
        private int size;

        public Node(Point2D point, boolean direction, int size) {
            this.point = point;
            this.direction = direction;
            this.size = size;
        }

        public int size(Node node) {
            if (node == null) return 0;
            return node.size;
        }

        public void insert(Point2D p) {
            if (p == null) throw new IllegalArgumentException();
            root = insert(root, p);
        }

        private Node insert(Node node, Point2D p) {
            if (node.point.x() == p.x() && node.point.y() == p.y()) return node;

            if (node.direction == VERTICAL && p.x() <= node.point.x()) {
                if (node.left == null) node.left = new Node(p, !node.direction, 1);
                else node.left = insert(node.left, p);
            }
            else if (node.direction == VERTICAL && p.x() > node.point.x()) {
                if (node.right == null) node.right = new Node(p, !node.direction, 1);
                else node.right = insert(node.right, p);
            }
            else if (node.direction == HORIZONTAL && p.y() <= node.point.y()) {
                if (node.left == null) node.left = new Node(p, !node.direction, 1);
                else node.left = insert(node.left, p);
            }
            else if (node.direction == HORIZONTAL && p.y() > node.point.y()) {
                if (node.right == null) node.right = new Node(p, !node.direction, 1);
                else node.right = insert(node.right, p);
            }

            node.size = 1 + size(node.left) + size(node.right);
            return node;
        }

        public boolean contains(Node node, Point2D p) {
            if (node.point.x() == p.x() && node.point.y() == p.y()) return true;

            if (node.direction == VERTICAL && p.x() <= node.point.x()) {
                if (node.left == null) return false;
                else return contains(node.left, p);
            }
            else if (node.direction == VERTICAL && p.x() > node.point.x()) {
                if (node.right == null) return false;
                else return contains(node.right, p);
            }
            else if (node.direction == HORIZONTAL && p.y() <= node.point.y()) {
                if (node.left == null) return false;
                else return contains(node.left, p);
            }
            else if (node.direction == HORIZONTAL && p.y() > node.point.y()) {
                if (node.right == null) return false;
                else return contains(node.right, p);
            }

            System.out.println("contains() failed");
            return false;
        }

        public LinkedList<Point2D> range(Node node, RectHV rect) {
            LinkedList<Point2D> list = new LinkedList<Point2D>();
            if (node == null) return list;
            RectHV firstRect, secondRect;
            if (node.direction == VERTICAL) {
                firstRect = new RectHV(0.0, 0.0, node.point.x(), 1.0);
                secondRect = new RectHV(node.point.x(), 0.0, 1.0, 1.0);
            }
            else { // (node.direction == HORIZONTAL)
                firstRect = new RectHV(0.0, 0.0, 1.0, node.point.y());
                secondRect = new RectHV(0.0, node.point.y(), 1.0, 1.0);
            }
            if (rect.intersects(firstRect)) {
                for (Point2D p : range(node.left, rect)) {
                    list.add(p);
                }
            }
            if (rect.intersects(secondRect)) {
                for (Point2D p : range(node.right, rect)) {
                    list.add(p);
                }
            }
            if (rect.contains(node.point)) list.add(node.point);

            return list;
        }

        public Point2D nearest(Node node, Point2D p, Double dist) {
            if (node.point.distanceTo(p) < dist) {
                dist = node.point.distanceTo(p);
            }
            Point2D nearest = node.point;
            Point2D temp;
            if (node.direction == VERTICAL) {
                if (p.x() <= node.point.x()) { // point on the left
                    if (node.left != null) {
                        temp = nearest(node.left, p, dist);
                        if (temp.distanceTo(p) < dist) {
                            dist = temp.distanceTo(p);
                            nearest = temp;
                        }
                    }
                    if (dist > (node.point.x() - p.x()) && node.right
                            != null) { // could be a nearer point on the right and right != null
                        temp = nearest(node.right, p, dist);
                        if (temp.distanceTo(p) < dist) {
                            dist = temp.distanceTo(p);
                            nearest = temp;
                        }
                    }
                }
                else { // point on the right
                    if (node.right != null) {
                        temp = nearest(node.right, p, dist);
                        if (temp.distanceTo(p) < dist) {
                            dist = temp.distanceTo(p);
                            nearest = temp;
                        }
                    }
                    if (dist > (p.x() - node.point.x()) && node.left
                            != null) { // could be a nearer point on the left and left != null
                        temp = nearest(node.left, p, dist);
                        if (temp.distanceTo(p) < dist) {
                            dist = temp.distanceTo(p);
                            nearest = temp;
                        }
                    }
                }
            }
            else { // HORIZONTAL
                if (p.y() <= node.point.y()) { // point below
                    if (node.left != null) {
                        temp = nearest(node.left, p, dist);
                        if (temp.distanceTo(p) < dist) {
                            dist = temp.distanceTo(p);
                            nearest = temp;
                        }
                    }
                    if (dist > (node.point.y() - p.y()) && node.right
                            != null) { // could be a nearer point on top and right != null
                        temp = nearest(node.right, p, dist);
                        if (temp.distanceTo(p) < dist) {
                            dist = temp.distanceTo(p);
                            nearest = temp;
                        }
                    }
                }
                else { // point on top
                    if (node.right != null) {
                        temp = nearest(node.right, p, dist);
                        if (temp.distanceTo(p) < dist) {
                            dist = temp.distanceTo(p);
                            nearest = temp;
                        }
                    }
                    if (dist > (p.y() - node.point.y()) && node.left
                            != null) { // could be a nearer point on the bottom and left != null
                        temp = nearest(node.left, p, dist);
                        if (temp.distanceTo(p) < dist) {
                            dist = temp.distanceTo(p);
                            nearest = temp;
                        }
                    }
                }
            }
            return nearest;
        }

        public void draw(Node node) {
            if (node.left != null) {
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                StdDraw.point(node.left.point.x(), node.left.point.y());
                if (node.left.direction == VERTICAL) { // vertical line below current
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.setPenRadius(0.002);
                    StdDraw.line(node.left.point.x(), 0.0, node.left.point.x(), node.point.y());
                }
                else { // horizontal line to the left of current
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.setPenRadius(0.002);
                    StdDraw.line(0.0, node.left.point.y(), node.point.x(), node.left.point.y());
                }
                draw(node.left);
            }
            if (node.right != null) {
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                StdDraw.point(node.right.point.x(), node.right.point.y());
                if (node.right.direction == VERTICAL) { // vertical line above current
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.setPenRadius(0.002);
                    StdDraw.line(node.right.point.x(), node.point.y(), node.right.point.x(), 1.0);
                }
                else { // horizontal line to the right of current
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.setPenRadius(0.002);
                    StdDraw.line(node.point.x(), node.right.point.y(), 1.0, node.right.point.y());
                }
                draw(node.right);
            }
        }
    }

    public KdTree() {
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        if (root == null) root = new Node(point, VERTICAL, 1);
        root.insert(point);
    }

    // is the set empty?
    public boolean isEmpty() {
        return (root == null);
    }

    // number of points in the set
    public int size() {
        if (root == null) return 0;
        return root.size;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return false;
        return root.contains(root, p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(root.point.x(), root.point.y());
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.002);
        StdDraw.line(root.point.x(), 0.0, root.point.x(), 1.0);
        root.draw(root);
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (root == null) return new LinkedList<Point2D>();
        return root.range(root, rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (root == null) return null;
        return root.nearest(root, p, 1.0);
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        System.out.println("1 (x) (y) -> Insert new point");
        System.out.println("2 (x) (y) -> Contains");
        System.out.println("3 (minX) (minY) (maxX) (maxY) -> Range");
        System.out.println("4 (x) (y) -> Nearest");
        System.out.println("5 -> Quit");
        System.out.println("6 -> Draw");
        int input;
        while (true) {
            input = StdIn.readInt();
            if (input == 1) kdTree.insert(new Point2D(StdIn.readDouble(), StdIn.readDouble()));
            if (input == 2) System.out.println(
                    kdTree.contains(new Point2D(StdIn.readDouble(), StdIn.readDouble())));
            if (input == 3) System.out.println(kdTree.range(
                    new RectHV(StdIn.readDouble(), StdIn.readDouble(), StdIn.readDouble(),
                               StdIn.readDouble())));
            if (input == 4)
                System.out.println(
                        kdTree.nearest(new Point2D(StdIn.readDouble(), StdIn.readDouble())));
            if (input == 5) break;
            if (input == 6) kdTree.draw();
        }
    }
}
