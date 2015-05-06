
package com.gdu.bw.svg.model.figure;

import java.util.List;

import com.gdu.bw.svg.model.Rectangle;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class FlowFigure extends SVGFigure{
    
    public FlowFigure(SVGFigure parentFigure) {
        super(parentFigure);
    }
    
    public void layout() {
       this.setBounds(this.getParentFigure().bounds);
       if(this.getParentFigure().bounds != null)
           updateFlowChildBounds(this.getParentFigure());
        if(childList != null ){
            for(SVGFigure figure:childList){
                figure.layout();
            }
        }
    }
    
    private void updateFlowChildBounds(SVGFigure figure) {
        List<SVGFigure> children = figure.getChildList();
        for (SVGFigure childFigure : children) {
            if (childFigure instanceof FlowFigure) {
                Rectangle rect = figure.getBounds().getCopy();
                if(!rect.equals(new Rectangle())){
                    rect.width = rect.width - 3;
                    rect.x = 0;
                    rect.y = 0;
                }
                childFigure.setBounds(rect);
                //this.addLocation(childFigure.getNode().getElement(), rect.height, rect.width, 0, 0);
                updateChildren(figure.getBounds().getCopy());
            }
        }

    }

    /**
     * @param rect
     */
    private void updateChildren(Rectangle rect) {
        List<SVGFigure> childList2 = this.getChildList();
        for(SVGFigure figure:childList2){
            figure.setBounds(rect);
        }
    }
}
