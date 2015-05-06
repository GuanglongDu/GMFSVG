
package com.gdu.bw.svg.helper;

import java.util.ArrayList;
import java.util.HashMap;

import com.gdu.bw.svg.model.BWNode;
import com.gdu.bw.svg.model.Dimension;
import com.gdu.bw.svg.model.LineSeg;
import com.gdu.bw.svg.model.Point;
import com.gdu.bw.svg.model.PointList;
import com.gdu.bw.svg.model.PositionConstants;
import com.gdu.bw.svg.model.PrecisionPoint;
import com.gdu.bw.svg.model.PrecisionRectangle;
import com.gdu.bw.svg.model.Rectangle;
import com.gdu.bw.svg.model.figure.SVGFigure;
import com.gdu.bw.svg.model.figure.SequenceFlowFigure;

/**
 * @author <a>Davy Du</a>
 * 
 * @since 1.0.0
 */
public class ObliqueRouter {

	static public class ArrayListMap {
		private HashMap<Object, Object> map = new HashMap<Object, Object>();

		public ArrayListMap() {
			super();
		}

		public ArrayList<Object> get(Object key) {
			Object value = map.get(key);
			if (value == null)
				return null;

			if (value instanceof ArrayList)
				return (ArrayList<Object>) value;
			ArrayList<Object> v = new ArrayList<Object>(1);
			v.add(value);
			return v;
		}

		public void put(Object key, Object value) {
			Object arrayListObject = map.get(key);
			if (arrayListObject == null) {
				map.put(key, value);
				return;
			}
			if (arrayListObject instanceof ArrayList) {
				ArrayList<Object> arrayList = (ArrayList<Object>) arrayListObject;
				if (!arrayList.contains(value))
					arrayList.add(value);
				return;
			}
			if (arrayListObject != value) {
				ArrayList<Object> arrayList = new ArrayList<Object>(2);
				arrayList.add(arrayListObject);
				arrayList.add(value);
				map.put(key, arrayList);
			}
		}

		public void remove(Object key, Object value) {
			Object arrayListObject = map.get(key);
			if (arrayListObject != null) {
				if (arrayListObject instanceof ArrayList) {
					ArrayList arrayList = (ArrayList) arrayListObject;
					arrayList.remove(value);
					if (arrayList.isEmpty())
						map.remove(key);
					return;
				}
				map.remove(key);
			}
		}

		public int size() {
			return map.size();
		}
	}

	private ArrayListMap selfRelConnections = new ArrayListMap();
	private ArrayListMap intersectingShapesConnections = new ArrayListMap();
	protected static final int SELFRELSIZEINIT = 62;
	protected static final int SELFRELSIZEINCR = 10;

	public boolean checkShapesIntersect(SVGFigure source, SVGFigure target,
			PointList newLine) {
		if (source == null || target == null) {
			return false;
		}
		if (newLine.size() < 3) {
			PrecisionRectangle sourceBounds = getShapeBounds(source);
			PrecisionRectangle targetBounds = getShapeBounds(target);
			sourceBounds = source.translateToAbsolute(sourceBounds);
			targetBounds = target.translateToAbsolute(targetBounds);
			if (sourceBounds.intersects(targetBounds)
					&& !sourceBounds.contains(targetBounds)
					&& !targetBounds.contains(sourceBounds)
					|| sourceBounds.equals(targetBounds)) {
				getVerticesForIntersectingShapes(source, target, newLine);
				return true;
			}
		} else {
			SequenceFlowFigure conn = new SequenceFlowFigure(source, target);
			removeIntersectingShapesConnection(conn);
		}
		return false;
	}

	private void removeIntersectingShapesConnection(SequenceFlowFigure conn) {
		if (conn.getSource() == null || conn.getTarget() == null
				|| conn.getSource().getNode() == null
				|| conn.getTarget().getNode() == null)
			return;
		Object key = getIntersectingShapesConnectionKey(conn.getSource()
				.getNode(), conn.getTarget().getNode());
		ArrayList<Object> connectionList = intersectingShapesConnections
				.get(key);
		if (connectionList != null) {
			int index = connectionList.indexOf(conn);
			if (index == -1)
				return;
			intersectingShapesConnections.remove(key, conn);
		}
	}

	public void getVerticesForIntersectingShapes(SVGFigure source,
			SVGFigure target, PointList newLine) {
		SequenceFlowFigure conn = new SequenceFlowFigure(source, target);
		Object key = getIntersectingShapesConnectionKey(source.getNode(),
				target.getNode());
		int nSelfIncr = 0;
		int nIndex = 0;
		ArrayList<Object> connectionList = intersectingShapesConnections
				.get(key);
		if (connectionList != null) {
			if (!connectionList.contains(conn)) {
				intersectingShapesConnections.put(key, conn);
				connectionList = intersectingShapesConnections.get(key);
			}

			nIndex = connectionList.indexOf(conn);
			assert nIndex >= 0;
		} else {
			intersectingShapesConnections.put(key, conn);
		}

		PrecisionPoint selfrelsizeincr = new PrecisionPoint(SELFRELSIZEINCR, 0);
		// boolean isFeedbackConn =
		// BWRouterHelper.getInstance().isFeedback(conn);
		SVGFigure sourceFig = conn.getSource();
		PrecisionRectangle sourceRect = getShapeBounds(sourceFig);
		sourceFig.translateToAbsolute(sourceRect);
		;

		SVGFigure targetFig = conn.getTarget();
		PrecisionRectangle targetRect = getShapeBounds(targetFig);
		targetFig.translateToAbsolute(targetRect);
		PrecisionRectangle union = sourceRect.getPreciseCopy()
				.union(targetRect);

		nSelfIncr = selfrelsizeincr.x * (nIndex);

		Rectangle intersection = sourceRect.getCopy().intersect(targetRect);

		Rectangle connArea = new Rectangle();
		int position = PositionConstants.NONE;
		int minArea = 0;
		Point unionTopLeft = union.getTopLeft();
		Point unionTopRight = union.getTopRight();
		Point unionBottomRight = union.getBottomRight();
		Point unionBottomLeft = union.getBottomLeft();

		if (!unionTopLeft.equals(sourceRect.getTopLeft())
				&& !unionTopLeft.equals(targetRect.getTopLeft())) {
			Dimension diffVector = unionTopLeft.getDifference(intersection
					.getTopLeft());
			absDimension(diffVector);
			int areaTopLeft = diffVector.getArea();
			if (minArea == 0 || minArea > areaTopLeft) {
				position = PositionConstants.NORTH_WEST;
				connArea.setSize(diffVector);
				connArea.setLocation(unionTopLeft.x, unionTopLeft.y);
				minArea = areaTopLeft;
			}
		}

		if (!unionTopRight.equals(sourceRect.getTopRight())
				&& !unionTopRight.equals(targetRect.getTopRight())) {
			Dimension diffVector = unionTopRight.getDifference(intersection
					.getTopRight());
			absDimension(diffVector);
			int areaTopRight = diffVector.getArea();
			if (minArea == 0 || minArea > areaTopRight) {
				position = PositionConstants.NORTH_EAST;
				connArea.setSize(diffVector);
				connArea.setLocation(unionTopRight.x - connArea.width,
						unionTopRight.y);
				minArea = areaTopRight;
			}
		}

		if (!unionBottomRight.equals(sourceRect.getBottomRight())
				&& !unionBottomRight.equals(targetRect.getBottomRight())) {
			Dimension diffVector = unionBottomRight.getDifference(intersection
					.getBottomRight());
			absDimension(diffVector);
			int areaBottomRight = diffVector.getArea();
			if (minArea == 0 || minArea > areaBottomRight) {
				position = PositionConstants.SOUTH_EAST;
				connArea.setSize(diffVector);
				connArea.setLocation(unionBottomRight.x - connArea.width,
						unionBottomRight.y - connArea.height);
				minArea = areaBottomRight;
			}
		}

		if (!unionBottomLeft.equals(sourceRect.getBottomLeft())
				&& !unionBottomLeft.equals(targetRect.getBottomLeft())) {
			Dimension diffVector = unionBottomLeft.getDifference(intersection
					.getBottomLeft());
			absDimension(diffVector);
			int areaBottomLeft = diffVector.getArea();
			if (minArea == 0 || minArea > areaBottomLeft) {
				position = PositionConstants.SOUTH_WEST;
				connArea.setSize(diffVector);
				connArea.setLocation(unionBottomLeft.x, unionBottomLeft.y
						- connArea.height);
				minArea = areaBottomLeft;
			}
		}

		if (position == PositionConstants.NONE) {
			connArea = intersection;
		}

		int sourcePosition = getSourcePositionFromConnectionRectangle(connArea,
				sourceRect, position);

		if (position != PositionConstants.NONE) {
			PrecisionPoint translateExpansion = new PrecisionPoint(Math.max(
					connArea.width, connArea.height), 0);
			// if (!isFeedbackConn) {
			// IMapMode mm = MapModeUtil.getMapMode(conn);
			// translateExpansion = (PrecisionPoint)
			// mm.LPtoDP(translateExpansion);
			// translateExpansion.preciseX =
			// Math.pow(translateExpansion.preciseX,
			// 0.8);
			// translateExpansion = (PrecisionPoint)
			// mm.DPtoLP(translateExpansion);
			// } else {
			translateExpansion.preciseX = Math.pow(translateExpansion.preciseX,
					0.8);
			// }
			translateExpansion.updateInts();

			getPrimaryPreciseConnectionArea(connArea, translateExpansion.x,
					position);
		} else {
			connArea.expand(selfrelsizeincr.x << 1, selfrelsizeincr.x << 1);
		}

		connArea.expand(nSelfIncr, nSelfIncr);
		getConnectionPoints(connArea, position, sourcePosition, newLine);

		PrecisionPoint ptS2 = new PrecisionPoint(newLine.getPoint(0));
		PrecisionPoint ptS1 = new PrecisionPoint(conn.getSource()
				.getReferencePoint());

		Point ptAbsS2 = new Point(ptS2);
		// conn.getSource().translateToAbsolute(ptAbsS2);
		PrecisionPoint ptEdge = new PrecisionPoint(conn.getSource()
				.getLocation(ptAbsS2));

		ptS1 = new PrecisionPoint(getStraightEdgePoint(ptEdge, ptS1, ptS2));

		PrecisionPoint ptE2 = new PrecisionPoint(newLine.getPoint(newLine
				.size() - 1));
		PrecisionPoint ptE1 = new PrecisionPoint(conn.getTarget()
				.getReferencePoint());
		// conn.translateToRelative(ptE1);
		PrecisionPoint ptAbsE2 = (PrecisionPoint) ptE2.getCopy();
		// conn.getTarget().translateToAbsolute(ptAbsE2);
		ptEdge = new PrecisionPoint(conn.getTarget().getLocation(ptAbsE2));
		// conn.translateToRelative(ptEdge);
		ptE1 = new PrecisionPoint(getStraightEdgePoint(ptEdge, ptE1, ptE2));

		newLine.insertPoint(
				new Point(Math.round(ptS1.preciseX), Math.round(ptS1.preciseY)),
				0);
		newLine.insertPoint(
				new Point(Math.round(ptE1.preciseX), Math.round(ptE1.preciseY)),
				newLine.size());
	}

	protected static Point getStraightEdgePoint(final Point ptEdge,
			final Point ptRef1, final Point ptRef2) {
		LineSeg lineSeg = new LineSeg(ptRef1, ptRef2);

		Point ptProj = lineSeg.perpIntersect(ptEdge.x, ptEdge.y);
		if (Math.abs(ptProj.x - ptRef2.x) < Math.abs(ptProj.y - ptRef2.y))
			ptProj.x = ptRef2.x;
		else
			ptProj.y = ptRef2.y;

		return ptProj;
	}

	private Object getIntersectingShapesConnectionKey(BWNode source,
			BWNode target) {
		return new Integer(source.hashCode() ^ target.hashCode());
	}

	private PrecisionRectangle getShapeBounds(SVGFigure source) {
		return new PrecisionRectangle(source.getPolygonPoints().getBounds());
	}

	private void absDimension(Dimension d) {
		d.width = Math.abs(d.width);
		d.height = Math.abs(d.height);
	}

	private int getSourcePositionFromConnectionRectangle(
			Rectangle connRectangle, Rectangle sourceRect, int position) {
		Dimension diff = null;
		switch (position) {
		case PositionConstants.NORTH_WEST:
			diff = connRectangle.getBottomRight().getDifference(
					sourceRect.getTopLeft());
			if (diff.width == 0) {
				return PositionConstants.EAST;
			} else {
				return PositionConstants.SOUTH;
			}
		case PositionConstants.NORTH_EAST:
			diff = connRectangle.getBottomLeft().getDifference(
					sourceRect.getTopRight());
			if (diff.width == 0) {
				return PositionConstants.WEST;
			} else {
				return PositionConstants.SOUTH;
			}
		case PositionConstants.SOUTH_EAST:
			diff = connRectangle.getTopLeft().getDifference(
					sourceRect.getBottomRight());
			if (diff.width == 0) {
				return PositionConstants.WEST;
			} else {
				return PositionConstants.NORTH;
			}
		case PositionConstants.SOUTH_WEST:
			diff = connRectangle.getTopRight().getDifference(
					sourceRect.getBottomLeft());
			if (diff.width == 0) {
				return PositionConstants.EAST;
			} else {
				return PositionConstants.NORTH;
			}
		case PositionConstants.NONE:
			diff = connRectangle.getCenter().getDifference(
					sourceRect.getCenter());
			if (diff.width == 0) {
				return diff.height < 0 ? PositionConstants.SOUTH
						: PositionConstants.NORTH;
			} else {
				return diff.width < 0 ? PositionConstants.EAST
						: PositionConstants.WEST;
			}
		}
		return PositionConstants.NONE;
	}

	private void getPrimaryPreciseConnectionArea(Rectangle r, int size,
			int positionOfConnArea) {
		r.expand(size, size);
		if (r.width < r.height) {
			r.height -= size;
			if ((positionOfConnArea & PositionConstants.SOUTH) != 0) {
				r.y += size;
			}
		} else {
			r.width -= size;
			if ((positionOfConnArea & PositionConstants.EAST) != 0) {
				r.x += size;
			}
		}
	}

	private void getConnectionPoints(Rectangle connRect, int position,
			int sourcePosition, PointList line) {
		line.removeAllPoints();
		switch (position) {
		case PositionConstants.NORTH_WEST:
			if (sourcePosition == PositionConstants.EAST) {
				line.addPoint(connRect.getTopRight());
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getBottomLeft());
			} else {
				line.addPoint(connRect.getBottomLeft());
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getTopRight());
			}
			break;
		case PositionConstants.NORTH_EAST:
			if (sourcePosition == PositionConstants.WEST) {
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getTopRight());
				line.addPoint(connRect.getBottomRight());
			} else {
				line.addPoint(connRect.getBottomRight());
				line.addPoint(connRect.getTopRight());
				line.addPoint(connRect.getTopLeft());
			}
			break;
		case PositionConstants.SOUTH_EAST:
			if (sourcePosition == PositionConstants.WEST) {
				line.addPoint(connRect.getBottomLeft());
				line.addPoint(connRect.getBottomRight());
				line.addPoint(connRect.getTopRight());
			} else {
				line.addPoint(connRect.getTopRight());
				line.addPoint(connRect.getBottomRight());
				line.addPoint(connRect.getBottomLeft());
			}
			break;
		case PositionConstants.SOUTH_WEST:
			if (sourcePosition == PositionConstants.EAST) {
				line.addPoint(connRect.getBottomRight());
				line.addPoint(connRect.getBottomLeft());
				line.addPoint(connRect.getTopLeft());
			} else {
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getBottomLeft());
				line.addPoint(connRect.getBottomRight());
			}
			break;
		case PositionConstants.NONE:
			if (sourcePosition == PositionConstants.NORTH) {
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getBottomLeft());
			} else if (sourcePosition == PositionConstants.SOUTH) {
				line.addPoint(connRect.getBottomLeft());
				line.addPoint(connRect.getTopLeft());
			} else if (sourcePosition == PositionConstants.WEST) {
				line.addPoint(connRect.getTopLeft());
				line.addPoint(connRect.getTopRight());
			} else {
				line.addPoint(connRect.getTopRight());
				line.addPoint(connRect.getTopLeft());
			}
		}
	}
}
