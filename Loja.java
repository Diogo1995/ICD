import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class Loja {
	
	public final static String contexto = "";
	private static Document Utilizadores;
	private Document Pecas;
	
	public Loja() {
		this.Utilizadores = ValidarXML("utilizador.xml"); //TODO alterar nomes de xmls?
		this.Pecas = ValidarXML("peça.xml");		
	}

	private static Document ValidarXML(String XMLdoc) {
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
		Node catalogo = Pecas.getDocumentElement().cloneNode(true);
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
				NodeList pecasH = getNodesByTag(catalogo, "Secção", "Homem");
				menuEquipamentos(sc, nif, pecasH, "Homem");
				break;
				
			case '2':
				NodeList pecasM = getNodesByTag(catalogo, "Secção", "Mulher");
				menuEquipamentos(sc, nif, pecasM, "Mulher");
				break;
				
			case '3':
				NodeList pecasC = getNodesByTag(catalogo, "Secção", "Criança");
				menuEquipamentos(sc, nif, pecasC, "Criança");
				break;
				
			case '4':
				NodeList pecasA = getNodesByTag(catalogo, "Secção", "Acessorio");
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
	
	public static Document getUtilizadores() {
		return ValidarXML("utilizador.xml");
	}
	
	public static Document getPecas() {
		return ValidarXML("peça.xml");
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
	private NodeList getNodesByTag(Node root, String tag, String tagValor) {
		NodeList nl = root.getChildNodes();
		System.out.println(root.getChildNodes().getLength());
		for(int i = 0; i < nl.getLength(); i++) {
			if(nl.item(i).getNodeType() == Node.ELEMENT_NODE && !nl.item(i).getAttributes().getNamedItem(tag).getTextContent().equals(tagValor)) {
				root.removeChild(nl.item(i));
			}
		}
		System.out.println(root.getChildNodes().getLength());

		return root.getChildNodes();
	}
	
	/**Recebe o input do utilizador e verifica se é um número e se este é uma das escolhas
	 * possiveis do menu
	 * 
	 * @param sc
	 * @param numPecas
	 * @return
	 */
	private String apenasNumeros(Scanner sc, int numPecas) {//TODO alterar nome
		String input = sc.nextLine();
		boolean nums = false;
		
		while (!nums) {
			for(int i = 0; i < input.length(); i++) {
				if (!Character.isDigit(input.charAt(i))) {
					System.out.println("\nIntroduza apenas números!\n");
					nums = false;
					break;
				}else nums = true;
			}if (!nums) continue;
			
			int valorIn = Integer.parseInt(input);
			if (valorIn < 0 || valorIn > numPecas) {
				nums = false;
				System.out.println("\nIntroduza um valor entre 0 e " + numPecas + "\n");
				input = sc.nextLine();
			}
		}
		return input;
	}

	
	/**
	 * Menu Equipamentos (Vestuario/Calcado)
	 * 
	 * @param sc
	 * @param nif
	 * @param nl
	 */
	public void menuEquipamentos(Scanner sc, String nif, NodeList pecas, String seccao) {
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
				menuPecas(sc, nif, pecas, "Vestuário", seccao);
				break;
				
			case '2':
				menuPecas(sc, nif, pecas, "Calçado", seccao);
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
	
	
	public void menuPecas(Scanner sc, String nif, NodeList pecas, String tipo, String seccao) {
		boolean escolhaExiste = false; //TODO alterar forma de fazer isto ou alterar nome da variavel
		
		while(!escolhaExiste) {
			System.out.println();
			System.out.println();
			System.out.println("*** " + tipo + " ***");
			System.out.println("");
			
			int numPecas = 0;
			for(int i = 0; i < pecas.getLength(); i++) {
				if(pecas.item(i).hasAttributes() && pecas.item(i).getChildNodes().item(5).getNodeName().equals(tipo))
				System.out.println(++numPecas + " - " + pecas.item(i).getAttributes().getNamedItem("Designação").getTextContent());
			}
			
			if (numPecas == 0) {
				System.out.println("Não existem peças de " + tipo + " disponíveis na secção de " + seccao + "! Voltando ao menú anterior...");
				return;
			}
			
			
			//Handle do input	
			System.out.println();
			System.out.println("Escreva o número da peça para ver a sua descrição. Ou escreva \"0\" para voltar ao menú anterior.");
			String input = apenasNumeros(sc, numPecas);
		

			if (input.equals("0")) {
				return;
			}
			
			int indexAux = 0;
			for(int i = 0; i < pecas.getLength(); i++) {
				if(pecas.item(i).hasAttributes() && pecas.item(i).getChildNodes().item(5).getNodeName().equals(tipo)) {
					indexAux++;
					if (input.equals(Integer.toString(indexAux))) {
						escolhaExiste = true;
						//mostrarPeca(pecas.item(i), nif, tipo);
						break;
						//TODO mostrar descrição do item e fazer um novo menu para comprar um tamanho ou voltar
					}
				}
			}
		}
	}
	
	
	/*public void mostrarPeca(Node peca, String nif, String tipo) {
		String descricao = peca.getChildNodes().item(1).getFirstChild().getNodeValue();
		if(tipo.equals("Acessorio")) {
			String quantidade = peca.getChildNodes().item(2).getFirstChild().getNodeValue();
			System.out.println("\nDescrição do item:\n" + descricao);
			System.out.println("\nQuantidade disponível: " + quantidade);
		}else {
			NodeList tamanhos = peca.getChildNodes().item(2).getChildNodes();
			String tamanho;
			String quantidade;
			System.out.println("\nDescrição do item:\n" + descricao);
			System.out.println("\nTamanhos e quantidades disponíveis:\n");
			
			for(int i = 0; i < tamanhos.getLength(); i++) {
				tamanho = tamanhos.item(i).getAttributes().getNamedItem("Valor").getTextContent();
				quantidade = tamanhos.item(i).getAttributes().getNamedItem("Quantidade").getTextContent();
				System.out.println(" -> " + tamanho + ": " + quantidade + "\n");
			}
		}
	}*/
	
	
	
	private static void clienteTCP() {
		String host = "10.10.7.219";  // Máquina onde reside a aplicação servidora
        int    port = 5025;      // Porto da aplicação servidora

        /*
        if (args.length > 0) {
            host = args[0];
        }
        
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
                if (port < 1 || port > 65535) port = DEFAULT_PORT;
            }
            catch (NumberFormatException e) {
                System.err.println("Erro no porto indicado");
            }
        }
        */
        
        System.out.println("-> " + host + ":" + port );
        
        
        Socket socket     = null;
        BufferedReader is = null;
        PrintWriter    os = null;
        
        try {
            socket = new Socket(host, port);
            // Mostrar os parametros da ligação
            System.out.println("Ligação: " + socket);

            // Stream para escrita no socket
            os = new PrintWriter(socket.getOutputStream(), true); 

            // Stream para leitura do socket
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Escreve no socket
            os.println("Olá mundo!!!");

            // Mostrar o que se recebe do socket
            System.out.println("Recebi -> " + is.readLine()); 
            
            
        } 
        catch (IOException e) {
            System.err.println("Erro na ligação " + e.getMessage());   
        }
        finally {
            // No fim de tudo, fechar os streams e o socket
            try {
                if (os != null) os.close();
                if (is != null) is.close();
                if (socket != null ) socket.close();
            }
            catch (IOException e) { 
                // if an I/O error occurs when closing this socket
            }
        } // end finally
	}
	
	/**Ele comeca com a o login como cliente**/
	
	public static void main(String[] args) {
	
		Loja loja = new Loja();
		//clienteTCP();
		if (XMLDoc.validDocXSD("utilizador.xml", "utilizador.xsd") &&
				XMLDoc.validDocXSD("peça.xml", "peça.xsd"))loja.menuAcesso();
	
	}
}
