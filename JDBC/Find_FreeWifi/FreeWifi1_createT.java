package Find_FreeWifi;
//create table
//�ǽ������� : ���������������ǥ�ص�����.xls => ������ �и� => ���������������ǥ�ص�����.txt
//����������� ���������� ���� �ش� �ʵ������ ���̺��� �����. �ʵ��,�ʵ�Ÿ��,ũ�� �� ����
//key�� �������� ���� ���ΰ� �ΰ� �̻��� key�� ��ƾ��Ѵ�.


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FreeWifi1_createT {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
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

		//���ڴ� date������ Ÿ������ �� ����
		//���� ���� ���� �� �������� ũ��� varchar�� �˳��ϰ� ����
		//�ǽ� �����Ϳ� �ѱ����ֱ⶧���� utf8�� ���ڵ�
		stmt.execute("create table freewifi("					// ���̺�� freewifi ���̺� ����
				+ "inst_place varchar(100), "					// ��ġ ��Ҹ�
				+ "inst_place_detail varchar(200), "			// ��ġ ��һ�
				+ "inst_city varchar(50), "						// ��ġ �õ���
				+ "inst_country varchar(50), "					// ��ġ �ñ�����
				+ "inst_place_flag varchar(50), "				// ��ġ �ü� ����
				+ "service_provider varchar(50), "				// ���� �������
				+ "wifi_ssid varchar(100), "						// �������� SSID
				+ "inst_date varchar(50), "							// ��ġ ��� (date)
				+ "place_addr_road varchar(200), "				// ������ ���θ��ּ�
				+ "place_addr_land varchar(200), "				// ������ �����ּ�
				+ "manage_office varchar(50), "					// ���������
				+ "manage_office_phone varchar(50), "			// ������� ��ȭ��ȣ
				+ "latitude double, "							// ���� (������)
				+ "longitude double, "							// �浵 (������)
				+ "write_date date,"							// ������ �������� (date)
				+ "CONSTRAINT pmk PRIMARY KEY (inst_place, inst_place_detail, wifi_ssid, inst_date, latitude, longitude))" //���� �����̸Ӹ�Ű: ��ġ��Ҹ�, ��ġ��һ�, ssid, ��ġ��� , ����, �浵
				+ "DEFAULT CHARSET=utf8;");						// �ѱ� ������� ���� utf-8

		//�ڿ�����
		stmt.close();
		conn.close();
	}
}
