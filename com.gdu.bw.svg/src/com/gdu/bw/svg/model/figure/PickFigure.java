
package com.gdu.bw.svg.model.figure;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.gdu.bw.svg.helper.BWDiagramUtil;
import com.gdu.bw.svg.model.BWNode;
import com.gdu.bw.svg.model.Insets;
import com.gdu.bw.svg.model.PrecisionRectangle;
import com.gdu.bw.svg.model.Rectangle;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class PickFigure extends SVGFigure {

    public PickFigure(SVGFigure parentFigure){
        super(parentFigure);
    }
    public void layout() {
        adjustPickFigureLayout();
        if(childList != null ){
            for(SVGFigure figure:childList){
                figure.layout();
            }
        }
    }
    /**
     * 
     */
    private void adjustPickFigureLayout() {
        if("4010".equals(type)){
            Rectangle pickFigureBounds = this.getBounds().getCopy();
        //21,8,9,8
            pickFigureBounds.width = this.parentFigure.bounds.width-10 -3;
            Element pick = this.node.getElement();
            boolean isCollapsed = BWDiagramUtil.INSTANCE.isCollapsed(this.diagram);
            Rectangle f = Rectangle.SINGLETON;
            Rectangle area = pickFigureBounds;
            if (isCollapsed) {
                f.x = area.x;
                f.y = area.y;
                f.width = area.width - 1;
                f.height = area.height - 1;
            } else {
                f.x = area.x + 0 + 25;
                f.y = area.y + 0 + 10; 
                f.width = area.width - 1 - 25 - 22; 
                f.height = area.height - 1 - 10;
            }
            setBounds(pickFigureBounds);
            this.addLocation(this, f.height, f.width, f.y, f.x);
           
        }else if("3011".equals(type)){
            //-1,0,0,0
            //ConstrainedToolbarLayout layout
           // Element pick = this.node.getElement();
            adjustPickChildrenFigure(this.node);
            Rectangle compartmentFigureBounds = this.getBounds().getCopy();
            int compartmentHeight = compartmentFigureBounds.height;
            Map<SVGFigure, Integer> figure2differenceMap = new Hashtable<SVGFigure, Integer>();
            List<SVGFigure> subChildrenEditParts = new ArrayList<SVGFigure>();
            if(!BWDiagramUtil.INSTANCE.isPinned(this.diagram)){
                double totalHeight = 0;
                double subTotalHeight = 0;
                Iterator<SVGFigure> iterator = this.getChildList().iterator();
                while(iterator.hasNext()){
                    SVGFigure childFigure = iterator.next();
                    Rectangle childBounds = childFigure.getBounds();
                    boolean isCollapsed = BWDiagramUtil.INSTANCE.isCollapsed(childFigure.getDiagram());
                    boolean isPinned = BWDiagramUtil.INSTANCE.isPinned(childFigure.getDiagram());
                    int height = childBounds.height;
                    totalHeight += (height - 1);
                    if (!isCollapsed && !isPinned) {
                        subTotalHeight += (height - 1);
                        subChildrenEditParts.add(childFigure);
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
            int defaultX = 2;
            int defaultWidth = compartmentFigureBounds.width - defaultX * 2;
            //pickCompartmentFigure.getScrollPane().getVerticalScrollBar().isVisible()
            //defaultWidth -= 13;
            int nextY = 0;
            Iterator<SVGFigure> childrenEditPartsItor = this.getChildList().iterator();
            while (childrenEditPartsItor.hasNext()) {
                SVGFigure childFigure = childrenEditPartsItor.next();
                boolean isCollapsed = BWDiagramUtil.INSTANCE.isCollapsed(childFigure.getDiagram());
                boolean isPinned = BWDiagramUtil.INSTANCE.isPinned(childFigure.getDiagram());
                Rectangle bounds = childFigure.getBounds();
                if (bounds == null) {
                    bounds = new Rectangle();
                }

                bounds.x = defaultX;
                bounds.y = nextY;
                bounds.width = defaultWidth;
                int height = bounds.height;
                if (figure2differenceMap.containsKey(childFigure)) {
                    int sharedDifference = figure2differenceMap.get(childFigure);
                    height += sharedDifference;
                }

                if (isCollapsed) {
                    if (height < 20) {
                        height = 20;
                    }
                } else if (!isPinned) {
                    if (height < 60) {
                        height = 60;
                    }
                }
                nextY += height - 1;
                bounds.height = height;
                childFigure.setBounds(bounds);
            }
        }
    }
    /**
     * 
     */
    private void adjustPickChildrenFigure(BWNode node) {
        Rectangle compartmentFigureBounds = this.getParentFigure().getBounds().getCopy();
        compartmentFigureBounds.x = -1;
        compartmentFigureBounds.width += 1;
        compartmentFigureBounds.crop(new Insets(21,8,9,8));
        this.setBounds(compartmentFigureBounds);
        //if(node != null)
        //this.addLocation(this, compartmentFigureBounds.height, compartmentFigureBounds.width, compartmentFigureBounds.y, compartmentFigureBounds.x);

    }
}
