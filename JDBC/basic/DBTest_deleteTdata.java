package DBTest;
//import�� �� java.sql�� �� ��.

//���̺� �ڷ� ���� �⺻ ����
//�ܰ� : Connection - Statement - ResultSet
//�ܼ�â�� ����� ��µȴٸ�, ResultSet rset = stmt.executeQuery ���
//�׷��� �ʴٸ�, execute

//executeQuery : select
//execute : create, delete


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest_deleteTdata {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		// MySQL DB�� �α����ϱ� ���� ����
		String localhost = "192.168.23.91:33060";
		String dbname = "kopoctc";
		String dbuser = "root";
		String dbpasswd = "kopo38";

		// JDBC���� ���� com.mysql.cj.jdbc.Driver ���
		Class.forName("com.mysql.cj.jdbc.Driver");

		// connection j.jar������ ��ɻ������ ��ü����
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + localhost + "/" + dbname, dbuser, dbpasswd);

		// connection j.jar������ ��ɻ������ ��ü����2
		Statement stmt = conn.createStatement();

		// ���� ResultSet rset = stmt.executeQuery("select * from table");
		// ResultSet �� �ʿ����.
		// �ܼ�â�� ��� ����ϴ°� �ƴ϶� �����ͺ��̽� ���̺��� �ڷḦ �����ϴ� ���̶� execute �Լ� ���
		//executeQuery : select
		//execute : create, delete
		stmt.execute("delete from examtable;");
		
		//�ڿ�����
		stmt.close();
		conn.close();
	} // main
} // class
