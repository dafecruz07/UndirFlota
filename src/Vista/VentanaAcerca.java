package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;

import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.JTextPane;

import java.awt.Toolkit;

@SuppressWarnings("serial")
public class VentanaAcerca extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaAcerca frame = new VentanaAcerca();
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
	public VentanaAcerca() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaAcerca.class.getResource("/img/sobre.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("About BattleShip");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setBounds(4, 13, 574, 34);
		contentPane.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(11, 49, 561, 302);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JTextPane txtpnBattleshiporSea = new JTextPane();
		txtpnBattleshiporSea.setText("Battleship (or Sea Battle) is a guessing game for two players.");
		txtpnBattleshiporSea.setBounds(11, 8, 514, 26);
		txtpnBattleshiporSea.setEditable(false);
		panel.add(txtpnBattleshiporSea);
		
		JTextPane txtpnTheGameOf = new JTextPane();
		txtpnTheGameOf.setText("The game of Battleship is thought to have its origins in the French game L'Attaque played during World War I, although parallels have also been drawn to E. I. Horseman's 1890 game Baslinda, and the game is said to have been played by Russian officers before World War I.The first commercial version of the game was Salvo, published in 1931 in the United States by the Starex company. Other versions of the game were printed in the 1930s and 1940s.");
		txtpnTheGameOf.setBounds(9, 28, 539, 82);
		txtpnTheGameOf.setEditable(false);
		panel.add(txtpnTheGameOf);
		
		JTextPane txtpnInMilton = new JTextPane();
		txtpnInMilton.setText("In 1967 Milton Bradley introduced a version of the game that used plastic boards and pegs. In 1977, Milton Bradley also released a computerized Electronic Battleship, followed in 1989 by Electronic Talking  Battleship. In 2010, an updated version of Battleship was released, using hexagonal tiles.");
		txtpnInMilton.setBounds(10, 109, 539, 49);
		txtpnInMilton.setEditable(false);
		panel.add(txtpnInMilton);
		
		JTextPane txtpnBattleshipWasOne = new JTextPane();
		txtpnBattleshipWasOne.setText("Battleship was one of the earliest games to be produced as a computer game, with a version being released for the Z80 Compucolor in 1979.");
		txtpnBattleshipWasOne.setBounds(10, 157, 533, 54);
		txtpnBattleshipWasOne.setEditable(false);
		panel.add(txtpnBattleshipWasOne);
		
		JTextPane txtpnThisGameWas = new JTextPane();
		txtpnThisGameWas.setFont(new Font("Tahoma", Font.ITALIC, 11));
		txtpnThisGameWas.setText("This game was developed in 2015 by: Danny Fernando Cruz Arango and Maria Alejandra Aguiar Vasquez. \u00A1Thanks for playing!.");
		txtpnThisGameWas.setBounds(14, 246, 525, 45);
		txtpnThisGameWas.setEditable(false);
		panel.add(txtpnThisGameWas);
	}

}
