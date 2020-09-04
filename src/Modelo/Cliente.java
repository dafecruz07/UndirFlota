package Modelo;
import java.net.*;
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import Controlador.ControlCliente;
import Vista.VentanaCliente;
import Vista.VentanaPuntaje;

/**
 * Clase: Cliente
 * Clase encargada de gestionar lo relacionado con los clientes envio y recepcion de objetos 
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
public class Cliente  {
	// Objetos para entrada y salida
	private ObjectInputStream sInput;		// Para leer por los sockets
	private ObjectOutputStream sOutput;		// Para escribir por los sockets
	private Socket socket;					// Para enviar y recibir los objetos
	private VentanaCliente ventana;			// Ventana del cliente
	private VentanaPuntaje ventanaPuntos;	// Ventana usada mostrar los puntajes acumulados de todos los usuarios
	private String server, username;		// Variables usadas para Servidor y nombre de usuario
	private int port;						// Varibale usada para el puerto

	/**
	 * Metodo constructor del cliente
	 * @param server 	Servidor usado para comunicacion
	 * @param port		Puerto que se usara para el envio y recepción de objetos
	 * @param username	Nombre del usuario(cliente)
	 */
	Cliente(String server, int port, String name) {
		this.server = server;
		this.port = port;
		username = name;
		ventana = new VentanaCliente(username + " - Sea Battle", username);
		ventana.setVisible(true);
		ventanaPuntos = new VentanaPuntaje();
	}
	
	/**
	 * Para iniciar la comunicación
	 * @return true/false 
	 * true  = En caso de que la comunicación con el servidor se establezca de forma correcta
	 * false = En caso de que se genere un error intentando comunicarse con el servidor
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws LineUnavailableException 
	 */
	public boolean start() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(ControlCliente.class.getResource("/Audio/musicaFondo.wav"));
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
		try {
			socket = new Socket(server, port);		// Se intenta realizar conexion con el servidor
		} 
		// En caso de falla en la comunicación
		catch(Exception ec) {
			JOptionPane.showMessageDialog(ventana, "Error connecting to server:" + ec.getMessage());
			return false;
		}
		
		try
		{
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			ventana.getCc().setsOutput(sOutput);
		}
		catch (IOException eIO) {
			JOptionPane.showMessageDialog(ventana, "Excepcion creando los stream I/O: " + eIO.getMessage());
			return false;
		}

		// Se crea un hilo para escuchar desde el servidor
		new EscuchadorServidor(sInput, ventana, ventanaPuntos).start();
		// Se envia el nombre de usuario al servidor
		// Se envia un string todos los demas mensajes seran objetos
		try
		{
			sOutput.writeObject(username);
		}
		catch (IOException eIO) {
			JOptionPane.showMessageDialog(ventana, "Se genero una excepcion conectandose al sevidor: " + eIO.getMessage());
			disconnect();
			return false;
		}
		// Informamos con un true que la conexión se realizo de forma exitosa
		return true;
	}

	/**
	 * Metodo usado para imprimir en el ventana del chat
	 * @param msg Texto que sera impreso en dicha ventana
	 */
	private void display(String msg) {
			ventana.imprimirMensaje(msg);
	}
	
	/**
	 * Metodo usado para enviar un objeto
	 * @param msg Objeto que sera enviado
	 */
	public void enviarMensaje(Mensaje msg) {
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception trying the send the object to the server: " + e.getMessage());
		}
	}

	/**
	 * Cuando algo va mal se desconectan los stream y el socket
	 */
	private void disconnect() {
		try { 
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(ventana, "Error closing the inputStream: " + e.getMessage());
		}
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(ventana, "Error closing the output stream: " + e.getMessage());
		}
        try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(ventana, "Error closing the socket: " + e.getMessage());
		}
	}

	/**
	 * @return the sInput
	 */
	public ObjectInputStream getsInput() {
		return sInput;
	}

	/**
	 * @param sInput the sInput to set
	 */
	public void setsInput(ObjectInputStream sInput) {
		this.sInput = sInput;
	}

	/**
	 * @return the sOutput
	 */
	public ObjectOutputStream getsOutput() {
		return sOutput;
	}

	/**
	 * @param sOutput the sOutput to set
	 */
	public void setsOutput(ObjectOutputStream sOutput) {
		this.sOutput = sOutput;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @return the ventana
	 */
	public VentanaCliente getVentana() {
		return ventana;
	}

	/**
	 * @param ventana the ventana to set
	 */
	public void setVentana(VentanaCliente ventana) {
		this.ventana = ventana;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Metodo que retorna el valor actual de puerto
	 * @return El port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Metodo usado para asirngar un valor al puerto
	 * @param port El valor a asignar
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Metodo main para inicar el cliente
	 * @param args
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		// Valores predeterminados
		int portNumber = 1234;
		// localhost o la ip
		String serverAddress = "192.168.0.33";
		String userName = JOptionPane.showInputDialog("Write your nickname: ");
		
		if (userName==null) 
			userName="Anonymous";
		
		Cliente client = new Cliente(serverAddress, portNumber, userName);	// Creamos el objeto cliente
		
		if(!client.start())				// Probamos si podemos conectarnos con el server
			return;						// Si la conexión falla no podeos hacer nada
	}
}