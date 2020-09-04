package Controlador;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import Modelo.Barco;
import Modelo.HiloCliente;
import Modelo.Mensaje;
import Vista.VentanaServidor;

/**
 * Clase encargada de gestionar lo relacionado con la interfaz del servidor
 * Extiende de Threat
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
public class ControlServidor implements ActionListener {
	
	public VentanaServidor ventana;			// Ventana principal
	private JButton[][] btnzUser1;			// Arreglo de botones del usuario
	private JButton[][] btnzUser2;			// Arreglo de botones para disparos
	private Barco[] flota1;					// Arreglo que contiene los barcos
	private Barco[] flota2;					// Arreglo que contiene los barcos
	private int posFlota1;					// Valor de la posicion de la ultima flota del usuario 1 que recibio un disparo
	private int posFlota2;					// Valor de la posicion de la ultima flota del usuario 2 que recibio un disparo
	private ArrayList<HiloCliente> arrayUsuarios; 		// Arreglo que contendra los usuarios que estan en juego
	
	/**
	 * Metodo constructor de la clase ControlServidor
	 * @param ventana: ventana en la cual se vusualizara el juego
	 */
	public ControlServidor(VentanaServidor ventana){
		this.ventana = ventana;
		btnzUser1 = new JButton[11][11];
		btnzUser2 = new JButton[11][11];
		llenarTablerosUsuarios();
		actualizarTableros();
	}
		
	/**
	 * Metodo que reinicia la informacion del servidor
	 */
	public void reiniciarDatos(){
		flota1=null;
		flota2=null;
		limpiarTableros();
		actualizarTableros();
	}
	
	/**
	 * Metodo encargado de hacer un set de valores predeterminados a la propiedad Image
	 * de los botones de los tableros
	 */
	public void limpiarTableros(){
		for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
            	btnzUser1[i][j].putClientProperty("imagen", 0);
            	btnzUser2[i][j].putClientProperty("imagen", 0);
                if(i==0||j==0){
                	if(i==0&&j!=0){
                		btnzUser1[i][j].putClientProperty("imagen", -1);
                    	btnzUser2[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i!=0){
                		btnzUser1[i][j].putClientProperty("imagen", -1);
                    	btnzUser2[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i==0){
                		btnzUser1[i][j].putClientProperty("imagen", -1);
                    	btnzUser2[i][j].putClientProperty("imagen", -1);
                	}
                }
            }
        }
	}
	
	/**
	 * Metodo encargado de cambiar las propiedades de los botones teniendo en cuenta las coodernadas de los barcos
	 * contenidos en el arreglo de barcos
	 */
	public void pintarBarcos(){
		if (flota1!=null) {
			for (int i = 0; i < flota1.length; i++) {
				if (flota1[i]!=null) {
					for (int j = 0; j < flota1[i].getPosiciones().length; j++) {
						if (flota1[i].getOrientacion()) {
							if (j==0) {
								btnzUser1[flota1[i].getPosiciones()[j].getY()][flota1[i].getPosiciones()[j].getX()].putClientProperty("imagen", 7);
							}else{
								if (j==flota1[i].getPosiciones().length-1) {
									btnzUser1[flota1[i].getPosiciones()[j].getY()][flota1[i].getPosiciones()[j].getX()].putClientProperty("imagen", 9);
								}else
									btnzUser1[flota1[i].getPosiciones()[j].getY()][flota1[i].getPosiciones()[j].getX()].putClientProperty("imagen", 8);
							}
						}else{
							if (j==0) {
								btnzUser1[flota1[i].getPosiciones()[j].getY()][flota1[i].getPosiciones()[j].getX()].putClientProperty("imagen", 4);
							}else{
								if (j==flota1[i].getPosiciones().length-1) {
									btnzUser1[flota1[i].getPosiciones()[j].getY()][flota1[i].getPosiciones()[j].getX()].putClientProperty("imagen", 6);
								}else
									btnzUser1[flota1[i].getPosiciones()[j].getY()][flota1[i].getPosiciones()[j].getX()].putClientProperty("imagen", 5);
							}
						}
					}
				}
			}
		}
		
		if (flota2!=null) {
			for (int i = 0; i < flota2.length; i++) {
				if (flota2[i]!=null) {
					for (int j = 0; j < flota2[i].getPosiciones().length; j++) {
						if (flota2[i].getOrientacion()) {
							if (j==0) {
								btnzUser2[flota2[i].getPosiciones()[j].getY()][flota2[i].getPosiciones()[j].getX()].putClientProperty("imagen", 7);
							}else{
								if (j==flota2[i].getPosiciones().length-1) {
									btnzUser2[flota2[i].getPosiciones()[j].getY()][flota2[i].getPosiciones()[j].getX()].putClientProperty("imagen", 9);
								}else
									btnzUser2[flota2[i].getPosiciones()[j].getY()][flota2[i].getPosiciones()[j].getX()].putClientProperty("imagen", 8);
							}
						}else{
							if (j==0) {
								btnzUser2[flota2[i].getPosiciones()[j].getY()][flota2[i].getPosiciones()[j].getX()].putClientProperty("imagen", 4);
							}else{
								if (j==flota2[i].getPosiciones().length-1) {
									btnzUser2[flota2[i].getPosiciones()[j].getY()][flota2[i].getPosiciones()[j].getX()].putClientProperty("imagen", 6);
								}else
									btnzUser2[flota2[i].getPosiciones()[j].getY()][flota2[i].getPosiciones()[j].getX()].putClientProperty("imagen", 5);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Metodo encargado de llenar los tableros de los usuarios con valores predeterminados
	 */
	public void llenarTablerosUsuarios(){
		for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
            	//Actualizamos el tablero del jugador 1
                btnzUser1[i][j]= new JButton("");
                btnzUser1[i][j].putClientProperty("row", i);
                btnzUser1[i][j].putClientProperty("column", j);
                btnzUser1[i][j].putClientProperty("Name", "User");
                btnzUser1[i][j].putClientProperty("imagen", 0);
                //Actualizamos el tablero del jugador 2
                btnzUser2[i][j]= new JButton("");
                btnzUser2[i][j].putClientProperty("row", i);
                btnzUser2[i][j].putClientProperty("column", j);
                btnzUser2[i][j].putClientProperty("Name", "User");
                btnzUser2[i][j].putClientProperty("imagen", 0);
                
                if(i==0||j==0){
                	if(i==0&&j!=0){
                		btnzUser1[i][j].setMargin(new Insets(0,0,0,0));
                		btnzUser1[i][j].setText(""+j);
                		btnzUser1[i][j].putClientProperty("imagen", -1);
                		
                		btnzUser2[i][j].setMargin(new Insets(0,0,0,0));
                		btnzUser2[i][j].setText(""+j);
                		btnzUser2[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i!=0){
                		btnzUser1[i][j].setMargin(new Insets(0,0,0,0));
                		btnzUser1[i][j].setText(String.valueOf(Character.toChars(64+i)));
                		btnzUser1[i][j].putClientProperty("imagen", -1);
                		
                		btnzUser2[i][j].setMargin(new Insets(0,0,0,0));
                		btnzUser2[i][j].setText(String.valueOf(Character.toChars(64+i)));
                		btnzUser2[i][j].putClientProperty("imagen", -1);
                	}
                	if(j==0&&i==0){
                		btnzUser1[i][j].putClientProperty("imagen", -1);
                		
                		btnzUser2[i][j].putClientProperty("imagen", -1);
                	}
                }
            }
        }
        ventana.llenarBotonesPanelUsuario1(btnzUser1);
        ventana.llenarBotonesPanelUsuario2(btnzUser2);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==ventana.getBtnEnviar()) {
			String mensaje = ventana.obtenerTextoJTextField();
			Mensaje msn = new Mensaje("Server", mensaje, 1);
			
			ventana.imprimirMensaje("Sever says: " + mensaje); 			// Imprimimos el mesaje en la ventana de chat
			for(int i = arrayUsuarios.size(); --i >= 0;) {
				HiloCliente ct = arrayUsuarios.get(i);
				ct.enviarInfo(mensaje, msn);
			}
			ventana.limpiarTextField();
		}
	}
	
	/**
	 * Metodo encargado de actualizar los graficos que corresponden a cada boton de los tableros
	 * determinados por la propiedad "Image"
	 */
	public void actualizarTableros(){
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
            	//Tablero Usuario 1
            	switch ((int)btnzUser1[i][j].getClientProperty("imagen")) {
				case 0:
					btnzUser1[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/agua.jpg")));
					break;
				case 1:
					btnzUser1[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/fallido.jpg")));
					break;
				case 2:
					btnzUser1[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/disparo.jpg")));
					break;
				case 3:
					btnzUser1[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/destruido.jpg")));
					break;
				case 4:
					btnzUser1[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/puntaH.jpg")));
					break;
				case 5:
					btnzUser1[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/mitadH.jpg")));
					break;
				case 6:
					btnzUser1[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/colaH.jpg")));
					break;
				case 7:
					btnzUser1[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/puntaV.jpg")));
					break;
				case 8:
					btnzUser1[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/mitadV.jpg")));
					break;
				case 9:
					btnzUser1[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/colaV.jpg")));
					break;
				case -1:
					break;
				}
            	//Tablero Usuario 2
            	switch ((int)btnzUser2[i][j].getClientProperty("imagen")) {
				case 0:
					btnzUser2[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/agua.jpg")));
					break;
				case 1:
					btnzUser2[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/fallido.jpg")));
					break;
				case 2:
					btnzUser2[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/disparo.jpg")));
					break;
				case 3:
					btnzUser2[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/destruido.jpg")));
					break;
				case 4:
					btnzUser2[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/puntaH.jpg")));
					break;
				case 5:
					btnzUser2[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/mitadH.jpg")));
					break;
				case 6:
					btnzUser2[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/colaH.jpg")));
					break;
				case 7:
					btnzUser2[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/puntaV.jpg")));
					break;
				case 8:
					btnzUser2[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/mitadV.jpg")));
					break;
				case 9:
					btnzUser2[i][j].setIcon(new ImageIcon(VentanaServidor.class.getResource("/img/colaV.jpg")));
					break;
				case -1:
					break;
				}
            }
        }
	}
	
	/**
	 * Metodo que verifica que un punto no este ocupado por ninguno de los barcos el alguno de los tableros
	 * @param x: Coordenada en x de la accion
	 * @param y: Coordenada en y de la accion
	 * @param usuario: EL usuario que realizo el disparo
	 * @return True en caso de que el punto este ocupado
	 * 		   False en caso de que el punto este disponible
	 */
	public boolean puntoContenido(int x, int y, int usuario){
		if (usuario==0	) {
			for (int i = 0; i < flota2.length; i++) {
				if (flota2[i]!=null) {
					for (int j = 0; j < flota2[i].getPosiciones().length; j++) {
						if (x==flota2[i].getPosiciones()[j].getX()&&y==flota2[i].getPosiciones()[j].getY()) {
							flota2[i].getPosiciones()[j].setDisparo(1);
							posFlota2 = i;
							return true;
						}
					}
				}
			}
		}else{
			for (int i = 0; i < flota1.length; i++) {
				if (flota1[i]!=null) {
					for (int j = 0; j < flota1[i].getPosiciones().length; j++) {
						if (x==flota1[i].getPosiciones()[j].getX()&&y==flota1[i].getPosiciones()[j].getY()) {
							flota1[i].getPosiciones()[j].setDisparo(1);
							posFlota1 = i;
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Este metodo se encarga de modificar la propiedad imagen de un boton por un valor que llega por parametro
	 * @param x La coordenada en X de la ubicación del boton en el arreglo
	 * @param y La coordenada en Y de la ubicación del boton en el arreglo
	 * @param usuario El usuario que se debe modificar
	 * @param propiedad El atributo que sera asignado a la propiedad imagen del boton
	 */
	public void cambiarPropiedad(int x, int y, int usuario, int propiedad){
		if(usuario == 0){
			btnzUser2[y][x].putClientProperty("imagen", propiedad);
		}
		if(usuario == 1){
			btnzUser1[y][x].putClientProperty("imagen", propiedad);
		}
	}

	/**
	 * Metodo que retorna el arreglo de botones del usuario1
	 * @return the btnzUser1
	 */
	public JButton[][] getBtnzUser1() {
		return btnzUser1;
	}

	/**
	 * Metodo que configura el arreglo de botones del usuario1
	 * @param btnzUser1 the btnzUser1 to set
	 */
	public void setBtnzUser1(JButton[][] btnzUser1) {
		this.btnzUser1 = btnzUser1;
	}

	/**
	 * Metodo que retorna el arreglo de botones del usuario2
	 * @return the btnzUser2
	 */
	public JButton[][] getBtnzUser2() {
		return btnzUser2;
	}

	/**
	 * Metodo que configura el arreglo de botones del usuario2
	 * @param btnzUser2 the btnzUser2 to set
	 */
	public void setBtnzUser2(JButton[][] btnzUser2) {
		this.btnzUser2 = btnzUser2;
	}

	/**
	 * Metodo que retorna la flota del usuario1
	 * @return the flota
	 */
	public Barco[] getFlota1() {
		return flota1;
	}

	/**
	 * Metodo que configura la flota del usuario1
	 * @param flota1 the flota to set
	 */
	public void setFlota1(Barco[] flota1) {
		this.flota1 = flota1;
	}

	/**
	 * Metodo que retorna la flota del usuario2
	 * @return the flota2
	 */
	public Barco[] getFlota2() {
		return flota2;
	}

	/**
	 * Metodo que configura la flota del usuario2
	 * @param flota2 the flota2 to set
	 */
	public void setFlota2(Barco[] flota2) {
		this.flota2 = flota2;
	}

	/**
	 * @return the posFlota1
	 */
	public int getPosFlota1() {
		return posFlota1;
	}

	/**
	 * @param posFlota1 the posFlota1 to set
	 */
	public void setPosFlota1(int posFlota1) {
		this.posFlota1 = posFlota1;
	}

	/**
	 * @return the posFlota2
	 */
	public int getPosFlota2() {
		return posFlota2;
	}

	/**
	 * @param posFlota2 the posFlota2 to set
	 */
	public void setPosFlota2(int posFlota2) {
		this.posFlota2 = posFlota2;
	}

	/**
	 * @return the arrayUsuarios
	 */
	public ArrayList<HiloCliente> getArrayUsuarios() {
		return arrayUsuarios;
	}

	/**
	 * @param arrayUsuarios the arrayUsuarios to set
	 */
	public void setArrayUsuarios(ArrayList<HiloCliente> arrayUsuarios) {
		this.arrayUsuarios = arrayUsuarios;
	}
}