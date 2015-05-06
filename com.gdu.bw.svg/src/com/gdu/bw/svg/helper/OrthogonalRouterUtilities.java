
package com.gdu.bw.svg.helper;

import com.gdu.bw.svg.model.Point;
import com.gdu.bw.svg.model.PointList;
import com.gdu.bw.svg.model.PositionConstants;

/**
 * @author <a>Davy Du</a>
 * 
 * @since 1.0.0
 */
public class OrthogonalRouterUtilities {

	public static void transformToOrthogonalPointList(PointList points,
			int offStartDirection, int offEndDirection) {
		if (points.size() > 1) {
			PointList startPoints = new PointList(points.size());
			PointList endPoints = new PointList(points.size());
			boolean isOffSourceDirectionSet = offStartDirection == PositionConstants.HORIZONTAL
					|| offStartDirection == PositionConstants.VERTICAL;
			boolean isOffTargetDirectionSet = offEndDirection == PositionConstants.VERTICAL
					|| offEndDirection == PositionConstants.HORIZONTAL;
			if (!isOffSourceDirectionSet && !isOffTargetDirectionSet) {
				/*
				 * If there is no off start and off end direction passed in,
				 * determine the off start direction.
				 */
				Point first = points.getPoint(0);
				Point second = points.getPoint(1);
				offStartDirection = Math.abs(first.x - second.x) < Math
						.abs(first.y - second.y) ? PositionConstants.HORIZONTAL
						: PositionConstants.VERTICAL;
				isOffSourceDirectionSet = true;
			}
			startPoints.addPoint(points.removePoint(0));
			endPoints.addPoint(points.removePoint(points.size() - 1));
			while (points.size() != 0) {
				if (isOffSourceDirectionSet) {
					Point nextPt = points.removePoint(0);
					Point lastStartPt = startPoints.getLastPoint();
					if (nextPt.x != lastStartPt.x && nextPt.y != lastStartPt.y) {
						/*
						 * If segment is not rectilinear insert a point to make
						 * it rectilinear
						 */
						if (offStartDirection == PositionConstants.VERTICAL) {
							startPoints.addPoint(new Point(lastStartPt.x,
									nextPt.y));
							offStartDirection = PositionConstants.HORIZONTAL;
						} else {
							startPoints.addPoint(new Point(nextPt.x,
									lastStartPt.y));
							offStartDirection = PositionConstants.VERTICAL;
						}
					} else {
						offStartDirection = nextPt.x == lastStartPt.x ? PositionConstants.VERTICAL
								: PositionConstants.HORIZONTAL;
					}
					startPoints.addPoint(nextPt);
				}
				if (isOffTargetDirectionSet && points.size() != 0) {
					Point nextPt = points.removePoint(points.size() - 1);
					Point firstEndPt = endPoints.getFirstPoint();
					if (nextPt.x != firstEndPt.x && nextPt.y != firstEndPt.y) {
						/*
						 * If segment is not rectilinear insert a point to make
						 * it rectilinear
						 */
						if (offEndDirection == PositionConstants.VERTICAL) {
							endPoints.insertPoint(new Point(firstEndPt.x,
									nextPt.y), 0);
							offEndDirection = PositionConstants.HORIZONTAL;
						} else {
							endPoints.insertPoint(new Point(nextPt.x,
									firstEndPt.y), 0);
							offEndDirection = PositionConstants.VERTICAL;
						}
					} else {
						offEndDirection = nextPt.x == firstEndPt.x ? PositionConstants.VERTICAL
								: PositionConstants.HORIZONTAL;
					}
					endPoints.insertPoint(nextPt, 0);
				}
			}
			/*
			 * Now we need to merge the two point lists such that the polyline
			 * formed by the points is still rectilinear. Hence there is a
			 * chance that one more point needs to be added.
			 */
			Point lastStartPt = startPoints.getLastPoint();
			Point firstEndPt = endPoints.getFirstPoint();
			if (lastStartPt.x != firstEndPt.x && lastStartPt.y != firstEndPt.y) {
				if ((!isOffSourceDirectionSet && isOffTargetDirectionSet)
						|| (isOffTargetDirectionSet && endPoints.size() < startPoints
								.size())) {
					if (offEndDirection == PositionConstants.VERTICAL) {
						startPoints.addPoint(new Point(firstEndPt.x,
								lastStartPt.y));
					} else {
						startPoints.addPoint(new Point(lastStartPt.x,
								firstEndPt.y));
					}
				} else if (offStartDirection == PositionConstants.VERTICAL) {
					startPoints
							.addPoint(new Point(lastStartPt.x, firstEndPt.y));
				} else {
					startPoints
							.addPoint(new Point(firstEndPt.x, lastStartPt.y));
				}
			}
			points.addAll(startPoints);
			points.addAll(endPoints);
		}
	}

	public static boolean isRectilinear(PointList points) {
		for (int i = 1; i < points.size(); i++) {
			Point currentPt = points.getPoint(i);
			Point previousPt = points.getPoint(i - 1);
			if (currentPt.x != previousPt.x && currentPt.y != previousPt.y) {
				return false;
			}
		}
		return true;
	}
}
