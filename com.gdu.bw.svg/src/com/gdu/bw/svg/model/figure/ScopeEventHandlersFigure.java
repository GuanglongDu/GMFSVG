
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
public class ScopeEventHandlersFigure extends SVGFigure {

    protected Rectangle childRectangle;
    
    public ScopeEventHandlersFigure(SVGFigure parentFigure){
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
        if ("4021".equals(type)) {
            Element eventHandler = this.node.getElement();
            if (eventHandler == null) {
                return;
            }
            ScopeFigure scope = BWDiagramUtil.INSTANCE.getFaultFlow(this);
            int absoluteHeight = 0;
            scope.calculateScopeActivityRectangle(scope.getBounds(), scope.getBounds().width, scope.getBounds().height, scope.getNode().getElement());
            if (BWDiagramUtil.INSTANCE.isRootScope(scope.getNode().getElement())) {// 4020

                absoluteHeight = 19;
            }
            Rectangle containerRect = this.getBounds();
            if (BWDiagramUtil.INSTANCE.getEvents(eventHandler) == null && BWDiagramUtil.INSTANCE.getAlarms(eventHandler) == null
                    && BWDiagramUtil.INSTANCE.getExtensibilityElements(eventHandler) == null) {
                int shiftX = 12;
                int insect = 10;
                int x = containerRect.x + shiftX + insect;
                int y = containerRect.y;
                int width = containerRect.width - shiftX - insect * 2;
                int height = containerRect.height;
                Rectangle rect1 = new Rectangle();
                rect1.setLocation(x, y);
                rect1.setSize(width, height);
                // eventHandlersToolbar
                Rectangle rect2 = Rectangle.SINGLETON;
                rect2.setLocation(-1, -1);
                rect2.setSize(-1, -1);
                // eventHandlersToolbarEx
            } else {
                int heightShift = 22;
                Rectangle rect = new Rectangle();
                rect.x = containerRect.x;
                rect.y = containerRect.y + heightShift;
                rect.width = containerRect.width;
                rect.height = containerRect.height - heightShift;
                // fScopeEventHandlersContainer
                childRectangle = rect.getCopy();

                int x = containerRect.x + 21;
                int y = containerRect.y;
                int width = containerRect.width - 31;
                int height = 40;
                Rectangle rect1 = new Rectangle();
                rect1.setLocation(x, y);
                rect1.setSize(width, height);
                // eventHandlersToolbarEx

                Rectangle rect2 = Rectangle.SINGLETON;
                rect2.setLocation(-1, -1);
                rect2.setSize(-1, -1);
                // eventHandlersToolbar
            }

            setAbsolutePoint(new Point(childRectangle.x, childRectangle.y));
            setBounds(childRectangle);
           // this.addLocation(this, containerRect.height, containerRect.width, containerRect.y, containerRect.x);
            updateChildBounds();
        }else {
            adjustOnEventFigure(this.getBounds());
        }
    }
    
    public PrecisionRectangle translateToAbsolute(PrecisionRectangle bounds) {
        bounds.x = bounds.x + getParentFigure().getAbsolutePoint().x;
        bounds.y = bounds.y + getParentFigure().getAbsolutePoint().y;
        return bounds;
    }

    /**
     * 
     */
    private void updateChildBounds() {
        List<SVGFigure> childList2 = this.getChildList();
        for(SVGFigure figure:childList2){
            if("3021".equals(figure.getType())){
                figure.setBounds(childRectangle);
            }
        }
    }

    public void adjustOnEventFigure(Rectangle compartmentFigureBounds) {
        
        int compartmentHeight = compartmentFigureBounds.height - 3;
        Map<SVGFigure, Integer> figure2differenceMap = new Hashtable<SVGFigure, Integer>();
        double totalHeightDifference = 0.0;
        if (!BWDiagramUtil.INSTANCE.isPinned(this.getDiagram())) {
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
            totalHeightDifference = compartmentHeight - totalHeight;
            if (compartmentHeight > 0 && totalHeightDifference != 0 && subChildrenEditParts.size() > 0) {
                int totalShared = 0;
                for (int i = 0; i < subChildrenEditParts.size(); i++) {
                    SVGFigure childEditPart = subChildrenEditParts.get(i);

                    int shared = 0;
                    if (i == subChildrenEditParts.size() - 1) {
                        shared = (int) totalHeightDifference - totalShared;
                    } else {
                        Rectangle childBounds = childEditPart.getBounds();
                        double height = childBounds.height;
                        double _shared = (height / subTotalHeight) * totalHeightDifference;
                        shared = (int) _shared;
                        totalShared += shared;
                    }
                    figure2differenceMap.put(childEditPart, shared);
                }
            }
        }
        int defaultWidth = compartmentFigureBounds.width - 10;
        int nextY = 0;
        Iterator<SVGFigure> childrenEditPartsItor = this.getChildList().iterator();
        while (childrenEditPartsItor.hasNext()) {
            SVGFigure childEditPart = childrenEditPartsItor.next();
            boolean isCollapsed = BWDiagramUtil.INSTANCE.isCollapsed(childEditPart.getDiagram());
            Rectangle bounds = childEditPart.getBounds();
            if (bounds == null) {
                bounds = new Rectangle();
            }
            bounds.x = 0;
            bounds.y = nextY;
            bounds.width = defaultWidth;
            int height = bounds.height;
            if (!isCollapsed) {
                if (height <= 0) {
                    if (BWDiagramUtil.INSTANCE.isScopeOnEventEditPart(childEditPart.getNode().getElement())) {
                        height = NodeNotation.SCOPE_ONEVENT_HEIGHT;
                    } else if (BWDiagramUtil.INSTANCE.isScopeOnAlarmEditPart(childEditPart.getNode().getElement())) {
                        height = NodeNotation.SCOPE_ONALARM_HEIGHT;
                    } else {
                        height = 180;
                    }
                }
            } else {
                if (height < 20) {
                    height = 20;
                }
            }
         // there is no new OnEvent or OnAlarm, height difference is shared by all children EditPart
//            if (figure2differenceMap.containsKey(childEditPart)) {
//                int sharedDifference = figure2differenceMap.get(childEditPart);
//                height += sharedDifference;
//            }
            nextY += height - 1;
            bounds.height = height;
            childEditPart.setBounds(bounds);
        } 
    }
}
