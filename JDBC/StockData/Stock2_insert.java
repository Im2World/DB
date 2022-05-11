package stock;
//insert
//테이블에 데이터 넣기 _ 2시간 정도 소요?
//StockDailyPrice 사용!

/*
Training : 일자별 주가 데이터정보로 실습, 테이블에 데이터 입력
(실습2 파일을 읽어서 table에 집어넣기, P-KEY 잡아주셔요 (일자, 단축코드 복합 P-KEY)
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Stock2_insert {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		// *******************
		// MySQL DB에 로그인하기 위한 정보
		String localhost = "192.168.23.91:33060";
		String dbname = "kopoctc";
		String dbuser = "root";
		String dbpasswd = "kopo38";

		// JDBC버전 따라서 com.mysql.cj.jdbc.Driver 사용 => cj가 들어간다.
		// JDBC라이브러리 불러오기
		Class.forName("com.mysql.cj.jdbc.Driver");

		// MYSQL에 연결
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + localhost + "/" + dbname, dbuser, dbpasswd);
		// *************************

		// stockdailyprice 테이블에 주가데이터를 입력하는 쿼리를 작성.
		// PreparedStatement를 사용하므로 values에 ?를 표시.
		String QueryTxt = "insert into StockDailyPrice (shrn_iscd, bsop_date, stck_oprc, stck_hgpr, stck_lwpr, stck_prpr, acml_vol, acml_tr_pbmn)"
				+ "values (?, ?, ?, ?, ?, ?, ?, ?)";

		// 대용량 데이터를 빠르게 Insert하기위해 PreparedStatement를 이용한다
		// PreparedStatement는 캐시에 SQL 문장을 담아 한번에 처리하기 때문에 Statement보다 처리속도가 빠르다.
		PreparedStatement pstmt = conn.prepareStatement(QueryTxt);


		//일자별 데이터를 읽어오기
		File f = new File("C:\\Users\\kopo\\Desktop\\StockData\\StockDailyPrice.csv");
		BufferedReader br = new BufferedReader(new FileReader(f));

		String readtxt;

		if ((readtxt = br.readLine()) == null) { // 빈 줄이면
			System.out.printf("빈 파일입니다\n");
			return;
		}

		String[] field_name = readtxt.split(","); // csv파일이니까 콤마로 구분

		int LineCnt = 0; // 라인수 출력
		conn.setAutoCommit(false); // insert속도 향상 위해 오토커밋을 false로 설정
		long startTime = System.currentTimeMillis();// 코드 실행 전에 시간 받아오기 ms로 받음

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 지정 포멧으로 시간get
		Calendar startTime_cal = Calendar.getInstance(); // 시작 - 현재시간get
		String format_time1 = format1.format(startTime_cal.getTime()); // 위에서 지정해준 포멧으로 시간프린트해주기

		while ((readtxt = br.readLine()) != null) {
			String[] field = readtxt.split(",");

			pstmt.setString(1, field[2]); // 유가증권 단축 종목코드
			pstmt.setString(2, field[1]); // 주식 영업 일자
			pstmt.setString(3, field[4]); // 주식 시가
			pstmt.setString(4, field[5]); // 주식 최고가
			pstmt.setString(5, field[6]); // 주식 최저가
			pstmt.setString(6, field[3]); // 주식 종가
			pstmt.setString(7, field[11]); // 누적 거래량
			pstmt.setString(8, field[12]); // 누적 거래 대금
			pstmt.addBatch();

			pstmt.clearParameters();
			LineCnt++;

			try {
				if (LineCnt % 10000 == 0) {
					pstmt.executeBatch();
					conn.commit();
					System.out.printf("%d번째 항목 addBatch OK\n", LineCnt); // 속도 개선위해 10,000줄마다 확인출력
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		try {
			pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}

		conn.commit();
		conn.setAutoCommit(true);

		Calendar afterTime_cal = Calendar.getInstance();// 끝 - 현재시간get

		String format_time2 = format1.format(afterTime_cal.getTime());// 위에서 지정해준 포멧으로 시간프린트해주기
		long endTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (endTime - startTime) / 1000; // 두 시간에 차 계산 초로 표현
		long minDiffTime = (endTime - startTime) / 1000 / 60; // 두 시간에 차 계산 분으로 표현

		System.out.println("시작시간: " + format_time1); // 시작시간 프린트
		System.out.println("완료시간: " + format_time2); // 완료시간 프린트
		System.out.println("\n***걸린시간 : " + minDiffTime + " 분 ( = " + secDiffTime + " 초) "); // 걸린시간을 분 으로 표현하고, 초로도 표현
		System.out.printf("Time	: %dms\n", endTime - startTime);

		br.close();
		pstmt.close();
		conn.close();
	}
}