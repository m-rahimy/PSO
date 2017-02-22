package ir.mrgkrahimy.pso_implementation;

import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mojtaba on 3/3/2016.
 */
public class Persian {

    public String fileUri= "res/values/persian.xml";
    public Document document = null;

    public static String[] strings;

    public static Persian persian = null;

    public static Persian getInstance(){
        if (persian == null){
            return new Persian();
        }
        return persian;
    }

    private Persian(){

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setValidating(true);
        builderFactory.setNamespaceAware(true);
        builderFactory.setIgnoringElementContentWhitespace(true);

        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {

                }

                @Override
                public void error(SAXParseException exception) throws SAXException {

                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {

                }
            });

            InputSource inputSource = new InputSource(fileUri);
            document=builder.parse(inputSource);
        }catch (ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }

        getStrings(getBase(), "string");
    }

    public String get(String id){
        Element element = document.getElementById(id);

        Text text=null;
        if(element==null)
            element = document.getElementById("null");

              text = (Text) element.getFirstChild();

        String res="";
        try {
            assert text != null;
            res=text.getNodeValue();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return res;
    }

    public List getChildElementsByName(Element parent, String name){
        List elements = new ArrayList();
        NodeList children = parent.getChildNodes();
        for (int i=0; i<children.getLength(); i++){
            Node node = children.item(i);
            if(node.getNodeType()==Node.ELEMENT_NODE){
                Element element = (Element)node;
                if(element.getTagName().equals(name)){
                    elements.add(element);
                }
            }
        }

        return elements;
    }

    public Element getBase(){
        return document.getDocumentElement();
    }

    public String[] getStrings(Element parent, String name){
        List children = getChildElementsByName(parent, name);
        int LAST = children.size();
        String str[] = new String[LAST];

        for (int i = 0; i<LAST; i++){
            Node child = (Node) children.get(i);
            StringBuilder buffer = new StringBuilder();
            NodeList nodeList = child.getChildNodes();
            for (int j=0; j<nodeList.getLength();j++){
                Node node = nodeList.item(j);
                if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType()==Node.CDATA_SECTION_NODE){
                    Text text = (Text) node;
                    buffer.append(text.getData().trim());
                }
            }
            str[i]=buffer.toString();
        }

        strings = new String[str.length];
        strings=str;
        return str;
    }
}