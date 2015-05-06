
package com.gdu.bw.svg.model.figure;


/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class SequenceFlowFigure {
    private double sourceDecorationScale = 1.0;

    private double targetDecorationScale = 1.0;

    public static final double POLYGONDECORATION_XSCALE = 7.0;

    public static final double POLYGONDECORATION_YSCALE = 3.0;

    // private SequenceFlowType currFlowType;
    private int currFlowType;

    public static final int[] ARROW_TEMPLATE = new int[] { 0, 0, -1, 1, -1, -1 };

    public static final int[] CONDITIONAL_TEMPLATE = new int[] { -0, 0, -1, 1, -2, 0, -1, -1 };

    public static final int[] DEFAULT_TEMPLATE = new int[] { 0, -1, -2, 1 };
    
    public SVGFigure source;
    
    public SVGFigure target;
    
    public SequenceFlowFigure(SVGFigure source,SVGFigure target){
        this.source = source;
        this.target = target;
    }

    /**
     * @return the source
     */
    public SVGFigure getSource() {
        return this.source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(SVGFigure source) {
        this.source = source;
    }

    /**
     * @return the target
     */
    public SVGFigure getTarget() {
        return this.target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(SVGFigure target) {
        this.target = target;
    }
    
}
