import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.balana.Balana;
import org.wso2.balana.Indenter;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.ctx.xacml2.Result;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;
import org.wso2.balana.utils.Utils;

public class BalanaXACML {

	
	static Balana balana;

	
	public static void main(String[] args) throws Exception {
		String policyFolder = (new File(".")).getCanonicalPath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "policies";
		String requestFolder = (new File(".")).getCanonicalPath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "requests";

		 
		
		Scanner s = new Scanner(System.in);
		System.out.println("Viable options for policy file are: \n");
		File polPath = new File(policyFolder);
	    String pcontents[] = polPath.list();
	    for(int i=0; i<pcontents.length; i++) {
	    	System.out.println(pcontents[i]);
	      }
		System.out.println("\nPolicyfile: ");
		String policyName = s.nextLine();
	    
	    System.out.println("Viable options for request file are: \n");
		File reqPath = new File(requestFolder);
	    String rcontents[] = reqPath.list();
	    for(int i=0; i<rcontents.length; i++) {
	    	System.out.println(rcontents[i]);
	    }
		System.out.println("\nRequestfile: ");
		String reqName = s.nextLine(); 
		
	 	 
	  
		 
		 initBalana(policyFolder, policyName);
		
		 PDPConfig pdpConfig = balana.getPdpConfig();
	     PDP pdp = new PDP(new PDPConfig(pdpConfig.getAttributeFinder(), pdpConfig.getPolicyFinder(), null, true));
	     
	     


	     String request = "src/main/resources/requests/" + reqName;
	     String response = pdp.evaluate(getXMLFromFilePath(request));
	     
	     
	     
	     System.out.println(response);

	}
	
	
	
	private static void initBalana(String policyFolder, String policyName) {
		String policyLocation = policyFolder + File.separator + policyName;
		System.setProperty(FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY, policyLocation);
        balana = Balana.getInstance();
    }
	
	public static String getXMLFromFilePath(String path) throws Exception {
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	       StringWriter writer = new StringWriter();
	       transformer.transform(new DOMSource(getDocument(path)), new StreamResult(writer));
	 
	       return writer.getBuffer().toString().replaceAll("\n|\r", "");
	   }
	 
	
	private static Document getDocument(String xmlFile) throws Exception {
	     DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
	     DocumentBuilder docBuilder = builder.newDocumentBuilder();
	    return docBuilder.parse(xmlFile);
	 }
	


}


