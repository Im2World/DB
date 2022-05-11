package stock;
//insert
//���̺� ������ �ֱ� _ 2�ð� ���� �ҿ�?
//StockDailyPrice ���!

/*
Training : ���ں� �ְ� ������������ �ǽ�, ���̺� ������ �Է�
(�ǽ�2 ������ �о table�� ����ֱ�, P-KEY ����ּſ� (����, �����ڵ� ���� P-KEY)
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Stock2_insert {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		// *******************
		// MySQL DB�� �α����ϱ� ���� ����
		String localhost = "192.168.23.91:33060";
		String dbname = "kopoctc";
		String dbuser = "root";
		String dbpasswd = "kopo38";

		// JDBC���� ���� com.mysql.cj.jdbc.Driver ��� => cj�� ����.
		// JDBC���̺귯�� �ҷ�����
		Class.forName("com.mysql.cj.jdbc.Driver");

		// MYSQL�� ����
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + localhost + "/" + dbname, dbuser, dbpasswd);
		// *************************

		// stockdailyprice ���̺� �ְ������͸� �Է��ϴ� ������ �ۼ�.
		// PreparedStatement�� ����ϹǷ� values�� ?�� ǥ��.
		String QueryTxt = "insert into StockDailyPrice (shrn_iscd, bsop_date, stck_oprc, stck_hgpr, stck_lwpr, stck_prpr, acml_vol, acml_tr_pbmn)"
				+ "values (?, ?, ?, ?, ?, ?, ?, ?)";

		// ��뷮 �����͸� ������ Insert�ϱ����� PreparedStatement�� �̿��Ѵ�
		// PreparedStatement�� ĳ�ÿ� SQL ������ ��� �ѹ��� ó���ϱ� ������ Statement���� ó���ӵ��� ������.
		PreparedStatement pstmt = conn.prepareStatement(QueryTxt);


		//���ں� �����͸� �о����
		File f = new File("C:\\Users\\kopo\\Desktop\\StockData\\StockDailyPrice.csv");
		BufferedReader br = new BufferedReader(new FileReader(f));

		String readtxt;

		if ((readtxt = br.readLine()) == null) { // �� ���̸�
			System.out.printf("�� �����Դϴ�\n");
			return;
		}

		String[] field_name = readtxt.split(","); // csv�����̴ϱ� �޸��� ����

		int LineCnt = 0; // ���μ� ���
		conn.setAutoCommit(false); // insert�ӵ� ��� ���� ����Ŀ���� false�� ����
		long startTime = System.currentTimeMillis();// �ڵ� ���� ���� �ð� �޾ƿ��� ms�� ����

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // ���� �������� �ð�get
		Calendar startTime_cal = Calendar.getInstance(); // ���� - ����ð�get
		String format_time1 = format1.format(startTime_cal.getTime()); // ������ �������� �������� �ð�����Ʈ���ֱ�

		while ((readtxt = br.readLine()) != null) {
			String[] field = readtxt.split(",");

			pstmt.setString(1, field[2]); // �������� ���� �����ڵ�
			pstmt.setString(2, field[1]); // �ֽ� ���� ����
			pstmt.setString(3, field[4]); // �ֽ� �ð�
			pstmt.setString(4, field[5]); // �ֽ� �ְ�
			pstmt.setString(5, field[6]); // �ֽ� ������
			pstmt.setString(6, field[3]); // �ֽ� ����
			pstmt.setString(7, field[11]); // ���� �ŷ���
			pstmt.setString(8, field[12]); // ���� �ŷ� ���
			pstmt.addBatch();

			pstmt.clearParameters();
			LineCnt++;

			try {
				if (LineCnt % 10000 == 0) {
					pstmt.executeBatch();
					conn.commit();
					System.out.printf("%d��° �׸� addBatch OK\n", LineCnt); // �ӵ� �������� 10,000�ٸ��� Ȯ�����
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		try {
			pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}

		conn.commit();
		conn.setAutoCommit(true);

		Calendar afterTime_cal = Calendar.getInstance();// �� - ����ð�get

		String format_time2 = format1.format(afterTime_cal.getTime());// ������ �������� �������� �ð�����Ʈ���ֱ�
		long endTime = System.currentTimeMillis(); // �ڵ� ���� �Ŀ� �ð� �޾ƿ���
		long secDiffTime = (endTime - startTime) / 1000; // �� �ð��� �� ��� �ʷ� ǥ��
		long minDiffTime = (endTime - startTime) / 1000 / 60; // �� �ð��� �� ��� ������ ǥ��

		System.out.println("���۽ð�: " + format_time1); // ���۽ð� ����Ʈ
		System.out.println("�Ϸ�ð�: " + format_time2); // �Ϸ�ð� ����Ʈ
		System.out.println("\n***�ɸ��ð� : " + minDiffTime + " �� ( = " + secDiffTime + " ��) "); // �ɸ��ð��� �� ���� ǥ���ϰ�, �ʷε� ǥ��
		System.out.printf("Time	: %dms\n", endTime - startTime);

		br.close();
		pstmt.close();
		conn.close();
	}
}