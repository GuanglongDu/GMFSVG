
package com.gdu.bw.svg.model.figure;

import org.dom4j.Element;

import com.gdu.bw.svg.helper.BWDiagramUtil;
import com.gdu.bw.svg.model.Point;
import com.gdu.bw.svg.model.Rectangle;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class ScopeOnEventFigure extends SVGFigure {

    public ScopeOnEventFigure(SVGFigure parentFigure){
        super(parentFigure);
    }
    
    public void layout() {
        adjustScopeOnEvent();
         if(childList != null ){
             for(SVGFigure figure:childList){
                 figure.layout();
             }
         }
     }
    
    public void adjustScopeOnEvent(){
        if("3026".equals(type)){
            int defaultX = 50;
            int defaultY = 10;
            setAbsolutePoint(new Point(defaultX,defaultY));
            if(getParentFigure().getBounds() != null){
                Rectangle rect1 = getParentFigure().getBounds().getCopy();
                rect1.width = rect1.width-defaultX-1;
                rect1.height = rect1.height-10;
               setBounds(rect1);
            }
        } else if("4026".equals(type)){
            Rectangle containerRect = this.getBounds();
            Rectangle area = containerRect.getCopy();
            area.x += 21;
            area.y += 0;
            area.width -= (21 + 1);
            area.height -= 1;

            Rectangle f = Rectangle.SINGLETON;
            f.x = area.x;
            f.y = area.y;
            f.width = area.width;
            f.height = area.height;
            if (!BWDiagramUtil.INSTANCE.isCollapsed(this.diagram)) {
                SVGFigure childScopeEditPart = BWDiagramUtil.INSTANCE.getOnEventChildScopeEditPart(this);
                if (childScopeEditPart != null) {
                    int heightAdjust = BWDiagramUtil.INSTANCE.getHeightAdjustForFaultHandlers(childScopeEditPart.getNode().getElement(),
                            childScopeEditPart.getDiagram(), -3);
                    f.height -= heightAdjust;
                }
            }

            this.addLocation(this, f.height, f.width, f.y, f.x);
        }
    }
    
    public Rectangle getElementRectangle(Element element){
        Rectangle rect =null;
        Element layout = element.element("layoutConstraint");
        if (layout != null){
            String x = layout.attributeValue("x");
            if(x==null){
                x="0";
            }
            String y = layout.attributeValue("y");
            if(y==null){
                y="0";
            }
            String width = layout.attributeValue("width");
            if(width==null){
                width = "-1";
            }
            String height = layout.attributeValue("height");
            if(height == null){
               height= "-1";
            }
           rect = new Rectangle(Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(width),Integer.parseInt(height));
        }
        return rect;
    }
}
