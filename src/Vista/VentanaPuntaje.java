package Vista;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import Controlador.ModeloTabla;
import java.awt.Toolkit;
import java.awt.Color;

@SuppressWarnings("serial")
public class VentanaPuntaje extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPuntaje frame = new VentanaPuntaje();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPuntaje() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaPuntaje.class.getResource("/img/puntajes.png")));
		setTitle("Scores");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 11, 474, 300);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblScores = new JLabel("Scores");
		lblScores.setHorizontalAlignment(SwingConstants.CENTER);
		lblScores.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblScores.setBounds(166, 11, 143, 25);
		panel.add(lblScores);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 54, 454, 235);
		panel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
	
	/**
	 * Metodo usado para modificar el modelo de la tabla
	 * @param modelo modelo que contiene la informacion que sera plasmada en la tabla
	 */
	public void cambiarModeloTabla(ModeloTabla modelo) {
		table.setModel(modelo);
	}
}
