package Report_Score;
//성적집계표 출력!
//****계산부, 출력부를 따로 만들어서 메서드 ItemPrint, thisPageSumPrint, allPageSumPrint에서는 값 계산 없이 단순 출력!
//계산부 : 
//조건 : 페이지당 30명씩 성적집계표 출력
//(단 개인별 총점/평균, 페이지별 총점/평균, 누적 총점/평균 은 select 쿼리문에서 처리)

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PrintReport {
	//***총원 변경하기***
	//person변수는 총 페이지수를 출력하기 위한 변수
	static final int person = 1000;	// 출력할 학생수
	static int printLine = 30;	//한페이지당 출력할 개수

	
	//1. 메인함수
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// MySQL DB에 로그인하기 위한 정보
		String localhost = "192.168.23.91:33060";
		String dbname = "kopoctc";
		String dbuser = "root";
		String dbpasswd = "kopo38";

		// JDBC버전 따라서 com.mysql.cj.jdbc.Driver 사용 => cj가 들어간다.
		// JDBC라이브러리 불러오기
		Class.forName("com.mysql.cj.jdbc.Driver");

		//MYSQL에 연결
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + localhost + "/" + dbname, dbuser, dbpasswd);

		//MYSQL 실행
		Statement stmt = conn.createStatement();
		
		ResultSet rset = null;
		
		//출력 메서드 실행
		printFormat(rset, stmt);
		
		stmt.close(); //open한 만큼 close
		conn.close(); //open한 만큼 close 
	}
	
	//****************
	//2. 출력 형식 설정
	public static void printFormat(ResultSet rset, Statement stmt) throws SQLException {
		int cnt = printLine;	//마지막페이지를 제외하고 한 페이지당 출력할 개수를 cnt변수에 대입 => 마지막 페이지때문에 변수생성함.
		
		//sql문의 limit활용!
		//limit 시작위치, 반환개수 => 시작위치는 0부터 시작
		//예) 한 페이지당 5명 출력 = > [limit 0*5, 5] => 0부터 5개 (=0,1,2,3,4), [limit 1*5, 5] => 5부터 5개 (5,6,7,8,9)
		for (int i = 0; i * cnt < person; i++) {
			int begin = i * printLine;	//sql 문 limit begin, printLine 에 넣을 변수
//			System.out.println(begin);   //개수 확인용
			
			//마지막 페이지 출력인원 계산
			//예) i가 여러번 반복되서 최종인원 36명보다 커지면(35 + 5) > 35
			if((begin + printLine) > person) {
				printLine = person - begin;		//마지막페이지에서 출력할 인원 수 재정의 => 36-35 = 1 => 마지막에 1명만 출력
			}
			
			//헤더 출력
			HeaderPrint(i+1);	//i는 0부터 시작, page는 1부터 시작해야함 => i+1
			
			//성적 값 출력
			rset = stmt.executeQuery("select *, kor+eng+mat, (kor+eng+mat)/3 "
					+ "from scoreTable limit " + begin + "," + printLine +";");
			while (rset.next()) {
				ItemPrint(rset);		//
			}

			//현재페이지 계산값
			//해당 페이지까지만 값 계산
			rset = stmt.executeQuery("select sum(kor), sum(eng), sum(mat), sum(kor+eng+mat), sum((kor+eng+mat)/3), "
					+ "avg(kor), avg(eng), avg(mat), avg(kor+eng+mat), avg((kor+eng+mat)/3) "
					+ "from (select * from scoreTable limit " + begin + "," + printLine + ") as page" + (i+1) + ";");	//(i+1)는 현재페이지
			while (rset.next()) {
				thisPageSumPrint(rset);	//현재페이지 값 출력 => 인자 : rset
			}
			
			//누적페이지 계산값
			//누적페이지는 0행~마지막행까지 값 모두 계산
			rset = stmt.executeQuery("select sum(kor), sum(eng), sum(mat), sum(kor+eng+mat), sum((kor+eng+mat)/3), "
					+ "avg(kor), avg(eng), avg(mat), avg(kor+eng+mat), avg((kor+eng+mat)/3) "
					+ "from (select * from scoreTable limit 0," + (begin + printLine)+ ") as page" + (i+1) + ";");	//(i+1)는 현재페이지
			while (rset.next()) {
				allPageSumPrint(rset);	//누적페이지 값 출력 => 인자 : rset
			}
		} //for
	}
	
	//**************출력부*****************
	//시간 출력 메서드
	public static String timeStamp() {
		Calendar cal = Calendar.getInstance();	// 캘린더 클래스 불러와서 인스턴스 생성
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.dd HH:mm:ss");	// 날짜 출력 포멧 지정
		return sdf.format(cal.getTime());	// 초까지 출력하는 날짜 형식
	}
	
	//헤더 출력 메서드
	public static void HeaderPrint(int page) {	
		String Date = timeStamp();		// 현재시간을 string변수에 대입
		System.out.printf(" %26s\n\n", "성적집계표");
		System.out.printf(" PAGE: %d%42s\n", page, "출력일자 : " + Date);	// 출력일자 출력 => 현재시간 함께 출력
		System.out.printf("========================================================\n");	// 구분선 출력
		System.out.printf(" %-4s %-4s%6s%6s%6s%6s%7s\n", "번호", "이름", "국어", "영어", "수학", "총점", "평균");	// 항목명
		System.out.printf("========================================================\n");	// 구분선 출력
	}
	
	//성적값 출력 메서드
	//printFormat에서 rset에 입력한 각 값을 항목 자료형에 맞춰 출력 => 번호, 이름, 국어, 영어, 수학, 총점, 평균
	public static void ItemPrint(ResultSet rset) throws SQLException {
		System.out.printf(" %03d   %-6s%6d %7d %7d %7d %8.1f\n",
				rset.getInt(1), rset.getString(2), rset.getInt(3), 
				rset.getInt(4), rset.getInt(5), rset.getInt(6), rset.getDouble(7));
	}

	//******sql에 자료형을 int로 선언했어도 double로 받을 수 있다.******
	//합계 출력 메서드
	//평균은 소수점이므로 getDouble로 받는다.
	//printFormat에서 rset에 입력한 각 값을 항목 자료형에 맞춰 출력
	public static void thisPageSumPrint(ResultSet rset) throws SQLException {
		System.out.printf("========================================================\n");	// 구분선 출력
		System.out.printf(" %s\n", "현재페이지");
		System.out.printf(" %-4s%7s  %6d  %6d  %6d  %6d %8.1f\n", "합계", " ", 	// 출력형식 지정
				rset.getInt(1), rset.getInt(2), rset.getInt(3), rset.getInt(4), rset.getDouble(5));	
		System.out.printf(" %-4s%7s  %6.2f  %6.2f  %6.2f  %6.2f %8.1f\n", "평균", " ", 	// 국영수합계의 총점에 대한 평균 계산.
				rset.getDouble(6), rset.getDouble(7), rset.getDouble(8), rset.getDouble(9), rset.getDouble(10));
	}
	
	//평균 출력 메서드 - 누적 페이지출력 메서드
	//평균은 소수점이므로 getDouble로 받는다.
	//printFormat에서 rset에 입력한 각 값을 항목 자료형에 맞춰 출력
	public static void allPageSumPrint(ResultSet rset) throws SQLException {
		System.out.printf("========================================================\n");	// 구분선 출력
		System.out.printf(" %s\n", "누적페이지");	
		System.out.printf(" %-4s%7s  %6d  %6d  %6d  %6d %8.2f\n", "합계", " ", 	// 국영수합계의 합계, 평균합 계산
				rset.getInt(1), rset.getInt(2), rset.getInt(3), rset.getInt(4), rset.getDouble(5));		
		System.out.printf(" %-4s%7s  %6.2f  %6.2f  %6.2f  %6.2f %8.2f\n", "평균", " ", 	// 누적된 국영수합계의 평균, 평균의 평균 계산.
				rset.getDouble(6), rset.getDouble(7), rset.getDouble(8), rset.getDouble(9), rset.getDouble(10));	
		System.out.println();
	}
}