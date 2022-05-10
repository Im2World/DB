package parkingLot;

//select data
//조건 : 실습1 분당융기원과 가까운 곳 찾기 /실습2 전부 보기 / 실습3 성남시 / 실습 4 무료 주차장
//sql문 where 다음 띄어쓰기 주의!!

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ParkingLot3_select {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		Class.forName("com.mysql.cj.jdbc.Driver");	// JDBC 드라이버 로드

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
		

		//현재 나의 위치(기준위치) : 분당융기원으로 설정
		double lat=37.3860521; 	//분당 융기원 위도를 실수형 변수로 선언 후 초기화
		double lng =127.1214038;	//분당 융기원 경도를 실수형 변수로 선언 후 초기화

		
		String QueryTxt; 	//값 담을 문자열 변수 선언
		
		
		
		
//		//실습1] 나와 가까운 주차장 찾기
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
//		//SQRT(POWER(latitude - %f,2) + POWER (longitude - %f,2)) => parkingLot 테이블에서 루트{(x1-x2)^2+(y1-y2)^2} 값 계산한 것이
//		//select MIN(SQRT(POWER(latitude - %f,2) + POWER(longitude - %f,2))) => min 인 것을 출력
		
//		QueryTxt = String.format("select * from parkingLot where "
//				+ "SQRT(POWER(latitude - %f,2) + POWER (longitude - %f,2)) = "
//				+ "(select MIN(SQRT(POWER(latitude - %f,2) + POWER(longitude - %f,2))) from parkingLot);",lat,lng,lat,lng);
		
		
		
		//실습2] 주차장 전부 출력						
//		QueryTxt = "select * from parkingLot";	
		
		//실습3] 무료 주차장만 출력
//		QueryTxt = "select * from parkingLot where price='무료'";	

		//실습4] 성남시 주차장만 출력
		QueryTxt = "select * from parkingLot where city_sub like '%성남시%'";		//성남시 를 포함하는 레코드 출력

		
		
		//필요한 대로 데이터 순서변경
		//주차장관리번호 - 지역코드 - 설치도명 - 설치시명 - 주차장명 - 주차장 구분 - 주차장 유형	=> 7
		//요금정보 - *주차구획수 - 운영요일 - 평일운영시작시각 - 평일운영종료시작 -토요일 운영시작시각 - 토요일운영종료시각- 공휴일운영시작시각 - 공휴일운영종료시각	 => 9
		//도로명주소 - 지번 - 관리기관명 - 연락처 - *위도 - *경도 - 수정일자	=> 7
		ResultSet rset = stmt.executeQuery(QueryTxt);
		int iCnt=0;
		while(rset.next()) {
			System.out.printf("*(%d)*************************************************************************\n",iCnt++);
			System.out.printf("주차장관리번호			:%s\n",rset.getString(1));
			System.out.printf("지역코드			:%s\n",rset.getString(23));
			System.out.printf("설치도명			:%s\n",rset.getString(19));
			System.out.printf("설치시명			:%s\n",rset.getString(20));
			System.out.printf("주차장명			:%s\n",rset.getString(2));
			System.out.printf("주차장 구분			:%s\n",rset.getString(5));
			System.out.printf("주차장 유형			:%s\n",rset.getString(6));
			System.out.printf("요금정보			:%s\n",rset.getString(17));
			System.out.printf("주차구획수			:%s\n",rset.getString(9));
			System.out.printf("운영요일			:%s\n",rset.getString(10));
			System.out.printf("평일운영시작시간		:%s\n",rset.getString(11));
			System.out.printf("평일운영종료시간		:%s\n",rset.getString(12));
			System.out.printf("토요일운영시작시각		:%s\n",rset.getString(13));
			System.out.printf("토요일운영종료시각		:%s\n",rset.getString(14));
			System.out.printf("공휴일운영시작시각		:%s\n",rset.getString(15));
			System.out.printf("공휴일운영종료시각		:%s\n",rset.getString(16));
			System.out.printf("도로명 주소			:%s\n",rset.getString(8));
			System.out.printf("지번				:%s\n",rset.getString(7));
			System.out.printf("관리기관명			:%s\n",rset.getString(18));
			System.out.printf("연락처				:%s\n",rset.getString(24));
			System.out.printf("위도				:%s\n",rset.getString(4));
			System.out.printf("경도				:%s\n",rset.getString(3));
			System.out.printf("수정일자			:%s\n",rset.getString(25));
		}

		rset.close(); 	//open한만큼 close
		stmt.close(); 	//open한만큼 close 
		conn.close();  	//open한만큼 close
	}
}