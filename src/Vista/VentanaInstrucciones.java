package Vista;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class VentanaInstrucciones extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaInstrucciones frame = new VentanaInstrucciones();
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
	public VentanaInstrucciones() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaInstrucciones.class.getResource("/img/instrucciones.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblInstrucciones = new JLabel("Instructions");
		lblInstrucciones.setBounds(4, 17, 574, 30);
		lblInstrucciones.setHorizontalAlignment(SwingConstants.CENTER);
		lblInstrucciones.setFont(new Font("Tahoma", Font.BOLD, 18));
		contentPane.add(lblInstrucciones);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(14, 50, 554, 301);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JTextPane txtpnEachPlayerPlaces = new JTextPane();
		txtpnEachPlayerPlaces.setText("* Each player places their ships in the squares of the board (Each player has his own board). The dashboard of a player can not be seen by the other player.");
		txtpnEachPlayerPlaces.setBounds(13, 10, 529, 38);
		txtpnEachPlayerPlaces.setEditable(false);
		panel.add(txtpnEachPlayerPlaces);
		
		JTextPane txtpnInitially = new JTextPane();
		txtpnInitially.setText("* Initially, the first player must click on the coordinate of the enemy board where he / she wants to shoot.");
		txtpnInitially.setBounds(12, 70, 530, 36);
		txtpnInitially.setEditable(false);
		panel.add(txtpnInitially);
		
		JTextPane txtpnIfIn = new JTextPane();
		txtpnIfIn.setText("* If in the coordinate finds a ship or part of it, then the ship receives hurt and the player can shoot again.");
		txtpnIfIn.setBounds(14, 107, 530, 36);
		txtpnIfIn.setEditable(false);
		panel.add(txtpnIfIn);
		
		JTextPane txtpnTurn = new JTextPane();
		txtpnTurn.setText("* If in the coordinates does not find a ship, then the player will have failed the shot will be the next player's turn.");
		txtpnTurn.setBounds(14, 141, 529, 36);
		txtpnTurn.setEditable(false);
		panel.add(txtpnTurn);
		
		JTextPane txtpnWhenA = new JTextPane();
		txtpnWhenA.setText("* When a boat has received damage in each coordinate it occupies, then the ship will be sunk.");
		txtpnWhenA.setBounds(14, 177, 529, 20);
		txtpnWhenA.setEditable(false);
		panel.add(txtpnWhenA);
		
		JTextPane txtpnAllThe = new JTextPane();
		txtpnAllThe.setText("* All the coordinates are marked in the board, in order that they do not repeat themselves.");
		txtpnAllThe.setBounds(15, 197, 529, 21);
		txtpnAllThe.setEditable(false);
		panel.add(txtpnAllThe);
		
		JTextPane txtpnWinsThe = new JTextPane();
		txtpnWinsThe.setText("* Wins the player who sinks first 5 ships of his enemy.");
		txtpnWinsThe.setBounds(14, 218, 529, 20);
		txtpnWinsThe.setEditable(false);
		panel.add(txtpnWinsThe);
		
		JLabel lblWater = new JLabel("");
		lblWater.setIcon(new ImageIcon(VentanaInstrucciones.class.getResource("/img/agua.jpg")));
		lblWater.setBounds(30, 260, 30, 30);
		panel.add(lblWater);
		
		JLabel lblNewLabel = new JLabel("Only water");
		lblNewLabel.setBounds(64, 270, 61, 14);
		panel.add(lblNewLabel);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(VentanaInstrucciones.class.getResource("/img/fallido.jpg")));
		label.setBounds(128, 260, 30, 30);
		panel.add(label);
		
		JLabel lblFailedShoot = new JLabel("Missed shot");
		lblFailedShoot.setBounds(162, 270, 78, 14);
		panel.add(lblFailedShoot);
		
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(VentanaInstrucciones.class.getResource("/img/disparo.jpg")));
		label_1.setBounds(244, 260, 30, 30);
		panel.add(label_1);
		
		JLabel lblSuccessfulShot = new JLabel("Successful shot");
		lblSuccessfulShot.setBounds(280, 270, 95, 14);
		panel.add(lblSuccessfulShot);
		
		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(VentanaInstrucciones.class.getResource("/img/destruido.jpg")));
		label_2.setBounds(386, 260, 30, 30);
		panel.add(label_2);
		
		JLabel lblDestroyedShip = new JLabel("Destroyed ship ");
		lblDestroyedShip.setBounds(423, 270, 97, 14);
		panel.add(lblDestroyedShip);
		
		JTextPane txtpnToChange = new JTextPane();
		txtpnToChange.setText("* To change a ship's orientation just press right click inside your board.");
		txtpnToChange.setBounds(13, 47, 495, 20);
		panel.add(txtpnToChange);
		
		
	}
}
