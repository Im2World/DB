package parkingLot;

//insert data
//데이터베이스프로그래밍 2강 슬라이드 25
//오류처리 완료
//핵심 : insert ignore

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

		File f = new File("C:\\Users\\kopo\\Desktop\\DataBaseClass\\practice\\한국교통안전공단_전국공영주차장정보_20191224.txt"); // 데이터 파일을 불러온다.
		BufferedReader br = new BufferedReader(new FileReader(f)); // 버퍼 리더 인스턴스 생성.
		String readtxt; // 한 줄을 먼저 읽기.
		if ((readtxt = br.readLine()) == null) { // 해당 줄에 데이터가 없다면(null)
			System.out.printf("빈 파일입니다\n"); // 문구 출력
			return; // 반환값 없음.
		}
		String[] field_name = readtxt.split("\t"); // tab을 구분자로 필드 분리

		int LineCnt = 0; // line 카운트하는 변수 선언.
		while ((readtxt = br.readLine()) != null) { // null값이 아니라면, 파일을 한 줄씩 계속 읽음.
			String[] field = readtxt.split("\t"); // tab으로 구분된 데이터값을 배열에 담음
			String QueryTxt = null; // 쿼리 텍스트를 넣을 String 변수선언 후 null로 초기화
			
			for(int i = 0; i < field_name.length; i++) {
				if(field[i].contains("\"")) {
					field[i] = field[i].replaceAll("\"", "");	// 각 필드에 포함된 " 제거
				}				
			}

			
			//ignore : 데이터에 중복값이 있으면 처리하지 않고 다음값을 읽는다.
			//varchar데이터를 insert할 때는 '로 문자를 감싸야 한다.
			//그외 타입은 '로 감싸지 않는다.
			try {	// try-catch 문으로 예외 처리
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
			catch (Exception e) {		// 이외 예외 발생시
				e.printStackTrace();	// 콘솔에 출력
			}
			stmt.execute(QueryTxt); // ********statement가 QueryTxt를 실행!!!**************
			System.out.printf("%d번째 항목 Insert OK \n[%s]\n", LineCnt, QueryTxt); // 콘솔창에서 데이터 제대로 들어가나 확인
			LineCnt++; // 과정 한 번 반복할 때마다 라인 한 줄씩 증가
		}
		br.close(); // BufferedReader 닫기
		stmt.close(); // Statement 닫기
		conn.close(); // Connection 닫기
	}
}