package Report_Score;
//��������ǥ ����� 1
//���̺����
//BasicTraining2 : ��������ǥ ���̺� ����
//����1 : ���̺� ����,���� :�й�, �̸�, ����, ����, ������ �������̺� ���� (�й��� primary key)
//����2 : �� �Է� : ���̺� ���� �����Լ��� �̿��Ͽ�, 1000���� �����͸� ���� ���� ��

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CreateTable {
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
		
		ArrayList<OneRec> reportData = dataSet();	//dataSet ��ȯ ���� reportData�� ����
		
		stmt.execute(makeTable());	// �޼��� makeTable ���� => ���̺� ����
		stmt.execute(insertData(reportData));	//�� �Է� => �� �л� �й�, �̸�, ����, ����, ���� ������ �޼��� insertData�� ���ڷ� �ѱ��.
		
		stmt.close(); //open�� ��ŭ close
		conn.close(); //open�� ��ŭ close 							
	}
	
	//1. ���̺� ����
	//�׸����� �й�, �л���, ��������, ��������, ��������	
	static public String makeTable() {
		String QueryTxt = "create table scoreTable(" 						
				+ "stu_id int not null primary key, " 	
				+ "stu_name varchar(50), "					
				+ "kor int, "						
				+ "eng int, " 								
				+ "mat int)" 					
				+ "DEFAULT CHARSET=utf8;";
		return QueryTxt;	//��ȯ
	}
	
	//2. ������ �ֱ�
	//�Ʒ� OneRec Ŭ�������� ������ �������� ���⿡�� �����ͷ� �ִ´�.
	//dataSet�� ���� �Է¹��� reportData�� ���ڷ� �޴� �޼���
	static public String insertData(ArrayList<OneRec> reportData) {
		StringBuffer sb = new StringBuffer();		//�� �Է����� StringBuffer �ν��Ͻ�����
		String QueryTxt = "insert into scoreTable("
				+ "stu_id, stu_name, kor, eng, mat)"
				+ " values ";
		//������ ũ�⸸ŭ �ݺ�
		for (int i = 0; i < reportData.size(); i++) {
			OneRec person = reportData.get(i);	//���ڸ� �ϳ��� �޴´�.
			sb.append(String.format("(%s, '%s', %s, %s, %s), ",
					person.studentId(), person.name(), person.kor(), person.eng(), person.mat()));	//sb�� �� �߰�
		}
		
		// substring(begin, end) begin,end�� �ε����̰�, end �ձ����� �߶�´�.
		//sb.length()���� �ε���+1�̴�.
		//=> sb.substring(0, sb.length()-2)
		//sql������ �� �� ��� ����� ;
		QueryTxt += sb.substring(0, sb.length()-2) + ";";		//sb�� �߰��� ���� ��� �߰�
		return QueryTxt;	//��ȯ
	}

	//3. �л��� ���� ���� ����
	public static ArrayList<OneRec> dataSet() {
		ArrayList<OneRec> ArrayOneRec = new ArrayList<OneRec>();	//ArrayList ArrayOneRec����
		int person = 1000;	//�л��� 1000��
		
		//�ݺ��������� �й�, �л��̸�, ����, ����, ���� ���� ����
		//(Math.random()���� �� ���� ��������: 0~100��
		//�й��� 20000 ~ 20999
		for (int i = 0; i < person; i++) {					
			String name = String.format("�л�%02d", i);	
			int kor = (int)(Math.random() * 100);
			int eng = (int)(Math.random() * 100);
			int mat = (int)(Math.random() * 100);
			ArrayOneRec.add(new OneRec((20000+ i), name, kor, eng, mat));	// �ϳ��� OneRecŬ���� ���� �� ArrayList�� �������
		}
		return ArrayOneRec;		//��ȯ
	}	//ArrayList<OneRec> dataSet()
}