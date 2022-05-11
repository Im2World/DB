package stock;
//select
//�׸�� ���� �ʿ�

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 *  Training : ���ں� �ְ� ������������ �ǽ�, ���̺� �����
 */

public class Stock3_select {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		
		/*ó���ð��� ����� ������*/
		Date start = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		// MySQL DB�� �α����ϱ� ���� ����
		String localhost = "192.168.23.91:33060";
		String dbname = "kopoctc";
		String dbuser = "root";
		String dbpasswd = "kopo38";

		// JDBC���� ���� com.mysql.cj.jdbc.Driver ��� => cj�� ����.
		// JDBC���̺귯�� �ҷ�����
		Class.forName("com.mysql.cj.jdbc.Driver");

		//MYSQL�� ����
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + localhost + "/" + dbname, dbuser, dbpasswd);

		//MYSQL ����
		Statement stmt = conn.createStatement();

		String QueryTxt;
		
		//�ǽ�1] ������ ��ü ���
//		QueryTxt = "select * from StockDailyPrice";
		
		
		//�ǽ�2] �Ｚ���� �����ڵ�� ��ȸ
		QueryTxt = "select * from StockDailyPrice where shrn_iscd = 'A005930";	
		
		
		//�ǽ�3] Ư������ ��ȸ => 
//		QueryTxt = "select * from StockDailyPrice where bsop_date = '20110708'";	
		
		
		ResultSet rset = stmt.executeQuery(QueryTxt);
		int iCnt=0;
		while(rset.next()) {
			System.out.printf("*(ó���Ǽ�:%d)*****************************************\n",iCnt++);
			System.out.printf("�����ڵ�	:%s\n",rset.getString(1));
			System.out.printf("����		:%s\n",rset.getString(2));
			System.out.printf("�ð�		:%s��\n",rset.getString(3));
			System.out.printf("��		:%s��\n",rset.getString(4));
			System.out.printf("����		:%s��\n",rset.getString(5));
			System.out.printf("����		:%s��\n",rset.getString(6));
			System.out.printf("�ŷ���		:%s��\n",rset.getString(7));
			System.out.printf("�ŷ����	:%s��\n",rset.getString(8));
	
		}
		Date end = new Date();
		System.out.println("*(ó������)*****************************************\n");
		System.out.println("End : " + sdf.format(end));
		System.out.printf("DB���� ���۽ð� :%s, DB ����ð� :%s",sdf.format(start),sdf.format(end));

		rset.close();
		stmt.close(); 
		conn.close(); 


	}

}