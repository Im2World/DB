package parkingLot;
//create table

//실습데이터 : 한국교통안전공단_전국공영주차장정보_20191224.xls => 탭으로 분리 => 한국교통안전공단_전국공영주차장정보_20191224.txt
//경기도지역으로 도 필터링함
//무료와이파이 정보파일을 보고 해당 필드명으로 테이블을 만든다. 필드명,필드타입,크기 등 구성
//key를 무엇으로 잡을 것인가 두개 이상을 key로 잡아야한다.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ParkingLot1_createT {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC 드라이버 로드

		// MySQL DB에 로그인하기 위한 정보
		String localhost = "192.168.23.91:33060";
		String dbname = "kopoctc";
		String dbuser = "root";
		String dbpasswd = "kopo38";

		// JDBC버전 따라서 com.mysql.cj.jdbc.Driver 사용 => cj가 들어간다.
		Class.forName("com.mysql.cj.jdbc.Driver");

		// connection j.jar파일의 기능사용위한 객체생성
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + localhost + "/" + dbname, dbuser, dbpasswd);

		// connection j.jar파일의 기능사용위한 객체생성2
		Statement stmt = conn.createStatement();

		// 일자는 date형으로 타입지정 후 정제
		// 오류 막기 위해 각 데이터의 크기는 varchar로 넉넉하게 배정
		// 실습 데이터에 한글이있기때문에 utf8로 인코딩
		stmt.execute("create table parkingLot(" // 테이블명 freewifi 테이블 생성
				+ "parkingLot_id varchar(30) NOT NULL PRIMARY KEY, " // 1. 주차장관리번호
				+ "place varchar(100), " // 2. 주차장명
				+ "longitude double, " // 3. 경도 (더블형)
				+ "latitude double, " // 4. 위도 (더블형)
				+ "public_type varchar(20), "	// 5. 주차장 구분 (공영 , 민영)
				+ "outdoor_type varchar(20), "	// 6. 주차장 유형 (노외 , 노상)	
				+ "place_addr_land varchar(200), " // 7. 소재지 지번주소				
				+ "place_addr_road varchar(200), " // 8. 소재지 도로명주소
				+ "parking_spaces_num int, "	// 9. 주차구획수 (정수형)
				+ "business_days varchar(20), "	// 10. 운영요일
				+ "weekday_start_time varchar(20), "	// 11. 평일운영시작시각
				+ "weekday_end_time varchar(20), "	// 12. 평일운영종료시각				
				+ "saturday_start_time varchar(20), "	// 13. 토요일운영시작시각
				+ "saturday_end_time varchar(20), "	// 14. 토요일운영종료시각				
				+ "holiday_start_time varchar(20), "	// 15. 공휴일운영시작시각
				+ "holiday_end_time varchar(20), "	// 16. 공휴일운영종료시각
				+ "price varchar(20), "	// 17. 요금정보(유료, 무료)
				+ "manage_office varchar(100), " // 18. 관리기관명				
				+ "city varchar(50), " // 19. 설치 도명				
				+ "city_sub varchar(50), " // 20. 설치 시명						
				+ "city_x double, " // 21. 지역중심x좌표 (더블형)
				+ "city_y double, " // 22. 지역중심y좌표 (더블형)
				+ "city_code varchar(20), "	// 23. 지역코드				
				+ "manage_office_phone varchar(50), " // 24. 연락처			
				+ "write_date varchar(200))" // 25. 수정일자
				+ "DEFAULT CHARSET=utf8;"); // 한글 정상출력 위해 utf-8

		stmt.close(); // Statement 인스턴스 닫기
		conn.close(); // Connection 인스턴스 닫기
	}
}
