package DBTest;
//import할 때 java.sql로 할 것.

//기본 형식이다!
//테이블 데이터 조회
//단계 : Connection - Statement - ResultSet
//콘솔창에 결과가 출력된다면, ResultSet rset = stmt.executeQuery 사용
//그렇지 않다면, execute

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest_selectT {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

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

		// 형식 ResultSet rset = stmt.executeQuery("select * from table");
		// 테이블 생성 시에는 ResultSet 이 필요없다.
		ResultSet rset = stmt.executeQuery("select * from examtable;"); // 결과가 나오는 애는 executeQuery함수로 실행

		// 결과물 처리
		// 주의 : sql은 인덱스를 1부터 시작한다.
		//rset.next 로 다음 레코드를 읽는다.
		System.out.printf("  이름	 학번 	국어   영어    수학\n");
		while (rset.next()) {
			System.out.printf("%4s	%6d	%3d	%3d	%3d \n", 
					rset.getString(1), rset.getInt(2), rset.getInt(3), rset.getInt(4), rset.getInt(5));//각 해당 값을 해당하는 string과 int에 값을 출력
		}
		//자원해제
		rset.close();
		stmt.close();
		conn.close();
	} // main
} // class
