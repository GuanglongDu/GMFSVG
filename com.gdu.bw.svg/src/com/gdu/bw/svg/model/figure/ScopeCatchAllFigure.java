
package com.gdu.bw.svg.model.figure;

import org.dom4j.Element;

import com.gdu.bw.svg.helper.BWDiagramUtil;
import com.gdu.bw.svg.model.BWNode;
import com.gdu.bw.svg.model.Point;
import com.gdu.bw.svg.model.PrecisionRectangle;
import com.gdu.bw.svg.model.Rectangle;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class ScopeCatchAllFigure extends SVGFigure {
    
    public ScopeCatchAllFigure(SVGFigure parentFigure){
        super(parentFigure);
    }
    
    public void layout() {
        adjustScopeCatchAllEditPart(this.node,this,this.type,this.diagram,this.getBounds());
         if(childList != null ){
             for(SVGFigure figure:childList){
                 figure.layout();
             }
         }
     }
    
    public void adjustScopeCatchAllEditPart(BWNode bwNode,SVGFigure figure, String type,Element editPart,Rectangle rect) {
        if ("4029".equals(type)) {
            Element scope = bwNode.getElement();
            Rectangle area = rect.getCopy();
            area.x += 15;
            area.y += 0;
            area.height -= 1;
            area.width -= 1;
            area.width -= 15;

            Rectangle f = Rectangle.SINGLETON;
            f.x = area.x;
            f.y = area.y;
            f.width = area.width;
            f.height = area.height;
            if (BWDiagramUtil.INSTANCE.isScopeCatchAllEditPart(scope)) {
                if (!BWDiagramUtil.INSTANCE.isCollapsed(editPart)) {
                    SVGFigure childScopeEditPart = BWDiagramUtil.INSTANCE.getScopeCatchAllEditPart(this);
                    if (childScopeEditPart != null) {
                        int heightAdjust = BWDiagramUtil.INSTANCE.getHeightAdjustForFaultHandlers(childScopeEditPart.getNode().getElement(),
                                childScopeEditPart.getDiagram(), -3);
                        f.height -= heightAdjust;
                       
                    }
                }
            } 
            figure.setAbsolutePoint(new Point(rect.x, rect.y));
//            if (!BWDiagramUtil.INSTANCE.isCollapsed(editPart)) {
//                SVGFigure childScopeEditPart = BWDiagramUtil.INSTANCE.getCatchParentScopeEditPart(this);
//                if (BWDiagramUtil.INSTANCE.isRootScope(childScopeEditPart.node.getElement())) {
//
//                }
//            }
            figure.setBounds(rect);
            this.addLocation(this, f.height, f.width, f.y, f.x);
        }else if("4033".equals(type)){
           int normalScopeHandlerX = 5;
           Rectangle bounds = rect.getCopy();
           bounds.x = normalScopeHandlerX;
           figure.setBounds(bounds);
           figure.setAbsolutePoint(new Point(bounds.x,bounds.y));
       }else if("3032".equals(type)){
           int defaultX = 50;
           int defaultY = 10;
            if (!this.getIsCollapse()) {
                figure.setAbsolutePoint(new Point(defaultX,defaultY));
                if (figure.getParentFigure().getBounds() != null) {
                    Rectangle rect1 = figure.getParentFigure().getBounds().getCopy();
                    rect1.width = rect1.width - defaultX - 1;
                    rect1.height = rect1.height - 10;
                    figure.setBounds(rect1);
                }
            }else{
                figure.setAbsolutePoint(new Point(defaultX-5,0));
                figure.setBounds(new Rectangle(50,0,0,0));
                for(SVGFigure child:figure.getChildList()){
                    if(child instanceof FlowFigure){
                        child.setBounds(new Rectangle(0,0,3,0));
                        child.setAbsolutePoint(new Point(0,5));
                    }
                }
            }
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
    
    public PrecisionRectangle translateToAbsolute(PrecisionRectangle bounds) {
        bounds.x = bounds.x + getParentFigure().getAbsolutePoint().x;
        bounds.y = bounds.y + getParentFigure().getAbsolutePoint().y;
        return bounds;
    }
}
