/**
 * @author Maria Alejandra Aguiar Vasquez - 1455775
 * @author Danny Fernando Cruz Arando - 1449949
 */
package Controlador;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ModeloTabla extends DefaultTableModel{
	
	public ModeloTabla(Object datos[][], Object encabezados[]){
		super(datos, encabezados);
	}
}
