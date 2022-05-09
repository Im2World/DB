package DBTest;
//import�� �� java.sql�� �� ��.

//���̺� ���ڵ� ���� �⺻ ����
//�ܰ� : Connection - Statement - ResultSet
//�ܼ�â�� ����� ��µȴٸ�, ResultSet rset = stmt.executeQuery ���
//�׷��� �ʴٸ�, execute

//executeQuery : select
//execute : create, delete


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest_InsertTrecords {

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
		// ResultSet �� �ʿ����.
		// �ܼ�â�� ��� ����ϴ°� �ƴ϶� �����ͺ��̽� ���̺� �ڷ� �Է��ϴ� ���̶� execute �Լ� ���
		//executeQuery : select
		//execute : create, delete, insert
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values (\"����\", 209901, 95, 100, 95);");
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values (\"����\", 209902, 100, 100, 100);");
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values (\"���\", 209903, 100, 90, 100);");
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values (\"�糪\", 209904, 100, 95, 90);");
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values (\"��ȿ\", 209905, 80, 100, 70);");
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values (\"�̳�\", 209906, 95, 90, 95);");
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values (\"����\", 209907, 100, 90, 100);");
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values (\"ä��\", 209908, 100, 75, 90);");
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values (\"����\", 209909, 100, 100, 70);");

		//�ڿ�����		
		stmt.close();
		conn.close();
	} // main
} // class
