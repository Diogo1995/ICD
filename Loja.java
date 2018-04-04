import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class Loja {
	
	public final static String contexto = "";
	private Document Utilizadores;
	private Document Peças;
	
	public Loja() {
		this.Utilizadores = ValidarXML("utilizador.xml"); //TODO alterar nomes de xmls?
		this.Peças = ValidarXML("peça.xml");		
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
			System.out.println();
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
				else if(login(nif).equals("Caixa")) menuFuncionarioLoja(sc, nif);
				
				break;
				
			case '2':
				//TODO
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
				//apresenta();
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
					return nodesUtilizador.item(i).getAttributes().getNamedItem("local").getTextContent();
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
		//TODO
		return false;
	}
	
	
	/**
	 * 
	 * @param nif
	 * @return
	 */
	private boolean registar(String nif) {
		//TODO
		return false;
	}
	
	
	/**Ele comeca com a o login como cliente**/
	
	public static void main(String[] args) {
	
		Loja loja = new Loja();
		if (XMLDoc.validDocXSD("utilizador.xml", "utilizador.xsd") &&
				XMLDoc.validDocXSD("peça.xml", "peça.xsd"))loja.menuAcesso();
	
	}
	
	/** Falta fazer a parte do sign in e do registar as pessoas
	 * 
	 */
	
	
	
	
	
	
	

}
