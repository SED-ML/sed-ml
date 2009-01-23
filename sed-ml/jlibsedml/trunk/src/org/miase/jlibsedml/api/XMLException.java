package org.miase.jlibsedml.api;

/**
 * XML library agnostic Exception signifying an error in reading or locating an 
 * XML data structure. The underlying exception can be obtained by calling getCause()
 * @author Richard Adams
 *
 */
public class XMLException extends Exception{

	public XMLException(String string, Exception e) {
	 super(string, e);
	}

	

}
