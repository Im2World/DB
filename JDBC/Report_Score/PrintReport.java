package Report_Score;
//��������ǥ ���!
//****����, ��ºθ� ���� ���� �޼��� ItemPrint, thisPageSumPrint, allPageSumPrint������ �� ��� ���� �ܼ� ���!
//���� : 
//���� : �������� 30�� ��������ǥ ���
//(�� ���κ� ����/���, �������� ����/���, ���� ����/��� �� select ���������� ó��)

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PrintReport {
	//***�ѿ� �����ϱ�***
	//person������ �� ���������� ����ϱ� ���� ����
	static final int person = 1000;	// ����� �л���
	static int printLine = 30;	//���������� ����� ����

	
	//1. �����Լ�
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
		
		ResultSet rset = null;
		
		//��� �޼��� ����
		printFormat(rset, stmt);
		
		stmt.close(); //open�� ��ŭ close
		conn.close(); //open�� ��ŭ close 
	}
	
	//****************
	//2. ��� ���� ����
	public static void printFormat(ResultSet rset, Statement stmt) throws SQLException {
		int cnt = printLine;	//�������������� �����ϰ� �� �������� ����� ������ cnt������ ���� => ������ ������������ ����������.
		
		//sql���� limitȰ��!
		//limit ������ġ, ��ȯ���� => ������ġ�� 0���� ����
		//��) �� �������� 5�� ��� = > [limit 0*5, 5] => 0���� 5�� (=0,1,2,3,4), [limit 1*5, 5] => 5���� 5�� (5,6,7,8,9)
		for (int i = 0; i * cnt < person; i++) {
			int begin = i * printLine;	//sql �� limit begin, printLine �� ���� ����
//			System.out.println(begin);   //���� Ȯ�ο�
			
			//������ ������ ����ο� ���
			//��) i�� ������ �ݺ��Ǽ� �����ο� 36���� Ŀ����(35 + 5) > 35
			if((begin + printLine) > person) {
				printLine = person - begin;		//���������������� ����� �ο� �� ������ => 36-35 = 1 => �������� 1�� ���
			}
			
			//��� ���
			HeaderPrint(i+1);	//i�� 0���� ����, page�� 1���� �����ؾ��� => i+1
			
			//���� �� ���
			rset = stmt.executeQuery("select *, kor+eng+mat, (kor+eng+mat)/3 "
					+ "from scoreTable limit " + begin + "," + printLine +";");
			while (rset.next()) {
				ItemPrint(rset);		//
			}

			//���������� ��갪
			//�ش� ������������ �� ���
			rset = stmt.executeQuery("select sum(kor), sum(eng), sum(mat), sum(kor+eng+mat), sum((kor+eng+mat)/3), "
					+ "avg(kor), avg(eng), avg(mat), avg(kor+eng+mat), avg((kor+eng+mat)/3) "
					+ "from (select * from scoreTable limit " + begin + "," + printLine + ") as page" + (i+1) + ";");	//(i+1)�� ����������
			while (rset.next()) {
				thisPageSumPrint(rset);	//���������� �� ��� => ���� : rset
			}
			
			//���������� ��갪
			//������������ 0��~����������� �� ��� ���
			rset = stmt.executeQuery("select sum(kor), sum(eng), sum(mat), sum(kor+eng+mat), sum((kor+eng+mat)/3), "
					+ "avg(kor), avg(eng), avg(mat), avg(kor+eng+mat), avg((kor+eng+mat)/3) "
					+ "from (select * from scoreTable limit 0," + (begin + printLine)+ ") as page" + (i+1) + ";");	//(i+1)�� ����������
			while (rset.next()) {
				allPageSumPrint(rset);	//���������� �� ��� => ���� : rset
			}
		} //for
	}
	
	//**************��º�*****************
	//�ð� ��� �޼���
	public static String timeStamp() {
		Calendar cal = Calendar.getInstance();	// Ķ���� Ŭ���� �ҷ��ͼ� �ν��Ͻ� ����
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.dd HH:mm:ss");	// ��¥ ��� ���� ����
		return sdf.format(cal.getTime());	// �ʱ��� ����ϴ� ��¥ ����
	}
	
	//��� ��� �޼���
	public static void HeaderPrint(int page) {	
		String Date = timeStamp();		// ����ð��� string������ ����
		System.out.printf(" %26s\n\n", "��������ǥ");
		System.out.printf(" PAGE: %d%42s\n", page, "������� : " + Date);	// ������� ��� => ����ð� �Բ� ���
		System.out.printf("========================================================\n");	// ���м� ���
		System.out.printf(" %-4s %-4s%6s%6s%6s%6s%7s\n", "��ȣ", "�̸�", "����", "����", "����", "����", "���");	// �׸��
		System.out.printf("========================================================\n");	// ���м� ���
	}
	
	//������ ��� �޼���
	//printFormat���� rset�� �Է��� �� ���� �׸� �ڷ����� ���� ��� => ��ȣ, �̸�, ����, ����, ����, ����, ���
	public static void ItemPrint(ResultSet rset) throws SQLException {
		System.out.printf(" %03d   %-6s%6d %7d %7d %7d %8.1f\n",
				rset.getInt(1), rset.getString(2), rset.getInt(3), 
				rset.getInt(4), rset.getInt(5), rset.getInt(6), rset.getDouble(7));
	}

	//******sql�� �ڷ����� int�� �����߾ double�� ���� �� �ִ�.******
	//�հ� ��� �޼���
	//����� �Ҽ����̹Ƿ� getDouble�� �޴´�.
	//printFormat���� rset�� �Է��� �� ���� �׸� �ڷ����� ���� ���
	public static void thisPageSumPrint(ResultSet rset) throws SQLException {
		System.out.printf("========================================================\n");	// ���м� ���
		System.out.printf(" %s\n", "����������");
		System.out.printf(" %-4s%7s  %6d  %6d  %6d  %6d %8.1f\n", "�հ�", " ", 	// ������� ����
				rset.getInt(1), rset.getInt(2), rset.getInt(3), rset.getInt(4), rset.getDouble(5));	
		System.out.printf(" %-4s%7s  %6.2f  %6.2f  %6.2f  %6.2f %8.1f\n", "���", " ", 	// �������հ��� ������ ���� ��� ���.
				rset.getDouble(6), rset.getDouble(7), rset.getDouble(8), rset.getDouble(9), rset.getDouble(10));
	}
	
	//��� ��� �޼��� - ���� ��������� �޼���
	//����� �Ҽ����̹Ƿ� getDouble�� �޴´�.
	//printFormat���� rset�� �Է��� �� ���� �׸� �ڷ����� ���� ���
	public static void allPageSumPrint(ResultSet rset) throws SQLException {
		System.out.printf("========================================================\n");	// ���м� ���
		System.out.printf(" %s\n", "����������");	
		System.out.printf(" %-4s%7s  %6d  %6d  %6d  %6d %8.2f\n", "�հ�", " ", 	// �������հ��� �հ�, ����� ���
				rset.getInt(1), rset.getInt(2), rset.getInt(3), rset.getInt(4), rset.getDouble(5));		
		System.out.printf(" %-4s%7s  %6.2f  %6.2f  %6.2f  %6.2f %8.2f\n", "���", " ", 	// ������ �������հ��� ���, ����� ��� ���.
				rset.getDouble(6), rset.getDouble(7), rset.getDouble(8), rset.getDouble(9), rset.getDouble(10));	
		System.out.println();
	}
}