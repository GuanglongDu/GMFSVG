
package com.gdu.bw.svg.model.figure;

import java.util.List;

import org.dom4j.Element;

import com.gdu.bw.svg.helper.BWDiagramUtil;
import com.gdu.bw.svg.model.Dimension;
import com.gdu.bw.svg.model.Point;
import com.gdu.bw.svg.model.PrecisionRectangle;
import com.gdu.bw.svg.model.Rectangle;
import com.gdu.bw.svg.model.Transposer;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class OnMessageFigure extends SVGFigure {
    
    protected int minorAlignment;
    
    public static final int ALIGN_CENTER = 0;
    
    public static final int ALIGN_TOPLEFT = 1;
    
    public static final int ALIGN_BOTTOMRIGHT = 2;
    
    public OnMessageFigure(SVGFigure parentFigure){
        super(parentFigure);
    }
    public void layout() {
        adjustOnMessageFigureLayout();
        if(childList != null ){
            for(SVGFigure figure:childList){
                figure.layout();
            }
        }
    }
    /**
     * 
     */
    private void adjustOnMessageFigureLayout() {
        if ("4011".equals(type)) {
            Rectangle compartmentFigureBounds = this.getBounds();
            int defaultWidth = compartmentFigureBounds.width;
            int defaultHeight = compartmentFigureBounds.height;
            List<Element> onMessageCompartments = this.node.getElement().elements();
            if (onMessageCompartments.size() == 1) {

            }
            Element onMessages = this.node.getElement();

            Rectangle area = getBounds();

            Rectangle f = Rectangle.SINGLETON;
            f.x = area.x + 25;
            f.y = area.y;
            f.width = area.width - 1 - 25 - 21;
            f.height = area.height - 1;
            if (!BWDiagramUtil.INSTANCE.isCollapsed(this.diagram)) {
                SVGFigure childScopeEditPart = BWDiagramUtil.INSTANCE.getOnMessageChildScopeEditPart(this);
                if (childScopeEditPart != null) {
                    int heightAdjust = BWDiagramUtil.INSTANCE.getHeightAdjustForFaultHandlers(childScopeEditPart.getNode().getElement(),
                            childScopeEditPart.getDiagram(), -3);
                    f.height -= heightAdjust;
                }
            }else{
                SVGFigure childScopeEditPart = BWDiagramUtil.INSTANCE.getOnMessageChildScopeEditPart(this);
                childScopeEditPart.setBounds(new Rectangle());
                childScopeEditPart.setIsCollapse(true);
                if (childScopeEditPart != null) {
                    List<SVGFigure> childList2 = childScopeEditPart.getChildList();
                    for(SVGFigure svgFigure:childList2){
                        svgFigure.setAbsolutePoint(new Point(-5,0));
                        svgFigure.setIsCollapse(true);
                    }
                }
            }
            this.setAbsolutePoint(new Point(area.x,f.y));
            this.addLocation(this, f.height, f.width, f.y, f.x);
        }else if("3013".equals(type)){
           // adjustToolbatLayout();
            Rectangle area = this.getParentFigure().getBounds().getCopy();
            Rectangle rect = new Rectangle();

            Dimension childSize;
            //left
            childSize = new Dimension(30,-1);
            rect.setLocation(area.x, area.y);
            rect.width = childSize.width;
            rect.height = Math.max(0, area.height);
            //3012 setBounds
            Rectangle leftBounds = new Rectangle(rect.x,rect.y+19,rect.width,rect.height-19);
            area.x += rect.width + 1;
            area.width -= rect.width + 1;
            //right
            childSize = new Dimension(30,-1);
            rect.width = childSize.width;
            rect.height = Math.max(0, area.height);
            rect.setLocation(area.x + area.width - rect.width, area.y);
            area.width -= rect.width + 1;
            //center
            if (area.width < 0)
                area.width = 0;
            if (area.height < 0)
                area.height = 0;
             Rectangle newBounds = new Rectangle(area.x,area.y+19,area.width,area.height-19);
             this.setBounds(newBounds);
           
            this.setAbsolutePoint(new Point(this.bounds.x-leftBounds.x,this.bounds.y-leftBounds.y));
        }else if("3012".equals(type)){
           // adjustToolbatLayout();
            Rectangle area = this.getParentFigure().getBounds().getCopy();
            Rectangle rect = new Rectangle();

            Dimension childSize;
            //left
            childSize = new Dimension(30,-1);
            rect.setLocation(area.x, area.y);
            rect.width = childSize.width;
            rect.height = Math.max(0, area.height);
            //3012 setBounds
            Rectangle newBounds = new Rectangle(rect.x,rect.y+19,rect.width,rect.height-19);
            this.setBounds(newBounds);
            this.setAbsolutePoint(new Point(newBounds.x,19));
            area.x += rect.width + 1;
            area.width -= rect.width + 1;
            //right
            childSize = new Dimension(30,-1);
            rect.width = childSize.width;
            rect.height = Math.max(0, area.height);
            rect.setLocation(area.x + area.width - rect.width, area.y);
            area.width -= rect.width + 1;
            //center
            if (area.width < 0)
                area.width = 0;
            if (area.height < 0)
                area.height = 0;
        }else if("3016".equals(type)){
            Rectangle area = this.getParentFigure().getBounds().getCopy();
            Rectangle rect = new Rectangle();

            Dimension childSize;
            //left
            childSize = new Dimension(30,-1);
            rect.setLocation(area.x, area.y);
            rect.width = childSize.width;
            rect.height = Math.max(0, area.height);
            //3012 setBounds
           
            area.x += rect.width + 1;
            area.width -= rect.width + 1;
            //right
            childSize = new Dimension(30,-1);
            rect.width = childSize.width;
            rect.height = Math.max(0, area.height);
            rect.setLocation(area.x + area.width - rect.width, area.y);
            area.width -= rect.width + 1;
            Rectangle newBounds = new Rectangle(rect.x,rect.y+19,rect.width,rect.height-19);
            this.setBounds(newBounds);
            this.setAbsolutePoint(new Point(newBounds.x,19));
        }
    }
    /**
     * 
     */
    protected void adjustToolbatLayout() {
        List children = this.getParentFigure().getChildList();
        int numChildren = children.size();
        Transposer transposer = new Transposer();
        transposer.disable();
        Rectangle clientArea = transposer.t(this.getParentFigure().getClientArea());
        int x = clientArea.x;
        int y = clientArea.y;
        int availableHeight = clientArea.height;

        Dimension prefSizes[] = new Dimension[numChildren];
        Dimension minSizes[] = new Dimension[numChildren];
        Dimension maxSizes[] = new Dimension[numChildren];
        
        int wHint = -1;
        int hHint = -1;
        wHint = this.getParentFigure().getClientArea(Rectangle.SINGLETON).width;
        
        SVGFigure child;
        int totalHeight = 0;
        int totalMinHeight = 0;
        double totalMaxHeight = 0;
        int prefMinSumHeight = 0;
        double prefMaxSumHeight = 0;

        for (int i = 0; i < numChildren; i++) {
            child = (SVGFigure) children.get(i);

            prefSizes[i] = transposer.t(new Dimension(clientArea.width,clientArea.height+1));
            minSizes[i] = transposer.t(new Dimension(11,11));
            maxSizes[i] = transposer.t(new Dimension(2147483647,2147483647));

//                if (getConstraint(child) != null) {
//                    double ratio = ((Double) getConstraint(child)).doubleValue();
//                    int prefHeight = (int) (ratio * availableHeight);
//                    prefHeight = Math.max(prefHeight, minSizes[i].height);
//                    prefHeight = Math.min(prefHeight, maxSizes[i].height);
//                    prefSizes[i].height = prefHeight;
//                }

            totalHeight += prefSizes[i].height;
            totalMinHeight += minSizes[i].height;
            totalMaxHeight += maxSizes[i].height;
        }
        totalHeight += (numChildren - 1) * 0;
        totalMinHeight += (numChildren - 1) * 0;
        totalMaxHeight += (numChildren - 1) * 0;
        prefMinSumHeight = totalHeight - totalMinHeight;
        prefMaxSumHeight = totalMaxHeight - totalHeight;

        /* 
         * The total amount that the children must be shrunk is the 
         * sum of the preferred Heights of the children minus  
         * Max(the available area and the sum of the minimum heights of the children).
         *
         * amntShrinkHeight is the combined amount that the children must shrink
         * amntShrinkCurrentHeight is the amount each child will shrink respectively  
         */
        int amntShrinkHeight =
            totalHeight - Math.max(availableHeight, totalMinHeight);

        for (int i = 0; i < numChildren; i++) {
            int amntShrinkCurrentHeight = 0;
            int prefHeight = prefSizes[i].height;
            int minHeight = minSizes[i].height;
            int maxHeight = maxSizes[i].height;
            int prefWidth = prefSizes[i].width;
            int minWidth = minSizes[i].width;
            int maxWidth = maxSizes[i].width;
            Rectangle newBounds = new Rectangle(x, y, prefWidth, prefHeight);

            child = (SVGFigure) children.get(i);
            if (getStretchMajorAxis()) {
                if (amntShrinkHeight > 0 && prefMinSumHeight != 0)
                    amntShrinkCurrentHeight = (int) ((long) (prefHeight - minHeight)
                        * amntShrinkHeight / (prefMinSumHeight));
                else if (amntShrinkHeight < 0 && totalHeight != 0)
                    amntShrinkCurrentHeight =
                        (int) (((maxHeight - prefHeight) / prefMaxSumHeight)
                            * amntShrinkHeight);
            }

            int width = Math.min(prefWidth, maxWidth);
            if (true)
                width = maxWidth;
            width = Math.max(minWidth, Math.min(clientArea.width, width));
            newBounds.width = width;

            int adjust = clientArea.width - width;
            switch (minorAlignment) {
                case ALIGN_TOPLEFT :
                    adjust = 0;
                    break;
                case ALIGN_CENTER :
                    adjust /= 2;
                    break;
                case ALIGN_BOTTOMRIGHT :
                    break;
            }
            newBounds.x += adjust;
            if (newBounds.height - amntShrinkCurrentHeight > maxHeight)
                amntShrinkCurrentHeight = newBounds.height - maxHeight;
            newBounds.height -= amntShrinkCurrentHeight;
            child.setBounds(transposer.t(newBounds));

            amntShrinkHeight -= amntShrinkCurrentHeight;
            prefMinSumHeight -= (prefHeight - minHeight);
            prefMaxSumHeight -= (maxHeight - prefHeight);
            totalHeight -= prefHeight;
            y += newBounds.height + 0;
        }
    }
    /**
     * @return
     */
    private boolean getStretchMajorAxis() {
        // TODO Auto-generated method stub
        return true;
    }
    
    public PrecisionRectangle translateToAbsolute(PrecisionRectangle bounds) {
        bounds.x = bounds.x + getParentFigure().getAbsolutePoint().x;
        bounds.y = bounds.y + getParentFigure().getAbsolutePoint().y;
        return bounds;
    }
}
