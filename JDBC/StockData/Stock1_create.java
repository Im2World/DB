package stock;
//create

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Training : 일자별 주가 데이터정보로 실습, 테이블 만들기
// (실습1 table생성,지움 (단 [단축코드, 일자, 시가,고가,저가,종가, 거래량, 거래대금] 만으로 테이블 생성)


public class Stock1_create {
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
				
		/*(3)일자별 데이터를 전부 넣을 테이블*/
		stmt.execute("create table StockDailyPrice(" +
	   			"shrn_iscd varchar(200), " +  //유가증권 단축 종목코드 
				"bsop_date int, "+ //  주식 영업 일자
				"stck_oprc int, " + //  주식 시가
				"stck_hgpr int, " + // 주식 최고가 
				"stck_lwpr int, " + //  주식 최저가
				"stck_prpr int, " +  // 주식 종가 
				"acml_vol bigint, " + // 누적 거래량 => 거래대금이 얼마나될지 몰라서 bigint사용
				"acml_tr_pbmn bigint, " + // 누적 거래 대금 => 거래대금이 얼마나될지 몰라서 bigint사용
				"CONSTRAINT pmk PRIMARY KEY (shrn_iscd, bsop_date));");			//복합 프라이머리키 : 단축종목코드, 주식영업일자		
		
		stmt.close(); //open한 만큼 close
		conn.close(); //open한 만큼 close
	}
}