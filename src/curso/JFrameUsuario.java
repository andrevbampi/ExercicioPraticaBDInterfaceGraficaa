package curso;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JFrameUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txfNome;
	private JTable tblUsuarios;
	private JTextField txfLogin;
	private JPasswordField pwdSenha;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameUsuario frame = new JFrameUsuario();
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
	public JFrameUsuario() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 501, 478);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setBounds(26, 63, 47, 14);
		contentPane.add(lblSenha);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(26, 93, 47, 14);
		contentPane.add(lblNome);
		
		txfNome = new JTextField();
		txfNome.setColumns(10);
		txfNome.setBounds(75, 90, 96, 20);
		contentPane.add(txfNome);
		
		JCheckBox cbxAdministrador = new JCheckBox("Administrador?");
		cbxAdministrador.setBounds(22, 150, 132, 21);
		contentPane.add(cbxAdministrador);
		
		JComboBox<String> cmbGenero = new JComboBox<String>();
		cmbGenero.setModel(new DefaultComboBoxModel<String>(new String[] {"Masculino", "Feminino", "Outro"}));
		cmbGenero.setBounds(75, 120, 96, 21);
		contentPane.add(cmbGenero);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Usuario usuario = new Usuario();
				usuario.setLogin(txfLogin.getText());
				usuario.setSenha(String.valueOf(pwdSenha.getPassword()));
				usuario.setNome(txfNome.getText());
				usuario.setGenero(getLetraGenero(cmbGenero.getSelectedIndex()));
				usuario.setAdministrador(cbxAdministrador.isSelected());

				if (UsuarioBD.buscarPorLogin(usuario.getLogin()) == null) {
					UsuarioBD.inserir(usuario);
				} else {
					UsuarioBD.editar(usuario);
				}

				tblUsuarios.setModel(listarTodos());
				
				txfLogin.setText("");
				pwdSenha.setText("");
				txfNome.setText("");
				cbxAdministrador.setSelected(false);
				txfLogin.requestFocus();
			}
		});
		btnSalvar.setBounds(26, 180, 89, 23);
		contentPane.add(btnSalvar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 219, 460, 204);
		contentPane.add(scrollPane);
		
		tblUsuarios = new JTable();
		tblUsuarios.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					String login = tblUsuarios.getModel().getValueAt(tblUsuarios.getSelectedRow(), 0).toString();
					Usuario usuario = UsuarioBD.buscarPorLogin(login);
					if (usuario != null) {
						txfLogin.setText(usuario.getLogin());
						pwdSenha.setText("");
						txfNome.setText(usuario.getNome());
						cmbGenero.setSelectedIndex(getIndexGenero(usuario.getGenero()));
						cbxAdministrador.setSelected(usuario.isAdministrador());
					}
		        }
			}
		});
		scrollPane.setViewportView(tblUsuarios);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(26, 33, 47, 14);
		contentPane.add(lblLogin);
		
		txfLogin = new JTextField();
		txfLogin.setColumns(10);
		txfLogin.setBounds(75, 30, 96, 20);
		contentPane.add(txfLogin);
		
		JLabel lblGenero = new JLabel("Gênero");
		lblGenero.setBounds(26, 123, 47, 14);
		contentPane.add(lblGenero);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tblUsuarios.getSelectedRow() != -1) {
					String login = tblUsuarios.getModel().getValueAt(tblUsuarios.getSelectedRow(), 0).toString();
					Object[] options = { "Sim", "Não" };
					int n = JOptionPane.showOptionDialog(null,
							"Tem certeza que deseja excluir o usuário " + login + "?",
							"Excluir usuário", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					
					if (n == 0) {
						UsuarioBD.excluir(login);
						tblUsuarios.setModel(listarTodos());
					}
				}
				//UsuarioBD.excluir(txfLogin.getText());
			}
		});
		btnExcluir.setBounds(126, 180, 89, 23);
		contentPane.add(btnExcluir);
		
		pwdSenha = new JPasswordField();
		pwdSenha.setBounds(75, 60, 96, 19);
		contentPane.add(pwdSenha);
		
		tblUsuarios.setDefaultEditor(Object.class, null);
		tblUsuarios.setModel(listarTodos());
	}
	
	private DefaultTableModel listarTodos() {
		DefaultTableModel dados = new DefaultTableModel();
		
		dados.addColumn("Login");
		dados.addColumn("Nome");
		dados.addColumn("Gênero");
		dados.addColumn("Administrador?");
		
		for (Usuario usuario : UsuarioBD.listarTodos()) {
			dados.addRow(new Object[] {usuario.getLogin(),
									   usuario.getNome(),
									   getDescricaoGenero(usuario.getGenero()),
									   usuario.isAdministrador() ? "Sim" : "Não"});
		}
		
		return dados;
	}
	
	private String getDescricaoGenero(char genero) {
		switch (genero) {
			case 'M': return "Masculino";
			case 'F': return "Feminino";
			default: return "Outro";
		}
	}
	
	private char getLetraGenero(int selectedIndex) {
		switch (selectedIndex) {
			case 0: return 'M';
			case 1: return 'F';
			default: return 'O';
		}
	}
	
	private int getIndexGenero(char genero) {
		switch (genero) {
		case 'M': return 0;
		case 'F': return 1;
		default: return 2;
	}
	}
	
	
}
