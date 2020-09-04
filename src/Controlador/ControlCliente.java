package Controlador;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import Modelo.Barco;
import Modelo.Coordenada;
import Modelo.Mensaje;
import Vista.VentanaAcerca;
import Vista.VentanaCliente;
import Vista.VentanaInstrucciones;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Clase encargada de gestionar lo relacionado con la interfaz del
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
public class ControlCliente implements ActionListener, MouseListener{

	public VentanaCliente ventana;			// Ventana principal
	private ObjectOutputStream sOutput;		// Para escribir por los sockets
	private JButton[][] btnzUser;			// Arreglo de botones del usuario
	private JButton[][] btnzEnemy;			// Arreglo de botones para disparos
	private int y, x, xi, yi, barcos;		// Variables usadas para la ubicación de los barcos
	private boolean orientacion;			// Variable usada para la orientacion de los barcos
	private Barco[] flota;					// Arreglo que contiene los barcos
	private String usuario;					// Nombre del usuario
	private int xDisparo, yDisparo;			// Variables usadas para determinar la posición de un disparo
	private String areaClick;				// Variable usada para determinar el area donde se ha dado click
	private int propiedadBoton;				// Variable usada para determinar si un boton ya fue modificado
	private int modalidad;                  // Variable usada para deterinar la modalidad de juego 1 = PvP // 2 = PvC
	private int barcosPc;					// Variable usada para ubicar los barcos enemigos
	private VentanaInstrucciones ventanaI;  //Ventana instrucciones
	private VentanaAcerca ventanaA;			//Ventana sobre el juego
	
	/**
	 * Metodo constructor de la clase ControlCliente
	 * @param ventana: ventana en la cual se vusualizara el juego
	 */
	public ControlCliente(VentanaCliente ventana, String nombre){
		this.ventana = ventana;
		ventanaI = new VentanaInstrucciones();
		ventanaA = new VentanaAcerca();
		// Inicializamos las variables necesarias para el funcionamiento del juego
		y=0;
		x=0;
		orientacion=true;
		barcos = 5;
		barcosPc = 5;
		btnzEnemy = new JButton[11][11];
		btnzUser = new JButton[11][11];
		flota = new Barco[5];
		usuario = nombre;
		modalidad = 0;
		llenarTableroUsuario(); 		// Llenamos el tablero del usuario a lo pro
		llenarTableroEnemigo();			// Llenamos el tablero del enemigo
		actualizarTableroUsuario();		// Actualizamos los graficos del tablero del usuario
		actualizarTableroEnemigo(); 	// Actualizamos los graficos del tablero del enemigo
		enviarBarcos();					// Metodo que esta constantemente verificando el estado del tablero para enviarlo
		
		for (int i = 0; i < 11; i++) {	// Ciclo que se encarga de agregar los listener todos los botones de la matriz
			for (int j = 0; j < 11; j++) {
				btnzUser[i][j].addMouseListener(this);
				btnzEnemy[i][j].addMouseListener(this);
			}
		}
	}
	
	/**
	 * Este metodo se encarga de modificar la propiedad imagen de un boton por un valor que llega por parametro
	 * @param x La coordenada en X de la ubicación del boton en el arreglo
	 * @param y La coordenada en Y de la ubicación del boton en el arreglo
	 * @param usuario El usuario que se debe modificar
	 * @param propiedad El atributo que sera asignado a la propiedad imagen del boton
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws LineUnavailableException 
	 */
	public void cambiarPropiedad(int x, int y, int usuario, int propiedad) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		if (propiedad==1) {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(ControlCliente.class.getResource("/Audio/fallido.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		}
		if (propiedad==2) {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(ControlCliente.class.getResource("/Audio/acertado.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		}
		if (propiedad==3) {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(ControlCliente.class.getResource("/Audio/destruido.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		}
		if(usuario == 0) // Tablero propio
			btnzUser[y][x].putClientProperty("imagen", propiedad);
		if(usuario == 1) // Tablero enemigo
			btnzEnemy[y][x].putClientProperty("imagen", propiedad);
	}
	
	/**
	 * Metodo encargado de llenar con valores predeterminados la matriz de botones del usuario
	 */
	public void llenarTableroUsuario(){
		for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                btnzUser[i][j]= new JButton("");
                btnzUser[i][j].putClientProperty("row", i);
                btnzUser[i][j].putClientProperty("column", j);
                btnzUser[i][j].putClientProperty("Name", "User");
                btnzUser[i][j].putClientProperty("imagen", 0);
                if(i==0||j==0){
                	if(i==0&&j!=0){
                		btnzUser[i][j].setMargin(new Insets(0,0,0,0));
                		btnzUser[i][j].setText(""+j);
                		btnzUser[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i!=0){
                		btnzUser[i][j].setMargin(new Insets(0,0,0,0));
                		btnzUser[i][j].setText(String.valueOf(Character.toChars(64+i)));
                		btnzUser[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i==0){
                		btnzUser[i][j].putClientProperty("imagen", -1);
                	}
                }
            }
        }
        ventana.llenarBotonesPanelUsuario(btnzUser);
	}
	
	/**
	 * Metodo encargado de llenar con valores predeterminados la matriz de botones del enemigo	
	 */
	public void llenarTableroEnemigo(){
		for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                btnzEnemy[i][j]= new JButton("");
                btnzEnemy[i][j].putClientProperty("row", i);
                btnzEnemy[i][j].putClientProperty("column", j);
                btnzEnemy[i][j].putClientProperty("Name", "Enemy");
                btnzEnemy[i][j].putClientProperty("imagen", 0);
                if(i==0||j==0){
                	if(i==0&&j!=0){
                		btnzEnemy[i][j].setMargin(new Insets(0,0,0,0));
                		btnzEnemy[i][j].setText(""+j);
                		btnzEnemy[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i!=0){
                		btnzEnemy[i][j].setMargin(new Insets(0,0,0,0));
                		btnzEnemy[i][j].setText(String.valueOf(Character.toChars(64+i)));
                		btnzEnemy[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i==0){
                		btnzEnemy[i][j].putClientProperty("imagen", -1);
                	}
                }
            }
        }
        ventana.llenarBotonesPanelEnemigo(btnzEnemy);
	}
	
	/**
	 * Metodo encargado de actualizar los graficos que corresponden a cada boton del usuario
	 * determinados por la propiedad "Image"
	 */
	public void actualizarTableroUsuario(){
		for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
            	/**
            	 * 0 = agua
            	 * 1 = fallido
            	 * 2 = acertado
            	 * 3 = destruido
            	 * 4 = punta horizontal
            	 * 5 = centro horizontal
            	 * 6 = cola horizontal
            	 * 7 = punta vertical
            	 * 8 = centro vertical
            	 * 9 = cola vertical
            	 */
            	switch ((int)btnzUser[i][j].getClientProperty("imagen")) {
				case 0:
					btnzUser[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/agua.jpg")));
					break;
				case 1:
					btnzUser[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/fallido.jpg")));
					break;
				case 2:
					btnzUser[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/disparo.jpg")));
					break;
				case 3:
					btnzUser[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/destruido.jpg")));
					break;
				case 4:
					btnzUser[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/puntaH.jpg")));
					break;
				case 5:
					btnzUser[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/mitadH.jpg")));
					break;
				case 6:
					btnzUser[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/colaH.jpg")));
					break;
				case 7:
					btnzUser[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/puntaV.jpg")));
					break;
				case 8:
					btnzUser[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/mitadV.jpg")));
					break;
				case 9:
					btnzUser[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/colaV.jpg")));
					break;
				case -1:
					break;
				}
            }
        }
	}
	
	/**
	 * Metodo encargado de actualizar los graficos que corresponden a cada boton del enemigo
	 * determinados por la propiedad "Image"
	 */
	public void actualizarTableroEnemigo(){
		for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
            	/**
            	 * 0 = agua
            	 * 1 = fallido
            	 * 2 = acertado
            	 * 3 = destruido
            	 * 4 = punta horizontal
            	 * 5 = centro horizontal
            	 * 6 = cola horizontal
            	 * 7 = punta vertical
            	 * 8 = centro vertical
            	 * 9 = cola vertical
            	 */
            	switch ((int)btnzEnemy[i][j].getClientProperty("imagen")) {
				case 0:
					btnzEnemy[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/agua.jpg")));
					break;
				case 1:
					btnzEnemy[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/fallido.jpg")));
					break;
				case 2:
					btnzEnemy[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/disparo.jpg")));
					break;
				case 3:
					btnzEnemy[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/destruido.jpg")));
					break;
				case 4:
					btnzEnemy[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/puntaH.jpg")));
					break;
				case 5:
					btnzEnemy[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/mitadH.jpg")));
					break;
				case 6:
					btnzEnemy[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/colaH.jpg")));
					break;
				case 7:
					btnzEnemy[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/puntaV.jpg")));
					break;
				case 8:
					btnzEnemy[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/mitadV.jpg")));
					break;
				case 9:
					btnzEnemy[i][j].setIcon(new ImageIcon(VentanaCliente.class.getResource("/img/colaV.jpg")));
					break;
				case -1:
					break;
				}
            }
        }
	}
	
	/**
	 * Metodo encargado de generar la vista previa de los barcos al momento en que van a ser ubicados
	 */
	public void vistaPreviaBarco(){
		 // true = vertical
		 // false = horizontal
		if (x!=0 && y!=0) {
			if(orientacion){
				if (barcos==5) { //Porta Aviones Vertical
					if (y>6){
						yi=6;
						xi=x;
					}
					else{
						yi=y;
						xi=x;
					}
					btnzUser[yi][xi].putClientProperty("imagen", 7);
					btnzUser[yi+1][xi].putClientProperty("imagen", 8);
					btnzUser[yi+2][xi].putClientProperty("imagen", 8);
					btnzUser[yi+3][xi].putClientProperty("imagen", 8);
					btnzUser[yi+4][xi].putClientProperty("imagen", 9);
				}
				if (barcos==4) {	//Acorazado Vertical
					if (y>7){
						yi=7;
						xi=x;
					}
					else{
						yi=y;
						xi=x;
					}
					btnzUser[yi][xi].putClientProperty("imagen", 7);
					btnzUser[yi+1][xi].putClientProperty("imagen", 8);
					btnzUser[yi+2][xi].putClientProperty("imagen", 8);
					btnzUser[yi+3][xi].putClientProperty("imagen", 9);
				}
				if (barcos==3) {	//Fragata Vertical
					if (y>8){
						yi=8;
						xi=x;
					}
					else{
						yi=y;
						xi=x;
					}
					btnzUser[yi][xi].putClientProperty("imagen", 7);
					btnzUser[yi+1][xi].putClientProperty("imagen", 8);
					btnzUser[yi+2][xi].putClientProperty("imagen", 9);
				}
				if (barcos==2) {	//Submarino Vertical
					if (y>7){
						yi=7;
						xi=x;
					}
					else{
						yi=y;
						xi=x;
					}
					btnzUser[yi][xi].putClientProperty("imagen", 7);
					btnzUser[yi+1][xi].putClientProperty("imagen", 8);
					btnzUser[yi+2][xi].putClientProperty("imagen", 8);
					btnzUser[yi+3][xi].putClientProperty("imagen", 9);
				}
				if (barcos==1) {	//Lancha rapida Vertical
					if (y>9){
						yi=9;
						xi=x;
					}
					else{
						yi=y;
						xi=x;
					}
					btnzUser[yi][xi].putClientProperty("imagen", 7);
					btnzUser[yi+1][xi].putClientProperty("imagen", 9);
				}
			}else{
				if (barcos==5) {	//Porta Aviones Horizontal
					if (x>6){
						yi=y;
						xi=6;
					}
					else{
						yi=y;
						xi=x;
					}
					btnzUser[yi][xi].putClientProperty("imagen", 4);
					btnzUser[yi][xi+1].putClientProperty("imagen", 5);
					btnzUser[yi][xi+2].putClientProperty("imagen", 5);
					btnzUser[yi][xi+3].putClientProperty("imagen", 5);
					btnzUser[yi][xi+4].putClientProperty("imagen", 6);
				}
				if (barcos==4) {	//Acorazado Horizontal
					if (x>7){
						yi=y;
						xi=7;
					}
					else{
						yi=y;
						xi=x;
					}
					btnzUser[yi][xi].putClientProperty("imagen", 4);
					btnzUser[yi][xi+1].putClientProperty("imagen", 5);
					btnzUser[yi][xi+2].putClientProperty("imagen", 5);
					btnzUser[yi][xi+3].putClientProperty("imagen", 6);
				}
				if (barcos==3) {	//Fragata Horizontal
					if (x>8){
						yi=y;
						xi=8;
					}
					else{
						yi=y;
						xi=x;
					}
					btnzUser[yi][xi].putClientProperty("imagen", 4);
					btnzUser[yi][xi+1].putClientProperty("imagen", 5);
					btnzUser[yi][xi+2].putClientProperty("imagen", 6);
				}
				if (barcos==2) {	//Submarino Horizontal
					if (x>7){
						yi=y;
						xi=7;
					}
					else{
						yi=y;
						xi=x;
					}
					btnzUser[yi][xi].putClientProperty("imagen", 4);
					btnzUser[yi][xi+1].putClientProperty("imagen", 5);
					btnzUser[yi][xi+2].putClientProperty("imagen", 5);
					btnzUser[yi][xi+3].putClientProperty("imagen", 6);
				}
				if (barcos==1) {	//Lancha rapida Horizontal
					if (x>9){
						yi=y;
						xi=9;
					}
					else{
						yi=y;
						xi=x;
					}
					btnzUser[yi][xi].putClientProperty("imagen", 4);
					btnzUser[yi][xi+1].putClientProperty("imagen", 6);
				}
			}
		}
	}
	
	/**
	 * Metodo encargado de ubicar los barcos en caso de que la ubicación este disponible
	 */
	public void ubicarBarco(){
		if (!choque()&&barcos>0) {
			if (barcos==5) 
				flota[0]=(new Barco(5, "Porta Aviones", orientacion, xi, yi));
			if (barcos==4) 
				flota[1]=(new Barco(4, "Acorazado", orientacion, xi, yi));
			if (barcos==3) 
				flota[2]=(new Barco(3, "Fragata", orientacion, xi, yi));
			if (barcos==2) 
				flota[3]=(new Barco(4, "Submarino", orientacion, xi, yi));
			if (barcos==1) 
				flota[4]=(new Barco(2, "Lancha rapida", orientacion, xi, yi));
			barcos--;
		}
	}
	
	/**
	 * Metodo que verifica que un punto no este ocupado por ninguno de los barcos
	 * @param x: Coordenada en x de la accion
	 * @param y: Coordenada en y de la accion
	 * @return True en caso de que el punto este ocupado
	 * 		   False en caso de que el punto este disponible
	 */
	public boolean puntoContenido(int x, int y){
		for (int i = 0; i < flota.length; i++) {
			if (flota[i]!=null) {
				for (int j = 0; j < flota[i].getPosiciones().length; j++) {
					if (x==flota[i].getPosiciones()[j].getX()&&y==flota[i].getPosiciones()[j].getY()) 
						return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Metodo que determina si la posible ubicación de un barco genera un choque con un barco ya ubicado
	 * @return true en caso de que dicha ubicación genere choque
	 * 		   false en caso de que dicha ubicación no genere choque
	 */
	public boolean choque(){
		if (orientacion) {		// Ubicacion de los barcos en posicion vertical
			if (barcos==5) 
				return false;
			if (barcos==4) {
				for (int i = 0; i < 4; i++) {
					if (puntoContenido(xi, yi+i)) 
						return true;
				}
			}
			if (barcos==3) {
				for (int i = 0; i < 3; i++) {
					if (puntoContenido(xi, yi+i)) 
						return true;
				}
			}
			if (barcos==2) {
				for (int i = 0; i < 4; i++) {
					if (puntoContenido(xi, yi+i)) 
						return true;
				}
			}
			if (barcos==1) {
				for (int i = 0; i < 2; i++) {
					if (puntoContenido(xi, yi+i)) 
						return true;
				}
			}
		}else{					// Ubicacion de los barcos en posicion horizontal
			if (barcos==5) 
				return false;
			if (barcos==4) {
				for (int i = 0; i < 4; i++) {
					if (puntoContenido(xi+i, yi)) 
						return true;
				}
			}
			if (barcos==3) {
				for (int i = 0; i < 3; i++) {
					if (puntoContenido(xi+i, yi)) 
						return true;
				}
			}
			if (barcos==2) {
				for (int i = 0; i < 4; i++) {
					if (puntoContenido(xi+i, yi)) 
						return true;
				}
			}
			if (barcos==1) {
				for (int i = 0; i < 2; i++) {
					if (puntoContenido(xi+i, yi)) 
						return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Metodo encargado de cambiar las propiedades de los botones teniendo en cuenta las coodernadas de los barcos
	 * contenidos en el arreglo de barcos
	 */
	public void pintarBarcos(){
		for (int i = 0; i < flota.length; i++) {
			if (flota[i]!=null) {
				for (int j = 0; j < flota[i].getPosiciones().length; j++) {
					if (flota[i].getOrientacion()) {
						if (j==0) {
							btnzUser[flota[i].getPosiciones()[j].getY()][flota[i].getPosiciones()[j].getX()].putClientProperty("imagen", 7);
						}else{
							if (j==flota[i].getPosiciones().length-1) {
								btnzUser[flota[i].getPosiciones()[j].getY()][flota[i].getPosiciones()[j].getX()].putClientProperty("imagen", 9);
							}else
								btnzUser[flota[i].getPosiciones()[j].getY()][flota[i].getPosiciones()[j].getX()].putClientProperty("imagen", 8);
						}
					}else{
						if (j==0) {
							btnzUser[flota[i].getPosiciones()[j].getY()][flota[i].getPosiciones()[j].getX()].putClientProperty("imagen", 4);
						}else{
							if (j==flota[i].getPosiciones().length-1) {
								btnzUser[flota[i].getPosiciones()[j].getY()][flota[i].getPosiciones()[j].getX()].putClientProperty("imagen", 6);
							}else
								btnzUser[flota[i].getPosiciones()[j].getY()][flota[i].getPosiciones()[j].getX()].putClientProperty("imagen", 5);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Metodo encargado de hacer un set de valores predeterminados a la propiedad Image
	 * de los botones para que todos sean agua
	 */
	public void limpiarTableroUsuario(){
		for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                btnzUser[i][j].putClientProperty("imagen", 0);
                if(i==0||j==0){
                	if(i==0&&j!=0){
                		btnzUser[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i!=0){
                		btnzUser[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i==0){
                		btnzUser[i][j].putClientProperty("imagen", -1);
                	}
                }
            }
        }
	}
	
	/**
	 * Metodo encargado de hacer un set de valores predeterminados a la propiedad Image
	 * de los botones del tablero enemigo
	 */
	public void limpiarTableroEnemigo(){
		for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                btnzEnemy[i][j].putClientProperty("imagen", 0);
                if(i==0||j==0){
                	if(i==0&&j!=0){
                		btnzEnemy[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i!=0){
                		btnzEnemy[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i==0){
                		btnzEnemy[i][j].putClientProperty("imagen", -1);
                	}
                }
            }
        }
	}
	
	/**
	 * Metodo encargado de enviar el tablero cuando todos los barcos han sido ubicados hace uso de un hilo para 
	 * verificar constantemente si los barcos ya se han ubicado
	 */
	public void enviarBarcos(){
		Thread enviarTablero = new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(barcos==0){
						Mensaje msn = new Mensaje("", 2, btnzUser);
						msn.setFlota(flota);
						msn.setUsuario(usuario);
						try {
							sOutput.writeObject(msn);
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(ventana, e1.getMessage());
							break;
						}
						barcos--;
						break;
					}
				}
			}
		});
		enviarTablero.start();
	}
	
	/**
	 * Metodo que informa el ganador de una partida cuando esta ha finalizado
	 * @param mensa Objeto que contiene la informacion del resultado de la partida
	 */
	public void finalizarPartida(Mensaje mensa){
		switch (JOptionPane.showConfirmDialog(ventana, mensa.getMensaje() + ". Do you want to continue playing?")) {
		case 0:
			y=0;
			x=0;
			orientacion=true;
			barcos = 5;
			flota = new Barco[5];
			enviarBarcos();				// Metodo que esta constantemente verificando el estado del tablero para enviarlo
			limpiarTableroUsuario();
			limpiarTableroEnemigo();
			actualizarTableroEnemigo();
			actualizarTableroUsuario();
			break;
			
		default:
			Mensaje msn = new Mensaje("Servidor", "You can not start the game because the user " + usuario + " has abandoned.", 1);
			try {
				sOutput.writeObject(msn);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(ventana, e1.getMessage());
			}
			break;
		}
	}
	
	/*******************************************************************************************************************/
	/************************************* INICIO DE METODOS MAQUINA ***************************************************/
	
	/**
	 * Metodo que informa el ganador de una partida cuando esta ha finalizado
	 * @param mensa Objeto que contiene la informacion del resultado de la partida
	 */
	public void finalizarPartidaMaquina(Mensaje mensa){
			barcosPc = 5;
			flota = new Barco[5];
			limpiarTableroUsuario();
			limpiarTableroEnemigo();
			actualizarTableroEnemigo();
			actualizarTableroUsuario();
			tableroMaquina();
	}

	/**
	 * Metodo que genera de forma aleatoria el tablero de la maquina con todos sus barcos en el
	 */
	public void tableroMaquina(){
		do{
			ubicarBarcoMaquina();
		}while(barcosPc!=0);
		
		Mensaje msn = new Mensaje("", 2, btnzUser);
		msn.setFlota(flota);
		msn.setUsuario(usuario);
		try {
			sOutput.writeObject(msn);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(ventana, e1.getMessage());
		}
	}
		
	/**
	 * Metodo encargado de ubicar los barcos del pc de forma recursiva
	 */
	public void ubicarBarcoMaquina(){
		// Generamos de forma aleatoria la orientacion y posicion inicial de los barcos
		int x = 1 + (int)(Math.random() * ((10 - 1) + 1));
		int y = 1 + (int)(Math.random() * ((10 - 1) + 1));
		boolean orientacion = Math.random() < 0.5;
		boolean auxParada = true;
		// Editamos los valores de X y Y dependiendo de la orientacion y tipo de barco
		if(orientacion){
			if (barcosPc==5) //Porta Aviones Vertical
				if (y>6) y=6;
			if (barcosPc==4) //Acorazado Vertical
				if (y>7) y=7;
			if (barcosPc==3) //Fragata Vertical
				if (y>8) y=8;
			if (barcosPc==2) //Submarino Vertical
				if (y>7) y=7;
			if (barcosPc==1) //Lancha rapida Vertical
				if (y>9) y=9;
		}else{
			if (barcosPc==5) //Porta Aviones Horizontal
				if (x>6) x=6;
			if (barcosPc==4) //Acorazado Horizontal
				if (x>7) x=7;
			if (barcosPc==3) //Fragata Horizontal
				if (x>8) x=8;
			if (barcosPc==2) //Submarino Horizontal
				if (x>7) x=7;
			if (barcosPc==1) //Lancha rapida Horizontal
				if (x>9) x=9;
		}
		// Se verifica que dicha posicion no genere un choque para de esta forma ubicar el barco
		if (!choque(x, y, barcosPc, orientacion)&&barcosPc>0) {
			if (barcosPc==5) 
				flota[0]=(new Barco(5, "Porta Aviones", orientacion, x, y));
			if (barcosPc==4) 
				flota[1]=(new Barco(4, "Acorazado", orientacion, x, y));
			if (barcosPc==3) 
				flota[2]=(new Barco(3, "Fragata", orientacion, x, y));
			if (barcosPc==2) 
				flota[3]=(new Barco(4, "Submarino", orientacion, x, y));
			if (barcosPc==1) {
				flota[4]=(new Barco(2, "Lancha rapida", orientacion, x, y));
				
				pintarBarcos();
				actualizarTableroUsuario();
			}
			barcosPc--;
			auxParada = false;
		}
		/***********************************************************************************************************************/
		if (auxParada) 
			ubicarBarcoMaquina();
	}
	
	/**
	 * Metodo que verifica que un punto no este ocupado por ninguno de los barcos
	 * @param x: Coordenada en x de la accion
	 * @param y: Coordenada en y de la accion
	 * @return True en caso de que el punto este ocupado
	 * 		   False en caso de que el punto este disponible
	 */
	public boolean puntoContenidoPc(int x, int y){
		for (int i = 0; i < flota.length; i++) {
			if (flota[i]!=null) {
				for (int j = 0; j < flota[i].getPosiciones().length; j++) {
					if (x==flota[i].getPosiciones()[j].getX()&&y==flota[i].getPosiciones()[j].getY()) 
						return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Metodo que determina si la posible ubicación de un barco genera un choque con un barco ya ubicado
	 * @return true en caso de que dicha ubicación genere choque
	 * 		   false en caso de que dicha ubicación no genere choque
	 */
	public boolean choque(int xi, int yi, int barcos, boolean orientacion){
		if (orientacion) {
			if (barcos==5) return false;
			if (barcos==4) {
				for (int i = 0; i < 4; i++) 
					if (puntoContenido(xi, yi+i)) return true;
			}
			if (barcos==3) {
				for (int i = 0; i < 3; i++) 
					if (puntoContenido(xi, yi+i)) return true;
			}
			if (barcos==2) {
				for (int i = 0; i < 4; i++) 
					if (puntoContenido(xi, yi+i)) return true;
			}
			if (barcos==1) {
				for (int i = 0; i < 2; i++) 
					if (puntoContenido(xi, yi+i)) return true;
			}
		}else{
			if (barcos==5) return false;
			if (barcos==4) {
				for (int i = 0; i < 4; i++) 
					if (puntoContenido(xi+i, yi)) return true;
			}
			if (barcos==3) {
				for (int i = 0; i < 3; i++) 
					if (puntoContenido(xi+i, yi)) return true;
			}
			if (barcos==2) {
				for (int i = 0; i < 4; i++) 
					if (puntoContenido(xi+i, yi)) return true;
			}
			if (barcos==1) {
				for (int i = 0; i < 2; i++) 
					if (puntoContenido(xi+i, yi)) return true;
			}
		}
		return false;
	}
	
	/**
	 * Metodo usado para realizar un disparo de forma aleatoria
	 */
	public void realizarDisparosMaquina(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int x = 1 + (int)(Math.random() * ((10 - 1) + 1));
		int y = 1 + (int)(Math.random() * ((10 - 1) + 1));
		propiedadBoton=(int) btnzEnemy[x][y].getClientProperty("imagen");
		
		Mensaje msn = new Mensaje(usuario);
		msn.setTipo(3);
		msn.setCoord(new Coordenada(x, y));
		msn.setMensaje("Disparo Pc");
		
		if (propiedadBoton==0) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				sOutput.writeObject(msn);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(ventana, e1.getMessage());
			}
		} else
			realizarDisparosMaquina();
	}
	/******************************************************************************************************************/

	/**
	 * Metodo que retorna el OutputStream
	 * @return the sOutput
	 */
	public ObjectOutputStream getsOutput() {
		return sOutput;
	}

	/**
	 * Metodo que configura el OutputStream
	 * @param sOutput El nuevo objeto a asignar
	 */
	public void setsOutput(ObjectOutputStream sOutput) {
		this.sOutput = sOutput;
	}
	
	/**
	 * Metodo actionPerformed que se implementa de forma automagica al implementar actionLinstener
	 * Usado para el boton del chat
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()== ventana.getBtnEnviar()){
			String mensaje = ventana.obtenerTextoJTextField();
			Mensaje msn = new Mensaje(usuario, mensaje, 1);
			try {
				sOutput.writeObject(msn);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(ventana, e1.getMessage());
			}
			ventana.limpiarTextField();
		}
		if (e.getSource()==ventana.getBtnPvP()) {
			modalidad=1;
			ventana.deshabilitarBoton(2);
			Mensaje mensaje = new Mensaje(getUsuario());
			mensaje.setModalidad(modalidad);
			mensaje.setTipo(6);
			
			try {
				sOutput.writeObject(mensaje);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(ventana, e1.getMessage());
			}
		}
		if (e.getSource()==ventana.getBtnPvC()) {
			modalidad=2;
			ventana.deshabilitarBoton(1);
			Mensaje mensaje = new Mensaje(getUsuario());
			mensaje.setModalidad(modalidad);
			mensaje.setTipo(6);
			
			try {
				sOutput.writeObject(mensaje);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(ventana, e1.getMessage());
			}
		}
		
		if(e.getSource()==ventana.getBtnInstrucciones()){
			ventanaI.setVisible(true);
			}
		
		if(e.getSource()==ventana.getBtnAcerca()){
			ventanaA.setVisible(true);
			}
		if (e.getSource()==ventana.getBtnPuntajes()) {
			Mensaje mensaje = new Mensaje(getUsuario());
			mensaje.setModalidad(modalidad);
			mensaje.setTipo(7);
			try {
				sOutput.writeObject(mensaje);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(ventana, e1.getMessage());
			}
		}
	}

	/**
	 * Metodo que determina que accion tomar cuando se presiona un boton
	 * se genera de forma automagica al implementar mouseListener
	 */
	public void mouseClicked(MouseEvent e) {
		if (modalidad!=0) {
			if(!SwingUtilities.isRightMouseButton(e)){
				if (areaClick.equals("User") && barcos != -1) {
					ubicarBarco();
					vistaPreviaBarco();
					limpiarTableroUsuario();
					pintarBarcos();
					actualizarTableroUsuario();
				}
				if (areaClick.equals("Enemy")) {
					if(xDisparo != 0 && yDisparo != 0 ){
						Mensaje msn = new Mensaje(usuario);
						msn.setTipo(3);
						msn.setCoord(new Coordenada(xDisparo, yDisparo));
						msn.setMensaje("Disparo");
						if (propiedadBoton==0) {
							try {
								sOutput.writeObject(msn);
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(ventana, e1.getMessage());
							}
						}			
					}
				}
		    }else{
		    	orientacion=!orientacion;
		    	vistaPreviaBarco();
		    	actualizarTableroUsuario();
		    }
		}
	}
		
	/**
	 * Metodo que obtiene las propiedades e informacion de un boton al entrar en su area
	 * se genera de forma automagica al implementar mouseListener
	 */
	public void mouseEntered(MouseEvent e) {
		if (modalidad!=0) {
			JButton btn = (JButton) (e.getSource());
			areaClick = (String) btn.getClientProperty("Name");
			propiedadBoton = (int) btn.getClientProperty("imagen");
			if (btn.getClientProperty("Name").equals("User")) { 
				x = (int)btn.getClientProperty("column");
				y = (int)btn.getClientProperty("row");
				if (barcos!=-1) {
					vistaPreviaBarco();
					actualizarTableroUsuario();
					limpiarTableroUsuario();
					pintarBarcos();
				}
			}
			if (btn.getClientProperty("Name").equals("Enemy")) {
				xDisparo = (int)btn.getClientProperty("column");
				yDisparo = (int)btn.getClientProperty("row");
			}
		}
	}

	/**
	 * Se genera de forma automagica al implementar mouseListener
	 */
	public void mouseExited(MouseEvent arg0) {
	}

	/**
	 * Se genera de forma automagica al implementar mouseListener
	 */
	public void mousePressed(MouseEvent arg0) {
	}

	/**
	 * Se genera de forma automagica al implementar mouseListener
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Metodo que retorna el nombre del usuario
	 * @return el usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Metodo para modificar el valor del nombre del usuario
	 * @param usuario El dato a modificar
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Metodo que retorna la modalidad de juego
	 * @return La modalidad
	 */
	public int getModalidad() {
		return modalidad;
	}

	/**
	 * Metodo que configura la modalidad del juego
	 * @param modalidad El dato que sera asignado
	 */
	public void setModalidad(int modalidad) {
		this.modalidad = modalidad;
	}
}