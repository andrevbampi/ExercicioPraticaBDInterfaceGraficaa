package curso;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JFrameLogin extends JFrame {

	private JPanel contentPane;
	private JTextField txfLogin;
	private JPasswordField pwdSenha;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameLogin frame = new JFrameLogin();
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
	public JFrameLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 199, 185);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(21, 28, 47, 14);
		contentPane.add(lblLogin);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(21, 58, 47, 14);
		contentPane.add(lblSenha);
		
		txfLogin = new JTextField();
		txfLogin.setColumns(10);
		txfLogin.setBounds(70, 25, 96, 20);
		contentPane.add(txfLogin);
		
		pwdSenha = new JPasswordField();
		pwdSenha.setBounds(70, 55, 96, 19);
		contentPane.add(pwdSenha);
		
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txfLogin.getText().trim().equals("")) {
					Usuario usuario = UsuarioBD.buscarPorLogin(txfLogin.getText());
					if (usuario == null) {
						JOptionPane.showMessageDialog(null, "Usuário inválido.");
					} else {
						if (usuario.getSenha().trim().equals(String.valueOf(pwdSenha.getPassword()).trim())) {
							new JFrameUsuario().setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(null, "Senha inválida.");
						}
					}
				}
			}
		});
		btnEntrar.setBounds(58, 99, 89, 23);
		contentPane.add(btnEntrar);
		
		UsuarioBD.criarTabelaSeNaoExistir();
	}

}
