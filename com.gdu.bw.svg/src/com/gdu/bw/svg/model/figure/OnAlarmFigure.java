
package com.gdu.bw.svg.model.figure;

import com.gdu.bw.svg.helper.BWDiagramUtil;
import com.gdu.bw.svg.model.Rectangle;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class OnAlarmFigure extends SVGFigure {

    public OnAlarmFigure(SVGFigure parentFigure){
        super(parentFigure);
    }
    
    public void layout() {
        adjustOnAlarmFigureLayout();
        if(childList != null ){
            for(SVGFigure figure:childList){
                figure.layout();
            }
        }
    }
    /**
     * 
     */
    private void adjustOnAlarmFigureLayout() {
        Rectangle area = this.getBounds();
        area.x += 21;
        area.y += 0;
        area.width -= (21 + 1);
        area.height -= 1;

        Rectangle f = Rectangle.SINGLETON;
        f.x = area.x ;
        f.y = area.y ;
        f.width = area.width;
        f.height = area.height;
        if(!BWDiagramUtil.INSTANCE.isCollapsed(this.diagram)){
            SVGFigure childScopeEditPart = BWDiagramUtil.INSTANCE.getOnAlarmChildScopeEditPart(this);
            int heightAdjust = BWDiagramUtil.INSTANCE.getHeightAdjustForFaultHandlers(childScopeEditPart.getNode().getElement(),
                    childScopeEditPart.getDiagram(), -3);
            f.height -= heightAdjust;
        }
        this.addLocation(this, f.height, f.width, f.y, f.x);
    }
}
