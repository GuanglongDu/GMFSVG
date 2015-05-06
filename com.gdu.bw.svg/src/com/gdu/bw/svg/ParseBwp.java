
package com.gdu.bw.svg;

import org.dom4j.DocumentException;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class ParseBwp {
    
    public static void main(String[] args) throws DocumentException{
        BWPReader reader = new BWPReader();
        reader.readBWPFile("c:\\temp\\process\\Process02.bwp", "c:\\temp\\SVG\\SVG02.xml");
    }
    
   
}
