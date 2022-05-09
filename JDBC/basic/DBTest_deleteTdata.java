package DBTest;
//import할 때 java.sql로 할 것.

//테이블 자료 삭제 기본 형식
//단계 : Connection - Statement - ResultSet
//콘솔창에 결과가 출력된다면, ResultSet rset = stmt.executeQuery 사용
//그렇지 않다면, execute

//executeQuery : select
//execute : create, delete


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest_deleteTdata {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		// MySQL DB에 로그인하기 위한 정보
		String localhost = "192.168.23.91:33060";
		String dbname = "kopoctc";
		String dbuser = "root";
		String dbpasswd = "kopo38";

		// JDBC버전 따라서 com.mysql.cj.jdbc.Driver 사용
		Class.forName("com.mysql.cj.jdbc.Driver");

		// connection j.jar파일의 기능사용위한 객체생성
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + localhost + "/" + dbname, dbuser, dbpasswd);

		// connection j.jar파일의 기능사용위한 객체생성2
		Statement stmt = conn.createStatement();

		// 형식 ResultSet rset = stmt.executeQuery("select * from table");
		// ResultSet 이 필요없다.
		// 콘솔창에 결과 출력하는게 아니라 데이터베이스 테이블의 자료를 삭제하는 것이라서 execute 함수 사용
		//executeQuery : select
		//execute : create, delete
		stmt.execute("delete from examtable;");
		
		//자원해제
		stmt.close();
		conn.close();
	} // main
} // class
