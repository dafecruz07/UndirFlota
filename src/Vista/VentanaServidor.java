package Vista;
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

import Controlador.ControlServidor;
import Modelo.HiloCliente;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 * Clase: VentanaPrincipal
 * Clase encargada de gestionar lo relacionado con lo visual del servidor y los clientes 
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
@SuppressWarnings("serial")
public class VentanaServidor extends JFrame /**implements ActionListener*/ {

	private JPanel contentPane;
	private JPanel panelPropio;
	private JPanel panelEnemigo;
	private JTextArea textAreaChat;
	private JTextField textField;
	private JButton btnEnviar;
	public ControlServidor controlServer;
	private JLabel lblAl;
	private JLabel lblServidor;

	/**
	 * Metodo constructor de la ventana
	 * @param tittle: variable usada para el titulo de la ventana.
	 */
	public VentanaServidor(String tittle) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaServidor.class.getResource("/img/servidor.png")));
		setTitle(tittle);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 785, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panelPropio = new JPanel();
		panelPropio.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelPropio.setBounds(140, 11, 310, 310);
		contentPane.add(panelPropio);
		panelPropio.setLayout(new GridLayout(11, 11));
		
		panelEnemigo = new JPanel();
		panelEnemigo.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelEnemigo.setBounds(460, 11, 310, 310);
		contentPane.add(panelEnemigo);
		panelEnemigo.setLayout(new GridLayout(11, 11));
		panelEnemigo.setLayout(new GridLayout(11, 11));
		
		JPanel panelChat = new JPanel();
		panelChat.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelChat.setBounds(140, 332, 630, 78);
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
		btnEnviar.setBounds(554, 51, 76, 27);
		panelChat.add(btnEnviar);
		
		JLabel lblBienvenido = new JLabel("\u00A1Welcome");
		lblBienvenido.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblBienvenido.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenido.setBounds(10, 75, 113, 37);
		contentPane.add(lblBienvenido);
		
		lblAl = new JLabel("to the");
		lblAl.setHorizontalAlignment(SwingConstants.CENTER);
		lblAl.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAl.setBounds(34, 111, 69, 14);
		contentPane.add(lblAl);
		
		lblServidor = new JLabel("Server!");
		lblServidor.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblServidor.setHorizontalAlignment(SwingConstants.CENTER);
		lblServidor.setBounds(27, 129, 88, 30);
		contentPane.add(lblServidor);
		
		controlServer = new ControlServidor(this);
		btnEnviar.addActionListener(controlServer);
		
		panelChat.getRootPane().setDefaultButton(btnEnviar);
	}
	
	/**
	 * Metodo usado para imprimir mensajes en el chat
	 * @param msg: Mensaje que sera impreso
	 */
	public void imprimirMensaje(String msg){
		textAreaChat.append(msg + "\n");
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
	public void llenarBotonesPanelUsuario1(JButton[][] btnzUser){
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
	public void llenarBotonesPanelUsuario2(JButton[][] btnzEnemy){
		for (int i = 0; i < btnzEnemy.length; i++) {
			for (int j = 0; j < btnzEnemy.length; j++) {
				panelEnemigo.add(btnzEnemy[i][j]);
			}
		}
	}

	/**
	 * @return the controlServer
	 */
	public ControlServidor getControlServer() {
		return controlServer;
	}

	/**
	 * @param controlServer the controlServer to set
	 */
	public void setControlServer(ControlServidor controlServer) {
		this.controlServer = controlServer;
	}
	
	/**
	 * Metodo usado para limpiar el text field usado para los mensajes de chat
	 */
	public void limpiarTextField(){
		textField.setText("");
	}

	/**
	 * @return the btnEnviar
	 */
	public JButton getBtnEnviar() {
		return btnEnviar;
	}
	
	/**
	 * 
	 * @param arrayUsuarios
	 */
	public void setArregloHilos(ArrayList<HiloCliente> arrayUsuarios) {
		controlServer.setArrayUsuarios(arrayUsuarios);
	}

	/**
	 * @param btnEnviar the btnEnviar to set
	 */
	public void setBtnEnviar(JButton btnEnviar) {
		this.btnEnviar = btnEnviar;
	}
}
