package Find_FreeWifi;
//create table
//실습데이터 : 전국무료와이파이표준데이터.xls => 탭으로 분리 => 전국무료와이파이표준데이터.txt
//무료와이파이 정보파일을 보고 해당 필드명으로 테이블을 만든다. 필드명,필드타입,크기 등 구성
//key를 무엇으로 잡을 것인가 두개 이상을 key로 잡아야한다.


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FreeWifi1_createT {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");	// JDBC 드라이버 로드

		// MySQL DB에 로그인하기 위한 정보
		String localhost = "192.168.23.91:33061";
		String dbname = "kopoctc";
		String dbuser = "root";
		String dbpasswd = "kopo38";

		// JDBC버전 따라서 com.mysql.cj.jdbc.Driver 사용 => cj가 들어간다.
		Class.forName("com.mysql.cj.jdbc.Driver");

		// connection j.jar파일의 기능사용위한 객체생성
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + localhost + "/" + dbname, dbuser, dbpasswd);

		// connection j.jar파일의 기능사용위한 객체생성2
		Statement stmt = conn.createStatement();

		//일자는 date형으로 타입지정 후 정제
		//오류 막기 위해 각 데이터의 크기는 varchar로 넉넉하게 배정
		//실습 데이터에 한글이있기때문에 utf8로 인코딩
		stmt.execute("create table freewifi("					// 테이블명 freewifi 테이블 생성
				+ "inst_place varchar(100), "					// 설치 장소명
				+ "inst_place_detail varchar(200), "			// 설치 장소상세
				+ "inst_city varchar(50), "						// 설치 시도명
				+ "inst_country varchar(50), "					// 설치 시군구명
				+ "inst_place_flag varchar(50), "				// 설치 시설 구분
				+ "service_provider varchar(50), "				// 서비스 제공사명
				+ "wifi_ssid varchar(100), "						// 와이파이 SSID
				+ "inst_date varchar(50), "							// 설치 년월 (date)
				+ "place_addr_road varchar(200), "				// 소재지 도로명주소
				+ "place_addr_land varchar(200), "				// 소재지 지번주소
				+ "manage_office varchar(50), "					// 관리기관명
				+ "manage_office_phone varchar(50), "			// 관리기관 전화번호
				+ "latitude double, "							// 위도 (더블형)
				+ "longitude double, "							// 경도 (더블형)
				+ "write_date date,"							// 데이터 기준일자 (date)
				+ "CONSTRAINT pmk PRIMARY KEY (inst_place, inst_place_detail, wifi_ssid, inst_date, latitude, longitude))" //복합 프라이머리키: 설치장소명, 설치장소상세, ssid, 설치년월 , 위도, 경도
				+ "DEFAULT CHARSET=utf8;");						// 한글 정상출력 위해 utf-8

		//자원해제
		stmt.close();
		conn.close();
	}
}
