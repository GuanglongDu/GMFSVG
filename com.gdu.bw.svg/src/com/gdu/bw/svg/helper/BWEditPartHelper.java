
package com.gdu.bw.svg.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.gdu.bw.svg.model.ArrayListKey;
import com.gdu.bw.svg.model.BWNode;
import com.gdu.bw.svg.model.Dimension;
import com.gdu.bw.svg.model.LineSeg;
import com.gdu.bw.svg.model.Point;
import com.gdu.bw.svg.model.PointList;
import com.gdu.bw.svg.model.PointListUtilities;
import com.gdu.bw.svg.model.PositionConstants;
import com.gdu.bw.svg.model.PrecisionPoint;
import com.gdu.bw.svg.model.PrecisionRectangle;
import com.gdu.bw.svg.model.Ray;
import com.gdu.bw.svg.model.Rectangle;
import com.gdu.bw.svg.model.RelativeBendpoint;
import com.gdu.bw.svg.model.figure.CatchFigure;
import com.gdu.bw.svg.model.figure.FlowFigure;
import com.gdu.bw.svg.model.figure.OnAlarmFigure;
import com.gdu.bw.svg.model.figure.OnMessageFigure;
import com.gdu.bw.svg.model.figure.PickFigure;
import com.gdu.bw.svg.model.figure.ProcessFigure;
import com.gdu.bw.svg.model.figure.SVGFigure;
import com.gdu.bw.svg.model.figure.ScopeCatchAllFigure;
import com.gdu.bw.svg.model.figure.ScopeCompartmentFigure;
import com.gdu.bw.svg.model.figure.ScopeEventHandlersFigure;
import com.gdu.bw.svg.model.figure.ScopeFaultHandlersFigure;
import com.gdu.bw.svg.model.figure.ScopeFigure;
import com.gdu.bw.svg.model.figure.ScopeOnEventFigure;

/**
 * @author <a>Davy Du</a>
 * 
 * @since 1.0.0
 */
public class BWEditPartHelper {

	public static BWEditPartHelper INSTANCE = new BWEditPartHelper();
	private static int STRAIGHT_LINE_TOLERANCE = 10;
	private static int maxNestedRoutingDepth = 1;

	public void loadProcessFigures(Element scope, BWNode root,
			SVGFigure figure, Element notation) {
		List<Element> connectionsToPaint = new ArrayList<Element>();
		calcuateFigureRectangle(root, figure, notation, connectionsToPaint);
		figure.layout();
		if (connectionsToPaint.size() > 0)
			calcuateConnectionPoints(figure.getChildList().get(0), notation,
					connectionsToPaint);

		iteratorBWNode(root, root.getAbsolutePoint());
	}

	private void iteratorBWNode(BWNode root, Rectangle rect) {
		List<BWNode> childList = root.getChildList();
		for (BWNode node : childList) {
			Rectangle absolutePoint = node.getAbsolutePoint().getCopy();
			if (!absolutePoint.isEmpty()) {
				absolutePoint.x = absolutePoint.x - rect.x;
				absolutePoint.y = absolutePoint.y - rect.y;
				Element element = node.getElement();
				element.addAttribute("x", String.valueOf(absolutePoint.x));
				element.addAttribute("y", String.valueOf(absolutePoint.y));
				element.addAttribute("width",
						String.valueOf(absolutePoint.width));
				element.addAttribute("height",
						String.valueOf(absolutePoint.height));
				System.out.println(absolutePoint.toString() + "  "
						+ node.getFigure().getType());
				iteratorBWNode(node, node.getAbsolutePoint().getCopy());
			} else if (node.getPointlist().size() > 0) {
				String points = changeRelativePoints(node.getPointlist(), rect);
				node.getElement().addAttribute("points", points);
				System.out.println(points.toString());
			} else {
				iteratorBWNode(node, rect);
			}
		}
	}

	/**
	 * @param sourceFigure
	 * @param targetFigure
	 * @param pointlist
	 */
	private String changeRelativePoints(PointList pointlist, Rectangle rect) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for (int i = 0; i < pointlist.size(); i++) {
			buffer.append("[" + (pointlist.getPoint(i).x - rect.x + 10) + ","
					+ (pointlist.getPoint(i).y - rect.y + 10) + "]");
			if (i != pointlist.size() - 1) {
				buffer.append(",");
			}
		}
		buffer.append("]");
		return buffer.toString();
	}

	public void calcuateFigureRectangle(BWNode root, SVGFigure figure,
			Element notation, List<Element> connectionsToPaint) {
		Iterator elementIterator = notation.elementIterator();
		SVGFigure childFigure = null;
		int index = 0;
		while (elementIterator.hasNext()) {
			Object next = elementIterator.next();
			if (next instanceof Element) {
				Element child = (Element) next;
				Element hrefElement = child.element("element");
				String type = child.attributeValue("type");
				if ("children".equals(child.getName())) {
					if (hrefElement != null) {
						Element layout = child.element("layoutConstraint");
						if (layout != null && figure != null) {

							String xpath = hrefElement.attributeValue("href");

							BWNode node = BWResourceImpl.INSTANCE.getEObject(
									root, xpath);
							if (node != null) {
								Element element = node.getElement();
								boolean collapsed = BWDiagramUtil.INSTANCE
										.isCollapsed(child);
								if (element != null) {
									childFigure = addRectangle(figure, node,
											child, type, collapsed);
								}
							} else {
								childFigure = new SVGFigure(figure);
								childFigure.setType(type);

							}
						} else {
							String xpath = child.element("element")
									.attributeValue("href");
							BWNode node = BWResourceImpl.INSTANCE.getEObject(
									root, xpath);
							if ("3004".equals(type)) {
								childFigure = new SVGFigure(figure);
								childFigure.setType(type);
								childFigure
										.setAbsolutePoint(new Point(175, 38));
								childFigure.setDiagram(child);
								if (node != null) {
									node.setFigure(childFigure);
									childFigure.setNode(node);
								}
								childFigure.setIsCollapse(figure
										.getIsCollapse());
							} else if ("3011".equals(type)) {
								childFigure = new PickFigure(figure);
								childFigure.setType(type);
								childFigure.setAbsolutePoint(new Point(7, 21));
								childFigure.setDiagram(child);
								if (node != null) {
									node.setFigure(childFigure);
									childFigure.setNode(node);
								}
								if (figure.getBounds() != null) {
									childFigure.setBounds(figure.getBounds());
								}
								childFigure.setIsCollapse(figure
										.getIsCollapse());
							} else if ("3012".equals(type)) {
								childFigure = new OnMessageFigure(figure);
								childFigure.setType(type);
								childFigure.setAbsolutePoint(new Point(5, 5));
								childFigure.setDiagram(child);
								if (node != null) {
									node.setFigure(childFigure);
									childFigure.setNode(node);
								}
								if (figure.getBounds() != null) {
									childFigure.setBounds(figure.getBounds());
								}
								childFigure.setIsCollapse(figure
										.getIsCollapse());
							} else if ("3013".equals(type)) {
								childFigure = new OnMessageFigure(figure);
								childFigure.setType(type);
								childFigure.setAbsolutePoint(new Point(33, 19));
								childFigure.setDiagram(child);
								if (node != null) {
									node.setFigure(childFigure);
									childFigure.setNode(node);
								}
								if (figure.getBounds() != null) {
									childFigure.setBounds(figure.getBounds());
								}
								childFigure.setIsCollapse(figure
										.getIsCollapse());
							} else if ("3016".equals(type)) {
								childFigure = new OnMessageFigure(figure);
								childFigure.setType(type);
								childFigure.setAbsolutePoint(new Point(33, 19));
								childFigure.setDiagram(child);
								if (node != null) {
									node.setFigure(childFigure);
									childFigure.setNode(node);
								}
								if (figure.getBounds() != null) {
									childFigure.setBounds(figure.getBounds());
								}
								childFigure.setIsCollapse(figure
										.getIsCollapse());
							} else {
								childFigure = new SVGFigure(figure);
								childFigure.setType(type);
								childFigure.setDiagram(child);
								childFigure.setBounds(figure.getBounds());
								childFigure.setIsCollapse(figure
										.getIsCollapse());
							}
						}
						index++;
					} else {
						if ("3030".equals(type)) {
							childFigure = new CatchFigure(figure);
							childFigure.setType(type);
							childFigure.setDiagram(child);
							if (figure.getBounds() != null) {
								childFigure.setBounds(figure.getBounds());
							}
							childFigure.setIsCollapse(figure.getIsCollapse());
						} else if ("3032".equals(type)) {
							childFigure = new ScopeCatchAllFigure(figure);
							childFigure.setType(type);
							childFigure.setDiagram(child);
							childFigure.setIsCollapse(figure.getIsCollapse());
						} else if ("3026".equals(type)) {
							childFigure = new ScopeOnEventFigure(figure);
							childFigure.setType(type);
							childFigure.setDiagram(child);
							childFigure.setIsCollapse(figure.getIsCollapse());
						} else if ("3013".equals(type)) {
							childFigure = new OnMessageFigure(figure);
							childFigure.setType(type);
							childFigure.setAbsolutePoint(new Point(33, 19));
							childFigure.setDiagram(child);
							if (figure.getBounds() != null) {
								childFigure.setBounds(figure.getBounds());
							}
							childFigure.setIsCollapse(figure.getIsCollapse());
						} else if ("3022".equals(type)) {
							childFigure = new ScopeFaultHandlersFigure(figure);
							childFigure.setType(type);
							childFigure.setDiagram(child);
							if (figure.getBounds() != null) {
								childFigure.setBounds(figure.getBounds());
							}
							childFigure.setIsCollapse(figure.getIsCollapse());
						} else if ("3011".equals(type)) {
							childFigure = new PickFigure(figure);
							childFigure.setType(type);
							childFigure.setAbsolutePoint(new Point(7, 21));
							childFigure.setDiagram(child);

							if (figure.getBounds() != null) {
								childFigure.setBounds(figure.getBounds());
							}
							childFigure.setIsCollapse(figure.getIsCollapse());
						} else if ("3023".equals(type)) {
							childFigure = new ScopeCompartmentFigure(figure);
							childFigure.setType(type);
							childFigure.setDiagram(child);
							if (figure.getBounds() != null) {
								childFigure.setBounds(figure.getBounds());
							}
							childFigure.setIsCollapse(figure.getIsCollapse());
						} else if ("3012".equals(type)) {
							childFigure = new OnMessageFigure(figure);
							childFigure.setType(type);
							childFigure.setAbsolutePoint(new Point(5, 5));
							childFigure.setDiagram(child);
							if (figure.getBounds() != null) {
								childFigure.setBounds(figure.getBounds());
							}
							childFigure.setIsCollapse(figure.getIsCollapse());
						} else if ("3016".equals(type)) {
							childFigure = new OnMessageFigure(figure);
							childFigure.setType(type);
							childFigure.setAbsolutePoint(new Point(5, 5));
							childFigure.setDiagram(child);
							if (figure.getBounds() != null) {
								childFigure.setBounds(figure.getBounds());
							}
							childFigure.setIsCollapse(figure.getIsCollapse());
						} else if ("3021".equals(type)) {
							childFigure = new ScopeEventHandlersFigure(figure);
							childFigure.setType(type);
							childFigure.setDiagram(child);
							if (figure.getBounds() != null) {
								childFigure.setBounds(figure.getBounds());
							}
							childFigure.setIsCollapse(figure.getIsCollapse());
						} else {
							childFigure = new SVGFigure(figure);
							childFigure.setType(type);
							childFigure.setDiagram(child);
							if (figure.getBounds() != null) {
								childFigure.setBounds(figure.getBounds());
							}
							childFigure.setIsCollapse(figure.getIsCollapse());
						}
					}
					calcuateFigureRectangle(root, childFigure, child,
							connectionsToPaint);
				} else if ("edges".equals(child.getName())) {
					connectionsToPaint.add(child);
				}
			}
		}
	}

	/**
	 * @param node
	 * @param notation
	 * @param connectionsToPaint
	 */
	private void calcuateConnectionPoints(SVGFigure figure, Element notation,
			List<Element> connectionsToPaint) {
		BWNode node = figure.getNode();
		for (Iterator<Element> connItr = connectionsToPaint.iterator(); connItr
				.hasNext();) {
			Element next = connItr.next();
			Element hrefElement = next.element("element");
			if (hrefElement != null) {
				String xpath = hrefElement.attributeValue("href");
				if (xpath != null) {
					Element element = next.element("bendpoints");
					Attribute attribute = element.attribute("points");
					Attribute source = next.attribute("source");
					Attribute target = next.attribute("target");
					String sourcePath = source.getValue();
					SVGFigure sourceFigure = BWResourceImpl.INSTANCE
							.getSVGFigure(figure, sourcePath);
					BWNode sourceNode = sourceFigure.getNode();
					Element sourceAnchor = next.element("sourceAnchor");
					if (sourceAnchor != null) {
						String attributeValue = sourceAnchor
								.attributeValue("id");
						sourceFigure.setRelativeReference(sourceNode
								.parseTerminalString(attributeValue));
					}
					String targetPath = target.getValue();
					SVGFigure targetFigure = BWResourceImpl.INSTANCE
							.getSVGFigure(figure, targetPath);
					BWNode targetNode = targetFigure.getNode();
					Element targetAnchor = next.element("targetAnchor");
					if (targetAnchor != null) {
						String attributeValue = targetAnchor
								.attributeValue("id");
						targetFigure.setRelativeReference(targetNode
								.parseTerminalString(attributeValue));
					}
					String pointValue = attribute.getValue();
					if (pointValue.indexOf("$") > 0) {
						String[] points = pointValue.split("\\$");
						PointList pointlist = new PointList(points.length);
						for (int i = 0; i < points.length; i++) {
							String point = points[i];
							int[] splitPoints = splitBentPoint(point);
							RelativeBendpoint sourceBendpoint = new RelativeBendpoint();
							sourceBendpoint.setRelativeDimensions(
									new Dimension(splitPoints[0],
											splitPoints[1]), new Dimension(
											splitPoints[2], splitPoints[3]));
							if (points.length == 1) {
								sourceBendpoint.setWeight(0.5f);
							} else {
								sourceBendpoint.setWeight(i
										/ ((float) points.length - 1));
							}
							Point sourcePoint = sourceBendpoint.getLocation(
									sourceFigure, targetFigure);
							pointlist.addPoint(sourcePoint);
						}
						if (points.length == 0) {
							Point r1 = sourceFigure.getReferencePoint()
									.getCopy();
							pointlist.addPoint(r1);
							Point r2 = targetFigure.getReferencePoint()
									.getCopy();
							pointlist.addPoint(r2);
						}

						routeLine(sourceFigure, targetFigure, 0, pointlist);
						// String relativePoints =
						// changeRelativePoints(sourceFigure,
						// targetFigure,pointlist);
						// Element hrefElement = next.element("element");
						// if (hrefElement != null) {
						// String xpath = hrefElement.attributeValue("href");
						String frontPath = xpath.substring(0,
								xpath.indexOf("/@links"));

						BWNode linkParentNode = BWResourceImpl.INSTANCE
								.getEObject(node, frontPath);
						int lastIndex = xpath.length() - 1;
						int dotIndex = -1;
						dotIndex = xpath.lastIndexOf('.', lastIndex - 1);
						if (dotIndex >= 0) {
							int position = 0;
							try {
								position = Integer.parseInt(xpath
										.substring(dotIndex + 1));
							} catch (NumberFormatException exception) {
								throw exception;
							}
							if (linkParentNode.getLinks().size() > position) {
								BWNode linkNode = linkParentNode.getLinks()
										.get(position);
								if (linkNode != null) {
									linkNode.getElement().addAttribute(
											"points", pointlist.toString());
									linkNode.setPointlist(pointlist);
									// linkNode.getElement().addAttribute("points",
									// relativePoints);
								}
							}
						}
					}
				}
			}
		}
	}

	protected boolean checkSelfRelConnection(BWNode source, BWNode target,
			PointList newLine) {
		if (source == target && newLine.size() < 4) {

			return true;
		} else {
			removeSelfRelConnection(source, target);
			return false;
		}
	}

	/**
	 * @param source
	 * @param target
	 */
	private void removeSelfRelConnection(BWNode source, BWNode target) {
		if (source == null || target == null) {
			return;
		}
		ArrayListKey connectionKey = new ArrayListKey(source, target);
	}

	private void routeLine(SVGFigure source, SVGFigure target,
			int nestedRoutingDepth, PointList newLine) {
		// boolean skipNormalization = (routerFlags &
		// ROUTER_FLAG_SKIPNORMALIZATION) != 0;
		ObliqueRouter check = new ObliqueRouter();
		if (checkSelfRelConnection(source.getNode(), target.getNode(), newLine)
				|| check.checkShapesIntersect(source, target, newLine)) {
			resetEndPointsToEdge(source, target, newLine, false);
			OrthogonalRouterUtilities.transformToOrthogonalPointList(
					newLine,
					getOffShapeDirection(getAnchorOffRectangleDirection(
							newLine.getFirstPoint(),
							sourceBoundsRelativeToConnection(source))),
					getOffShapeDirection(getAnchorOffRectangleDirection(
							newLine.getLastPoint(),
							targetBoundsRelativeToConnection(target))));
			removeRedundantPoints(newLine);
			return;
		}

		if (source == target) {
			nestedRoutingDepth = maxNestedRoutingDepth;
		}
		Boolean needToAdjustEdgePoint;
		if (newLine.size() <= 2) {
			needToAdjustEdgePoint = true;
		} else {
			needToAdjustEdgePoint = false;
		}
		Point lastStartAnchor = newLine.removePoint(0);
		Point lastEndAnchor = newLine.removePoint(newLine.size() - 1);
		if (!OrthogonalRouterUtilities.isRectilinear(newLine)) {
			OrthogonalRouterUtilities.transformToOrthogonalPointList(newLine,
					PositionConstants.NONE, PositionConstants.NONE);
		}
		removeRedundantPoints(newLine);
		removePointsInViews(source, target, newLine, lastStartAnchor,
				lastEndAnchor);

		Dimension tolerance = new Dimension(3, 0);

		if (PointListUtilities.normalizeSegments(newLine, tolerance.width)) {
			normalizeToStraightLineTolerance(newLine, tolerance.width);
		}

		if (newLine.size() == 2) {
			Ray middleSeg = new Ray(newLine.getFirstPoint(),
					newLine.getLastPoint());
			if (middleSeg.length() <= tolerance.width) {
				newLine.removePoint(0);
			}
		}
		resetEndPointsToEdge(source, target, newLine, needToAdjustEdgePoint);
		if (nestedRoutingDepth < maxNestedRoutingDepth
				&& !isValidRectilinearLine(source, target, newLine)) {
			routeLine(source, target, ++nestedRoutingDepth, newLine);
		}
	}

	private boolean isValidRectilinearLine(SVGFigure sourceNode,
			SVGFigure targetNode, PointList line) {
		PrecisionRectangle source = new PrecisionRectangle(sourceNode
				.getPolygonPoints().getBounds());
		sourceNode.translateToAbsolute(source);
		if (source.contains(line.getPoint(1))) {
			return false;
		}
		int firstSegmentOrientation = line.getFirstPoint().x == line
				.getPoint(1).x ? PositionConstants.VERTICAL
				: PositionConstants.HORIZONTAL;
		if (getOutisePointOffRectanglePosition(line.getPoint(1), source) != getAnchorLocationBasedOnSegmentOrientation(
				line.getFirstPoint(), source, firstSegmentOrientation)) {
			return false;
		}

		PrecisionRectangle target = new PrecisionRectangle(targetNode
				.getPolygonPoints().getBounds());
		targetNode.translateToAbsolute(target);
		if (target.contains(line.getPoint(line.size() - 2))) {
			return false;
		}
		int lastSegmentOrientation = line.getLastPoint().x == line
				.getPoint(line.size() - 2).x ? PositionConstants.VERTICAL
				: PositionConstants.HORIZONTAL;
		if (getOutisePointOffRectanglePosition(line.getPoint(line.size() - 2),
				target) != getAnchorLocationBasedOnSegmentOrientation(
				line.getLastPoint(), target, lastSegmentOrientation)) {
			return false;
		}

		return true;
	}

	private int getOutisePointOffRectanglePosition(Point p, Rectangle r) {
		int position = PositionConstants.NONE;
		if (r.x > p.x) {
			position |= PositionConstants.WEST;
		} else if (r.x + r.width < p.x) {
			position |= PositionConstants.EAST;
		}
		if (r.y > p.y) {
			position |= PositionConstants.NORTH;
		} else if (r.y + r.height < p.y) {
			position |= PositionConstants.SOUTH;
		}
		return position;
	}

	private int getAnchorLocationBasedOnSegmentOrientation(Point anchorPoint,
			Rectangle rectangle, int segmentOrientation) {
		if (segmentOrientation == PositionConstants.VERTICAL) {
			if (Math.abs(anchorPoint.y - rectangle.y) < Math.abs(anchorPoint.y
					- rectangle.y - rectangle.height)) {
				return PositionConstants.NORTH;
			} else {
				return PositionConstants.SOUTH;
			}
		} else if (segmentOrientation == PositionConstants.HORIZONTAL) {
			if (Math.abs(anchorPoint.x - rectangle.x) < Math.abs(anchorPoint.x
					- rectangle.x - rectangle.width)) {
				return PositionConstants.WEST;
			} else {
				return PositionConstants.EAST;
			}
		}
		return PositionConstants.NONE;
	}

	/**
	 * @param source
	 * @param target
	 * @param newLine
	 */
	private void resetEndPointsToEdge(SVGFigure sourceNode,
			SVGFigure targetNode, PointList pointlist,
			Boolean needToAdjustEdgePoint) {

		Rectangle source = calcuateSourceOrTargetLocation(sourceNode);// 223,146,49,79
		Rectangle target = calcuateSourceOrTargetLocation(targetNode);// Rectangle(461,
																		// 151,
																		// 49,
																		// 69)
		int offSourceDirection = 0;
		int offTargetDirection = 0;
		int sourceAnchorRelativeLocation = 0;
		int targetAnchorRelativeLocation = 0;
		if (pointlist.size() == 0) {
			pointlist.addPoint(new Point());
			pointlist.addPoint(new Point());
			BWRouterHelper.getInstance().resetEndPointsToEdge(sourceNode,
					targetNode, pointlist, needToAdjustEdgePoint);
			sourceAnchorRelativeLocation = getAnchorOffRectangleDirection(
					pointlist.getFirstPoint(), source);
			targetAnchorRelativeLocation = getAnchorOffRectangleDirection(
					pointlist.getLastPoint(), target);
			Point offStart = pointlist.getFirstPoint();
			Point offEnd = pointlist.getLastPoint();
			Dimension offsetDim = offStart.getDifference(offEnd).scale(0.5);
			offStart.translate(getTranslationValue(
					sourceAnchorRelativeLocation, Math.abs(offsetDim.width),
					Math.abs(offsetDim.height)));
			offEnd.translate(getTranslationValue(targetAnchorRelativeLocation,
					Math.abs(offsetDim.width), Math.abs(offsetDim.height)));
			pointlist.insertPoint(offStart, 1);
			pointlist.insertPoint(offEnd, 2);
			offSourceDirection = getOffShapeDirection(sourceAnchorRelativeLocation);
			offTargetDirection = getOffShapeDirection(targetAnchorRelativeLocation);
		} else {
			Point start = pointlist.getFirstPoint();
			Point end = pointlist.getLastPoint();
			// if source instanceof OrthogonalConnectionAnchor
			LineSeg lineSeg = getOrthogonalLineSegToAnchorLoc(sourceNode, start);// 247,185
			Point startPoint = lineSeg.getOrigin();// 272,185
			PrecisionRectangle sourceBounds = sourceBoundsRelativeToConnection(sourceNode);// 223,146
			if (needToAdjustEdgePoint) {
				adjustEdgePoint(startPoint, sourceBounds);
			}

			pointlist.insertPoint(startPoint, 0);
			LineSeg lineSeg2 = getOrthogonalLineSegToAnchorLoc(targetNode, end);
			Point endPoint = lineSeg2.getOrigin();
			PrecisionRectangle targetBounds = targetBoundsRelativeToConnection(targetNode);
			if (needToAdjustEdgePoint) {
				adjustEdgePoint(endPoint, targetBounds);
			}
			pointlist.addPoint(endPoint);

			sourceAnchorRelativeLocation = getAnchorOffRectangleDirection(
					pointlist.getFirstPoint(), source);
			offSourceDirection = getOffShapeDirection(sourceAnchorRelativeLocation);
			targetAnchorRelativeLocation = getAnchorOffRectangleDirection(
					pointlist.getLastPoint(), target);
			offTargetDirection = getOffShapeDirection(targetAnchorRelativeLocation);
		}
		OrthogonalRouterUtilities.transformToOrthogonalPointList(pointlist,
				offSourceDirection, offTargetDirection);
		removeRedundantPoints(pointlist);

	}

	private Dimension getTranslationValue(int position, int xFactorValue,
			int yFactorValue) {
		Dimension translationDimension = new Dimension();
		if (position == PositionConstants.EAST) {
			translationDimension.width = xFactorValue;
		} else if (position == PositionConstants.SOUTH) {
			translationDimension.height = yFactorValue;
		} else if (position == PositionConstants.WEST) {
			translationDimension.width = -xFactorValue;
		} else if (position == PositionConstants.NORTH) {
			translationDimension.height = -yFactorValue;
		}
		return translationDimension;
	}

	private int getOffShapeDirection(int anchorRelativeLocation) {
		if (anchorRelativeLocation == PositionConstants.EAST
				|| anchorRelativeLocation == PositionConstants.WEST) {
			return PositionConstants.HORIZONTAL;
		} else if (anchorRelativeLocation == PositionConstants.NORTH
				|| anchorRelativeLocation == PositionConstants.SOUTH) {
			return PositionConstants.VERTICAL;
		}
		return PositionConstants.NONE;
	}

	private PrecisionRectangle targetBoundsRelativeToConnection(SVGFigure node) {
		Rectangle bounds = node.getHandlerBounds().getCopy();
		bounds.y -= 10;
		bounds.height += 20;
		PrecisionRectangle target = new PrecisionRectangle(bounds);
		target = node.translateToAbsolute(target);
		return target;
	}

	protected void adjustEdgePoint(Point edgePoint,
			PrecisionRectangle activityBounds) {
		double percent = 0.0;
		Point activityCenter = activityBounds.getCenter();
		int direction = getAnchorOffRectangleDirection(edgePoint,
				activityBounds);
		if (direction == PositionConstants.WEST
				|| direction == PositionConstants.EAST) {
			if (edgePoint.y < activityCenter.y) {
				int distance = activityCenter.y - edgePoint.y;
				double newDistance = percent * (double) distance;
				edgePoint.y = (int) (activityCenter.y - newDistance);

			} else if (edgePoint.y > activityCenter.y) {
				int distance = edgePoint.y - activityCenter.y;
				double newDistance = percent * (double) distance;
				edgePoint.y = (int) (activityCenter.y + newDistance);
			}

		} else if (direction == PositionConstants.NORTH
				|| direction == PositionConstants.SOUTH) {
			if (edgePoint.x < activityCenter.x) {
				int distance = activityCenter.x - edgePoint.x;
				double newDistance = percent * (double) distance;
				edgePoint.x = (int) (activityCenter.x - newDistance);

			} else if (edgePoint.x > activityCenter.x) {
				int distance = edgePoint.x - activityCenter.x;
				double newDistance = percent * (double) distance;
				edgePoint.x = (int) (activityCenter.x + newDistance);
			}
		}
	}

	private int getAnchorOffRectangleDirection(Point anchorPoint, Rectangle rect) {
		int position = PositionConstants.NORTH;
		int criteriaValue = Math.abs(anchorPoint.y - rect.y);
		int tempCriteria = Math.abs(anchorPoint.y - rect.y - rect.height);
		if (tempCriteria < criteriaValue) {
			criteriaValue = tempCriteria;
			position = PositionConstants.SOUTH;
		}

		tempCriteria = Math.abs(anchorPoint.x - rect.x);
		if (tempCriteria < criteriaValue) {
			criteriaValue = tempCriteria;
			position = PositionConstants.WEST;
		}

		tempCriteria = Math.abs(anchorPoint.x - rect.x - rect.width);
		if (tempCriteria < criteriaValue) {
			criteriaValue = tempCriteria;
			position = PositionConstants.EAST;
		}

		return position;
	}

	private PrecisionRectangle sourceBoundsRelativeToConnection(SVGFigure node) {
		Rectangle bounds = node.getHandlerBounds().getCopy();
		bounds.y -= 10;
		bounds.height += 20;
		PrecisionRectangle source = new PrecisionRectangle(bounds);
		source = node.translateToAbsolute(source);
		return source;
	}

	public LineSeg getOrthogonalLineSegToAnchorLoc(SVGFigure sourceNode,
			Point ref) {
		PrecisionPoint refAbs = new PrecisionPoint(ref);
		PrecisionPoint anchorPoint = new PrecisionPoint(getOrthogonalLocation(
				sourceNode, refAbs));
		return new LineSeg(anchorPoint, ref);
	}

	public Point getOrthogonalLocation(SVGFigure sourceNode,
			Point orthoReference) {
		PrecisionPoint ownReference = new PrecisionPoint(
				sourceNode.getReferencePoint());// 247,185
		PrecisionRectangle bounds = new PrecisionRectangle(sourceNode
				.getPolygonPoints().getBounds());
		bounds = new PrecisionRectangle(sourceNode.translateToAbsolute(bounds));
		bounds.expand(0.000001, 0.000001);
		PrecisionPoint preciseOrthoReference = new PrecisionPoint(
				orthoReference);
		int orientation = 0;
		if (bounds.contains(preciseOrthoReference)) {
			int side = getClosestSide(ownReference, bounds);
			switch (side) {
			case PositionConstants.LEFT:
			case PositionConstants.RIGHT:
				ownReference.preciseY = preciseOrthoReference.preciseY();
				orientation = PositionConstants.HORIZONTAL;
				break;
			case PositionConstants.TOP:
			case PositionConstants.BOTTOM:
				ownReference.preciseX = preciseOrthoReference.preciseX();
				orientation = PositionConstants.VERTICAL;
				break;
			}
		} else if (preciseOrthoReference.preciseX >= bounds.preciseX
				&& preciseOrthoReference.preciseX <= bounds.preciseX
						+ bounds.preciseWidth) {
			ownReference.preciseX = preciseOrthoReference.preciseX;
			orientation = PositionConstants.VERTICAL;
		} else if (preciseOrthoReference.preciseY >= bounds.preciseY
				&& preciseOrthoReference.preciseY <= bounds.preciseY
						+ bounds.preciseHeight) {
			ownReference.preciseY = preciseOrthoReference.preciseY;
			orientation = PositionConstants.HORIZONTAL;
		}
		ownReference.updateInts();
		Point location = getLocation(ownReference, preciseOrthoReference,
				sourceNode);
		if (location == null) {
			location = getLocation(orthoReference, sourceNode);
			orientation = PositionConstants.NONE;
		}
		if (orientation != PositionConstants.NONE) {
			PrecisionPoint loc = new PrecisionPoint(location);
			if (orientation == PositionConstants.VERTICAL) {
				loc.preciseX = preciseOrthoReference.preciseX;
			} else {
				loc.preciseY = preciseOrthoReference.preciseY;
			}
			loc.updateInts();
			location = loc;
		}

		return location;
	}

	public Point getLocation(Point reference, SVGFigure sourceNode) {
		Point ownReference = normalizeToStraightlineTolerance(reference,
				sourceNode.getReferencePoint(), STRAIGHT_LINE_TOLERANCE);

		Point location = getLocation(ownReference, reference, sourceNode);
		if (location == null) {
			location = getLocation(
					new PrecisionPoint(sourceNode.getReferencePoint()),
					reference, sourceNode);
			if (location == null) {
				location = sourceNode.getReferencePoint();
			}
		}

		return location;
	}

	protected Point normalizeToStraightlineTolerance(Point foreignReference,
			Point ownReference, int tolerance) {
		PrecisionPoint preciseOwnReference = new PrecisionPoint(ownReference);
		PrecisionPoint normalizedReference = (PrecisionPoint) preciseOwnReference
				.getCopy();
		PrecisionPoint preciseForeignReference = new PrecisionPoint(
				foreignReference);
		if (Math.abs(preciseForeignReference.preciseX
				- preciseOwnReference.preciseX) < tolerance) {
			normalizedReference.preciseX = preciseForeignReference.preciseX;
			normalizedReference.updateInts();
			return normalizedReference;
		}
		if (Math.abs(preciseForeignReference.preciseY
				- preciseOwnReference.preciseY) < tolerance) {
			normalizedReference.preciseY = preciseForeignReference.preciseY;
			normalizedReference.updateInts();
		}
		return normalizedReference;
	}

	protected Point getLocation(Point ownReference, Point foreignReference,
			SVGFigure sourceNode) {
		PointList intersections = getIntersectionPoints(ownReference,
				foreignReference, sourceNode);
		if (intersections != null && intersections.size() != 0) {
			Point location = pickClosestPoint(intersections, foreignReference);
			return location;
		}
		return null;
	}

	public Point pickClosestPoint(PointList points, Point p) {
		Point result = null;
		if (points.size() != 0) {
			result = points.getFirstPoint();
			for (int i = 1; i < points.size(); i++) {
				Point temp = points.getPoint(i);
				if (Math.abs(temp.x - p.x) < Math.abs(result.x - p.x))
					result = temp;
				else if (Math.abs(temp.y - p.y) < Math.abs(result.y - p.y))
					result = temp;
			}
		}
		return result;
	}

	protected PointList getIntersectionPoints(Point ownReference,
			Point foreignReference, SVGFigure sourceNode) {
		PointList polygon = sourceNode.getPolygonPoints();
		polygon = sourceNode.translateToAbsolute(polygon);
		return (new LineSeg(ownReference, foreignReference))
				.getLineIntersectionsWithLineSegs(polygon);
	}

	private int getClosestSide(Point p, Rectangle r) {
		double diff = Math.abs(r.preciseX() + r.preciseWidth() - p.preciseX());
		int side = PositionConstants.RIGHT;
		double currentDiff = Math.abs(r.preciseX() - p.preciseX());
		if (currentDiff < diff) {
			diff = currentDiff;
			side = PositionConstants.LEFT;
		}
		currentDiff = Math.abs(r.preciseY() + r.preciseHeight() - p.preciseY());
		if (currentDiff < diff) {
			diff = currentDiff;
			side = PositionConstants.BOTTOM;
		}
		currentDiff = Math.abs(r.preciseY() - p.preciseY());
		if (currentDiff < diff) {
			diff = currentDiff;
			side = PositionConstants.TOP;
		}
		return side;
	}

	private void normalizeToStraightLineTolerance(PointList line, int tolerance) {
		for (int i = 0; i < line.size() - 1; i++) {
			Point pt1 = line.getPoint(i);
			Point pt2 = line.getPoint(i + 1);
			if (Math.abs(pt1.x - pt2.x) < tolerance) {
				line.setPoint(new Point(pt1.x, pt2.y), i + 1);
			} else if (Math.abs(pt1.y - pt2.y) < tolerance) {
				line.setPoint(new Point(pt2.x, pt1.y), i + 1);
			}
		}
	}

	/**
	 * @param newLine
	 * @param lastStartAnchor
	 * @param lastEndAnchor
	 */
	private void removePointsInViews(SVGFigure source, SVGFigure target,
			PointList newLine, Point start, Point end) {
		if (source == target) {
			return;
		}

		PrecisionRectangle sr = new PrecisionRectangle(source
				.getPolygonPoints().getBounds());
		PrecisionRectangle rt = new PrecisionRectangle(target
				.getPolygonPoints().getBounds());
		if (sr != null) {
			sr = new PrecisionRectangle(source.translateToAbsolute(sr));
		}
		if (rt != null) {
			rt = new PrecisionRectangle(target.translateToAbsolute(rt));
		}
		Point lastRemovedFromSource = null;
		Point lastRemovedFromTarget = null;

		if (newLine.size() != 0
				&& sr.contains(new PrecisionPoint(newLine.getFirstPoint()))) {
			lastRemovedFromSource = newLine.removePoint(0);
			for (int i = 0; i < newLine.size()
					&& sr.contains(new PrecisionPoint(newLine.getPoint(i))); i++) {
				lastRemovedFromSource = newLine.removePoint(i--);
			}
		}

		if (newLine.size() != 0
				&& rt.contains(new PrecisionPoint(newLine.getLastPoint()))) {
			lastRemovedFromTarget = newLine.removePoint(newLine.size() - 1);
			for (int i = newLine.size(); i > 0
					&& rt.contains(new PrecisionPoint(newLine.getPoint(i - 1))); i--) {
				lastRemovedFromTarget = newLine.removePoint(i - 1);
			}
		}
		if (newLine.size() == 0) {
			Dimension tolerance = new Dimension(1, 0);
			int toleranceValue = tolerance.width;
			if (lastRemovedFromSource == null) {
				lastRemovedFromSource = start;
			}
			if (lastRemovedFromTarget == null) {
				lastRemovedFromTarget = end;
			}
			if (Math.abs(lastRemovedFromSource.x - lastRemovedFromTarget.x) < toleranceValue) {
				// Vertical
				if (sr.preciseY < rt.preciseY) {
					newLine.addPoint(lastRemovedFromSource.x,
							(sr.getBottom().y + rt.getTop().y) / 2);
				} else {
					newLine.addPoint(lastRemovedFromSource.x,
							(sr.getTop().y + rt.getBottom().y) / 2);
				}
			} else if (Math.abs(lastRemovedFromSource.y
					- lastRemovedFromTarget.y) < toleranceValue) {
				// Horizontal
				if (sr.preciseX < rt.preciseX) {
					newLine.addPoint((sr.getRight().x + rt.getLeft().x) / 2,
							lastRemovedFromSource.y);
				} else {
					newLine.addPoint((sr.getLeft().x + rt.getRight().x) / 2,
							lastRemovedFromSource.y);
				}
			} else if (BWDiagramUtil.BLANK.equals(source.getTerminal())
					&& BWDiagramUtil.BLANK.equals(target.getTerminal())) {
				if (lastRemovedFromSource != null
						&& lastRemovedFromTarget != null) {
					newLine.addPoint(
							(lastRemovedFromSource.x + lastRemovedFromTarget.x) / 2,
							(lastRemovedFromSource.y + lastRemovedFromTarget.y) / 2);
				} else {
					double startX = Math.max(sr.preciseX, rt.preciseX);
					double endX = Math.min(sr.preciseX + sr.preciseWidth,
							rt.preciseX + rt.preciseWidth);
					double startY = Math.max(sr.preciseY, rt.preciseY);
					double endY = Math.min(sr.preciseY + sr.preciseHeight,
							rt.preciseY + rt.preciseHeight);
					if (startX < endX) {
						if (sr.preciseY < rt.preciseY) {
							newLine.addPoint(
									(int) Math.round((startX + endX) / 2.0),
									(sr.getBottom().y + rt.getTop().y) / 2);
						} else {
							newLine.addPoint(
									(int) Math.round((startX + endX) / 2.0),
									(sr.getTop().y + rt.getBottom().y) / 2);
						}
					} else if (startY < endY) {
						if (sr.preciseX < rt.preciseX) {
							newLine.addPoint(
									(sr.getRight().x + rt.getLeft().x) / 2,
									(int) Math.round((startY + endY) / 2.0));
						} else {
							newLine.addPoint(
									(sr.getLeft().x + rt.getRight().x) / 2,
									(int) Math.round((startY + endY) / 2.0));
						}
					}
				}
			}
		}
	}

	private boolean removeRedundantPoints(PointList line) {
		int initialNumberOfPoints = line.size();
		if (line.size() > 2) {
			PointList newLine = new PointList(line.size());
			newLine.addPoint(line.removePoint(0));
			while (line.size() >= 2) {
				Point p0 = newLine.getLastPoint();
				Point p1 = line.getPoint(0);
				Point p2 = line.getPoint(1);
				if (p0.x == p1.x && p0.x == p2.x) {
					line.removePoint(0);
				} else if (p0.y == p1.y && p0.y == p2.y) {
					line.removePoint(0);
				} else {
					newLine.addPoint(line.removePoint(0));
				}
			}
			while (line.size() > 0) {
				newLine.addPoint(line.removePoint(0));
			}
			line.removeAllPoints();
			line.addAll(newLine);
		}
		return line.size() != initialNumberOfPoints;
	}

	private int[] splitBentPoint(String bendpoint) {
		bendpoint = bendpoint.replaceAll("\\[", "");
		bendpoint = bendpoint.replaceAll("]", "");
		String[] pointAll = bendpoint.split(",");
		int[] points = new int[pointAll.length];
		for (int i = 0; i < pointAll.length; i++) {
			points[i] = Integer.parseInt(pointAll[i].trim());
		}
		return points;
	}

	public Rectangle calcuateSourceOrTargetLocation(SVGFigure node) {
		Rectangle sourceBounds = node.getHandlerBounds().getCopy();
		// after getHandlerBounds
		sourceBounds.y -= 10;
		sourceBounds.height += 20;
		PrecisionRectangle source = new PrecisionRectangle(sourceBounds);
		source = node.translateToAbsolute(source);
		return source;
	}

	public SVGFigure addRectangle(SVGFigure figure, BWNode node, Element child,
			String type, boolean collapsed) {
		Element element = node.getElement();
		SVGFigure childFigure = null;
		if ("flow".equals(element.getName())) {
			childFigure = new FlowFigure(figure);
			childFigure.setNode(node);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			node.setFigure(childFigure);
			childFigure.setAbsolutePoint(new Point(5, 5));
			Rectangle rectangle = childFigure.getElementRectangle(child);
			if (figure.getBounds() != null) {
				rectangle = figure.getBounds();
			}
			childFigure.setIsCollapse(figure.getIsCollapse());
			childFigure.setBounds(rectangle);
		} else if ("scope".equals(element.getName())) {
			childFigure = new ScopeFigure(figure);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			childFigure.setNode(node);
			node.setFigure(childFigure);
			Rectangle rect = childFigure.getElementRectangle(child);
			childFigure.setBounds(rect);
			if (figure.getIsCollapse()) {
				childFigure.setIsCollapse(figure.getIsCollapse());
			} else {
				childFigure.setIsCollapse(collapsed);
			}
			childFigure.adjustScopeEditPart(node, childFigure, type, child,
					rect, 0, 0);
		} else if ("onAlarm".equals(element.getName())) {
			childFigure = new OnAlarmFigure(figure);
			childFigure.setNode(node);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			node.setFigure(childFigure);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			childFigure.setBounds(rectangle);
		} else if ("faultHandlers".equals(element.getName())) {
			childFigure = new ScopeFaultHandlersFigure(figure);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			childFigure.setNode(node);
			node.setFigure(childFigure);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			childFigure.setBounds(rectangle);
			if (figure.getIsCollapse()) {
				childFigure.setIsCollapse(figure.getIsCollapse());
			} else {
				childFigure.setIsCollapse(collapsed);
			}
			childFigure.setAbsolutePoint(new Point(rectangle.x, rectangle.y));
		} else if ("eventHandlers".equals(element.getName())) {
			childFigure = new ScopeEventHandlersFigure(figure);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			childFigure.setNode(node);
			node.setFigure(childFigure);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			childFigure.setBounds(rectangle);
			childFigure.setAbsolutePoint(new Point(rectangle.x, rectangle.y));
			if (figure.getIsCollapse()) {
				childFigure.setIsCollapse(figure.getIsCollapse());
			} else {
				childFigure.setIsCollapse(collapsed);
			}
		} else if ("catch".equals(element.getName())) {
			childFigure = new CatchFigure(figure);
			childFigure.setNode(node);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			node.setFigure(childFigure);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			childFigure.setBounds(rectangle);
			if (figure.getIsCollapse()) {
				childFigure.setIsCollapse(figure.getIsCollapse());
			} else {
				childFigure.setIsCollapse(collapsed);
			}
		} else if ("onEvent".equals(element.getName())) {
			childFigure = new ScopeOnEventFigure(figure);
			childFigure.setNode(node);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			node.setFigure(childFigure);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			childFigure.setAbsolutePoint(new Point(rectangle.x, rectangle.y));
			childFigure.setBounds(rectangle);
		} else if ("pick".equals(element.getName())) {
			childFigure = new PickFigure(figure);
			childFigure.setNode(node);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			node.setFigure(childFigure);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			childFigure.setBounds(rectangle);
			if (figure.getIsCollapse()) {
				childFigure.setIsCollapse(figure.getIsCollapse());
			} else {
				childFigure.setIsCollapse(collapsed);
			}
		} else if ("onMessage".equals(element.getName())) {
			childFigure = new OnMessageFigure(figure);
			childFigure.setNode(node);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			node.setFigure(childFigure);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			childFigure.setBounds(rectangle);
			childFigure.setAbsolutePoint(new Point(rectangle.x, rectangle.y));
			if (figure.getIsCollapse()) {
				childFigure.setIsCollapse(figure.getIsCollapse());
			} else {
				childFigure.setIsCollapse(collapsed);
			}
		} else if ("catchAll".equals(element.getName())) {
			childFigure = new ScopeCatchAllFigure(figure);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			childFigure.setNode(node);
			node.setFigure(childFigure);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			childFigure.setBounds(rectangle);
			childFigure.setIsCollapse(collapsed);
		} else if ("compensationHandler".equals(element.getName())) {
			childFigure = new ScopeCompartmentFigure(figure);
			childFigure.setNode(node);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			node.setFigure(childFigure);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			childFigure.setBounds(rectangle);
			if (figure.getBounds() != null) {
				childFigure.setAbsolutePoint(new Point(
						figure.getBounds().width, 0));
			}
			if (figure.getIsCollapse()) {
				childFigure.setIsCollapse(figure.getIsCollapse());
			} else {
				childFigure.setIsCollapse(collapsed);
			}
		} else if ("process".equals(element.getName())) {
			childFigure = new ProcessFigure(figure);
			childFigure.setNode(node);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			if (rectangle != null) {
				childFigure.setBounds(rectangle);
			}
		} else {
			childFigure = new SVGFigure(figure);
			childFigure.setNode(node);
			childFigure.setType(type);
			childFigure.setDiagram(child);
			node.setFigure(childFigure);
			Rectangle rectangle = childFigure.getElementRectangle(child);
			if (rectangle != null) {
				childFigure.setBounds(rectangle);
			}
		}
		if (!BWDiagramUtil.INSTANCE.isGroupStart(node.getElement())
				&& !BWDiagramUtil.INSTANCE.isGroupEnd(node.getElement())
				&& !BWDiagramUtil.isOnMessageActivity(node.getElement())) {
			figure.createBorderLayerFigure(
					childFigure.getBounds().x < 0 ? childFigure.getBounds().x
							: 0,
					childFigure.getBounds().y < 0 ? childFigure.getBounds().y
							: 0);
		}

		String activityTypeId = BWDiagramUtil.INSTANCE.getActivityTypeId(node
				.getElement());
		if (activityTypeId != null) {
			if (activityTypeId.lastIndexOf(".") > 0) {
				activityTypeId = activityTypeId.substring(
						activityTypeId.lastIndexOf(".") + 1,
						activityTypeId.length());
			}
			childFigure.setId(activityTypeId);
		}
		return childFigure;
	}

	public BWNode findElementFromScope(Element scope, BWNode node,
			Boolean isStart, int index) {
		int size = scope.elements().size();
		List<Element> links = new ArrayList<Element>();
		List<Element> activies = new ArrayList<Element>();
		List<Element> catches = new ArrayList<Element>();
		List<Element> events = new ArrayList<Element>();
		List<Element> messages = new ArrayList<Element>();
		List<Element> variables = new ArrayList<Element>();
		for (int i = 0; i < size; i++) {
			Element element = (Element) scope.elements().get(i);
			if (element.getName().equals("links")) {
				// links.add(element);
				Object linkElement = element.elements("link");
				if (linkElement instanceof Element) {
					BWNode link = new BWNode(node);
					node.addChildNode(link);
					node.addLinks(link);
					link.setElement((Element) linkElement);
					link.setSelfId(node.getSelfId() + "/@links/@children." + i);
				} else if (linkElement instanceof List) {
					List<Element> childList = (List) linkElement;
					for (int t = 0; t < childList.size(); t++) {
						Element celement = childList.get(t);
						BWNode link2 = new BWNode(node);
						node.addChildNode(link2);
						node.addLinks(link2);
						link2.setElement(celement);
						link2.setSelfId(node.getSelfId() + "/@links/@children."
								+ t);
					}
				}

			} else if (containsBWElementKey(element.getName())) {
				if (element.getName().equals("faultHandlers")) {
					BWNode faultHandler = new BWNode(node);
					node.addChildNode(faultHandler);
					node.addFaultHandlers(faultHandler);
					faultHandler.setElement(element);
					faultHandler
							.setSelfId(node.getSelfId() + "/@faultHandlers");
					findElementFromScope(element, faultHandler, false, -1);
				} else if ("eventHandlers".equals(element.getName())) {
					BWNode eventHandler = new BWNode(node);
					node.addChildNode(eventHandler);
					node.addEventHandlers(eventHandler);
					eventHandler.setElement(element);
					eventHandler
							.setSelfId(node.getSelfId() + "/@eventHandlers");
					findElementFromScope(element, eventHandler, false, -1);
				} else if (element.getName().equals("compensationHandler")) {
					BWNode compensationHandler = new BWNode(node);
					node.addChildNode(compensationHandler);
					node.addCompensationHandlers(compensationHandler);
					compensationHandler.setElement(element);
					compensationHandler.setSelfId(node.getSelfId()
							+ "/@compensationHandler");
					findElementFromScope(element, compensationHandler, false,
							-1);
				} else if (element.getName().equals("catch")) {
					catches.add(element);
				} else if (element.getName().equals("catchAll")) {
					BWNode catchAll = new BWNode(node);
					node.addChildNode(catchAll);
					node.addCatchAlls(catchAll);
					catchAll.setElement(element);
					catchAll.setSelfId(node.getSelfId() + "/@catchAll");
					findElementFromScope(element, catchAll, false, -1);
				} else if (element.getName().equals("onEvent")) {
					events.add(element);
				} else if ("onMessage".equals(element.getName())) {
					messages.add(element);
				} else if ("variables".equals(element.getName())) {
					variables.add(element);
				} else {
					activies.add(element);
				}
			}
		}

		for (int i = 0; i < variables.size(); i++) {
			Element element = variables.get(i);
			BWNode link = new BWNode(node);
			node.addChildNode(link);
			node.addVariables(link);
			link.setElement(element);
			link.setSelfId(node.getSelfId() + "/@variables");
		}

		for (int i = 0; i < catches.size(); i++) {
			Element element = catches.get(i);
			BWNode catchNode = new BWNode(node);
			node.addChildNode(catchNode);
			node.addCatches(catchNode);
			catchNode.setElement(element);
			catchNode.setSelfId(node.getSelfId() + "/@catch." + i);
			findElementFromScope(element, catchNode, false, -1);
		}
		for (int i = 0; i < events.size(); i++) {
			Element element = events.get(i);
			BWNode eventNode = new BWNode(node);
			node.addChildNode(eventNode);
			node.addEvents(eventNode);
			eventNode.setElement(element);
			eventNode.setSelfId(node.getSelfId() + "/@events." + i);
			findElementFromScope(element, eventNode, false, -1);
		}

		for (int i = 0; i < messages.size(); i++) {
			Element element = messages.get(i);
			BWNode messageNode = new BWNode(node);
			node.addChildNode(messageNode);
			node.addMessages(messageNode);
			messageNode.setElement(element);
			messageNode.setSelfId(node.getSelfId() + "/@messages." + i);
			findElementFromScope(element, messageNode, false, -1);
		}

		if (activies.size() > 1) {
			for (int i = 0; i < activies.size(); i++) {
				Element element = activies.get(i);
				BWNode root = new BWNode(node);
				node.addChildNode(root);
				node.addAvtivities(root);
				root.setElement(element);
				root.setSelfId(node.getSelfId() + "/@activities." + i);
				findElementFromScope(element, root, false, i);
			}
		} else if (activies.size() == 1) {
			Element element = activies.get(0);
			BWNode root = new BWNode(node);
			node.addChildNode(root);
			node.addAvtivities(root);
			root.setElement(element);
			root.setSelfId(node.getSelfId() + "/@activity");
			findElementFromScope(element, root, false, -1);
		}

		return node;
	}

	/**
	 * @param name
	 * @return
	 */
	private boolean containsBWElementKey(String name) {
		boolean isContains = false;
		if ("extensionActivity".equals(name)) {
			isContains = true;
		} else if ("empty".equals(name)) {
			isContains = true;
		} else if ("flow".equals(name)) {
			isContains = true;
		} else if ("scope".equals(name)) {
			isContains = true;
		} else if ("invoke".equals(name)) {
			isContains = true;
		} else if ("receiveEvent".equals(name)) {
			isContains = true;
		} else if ("receive".equals(name)) {
			isContains = true;
		} else if ("reply".equals(name)) {
			isContains = true;
		} else if ("faultHandlers".equals(name)) {
			isContains = true;
		} else if ("catch".equals(name)) {
			isContains = true;
		} else if ("pick".equals(name)) {
			isContains = true;
		} else if ("onMessage".equals(name)) {
			isContains = true;
		} else if ("catchAll".equals(name)) {
			isContains = true;
		} else if ("compensationHandler".equals(name)) {
			isContains = true;
		} else if ("process".equals(name)) {
			isContains = true;
		} else if ("forEach".equals(name)) {
			isContains = true;
		} else if ("eventHandlers".equals(name)) {
			isContains = true;
		} else if ("onEvent".equals(name)) {
			isContains = true;
		} else if ("onAlarm".equals(name)) {
			isContains = true;
		} else if ("variables".equals(name)) {
			isContains = true;
		} else if ("repeatUntil".equals(name)) {
			isContains = true;
		} else if ("assign".equals(name)) {
			isContains = true;
		} else if ("throw".equals(name)) {
			isContains = true;
		} else if ("copy".equals(name)) {
			isContains = true;
		} else if ("rethrow".equals(name)) {
			isContains = true;
		} else if ("wait".equals(name)) {
			isContains = true;
		} else if ("opaqueActivity".equals(name)) {
			isContains = true;
		} else if ("forEach".equals(name)) {
			isContains = true;
		} else if ("validate".equals(name)) {
			isContains = true;
		} else if ("if".equals(name)) {
			isContains = true;
		} else if ("compensate".equals(name)) {
			isContains = true;
		} else if ("compensateScope".equals(name)) {
			isContains = true;
		} else if ("messageExchange".equals(name)) {
			isContains = true;
		} else if ("import".equals(name)) {
			isContains = true;
		} else if ("terminationHandler".equals(name)) {
			isContains = true;
		} else if ("elseIf".equals(name)) {
			isContains = true;
		} else if ("else".equals(name)) {
			isContains = true;
		} else if ("while".equals(name)) {
			isContains = true;
		} else if ("extension".equals(name)) {
			isContains = true;
		} else if ("correlation".equals(name)) {
			isContains = true;
		}
		return isContains;
	}
}
