package Modelo;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

import Vista.VentanaCliente;
import Vista.VentanaPuntaje;

/**
 * Clase: EscuchadorServidor
 * Clase encargada de escuchar los mensajes del servidor y agregarlos a su respectivo panel o area 
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
class EscuchadorServidor extends Thread {
	private ObjectInputStream streamInput;
	private VentanaCliente ventana;
	private VentanaPuntaje ventanaPuntos;
	
	/**
	 * Contructor de la clase Escuchador Servidor
	 * @param streamInput	Objeto usado para recibir informacion
	 * @param ventana		Ventana en la cual sera impresa dicha información
	 */
	public EscuchadorServidor(ObjectInputStream streamInput, VentanaCliente ventana, VentanaPuntaje ventanaPuntos) {
		this.streamInput = streamInput;
		this.ventana = ventana;
		this.ventanaPuntos = ventanaPuntos;
	}

	public void run() {
		while(true) {
			try {
				Mensaje msg = (Mensaje) streamInput.readObject();
				if (msg.getTipo()==1) {
					ventana.imprimirMensaje(msg.getUsuario() + " says: " + msg.getMensaje());
				}
				if (msg.getTipo()==3) {
					if (ventana.getUsername().equals(msg.getUsuario())) {
						ventana.enviarPropiedad(msg.getCoord().getX(), msg.getCoord().getY(), 1, msg.getPropiedad());
						ventana.actualizarTableroEnemigo();
					}else{
						ventana.enviarPropiedad(msg.getCoord().getX(), msg.getCoord().getY(), 0, msg.getPropiedad());
						ventana.actualizarTableroPropio();
					}
				}
				if (msg.getTipo()==4) {
					ventana.finalizarPartida(msg);
				}
				if (msg.getTipo()==5) {
					ventana.modificarTurno(msg.getUsuarioTurno());
				}
				
				if (msg.getTipo()==6) {
					if (msg.getModalidad()==1) {
						ventana.deshabilitarBoton(2);
					}
				}
				if (msg.getTipo()==7) {
					ventanaPuntos.cambiarModeloTabla(msg.getModelo());
					ventanaPuntos.setVisible(true);
				}if(msg.getTipo()==8){
					ventana.finalizarPartidaMaquina(msg);
				}
			}
			catch(IOException e) {
				JOptionPane.showMessageDialog(ventana, "Se ha cerrado la conexion con el servidor: " + e.getMessage());
				break;
			}
			catch(ClassNotFoundException e2) {
				JOptionPane.showMessageDialog(ventana, e2.getMessage());
				break;
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
				break;
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	/**
	 * @return the streamInput
	 */
	public ObjectInputStream getStreamInput() {
		return streamInput;
	}

	/**
	 * @param streamInput the streamInput to set
	 */
	public void setStreamInput(ObjectInputStream streamInput) {
		this.streamInput = streamInput;
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
}