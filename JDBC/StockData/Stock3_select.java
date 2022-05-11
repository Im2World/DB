package stock;
//select
//항목명 수정 필요

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 *  Training : 일자별 주가 데이터정보로 실습, 테이블 만들기
 */

public class Stock3_select {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		
		/*처리시간을 찍어줄 데이터*/
		Date start = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
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

		String QueryTxt;
		
		//실습1] 데이터 전체 출력
//		QueryTxt = "select * from StockDailyPrice";
		
		
		//실습2] 삼성전자 단축코드로 조회
		QueryTxt = "select * from StockDailyPrice where shrn_iscd = 'A005930";	
		
		
		//실습3] 특정일자 조회 => 
//		QueryTxt = "select * from StockDailyPrice where bsop_date = '20110708'";	
		
		
		ResultSet rset = stmt.executeQuery(QueryTxt);
		int iCnt=0;
		while(rset.next()) {
			System.out.printf("*(처리건수:%d)*****************************************\n",iCnt++);
			System.out.printf("단축코드	:%s\n",rset.getString(1));
			System.out.printf("일자		:%s\n",rset.getString(2));
			System.out.printf("시가		:%s원\n",rset.getString(3));
			System.out.printf("고가		:%s원\n",rset.getString(4));
			System.out.printf("저가		:%s원\n",rset.getString(5));
			System.out.printf("종가		:%s원\n",rset.getString(6));
			System.out.printf("거래량		:%s주\n",rset.getString(7));
			System.out.printf("거래대금	:%s원\n",rset.getString(8));
	
		}
		Date end = new Date();
		System.out.println("*(처리종료)*****************************************\n");
		System.out.println("End : " + sdf.format(end));
		System.out.printf("DB실행 시작시간 :%s, DB 종료시각 :%s",sdf.format(start),sdf.format(end));

		rset.close();
		stmt.close(); 
		conn.close(); 


	}

}