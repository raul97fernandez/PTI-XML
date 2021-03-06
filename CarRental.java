import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import org.jdom2.input.sax.XMLReaders;

public class CarRental {

	//public static int id = 0;

    /**
     * Read and parse an xml document from the file at example.xml.
     * @return the JDOM document parsed from the file.
     */
    public static Document readDocument() {
        try {
            SAXBuilder builder = new SAXBuilder();
			Document anotherDocument = builder.build(new File("CarRental.xml"));
			return anotherDocument;
        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method creates a JDOM document with elements that represent the
     * properties of a car.
     * @return a JDOM Document that represents the properties of a car.
     */
    public static Document createDocument() {
        // Create the root element
        Element carrentalElement = new Element("carrental");
        //create the document
        Document myDocument = new Document(carrentalElement);

        return myDocument;
    }

    /**
     * This method accesses a child element of the root element 
     * @param myDocument a JDOM document 
     */
    public static void accessChildElement(Document myDocument) {
        //some setup
        Element carElement = myDocument.getRootElement();

        //Access a child element
        Element yearElement = carElement.getChild("year");

        //show success or failure
        if(yearElement != null) {
            System.out.println("Here is the element we found: " +
                yearElement.getName() + ".  Its content: " +
                yearElement.getText() + "\n");
        } else {
            System.out.println("Something is wrong.  We did not find a year Element");
        }
    }

    /**
     * This method removes a child element from a document.
     * @param myDocument a JDOM document.
     */
    public static void removeChildElement(Document myDocument) {
        //some setup
        System.out.println("About to remove the year element.\nThe current document:");
        outputDocument(myDocument);
        Element carElement = myDocument.getRootElement();

        //remove a child Element
        boolean removed = carElement.removeChild("year");

        //show success or failure
        if(removed) {
            System.out.println("Here is the modified document without year:");
            outputDocument(myDocument);
        } else {
            System.out.println("Something happened.  We were unable to remove the year element.");
        }
    }

    /**
     * This method shows how to use XMLOutputter to output a JDOM document to
     * the stdout.
     * @param myDocument a JDOM document.
     */
    public static void outputDocument(Document myDocument) {
        try {
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(myDocument, System.out);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method shows how to use XMLOutputter to output a JDOM document to
     * a file located at myFile.xml.
     * @param myDocument a JDOM document.
     */
    public static void outputDocumentToFile(Document myDocument) {
        //setup this like outputDocument
        try {
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter();

            //output to a file
            FileWriter writer = new FileWriter("CarRental.xml");
            outputter.output(myDocument, writer);
            writer.close();

        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void validate () {
        try {
            SAXBuilder builder = new SAXBuilder(XMLReaders.XSDVALIDATING);
            Document anotherDocument = builder.build(new File("carrental.xml"));
            System.out.println("Root: " + anotherDocument.getRootElement().getName());

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method takes a JDOM document in memory, an XSLT file at example.xslt,
     * and outputs the results to stdout.
     * @param myDocument a JDOM document .
     */
    public static void executeXSLT(Document myDocument) {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
            // Make the input sources for the XML and XSLT documents
            org.jdom2.output.DOMOutputter outputter = new org.jdom2.output.DOMOutputter();
            org.w3c.dom.Document domDocument = outputter.output(myDocument);
            javax.xml.transform.Source xmlSource = new javax.xml.transform.dom.DOMSource(domDocument);
            StreamSource xsltSource = new StreamSource(new FileInputStream("carrental.xslt"));
			//Make the output result for the finished document
            StreamResult xmlResult = new StreamResult(System.out);
			//Get a XSLT transformer
			Transformer transformer = tFactory.newTransformer(xsltSource);
			//do the transform
			transformer.transform(xmlSource, xmlResult);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(TransformerConfigurationException e) {
            e.printStackTrace();
		} catch(TransformerException e) {
            e.printStackTrace();
        } catch(org.jdom2.JDOMException e) {
            e.printStackTrace();
        }
	}

    /**
     * Main method that allows the various methods to be used.
     * It takes a single command line parameter.  If none are
     * specified, or the parameter is not understood, it prints
     * its usage.
     */
    public static void main(String argv[]) {
        if(argv.length == 1) {
            String command = argv[0];
            if(command.equals("reset")) outputDocumentToFile(createDocument());
            else if(command.equals("new")) new_element();
            else if(command.equals("list")) outputDocument(readDocument());
            else if(command.equals("xslt")) executeXSLT(readDocument());
            else if(command.equals("validate")) outputDocument(readDocument());
            else {
                System.out.println(command + " is not a valid option.");
                printUsage();
            }
        } else {
            printUsage();
        }
    }

    /**
     * Convience method to print the usage options for the class.
     */
    public static void printUsage() {
        System.out.println("Usage: Example [option] \n where option is one of the following:");
        System.out.println("  reset - create a new document in memory and print it to the console");
        System.out.println("  new - create a new document and show its child element");
        System.out.println("  list - create a new document and remove its child element");
        System.out.println("  xslt  - create a new document and save it to myFile.xml");
        System.out.println("  validate   - read and parse a document from example.xml");
    }

	public static void new_element(){
		Document doc = readDocument();
		Element root = doc.getRootElement();
		Element e = askData();
		root.addContent(e);
		doc.setContent(root);
		outputDocumentToFile(doc);
		outputDocument(doc);
		//++id;
	}
	
	public static Element askData() {
		Element rentalElement = new Element("rental");

		//rentalElement.setAttribute(new Attribute("id", "" + id));

		System.out.print("Make:");
		String input = System.console().readLine();
		Element make = new Element("make");
		make.addContent(input);
		rentalElement.addContent(make);

		System.out.print("Model:");
		input = System.console().readLine();
		Element model = new Element("model");
		model.addContent(input);
		rentalElement.addContent(model);

		System.out.print("Number of Days:");
		input = System.console().readLine();
		Element nofdays = new Element("nofdays");
		nofdays.addContent(input);
		rentalElement.addContent(nofdays);

		System.out.print("Number of Units:");
		input = System.console().readLine();
		Element nofunits = new Element("nofunits");
		nofunits.addContent(input);
		rentalElement.addContent(nofunits);	

		System.out.print("Discount:");
		input = System.console().readLine();
		Element discount = new Element("discount");
		discount.addContent(input);
		rentalElement.addContent(discount);

		return rentalElement;
	}	
}
