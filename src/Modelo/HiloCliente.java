package Modelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

/**
 * Clase encargada de gestionar los mensajes de los Clientes(Hilos)
 * Extiende de Threat
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
public class HiloCliente extends Thread {
	// El socket para leer y escribir
	private Socket socket;
	// Objetos para lectura y escritura
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	// Id para identificar los usuarios y gestionar los turnos
	@SuppressWarnings("unused")
	private int id;
	// Nombre de usuario del cliente
	private String username;
	// El unico tipo de objeto que sera enviado
	private Mensaje msn;
	// La fecha de conexion
	@SuppressWarnings("unused")
	private String date;
	//Objeto tipo servidor para impimir los mensajes
	private Servidor servidor;

	/**
	 * Metodo constructor de HiloCLiente
	 * @param socket Usado para la conexion con el servidor
	 * @param uId Identificador del usuario
	 * @param server Objeto servidor
	 */
	HiloCliente(Socket socket, int uId, Servidor server) {
		// Id unico de usuario
		id = uId;
		this.socket = socket;
		// Creamos los data stream
		servidor = server;
		try
		{
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			sInput  = new ObjectInputStream(socket.getInputStream());
			// Leemos el nombre de usuario
			username = (String) sInput.readObject();
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		// Se captura el error y se notifica de ello
		catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
        date = new Date().toString() + "\n";
	}
	
	/**
	 * Metodo que cierra todos los medio usados para transmitir informacion
	 * @throws IOException
	 */
	public void cerrarTodo() throws IOException{
		sInput.close();
		sOutput.close();
		socket.close();
	}

	/**
	 * Metodo run usado para la funcionalidad principal del hilo
	 */
	public void run() {
		// Correra siempre y cuando se este ejecutando
		boolean keepGoing = true;
		while(keepGoing) {
			// Se lee un String que llega en un objeto
			try {
				msn = (Mensaje) sInput.readObject();
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(null, username + " Excepcion leyendo los streams: " + e);
				break;				
			}
			catch(ClassNotFoundException e2) {
				break;
			}
			// EL mensaje que sera parte del chat
			String message = msn.toString();
			// Switch para determinar el tipo de mensaje recibido
			switch(msn.getTipo()) {

			case 1: // Mensaje para el chat
				try {
					servidor.broadcast(username + " dice: " + message, msn);
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2: // Envio de tablero
				try {
					servidor.broadcast(username + ": " + "Se recibio el tablero del usuario", msn);
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 3: // Envio de disparo
				try {
					servidor.broadcast(username + ": " + "Realizo un disparo", msn);
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 6: // Modalidad Juego
				try {
					servidor.broadcast(null, msn);
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 7: // mensaje puntajes
				try {
					servidor.broadcast(null, msn);
				} catch (UnsupportedAudioFileException | IOException
						| LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		close();
	}
	
	/**
	 * Se cierran todos los objetos usados para comunicacion
	 */
	private void close() {
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {}
		try {
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {};
		try {
			if(socket != null) socket.close();
		}
		catch (Exception e) {}
	}

	/**
	 * Metodo que escribe un String a los clientes usando el Output Stream
	 * @param msg La cadena que sera escrita
	 * @return true si el proceso se culmino de forma correcta
	 *         flase si no hay conexion disponible 
	 */
	public boolean enviarInfo(String msg, Mensaje mens) {
		if(!socket.isConnected()) { 			// Si el cliente esta conectado y se puede enviar el mensaje
			close();
			return false;
		}
		try { 									// Se escribe el mensaje por el Stream
			sOutput.writeObject(mens);
		}
		catch(IOException e) { 					// Si ocurre un error, no se abrota el proceso, solo se informa al usuario
			JOptionPane.showMessageDialog(null, "Error enviando mensaje a " + username + e.getMessage());
		}
		return true;
	}
	
	/**
	 * Metodo que retorna el valor del username
	 * @return El valor del username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Metodo usado para asignar un valor a username
	 * @param username El valor a asignar
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}