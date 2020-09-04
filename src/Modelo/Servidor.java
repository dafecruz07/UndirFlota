package Modelo;
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import Controlador.ModeloTabla;
import Vista.VentanaServidor;

/**
 * Clase: Servidor
 * Clase encargada de gestionar lo relacionado con el servidor envio recepcion de mensajes y gestion de clientes
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
public class Servidor {
	
	private int uniqueId; 								// El ID unico para cada conexion
	private ArrayList<HiloCliente> arrayUsuarios; 		// Arreglo que contendra los usuarios que estan en juego
	private static VentanaServidor ventanaServidor; 	// Interfaz ventana
	private SimpleDateFormat simpleDateFormat; 			// Variable usada para imprimir la hora de conexion de ususarios
	private int port;		 							// El puerto por el cual se realizara la comunicacion
	private boolean keepGoing; 							// Booleano que determina cuando debe correr el servidor
	private ArrayList<String> nombres; 					// Arreglo que cientiene los nombre de los dos jugadores
	private int turno; 									// Variable usada para identificar el turno
	private int modalidad; 								// Variable usada para determinar la modalidad de juego 1 = Jugador vs Jugador / 2 = Jugardor vs Maquina
	private int tablerosListos; 						// Variable usada pra determinar cuantos tableros se han enviado
	private int barcosHundidos1, barcosHundidos2; 		// Variables usadas para determinar el ganador de la partida
	private ClienteMaquina pc;							// Objeto usado para la modalidad Jugador vs Pc
	private boolean desempate;							// Variable usada para terminar de forma definitiva una partida
	private int puntaje;
	
	/**
	 * Metodo constructor
	 * @param ventana: La interfaz del servidor
	 */
	@SuppressWarnings("static-access")
	public Servidor(VentanaServidor ventana) {
		
		this.ventanaServidor = ventana; 						// Inicializamos GUI del servidor
		uniqueId = 1;											// Inicializamos el id
		port = 1234;											// El puerto
		simpleDateFormat = new SimpleDateFormat("HH:mm:ss");	// Hora para imprimir
		arrayUsuarios = new ArrayList<HiloCliente>();			// ArrayList para los hilos clientes
		ventanaServidor.setVisible(true);						// Hacemos visible la GUI del servidor
		nombres = new ArrayList<String>();						// Inicializamos el arreglo de nombres de tamanio 2 para los nombres de los usuarios
		tablerosListos = 0;										// Inicializamos el entero de tableros con un valor de 0, el cual se incrementara cuando los jugadores envien sus respectivos tableros
		turno = 0;												// Inicializamos la variable de turnos
		barcosHundidos1 = barcosHundidos2 = 0;					// Inicializamos en 0 las variables que determinan cuantos barcos han sido hundidos por cada jugador
		determinarGanador();									// Inicializamos el metodo que verifica el ganador de la partida
	}
	
	/**
	 * Metodo que determina el ganador de la partida
	 */
	public void determinarGanador(){
		desempate=false;
		puntaje = 10000;
		
		Thread ganador = new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					puntaje--;
					
					if(barcosHundidos2 == 5 && barcosHundidos1 == 5){	// Empate
						//Creamos un mensaje con la informacion necesaria
						Mensaje mens = new Mensaje(nombres.get(0));
						mens.setTipo(4);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (modalidad==1) {
							mens.setMensaje("¡Draw!");
							arrayUsuarios.get(1).enviarInfo(null, mens);
							arrayUsuarios.get(0).enviarInfo(null, mens);
						}else{
							mens.setMensaje("¡Draw!");
							arrayUsuarios.get(0).enviarInfo(null, mens);
							mens.setTipo(8);
							arrayUsuarios.get(1).enviarInfo(null, mens);
						}	
						
						tablerosListos = 0;
						turno = 0;
						barcosHundidos1 = barcosHundidos2 = 0;
						ventanaServidor.controlServer.reiniciarDatos();
						
						break;
					}
					
					if(barcosHundidos1 == 5 && turno == 0){	// Gano jugador 0
						if (desempate) {
							
							String comando = "INSERT INTO `undir_flota`.`puntaje` (`id`, `usuario`, `puntaje`) VALUES (NULL, '" + nombres.get(0) + "', '" + puntaje + "');";
							Conexion con = new Conexion();
							boolean error = con.conectarMySQL("undir_flota","root", "", "127.0.0.1");
							if(!error){
								con.actualizar(comando);
							}
							//Creamos un mensaje con la informacion necesaria
							Mensaje mens = new Mensaje(nombres.get(0));
							mens.setTipo(4);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (modalidad==1) {
								mens.setMensaje("¡You win this match!");
								arrayUsuarios.get(0).enviarInfo(null, mens);
								mens.setMensaje("¡You lose this match!");
								arrayUsuarios.get(1).enviarInfo(null, mens);
							}else{
								mens.setMensaje("¡You win this match!");
								arrayUsuarios.get(0).enviarInfo(null, mens);
								mens.setTipo(8);
								arrayUsuarios.get(1).enviarInfo(null, mens);
							}
							
							tablerosListos = 0;
							turno = 0;
							
							barcosHundidos1 = barcosHundidos2 = 0;
							ventanaServidor.controlServer.reiniciarDatos();
							break;
						} else{
							desempate=!desempate;
							turno=1;
							
							if (modalidad==2) 
								pc.getVentana().getCc().realizarDisparosMaquina();
							
							Mensaje mensaj = new Mensaje(null);
							mensaj.setTipo(5);
							mensaj.setUsuarioTurno(nombres.get(turno));
							
							arrayUsuarios.get(0).enviarInfo(null, mensaj);
							arrayUsuarios.get(1).enviarInfo(null, mensaj);
						}
					}
					if(barcosHundidos2 == 5 && turno == 1){ // Gana jugador 1
						
						String comando = "INSERT INTO `undir_flota`.`puntaje` (`id`, `usuario`, `puntaje`) VALUES (NULL, '" + nombres.get(0) + "', '" + puntaje + "');";
						Conexion con = new Conexion();
						boolean error = con.conectarMySQL("undir_flota","root", "", "127.0.0.1");
						if(!error){
							con.actualizar(comando);
						}
						
						Mensaje mens = new Mensaje(nombres.get(0));		//Creamos un mensaje con la informacion necesaria
						mens.setTipo(4);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (modalidad==1) {
							mens.setMensaje("¡You win this match!");
							arrayUsuarios.get(1).enviarInfo(null, mens);
							mens.setMensaje("¡You lose this match!");
							arrayUsuarios.get(0).enviarInfo(null, mens);
						}else{								
							mens.setMensaje("¡You lose this match!");
							arrayUsuarios.get(0).enviarInfo(null, mens);
							mens.setTipo(8);
							arrayUsuarios.get(1).enviarInfo(null, mens);
						}
						
						tablerosListos = 0;
						turno = 0;
						
						Mensaje mensaj = new Mensaje(null);
						mensaj.setTipo(5);
						mensaj.setUsuarioTurno(nombres.get(turno));
						arrayUsuarios.get(0).enviarInfo(null, mensaj);
						arrayUsuarios.get(1).enviarInfo(null, mensaj);
						
						barcosHundidos1 = barcosHundidos2 = 0;
						ventanaServidor.controlServer.reiniciarDatos();
						
						break;
					}
				}
				determinarGanador();
			}
		});
		ganador.start();
	}
	
	public ModeloTabla puntaje () {
		String encab[] = {"User", "Score"};
		Object datos[][] = {};
		ModeloTabla modelo = new ModeloTabla(datos, encab);
		Conexion con = new Conexion();
		
		boolean error = con.conectarMySQL("undir_flota","root", "", "127.0.0.1");
		if(!error){
		    String comando = "SELECT * FROM `puntaje` ORDER BY `puntaje`.`puntaje` DESC";
			ResultSet rs = con.consulta(comando);
			
			@SuppressWarnings("unused")
			int cant = con.getSizeQuery(rs);
			
			try{
				Object campos[] = new Object[2];
				while(rs.next()){
					campos[0] = rs.getString(2);
					campos[1] = rs.getString(3);
					modelo.addRow(campos);
				}
			}catch(SQLException sqle){
				JOptionPane.showMessageDialog(ventanaServidor, 
						"Error al tratar de obtener la información.",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
			con.cerrarConsulta();
			con.desconectar();
		}
		return modelo;
	}
	
	/**
	 *  Metodo usado para enviar mensajes a todos los usuarios
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public synchronized void broadcast(String message, Mensaje mens) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		String time = simpleDateFormat.format(new Date()); // Agregamos HH:mm:ss al mensaje
		String messageLf = time + " " + message;
		
		if (mens.getTipo()==7) {
			
			ModeloTabla modelo = puntaje();
			mens.setModelo(modelo);
			if (mens.getUsuario().equals(nombres.get(0))) {
				arrayUsuarios.get(0).enviarInfo(null, mens);
			}else{
				arrayUsuarios.get(1).enviarInfo(null, mens);
			}
		}
				
		if (mens.getTipo()==6) {
			modalidad=mens.getModalidad();
			if (modalidad==2) {
				pc = new ClienteMaquina("localhost", port, "Pc");
				pc.start();
				pc.getVentana().getCc().tableroMaquina();
			}
			if (modalidad == 1) {
				// Mensaje deshabilitacion
				arrayUsuarios.get(0).enviarInfo(null, mens);
				arrayUsuarios.get(1).enviarInfo(null, mens);
			}
		}
		if (nombres.get(0)!=null) { // Recepcion de tableros
			if (mens!=null && nombres.get(0).equals(mens.getUsuario()) && mens.getTipo()==2) { // Tablero Usuario 1
				// Imprimimos el registro de llegada en el servidor
				ventanaServidor.imprimirMensaje(messageLf);
				ventanaServidor.controlServer.setFlota1(mens.getFlota());
				ventanaServidor.controlServer.pintarBarcos();
				ventanaServidor.controlServer.actualizarTableros();
				tablerosListos++;
			}
		}
		if (nombres.size() > 1) { // recepcion de tablero
			if (mens!=null && nombres.get(1).equals(mens.getUsuario()) && mens.getTipo()==2) { // Tablero Usuario 2
				// Imprimimos el registro de llegada en el servidor
				ventanaServidor.imprimirMensaje(messageLf);
				ventanaServidor.controlServer.setFlota2(mens.getFlota());
				ventanaServidor.controlServer.pintarBarcos();
				ventanaServidor.controlServer.actualizarTableros();
				tablerosListos++;
			}
		}
		
		/*
		 * Se verifica si la flota fue hundida para enviar la respectiva propiedad a los usuarios
		 * Se usa la varibale posFlota1 o posFlota2 dado el caso
		 * turno 0 = flota 2
		 * turno 1 = flota 1
		 */
		if (nombres.size() == 2 && tablerosListos == 2 && mens.getTipo() == 3 && modalidad == 1) { // recepcion de disparos usuario usuario
			if (nombres.get(turno).equals(mens.getUsuario())) {
				if (ventanaServidor.controlServer.puntoContenido(mens.getCoord().getX(), mens.getCoord().getY(), turno)) {
					
					ventanaServidor.imprimirMensaje(mens.getUsuario() +" " + mens.getCoord().toString() + " Successful shot");
					ventanaServidor.controlServer.cambiarPropiedad(mens.getCoord().getX(), mens.getCoord().getY(), turno, 2);
					ventanaServidor.controlServer.actualizarTableros();
					mens.setPropiedad(2);
					
					arrayUsuarios.get(0).enviarInfo(null, mens);
					arrayUsuarios.get(1).enviarInfo(null, mens);
					
					if (turno==0) {
						if (ventanaServidor.controlServer.getFlota2()[ventanaServidor.controlServer.getPosFlota2()].flotaHundida()) {
							barcosHundidos1++;
							for (int i = 0; i < ventanaServidor.controlServer.getFlota2()[ventanaServidor.controlServer.getPosFlota2()].getPosiciones().length ; i++) {
								
								int x = ventanaServidor.controlServer.getFlota2()[ventanaServidor.controlServer.getPosFlota2()].getPosiciones()[i].getX();
								int y = ventanaServidor.controlServer.getFlota2()[ventanaServidor.controlServer.getPosFlota2()].getPosiciones()[i].getY();
								
								Mensaje mensa = new Mensaje(mens.getUsuario());
								mensa.setTipo(3);
								Coordenada coorde = new Coordenada(x, y);
								mensa.setCoord(coorde);
								mensa.setPropiedad(3);
								
								// Enviamos cada una de las posiciones del barco hundico
								arrayUsuarios.get(0).enviarInfo(null, mensa);
								arrayUsuarios.get(1).enviarInfo(null, mensa);
								
								ventanaServidor.controlServer.cambiarPropiedad(x, y, turno, 3);
								ventanaServidor.controlServer.actualizarTableros();
							}
							Mensaje msn = new Mensaje(mens.getUsuario(), "I have sunk the ship " + ventanaServidor.controlServer.getFlota2()[ventanaServidor.controlServer.getPosFlota2()].getNombre(), 1);
							
							arrayUsuarios.get(0).enviarInfo(null, msn);
							arrayUsuarios.get(1).enviarInfo(null, msn);
						}
					}else {
						if (ventanaServidor.controlServer.getFlota1()[ventanaServidor.controlServer.getPosFlota1()].flotaHundida()) {
							barcosHundidos2++;
							for (int i = 0; i < ventanaServidor.controlServer.getFlota1()[ventanaServidor.controlServer.getPosFlota1()].getPosiciones().length ; i++) {
								
								int x = ventanaServidor.controlServer.getFlota1()[ventanaServidor.controlServer.getPosFlota1()].getPosiciones()[i].getX();
								int y = ventanaServidor.controlServer.getFlota1()[ventanaServidor.controlServer.getPosFlota1()].getPosiciones()[i].getY();
								
								Mensaje mensa = new Mensaje(mens.getUsuario());
								mensa.setTipo(3);
								Coordenada coorde = new Coordenada(x, y);
								mensa.setCoord(coorde);
								mensa.setPropiedad(3);
								
								// Enviamos cada una de las posiciones del barco hundico
								arrayUsuarios.get(0).enviarInfo(null, mensa);
								arrayUsuarios.get(1).enviarInfo(null, mensa);
								ventanaServidor.controlServer.cambiarPropiedad(x, y, turno, 3);
								ventanaServidor.controlServer.actualizarTableros();
							}
							Mensaje msn = new Mensaje(mens.getUsuario(), "I have sunk the ship " + ventanaServidor.controlServer.getFlota1()[ventanaServidor.controlServer.getPosFlota1()].getNombre(), 1);

							
							
							arrayUsuarios.get(0).enviarInfo(null, msn);
							arrayUsuarios.get(1).enviarInfo(null, msn);
						}
					}
				}else{
					ventanaServidor.imprimirMensaje(mens.getUsuario() +" " + mens.getCoord().toString() + " Missed shot");
					ventanaServidor.controlServer.cambiarPropiedad(mens.getCoord().getX(), mens.getCoord().getY(), turno, 1);
					ventanaServidor.controlServer.actualizarTableros();
					mens.setPropiedad(1);
					
					arrayUsuarios.get(0).enviarInfo(null, mens);
					arrayUsuarios.get(1).enviarInfo(null, mens);
					if (turno==0){
						turno++;
						Mensaje mensaj = new Mensaje(null);
						mensaj.setTipo(5);
						mensaj.setUsuarioTurno(nombres.get(turno));
						
						arrayUsuarios.get(0).enviarInfo(null, mensaj);
						arrayUsuarios.get(1).enviarInfo(null, mensaj);
					}
					else{
						turno--;
						Mensaje mensaj = new Mensaje(null);
						mensaj.setTipo(5);
						mensaj.setUsuarioTurno(nombres.get(turno));
						
						arrayUsuarios.get(0).enviarInfo(null, mensaj);
						arrayUsuarios.get(1).enviarInfo(null, mensaj);
					}
				}
			}
		}
		
		if (nombres.size() == 2 && tablerosListos == 2 && mens.getTipo() == 3 && modalidad == 2) { // recepcion de disparos usuario Pc
			if (nombres.get(turno).equals(mens.getUsuario())) {
				if (ventanaServidor.controlServer.puntoContenido(mens.getCoord().getX(), mens.getCoord().getY(), turno)) {
					
					ventanaServidor.imprimirMensaje(mens.getUsuario() +" " + mens.getCoord().toString() + " Successful shot");
					ventanaServidor.controlServer.cambiarPropiedad(mens.getCoord().getX(), mens.getCoord().getY(), turno, 2);
					ventanaServidor.controlServer.actualizarTableros();
					mens.setPropiedad(2);
					
					arrayUsuarios.get(0).enviarInfo(null, mens);
					arrayUsuarios.get(1).enviarInfo(null, mens);
					
					if (turno==0) {
						if (ventanaServidor.controlServer.getFlota2()[ventanaServidor.controlServer.getPosFlota2()].flotaHundida()) {
							barcosHundidos1++;
							for (int i = 0; i < ventanaServidor.controlServer.getFlota2()[ventanaServidor.controlServer.getPosFlota2()].getPosiciones().length ; i++) {
								
								int x = ventanaServidor.controlServer.getFlota2()[ventanaServidor.controlServer.getPosFlota2()].getPosiciones()[i].getX();
								int y = ventanaServidor.controlServer.getFlota2()[ventanaServidor.controlServer.getPosFlota2()].getPosiciones()[i].getY();
								
								Mensaje mensa = new Mensaje(mens.getUsuario());
								mensa.setTipo(3);
								Coordenada coorde = new Coordenada(x, y);
								mensa.setCoord(coorde);
								mensa.setPropiedad(3);
								
								
								// Enviamos cada una de las posiciones del barco hundico
								arrayUsuarios.get(0).enviarInfo(null, mensa);
								arrayUsuarios.get(1).enviarInfo(null, mensa);
								
								ventanaServidor.controlServer.cambiarPropiedad(x, y, turno, 3);
								ventanaServidor.controlServer.actualizarTableros();
							}
							Mensaje msn = new Mensaje(mens.getUsuario(), "I have sunk the ship " + ventanaServidor.controlServer.getFlota2()[ventanaServidor.controlServer.getPosFlota2()].getNombre(), 1);
							
							arrayUsuarios.get(0).enviarInfo(null, msn);
							arrayUsuarios.get(1).enviarInfo(null, msn);
						}
					}else {
						if (ventanaServidor.controlServer.getFlota1()[ventanaServidor.controlServer.getPosFlota1()].flotaHundida()) {
							barcosHundidos2++;
							for (int i = 0; i < ventanaServidor.controlServer.getFlota1()[ventanaServidor.controlServer.getPosFlota1()].getPosiciones().length ; i++) {
								
								int x = ventanaServidor.controlServer.getFlota1()[ventanaServidor.controlServer.getPosFlota1()].getPosiciones()[i].getX();
								int y = ventanaServidor.controlServer.getFlota1()[ventanaServidor.controlServer.getPosFlota1()].getPosiciones()[i].getY();
								
								Mensaje mensa = new Mensaje(mens.getUsuario());
								mensa.setTipo(3);
								Coordenada coorde = new Coordenada(x, y);
								mensa.setCoord(coorde);
								mensa.setPropiedad(3);
								
								// Enviamos cada una de las posiciones del barco hundico
								arrayUsuarios.get(0).enviarInfo(null, mensa);
								arrayUsuarios.get(1).enviarInfo(null, mensa);
								
								ventanaServidor.controlServer.cambiarPropiedad(x, y, turno, 3);
								ventanaServidor.controlServer.actualizarTableros();
							}
							Mensaje msn = new Mensaje(mens.getUsuario(), "I have sunk the ship " + ventanaServidor.controlServer.getFlota1()[ventanaServidor.controlServer.getPosFlota1()].getNombre(), 1);
							
							arrayUsuarios.get(0).enviarInfo(null, msn);
							arrayUsuarios.get(1).enviarInfo(null, msn);
						}
					}
					if (turno==1)
					pc.getVentana().getCc().realizarDisparosMaquina();
				}else{
					ventanaServidor.imprimirMensaje(mens.getUsuario() +" " + mens.getCoord().toString() + " Missed shot");
					ventanaServidor.controlServer.cambiarPropiedad(mens.getCoord().getX(), mens.getCoord().getY(), turno, 1);
					ventanaServidor.controlServer.actualizarTableros();
					mens.setPropiedad(1);
					
					arrayUsuarios.get(0).enviarInfo(null, mens);
					arrayUsuarios.get(1).enviarInfo(null, mens);
					
					if (turno==0){
						turno++;
						Mensaje mensaj = new Mensaje(null);
						mensaj.setTipo(5);
						mensaj.setUsuarioTurno(nombres.get(turno));
						
						arrayUsuarios.get(0).enviarInfo(null, mensaj);
						arrayUsuarios.get(1).enviarInfo(null, mensaj);
					}
					else{
						turno--;
						Mensaje mensaj = new Mensaje(null);
						mensaj.setTipo(5);
						mensaj.setUsuarioTurno(nombres.get(turno));
						
						arrayUsuarios.get(0).enviarInfo(null, mensaj);
						arrayUsuarios.get(1).enviarInfo(null, mensaj);
					}
					if (turno==1) {
						pc.getVentana().getCc().realizarDisparosMaquina();
					}
				}
			}
		}
		if (mens.getTipo()==1) {
			ventanaServidor.imprimirMensaje(messageLf); 									// Imprimimos el mesaje en la ventana de chat
			for(int i = arrayUsuarios.size(); --i >= 0;) {
				HiloCliente ct = arrayUsuarios.get(i);
				ct.enviarInfo(messageLf, mens);
			}
		}	
	}
	
	/**
	 * Metodo para iniciar el servidor
	 * @throws InterruptedException 
	 */
	public void start() throws InterruptedException {
		keepGoing = true;
		try 																				// Se crea el serverSocket y se esperan las conexiones 
		{
			ServerSocket serverSocket = new ServerSocket(port); 							// El socket usado para la comunicación
			
			try {
				String ipLocal;
				ipLocal = InetAddress.getLocalHost().toString();
				System.out.println("Localhost: " + ipLocal);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			while(keepGoing) 																// Ciclo infinito en espera de conexiones 
			{
				imprimirChat("Server waiting for data on port " + port + "."); 		// Mensaje (esperando conexiones)
				if (modalidad!=2) {
					if (modalidad == 0) {
						Socket socket = serverSocket.accept();  							// Se aceptan las conexiones
						if(!keepGoing){														// En caso de que se pida detener el servidor
							break;
						}
						HiloCliente hCliente = new HiloCliente(socket, uniqueId, this);  	// Se inicializa un hilo por cada cliente que se conecte
						uniqueId +=1;														// Incrementamos el valor de uniqueId
						imprimirChat(hCliente.getUsername());								// Se imprime en ventana el nombre del nuevo usuario
						nombres.add(hCliente.getUsername());
						arrayUsuarios.add(hCliente);										// Agregamos dicho hiloCliente al arreglo
						hCliente.start();													// Y lo inicializamos
						ventanaServidor.setArregloHilos(this.arrayUsuarios);
						
						Mensaje mens = new Mensaje("Server");
						mens.setTipo(1);
						mens.setMensaje("Welcome to the game.");
						mens.setUsuarioTurno(nombres.get(0));
						hCliente.enviarInfo(null, mens);
						
						Mensaje mensaj = new Mensaje(null);
						mensaj.setTipo(5);
						mensaj.setUsuarioTurno(nombres.get(turno));
						hCliente.enviarInfo(null, mensaj);
					}else {
						Socket socket = serverSocket.accept();  							// Se aceptan las conexiones
						if(!keepGoing){														// En caso de que se pida detener el servidor
							break;
						}
						HiloCliente hCliente = new HiloCliente(socket, uniqueId, this);  	// Se inicializa un hilo por cada cliente que se conecte
						uniqueId +=1;														// Incrementamos el valor de uniqueId
						imprimirChat(hCliente.getUsername());								// Se imprime en ventana el nombre del nuevo usuario
						nombres.add(hCliente.getUsername());
						arrayUsuarios.add(hCliente);										// Agregamos dicho hiloCliente al arreglo
						hCliente.start();													// Y lo inicializamos
						
						Mensaje mens = new Mensaje("Server");
						mens.setTipo(1);
						mens.setMensaje("Welcome to the game.");
						mens.setUsuarioTurno(nombres.get(0));
						hCliente.enviarInfo(null, mens);
						
						Mensaje mensaj = new Mensaje(null);
						mensaj.setTipo(5);
						mensaj.setUsuarioTurno(nombres.get(turno));
						hCliente.enviarInfo(null, mensaj);
						
						Mensaje mensajo = new Mensaje(null);
						mensajo.setTipo(6);
						mensajo.setUsuarioTurno(nombres.get(turno));
						mensajo.setModalidad(modalidad);
						hCliente.enviarInfo(null, mensajo);
					}
				} else {
					Socket socket = serverSocket.accept();  							// Se aceptan las conexiones
					if(!keepGoing){														// En caso de que se pida detener el servidor
						break;
					}
					HiloCliente hCliente = new HiloCliente(socket, uniqueId, this);  	// Se inicializa un hilo por cada cliente que se conecte
					uniqueId +=1;														// Incrementamos el valor de uniqueId
					imprimirChat(hCliente.getUsername());								// Se imprime en ventana el nombre del nuevo usuario
					hCliente.start();													// Y lo inicializamos
					
					Mensaje mens = new Mensaje("Server");
					mens.setTipo(1);
					mens.setMensaje("El servidor se encuentra actualmente ocupado");
					mens.setUsuarioTurno(nombres.get(0));
					hCliente.enviarInfo(null, mens);
					
					hCliente.cerrarTodo();
				}
			}
			// Se detiene el servidor al estar fuera del ciclo
			try {
				// Se detiene el socket del servidor
				serverSocket.close();
				// Se detienen todos los hilos que esten contenidos en el arreglo de usuarios
				for(int i = 0; i < arrayUsuarios.size(); ++i) {
					HiloCliente hCLiente = arrayUsuarios.get(i);
					try {
						hCLiente.cerrarTodo();
					}
					catch(IOException ioE) {
						JOptionPane.showMessageDialog(ventanaServidor, "Excepcion cerrando los clientes: " +  ioE.getMessage());
					}
				}
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(ventanaServidor, "Excepcion cerrando el servidor: " + e.getMessage());
			}
		}
		// Algo paso paila
		catch (IOException e) {
            String msg = simpleDateFormat.format(new Date()) + "Excepcion creando el nuevo socket: " + e.getMessage();
            JOptionPane.showMessageDialog(ventanaServidor, msg);
		}
	}		
	
    /**
     * Metodo usado para detener el servidor
     */
	public void stop() {
		keepGoing = false;
		// Se conecta como cliente para iniciar el exit 
		// Socket socket = serverSocket.accept();
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(ventanaServidor, e.getMessage());
		}
	}
	
	/**
	 * Metodo usado para imprimir en el chat del servidor
	 * @param msg: mensaje usado para imprimir el en servidor
	 */
	private void imprimirChat(String msg) {
		String mensaje = simpleDateFormat.format(new Date()) + " " + msg;
		ventanaServidor.imprimirMensaje(mensaje);
	}

	/**
	 * Metodo Main usado para ejecutar la aplicación 
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		ventanaServidor = new VentanaServidor("Server");
		Servidor server = new Servidor(ventanaServidor);
		server.start();
		
	}
}