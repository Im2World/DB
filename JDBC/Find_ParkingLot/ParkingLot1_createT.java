package parkingLot;
//create table

//�ǽ������� : �ѱ������������_������������������_20191224.xls => ������ �и� => �ѱ������������_������������������_20191224.txt
//��⵵�������� �� ���͸���
//����������� ���������� ���� �ش� �ʵ������ ���̺��� �����. �ʵ��,�ʵ�Ÿ��,ũ�� �� ����
//key�� �������� ���� ���ΰ� �ΰ� �̻��� key�� ��ƾ��Ѵ�.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ParkingLot1_createT {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
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

		// ���ڴ� date������ Ÿ������ �� ����
		// ���� ���� ���� �� �������� ũ��� varchar�� �˳��ϰ� ����
		// �ǽ� �����Ϳ� �ѱ����ֱ⶧���� utf8�� ���ڵ�
		stmt.execute("create table parkingLot(" // ���̺�� freewifi ���̺� ����
				+ "parkingLot_id varchar(30) NOT NULL PRIMARY KEY, " // 1. �����������ȣ
				+ "place varchar(100), " // 2. �������
				+ "longitude double, " // 3. �浵 (������)
				+ "latitude double, " // 4. ���� (������)
				+ "public_type varchar(20), "	// 5. ������ ���� (���� , �ο�)
				+ "outdoor_type varchar(20), "	// 6. ������ ���� (��� , ���)	
				+ "place_addr_land varchar(200), " // 7. ������ �����ּ�				
				+ "place_addr_road varchar(200), " // 8. ������ ���θ��ּ�
				+ "parking_spaces_num int, "	// 9. ������ȹ�� (������)
				+ "business_days varchar(20), "	// 10. �����
				+ "weekday_start_time varchar(20), "	// 11. ���Ͽ���۽ð�
				+ "weekday_end_time varchar(20), "	// 12. ���Ͽ����ð�				
				+ "saturday_start_time varchar(20), "	// 13. ����Ͽ���۽ð�
				+ "saturday_end_time varchar(20), "	// 14. ����Ͽ����ð�				
				+ "holiday_start_time varchar(20), "	// 15. �����Ͽ���۽ð�
				+ "holiday_end_time varchar(20), "	// 16. �����Ͽ����ð�
				+ "price varchar(20), "	// 17. �������(����, ����)
				+ "manage_office varchar(100), " // 18. ���������				
				+ "city varchar(50), " // 19. ��ġ ����				
				+ "city_sub varchar(50), " // 20. ��ġ �ø�						
				+ "city_x double, " // 21. �����߽�x��ǥ (������)
				+ "city_y double, " // 22. �����߽�y��ǥ (������)
				+ "city_code varchar(20), "	// 23. �����ڵ�				
				+ "manage_office_phone varchar(50), " // 24. ����ó			
				+ "write_date varchar(200))" // 25. ��������
				+ "DEFAULT CHARSET=utf8;"); // �ѱ� ������� ���� utf-8

		stmt.close(); // Statement �ν��Ͻ� �ݱ�
		conn.close(); // Connection �ν��Ͻ� �ݱ�
	}
}
