
package com.gdu.bw.svg.helper;

import com.gdu.bw.svg.model.BWNode;
import com.gdu.bw.svg.model.Dimension;
import com.gdu.bw.svg.model.Point;
import com.gdu.bw.svg.model.PointList;
import com.gdu.bw.svg.model.PrecisionPoint;
import com.gdu.bw.svg.model.figure.SVGFigure;
import com.gdu.bw.svg.model.figure.SequenceFlowFigure;

/**
 * @author <a>Davy Du</a>
 * 
 * @since 1.0.0
 */
public class BWRouterHelper {
	static private BWRouterHelper sprm = new BWRouterHelper(false);

	/**
	 * @return the <code>RouterHelper</code> singleton instance
	 */
	static public BWRouterHelper getInstance() {
		return sprm;
	}

	protected boolean useGEFRouter = false;

	protected BWRouterHelper(boolean useGEFRouter) {
		this.useGEFRouter = useGEFRouter;
	}

	public void resetEndPointsToEdge(SVGFigure sourceNode,
			SVGFigure targetNode, PointList newLine,
			Boolean needToAdjustEdgePoint) {
		if (newLine.size() < 2) {
			newLine.addPoint(0, 0);
			newLine.insertPoint(new Point(), 0);
		}
		PrecisionPoint sourceAnchorPoint, targetAnchorPoint;
		if (newLine.size() > 2) {
			PrecisionPoint sourceReference = new PrecisionPoint(
					newLine.getPoint(1));
			PrecisionPoint targetReference = new PrecisionPoint(
					newLine.getPoint(newLine.size() - 2));
			// sourceNode.getFigure().translateToAbsolute(sourceReference);
			// targetNode.getFigure().translateToAbsolute(targetReference);
			sourceAnchorPoint = getAnchorLocation(sourceNode.getNode(),
					sourceReference);
			targetAnchorPoint = getAnchorLocation(targetNode.getNode(),
					targetReference);
		} else {
			PrecisionPoint sourceReference = getAnchorReference(targetNode
					.getNode());
			sourceAnchorPoint = getAnchorLocation(sourceNode.getNode(),
					sourceReference);
			targetAnchorPoint = getAnchorLocation(targetNode.getNode(),
					sourceAnchorPoint);
		}

		newLine.setPoint(
				new Point(Math.round(sourceAnchorPoint.preciseX), Math
						.round(sourceAnchorPoint.preciseY)), 0);
		newLine.setPoint(
				new Point(Math.round(targetAnchorPoint.preciseX), Math
						.round(targetAnchorPoint.preciseY)), newLine.size() - 1);
	}

	private PrecisionPoint getAnchorLocation(BWNode node, Point reference) {
		return new PrecisionPoint(node.getFigure().getLocation(reference));
	}

	private PrecisionPoint getAnchorReference(BWNode anchor) {
		return new PrecisionPoint(anchor.getFigure().getReferencePoint());
	}

	public boolean isFeedback(SequenceFlowFigure conn) {
		Dimension dim = new Dimension(100, 100);
		Dimension dimCheck = dim.getCopy();
		// conn.translateToRelative(dimCheck);
		return dim.equals(dimCheck);
	}
}
