
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author PFilipe
 *
 */
public class ClienteTCP {

	public final static String DEFAULT_HOSTNAME = "localhost";

	public final static int DEFAULT_PORT = 5025;

	/**
	 * @param sock
	 */
	public static void menu(Socket sock) {
		char op;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println();
			System.out.println();
			System.out.println("*** Login Loja ***");
			System.out
					.println("1 - Login");
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
				if(Login(sock, nif).equals("Cliente")){
					menuCliente(sock, sc, nif);
				}else if(Login(sock, nif).equals("Loja")) menuFuncionarioLoja(sock, sc, nif);
				else if(Login(sock, nif).equals("Caixa")) menuFuncionarioCaixa(sock, sc, nif);
				
				//Login(sock);
				break;
			case '2':
				System.out
						.println("Obter um poema que inclua um dado conjunto de palavras");
				System.out.println("Indique as palavras: ");
				ArrayList<String> palList = new ArrayList<String>();
				String pv = "";
				do {
					pv = sc.nextLine();
					if (pv.compareTo("") == 0)
						break;
					palList.add(pv);
					System.out
							.println("Indique outra palavra ou <enter> para terminar:");
				} while (true);
				String[] palArray = palList.toArray(new String[palList.size()]);
				//Obter(sock, palArray);
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
	
	public static void menuCliente(Socket sock, Scanner sc, String nif) {
		char op;
		Node catalogo = Loja.getPecas().getDocumentElement().cloneNode(true);
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
				NodeList pecasHSocket = Catalogo(sock, "Homem");
				NodeList pecasH = getNodesByTag(catalogo, "Secção", "Homem");
				menuEquipamentos(sock, sc, nif, pecasH, "Homem");
				break;
				
			case '2':
				NodeList pecasM = getNodesByTag(catalogo, "Secção", "Mulher");
				menuEquipamentos(sock, sc, nif, pecasM, "Mulher");
				break;
				
			case '3':
				NodeList pecasC = getNodesByTag(catalogo, "Secção", "Criança");
				menuEquipamentos(sock, sc, nif, pecasC, "Criança");
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
				menu(sock);
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
	
	
	public static void menuFuncionarioCaixa(Socket sock, Scanner sc, String nif) {
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
				menu(sock);
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
	
	public static void menuFuncionarioLoja(Socket sock, Scanner sc, String nif) {
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
				menu(sock);
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
	
	public static void menuEquipamentos(Socket sock, Scanner sc, String nif, NodeList pecas, String seccao) {
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
				menuPecas(sock, sc, nif, pecas, "Vestuário", seccao);
				break;
				
			case '2':
				menuPecas(sock, sc, nif, pecas, "Calçado", seccao);
				break;
				
			case '3':
				menuCliente(sock, sc, nif);
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
	
	public static void menuPecas(Socket sock, Scanner sc, String nif, NodeList pecas, String tipo, String seccao) {
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
						
						//TODO mostrar descrição do item e fazer um novo menu para comprar um tamanho ou voltar
					}
				}
			}
			
		}
	}
	
	private static String apenasNumeros(Scanner sc, int numPecas) {//TODO alterar nome
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
	
	private static NodeList getNodesByTag(Node root, String tag, String tagValor) {
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
	
	private static String menuLogin(Scanner sc) {
		System.out.println("Introduza o seu NIF.");
		String nif = sc.nextLine();
		
		while(!validarNif(nif)) {
			if (nif.equals("0")) break;
			System.out.println("NIF introduzido inválido. Introduza novamente o NIF.");
			nif = sc.nextLine();
		}
		
		return nif;
	}

	private static boolean validarNif(String nif) {
		if (nif.length() == 9) {
			for (int i = 0; i < nif.length(); i++) {
				if(!Character.isDigit(nif.charAt(i))) return false;
			}
			return true;
		}
		return false;
	}

	private static String Login(Socket sock, String nif) {
		comando cmd = new comando();
		Document request = cmd.requestLogin(nif);
		// envia pedido
		XMLReadWrite.documentToSocket(request, sock);
		// obtém resposta
		Document reply = XMLReadWrite.documentFromSocket(sock);
		NodeList utilizadores = reply.getElementsByTagName("Utilizador");
	
		Node item = utilizadores.item(0);
		return item.getChildNodes().item(0).getNodeName();
	}
	
	private static NodeList Catalogo(Socket sock, String seccao) {
		//TODO
		comando cmd = new comando();
		Document request = cmd.requestCatalogo(seccao);
		XMLDoc.writeDocument(request, "request.xml");
		//envia pedido
		XMLReadWrite.documentToSocket(request, sock);
		//obtém resposta
		Document reply = XMLReadWrite.documentFromSocket(sock);
		XMLDoc.writeDocument(reply, "reply.xml");
		return null;
	}
	
	/**
	 * Envia e recebe a o comando associado ao serviço Obter
	 * @param sock
	 */
	/*
	private static void Obter(Socket sock, String[] palavras) {

		comando cmd = new comando();
		Document request = cmd.requestObter(palavras);

		// envia pedido
		XMLReadWrite.documentToSocket(request, sock);
		// obtém resposta
		Document reply = XMLReadWrite.documentFromSocket(sock);

		NodeList L = reply.getElementsByTagName("poema");
		if (L.getLength() == 0)
			System.out
					.println("\nNão existe nenhum poema com todas as palavras indicadas!");
		else {
			System.out.println("\nPoema:");
			Element pm = (Element) reply.getElementsByTagName("poema").item(0);
			//Loja resposta = new Loja(pm);
			//resposta.apresenta();
		}
	}
	*/
	public static void main(String[] args) {

		String host = DEFAULT_HOSTNAME; // Máquina onde reside a aplicação
										// servidora
		int port = DEFAULT_PORT; // Porto da aplicação servidora

		if (args.length > 0) {
			host = args[0];
		}

		if (args.length > 1) {
			try {
				port = Integer.parseInt(args[1]);
				if (port < 1 || port > 65535)
					port = DEFAULT_PORT;
			} catch (NumberFormatException e) {
				System.err.println("Erro no porto indicado");
			}
		}

		System.out.println("-> " + host + ":" + port);

		Socket socket = null;

		try {
			socket = new Socket(host, port);
			
			if (XMLDoc.validDocXSD("utilizador.xml", "utilizador.xsd") &&
					XMLDoc.validDocXSD("peça.xml", "peça.xsd"))menu(socket);
		} catch (Exception e) {
			System.err.println("Erro na ligação " + e.getMessage());
		} finally {
			// No fim de tudo, fechar os streams e o socket
			try {
				// if (os != null) os.close();
				// if (is != null) is.close();
				if (socket != null)
					socket.close();
			} catch (Exception e) {
				// if an I/O error occurs when closing this socket
				System.err
						.println("Erro no fecho da ligação:" + e.getMessage());
			}
		} // end finally

	} // end main

} // end ClienteTCP



