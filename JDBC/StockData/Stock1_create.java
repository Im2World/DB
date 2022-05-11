package stock;
//create

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Training : ���ں� �ְ� ������������ �ǽ�, ���̺� �����
// (�ǽ�1 table����,���� (�� [�����ڵ�, ����, �ð�,��,����,����, �ŷ���, �ŷ����] ������ ���̺� ����)


public class Stock1_create {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
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
				
		/*(3)���ں� �����͸� ���� ���� ���̺�*/
		stmt.execute("create table StockDailyPrice(" +
	   			"shrn_iscd varchar(200), " +  //�������� ���� �����ڵ� 
				"bsop_date int, "+ //  �ֽ� ���� ����
				"stck_oprc int, " + //  �ֽ� �ð�
				"stck_hgpr int, " + // �ֽ� �ְ� 
				"stck_lwpr int, " + //  �ֽ� ������
				"stck_prpr int, " +  // �ֽ� ���� 
				"acml_vol bigint, " + // ���� �ŷ��� => �ŷ������ �󸶳����� ���� bigint���
				"acml_tr_pbmn bigint, " + // ���� �ŷ� ��� => �ŷ������ �󸶳����� ���� bigint���
				"CONSTRAINT pmk PRIMARY KEY (shrn_iscd, bsop_date));");			//���� �����̸Ӹ�Ű : ���������ڵ�, �ֽĿ�������		
		
		stmt.close(); //open�� ��ŭ close
		conn.close(); //open�� ��ŭ close
	}
}