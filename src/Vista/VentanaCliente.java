package Vista;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.GridLayout;
import Controlador.ControlCliente;
import Modelo.Mensaje;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.awt.Toolkit;
import javax.swing.SwingConstants;

/**
 * Clase: VentanaPrincipal
 * Clase encargada de gestionar lo relacionado con lo visual del servidor y los clientes 
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
@SuppressWarnings("serial")
public class VentanaCliente extends JFrame /**implements ActionListener*/ {

	private JPanel contentPane;
	private JPanel panelPropio;
	private JPanel panelEnemigo;
	private JTextArea textAreaChat;
	private JTextField textField;
	private JButton btnPvP;
	private JButton btnPuntajes;
	private ControlCliente cc;
	private String user;
	private JLabel lblTextoturno;
	private JButton btnEnviar;
	private JButton btnPvC;
	private JButton btnInstrucciones;
	private JButton btnAcerca;

	/**
	 * Metodo constructor de la ventana
	 * @param tittle: variable usada para el titulo de la ventana.
	 */
	public VentanaCliente(String tittle, String nombreU) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaCliente.class.getResource("/img/ancla.png")));
		setTitle(tittle);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 785, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panelPropio = new JPanel();
		panelPropio.setBounds(141, 49, 310, 310);
		panelPropio.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panelPropio);
		panelPropio.setLayout(new GridLayout(11, 11));
		
		panelEnemigo = new JPanel();
		panelEnemigo.setBounds(461, 49, 310, 310);
		panelEnemigo.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panelEnemigo);
		panelEnemigo.setLayout(new GridLayout(11, 11));
		panelEnemigo.setLayout(new GridLayout(11, 11));
		
		JPanel panelChat = new JPanel();
		panelChat.setBounds(140, 371, 630, 78);
		panelChat.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panelChat);
		panelChat.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 630, 51);
		panelChat.add(scrollPane);
		
		textAreaChat = new JTextArea();
		textAreaChat.setEditable(false);
		
		DefaultCaret caret = (DefaultCaret)textAreaChat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); 
		
		scrollPane.setViewportView(textAreaChat);
		
		textField = new JTextField();
		textField.setBounds(0, 51, 556, 27);
		panelChat.add(textField);
		textField.setColumns(10);
		
		btnEnviar = new JButton("Send");
		btnEnviar.setBounds(555, 51, 76, 27);
		panelChat.add(btnEnviar);
		
		panelChat.getRootPane().setDefaultButton(btnEnviar);
		
		JLabel lblTurno = new JLabel("Turn: ");
		lblTurno.setBounds(10, 11, 47, 20);
		lblTurno.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblTurno);
		
		lblTextoturno = new JLabel("");
		lblTextoturno.setBounds(48, 11, 82, 21);
		lblTextoturno.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblTextoturno);
		
		btnPvP = new JButton("User vs User");
		btnPvP.setBounds(10, 65, 110, 23);
		btnPvP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		contentPane.add(btnPvP);
		
		btnPuntajes = new JButton("Scores");
		btnPuntajes.setBounds(10, 147, 110, 23);
		contentPane.add(btnPuntajes);
		
		btnPvC = new JButton("User vs PC");
		btnPvC.setBounds(10, 105, 110, 23);
		contentPane.add(btnPvC);
		
		btnInstrucciones = new JButton("Instructions");
		btnInstrucciones.setBounds(10, 189, 110, 23);
		contentPane.add(btnInstrucciones);
		
		btnAcerca = new JButton("About");
		btnAcerca.setBounds(10, 231, 110, 23);
		btnAcerca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(btnAcerca);
		
		JLabel lblUsuer = new JLabel("Area User");
		lblUsuer.setBounds(206, 10, 171, 28);
		lblUsuer.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsuer.setFont(new Font("Tahoma", Font.BOLD, 18));
		contentPane.add(lblUsuer);
		
		JLabel lblEnemy = new JLabel("Area Enemy");
		lblEnemy.setBounds(504, 11, 228, 27);
		lblEnemy.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblEnemy.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblEnemy);

		user = nombreU;
		cc = new ControlCliente(this, user);
		btnEnviar.addActionListener(cc);
		btnPvP.addActionListener(cc);
		btnPvC.addActionListener(cc);
		btnPuntajes.addActionListener(cc);
		btnInstrucciones.addActionListener(cc);
		btnAcerca.addActionListener(cc);
	}
	
	/**
	 * Metodo usado para imprimir en la ventana el nombre del usuario que tiene el turno
	 * @param turno: Nombre del usuario
	 */
	public void modificarTurno(String usuarioTurno){
		lblTextoturno.setText(usuarioTurno);
	}
	
	/**
	 * Metodo usado para imprimir mensajes en el chat
	 * @param msg: Mensaje que sera impreso
	 */
	public void imprimirMensaje(String msg){
		textAreaChat.append(msg + "\n");
	}

	/**
	 * @return the cc
	 */
	public ControlCliente getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(ControlCliente cc) {
		this.cc = cc;
	}
	
	/**
	 * Metodo que retorna el string escrito en el JTextField
	 * @return Cadena contenida en el JTextField
	 */
	public String obtenerTextoJTextField(){
		return textField.getText();
	}
	
	/**
	 * Metodo usado para mostrar un mensaje en el JtextArea de chat
	 * @param mensaje
	 */
	public void mensajeEnviado(String mensaje){
		imprimirMensaje(mensaje);
		textField.setText("");
	}
	
	/**
	 * Metodo encargado de agregar los botones al panel del usuario
	 * @param btnzUser: Arreglo bidimensional que contiene los botones que van a ser agregados
	 */
	public void llenarBotonesPanelUsuario(JButton[][] btnzUser){
		for (int i = 0; i < btnzUser.length; i++) {
			for (int j = 0; j < btnzUser.length; j++) {
				panelPropio.add(btnzUser[i][j]);
			}
		}
	}
	
	/**
	 * Metodo encargado de agregar los botones al panel del enemigo
	 * @param btnzEnemy: Arreglo bidimensional que contiene los botones que van a ser agregados
	 */
	public void llenarBotonesPanelEnemigo(JButton[][] btnzEnemy){
		for (int i = 0; i < btnzEnemy.length; i++) {
			for (int j = 0; j < btnzEnemy.length; j++) {
				panelEnemigo.add(btnzEnemy[i][j]);
			}
		}
	}
	
	/**
	 * Metodo usado para limpiar el text field usado para los mensajes de chat
	 */
	public void limpiarTextField(){
		textField.setText("");
	}
	
	/**
	 * Metodo usado para cambiar la propiedad de un boton en el tablero
	 * @param x La coordenada en X de la ubicación del boton en el arreglo
	 * @param y La coordenada en Y de la ubicación del boton en el arreglo
	 * @param usuario El usuario que se debe modificar
	 * @param propiedad El atributo que sera asignado a la propiedad imagen del boton
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public void enviarPropiedad(int x, int y, int usuario, int propiedad) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		cc.cambiarPropiedad(x, y, usuario, propiedad);
	}
	
	/**
	 * Metodo usado para actualizar el tablero del usuario
	 */
	public void actualizarTableroPropio() {
		cc.actualizarTableroUsuario();
	}
	
	/**
	 * Metodo usado para actualizar el tablero del enemigo
	 */
	public void actualizarTableroEnemigo() {
		cc.actualizarTableroEnemigo();
	}
	
	/**
	 * Metodo que hace uso del control para finalizar la partida
	 * @param mensa Objeto que contiene la informacion de la partida
	 */
	public void finalizarPartida(Mensaje mensa){
		cc.finalizarPartida(mensa);
	}
	
	/**
	 * Metodo que hace uso del control para finalizar la partida
	 * @param mensaj Objeto que contiene la informaicon del partida
	 */
	public void finalizarPartidaMaquina(Mensaje mensaj){
		cc.finalizarPartidaMaquina(mensaj);
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return user;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String user) {
		this.user = user;
	}

	/**
	 * @return the btnPvP
	 */
	public JButton getBtnPvP() {
		return btnPvP;
	}

	/**
	 * @param btnPvP the btnPvP to set
	 */
	public void setBtnPvP(JButton btnPvP) {
		this.btnPvP = btnPvP;
	}

	/**
	 * @return the btnPuntajes
	 */
	public JButton getBtnPuntajes() {
		return btnPuntajes;
	}

	/**
	 * @param btnPuntajes the btnPuntajes to set
	 */
	public void setBtnPuntajes(JButton btnPuntajes) {
		this.btnPuntajes = btnPuntajes;
	}

	/**
	 * @return the btnEnviar
	 */
	public JButton getBtnEnviar() {
		return btnEnviar;
	}

	/**
	 * @param btnEnviar the btnEnviar to set
	 */
	public void setBtnEnviar(JButton btnEnviar) {
		this.btnEnviar = btnEnviar;
	}

	/**
	 * @return the btnPvC
	 */
	public JButton getBtnPvC() {
		return btnPvC;
	}

	/**
	 * @param btnPvC the btnPvC to set
	 */
	public void setBtnPvC(JButton btnPvC) {
		this.btnPvC = btnPvC;
	}

	/**
	 * @return the btnInstrucciones
	 */
	public JButton getBtnInstrucciones() {
		return btnInstrucciones;
	}

	/**
	 * @param btnInstrucciones the btnInstrucciones to set
	 */
	public void setBtnInstrucciones(JButton btnInstrucciones) {
		this.btnInstrucciones = btnInstrucciones;
	}

	/**
	 * @return the btnAcerca
	 */
	public JButton getBtnAcerca() {
		return btnAcerca;
	}

	/**
	 * @param btnAcerca the btnAcerca to set
	 */
	public void setBtnAcerca(JButton btnAcerca) {
		this.btnAcerca = btnAcerca;
	}
	
	/**
	 * Metodo usado para deshabilitar un boton
	 * @param boton Variable usada para determinar que boton deshabilitar
	 * 		  1 = btnPvp
	 * 		  2 = btnPvC
	 */
	public void deshabilitarBoton(int boton) {
		if (boton==1) {
			btnPvP.setEnabled(false);
			cc.setModalidad(2);
		}
		if (boton==2) {
			btnPvC.setEnabled(false);
			cc.setModalidad(1);
		}
	}
}