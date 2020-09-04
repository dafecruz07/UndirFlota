package Modelo;
import java.io.Serializable;
import javax.swing.JButton;
import Controlador.ModeloTabla;

/**
 * Clase encargada de gestionar los mensajes es serializable ya que este tipo de objetos es enviado usando sockets
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
@SuppressWarnings("serial")
public class Mensaje implements Serializable{
	private String usuario=null;
	private String mensaje =null;
	private int tipo;
	private JButton[][] btnzUser;
	private Barco[] flota;					// Arreglo que contiene los barcos
	private Coordenada coord;
	private int propiedad;
	private String usuarioTurno;
	private int modalidad;
	private ModeloTabla modelo;
	
	/**
	 * Metodo constructor de mensaje
	 * @param usuario
	 * @param mensaje
	 * @param tipo
	 */
	public Mensaje(String usuario, String mensaje, int tipo) {
		this.usuario = usuario;
		this.mensaje = mensaje;
		this.tipo = tipo;
	}

	public Mensaje(String usuario) {
		this.usuario=usuario;
	}
	
	/**
	 * @param usuario
	 * @param tipo
	 * @param btnzUser
	 */
	public Mensaje(String usuario, int tipo, JButton[][] btnzUser) {
		this.usuario = usuario;
		this.tipo = tipo;
		this.btnzUser = btnzUser;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String toString() {
		return mensaje;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the btnzUser
	 */
	public JButton[][] getBtnzUser() {
		return btnzUser;
	}

	/**
	 * @param btnzUser the btnzUser to set
	 */
	public void setBtnzUser(JButton[][] btnzUser) {
		this.btnzUser = btnzUser;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @return the flota
	 */
	public Barco[] getFlota() {
		return flota;
	}

	/**
	 * @param flota the flota to set
	 */
	public void setFlota(Barco[] flota) {
		this.flota = flota;
	}

	/**
	 * @return the coord
	 */
	public Coordenada getCoord() {
		return coord;
	}

	/**
	 * @param coord the coord to set
	 */
	public void setCoord(Coordenada coord) {
		this.coord = coord;
	}

	/**
	 * @return the propiedad
	 */
	public int getPropiedad() {
		return propiedad;
	}

	/**
	 * @param propiedad the propiedad to set
	 */
	public void setPropiedad(int propiedad) {
		this.propiedad = propiedad;
	}

	/**
	 * @return the turno
	 */
	public String getUsuarioTurno() {
		return usuarioTurno;
	}

	/**
	 * @param turno the turno to set
	 */
	public void setUsuarioTurno(String usuarioTurno) {
		this.usuarioTurno = usuarioTurno;
	}

	/**
	 * @return the modalidad
	 */
	public int getModalidad() {
		return modalidad;
	}

	/**
	 * @param modalidad the modalidad to set
	 */
	public void setModalidad(int modalidad) {
		this.modalidad = modalidad;
	}

	public ModeloTabla getModelo() {
		return modelo;
	}

	public void setModelo(ModeloTabla modelo) {
		this.modelo = modelo;
	}
}