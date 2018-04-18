import java.util.ArrayList;

import javax.xml.XMLConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class comando {
	Document cmd = null; 
	
	public comando() {
		cmd = XMLReadWrite.documentFromString("<?xml version='1.0' encoding='ISO-8859-1'?><protocol></protocol>");
	}
	
	public comando(Document D) {
		cmd = D;
	}
	
	public void show() {
		XMLReadWrite.writeDocument(cmd, System.out);
	}
	
	public Document requestLogin(String nif) {
		Element login = cmd.createElement("login");
		Element request = cmd.createElement("request");
		login.appendChild(request);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(login);
		
		Element nifElem = cmd.createElement("nif");	
		nifElem.appendChild(cmd.createTextNode(nif));
		request.appendChild(nifElem);

		return cmd;
	}
	
	public Document requestConsultar() {  // usar no cliente
		// <consultar><request></request></consultar>
		Element consultar = cmd.createElement("consultar");
		Element request = cmd.createElement("request");
		consultar.appendChild(request);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(consultar);
		return cmd; 
	}

	/*
	private Document replyConsultar() {  // usar no servidor
		//TODO
		//mudar para loja e tal
		Document titulos=Poema.Consultar();
		NodeList T = titulos.getElementsByTagName("título");
		Element reply = cmd.createElement("reply");
		for(int i=0; i<T.getLength();i++) {
			Element clone = (Element) cmd.importNode(T.item(i), true);
			reply.appendChild(clone);
		}
		Element consultar = (Element) cmd.getElementsByTagName("consultar").item(0);
		consultar.appendChild(reply);
		return cmd; 
	}

	*/
	
	public Document replyLogin() {
		Document utilizadores = Loja.getUtilizadores();
		NodeList util = utilizadores.getElementsByTagName("Utilizador");
		
		Element reply = cmd.createElement("reply");
		
		for(int i = 0; i < util.getLength(); i++) {
			if(util.item(i).getAttributes().getNamedItem("NIF").getTextContent().equals(cmd.getElementsByTagName("nif").item(0).getTextContent())) {
				System.out.println("FREEDOM!");
			}
			Element clone = (Element) cmd.importNode(util.item(i), true);
			reply.appendChild(clone);
		}
		Element login = (Element)cmd.getElementsByTagName("login").item(0);
		login.appendChild(reply);
		return cmd;
	}
	/*
	private Document replyObter() {  // usado no reply
		// as palavras já estão no comando
				
		NodeList pal = cmd.getElementsByTagName("palavra");
		
		ArrayList<String> palList = new ArrayList<String>();
		for (int i=0; i<pal.getLength(); i++) {
			palList.add(pal.item(i).getTextContent());
		}
		String[] palavras = palList.toArray(new String[palList.size()]);
		
		Element reply = cmd.createElement("reply");
		//TODO
		//mudar para loja e tal
		Document poema = Poema.Obter(palavras);
		if(poema!=null) {
			Element p = (Element) poema.getElementsByTagName("poema").item(0);
			Element clone = (Element) cmd.importNode(p, true);
			reply.appendChild(clone);
		}
		Element obter = (Element) cmd.getElementsByTagName("obter").item(0);
		obter.appendChild(reply);
		show();
		return cmd; 
	}
	*/
	public Document reply() {  // usar no servidor
		Document com = null;
		show();
		if(cmd.getElementsByTagName("login").getLength()==1)
			com = replyLogin();
		/*
		if(cmd.getElementsByTagName("obter").getLength()==1)
			com = replyObter();
		*/
		if(com==null)
			return cmd;
		else {
			try {
				if (XMLDoc.validDoc(com, Loja.contexto+"protocol.xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI))
					System.out.println("\nValidação com XSD realizada com sucesso!");
			} catch (SAXException e) {
				System.out.println("\nFalhou a validação com XSD (protocol.xsd)!"+e.getMessage());
				e.printStackTrace();
			}
			return com;
		}
	}
}
