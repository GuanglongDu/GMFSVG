
package com.gdu.bw.svg.model;

import com.gdu.bw.svg.model.figure.SVGFigure;




/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class RelativeBendpoint {

    private float weight = 0.5f;
    private Dimension d1, d2;
    
    public RelativeBendpoint() {
    }
    
    
    public Point getLocation(SVGFigure source,SVGFigure target) {
        PrecisionPoint a1 = new PrecisionPoint(source.getReferencePoint());
        PrecisionPoint a2 = new PrecisionPoint(target.getReferencePoint());
        return new PrecisionPoint(
                (a1.preciseX + d1.preciseWidth()) * (1.0 - weight) + weight
                        * (a2.preciseX + d2.preciseWidth()),
                (a1.preciseY + d1.preciseHeight()) * (1.0 - weight) + weight
                        * (a2.preciseY + d2.preciseHeight()));
    }
    
    public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
        d1 = dim1;
        d2 = dim2;
    }
    
    public void setWeight(float w) {
        weight = w;
    }
    
}
