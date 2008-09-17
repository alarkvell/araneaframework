/**
 * Copyright 2006-2007 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

package org.araneaframework.buildutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility to merge tag attribute information found from 'tag-class-descriptor' (contained in
 * aranea.jar) with TLD generated by xdoclet and containing tags that extend concrete or abstract
 * tag classes of Aranea JSP. Its purpose is to avoid unnecessary duplication of methods and
 * xdoclet annotations; or needing always to keep Aranea JSP source ready when compiling tags
 * depending on Aranea JSP.
 *  
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class TcdAndTldMerger {
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("USAGE: TCD TLD outputTLD");
			System.err.println("If TCD is not found from file system, it is searched from classpath.");
			System.err.println("TLD and outputTLD may be the same, though it is not recommended.");
			System.err.println("Tag classes referenced by TCD and/or TLD must all be available on classpath.");
			System.exit(1);
		}
		
		//System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");

		String tcdName = args[0], tldName = args[1], outputTLD = args[2];
		
		File firstFile = new File(tcdName);
		InputStream first = firstFile.exists() ? new FileInputStream(tcdName) : TcdAndTldMerger.class.getClassLoader().getResourceAsStream(tcdName);
		
		if (first == null) {
			System.err.println(tcdName + " was not found from filesystem or classpath.");
			System.exit(1);
		}

		File second = new File(tldName);
		
		if (!second.exists() || !second.canRead()) {
			System.err.println(tldName + " unreadable.");
			System.exit(1);
		}
		
		File output = new File(outputTLD);
		
		process(first, second, output);
	}
	
	public static void process(InputStream first, File second, File output) throws Exception {
		 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		 
		 Document fDoc = docBuilder.parse(first);
		 Document sDoc = docBuilder.parse(second);
		 
		 NodeList sTags = sDoc.getElementsByTagName("tag");
		 
		 for (int i = 0; i < sTags.getLength(); i++) {
			 Node current = sTags.item(i);
			 NodeList children = current.getChildNodes();
			 for (int j = 0; j < children.getLength(); j++) {
				 Node node = children.item(j);
				 if (node.getNodeName().equals("tag-class")) {
					 String tagClassName = node.getTextContent();
					 if (tagClassName == null)
						 continue;
					 Class tagClazz = Class.forName(tagClassName);
					 while (tagClazz.getSuperclass() != null) {
						 List additionalAttributes = getAttributesFromTCD(fDoc,  tagClazz.getSuperclass().getName());
						 for (Iterator it = additionalAttributes.iterator(); it.hasNext(); ) {
							 Node additional = (Node) it.next();
							 String attrName = ((Element) additional).getElementsByTagName("name").item(0).getTextContent();
							 
							 // attribute from parentclass should only be added if it does not exist already 
							 NodeList currentAttrs = ((Element)current).getElementsByTagName("attribute");
							 
							 boolean found = false;
							 for (int zz = 0; zz < currentAttrs.getLength(); zz++) {
								 if (((Element)currentAttrs.item(zz)).getElementsByTagName("name").item(0).
										 getTextContent().equals(attrName)) {
									 found = true;
									 break;
								 }
							 }
							 
							 if (found) continue;

							 current.appendChild(sDoc.adoptNode((additional.cloneNode(true))));
						 }
						 
						 // if any supertag has extra attributes, it should have them all
						 if (additionalAttributes.size() > 0) {
							 System.out.println(additionalAttributes.size() + " attributes for '" + tagClassName + "' found from '" +  tagClazz.getSuperclass().getName() + "'.");
							 break;
						 }

						 tagClazz = tagClazz.getSuperclass();
					 }
				 }
			 }
		 }

		Source input = new DOMSource(sDoc);
		OutputStream ostream = new FileOutputStream(output);
		Result out = new StreamResult(ostream);

		TransformerFactory xformFactory = TransformerFactory.newInstance();

		try {
			Transformer idTransformer = xformFactory.newTransformer();
			
			if (sDoc.getDoctype() != null){
				String publicValue = (sDoc.getDoctype().getPublicId());
				idTransformer.setOutputProperty(
						OutputKeys.DOCTYPE_PUBLIC, publicValue
				);

				String systemValue = (sDoc.getDoctype().getSystemId());
				idTransformer.setOutputProperty(
						OutputKeys.DOCTYPE_SYSTEM, systemValue
				);
				
				idTransformer.setOutputProperty(OutputKeys.INDENT, "yes" );
			}
			
			idTransformer.transform(input, out);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ostream.flush();
			ostream.close();
		}
	}
	

	public static List getAttributesFromTCD(Document tcd, String tagClass) {
		NodeList nodes = tcd.getElementsByTagName("tag-class");
		Node tagClassNode = null;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node current = nodes.item(i);
			if (tagClass.equals(current.getTextContent())) {
				tagClassNode = current;
				break;
			}
		}
		
		if (tagClassNode == null)
			return Collections.EMPTY_LIST;
		
		Node node = tagClassNode;
		while (node.getPreviousSibling() != null) {
			node = node.getPreviousSibling();
		}

		List result = new ArrayList();
		
		while (node.getNextSibling() != null) {
			if (node.getNextSibling().getNodeName().equals("attribute"))
				result.add(node.getNextSibling());
			
			node = node.getNextSibling();
		}
		
		return result;
	}
}
