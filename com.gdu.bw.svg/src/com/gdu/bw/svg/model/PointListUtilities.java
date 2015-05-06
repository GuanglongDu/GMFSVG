
package com.gdu.bw.svg.model;




/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class PointListUtilities {


    /**
     * Removes redudant points from the point list.
     * 
     * @param points the <code>PointList</code> which will be modified to remove redudant points
     * @param straightLineTolerance the tolerance value within which indicates the line is straight
     * @return <code>boolean</code> <code>true</code> if segments were modified, <code>false</code> otherwise.
     */
    static boolean flattenSegments(PointList points, int straightLineTolerance) {
        boolean changed = false;
        
        for (int i = 0; i < points.size() - 1; i++) {
            Point ptStart = points.getPoint(i);
    
            Point ptTerm = null;
            if (i + 1 < points.size())
                ptTerm = points.getPoint(i + 1);
    
            Point ptNext = null;
            if (i + 2 < points.size())
                ptNext = points.getPoint(i + 2);
    
            // We conservatively avoid changing the endpoint of the line, since it may then
            // fail to attach to the terminal view.  This implies ptNext must be non-NIL.
            if (points.size() <= 2 || ptTerm == null || ptNext == null) {
                return changed;
            }
    
            if (sameOrientation(ptStart, ptTerm, ptNext, straightLineTolerance)) {
                removePoint(points, i + 1);
                
                Ray seg = new Ray(ptStart, ptTerm);
                if (Math.abs(seg.y) < straightLineTolerance) {
                    points.setPoint(new Point(ptNext.x, ptStart.y), i + 1);
                } else if (Math.abs(seg.x) < straightLineTolerance){
                    points.setPoint(new Point(ptStart.x, ptNext.y), i + 1);
                }
                
                changed = true;
            }
        }
        
        return changed;                 
    }
        
    
    static private Point removePoint(PointList points, int index) {
        Point removedPt = points.getPoint(index);
        for (int i=index; i<points.size()-1; i++) {
            points.setPoint(points.getPoint(i+1), i);
        }
        points.setSize(points.size() - 1);
        return removedPt;
    }
    
    /**
     * Normalizes the line segments in the polyline.  Checks for lines that are with-in a threshold for length
     * and removes them.  Additionally, it will remove points that that adjacent segments that are both
     * horizontal, or both vertical.
     * 
     * @param points <code>PointList</code> to be normalized
     * @return <code>boolean</code> <code>true</code> if segments were changed, <code>false</code> otherwise
     */
    static public boolean normalizeSegments(PointList points) {
        return normalizeSegments(points, 0);
    }
    
    /**
     * Normalizes the line segments in the polyline.  Checks for lines that are with-in a threshold for length
     * and removes them.  Additionally, it will remove points that that adjacent segments that are both
     * horizontal, or both vertical.  Will utilize a tolerance value to determine if segments needs to flattened
     * or not.
     * 
     * @param points <code>PointList</code> to normalize
     * @param straightLineTolerance the tolerance value within which indicates the line is straight in
     * relative coordinates.
     * @return <code>boolean</code> <code>true</code> if segments were changed, <code>false</code> otherwise
     */
    static public boolean normalizeSegments(PointList points, int straightLineTolerance) {
        
        boolean hasChanged = false;

        // first pass to remove points with-in the length tolerance
        for (int i = 1; i < points.size() - 1; i++) {
            Point pt1 = points.getPoint(i);
            Point pt2 = points.getPoint(i - 1);
            double diffX = pt1.preciseX() - pt2.preciseX();
            double diffY = pt1.preciseY() - pt2.preciseY();
            double nextLength = Math.sqrt(diffX * diffX + diffY * diffY);

            if (nextLength <= straightLineTolerance) {
                points.removePoint(i--);
                hasChanged = true;
            }
        }
        
        // second pass to flatten segments that are parallel to each other with-in a tolerance value
        hasChanged |= flattenSegments(points, straightLineTolerance);
        
        // recursively normalize the points if something has changed.
        if (hasChanged)
            normalizeSegments(points, straightLineTolerance);
        
        return hasChanged;
    }

    /**
     * Method getPointsSupremum.
     * Get points representing the extrema for this poly line.
     * 
     * @param points PointList to calculate the highest point from.
     * @return Point value of the highest point in the bounding box of the
     *      polyline.
     */
    static public Point getPointsSupremum(PointList points) {
        Point theSupremum = points.getFirstPoint();
        for (int i = 1; i < points.size(); i++) {
            Point other = points.getPoint(i);
            theSupremum =
                new Point(
                    Math.max(theSupremum.x, other.x),
                    Math.max(theSupremum.y, other.y));
        }

        return theSupremum;
    }

    /**
     * Method getPointsSupremum.
     * Get points representing the minimum for this poly line.
     * 
     * @param points PointList to calculate the minimum point from.
     * @return Point value of the lowest point in the bounding box of the
     *      polyline.
     */
    static public Point getPointsInfimum(PointList points) {
        Point theInfimum = points.getFirstPoint();
        for (int i = 1; i < points.size(); i++) {
            Point other = points.getPoint(i);
            theInfimum =
                new Point(
                    Math.min(theInfimum.x, other.x),
                    Math.min(theInfimum.y, other.y));
        }

        return theInfimum;
    }

    /**
     * createPointsFromRect
     * 
     * @param rBox Rectangle to base the PointList from
     * @return PointList that is equivalent to the Rectangle
     */
    static public PointList createPointsFromRect(Rectangle rBox) {
        PointList points = new PointList(5);
        Point pt = new Point(rBox.getLeft().x, rBox.getTop().y);
        points.addPoint(pt);
        pt = new Point(rBox.getRight().x, rBox.getTop().y);
        points.addPoint(pt);
        pt = new Point(rBox.getRight().x, rBox.getBottom().y);
        points.addPoint(pt);
        pt = new Point(rBox.getLeft().x, rBox.getBottom().y);
        points.addPoint(pt);
        pt = new Point(rBox.getLeft().x, rBox.getTop().y);
        points.addPoint(pt);

        return points;
    }
   
    static boolean sameOrientation(Point pt1, Point pt2, Point pt3, int straightLineTolerance) {
        LineSeg line = new LineSeg(pt1, pt3);
        Point pt = line.perpIntersect(pt2.x, pt2.y);
        return Math.round(pt.getDistance(new Point(pt2.x, pt2.y))) < straightLineTolerance;
    }
    
    public static Point pickClosestPoint(PointList points, Point p) {
        Point result = null;
        if (points.size() != 0) {
            result = points.getFirstPoint();
            for (int i=1; i<points.size(); i++) {
                Point temp = points.getPoint(i);
                if (Math.abs(temp.x - p.x) < Math.abs(result.x - p.x))
                    result = temp;
                else if (Math.abs(temp.y - p.y) < Math.abs(result.y - p.y))
                    result = temp;
            }
        }
        return result;
    }
}
