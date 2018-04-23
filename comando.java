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
	
	public Document requestRegistar(String nif, String nome, String dataNasc) {
		Element registar = cmd.createElement("registar");
		Element request = cmd.createElement("request");
		registar.appendChild(request);
		
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		
		protocol.appendChild(registar);
		
		Element nifElem = cmd.createElement("nif");
		nifElem.appendChild(cmd.createTextNode(nif));
		Element nomeElem = cmd.createElement("nome");
		nomeElem.appendChild(cmd.createTextNode(nome));
		Element dataElem = cmd.createElement("dataNasc");
		dataElem.appendChild(cmd.createTextNode(dataNasc));
		
		request.appendChild(nifElem);
		request.appendChild(nomeElem);
		request.appendChild(dataElem);
		
		return cmd;
	}
	
	public Document replyRegistar() {
		Document utilizadores = Loja.getUtilizadores();
		String nif = cmd.getElementsByTagName("nif").item(0).getTextContent();
		String nome = cmd.getElementsByTagName("nome").item(0).getTextContent();
		String dataNasc = cmd.getElementsByTagName("dataNasc").item(0).getTextContent();
		
		Element utilizador = utilizadores.createElement("Utilizador");
		Element cliente = utilizadores.createElement("Cliente");
		
		utilizador.setAttribute("NIF", nif);
		utilizador.setAttribute("Nome", nome);
		utilizador.setAttribute("DataNasc", dataNasc);
		
		utilizador.appendChild(cliente);
		utilizadores.getDocumentElement().appendChild(utilizador);
		
		Element reply = cmd.createElement("reply");
	
		Element registar = (Element) cmd.getElementsByTagName("registar").item(0);

		Element utilizadorVazio = cmd.createElement("Utilizador");
		
		registar.appendChild(reply);
		
		try {
			if(!XMLDoc.validDoc(utilizadores, "utilizador.xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
				utilizadores.getDocumentElement().removeChild(utilizador);
				reply.appendChild(utilizadorVazio);
				return cmd;
			}
			XMLDoc.writeDocument(utilizadores, "utilizador.xml");
			
			NodeList todosUtilizadores = utilizadores.getElementsByTagName("Utilizador");
			for(int i = 0; i < todosUtilizadores.getLength(); i++) {
				if(todosUtilizadores.item(i).getAttributes().getNamedItem("NIF").getTextContent().equals(nif)) {
					Element clone = (Element) cmd.importNode(todosUtilizadores.item(i), true);
					reply.appendChild(clone);
				}
			}
			return cmd;
		} catch (SAXException e) {
			//e.printStackTrace();
			utilizadores.getDocumentElement().removeChild(utilizador);
			reply.appendChild(utilizadorVazio);
			return cmd;
		}
	}
	
	public Document requestPecasTotal() {
		Element pecasTotal = cmd.createElement("pecasTotal");
		Element request = cmd.createElement("request");
		pecasTotal.appendChild(request);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(pecasTotal);
		return cmd;
	}
	
	public Document replyPecasTotal() {
		//TODO
		Document catalogo = Loja.getPecas();
		Element reply = cmd.createElement("reply");
		
		NodeList pecas = catalogo.getElementsByTagName("Peça");
		
		for(int i = 0; i < pecas.getLength(); i++) {
			Element clone = (Element) cmd.importNode(pecas.item(i), true);
			reply.appendChild(clone);
		}
		Element pecasTotal = (Element) cmd.getElementsByTagName("pecasTotal").item(0);
		pecasTotal.appendChild(reply);
		return cmd;
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
	
	public Document replyLogin() {
		Document utilizadores = Loja.getUtilizadores();
		NodeList util = utilizadores.getElementsByTagName("Utilizador");
		
		Element reply = cmd.createElement("reply");
		
		for(int i = 0; i < util.getLength(); i++) {
			if(util.item(i).getAttributes().getNamedItem("NIF").getTextContent().equals(cmd.getElementsByTagName("nif").item(0).getTextContent())) {
				Element clone = (Element) cmd.importNode(util.item(i), true);
				reply.appendChild(clone);
			}
		}
		Element login = (Element)cmd.getElementsByTagName("login").item(0);
		login.appendChild(reply);
		return cmd;
	}
	
	public Document requestCatalogo(String seccao) {
		Element catalogo = cmd.createElement("catalogo");
		Element request = cmd.createElement("request");
		catalogo.appendChild(request);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(catalogo);
		
		Element seccaoElem = cmd.createElement("seccao");
		seccaoElem.appendChild(cmd.createTextNode(seccao));
		request.appendChild(seccaoElem);

		return cmd;
	}
	
	public Document replyCatalogo() {
		Document catalogo = Loja.getPecas();
		NodeList pecas = catalogo.getElementsByTagName("Peça");
		
		Element reply = cmd.createElement("reply");
		
		for(int i = 0; i < pecas.getLength(); i++) {
			if(pecas.item(i).getAttributes().getNamedItem("Secção").getTextContent().equals(cmd.getElementsByTagName("seccao").item(0).getTextContent())) {
				Element clone = (Element) cmd.importNode(pecas.item(i), true);
				reply.appendChild(clone);
			}
		}
		Element catal = (Element)cmd.getElementsByTagName("catalogo").item(0);
		catal.appendChild(reply);
		
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
		
		if(cmd.getElementsByTagName("catalogo").getLength()==1)
			com = replyCatalogo();
		
		if(cmd.getElementsByTagName("registar").getLength()==1)
			com = replyRegistar();
		
		if(cmd.getElementsByTagName("pecasTotal").getLength()==1)
			com = replyPecasTotal();
		
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
