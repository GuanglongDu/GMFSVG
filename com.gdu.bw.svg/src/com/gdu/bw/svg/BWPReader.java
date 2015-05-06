
package com.gdu.bw.svg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.PortType;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.gdu.bw.svg.helper.BWEditPartHelper;
import com.gdu.bw.svg.model.BWNode;
import com.gdu.bw.svg.model.figure.SVGFigure;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class BWPReader {

    public BWPReader(){
        
    }
    
    
    public void iteratorWSDL(String projectPath){
        
    }
    
    
    public void readBWPFile(String inputFilePath,String outputFilePath) throws DocumentException{
        SAXReader saxReader = new SAXReader();  
        File bwpFile = new File(inputFilePath);
        Document document = saxReader.read(bwpFile);  
        Element root = document.getRootElement(); 
        Element notation = root.element("Diagram");
        Element scope = root.element("scope");
        BWNode rootNode = new BWNode();
        rootNode.setElement(root);
        rootNode.setSelfId("//0/@process");
        BWNode scopeNode = new BWNode(rootNode);
        scopeNode.setElement(scope);
        scopeNode.setSelfId("//0/@process/@activity");
        rootNode.addChildNode(scopeNode);
        rootNode.addAvtivities(scopeNode);
        BWEditPartHelper.INSTANCE.findElementFromScope(scope,scopeNode,true,-1);
        
        List<Element> imports = root.elements("import");
        for(Element element:imports){
            String location = element.attributeValue("location");
           
        }

        SVGFigure figure = new SVGFigure();
        BWEditPartHelper.INSTANCE.loadProcessFigures(scope,rootNode,figure,notation);
        writeDocument(document,outputFilePath);
    }
    
    private void parseWSDL(String path) throws WSDLException{
        WSDLFactory factory = WSDLFactory.newInstance();  
        WSDLReader reader=factory.newWSDLReader();
        Definition def=reader.readWSDL(path);
        Map portTypes = def.getPortTypes();
        PortType portType = def.getPortType(new QName("http://www.tibco.com/NewWSDLFile/","NewWSDLFile"));
        List<Operation> operations = portType.getOperations();
        Iterator operIter=operations.iterator();  
        while (operIter.hasNext()) {
            Operation operation = (Operation) operIter.next();
            if (!operation.isUndefined()) {
                System.out.println(operation.getInput() +" "+operation.getOutput());
            }
        }  
    }
    
    
    private void writeDocument( Document document,String outPath) {
        XMLWriter writer = null;
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        File file = new File(outPath);
        try {
            if(!file.exists())
                file.createNewFile();
            writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
