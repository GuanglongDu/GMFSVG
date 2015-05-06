
package com.gdu.bw.svg.model.figure;

import org.dom4j.Element;

import com.gdu.bw.svg.helper.BWDiagramUtil;
import com.gdu.bw.svg.helper.NodeNotation;
import com.gdu.bw.svg.model.Point;
import com.gdu.bw.svg.model.PrecisionRectangle;
import com.gdu.bw.svg.model.Rectangle;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class ScopeCompartmentFigure extends SVGFigure {

    /**
     * @param parentFigure
     */
    public ScopeCompartmentFigure(SVGFigure parentFigure) {
        super(parentFigure);
        // TODO Auto-generated constructor stub
    }

    public void adjustScopeCompartmentRectangle(){
        if ("4023".equals(type)) {
            Rectangle area = this.getBounds();
            Rectangle f = Rectangle.SINGLETON;
            f.x = area.x;
            f.y = area.y + 10;
            f.width = area.width - 1;
            f.height = area.height - 12;
            this.setAbsolutePoint(new Point(f.x, 0));
            this.addLocation(this, f.height, f.width, f.y, f.x);
        }else{
            this.setAbsolutePoint(new Point(0,20));
        }
    }
    
    public void layout() {
        adjustScopeCompartmentRectangle();
         if(childList != null ){
             for(SVGFigure figure:childList){
                 figure.layout();
             }
         }
     }
    
    public void calcuateScopeCompartmentRectangle(){
        Rectangle activityFigureBounds = new Rectangle();
        Rectangle compensationFigureBounds = new Rectangle();
        Element compensationHandler =this.node.getElement();
        Element scope = null;
        if (compensationHandler != null) {
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
                // if (scopeEventHandlersEditPart != null &&
                // scopeEventHandlersEditPart.getFigure() != null) {
                // eventHandlerHeight =
                // scopeEventHandlersEditPart.getFigure().getBounds().height;
                // }
                compensationFigureBounds.height += eventHandlerHeight;
                compensationFigureBounds.height += 6;
            } else {
                int eventHandlerHeight = NodeNotation.SCOPE_EVENTHANDLERS_HEIGHT;
                // if (scopeEventHandlersEditPart != null &&
                // scopeEventHandlersEditPart.getFigure() != null) {
                // eventHandlerHeight =
                // scopeEventHandlersEditPart.getFigure().getBounds().height;
                // }
                compensationFigureBounds.height += eventHandlerHeight;
                compensationFigureBounds.height += 10;
                if (faultHandler != null) {
                    if (BWDiagramUtil.INSTANCE.getCatch(faultHandler).size() > 0 || BWDiagramUtil.INSTANCE.getCatchAll(faultHandler) != null) {
                        compensationFigureBounds.height += 22;
                    }
                }
            }

            if (BWDiagramUtil.INSTANCE.isRootScope(scope)) {
                compensationFigureBounds.y += 5;
                compensationFigureBounds.height -= (10 + 5);
            }
            
            if(BWDiagramUtil.INSTANCE.isCollapsed(compensationHandler)){
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
            
            //setBounds
        }

    }
    
    public PrecisionRectangle translateToAbsolute(PrecisionRectangle bounds) {
        bounds.x = bounds.x + getParentFigure().getAbsolutePoint().x;
        bounds.y = bounds.y + getParentFigure().getAbsolutePoint().y;
        return bounds;
    }
}
