/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.LinkedQueue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class FastCollinearPoints {

    private Point[] points;
    private final Point[] immutablePoints;
    private LineSegment[] segments;
    private LinkedQueue<LineSegment> tempSegments;
    private LinkedQueue<Point> minPoints;
    private LinkedQueue<Point> maxPoints;
    private double[] slopes;

    private Point currentMinPoint;
    private Point currentMaxPoint;


    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] originalPoints) {
        if (originalPoints == null) throw new IllegalArgumentException();

        tempSegments = new LinkedQueue<LineSegment>();
        minPoints = new LinkedQueue<Point>();
        maxPoints = new LinkedQueue<Point>();
        points = Arrays.copyOf(originalPoints, originalPoints.length);
        immutablePoints = Arrays.copyOf(originalPoints, originalPoints.length);
        slopes = new double[originalPoints.length];

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
        }

        Arrays.sort(points); // sort by y

        for (int i = 0; i < points.length; i++) {
            if (i > 0 && points[i].compareTo(points[i - 1]) == 0)
                throw new IllegalArgumentException();
        }

        Point maxPoint;
        Point minPoint;
        for (int i = 0; i < points.length; i++) {
            Comparator<Point> comparator = immutablePoints[i]
                    .slopeOrder();
            Arrays.sort(points); // sort by y
            Arrays.sort(points, comparator); // stable sort, preserve y ordering
            for (int a = 0; a < originalPoints.length; a++) {
                slopes[a] = immutablePoints[i].slopeTo(points[a]);
            }
            for (int j = 1; j < points.length - 2; j++) {
                if (slopes[j] == slopes[j + 1] && slopes[j + 1] == slopes[j + 2]) {
                    int k = 2;
                    // Points[j + k] is the highest point we could find
                    while ((j + k + 1) < points.length
                            && slopes[j] == slopes[j + k + 1]) {
                        k++;
                    }
                    int m = 0;
                    // Points[j - m] is the smallest point we could find
                    while ((j - m - 1) >= 0
                            && slopes[j] == slopes[j - m - 1]) {
                        m++;
                    }

                    if (immutablePoints[i].compareTo(points[j + k]) > 0)
                        maxPoint = immutablePoints[i];
                    else maxPoint = points[j + k];

                    if (immutablePoints[i].compareTo(points[j - m]) < 0)
                        minPoint = immutablePoints[i];
                    else minPoint = points[j - m];

                    boolean repeatedSegment = false;
                    Iterator minPointsIterator = minPoints.iterator();
                    Iterator maxPointsIterator = maxPoints.iterator();

                    for (int l = 0; l < tempSegments.size(); l++) {
                        currentMinPoint = (Point) minPointsIterator.next();
                        currentMaxPoint = (Point) maxPointsIterator.next();
                        if (minPoint.compareTo(currentMinPoint) == 0
                                && maxPoint.compareTo(currentMaxPoint) == 0) {
                            repeatedSegment = true;
                            break;
                        }
                    }
                    if (!repeatedSegment) {
                        minPoints.enqueue(minPoint);
                        maxPoints.enqueue(maxPoint);
                        tempSegments.enqueue(new LineSegment(minPoint, maxPoint));
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return tempSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        segments = new LineSegment[tempSegments.size()];
        Iterator lineSegmentIterator = tempSegments.iterator();
        for (int i = 0; i < tempSegments.size(); i++) {
            segments[i] = (LineSegment) lineSegmentIterator.next();
        }
        return segments;
    }
}
