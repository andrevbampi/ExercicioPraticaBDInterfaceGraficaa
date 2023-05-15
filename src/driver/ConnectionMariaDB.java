package driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMariaDB {
	
	private static String serverName = "localhost"; //Endereço do servidor. Se for a própria máquina, localhost. 
													//Se for máquina remota, IP do servidor;
	private static String database = "exercicioBD"; //Nome do banco de dados/schema criado
	private static String url = "jdbc:mariadb://" + serverName + "/" + database; //Montando a URL de conexão
	private static String username = "root"; //Nome do usuário do banco de dados
	private static String password = "135790"; //Senha do usuário, se for o usuário root a senha foi 
											   //cadastrada na instalação do MariaDB
	
	public static Connection conectar() throws SQLException {
		try {
			//Procurando e inicializando o driver do MariaDB
			String driverName = "org.mariadb.jdbc.Driver";
			Class.forName(driverName);
			
			//Iniciando a conexão com o banco de dados
			Connection retornoConexao = DriverManager.getConnection(url, username, password);
			return retornoConexao;
		} catch (ClassNotFoundException e) {
			System.out.println("O driver especificado não foi encontrado.");
		}
		return null;
	}
}


