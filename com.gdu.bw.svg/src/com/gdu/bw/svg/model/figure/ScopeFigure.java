
package com.gdu.bw.svg.model.figure;

import java.util.List;

import org.dom4j.Element;

import com.gdu.bw.svg.helper.BWDiagramUtil;
import com.gdu.bw.svg.helper.NodeNotation;
import com.gdu.bw.svg.model.BWNode;
import com.gdu.bw.svg.model.Point;
import com.gdu.bw.svg.model.PointList;
import com.gdu.bw.svg.model.PrecisionPoint;
import com.gdu.bw.svg.model.PrecisionRectangle;
import com.gdu.bw.svg.model.Rectangle;

/**
 * @author <a>Davy Du</a>
 * 
 * @since 1.0.0
 */
public class ScopeFigure extends SVGFigure {

   
    public ScopeFigure(SVGFigure parentFigure) {
        super(parentFigure);
    }

    @Override
    public Rectangle getHandlerBounds() {
        Rectangle handleBounds = this.getBounds().getCopy();
        if (!BWDiagramUtil.INSTANCE.isCollapsed(this.getDiagram())) {
            Element scope = this.getNode().getElement();
            if (scope != null) {
                handleBounds.x += 11;
                handleBounds.width -= 11;
                handleBounds.y += 10;
                handleBounds.height -= 10;
                if (BWDiagramUtil.INSTANCE.getCompensationHandler(scope) == null) {
                    handleBounds.width -= 24;
                } else {
                    Object scopeCompensationHandlerEditPart = BWDiagramUtil.INSTANCE.getScopeCompensationHandlerEditPart(this.getDiagram());
                    if (scopeCompensationHandlerEditPart != null && BWDiagramUtil.INSTANCE.isCollapsed(scopeCompensationHandlerEditPart)) {
                        handleBounds.width -= 27;
                    } else {
                        handleBounds.width -= 3;
                    }
                }

                if (BWDiagramUtil.INSTANCE.getFaultHandlers(scope) == null) {
                    if (BWDiagramUtil.INSTANCE.getEventHandlers(scope) == null) {
                        handleBounds.height -= 24;
                    } else {
                        handleBounds.height -= 1;
                    }
                } else {
                    handleBounds.height -= 1;
                }
            }
        }
        return handleBounds;
    }

    public Point getAnchorPosition() {
        Rectangle sourceRect = getHandlerBounds().getCopy();
        this.parentFigure.translateToAbsolute(sourceRect);
        PrecisionRectangle rBox = new PrecisionRectangle(sourceRect);
        if (isDefaultAnchor()){
            return rBox.getCenter();
        }
        return new PrecisionPoint(relativeReference.preciseX * rBox.preciseWidth
                + rBox.preciseX, relativeReference.preciseY * rBox.preciseHeight
                + rBox.preciseY);
    }
    
    public void layout() {
        Rectangle rectangle = getParentFigure().getBounds();
        if (rectangle != null) {
            adjustScopeEditPart(this.node, this, this.type, this.diagram, this.bounds, rectangle.width, rectangle.height);
        } else {
            this.setBounds(bounds);
        }

        if (childList != null) {
            for (SVGFigure figure : childList) {
                figure.layout();
            }
        }
    }
    

    public void adjustScopeEditPart(BWNode bwNode, SVGFigure figure, String type, Element editPart, Rectangle rect, int compartmentWidth,
            int compartmentHeight) {
        Element scope = bwNode.getElement();
        if (BWDiagramUtil.INSTANCE.isProcessActivity(scope)) {
            if ("4018".equals(type)) {
                figure.setAbsolutePoint(new Point(1, 1));
                figure.setBounds(rect);
            }else if("4020".equals(type)){
                Rectangle activityFigureBounds = calculateScopeActivityRectangle(rect.getCopy(), compartmentWidth, compartmentHeight, scope);
                figure.setBounds(activityFigureBounds);
                //addLocation(bwNode.getElement(), activityFigureBounds.height, activityFigureBounds.width, activityFigureBounds.y, activityFigureBounds.x);
                updateChildFigure(activityFigureBounds.getCopy());
            }
        } else {
            if ("4018".equals(type)) {
                Rectangle area = rect.getCopy();
                if (!BWDiagramUtil.INSTANCE.isProcessActivity(scope) && !BWDiagramUtil.INSTANCE.isRootScope(scope)) {
                    if (BWDiagramUtil.INSTANCE.isCollapsed(editPart)) {
                        Rectangle f = Rectangle.SINGLETON;
                        f.x = area.x;
                        f.y = area.y;
                        f.width = area.width - 1;
                        f.height = area.height - 1;
                        addLocation(this, f.height, f.width, f.y, f.x);
                        figure.setAbsolutePoint(new Point(f.x-5, f.y));
                        updateChildBounds(area);
                        // element setAttribute
                    } else {// 4018->4020
                        Object object = BWDiagramUtil.INSTANCE.getScopeActivityEditPart(editPart);
                        if (object != null && object instanceof Element) {
                            
                            figure.setNode(bwNode);
                            bwNode.setFigure(figure);
                            figure.setAbsolutePoint(new Point(rect.x, rect.y));
                            figure.setBounds(rect);
                            
                        }
                    }
                }else {
                    if (BWDiagramUtil.INSTANCE.isCollapsed(editPart) || this.getIsCollapse()) {
                        updateChildrenBounds(new Rectangle(0,0,0,11));
                    }else{
                        Rectangle bounds2 = this.getParentFigure().getBounds();
                        Rectangle realBounds = new Rectangle(0,0,bounds2.width,bounds2.height);
                        figure.setBounds(realBounds);
                        updateChildrenBounds(realBounds);
                    }
                }
            } else if ("4020".equals(type)) {// ScopeCompartmentXYLayout
                Rectangle activityFigureBounds = calculateScopeActivityRectangle(rect.getCopy(), compartmentWidth, compartmentHeight, scope);
                figure.setAbsolutePoint(new Point(rect.x, rect.y));
                figure.setBounds(activityFigureBounds);
                figure.setNode(bwNode);
                updateChildFigure(activityFigureBounds.getCopy());
                Rectangle area = this.getParentFigure().getBounds().getCopy();
                SVGFigure scopeFigure = getParentScopeFigure();
                //get4018
                if (BWDiagramUtil.INSTANCE.isCollapsed(scopeFigure.getDiagram())) {
                    Rectangle f = Rectangle.SINGLETON;
                    f.x = activityFigureBounds.x;
                    f.y = activityFigureBounds.y;
                    f.width = activityFigureBounds.width - 1;
                    f.height = activityFigureBounds.height - 1;
                    //addLocation(scopeFigure, f.height, f.width, f.y, f.x);
                    updateChildBounds(area);
                } else {
                        Rectangle scopeActivityBounds = activityFigureBounds;
                        area.x += 11;
                        area.y += 10;
                        area.width = scopeActivityBounds.width + 4;
                        if (BWDiagramUtil.INSTANCE.getEventHandlers(scope) == null && BWDiagramUtil.INSTANCE.getFaultHandlers(scope) == null) {
                            area.height = scopeActivityBounds.y + scopeActivityBounds.height - 11;
                        } else if (BWDiagramUtil.INSTANCE.getEventHandlers(scope) == null
                                && BWDiagramUtil.INSTANCE.getFaultHandlers(scope) != null) {
                            area.height = (scopeActivityBounds.y + scopeActivityBounds.height) - 11;
                            area.height += 11;
                        } else if (BWDiagramUtil.INSTANCE.getEventHandlers(scope) != null
                                && BWDiagramUtil.INSTANCE.getFaultHandlers(scope) == null) {
                            int eventHandlerHeight = NodeNotation.SCOPE_EVENTHANDLERS_HEIGHT;
                            Object scopeEventHandlersEditPart = BWDiagramUtil.INSTANCE.getScopeEventHandlersFromScope(editPart.getParent());
                            if (scopeEventHandlersEditPart != null && scopeEventHandlersEditPart instanceof Element) {
                                Rectangle elementRectangle = getElementRectangle((Element) scopeEventHandlersEditPart);
                                if (elementRectangle != null) {
                                    eventHandlerHeight = elementRectangle.height;
                                }
                            }
                            area.height = (scopeActivityBounds.y + scopeActivityBounds.height) - 11;
                            area.height += eventHandlerHeight;
                            area.height += 7;
                        } else {
                            Object scopeEventHandlersEditPart = BWDiagramUtil.INSTANCE.getScopeEventHandlersFromScope(editPart.getParent());
                            if (scopeEventHandlersEditPart != null && scopeEventHandlersEditPart instanceof Element) {
                                Rectangle scopeEventHandlersBounds = getElementRectangle((Element) scopeEventHandlersEditPart);
                                area.height = (scopeActivityBounds.y + scopeActivityBounds.height) - 11;
                                area.height += scopeEventHandlersBounds.height;
                                area.height += 11;
                            }
                        }
                        Element faultHandler = BWDiagramUtil.INSTANCE.getFaultHandlers(scope);
                        if (faultHandler != null) {
                            if (BWDiagramUtil.INSTANCE.getCatch(faultHandler).size() > 0
                                    || BWDiagramUtil.INSTANCE.getCatchAll(faultHandler) != null) {
                                area.height += 22;
                            }
                        }
                        Rectangle f = Rectangle.SINGLETON;
                        f.x = area.x;
                        f.y = area.y;
                        f.width = area.width;
                        f.height = area.height;
                        if(!BWDiagramUtil.INSTANCE.isRootScope(scope)){
                            addLocation(scopeFigure, f.height, f.width, f.y, f.x);
                        }
                }
            
            }
        }
    }

    /**
     * @return
     */
    private SVGFigure getParentScopeFigure() {
        SVGFigure parentFigure2 = this.getParentFigure();
        if(parentFigure2!= null){
            SVGFigure figure = parentFigure2.getParentFigure();
            if("4018".equals(figure.getType())){
                return figure;
            }
        }
        return null;
    }

    private SVGFigure getChildScopeFigure(){
        List<SVGFigure> children = this.getChildList();
        for(SVGFigure child:children){
            if("3018".equals(child.getType())){
                List<SVGFigure> childList2 = child.getChildList();
                for(SVGFigure figure:childList2){
                    if("4020".equals(figure.getType())){
                        return figure;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 
     */
    private void updateChildBounds(Rectangle realBounds) {
      List<SVGFigure> children = this.getChildList();
        for(SVGFigure child:children){
            if("3018".equals(child.getType())){
                child.setBounds(new Rectangle(realBounds.x,realBounds.y,0,0));
                break;
            }
        }
    }

    /**
     * @param realBounds
     */
    private void updateChildrenBounds(Rectangle realBounds) {
       List<SVGFigure> childList = this.getChildList();
        for(SVGFigure figure:childList){
            figure.setBounds(realBounds);
        }
    }

    /**
     * @param activityFigureBounds
     */
    private void updateChildFigure(Rectangle activityFigureBounds) {
       List<SVGFigure> childList2 = this.getChildList();
       if(childList2 != null){
       for(SVGFigure figure:childList2){
           if("3020".equals(figure.getType())){
               //activityFigureBounds.width = activityFigureBounds.width - 3;
               figure.setBounds(activityFigureBounds);
           }
       }
       }
    }

    /**
     * @param rect
     * @param compartmentWidth
     * @param compartmentHeight
     * @param scope
     * @return
     */
    protected Rectangle calculateScopeActivityRectangle(Rectangle rect, int compartmentWidth, int compartmentHeight, Element scope) {
        int childrenWidth = 0;
        int childrenHeight = 0;
        compartmentHeight = compartmentHeight - 1;
        Rectangle activityFigureBounds = rect.getCopy();
        Rectangle variablesFigureBounds = new Rectangle();
        Rectangle eventHandlersFigureBounds = new Rectangle();
        Rectangle faultHandlersFigureBounds = new Rectangle();
        Rectangle compensationFigureBounds = new Rectangle();
        Element faultHandlers = getFaultHandlers(getDiagram().getParent());
        if (faultHandlers != null) {
            faultHandlersFigureBounds = this.getElementRectangle(faultHandlers);
        }
        Element eventHandlers = getEventHandlers(getDiagram().getParent());
        if(eventHandlers != null){
            eventHandlersFigureBounds = this.getElementRectangle(eventHandlers);
        }
        Element compensationHandlers = getCompensationHandlers(getDiagram().getParent());
       if(compensationHandlers != null){
           compensationFigureBounds = this.getElementRectangle(compensationHandlers);
       }
        int shiftX = 15;
        if (BWDiagramUtil.INSTANCE.isProcessActivity(scope)) {
            shiftX = 0;
        }
        int activityWidth = activityFigureBounds.width;
        if (!BWDiagramUtil.INSTANCE.getVariables(scope).isEmpty()) {
            variablesFigureBounds.x = shiftX + 6 + 21; 
            variablesFigureBounds.y = 0;
            variablesFigureBounds.width = 25;

            List variables = BWDiagramUtil.INSTANCE.getVariables(scope);
            if (variables != null && variables.isEmpty()) {
                variablesFigureBounds.height = NodeNotation.SCOPE_VARIABLES_HEIGHT;
            }
            if (variablesFigureBounds.height < NodeNotation.SCOPE_VARIABLES_HEIGHT) {
                variablesFigureBounds.height = NodeNotation.SCOPE_VARIABLES_HEIGHT;
            }
        }

        if (BWDiagramUtil.INSTANCE.getActivities(scope) != null) {
            activityFigureBounds.x = shiftX;
            if (BWDiagramUtil.INSTANCE.isProcessActivity(scope)) {
                activityFigureBounds.y = 0;
                childrenWidth = activityFigureBounds.x + activityFigureBounds.width;

            } else if (BWDiagramUtil.INSTANCE.isRootScope(scope)) {
                activityFigureBounds.y = 0;
                childrenWidth = activityFigureBounds.x + activityFigureBounds.width + 25;

            } else {
                if (!BWDiagramUtil.INSTANCE.getVariables(scope).isEmpty()) {
                    activityFigureBounds.y = variablesFigureBounds.y + variablesFigureBounds.height;
                } else {
                    activityFigureBounds.y = NodeNotation.SCOPE_VARIABLES_HEIGHT;
                }
                childrenWidth = activityFigureBounds.x + activityFigureBounds.width + 25;
            }

            childrenHeight = activityFigureBounds.y + activityFigureBounds.height + 23;
        }

        if (BWDiagramUtil.INSTANCE.getEventHandlers(scope) != null) {
            eventHandlersFigureBounds.x = 0;
            eventHandlersFigureBounds.y = activityFigureBounds.y + activityFigureBounds.height;
            eventHandlersFigureBounds.width = shiftX + activityFigureBounds.width;
            Element event = BWDiagramUtil.INSTANCE.getEventHandlers(scope);
            List events = BWDiagramUtil.INSTANCE.getEvents(event);
            List alarms = BWDiagramUtil.INSTANCE.getAlarms(event);
            List extensions = BWDiagramUtil.INSTANCE.getExtensibilityElements(event);
            if (!events.isEmpty() || !alarms.isEmpty() || !extensions.isEmpty()) {
                childrenHeight = eventHandlersFigureBounds.y + eventHandlersFigureBounds.height + 6;
            } else {
                eventHandlersFigureBounds.height = NodeNotation.SCOPE_EVENTHANDLERS_HEIGHT;
                childrenHeight = eventHandlersFigureBounds.y;
            }
        }
        
        if (BWDiagramUtil.INSTANCE.getFaultHandlers(scope) != null) {
            int faultShiftX = 6;
            faultHandlersFigureBounds.x = shiftX + faultShiftX;
            if (BWDiagramUtil.INSTANCE.isProcessActivity(scope)) {
                faultShiftX += 10;
                faultHandlersFigureBounds.x += faultShiftX;
            }
            faultHandlersFigureBounds.width = activityWidth - faultShiftX * 2 - 8;
            Element fault = BWDiagramUtil.INSTANCE.getFaultHandlers(scope);
            if (BWDiagramUtil.INSTANCE.getCatchAll(fault) == null && BWDiagramUtil.INSTANCE.getCatch(fault).isEmpty()) {
                faultHandlersFigureBounds.height = NodeNotation.SCOPE_FAULTHANDLERS_HEIGHT;
            }
            if (BWDiagramUtil.INSTANCE.getEventHandlers(scope) != null) {
                faultHandlersFigureBounds.y = eventHandlersFigureBounds.y + eventHandlersFigureBounds.height;
                if (!BWDiagramUtil.INSTANCE.getCatch(fault).isEmpty() || BWDiagramUtil.INSTANCE.getCatchAll(fault) != null) {
                    faultHandlersFigureBounds.y = eventHandlersFigureBounds.y + eventHandlersFigureBounds.height - 0;
                }
            } else {
                faultHandlersFigureBounds.y = activityFigureBounds.y + activityFigureBounds.height;
            }
            childrenHeight = faultHandlersFigureBounds.y + faultHandlersFigureBounds.height;
        }

        Element compensationHandler = BWDiagramUtil.INSTANCE.getCompensationHandler(scope);
        if (compensationHandler != null) {
           SVGFigure compensationFigure =  BWDiagramUtil.INSTANCE.getCompensationHandler(this);
           if(compensationFigure != null){
               compensationFigureBounds =compensationFigure.bounds.getCopy();
           }
            compensationFigureBounds.x = activityFigureBounds.x + activityFigureBounds.width;
            compensationFigureBounds.y = 0;
            compensationFigureBounds.height = activityFigureBounds.y + activityFigureBounds.height + 1;
            Element faultHandler = BWDiagramUtil.INSTANCE.getFaultHandlers(scope);
            Element eventHandler = BWDiagramUtil.INSTANCE.getEventHandlers(scope);
            if (eventHandler == null && faultHandler != null) {
                compensationFigureBounds.height += 10;
                if (faultHandler != null) {
                    if (BWDiagramUtil.INSTANCE.getCatch(faultHandler).size() > 0 || BWDiagramUtil.INSTANCE.getCatchAll(faultHandler) != null) {
                        compensationFigureBounds.height += 22;
                    }
                }
            } else if (eventHandler != null && faultHandler == null) {
                int eventHandlerHeight = NodeNotation.SCOPE_EVENTHANDLERS_HEIGHT;
                 if (BWDiagramUtil.INSTANCE.getFaultHandlerFigure(this) != null){
                     eventHandlerHeight = BWDiagramUtil.INSTANCE.getFaultHandlerFigure(this).getBounds().height;
                 }
                compensationFigureBounds.height += eventHandlerHeight;
                compensationFigureBounds.height += 6;
            } else {
                int eventHandlerHeight = NodeNotation.SCOPE_EVENTHANDLERS_HEIGHT;
                 if (BWDiagramUtil.INSTANCE.getEventHandlerFigure(this) != null){
                     eventHandlerHeight = BWDiagramUtil.INSTANCE.getEventHandlerFigure(this).getBounds().height;
                 }
                compensationFigureBounds.height += eventHandlerHeight;
                compensationFigureBounds.height += 10;
                if (faultHandler != null) {
                    if (BWDiagramUtil.INSTANCE.getCatch(faultHandler).size() > 0 || BWDiagramUtil.INSTANCE.getCatchAll(faultHandler) != null) {
                        compensationFigureBounds.height += 22;
                    }
                }
            }

            childrenWidth = compensationFigureBounds.x + compensationFigureBounds.width + 3;
        }

        if (BWDiagramUtil.INSTANCE.getActivities(scope) != null) {
            int widthDifference = compartmentWidth - childrenWidth;
            int heightDifference = compartmentHeight - childrenHeight;

            activityFigureBounds.width += widthDifference;
            activityFigureBounds.height += heightDifference;
            if (activityFigureBounds.width < 100) {
                activityFigureBounds.width = 100;
            }
            if (activityFigureBounds.height < 10) {
                activityFigureBounds.height = 10;
            }
        }
        
        if (BWDiagramUtil.INSTANCE.getEventHandlers(scope) != null ) {
            ScopeEventHandlersFigure eventHandlerFigure = getScopeEventHandlerFigure();
            if(eventHandlerFigure != null)
                eventHandlerFigure.setBounds(eventHandlersFigureBounds);
            
        }
        
        if (BWDiagramUtil.INSTANCE.getFaultHandlers(scope) != null) {
            ScopeFaultHandlersFigure faultHandlersFigure = getScopeFaultHandlerFigure();
            if(faultHandlersFigure != null)
                faultHandlersFigure.setBounds(faultHandlersFigureBounds);
        }
        SVGFigure compensationFigure =  BWDiagramUtil.INSTANCE.getCompensationHandler(this);
        if(compensationFigure != null){
        if(BWDiagramUtil.INSTANCE.isCollapsed(compensationFigure.getDiagram())){
            if (BWDiagramUtil.INSTANCE.isRootScope(scope)) {
                compensationFigureBounds.y += 5;
                compensationFigureBounds.height -= (10 + 5);
            }

            compensationFigureBounds.width = 17;

            int normalHeight = compensationFigureBounds.height;
            int collapsedHeight = 80;
            int minHeight = 40;
            int preferedHeight = (normalHeight / 2) - 18;
            if (preferedHeight < minHeight) {
                preferedHeight = minHeight;
            }
            compensationFigureBounds.height = preferedHeight < collapsedHeight ? preferedHeight : collapsedHeight;
            
            }
        compensationFigure.setAbsolutePoint(new Point(activityFigureBounds.x+activityFigureBounds.width,20));
        }
        
        return activityFigureBounds;
    }

    
    /**
     * @param scopeFigure
     * @return
     */
    private ScopeFaultHandlersFigure getScopeFaultHandlerFigure() {
        if(this.node.getFaultHandlers().size()>0){
            BWNode bwNode = this.node.getFaultHandlers().get(0);
            if(bwNode.getFigure() != null){
                return (ScopeFaultHandlersFigure) bwNode.getFigure();
            }
        }
        return null;
    }

    /**
     * @param scopeFigure
     * @return
     */
    private ScopeEventHandlersFigure getScopeEventHandlerFigure() {
        if(this.node.getEventHandlers().size()>0){
            BWNode bwNode = this.node.getEventHandlers().get(0);
            if(bwNode.getFigure() != null){
                return (ScopeEventHandlersFigure) bwNode.getFigure();
            }
        }
        return null;
    }

    /**
     * @param parent
     * @return
     */
    private Element getCompensationHandlers(Element diagram) {
        if (diagram != null && !diagram.elements().isEmpty()) {
            for (Object object : diagram.elements()) {
                if (object instanceof Element) {
                    Element element = (Element) object;
                    if ("4023".equals(element.attributeValue("type"))) {
                        return element;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param parent
     * @return
     */
    private Element getEventHandlers(Element diagram) {
        if (diagram != null && !diagram.elements().isEmpty()) {
            for (Object object : diagram.elements()) {
                if (object instanceof Element) {
                    Element element = (Element) object;
                    if ("4021".equals(element.attributeValue("type"))) {
                        return element;
                    }
                }
            }
        }
        return null;
    }

    public PrecisionRectangle translateToAbsolute(PrecisionRectangle bounds) {
        bounds.x = bounds.x + getParentFigure().getAbsolutePoint().x;
        bounds.y = bounds.y + getParentFigure().getAbsolutePoint().y;
        return bounds;
    }

    private Element getFaultHandlers(Element diagram) {
        if (diagram != null && !diagram.elements().isEmpty()) {
            for (Object object : diagram.elements()) {
                if (object instanceof Element) {
                    Element element = (Element) object;
                    if ("4022".equals(element.attributeValue("type"))) {
                        return element;
                    }
                }
            }
        }
        return null;
    }

    public PointList translateToAbsolute(PointList bounds) {
        PointList cache = bounds.getCopy();
        cache.removeAllPoints();
        for (int i = 0; i < bounds.size(); i++) {
            Point point = bounds.getPoint(i);
            point.x = point.x + getParentFigure().getAbsolutePoint().x;
            point.y = point.y + getParentFigure().getAbsolutePoint().y;
            cache.addPoint(point);
        }
        return cache;
    }



}
