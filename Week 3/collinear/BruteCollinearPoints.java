/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {

    private int numberOfSegments;
    private LineSegment[] segments;
    private LineSegment[] tempSegments;
    private Point[] points;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] originalPoints) {
        if (originalPoints == null) throw new IllegalArgumentException();

        numberOfSegments = 0;
        tempSegments = new LineSegment[originalPoints.length];
        this.points = Arrays.copyOf(originalPoints, originalPoints.length);

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
        }

        Arrays.sort(points);

        for (int i = 0; i < points.length; i++) {
            if (i > 0 && points[i].compareTo(points[i - 1]) == 0)
                throw new IllegalArgumentException();
        }

        // brute force
        Comparator<Point> comparator;
        for (int i = 0; i < points.length - 3; i++) {
            comparator = points[i].slopeOrder();
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        if (comparator.compare(points[j], points[k]) == 0
                                && comparator.compare(points[k], points[l]) == 0) {
                            tempSegments[numberOfSegments++] = new LineSegment(points[i],
                                                                               points[l]);
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        segments = new LineSegment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            segments[i] = tempSegments[i];
        }
        return segments;
    }
}

