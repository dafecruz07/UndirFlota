package Modelo;

import java.io.Serializable;

/**
 * Clase encargada de gestionar las coordenadas que son usadas para los disparos y posiciones de los barcos
 * Extiende de Threat
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arango     - 1449949
 */
@SuppressWarnings("serial")
public class Coordenada implements Serializable{
	public int x, y, disparo;

	/**
	 * @param x
	 * @param y
	 */
	public Coordenada(int x, int y) {
		this.x = x;
		this.y = y;
		disparo = 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Coordenada [x=" + x + ", y=" + y + ", disparo=" + disparo + "]";
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the disparo
	 */
	public int getDisparo() {
		return disparo;
	}

	/**
	 * @param disparo the disparo to set
	 */
	public void setDisparo(int disparo) {
		this.disparo = disparo;
	}
	
	
}
