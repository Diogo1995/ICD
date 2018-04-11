import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class Loja {
	
	public final static String contexto = "";
	private Document Utilizadores;
	private Document Pecas;
	
	public Loja() {
		this.Utilizadores = ValidarXML("utilizador.xml"); //TODO alterar nomes de xmls?
		this.Pecas = ValidarXML("peça.xml");		
	}

	private Document ValidarXML(String XMLdoc) {
		XMLdoc = contexto + XMLdoc;
		DocumentBuilder docBuilder;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		Document D = null; // representa a arvore DOM com o xml
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			File sourceFile = new File(XMLdoc);
			D = docBuilder.parse(sourceFile);
			return D;
		} catch (ParserConfigurationException e) {
			System.out.println("Wrong parser configuration: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Could not read source file: " + e.getMessage());
		}
		return D;
	}
	
	
	public void menuAcesso() {
		char op;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println(Utilizadores.getDocumentElement().getElementsByTagName("Utilizador").getLength());
			System.out.println();
			System.out.println("*** Login Loja ***");
			System.out
					.println("1 - Login.");
			System.out
					.println("2 - Registar");
			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.charAt(0);
			else
				op = ' ';
			switch (op) {
			case '1':
				String nif = menuLogin(sc);
				System.out.println(nif);
				if(login(nif).equals("Cliente")){
					menuCliente(sc, nif);
				}else if(login(nif).equals("Loja")) menuFuncionarioLoja(sc, nif);
				else if(login(nif).equals("Caixa")) menuFuncionarioCaixa(sc, nif);
				
				break;
				
			case '2':
				if(!menuRegistar(sc)) System.out.println("Erro ao registar. Voltando ao menu principal.");
				else System.out.println("Registado com sucesso!");
				System.out.println();
				break;
				
			case '0':
				break;
			default:
				System.out.println("Opção inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.out.println("Terminou a execução.");
		System.exit(0);
	}


	/**Aqui entra assumindo que é cliente**/
	
	public void menuCliente(Scanner sc, String nif) {
		char op;
		Node catalogo = Pecas.getDocumentElement();
		do {
			System.out.println();
			System.out.println();
			System.out.println("*** Catalogo Loja ***");
			System.out
					.println("1 - Equipamentos Masculinos.");
			System.out
					.println("2 - Equipamentos Femininos.");
			System.out
					.println("3 - Equipamentos Criança.");
			System.out.println("4 - Acessórios");
			System.out.println("5 - Ver Carrinho De Compras.");
			System.out.println("6 - Terminar Sessão.");
			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.charAt(0);
			else
				op = ' ';
			switch (op) {
			case '1':
				NodeList pecasH = getNodesByTag(catalogo.getChildNodes(), "Secção", "Homem");
				menuEquipamentos(sc, nif, pecasH);
				break;
				
			case '2':
				NodeList pecasM = getNodesByTag(catalogo.getChildNodes(), "Secção", "Mulher");
				menuEquipamentos(sc, nif, pecasM);
				break;
				
			case '3':
				NodeList pecasC = getNodesByTag(catalogo.getChildNodes(), "Secção", "Criança");
				menuEquipamentos(sc, nif, pecasC);
				break;
				
			case '4':
				NodeList pecasA = getNodesByTag(catalogo.getChildNodes(), "Secção", "Acessorio");
				//menuPecas();
				//TODO
				break;
				
			case '5':
				//TODO
				break;
				
			
			case '6':
				menuAcesso();
				break;
				
			case '0':
				break;
			default:
				System.out.println("Opção inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.out.println("Terminou a execução.");
		System.exit(0);
	}
	

	
	public void menuFuncionarioCaixa(Scanner sc, String nif) {
		char op;
		do {
			System.out.println();
			System.out.println();
			System.out.println("*** Funcionário Caixa ***");
			System.out
					.println("1 - Consultar carrinhos de compras por aprovar.");
			System.out.println("2 - Terminar Sessão.");
			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.charAt(0);
			else
				op = ' ';
			switch (op) {
			
			case '1':
				//apresenta();
				//TODO
				break;
				
			case '2':
				menuAcesso();
				break;
				
			case '0':
				break;
			default:
				System.out.println("Opção inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.out.println("Terminou a execução.");
		System.exit(0);
	}
	
	
	
	public void menuFuncionarioLoja(Scanner sc, String nif) {
		char op;
		do {
			System.out.println();
			System.out.println();
			System.out.println("*** Funcionário Loja ***");
			System.out
					.println("1 - Adicionar nova peça.");
			System.out
					.println("2 - Modificar preço de peça existente.");
			System.out
					.println("3 - Modificar quantidade de peça existente.");
			System.out.println("4 - Terminar Sessão.");
			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.charAt(0);
			else
				op = ' ';
			switch (op) {
			
			case '1':
				//apresenta();
				break;
				
			case '4':
				menuAcesso();
				break;
				
			case '0':
				break;
			default:
				System.out.println("Opção inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.out.println("Terminou a execução.");
		System.exit(0);
	}
	
	/**TODO
	 * 
	 * @param sc
	 * @return
	 */
	private String menuLogin(Scanner sc) {
		System.out.println("Introduza o seu NIF.");
		String nif = sc.nextLine();
		
		while(!validarNif(nif)) {
			if (nif.equals("0")) break;
			System.out.println("NIF introduzido inválido. Introduza novamente o NIF.");
			nif = sc.nextLine();
		}
		
		return nif;
	}
	
	/**TODO
	 * 
	 * @param nif
	 * @return
	 */
	private boolean validarNif(String nif) {
		if (nif.length() == 9) {
			for (int i = 0; i < nif.length(); i++) {
				if(!Character.isDigit(nif.charAt(i))) return false;
			}
			return true;
		}
		return false;
	}
	
	
	/**TODO
	 * 
	 * @param nif
	 * @return
	 */
	private String login(String nif) {
		//TODO
		NodeList nodesUtilizador = Utilizadores.getDocumentElement().getElementsByTagName("Utilizador");
		for(int i = 0; i < nodesUtilizador.getLength(); i++) {
			if(nodesUtilizador.item(i).getAttributes().getNamedItem("NIF").getTextContent().equals(nif)) {
				if( nodesUtilizador.item(i).getChildNodes().item(1).getNodeName().equals("Funcionário")) {
					return nodesUtilizador.item(i).getChildNodes().item(1).getAttributes().getNamedItem("Local").getTextContent();
				}
				else return nodesUtilizador.item(i).getChildNodes().item(1).getNodeName();
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param sc
	 * @return
	 */
	private boolean menuRegistar(Scanner sc) {
		System.out.println("Insira o seu nome completo.");
		String nome = sc.nextLine();
		System.out.println("Insira o seu NIF.");
		String nif = sc.nextLine();
		if(!validarNif(nif)) {
			System.out.println("Nif inserido inválido ou já existente na base de dados.");
			return false;
		}
		System.out.println("Insira a sua data de nascimento (aaaa-mm-dd) ");
		String dataNasc = sc.nextLine();
		if (dataNasc.charAt(0) == '(') {
			dataNasc = dataNasc.substring(1, dataNasc.length()-1);
		}
		return registar(nome, nif, dataNasc);
	}
	
	
	/**
	 * 
	 * @param nif
	 * @return
	 */
	private boolean registar(String nome, String nif, String data) {		
		Node utilizador = Utilizadores.createElement("Utilizador");
		((Element)utilizador).setAttribute("Nome", nome);
		((Element)utilizador).setAttribute("NIF", nif);
		((Element)utilizador).setAttribute("DataNasc", data);
		
		Node cliente = Utilizadores.createElement("Cliente");
		
		utilizador.appendChild(cliente);
		Utilizadores.getDocumentElement().appendChild(utilizador);

		try {
			if(!XMLDoc.validDoc(Utilizadores, "utilizador.xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
				Utilizadores.getDocumentElement().removeChild(utilizador);
				return false;
			}
			XMLDoc.writeDocument(Utilizadores, "utilizador.xml");
			this.Utilizadores = ValidarXML("utilizador.xml");
			return true;
		} catch (SAXException e) {
			//e.printStackTrace();
			Utilizadores.getDocumentElement().removeChild(utilizador);
			return false;
		}
	}
	
	/**
	 * Retorna Nos filhos de uma nodeList a partir de uma Tag (String) filha da NodeList anterior
	 * exemplo (nl = Catálogo, tag = "Secção", tagValor = "Homem")
	 * 
	 * @param nl
	 * @param tag
	 * @return
	 */
	private NodeList getNodesByTag(NodeList nl, String tag, String tagValor) {
		Node root = Pecas.getDocumentElement();
		for(int i = 1; i < nl.getLength()-1; i++) {
			System.out.println(i);
			if(!nl.item(i).getAttributes().getNamedItem(tag).getTextContent().equals(tagValor)) {
				root.removeChild(nl.item(i));
			}
		}
		return root.getChildNodes();
	}

	
	/**
	 * Menu Equipamentos (Vestuario/Calcado)
	 * 
	 * @param sc
	 * @param nif
	 * @param nl
	 */
	public void menuEquipamentos(Scanner sc, String nif, NodeList pecas) {
		char op;

		do {
			System.out.println();
			System.out.println();
			System.out.println("*** Vestuário/Calçado ***");
			System.out.println("1 - Vestuário.");
			System.out.println("2 - Calçado.");
			System.out.println("3 - Voltar.");
			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.charAt(0);
			else
				op = ' ';
			switch (op) {
			
			case '1':
				menuPecas(sc, nif, pecas, "Vestuário");
				break;
				
			case '2':
				menuPecas(sc, nif, pecas, "Calçado");
				break;
				
			case '3':
				menuCliente(sc, nif);
				break;
				
			case '0':
				break;
			default:
				System.out.println("Opção inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.exit(0);
	}
	
	
	public void menuPecas(Scanner sc, String nif, NodeList pecas, String tipo) {
		System.out.println();
		System.out.println();
		System.out.println("*** " + tipo + " ***");
		System.out.println("");
		
		
		int index = 0;
		/*System.out.println(pecas.item(index).getChildNodes().getLength());
		System.out.println(pecas.item(index).getChildNodes().item(5));*/

		for(int i = 0; i < pecas.getLength(); i++) {
			if(pecas.item(2).getChildNodes().item(5).getNodeName().equals(tipo))
			System.out.println(++index + " - " + pecas.item(2).getAttributes().getNamedItem("Designação").getTextContent());
		}
		
		//TODO utilizar input para passar ao proximo menu
	}
	
	
	/**Ele comeca com a o login como cliente**/
	
	public static void main(String[] args) {
	
		Loja loja = new Loja();
		if (XMLDoc.validDocXSD("utilizador.xml", "utilizador.xsd") &&
				XMLDoc.validDocXSD("peça.xml", "peça.xsd"))loja.menuAcesso();
	
	}
}
