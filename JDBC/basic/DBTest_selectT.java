package DBTest;
//import�� �� java.sql�� �� ��.

//�⺻ �����̴�!
//���̺� ������ ��ȸ
//�ܰ� : Connection - Statement - ResultSet
//�ܼ�â�� ����� ��µȴٸ�, ResultSet rset = stmt.executeQuery ���
//�׷��� �ʴٸ�, execute

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest_selectT {

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
		ResultSet rset = stmt.executeQuery("select * from examtable;"); // ����� ������ �ִ� executeQuery�Լ��� ����

		// ����� ó��
		// ���� : sql�� �ε����� 1���� �����Ѵ�.
		//rset.next �� ���� ���ڵ带 �д´�.
		System.out.printf("  �̸�	 �й� 	����   ����    ����\n");
		while (rset.next()) {
			System.out.printf("%4s	%6d	%3d	%3d	%3d \n", 
					rset.getString(1), rset.getInt(2), rset.getInt(3), rset.getInt(4), rset.getInt(5));//�� �ش� ���� �ش��ϴ� string�� int�� ���� ���
		}
		//�ڿ�����
		rset.close();
		stmt.close();
		conn.close();
	} // main
} // class
