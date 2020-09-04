package Modelo;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Clase encargada de gestionar lo relacionado con los barcos
 * Es serializable ya que es necesaria eviar por sockets objetos de este tipo
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
@SuppressWarnings("serial")
public class Barco implements Serializable{
	public boolean orientacion;
	public Coordenada[] posiciones;
	public String nombre;
	public boolean hundido;

	public Barco(int tamanio, String nombre, boolean orientacion, int x, int y) {
		posiciones = new Coordenada[tamanio];
		this.nombre = nombre;
		this.orientacion=orientacion;
		
		if (orientacion) {
			for (int i = 0; i < tamanio; i++) {
				posiciones[i]=(new Coordenada(x, y+i));
			}	
		}else{
			for (int i = 0; i < tamanio; i++) {
				posiciones[i]=(new Coordenada(x+i, y));
			}
		}
	}
	
	/**
	 * Metodo que determina si una flota tiene todas sus posiciones con disparo
	 * @return true En caso de que el barco tenga todas las posiciones con disparo
	 * @return flase En caso de que el barco tenga posiciones aun sin disparo
	 */
	public boolean flotaHundida() {
		boolean aux = true;
		for (int i = 0; i < posiciones.length; i++) {
			if (posiciones[i].getDisparo()==0) {
				aux = false;
			}
		}
		return aux;
	}

	/**
	 * Metodo usado mostrar la informacion de un objeto Barco en un String
	 * @return String del objeto
	 */
	public String toString() {
		return "Barco [orientacion=" + orientacion + ", posiciones="
				+ Arrays.toString(posiciones) + ", nombre=" + nombre + "]";
	}

	/**
	 * Metodo que retorna la orientacion
	 * @return la orientacion
	 */
	public boolean getOrientacion() {
		return orientacion;
	}

	/**
	 * Metodo para asignar un valor a irientacion
	 * @param orientacion El valor a asigngar
	 */
	public void setOrientacion(boolean orientacion) {
		this.orientacion = orientacion;
	}

	/**
	 * Metodo que retorna el arreglo de posiciones
	 * @return El arreglo de posiciones
	 */
	public Coordenada[] getPosiciones() {
		return posiciones;
	}

	/**
	 * Metodo para asignar un arreglo de posiciones
	 * @param posiciones El objeto a asignar
	 */
	public void setPosiciones(Coordenada[] posiciones) {
		this.posiciones = posiciones;
	}

	/**
	 * Metodo que retorna el nombre
	 * @return el nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Metodo usado para asignar un valor a la variable nombre
	 * @param nombre El nombre a asignar
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	
}