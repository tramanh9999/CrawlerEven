/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author STP
 */
public class ErrorHandlerWriter implements ErrorHandler{

    @Override
    public void warning(SAXParseException e) throws SAXException {
        System.out.print("Warning at line " + e.getLineNumber() + ": ");
        System.out.println(e.getMessage());
        throw(e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        System.out.print("Error at line " + e.getLineNumber() + ": ");
        System.out.println(e.getMessage());
        throw(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        System.out.print("Fatal Error at line " + e.getLineNumber() + ": ");
        System.out.println(e.getMessage());
        throw(e);
    }

}
