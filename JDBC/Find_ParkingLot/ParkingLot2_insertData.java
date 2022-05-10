package parkingLot;

//insert data
//�����ͺ��̽����α׷��� 2�� �����̵� 25
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

public class ParkingLot2_insertData {
	public static void main(String[] args) throws ClassNotFoundException, IOException, ParseException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC ����̹� �ε�

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

		File f = new File("C:\\Users\\kopo\\Desktop\\DataBaseClass\\practice\\�ѱ������������_������������������_20191224.txt"); // ������ ������ �ҷ��´�.
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
			//varchar�����͸� insert�� ���� '�� ���ڸ� ���ξ� �Ѵ�.
			//�׿� Ÿ���� '�� ������ �ʴ´�.
			try {	// try-catch ������ ���� ó��
					QueryTxt = String.format("insert ignore into parkingLot("
								+ "parkingLot_id, place, longitude, latitude, public_type, outdoor_type,"	//6
								+ "place_addr_land, place_addr_road, parking_spaces_num, business_days, weekday_start_time,"	//5
								+ "weekday_end_time, saturday_start_time, saturday_end_time, holiday_start_time, holiday_end_time," //5
								+ "price, manage_office, city, city_sub, city_x,"	//5
								+ "city_y, city_code, manage_office_phone, write_date)"	//4
								+ "values ("
								+ "'%s', '%s', %s, %s, '%s', '%s',"	//6
								+ "'%s', '%s', %s, '%s', '%s',"	//5
								+ "'%s', '%s', '%s', '%s', '%s',"	//5
								+ "'%s', '%s', '%s', '%s', %s,"	//5
								+ "%s, '%s', '%s', '%s');",	//4
								field[0], field[1], field[2], field[3], field[4],field[5],field[6], field[7], field[8], field[9], field[10], field[11],field[12], field[13], field[14], field[15],
								field[16], field[17], field[18], field[19], field[20], field[21], field[22], field[23], field[24]);
			}
			catch (Exception e) {		// �̿� ���� �߻���
				e.printStackTrace();	// �ֿܼ� ���
			}
			stmt.execute(QueryTxt); // ********statement�� QueryTxt�� ����!!!**************
			System.out.printf("%d��° �׸� Insert OK \n[%s]\n", LineCnt, QueryTxt); // �ܼ�â���� ������ ����� ���� Ȯ��
			LineCnt++; // ���� �� �� �ݺ��� ������ ���� �� �پ� ����
		}
		br.close(); // BufferedReader �ݱ�
		stmt.close(); // Statement �ݱ�
		conn.close(); // Connection �ݱ�
	}
}