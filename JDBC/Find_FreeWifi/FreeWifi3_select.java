package Find_FreeWifi;

//select data
//���� : �ǽ�1 �д�������� ����� �� ã�� /�ǽ�2 ���� ���� / �ǽ�3 SKT �������� / �ǽ�4 �Ⱦ�� �������� ã��
//sql�� where ���� ���� ����!!

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class FreeWifi3_select {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		Class.forName("com.mysql.cj.jdbc.Driver");	// JDBC ����̹� �ε�

		// MySQL DB�� �α����ϱ� ���� ����
		String localhost = "192.168.23.91:33061";
		String dbname = "kopoctc";
		String dbuser = "root";
		String dbpasswd = "kopo38";

		// JDBC���� ���� com.mysql.cj.jdbc.Driver ��� => cj�� ����.
		Class.forName("com.mysql.cj.jdbc.Driver");

		// connection j.jar������ ��ɻ������ ��ü����
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + localhost + "/" + dbname, dbuser, dbpasswd);

		// connection j.jar������ ��ɻ������ ��ü����2
		Statement stmt = conn.createStatement();
		

		//���� ���� ��ġ(������ġ) : �д���������� ����
		double lat=37.3860521; 	//�д� ����� ������ �Ǽ��� ������ ���� �� �ʱ�ȭ
		double lng =127.1214038;	//�д� ����� �浵�� �Ǽ��� ������ ���� �� �ʱ�ȭ

		
		String QueryTxt; 	//�� ���� ���ڿ� ���� ����
		
		
//		//�ǽ�1] ���� ����� �������� ã��
//		//select �� �ȿ� select���� �־���.
//		
//		/* ********���� ������ ��ǥ������ �Ÿ����*********
//		��Ÿ��� ���� Ȱ��
//		��ǥ(x1,y1), ��ǥ(x2,y2)�� ��� 
//		������� �� ������ ���θ��ּ� ����, �浵 ��
//		��Ʈ{(x1-x2)^2+(y1-y2)^2}
//		SQRT : ��Ʈ �����
//		POWER : ���� ǥ�� => 2�����̶� ���� 2 */
//		
//		//SQRT(POWER(latitude - %f,2) + POWER (longitude - %f,2)) => freewifi ���̺��� ��Ʈ{(x1-x2)^2+(y1-y2)^2} �� ����� ����
//		//select MIN(SQRT(POWER(latitude - %f,2) + POWER(longitude - %f,2))) => min �� ���� ���
		
//		QueryTxt = String.format("select * from freewifi where "
//				+ "SQRT(POWER(latitude - %f,2) + POWER (longitude - %f,2)) = "
//				+ "(select MIN(SQRT(POWER(latitude - %f,2) + POWER(longitude - %f,2))) from freewifi);",lat,lng,lat,lng);

		
		
		//�ǽ�2] �������� ���� ���						
//		QueryTxt = "select * from freewifi";	
		
		//�ǽ�3] SKT �������̸� ���
//		QueryTxt = "select * from freewifi where service_provider='SKT'";	

		//�ǽ�4] �Ⱦ�� �������̸� ���
		QueryTxt = "select * from freewifi where inst_country='�Ⱦ��'";		


		ResultSet rset = stmt.executeQuery(QueryTxt);
		int iCnt=0;
		while(rset.next()) {
			System.out.printf("*(%d)*************************************************************************\n",iCnt++);
			System.out.printf("��ġ��Ҹ�		:%s\n",rset.getString(1));
			System.out.printf("��ġ��һ�		:%s\n",rset.getString(2));
			System.out.printf("��ġ�õ���		:%s\n",rset.getString(3));
			System.out.printf("��ġ�ñ�����		:%s\n",rset.getString(4));
			System.out.printf("��ġ�ü�����		:%s\n",rset.getString(5));
			System.out.printf("�����������		:%s\n",rset.getString(6));
			System.out.printf("��������SSID		:%s\n",rset.getString(7));
			System.out.printf("��ġ���		:%s\n",rset.getString(8));
			System.out.printf("���������θ��ּ�	:%s\n",rset.getString(9));
			System.out.printf("�����������ּ�		:%s\n",rset.getString(10));
			System.out.printf("���������		:%s\n",rset.getString(11));
			System.out.printf("���������ȭ��ȣ	:%s\n",rset.getString(12));
			System.out.printf("����			:%s\n",rset.getString(13));
			System.out.printf("�浵			:%s\n",rset.getString(14));
			System.out.printf("�����ͱ�������		:%s\n",rset.getString(15));
		}
		//�ڿ�����
		rset.close();
		stmt.close(); 
		conn.close(); 


	}

}