package Find_FreeWifi;

//select data
//조건 : 실습1 분당융기원과 가까운 곳 찾기 /실습2 전부 보기 / 실습3 SKT 와이파이 / 실습4 안양시 와이파이 찾기
//sql문 where 다음 띄어쓰기 주의!!

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class FreeWifi3_select {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

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
		

		//현재 나의 위치(기준위치) : 분당융기원으로 설정
		double lat=37.3860521; 	//분당 융기원 위도를 실수형 변수로 선언 후 초기화
		double lng =127.1214038;	//분당 융기원 경도를 실수형 변수로 선언 후 초기화

		
		String QueryTxt; 	//값 담을 문자열 변수 선언
		
		
//		//실습1] 나와 가까운 와이파이 찾기
//		//select 문 안에 select문을 넣었다.
//		
//		/* ********현재 지점과 목표지점의 거리계산*********
//		피타고라스 정리 활용
//		좌표(x1,y1), 좌표(x2,y2)인 경우 
//		융기원과 각 목적지 도로명주소 위도, 경도 비교
//		루트{(x1-x2)^2+(y1-y2)^2}
//		SQRT : 루트 씌우기
//		POWER : 제곱 표현 => 2제곱이라서 숫자 2 */
//		
//		//SQRT(POWER(latitude - %f,2) + POWER (longitude - %f,2)) => freewifi 테이블에서 루트{(x1-x2)^2+(y1-y2)^2} 값 계산한 것이
//		//select MIN(SQRT(POWER(latitude - %f,2) + POWER(longitude - %f,2))) => min 인 것을 출력
		
//		QueryTxt = String.format("select * from freewifi where "
//				+ "SQRT(POWER(latitude - %f,2) + POWER (longitude - %f,2)) = "
//				+ "(select MIN(SQRT(POWER(latitude - %f,2) + POWER(longitude - %f,2))) from freewifi);",lat,lng,lat,lng);

		
		
		//실습2] 와이파이 전부 출력						
//		QueryTxt = "select * from freewifi";	
		
		//실습3] SKT 와이파이만 출력
//		QueryTxt = "select * from freewifi where service_provider='SKT'";	

		//실습4] 안양시 와이파이만 출력
		QueryTxt = "select * from freewifi where inst_country='안양시'";		


		ResultSet rset = stmt.executeQuery(QueryTxt);
		int iCnt=0;
		while(rset.next()) {
			System.out.printf("*(%d)*************************************************************************\n",iCnt++);
			System.out.printf("설치장소명		:%s\n",rset.getString(1));
			System.out.printf("설치장소상세		:%s\n",rset.getString(2));
			System.out.printf("설치시도명		:%s\n",rset.getString(3));
			System.out.printf("설치시군구명		:%s\n",rset.getString(4));
			System.out.printf("설치시설구분		:%s\n",rset.getString(5));
			System.out.printf("서비스제공사명		:%s\n",rset.getString(6));
			System.out.printf("와이파이SSID		:%s\n",rset.getString(7));
			System.out.printf("설치년월		:%s\n",rset.getString(8));
			System.out.printf("소재지도로명주소	:%s\n",rset.getString(9));
			System.out.printf("소재지지번주소		:%s\n",rset.getString(10));
			System.out.printf("관리기관명		:%s\n",rset.getString(11));
			System.out.printf("관리기관전화번호	:%s\n",rset.getString(12));
			System.out.printf("위도			:%s\n",rset.getString(13));
			System.out.printf("경도			:%s\n",rset.getString(14));
			System.out.printf("데이터기준일자		:%s\n",rset.getString(15));
		}
		//자원해제
		rset.close();
		stmt.close(); 
		conn.close(); 


	}

}