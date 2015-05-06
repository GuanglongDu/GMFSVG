
package com.gdu.bw.svg.model.figure;

import com.gdu.bw.svg.helper.BWDiagramUtil;
import com.gdu.bw.svg.helper.NodeNotation;
import com.gdu.bw.svg.model.Insets;
import com.gdu.bw.svg.model.Rectangle;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class ProcessFigure extends SVGFigure {

    public ProcessFigure(SVGFigure parentFigure) {
        this.parentFigure = parentFigure;
        parentFigure.addChildFigure(this);
    }
    public void layout() {
        adjustProcessFigure();
        if(childList != null ){
            for(SVGFigure figure:childList){
                figure.layout();
            }
        }
    }
    
    private void adjustProcessFigure(){
        Rectangle area = getClientArea().getCopy();
        area.crop(new Insets(10, 10, 0, 0));
        area.x += (NodeNotation.OPERATION_OUTSIDE_WIDTH - 10);
        area.width = area.width - (2 * NodeNotation.OPERATION_OUTSIDE_WIDTH) + 19;
        area.height -= 10;

        ScopeFigure childScopeEditPart = BWDiagramUtil.INSTANCE.getProcessScopeEditPart(this);
        if (childScopeEditPart != null) {
            int heightAdjust = BWDiagramUtil.INSTANCE.getHeightAdjustForFaultHandlers(childScopeEditPart);
            area.height -= heightAdjust;
        }
        
        this.addLocation(this, area.height, area.width, area.y, area.x);
    }
}
