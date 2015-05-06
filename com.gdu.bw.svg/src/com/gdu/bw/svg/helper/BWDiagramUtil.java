
package com.gdu.bw.svg.helper;

import java.util.List;

import org.dom4j.Element;

import com.gdu.bw.svg.model.Rectangle;
import com.gdu.bw.svg.model.figure.SVGFigure;
import com.gdu.bw.svg.model.figure.ScopeCompartmentFigure;
import com.gdu.bw.svg.model.figure.ScopeEventHandlersFigure;
import com.gdu.bw.svg.model.figure.ScopeFaultHandlersFigure;
import com.gdu.bw.svg.model.figure.ScopeFigure;

/**
 * @author <a>Davy Du</a>
 * 
 * @since 1.0.0
 */
public class BWDiagramUtil {

	public static BWDiagramUtil INSTANCE = new BWDiagramUtil();
	public static final String GROUP_START = "groupStart"; //$NON-NLS-1$
	public static final String GROUP_END = "groupEnd";
	public static final String ONMESSAGE_START = "onMessageStart"; //$NON-NLS-1$
	public static final String ONMESSAGE_END = "onMessageEnd";
	public static final String GROUP_ATTRIBUTE_NAME = "group";
	public static final String CONSTRUCTOR_ATTRIBUTE_NAME = "constructor";
	public static final String BLANK = "";

	public boolean isRootScope(Element scope) {
		if (isOnMessageActivity(scope) || isOnAlarmActivity(scope)
				|| isScopeActivity(scope) || isOnEventActivity(scope)
				|| isOnReceiveEventActivity(scope) || isCatchActivity(scope)
				|| isCatchAllActivity(scope) || isCompensationActivity(scope)) {
			return true;
		}
		return false;
	}

	public static boolean isOnMessageActivity(Element scope) {

		if (scope == null || scope.getParent() == null) {
			return false;
		}
		Element parent = scope.getParent();
		if ("onMessage".equals(parent.getName())) {
			return true;
		}

		if ("scope".equals(parent.getName())) {
			if ("onMessage".equals(parent.getParent().getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isCompensationActivity(Element activity) {
		if (activity == null || activity.getParent() == null) {
			return false;
		}

		if ("compensationHandler".equals(activity.getParent().getName())) {
			return true;
		}

		if ("scope".equals(activity.getParent().getName())) {
			Element scope = activity.getParent();
			if ("compensationHandler".equals(scope.getParent().getName())) {
				return true;
			}
		}
		return false;

	}

	public boolean isCatchAllActivity(Element activity) {
		if (activity == null || activity.getParent() == null) {
			return false;
		}

		if ("catchAll".equals(activity.getParent().getName())) {
			return true;
		}

		if ("scope".equals(activity.getParent().getName())) {
			Element scope = activity.getParent();
			if ("catchAll".equals(scope.getParent().getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isCatchAll(Element activity) {
		if ("catchAll".equals(activity.getName())) {
			return true;
		}
		return false;
	}

	public boolean isCatchActivity(Element activity) {
		if (activity == null || activity.getParent() == null) {
			return false;
		}

		if ("catch".equals(activity.getParent().getName())) {
			return true;
		}

		if ("scope".equals(activity.getParent().getName())) {
			Element scope = activity.getParent();
			if ("catch".equals(scope.getParent().getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isOnReceiveEventActivity(Element activity) {
		if (activity == null || activity.getParent() == null) {
			return false;
		}

		if ("onReceiveEvent".equals(activity.getParent().getName())) {
			return true;
		}

		if ("scope".equals(activity.getParent().getName())) {
			Element scope = activity.getParent();
			if ("onReceiveEvent".equals(scope.getParent().getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isOnEventActivity(Element activity) {
		if (activity == null || activity.getParent() == null) {
			return false;
		}

		if ("onEvent".equals(activity.getParent().getName())) {
			return true;
		}

		if ("scope".equals(activity.getParent().getName())) {
			Element scope = activity.getParent();
			if ("onEvent".equals(scope.getParent().getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isOnAlarmActivity(Element activity) {
		if (activity == null || activity.getParent() == null) {
			return false;
		}
		if ("onAlarm".equals(activity.getParent().getName())) {
			return true;
		}

		if ("scope".equals(activity.getParent().getName())) {
			Element scope = activity.getParent();
			if ("onAlarm".equals(scope.getParent().getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isScopeActivity(Element activity) {
		if (activity == null || activity.getParent() == null) {
			return false;
		}
		if ("scope".equals(activity.getParent().getName())) {
			return true;
		}
		return false;
	}

	public boolean isProcessActivity(Element activity) {
		if (activity == null || activity.getParent() == null) {
			return false;
		}
		if ("process".equals(activity.getParent().getName())) {
			return true;
		}

		if ("scope".equals(activity.getParent().getName())) {
			Element scope = activity.getParent();
			if ("process".equals(scope.getParent().getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param element
	 * @return
	 */
	public boolean isGroupStart(Element element) {
		if (element != null) {// tibex:group
			String value = getTibexAttribute(element, GROUP_ATTRIBUTE_NAME);
			if (GROUP_START.equals(value)) {
				return true;
			}
			value = getGroupAttrbiuteValueFromElement(element);
			if (GROUP_START.equals(value)) {
				return true;
			}
			for (Object object : element.elements()) {
				if (object instanceof Element) {
					Element childElement = (Element) object;
					value = getGroupAttrbiuteValueFromElement(childElement);
					if (GROUP_START.equals(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getGroupAttrbiuteValueFromElement(Element element) {
		if (element != null && element.attribute(GROUP_ATTRIBUTE_NAME) != null) {
			return element.attribute(GROUP_ATTRIBUTE_NAME).getValue();
		}
		return null;
	}

	/**
	 * @param element
	 * @return
	 */
	public boolean isGroupEnd(Element element) {
		if (element != null) {// tibex:group
			String value = getTibexAttribute(element, GROUP_ATTRIBUTE_NAME);
			if (GROUP_END.equals(value)) {
				return true;
			}
			value = getGroupAttrbiuteValueFromElement(element);
			if (GROUP_END.equals(value)) {
				return true;
			}
			for (Object object : element.elements()) {
				if (object instanceof Element) {
					Element childElement = (Element) object;
					value = getGroupAttrbiuteValueFromElement(childElement);
					if (GROUP_END.equals(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * @param element
	 * @return
	 */
	public boolean isOnMessageStart(Element element) {
		if (element != null) {
			String value = getTibexAttribute(element,
					CONSTRUCTOR_ATTRIBUTE_NAME);
			if (ONMESSAGE_START.equals(value)) { //$NON-NLS-1$
				return true;
			}
			value = getMessageAttrbiuteValueFromElement(element);
			if (ONMESSAGE_START.equals(value)) { //$NON-NLS-1$
				return true;
			}
		}
		return false;
	}

	/**
	 * @param element
	 * @return
	 */
	private String getMessageAttrbiuteValueFromElement(Element element) {
		if (element != null
				&& element.attribute(CONSTRUCTOR_ATTRIBUTE_NAME) != null) {
			return element.attribute(CONSTRUCTOR_ATTRIBUTE_NAME).getValue();
		}
		return null;
	}

	/**
	 * @param element
	 * @return
	 */
	public boolean isOnMessageEnd(Element element) {
		if (element != null) {
			String value = getTibexAttribute(element,
					CONSTRUCTOR_ATTRIBUTE_NAME);
			if (ONMESSAGE_END.equals(value)) { //$NON-NLS-1$
				return true;
			}
			value = getMessageAttrbiuteValueFromElement(element);
			if (ONMESSAGE_END.equals(value)) { //$NON-NLS-1$
				return true;
			}
		}
		return false;
	}

	public String getTibexAttribute(Element element, String attributeTag) {
		if (element != null && attributeTag != null) {
			for (Object e : element.elements()) {
				if (e instanceof Element) {
					Element child = (Element) e;
					if (child.attribute(attributeTag) != null) {
						return child.attributeValue(attributeTag);
					}
				}
			}
		}
		return null;
	}

	public boolean isCollapsed(Object editPart) {
		List elements = ((Element) editPart).elements("styles");
		for (Object object : elements) {
			if (object instanceof Element) {
				Element element = (Element) object;
				if (element.attribute("collapsed") != null) {
					return true;
				}
			}
		}
		return false;
	}

	public Element getEventHandlers(Element element) {
		if (element.element("eventHandlers") != null) {
			return element.element("eventHandlers");
		}
		return null;
	}

	public Element getFaultHandlers(Element element) {
		if (element.element("faultHandlers") != null) {
			return element.element("faultHandlers");
		}
		return null;
	}

	/**
	 * @param faultHandler
	 * @return
	 */
	public List getCatch(Element faultHandler) {
		List list = faultHandler.elements("catch");
		return list;
	}

	/**
	 * @param faultHandler
	 * @return
	 */
	public Element getCatchAll(Element faultHandler) {
		Object object = faultHandler.element("catchAll");
		if (object != null) {
			return (Element) object;
		} else {
			Element element = faultHandler.element("faultHandlers");
			if (element != null) {
				return element.element("catchAll");
			}
		}
		return null;
	}

	public List getCatchAlls(Element faultHandler) {
		List list = faultHandler.elements("catchAll");
		return list;
	}

	/**
	 * @param scope
	 * @return
	 */
	public Element getCompensationHandler(Element scope) {
		Element element = scope.element("compensationHandler");
		return element;
	}

	public SVGFigure getCompensationHandler(SVGFigure figure) {
		if (figure.getParentFigure() != null) {
			SVGFigure parent = figure.getParentFigure();
			if (!parent.getChildList().isEmpty()) {
				for (SVGFigure child : parent.getChildList()) {
					if (child instanceof ScopeCompartmentFigure) {
						return child;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param diagram
	 * @return
	 */
	public Object getScopeCompensationHandlerEditPart(Element diagram) {
		Object handler = null;
		List elements = diagram.elements();
		for (Object object : elements) {
			if (object instanceof Element) {
				Element element = (Element) object;
				if ("3018".equals(element.attributeValue("type"))) {
					for (Object child : element.elements()) {
						if (object instanceof Element) {
							Element element2 = (Element) child;
							if ("4023".equals(element2.attributeValue("type"))) {
								handler = element2;
							}
						}
					}
				}
			}
		}
		return handler;
	}

	/**
	 * @param scope
	 */
	public Object getScopeActivityEditPart(Element diagram) {
		Object handler = null;
		List elements = diagram.elements();
		for (Object object : elements) {
			if (object instanceof Element) {
				Element element = (Element) object;
				if ("3018".equals(element.attributeValue("type"))) {
					for (Object child : element.elements()) {
						if (object instanceof Element) {
							Element element2 = (Element) child;
							if ("4020".equals(element2.attributeValue("type"))) {
								handler = element2;
							}
						}
					}
				}
			}
		}
		return handler;
	}

	/**
	 * @param editPart
	 */
	public Object getScopeEventHandlersEditPart(Element diagram) {
		Object handler = null;
		List elements = diagram.elements();
		for (Object object : elements) {
			if (object instanceof Element) {
				Element element = (Element) object;
				if ("3018".equals(element.attributeValue("type"))) {
					for (Object child : element.elements()) {
						if (object instanceof Element) {
							Element element2 = (Element) child;
							if ("4021".equals(element2.attributeValue("type"))) {
								handler = element2;
							}
						}
					}
				}
			}
		}
		return handler;
	}

	public Object getScopeEventHandlersFromScope(Element diagram) {
		Object handler = null;
		List elements = diagram.elements();
		for (Object object : elements) {
			if (object instanceof Element) {
				Element element = (Element) object;
				if ("4021".equals(element.attributeValue("type"))) {
					handler = element;

				}
			}
		}
		return handler;
	}

	/**
	 * @param editPart
	 */
	public SVGFigure getScopeCatchAllEditPart(SVGFigure diagram) {
		SVGFigure handler = null;
		List<SVGFigure> elements = diagram.getChildList();
		for (SVGFigure object : elements) {
			if ("3032".equals(object.getType())) {
				if (object.getChildList() != null
						&& !object.getChildList().isEmpty()) {
					SVGFigure object2 = object.getChildList().get(0);
					Element element = object2.getNode().getElement();
					if ("scope".equals(element.getName())) {
						handler = object2;
					}
				}
			}

		}
		return handler;
	}

	public SVGFigure getOnEventChildScopeEditPart(SVGFigure diagram) {
		SVGFigure handler = null;
		List<SVGFigure> elements = diagram.getChildList();
		for (SVGFigure object : elements) {
			if ("3026".equals(object.getType())) {
				if (object.getChildList() != null
						&& !object.getChildList().isEmpty()) {
					SVGFigure object2 = object.getChildList().get(0);
					Element element = object2.getNode().getElement();
					if ("scope".equals(element.getName())) {
						handler = object2;
					}
				}
			}

		}
		return handler;
	}

	public Boolean isScopeCatchAllEditPart(Element diagram) {
		if ("catchAll".equals(diagram.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * @param childScopeEditPart
	 * @param i
	 * @return
	 */
	public int getHeightAdjustForFaultHandlers(Element element,
			Object childScopeEditPart, int offset) {
		if (childScopeEditPart instanceof Element) {
			Element scope = (Element) childScopeEditPart;
			if ("scope".equals(element.getName())) {
				int heightAdjust = 0;
				Element faultHandler = this.getFaultHandlers(element);
				if (faultHandler == null) {
					heightAdjust = 0;
				} else {
					List catches = this.getCatch(element);
					Element catchAll = this.getCatchAll(element);
					if ((catches == null || catches.isEmpty())
							&& catchAll == null) {
						heightAdjust = 0;
					} else {
						Object object = getScopeFaultHandlersEditPart(scope);
						if (object != null && object instanceof Element) {
							Element node = (Element) object;
							Element layout = node.element("layoutConstraint");
							if (layout != null
									&& layout.attribute("height") != null) {
								int height = Integer.parseInt(layout
										.attributeValue("height"));
								heightAdjust = height - 30;
							}
						}
					}
				}
				if (heightAdjust == 0) {
					heightAdjust = 0;
				}
				heightAdjust += offset;
				if (heightAdjust < 0) {
					heightAdjust = 0;
				}
				return heightAdjust;
			}
		}
		return 0;
	}

	/**
	 * @param scope
	 */
	public Object getScopeFaultHandlersEditPart(Element diagram) {
		Object handler = null;
		List elements = diagram.elements();
		for (Object object : elements) {
			if (object instanceof Element) {
				Element element = (Element) object;
				if ("3018".equals(element.attributeValue("type"))) {
					for (Object child : element.elements()) {
						if (object instanceof Element) {
							Element element2 = (Element) child;
							if ("4022".equals(element2.attributeValue("type"))) {
								handler = element2;
							}
						}
					}
				}
			}
		}
		return handler;
	}

	/**
	 * @param editPart
	 * @return
	 */
	public boolean isScopeCatchEditPart(Element diagram) {
		if ("catch".equals(diagram.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * @param editPart
	 * @return
	 */
	public SVGFigure getCatchChildScopeEditPart(SVGFigure diagram) {
		SVGFigure handler = null;
		List<SVGFigure> elements = diagram.getChildList();
		if (elements != null) {
			for (SVGFigure object : elements) {
				if ("3030".equals(object.getType())) {
					if (object.getChildList() != null
							&& !object.getChildList().isEmpty()) {
						SVGFigure object2 = object.getChildList().get(0);
						Element element = object2.getNode().getElement();
						if ("scope".equals(element.getName())) {
							handler = object2;
						}
					}
				}
			}
		}
		return handler;
	}

	public SVGFigure getCatchParentScopeEditPart(SVGFigure diagram) {
		SVGFigure handler = null;
		List<SVGFigure> elements = diagram.getChildList();
		if (elements != null) {
			for (SVGFigure object : elements) {
				if ("3030".equals(object.getType())) {
					if (object.getChildList() != null
							&& !object.getChildList().isEmpty()) {
						SVGFigure object2 = object.getChildList().get(0);
						Element element = object2.getNode().getElement();
						if ("scope".equals(element.getName())) {
							handler = object2;
						}
					}
				}
			}
		}
		return handler;
	}

	/**
	 * @param scope
	 * @return
	 */
	public List getVariables(Element scope) {
		return scope.elements("variables");
	}

	/**
	 * @param scope
	 * @return
	 */
	public Object getActivities(Element scope) {
		return scope.elements().get(0);
	}

	/**
	 * @param event
	 */
	public List getEvents(Element event) {
		return event.elements("onEvent");
	}

	/**
	 * @param event
	 * @return
	 */
	public List getAlarms(Element event) {
		return event.elements("alarm");
	}

	/**
	 * @param event
	 * @return
	 */
	public List getExtensibilityElements(Element event) {
		return event.elements("extensionActivity");
	}

	/**
	 * @param diagram
	 * @return
	 */
	public boolean isPinned(Element diagram) {
		Element element = diagram.element("styles");
		if (element != null) {
			if ("resizingStyle".equals(element.attributeValue("type"))) {
				return element.attribute("pinned") != null;
			}
		}
		return false;
	}

	/**
	 * @param onMessageFigure
	 * @return
	 */
	public SVGFigure getOnMessageChildScopeEditPart(SVGFigure diagram) {
		SVGFigure handler = null;
		List<SVGFigure> elements = diagram.getChildList();
		for (SVGFigure object : elements) {
			if ("3013".equals(object.getType())) {
				if (object.getChildList() != null
						&& !object.getChildList().isEmpty()) {
					SVGFigure object2 = object.getChildList().get(0);
					Element element = object2.getNode().getElement();
					if ("scope".equals(element.getName())) {
						handler = object2;
					}
				}
			}

		}
		return handler;
	}

	/**
	 * @param onAlarmFigure
	 * @return
	 */
	public SVGFigure getOnAlarmChildScopeEditPart(SVGFigure diagram) {
		SVGFigure handler = null;
		List<SVGFigure> elements = diagram.getChildList();
		for (SVGFigure object : elements) {
			if ("3028".equals(object.getType())) {
				if (object.getChildList() != null
						&& !object.getChildList().isEmpty()) {
					SVGFigure object2 = object.getChildList().get(0);
					Element element = object2.getNode().getElement();
					if ("scope".equals(element.getName())) {
						handler = object2;
					}
				}
			}

		}
		return handler;
	}

	/**
	 * @param scopeFigure
	 * @return
	 */
	public SVGFigure getFaultHandlerFigure(SVGFigure figure) {
		if (figure.getParentFigure() != null) {
			SVGFigure parent = figure.getParentFigure();
			if (!parent.getChildList().isEmpty()) {
				for (SVGFigure child : parent.getChildList()) {
					if (child instanceof ScopeFaultHandlersFigure) {
						return child;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param scopeFigure
	 * @return
	 */
	public SVGFigure getEventHandlerFigure(SVGFigure figure) {
		if (figure.getParentFigure() != null) {
			SVGFigure parent = figure.getParentFigure();
			if (!parent.getChildList().isEmpty()) {
				for (SVGFigure child : parent.getChildList()) {
					if (child instanceof ScopeEventHandlersFigure) {
						return child;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param scopeFaultHandlersFigure
	 * @return
	 */
	public ScopeFigure getFaultFlow(SVGFigure figure) {
		if (figure.getParentFigure() != null) {
			SVGFigure parent = figure.getParentFigure();
			if (!parent.getChildList().isEmpty()) {
				for (SVGFigure child : parent.getChildList()) {
					if (child instanceof ScopeFigure) {
						return (ScopeFigure) child;
					}
				}
			}
		}
		return null;
	}

	public ScopeFigure getFaultScope(SVGFigure figure) {
		if (figure.getParentFigure() != null) {
			SVGFigure parent = figure.getParentFigure();
			if (parent != null) {
				SVGFigure scopeFigure = parent.getParentFigure();
				if (scopeFigure instanceof ScopeFigure) {
					return (ScopeFigure) scopeFigure;
				}
			}
		}
		return null;
	}

	/**
	 * @param element
	 * @return
	 */
	public boolean isScopeOnEventEditPart(Element element) {
		if ("onEvent".equals(element.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * @param element
	 * @return
	 */
	public boolean isScopeOnAlarmEditPart(Element element) {
		if ("onAlarm".equals(element.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * @param processFigure
	 * @return
	 */
	public ScopeFigure getProcessScopeEditPart(SVGFigure processFigure) {
		for (SVGFigure childFigure : processFigure.getChildList()) {
			if ("3004".equals(childFigure.getType())) {
				for (SVGFigure scopeFigure : childFigure.getChildList()) {
					if (scopeFigure instanceof ScopeFigure) {
						return (ScopeFigure) scopeFigure;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param childScopeEditPart
	 * @return
	 */
	public int getHeightAdjustForFaultHandlers(ScopeFigure childScopeEditPart) {
		Element scope = childScopeEditPart.getNode().getElement();
		if (scope == null) {
			return 0;
		}
		int heightAdjust = 0;
		Object faultHandlersEditPart = getScopeFaultHandlersEditPart(childScopeEditPart
				.getDiagram());
		if (faultHandlersEditPart != null
				&& faultHandlersEditPart instanceof Element) {
			Element element = (Element) faultHandlersEditPart;
			Element layout = element.element("layoutConstraint");
			if (layout != null) {
				Rectangle bounds = getElementRectangle(element);
				heightAdjust = bounds.getHeight() - 29;
			}
		} else {
			Object eventHandlersEditPart = this
					.getScopeEventHandlersEditPart(childScopeEditPart
							.getDiagram());
			if (eventHandlersEditPart != null) {
				heightAdjust += 2;
			} else {
				heightAdjust += 28;
			}
		}
		return heightAdjust;
	}

	public Rectangle getElementRectangle(Element element) {
		Rectangle rect = null;
		Element layout = element.element("layoutConstraint");
		if (layout != null) {
			String x = layout.attributeValue("x");
			if (x == null) {
				x = "0";
			}
			String y = layout.attributeValue("y");
			if (y == null) {
				y = "0";
			}
			String width = layout.attributeValue("width");// default 123
			if (width == null) {
				width = "123";
			}
			String height = layout.attributeValue("height");// default 75
			if (height == null) {
				height = "75";
			}
			rect = new Rectangle(Integer.parseInt(x), Integer.parseInt(y),
					Integer.parseInt(width), Integer.parseInt(height));
		}
		return rect;
	}

	public String getActivityName(Element element) {
		if (element == null) {
			return null;
		}
		if ("extensionActivity".equals(element.getName())) {
			for (Object object : element.elements()) {
				if (object instanceof Element) {
					Element child = (Element) object;
					String attributeValue = child.attributeValue("name");
					if (attributeValue != null) {
						return attributeValue;
					}
				}
			}
		} else if ("extActivity".equals(element.getName())) {
			return element.attributeValue("name");
		}

		return null;

	}

	public String getActivityTypeId(Element element) {
		String activityTypeId = getActivityDesign(element);
		if (activityTypeId != null) {
			return activityTypeId;
		}
		return activityTypeId;
	}

	public String getGroupTypeId(Element element) {
		String activityTypeId = element.attributeValue("group");
		if (activityTypeId != null) {
			return activityTypeId;
		}
		return activityTypeId;
	}

	public String getOnMessageGroupId(Element element) {
		String activityTypeId = element.attributeValue("constructor");
		if (activityTypeId != null) {
			return activityTypeId;
		}
		return activityTypeId;
	}

	public String getActivityDesign(Element element) {
		if (element == null)
			return null;
		if ("extensionActivity".equals(element.getName())) {
			return getActivityTypeID(element);
		}

		if ("receiveEvent".equals(element.getName())) {
			return getActivityTypeID(element);
		}

		if ("extActivity".equals(element.getName())) {
			return element.attributeValue("type");
		}

		return element.getName();
	}

	private String getActivityTypeID(Element element) {
		String activityTypeID = null;
		if (element != null) {
			if ("BWActivity".equals(element.getName())) {
				activityTypeID = element.attributeValue("activityTypeID");
				if (activityTypeID != null) {
					return activityTypeID;
				}
			} else {
				List elements = element.elements();
				for (Object object : elements) {
					if (object instanceof Element) {
						activityTypeID = getActivityTypeID((Element) object);
					}
				}
			}
		}
		return activityTypeID;
	}
}
