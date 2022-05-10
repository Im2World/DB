package Find_FreeWifi;

//insert data
//�����ͺ��̽����α׷��� 2�� �����̵� 23 _ �ǽ�3 _ ���������̸Ӹ�Ű �����ؼ� �ߺ��Ǿ� ���� �ڷ� ����
//����ó�� �Ϸ�
//�ٽ� : insert ignore

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FreeWifi2_insertData {
	public static void main(String[] args) throws ClassNotFoundException, IOException, ParseException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC ����̹� �ε�

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

		File f = new File("C:\\Users\\kopo\\Desktop\\DataBaseClass\\practice\\���������������ǥ�ص�����_����.txt"); // ������ ������ �ҷ��´�.
		BufferedReader br = new BufferedReader(new FileReader(f)); // ���� ���� �ν��Ͻ� ����.
		String readtxt; // �� ���� ���� �б�.
		if ((readtxt = br.readLine()) == null) { // �ش� �ٿ� �����Ͱ� ���ٸ�(null)
			System.out.printf("�� �����Դϴ�\n"); // ���� ���
			return; // ��ȯ�� ����.
		}
		String[] field_name = readtxt.split("\t"); // tab�� �����ڷ� �ʵ� �и�

		int LineCnt = 0; // line ī��Ʈ�ϴ� ���� ����.
		while ((readtxt = br.readLine()) != null) { // null���� �ƴ϶��, ������ �� �پ� ��� ����.
			String[] field = readtxt.split("\t"); // tab���� ���е� �����Ͱ��� �迭�� ����
			String QueryTxt = null; // ���� �ؽ�Ʈ�� ���� String �������� �� null�� �ʱ�ȭ
			
			for(int i = 0; i < field_name.length; i++) {
				if(field[i].contains("\"")) {
					field[i] = field[i].replaceAll("\"", "");	// �� �ʵ忡 ���Ե� " ����
				}				
			}

			//ignore : �����Ϳ� �ߺ����� ������ ó������ �ʰ� �������� �д´�.
			try {	// try-catch ������ ���� ó��
					QueryTxt = String.format("insert ignore into freewifi("
								+ "inst_place, inst_place_detail, inst_city, inst_country, inst_place_flag,"
								+ "service_provider, wifi_ssid, inst_date, place_addr_road, place_addr_land,"
								+ "manage_office, manage_office_phone, latitude, longitude, write_date)"
								+ "values ("
								+ "'%s', '%s', '%s', '%s', '%s',"
								+ "'%s', '%s', '%s', '%s', '%s',"
								+ "'%s', '%s', %s, %s, '%s');",
								field[0], field[1], field[2], field[3], field[4],
								field[5], field[6], field[7], field[8], field[9],
								field[10], field[11], field[12], field[13], field[14]);
			}
			catch (Exception e) {		// �̿� ���� �߻���
				e.printStackTrace();	// �ֿܼ� ���
			}
			stmt.execute(QueryTxt); // ********statement�� QueryTxt�� ����!!!**************
			System.out.printf("%d��° �׸� Insert OK \n[%s]\n", LineCnt, QueryTxt); // �ܼ�â���� ������ ����� ���� Ȯ��
			LineCnt++; // ���� �� �� �ݺ��� ������ ���� �� �پ� ����
		}
		//�ڿ�����
		br.close(); // BufferedReader �ݱ�
		stmt.close(); // Statement �ݱ�
		conn.close(); // Connection �ݱ�
	}
}