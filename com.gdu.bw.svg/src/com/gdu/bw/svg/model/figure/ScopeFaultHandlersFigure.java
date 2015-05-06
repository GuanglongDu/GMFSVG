
package com.gdu.bw.svg.model.figure;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class ScopeFaultHandlersFigure extends SVGFigure {

    protected Rectangle childRectangle;
    
    public ScopeFaultHandlersFigure(SVGFigure parentFigure){
        super(parentFigure);
    }
    
    public void layout() {
        adjustScopeFaultHandlersFigureLayout();
         if(childList != null ){
             for(SVGFigure figure:childList){
                 figure.layout();
             }
         }
     }
    
    public void adjustScopeFaultHandlersFigureLayout(){
        if ("4022".equals(this.type)) {
            ScopeFigure scope = BWDiagramUtil.INSTANCE.getFaultFlow(this);
            int absoluteHeight = 0;
            scope.calculateScopeActivityRectangle(scope.getBounds(), scope.getBounds().width, scope.getBounds().height, scope.getNode().getElement());
            if (BWDiagramUtil.INSTANCE.isRootScope(scope.getNode().getElement()) && !this.getIsCollapse()) {// 4020
                absoluteHeight = 19;
            }
            Rectangle containerRect = this.getBounds();
            Element faultHandler = this.getNode().getElement();
            if (faultHandler != null && BWDiagramUtil.INSTANCE.getCatch(faultHandler).isEmpty()
                    && BWDiagramUtil.INSTANCE.getCatchAll(faultHandler) == null) {
                int shiftX = 8;
                int insect = 10;
                int x = containerRect.x + shiftX + insect;
                int y = containerRect.y;
                int width = containerRect.width - shiftX - insect * 2;
                int height = containerRect.height;
                Rectangle rect = new Rectangle();
                rect.setLocation(x, y);
                rect.setSize(width, height);
                // faultHandlerToolbar
            } else {
                int heightShift = 22;
                Rectangle rect = new Rectangle();
                rect.x = containerRect.x;
                rect.y = containerRect.y + heightShift;
                rect.width = containerRect.width;
                rect.height = containerRect.height - heightShift;
                // faulthandlerContainer
                childRectangle = rect.getCopy();

                int x = containerRect.x + 15;
                int y = containerRect.y;
                int width = containerRect.width - 25;
                int height = 40;
                Rectangle rect1 = new Rectangle();
                rect1.setLocation(x, y);
                rect1.setSize(width, height);
                // faultHandlerToolbarEx

            }

            setAbsolutePoint(new Point(childRectangle.x, childRectangle.y + absoluteHeight));
            setBounds(childRectangle);
            updateChildrenBounds(childRectangle);
           // this.addLocation(this, childRectangle.height, childRectangle.width, childRectangle.y, childRectangle.x);
        }else{
            Rectangle compartmentFigureBounds = this.getBounds();
            int compartmentHeight = 0;//compartmentFigureBounds.height;
            Map<SVGFigure, Integer> figure2differenceMap = new Hashtable<SVGFigure, Integer>();
            if (!BWDiagramUtil.INSTANCE.isPinned(this.diagram)) {
                double totalHeight = 0;
                double subTotalHeight = 0;
                List<SVGFigure> subChildrenEditParts = new ArrayList<SVGFigure>();
                Iterator<SVGFigure> childrenEditPartsItor = this.getChildList().iterator();
                while (childrenEditPartsItor.hasNext()) {
                    SVGFigure childEditPart = childrenEditPartsItor.next();
                    Rectangle childBounds = childEditPart.getBounds();
                    boolean isCollapsed = BWDiagramUtil.INSTANCE.isCollapsed(childEditPart.getDiagram());
                    boolean isPinned = BWDiagramUtil.INSTANCE.isPinned(childEditPart.getDiagram());
                    int height = childBounds.height;
                    totalHeight += (height - 1);
                    if (!isCollapsed && !isPinned) {
                        subTotalHeight += (height - 1);
                        subChildrenEditParts.add(childEditPart);
                    }
                }
                if (totalHeight > 0) {
                    totalHeight += 1;
                }
                double difference = compartmentHeight - totalHeight;
                if (compartmentHeight > 0 && difference != 0 && subChildrenEditParts.size() > 0) {
                    int totalShared = 0;
                    for (int i = 0; i < subChildrenEditParts.size(); i++) {
                        SVGFigure childFigure = subChildrenEditParts.get(i);
                        int shared = 0;
                        if (i == subChildrenEditParts.size() - 1) {
                            shared = (int) difference - totalShared;
                        } else {
                            Rectangle childBounds = childFigure.getBounds();
                            double height = childBounds.height;
                            double _shared = (height / subTotalHeight) * difference;
                            shared = (int) _shared;
                            totalShared += shared;
                        }

                        figure2differenceMap.put(childFigure, shared);
                    } 
                }
            }
            int defaultWidth = compartmentFigureBounds.width - 10; 
            //defaultWidth -= 13;
            int nextY = 0;
            Iterator<SVGFigure> childrenEditPartsItor = this.getChildList().iterator();
            while (childrenEditPartsItor.hasNext()) {
                SVGFigure childEditPart = childrenEditPartsItor.next();
                boolean isCollapsed = BWDiagramUtil.INSTANCE.isCollapsed(childEditPart.getDiagram());
                SVGFigure childFigure = childEditPart;

                Rectangle bounds = childFigure.getBounds();
                if (bounds == null) {
                    bounds = new Rectangle();
                }
                bounds.x = 0;
                bounds.y = nextY;
                bounds.width = defaultWidth;
                int height = bounds.height;
                if (!isCollapsed) {
                    if (height <= 0) {
                        if (BWDiagramUtil.INSTANCE.isScopeCatchEditPart(childFigure.getNode().getElement())) {
                            height = NodeNotation.SCOPE_CATCH_HEIGHT;
                        } else if (BWDiagramUtil.INSTANCE.isScopeCatchAllEditPart(childFigure.getNode().getElement())) {
                            height = NodeNotation.SCOPE_CATCHALL_HEIGHT;
                        } else {
                            height = 180;
                        }
                    }
                } else {
                    if (height < 20) {
                        height = 20;
                    }
                }

                if (figure2differenceMap.containsKey(childFigure)) {
                    int sharedDifference = figure2differenceMap.get(childFigure);
                    height += sharedDifference;
                }

                nextY += height - 1;

                // a new Catch or CatchAll is created, height difference is used only by the new created child EditPart
                Element element = childEditPart.getNode().getElement();
//                if (BWDiagramUtil.INSTANCE.isScopeCatchEditPart(element)) {
//                    Catch catchObj = (Catch) element;
//                    if (!cachedCatches.contains(catchObj)) {
//                        // the current onEvent is a new created Catch
//                        height = NodeNotation.SCOPE_ONEVENT_HEIGHT;
//                    }
//                } else 
                if (BWDiagramUtil.INSTANCE.isCatchAll(element)) {
                    List cachedCatchAlls = BWDiagramUtil.INSTANCE.getCatchAlls(this.parentFigure.getNode().getElement());
                    if (cachedCatchAlls.size() == 0) {
                        height = NodeNotation.SCOPE_ONEVENT_HEIGHT;
                    }
                }
                bounds.height = height;
                childFigure.setBounds(bounds);
            } 

        }
    }

    /**
     * 
     */
    private void updateChildrenBounds(Rectangle bounds) {
       List<SVGFigure> list = this.getChildList();
       for(SVGFigure figure:list){
           if(figure instanceof ScopeFaultHandlersFigure){
               figure.setBounds(bounds);
           }
       }
        
    }

    /**
     * @return the childRectangle
     */
    public Rectangle getChildRectangle() {
        return this.childRectangle;
    }
    
    public PrecisionRectangle translateToAbsolute(PrecisionRectangle bounds) {
        bounds.x = bounds.x + getParentFigure().getAbsolutePoint().x;
        bounds.y = bounds.y + getParentFigure().getAbsolutePoint().y;
        return bounds;
    }
}
