import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.io.StringReader;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.BadLocationException;

/**
 * Creates, writes and read from a html file. 
 * Axtractes hyperlinks from a html file.
 * 
 * @see java.io.File
 * @see java.util.Scanner
 * @see java.io.FileWriter
 * @see java.io.IOException
 * @see java.util.ArrayList
 * @see java.io.StringReader
 * @see java.io.FileNotFoundException
 * @see java.net.MalformedURLException
 *
 * @see javax.swing.text.html.HTML
 * @see javax.swing.text.html.HTMLDocument
 * @see javax.swing.text.html.HTMLEditorKit
 * @see javax.swing.text.BadLocationException
 * 
 * @author Mahmoud Tisry
 * @version 1.0
 * 
 */
public class FileReadWrite {
	
	ArrayList<String> linkURL = new ArrayList<String>();
	ArrayList<String> linkhyper = new ArrayList<String>();

	String fileName = new String();
	String hyperLink = new String();
	
	/**
	 * Initializes a newly created FileReadWrite object. 
	 * 
	 */
	public FileReadWrite() {
		linkURL = null;	
		fileName = "";
		hyperLink = "";
		linkhyper = null;
	}
	
	/**
	 * Allocates a new FileReadWrite that contains a new file name.
	 * 
	 * @param fileName
	 */
	public FileReadWrite( String fileName ) {
		if( fileName != null )
			this.fileName = fileName;
		else
			System.err.println( "Kein gueltiger Dateiname!" );
	}
	
   /**
    * Read URLs from a html file, and store them as ArrayList.
    *     
    */
    public void readFromFile()
    {
        // es wird die Klasse File benutzt um eine Datei pages.txt zu referenzieren.
        File file = new File ( this.fileName );
        // Die Klasse Scanner emoeglicht es uns einfach mit einem File Objekt zu arbeiten und dessen Inhalt einzulesen
        Scanner scanner = null;
        try {
        	// hier verbinde ich die Objekte der Klasse Scanner und File.
        	scanner = new Scanner ( file );
        	
        } catch (FileNotFoundException e) {
        	System.out.println ("File not found: " + file );
        	System.exit (0);
        }
   
        // solange die methode hasNext im Scanner weitere Zeilen feststellt
        while(scanner.hasNext())
        {
            // es wird eine zeile ausgelesen aus der datei und in der variable tmp gespeichert
            String tmp = scanner.nextLine();

            // die gerade eingelesene Zeile wird in ArrayList gespeichert
            this.linkURL.add(tmp);
            System.out.println( "Read URL " +  "'" + tmp + "'" + " From " + "file " + "'" + file + "'");
        }
    }
    /**
     * Writes the answer from the server to a txt file.
     * 
     * @param responseBody
     * @throws MalformedURLException 
     * 
     */
    public void writeToFileTXT( String responseBody, String fileName ) {
    	
    	fileName = fileName.replace("/", "-");
    	
    	FileWriter tmpFileWriter = null;
   
    	// Speichere den Dateinamen "linkURL.txt" in tmpfw
    	File tmpfw = new File( fileName + ".txt");
    	
    	try {
			tmpfw.createNewFile();
		} catch (IOException e1) {
			System.err.println("Can not create File: " + tmpfw );
    		System.exit(0);
		}
    	System.out.println("Create file: " + tmpfw );
		
		try {
	    	// Speichere den Dateinamen "linkURL.txt" in tmpFileWriter
			tmpFileWriter = new FileWriter( tmpfw );
		} catch (IOException e) {
    		System.err.println("Can not find file: " + tmpfw );
    		System.exit(0);
		}
	
    	try {    	 
    		// Schreibe den Inhalt der Server-Antwort responseBody in der Datei "linkURL.txt"
    		tmpFileWriter.write( responseBody.toString() );
    	} catch (IOException e) {
    		System.err.println("Can not write to File: " + tmpfw );
    		System.exit(0);
    	}	
    	System.out.println("Write to file: " + tmpfw );
    	
    	try {
			tmpFileWriter.close();
		} catch (IOException e) {
			System.err.println("Can not close File: " + tmpfw );
    		System.exit(0);
		}
    }
    
    /**
     * Writes the answer from the server to a html file.
     * 
     * @param responseBody
     * @param fileName
     * 
     * @throws MalformedURLException 
     * 
     */
    public void writeToFileHTML( String responseBody, String fileName) throws MalformedURLException {
    	
    	fileName = fileName.replace("/", "-");

    	FileWriter tmpFileWriter = null;
    	
    	// Speichere den Dateinamen "linkURL.html" in tmpfw
    	File tmpfw = new File( fileName + ".html");

		try {
	    	// Speichere den Dateinamen "linkURL.html" in tmpFileWriter
			tmpFileWriter = new FileWriter( tmpfw );
		} catch (IOException e) {
    		System.err.println("Can not find file: " + tmpfw );
    		System.exit(0);
		}
	
    	try {    	 
    		// Schreibe den Inhalt der Server-Antwort responseBody in der Datei "linkURL.html"
    		tmpFileWriter.write( responseBody );
    	} catch (IOException e) {
    		System.err.println( "Can not write to File: " + tmpfw );
    		System.exit(0);
    	}	
    	System.out.println( "Write to file: " + tmpfw );

    	try {
			tmpFileWriter.close();
		} catch (IOException e) {
			System.err.println("Can not close File: " + tmpfw );
    		System.exit(0);
		}
    }
    /**
     * Extractes hyperlinks from html document.
     *
     * @param index
     * @param responseBody
     * 
     * @throws IOException
     * @throws BadLocationException
     */
    public String linkExtractor(int index, String responseBody ) throws IOException, BadLocationException {

    	  HTMLEditorKit editorKit = new HTMLEditorKit();
	      HTMLDocument htmlDoc = new HTMLDocument();
	      htmlDoc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
	      
	      StringReader is = new StringReader( responseBody );
	      
	      editorKit.read(is , htmlDoc, 0);
	 
	      HTMLDocument.Iterator iter = htmlDoc.getIterator(HTML.Tag.A);
	      int i = 0;
	      while (iter.isValid()) {
    		  if(i++ > 2)
    			  this.hyperLink = iter.getAttributes().getAttribute(HTML.Attribute.HREF).toString();
	    	  // nur links die http:// enthalten
	    	  if ( hyperLink.contains( "/permalink/" ) == true) {

		    		  System.out.println( hyperLink );
		    		  System.out.println( );

	    		//  }
	    		  
	    	  }
	    	  iter.next();	
	      }
		return this.hyperLink;
	  }
}
