package utilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.xml.parsers.*;

import constant.StringContant;
import entities.TblCategory;
import error.ErrorHandlerWriter;
import org.xml.sax.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLUtilities {

    public boolean validationWithDTD(String filePath) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandlerWriter());
            builder.parse(new InputSource(filePath));

            System.out.println("Validation success, start importing to Database...\n");
            return true;

        } catch (Throwable e) {
            System.out.println("Validation failed");
            return false;
        }
    }


    public void createXMLwithDOMandDTD(Map<String, String> crawlPageCategories,
                                       String outputFilePath, String dtdPath) {
        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            //<categories>
            Element categories = doc.createElement("categories");
            doc.appendChild(categories);

            for (Map.Entry<String, String> entry : crawlPageCategories.entrySet()) {
                String cteLink = entry.getKey();
                String cteName = entry.getValue();
                //<category>
                Element category = doc.createElement("category");
                categories.appendChild(category);

                //<name>
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(cteLink));
                category.appendChild(name);

                //<url>
                Element url = doc.createElement("url");
                url.appendChild(doc.createTextNode(cteName));
                category.appendChild(url);
            }

            //document prolog
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdPath);

            DOMSource source = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(outputFilePath));
            transformer.transform(source, streamResult);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public void createXMLwithDOMForCategorizedProduct(ArrayList<CategorizedProduct> products,
//                                                      String outputFilePath) {
//        try {
//            DocumentBuilderFactory dbFactory
//                    = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.newDocument();
//
//            //<categories>
//            Element root = doc.createElement("products");
//            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//            root.setAttribute("xmlns", "http://xml.netbeans.org/schema/products");
//            root.setAttribute("xsi:schemaLocation", "http://xml.netbeans.org/schema/products categorizedProduct.xsd");
//
//            doc.appendChild(root);
//
//            for (int i = 0; i < products.size(); i++) {
//                //<category>
//                Element product = doc.createElement("product");
//                root.appendChild(product);
//
//                //<categoryId>
//                int categoryId = 0;
//                if(products.get(i).getGear().toLowerCase().contains("vô cấp") ||
//                        products.get(i).getGear().toLowerCase().contains("tự động") ||
//                        products.get(i).getGear().toLowerCase().contains("điện") ||
//                        products.get(i).getGear().toLowerCase().contains("v-matic")){
//                    categoryId = 1;
//                }else{
//                    categoryId = 2;
//                }
//
//                Element ecategoryId = doc.createElement("categoryId");
//                ecategoryId.appendChild(doc.createTextNode(String.valueOf(categoryId)));
//                product.appendChild(ecategoryId);
//
//                //<brandId>
//                Element brandId = doc.createElement("brandId");
//                brandId.appendChild(doc.createTextNode(String.valueOf(products.get(i).getBrandId())));
//                product.appendChild(brandId);
//
//                //<title>
//                Element title = doc.createElement("title");
//                title.appendChild(doc.createTextNode(products.get(i).getTitle()));
//                product.appendChild(title);
//
//                //<url>
//                Element url = doc.createElement("url");
//                url.appendChild(doc.createTextNode(products.get(i).getUrl()));
//                product.appendChild(url);
//
//                //<standardPrice>
//                Element standardPrice = doc.createElement("standardPrice");
//                standardPrice.appendChild(doc.createTextNode(String.valueOf(products.get(i).getStandardPrice())));
//                product.appendChild(standardPrice);
//
//                //<length>
//                Element length = doc.createElement("length");
//                length.appendChild(doc.createTextNode(String.valueOf(products.get(i).getLength())));
//                product.appendChild(length);
//
//                //<width>
//                Element width = doc.createElement("width");
//                width.appendChild(doc.createTextNode(String.valueOf(products.get(i).getWidth())));
//                product.appendChild(width);
//
//                //<height>
//                Element height = doc.createElement("height");
//                height.appendChild(doc.createTextNode(String.valueOf(products.get(i).getHeigh())));
//                product.appendChild(height);
//
//                //<tire>
//                Element tire = doc.createElement("tire");
//                tire.appendChild(doc.createTextNode(products.get(i).getTireSize()));
//                product.appendChild(tire);
//
//                //<engine>
//                Element engine = doc.createElement("engine");
//                engine.appendChild(doc.createTextNode(products.get(i).getEngineType()));
//                product.appendChild(engine);
//
//                //<hp>
//                Element hp = doc.createElement("hp");
//                hp.appendChild(doc.createTextNode(String.valueOf(products.get(i).getHp())));
//                product.appendChild(hp);
//
//                //<cylinder>
//                Element cylinder = doc.createElement("cylinder");
//                cylinder.appendChild(doc.createTextNode(String.valueOf(products.get(i).getCylinder())));
//                product.appendChild(cylinder);
//
//                //<gear>
//                Element gear = doc.createElement("gear");
//                gear.appendChild(doc.createTextNode(products.get(i).getGear()));
//                product.appendChild(gear);
//
//                //<hp>
//                Element saddleHeight = doc.createElement("saddleHeight");
//                saddleHeight.appendChild(doc.createTextNode(String.valueOf(products.get(i).getSaddleHeight())));
//                product.appendChild(saddleHeight);
//            }
//
//            //write to xml file
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//            DOMSource source = new DOMSource(doc);
//            StreamResult streamResult = new StreamResult(new File(MyConstants.BASE_PATH_XML + "//" +  outputFilePath));
//            transformer.transform(source, streamResult);
//
////            StreamResult consoleResult = new StreamResult(System.out);
////            transformer.transform(source, consoleResult);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

//read file
    public static XMLStreamReader parseFileToStAXCursor(InputStream is)
            throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(is);
        return reader;
    }

    public static String getNodeStAXAttribute(XMLStreamReader reader, String elementName,
                                              String namespaceURI, String attrName) throws XMLStreamException {
        if (reader != null) {
            while (reader.hasNext()) {
                int cursor = reader.getEventType();
                if (cursor == XMLStreamConstants.START_ELEMENT){
                    String tagName = reader.getLocalName();
                    if(tagName.equals(elementName)){
                        String result = reader.getAttributeValue(namespaceURI, attrName);
                        return result;
                    }//end if tagName is equals to elementName
                }//end if cursor is start element
            }//end while reader not null
        };//end if reader is null
        return null;
    }

    public static String getTextStAXContext(XMLStreamReader reader, String elementName) throws XMLStreamException{
        if(reader != null){
            while(reader.hasNext()){
                int cursor = reader.getEventType();
                if (cursor == XMLStreamConstants.START_ELEMENT){
                    String tagName = reader.getLocalName();
                    if(tagName.equals(elementName)){
                        reader.next();//go to value
                        String result = reader.getText();
                        reader.nextTag();//end element
                        return result;
                    }//end if tagName is equals to elementName
                }//end if cursor is start element
                reader.next();
            } //end while
        }//end if reader is null
        return null;
    }

    public boolean validateWithDOMAndSchema(String xmlFilePath, String xsdFilePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);

            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            factory.setSchema(schemaFactory.newSchema(new Source[]{
                    new StreamSource(StringContant.BASE_PATH_XML + "//" + xsdFilePath)
            }));


            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandlerWriter());
            Document document = builder.parse(new InputSource(StringContant.BASE_PATH_XML + "//" + xmlFilePath));

            return true;
        } catch (Throwable e) {
            System.out.println("Validation failed");
            e.printStackTrace();
            return false;
        }
    }
}
