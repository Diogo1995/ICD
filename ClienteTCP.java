
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
	
	private static Node utilizador;

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
				NodeList teste = PecasTotal(sock);
				String nif = menuLogin(sc);
				Login(sock, nif);
				if(utilizador != null) {
					String tipoUtilizador = utilizador.getChildNodes().item(0).getNodeName();
					if(tipoUtilizador.equals("Cliente")){
						menuCliente(sock, sc, nif);
					}else if(utilizador.getChildNodes().item(0).getAttributes().getNamedItem("Local").getTextContent().equals("Loja")) menuFuncionarioLoja(sock, sc, nif);
					else if(utilizador.getChildNodes().item(0).getAttributes().getNamedItem("Local").getTextContent().equals("Caixa")) menuFuncionarioCaixa(sock, sc, nif);
					
				}
				else  {
					System.out.println("NIF inexistente no sistema.");
				}
				

				break;
			case '2':
				menuRegistar(sock, sc);
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
	
	private static boolean menuRegistar(Socket sock, Scanner sc) {
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
		return Registar(sock, nif, nome, dataNasc);
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
				//NodeList pecasH = getNodesByTag(catalogo, "Secção", "Homem");
				menuEquipamentos(sock, sc, nif, pecasHSocket, "Homem");
				break;
				
			case '2':
				NodeList pecasMSocket = Catalogo(sock, "Mulher");
				//NodeList pecasM = getNodesByTag(catalogo, "Secção", "Mulher");
				menuEquipamentos(sock, sc, nif, pecasMSocket, "Mulher");
				break;
				
			case '3':
				NodeList pecasCSocket = Catalogo(sock, "Criança");
				//NodeList pecasC = getNodesByTag(catalogo, "Secção", "Criança");
				menuEquipamentos(sock, sc, nif, pecasCSocket, "Criança");
				break;
				
			case '4':
				NodeList pecasASocket = Catalogo(sock, "Acessórios");
				//NodeList pecasA = getNodesByTag(catalogo, "Secção", "Acessorio");
				menuPecas(sock,sc,nif,pecasASocket,"Acessórios","Acessórios");
				
				//TODO
				break;
				
			case '5':
				//TODO
				NodeList pecasCarrinho = Carrinho(sock);
				menuCarrinho(sock,sc,nif,pecasCarrinho);
				break;
				
			
			case '6':
				utilizador = null;
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
				NodeList carrinhos = mostrarTodosCarrinhos(sock);
				
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
						mostrarPeca(sock, sc, pecas.item(i), nif, tipo);
						//TODO mostrar descrição do item e fazer um novo menu para comprar um tamanho ou voltar
					}
				}
			}
			
		}
	}
	
	public static void menuCarrinho(Socket sock, Scanner sc,String nif,NodeList pecasCarrinho) {
		char op;
		
		if(!utilizador.getFirstChild().hasChildNodes()){
			System.out.println("##########################");
			System.out.println();
			System.out.println("Não Tem Peças no Carrinho");
			System.out.println();
			System.out.println("##########################");
			return;
		}
		
		NodeList n = utilizador.getFirstChild().getChildNodes();
		
		
		//System.out.println(n.item(0).getChildNodes().item(0).getAttributes().getNamedItem("ID"));
		
		
		
		
		
		
		do {
			int numPecas =0;
			for(int i = 0; i < utilizador.getFirstChild().getChildNodes().item(0).getChildNodes().getLength(); i++) {
				System.out.println(++numPecas + " - " + utilizador.getFirstChild().getChildNodes().item(0).getChildNodes().item(i).getAttributes().getNamedItem("ID"));
			}
			System.out.println();
			System.out.println();
			System.out.println("*** Caso queira retirar uma peça escolha o número da peça a retirar ***");
			System.out.println("! - Voltar.");
			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.charAt(0);
			else
				op = ' ';
			switch (op) {
			
			
			case '!':
				return;
				
			case '0':
				break;
			default:
				System.out.println("Opção inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.exit(0);
	}
		//TODO Não está finalizado
		
	
	
	private static String apenasNumeros(Scanner sc, int numPecas) {//TODO alterar nome
		String input = sc.nextLine();
		boolean nums = false;
		
		while (!nums) {
			for(int i = 0; i < input.length(); i++) {
				if (!Character.isDigit(input.charAt(i))) {
					System.out.println("\nIntroduza apenas números! Valor entre 0 e " + numPecas + "\n");
					input = sc.nextLine();
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

	private static void Login(Socket sock, String nif) {
		comando cmd = new comando();
		Document request = cmd.requestLogin(nif);
		// envia pedido
		XMLReadWrite.documentToSocket(request, sock);
		// obtém resposta
		Document reply = XMLReadWrite.documentFromSocket(sock);
		NodeList utilizadores = reply.getElementsByTagName("Utilizador");
		
		Node item = utilizadores.item(0);
		utilizador = item;
	}
	
	private static NodeList Catalogo(Socket sock, String seccao) {
		comando cmd = new comando();
		Document request = cmd.requestCatalogo(seccao);
		//XMLDoc.writeDocument(request, "request.xml");
		//envia pedido
		XMLReadWrite.documentToSocket(request, sock);
		//obtém resposta
		Document reply = XMLReadWrite.documentFromSocket(sock);
		//XMLDoc.writeDocument(reply, "reply.xml");
		return reply.getElementsByTagName("Peça");
	}
	
	private static NodeList Carrinho(Socket sock){
		comando cmd = new comando();
		Document request = cmd.requestCarrinhos();
		//XMLDoc.writeDocument(request, "request.xml");
		//envia pedido
		XMLReadWrite.documentToSocket(request, sock);
		//obtém resposta
		Document reply = XMLReadWrite.documentFromSocket(sock);
		//XMLDoc.writeDocument(reply, "reply.xml");
		return reply.getElementsByTagName("Carrinho");
	}
	
	private static boolean Registar(Socket sock, String nif, String nome, String dataNasc) {
		comando cmd = new comando();
		Document request = cmd.requestRegistar(nif, nome, dataNasc);
		
		XMLReadWrite.documentToSocket(request, sock);
		
		Document reply = XMLReadWrite.documentFromSocket(sock);
		
		if(reply.getElementsByTagName("Utilizador").item(0) != null) {
			utilizador = reply.getElementsByTagName("Utilizador").item(0);
			return true;
		}
		return false;
	}
	
<<<<<<< HEAD
<<<<<<< HEAD
	private static NodeList AdicionarPeca(Socket sock, String designacao, String seccao, String preco, String tipo) {
		comando cmd = new comando();
		
		Document request = cmd.requestAdicionarPeca(designacao, seccao, preco, tipo);
		
		XMLReadWrite.documentToSocket(request, sock);
		
		Document reply = XMLReadWrite.documentFromSocket(sock);
		
		NodeList pecas = reply.getElementsByTagName("Peça");
		return pecas;
	}
	
=======
>>>>>>> parent of 9c4bf49... "Final" de TP1
	private static NodeList PecasTotal(Socket sock) {
		//TODO
		comando cmd = new comando();
		Document request = cmd.requestPecasTotal();
		
		XMLDoc.writeDocument(request, "requestPecas.xml");
		
		XMLReadWrite.documentToSocket(request, sock);
		
		Document reply = XMLReadWrite.documentFromSocket(sock);
		
		XMLDoc.writeDocument(reply, "replyPecas.xml");
		return null;
	}
	
	
<<<<<<< HEAD
	private static Node AdicionarCarrinho(Socket sock, int idPeca, int quantidade, String tamanho) {
		comando cmd = new comando();
		
		Document request = cmd.requestAdicionarCarrinho(utilizador.getAttributes().getNamedItem("NIF").getTextContent(), idPeca, tamanho, quantidade);
		
		XMLReadWrite.documentToSocket(request, sock);
				
		Document reply = XMLReadWrite.documentFromSocket(sock);
				
		Node carrinho = reply.getElementsByTagName("Carrinho").item(0);
	
		return carrinho;
	}
	
	private static NodeList AprovarCarrinho(Socket sock, String nif) {
		comando cmd = new comando();
		
		Document request = cmd.requestAprovarCarrinho(nif);
		
		XMLReadWrite.documentToSocket(request, sock);
				
		Document reply = XMLReadWrite.documentFromSocket(sock);
				
		NodeList carrinhos = reply.getElementsByTagName("Carrinho");
		
		return carrinhos;
	}
	
	private static NodeList mostrarTodosCarrinhos(Socket sock) {
		comando cmd = new comando();
		
		Document request = cmd.requestTodosCarrinhos();
		
		XMLReadWrite.documentToSocket(request, sock);
		
		Document reply = XMLReadWrite.documentFromSocket(sock);
		
		NodeList carrinhos = reply.getElementsByTagName("Carrinho");
		
		return carrinhos;
	}
	
	public static int quantidadePretendida(Scanner sc, int quantMax) {
		System.out.println("\nIntroduza a quantidade de itens pretendida (min 0 e máx " + quantMax + ")");
		int input = Integer.parseInt(apenasNumeros(sc, quantMax));
		
		return input;
	}
	
	
	public static void mostrarPeca(Socket sock, Scanner sc, Node peca, String nif, String tipo) {
=======
=======
>>>>>>> parent of 9c4bf49... "Final" de TP1
	public static void mostrarPeca(Scanner sc, Node peca, String nif, String tipo) {
>>>>>>> 9c4bf49cf82142d63e3059a6fe7b12bada33e795
		Element pecaElement = (Element) peca.getChildNodes();
		Element descricao = (Element) pecaElement.getElementsByTagName("Descrição").item(0).getChildNodes();
		String caracteristicas = descricao.getElementsByTagName("Caracteristica").item(0).getTextContent();
		
		System.out.println("\nDescrição do item:\n" + caracteristicas);
		
		if(tipo.equals("Acessórios")) {			
			Element AcessoriosElement = (Element) pecaElement.getElementsByTagName("Acessórios").item(0).getChildNodes();
			String quantidade = AcessoriosElement.getElementsByTagName("Quantidade").item(0).getAttributes().getNamedItem("Quantidade").getTextContent();
			System.out.println("\nQuantidade disponível: " + quantidade + " em stock");
			
			System.out.println("\nIntroduza 1 para adicionar este item ao seu carrinho ou 0 para voltar ao menu anterior.");
			String input = apenasNumeros(sc, 1);
			if(input.equals("0")) return;
			else {
				int idPeca = Integer.parseInt(peca.getAttributes().getNamedItem("idPeça").getTextContent());
				int quantidadePecas = quantidadePretendida(sc, Integer.parseInt(quantidade));
				
				AdicionarCarrinho(sock, idPeca, quantidadePecas, "");
			}
			
		}else {
			NodeList tamanhos;
			
			if(tipo.equals("Vestuário")) {
				Element VestuárioElement = (Element) pecaElement.getElementsByTagName("Vestuário").item(0).getChildNodes();
				tamanhos = VestuárioElement.getElementsByTagName("Tamanho");
			}else {
				Element VestuárioElement = (Element) pecaElement.getElementsByTagName("Calçado").item(0).getChildNodes();
				tamanhos = VestuárioElement.getElementsByTagName("Tamanho");
			}
			
			String tamanho;
			String quantidade;
			
			System.out.println("\nTamanhos e quantidades disponíveis:\n");
			
			for(int i = 0; i < tamanhos.getLength(); i++) {
				tamanho = tamanhos.item(i).getAttributes().getNamedItem("Valor").getTextContent();
				quantidade = tamanhos.item(i).getAttributes().getNamedItem("Quantidade").getTextContent();
				System.out.println(1+i + " -> tamanho " + tamanho + ": " + quantidade + " em stock");
			}
			
			System.out.println("\nIntroduza o número do item que deseja adicionar ao seu carrinho ou 0 para voltar ao menu anterior.");
			String input = apenasNumeros(sc, tamanhos.getLength());
			if(input.equals("0")) return;
			else {
				int idPeca = Integer.parseInt(peca.getAttributes().getNamedItem("idPeça").getTextContent());
				int quantidadePecas = quantidadePretendida(sc, Integer.parseInt(tamanhos.item(Integer.parseInt(input)-1).getAttributes().getNamedItem("Quantidade").getTextContent()));
				String tamanhoPretendido = tamanhos.item(Integer.parseInt(input)-1).getAttributes().getNamedItem("Valor").getTextContent();
				
				AdicionarCarrinho(sock, idPeca, quantidadePecas, tamanhoPretendido);
			}
		}
	}
	
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



