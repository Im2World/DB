package DBTest;
//import�� �� java.sql�� �� ��.

//���̺� ���� �⺻ ����
//�ܰ� : Connection - Statement - ResultSet
//�ܼ�â�� ����� ��µȴٸ�, ResultSet rset = stmt.executeQuery ���
//�׷��� �ʴٸ�, execute

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest_makeT {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		// MySQL DB�� �α����ϱ� ���� ����
		String localhost = "192.168.23.91:33060";
		String dbname = "kopoctc";
		String dbuser = "root";
		String dbpasswd = "kopo38";

		// JDBC���� ���� com.mysql.cj.jdbc.Driver ��� => cj�� ����.
		Class.forName("com.mysql.cj.jdbc.Driver");

		// connection j.jar������ ��ɻ������ ��ü����
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + localhost + "/" + dbname, dbuser, dbpasswd);

		// connection j.jar������ ��ɻ������ ��ü����2
		Statement stmt = conn.createStatement();

		// ���� ResultSet rset = stmt.executeQuery("select * from table");
		// ���̺� ���� �ÿ��� ResultSet �� �ʿ����.
		// �ܼ�â�� ��� ����ϴ°� �ƴ϶� �����ͺ��̽� ���̺��� ����� ���̶� execute �Լ� ���
		//executeQuery -> execute
		stmt.execute("create table examtable("
				+ "name varchar(20),"
				+ "studentid int not null primary key,"
				+ "kor int,"
				+ "eng int,"
				+ "mat int)"
				+ "DEFAULT CHARSET=utf8;");
		//�ڿ�����		
		stmt.close();
		conn.close();
	} // main
} // class
