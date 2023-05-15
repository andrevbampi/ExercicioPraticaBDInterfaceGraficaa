package curso;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import driver.ConnectionMariaDB;

public class UsuarioBD {
	
	public static void criarTabelaSeNaoExistir() {
		
		//Verificar se a tabela existe. Caso não existir, criar e inserir um usuário padrão.
		if (!tabelaExiste()) {
			try {
				//Iniciando a conexão com o banco de dados
				Connection connection = ConnectionMariaDB.conectar();
				
				//Comando de criar a tabela
				String comandoSql = "CREATE TABLE IF NOT EXISTS exercicioBD.usuario ("
										+ "login VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,"
										+ "senha VARCHAR(50) NOT NULL,"
										+ "nome VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,"
									    + "genero CHAR NOT NULL,"
										+ "administrador BOOLEAN NOT NULL,"
									    + "PRIMARY KEY (login))";
				
				//Preparando e executando o comando SQL
				Statement statement = connection.createStatement();
				statement.execute(comandoSql);
	
				//Fechando a conexão com o banco de dados. Nunca esquecer de fazer isso após terminar de utilizar a conexão.
				connection.close();
				
			} catch (SQLException e) {
				System.out.println("Erro ao inserir usuário: " + e.getMessage());
			}
			
			//Inserindo um usuário padrão para poder entrar no sistema
			Usuario usuario = new Usuario();
			usuario.setLogin("root");
			usuario.setSenha("123");
			usuario.setNome("Root");
			usuario.setAdministrador(true);
			usuario.setGenero('O');
			
			inserir(usuario);
		}
	}
	
	public static boolean tabelaExiste() {
		boolean achou = false;
		try {
			//Iniciando a conexão com o banco de dados
			Connection connection = ConnectionMariaDB.conectar();
			
			String comandoSql = "SELECT table_name"
								+ " FROM information_schema.tables"
								+ " WHERE table_schema = 'exercicioBD'"
								+ " AND table_name = 'usuario'";
			
			//Preparando o comando SQL
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			
			//Executando o comando SQL
			ResultSet rs = ps.executeQuery();
			
			achou = rs.next();

			//Fechando a conexão com o banco de dados. Nunca esquecer de fazer isso após terminar de utilizar a conexão.
			connection.close();
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir usuário: " + e.getMessage());
		}
		return achou;
	}
	
	public static void inserir(Usuario usuario) {
		try {
			//Iniciando a conexão com o banco de dados
			Connection connection = ConnectionMariaDB.conectar();
			
			String comandoSql = "insert into exercicioBD.usuario"
					+ " (login, senha, nome, genero, administrador)"
					+ " values (?, ?, ?, ?, ?)";
			
			//Preparando o comando SQL
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			
			//Substituindo os "?" por valores válidos para serem utilizados no comando SQL
			ps.setString(1, usuario.getLogin());
			ps.setString(2, usuario.getSenha());
			ps.setString(3, usuario.getNome());
			ps.setString(4, String.valueOf(usuario.getGenero()));
			ps.setBoolean(5, usuario.isAdministrador());
			
			//Executando o comando SQL
			ps.execute();
			
			//Fechando a conexão com o banco de dados. Nunca esquecer de fazer isso após terminar de utilizar a conexão.
			connection.close();
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir usuário: " + e.getMessage());
		}
	}
	
	public static List<Usuario> listarTodos() {
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		try {
			Connection connection = ConnectionMariaDB.conectar();
			String comandoSql = "select login, senha, nome, genero, administrador"
					+ " from exercicioBD.usuario order by login";
			
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			
			//ResultSet é utilizado para comandos SQL que tenham resultado, como selects.
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setLogin(rs.getString("login"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setNome(rs.getString("nome"));
				usuario.setGenero(rs.getString("genero").charAt(0));
				usuario.setAdministrador(rs.getBoolean("administrador"));
				listaUsuarios.add(usuario);
			}
			
			connection.close();
		} catch (SQLException e) {
			System.out.println("Erro ao listar usuários: " + e.getMessage());
		}
		return listaUsuarios;
	}
	
	public static void excluir(String login) {
		try {
			Connection connection = ConnectionMariaDB.conectar();
			
			String comandoSql = "delete from exercicioBD.usuario"
					+ " where login = ?";
			
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			ps.setString(1, login);
			
			if (ps.executeUpdate() > 0) {
				System.out.println("Usuário excluído com sucesso.");
			} else {
				System.out.println("Login inválido.");
			}
			
			connection.close();
		} catch (SQLException e) {
			System.out.println("Erro ao excluir usuário: " + e.getMessage());
		}
	}
	
	public static Usuario buscarPorLogin(String login) {
		Usuario usuario = null;
		try {
			Connection connection = ConnectionMariaDB.conectar();
			
			String comandoSql = "select login, senha, nome, genero, administrador"
					+ " from exercicioBD.usuario where login = ?";
			
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			
			ps.setString(1, login);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				usuario = new Usuario();
				usuario.setLogin(rs.getString("login"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setNome(rs.getString("nome"));
				usuario.setGenero(rs.getString("genero").charAt(0));
				usuario.setAdministrador(rs.getBoolean("administrador"));
			}
			connection.close();
		} catch (SQLException e) {
			System.out.println("Erro ao buscar usuário por login: " + e.getMessage());
		}
		return usuario;
	}
	
	public static void editar(Usuario usuario) {
		try {
			Connection connection = ConnectionMariaDB.conectar();
			String comandoSql = "update exercicioBD.usuario"
					+ " set senha = ?, nome = ?, genero = ?, administrador = ?"
					+ " where login = ?";
			
			PreparedStatement ps = connection.prepareStatement(comandoSql);
			
			ps.setString(1, usuario.getSenha());
			ps.setString(2, usuario.getNome());
			ps.setString(3, String.valueOf(usuario.getGenero()));
			ps.setBoolean(4, usuario.isAdministrador());
			ps.setString(5, usuario.getLogin());
			
			ps.execute();
			
			connection.close();
		} catch (SQLException e) {
			System.out.println("Erro ao editar usuário: " + e.getMessage());
		}
	}
}
