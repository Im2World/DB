package Report_Score;

//Drop table
//테이블 제거

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class report_DropTable {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
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
		
		stmt.execute("drop table scoreTable;"); //테이블 제거 => 결과 출력이아니라서 execute사용
		
		stmt.close();	// Statement 인스턴스 닫기
		conn.close();	// Connection 인스턴스 닫기
	}

}
