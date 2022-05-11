package Report_Score;
//성적집계표 만들기 1
//테이블생성
//BasicTraining2 : 성적집계표 테이블 생성
//조건1 : 테이블 생성,지움 :학번, 이름, 국어, 영어, 수학의 성적테이블 생성 (학번이 primary key)
//조건2 : 값 입력 : 테이블 값은 랜덤함수를 이용하여, 1000명의 데이터를 집어 넣을 것

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CreateTable {
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
		
		ArrayList<OneRec> reportData = dataSet();	//dataSet 반환 값을 reportData에 대입
		
		stmt.execute(makeTable());	// 메서드 makeTable 실행 => 테이블 생성
		stmt.execute(insertData(reportData));	//값 입력 => 각 학생 학번, 이름, 국어, 영어, 수학 점수를 메서드 insertData의 인자로 넘긴다.
		
		stmt.close(); //open한 만큼 close
		conn.close(); //open한 만큼 close 							
	}
	
	//1. 테이블 생성
	//항목으로 학번, 학생명, 국어점수, 영어점수, 수학점수	
	static public String makeTable() {
		String QueryTxt = "create table scoreTable(" 						
				+ "stu_id int not null primary key, " 	
				+ "stu_name varchar(50), "					
				+ "kor int, "						
				+ "eng int, " 								
				+ "mat int)" 					
				+ "DEFAULT CHARSET=utf8;";
		return QueryTxt;	//반환
	}
	
	//2. 데이터 넣기
	//아래 OneRec 클래스에서 생성한 점수들을 여기에서 데이터로 넣는다.
	//dataSet의 값을 입력받은 reportData를 인자로 받는 메서드
	static public String insertData(ArrayList<OneRec> reportData) {
		StringBuffer sb = new StringBuffer();		//값 입력위해 StringBuffer 인스턴스생성
		String QueryTxt = "insert into scoreTable("
				+ "stu_id, stu_name, kor, eng, mat)"
				+ " values ";
		//데이터 크기만큼 반복
		for (int i = 0; i < reportData.size(); i++) {
			OneRec person = reportData.get(i);	//인자를 하나씩 받는다.
			sb.append(String.format("(%s, '%s', %s, %s, %s), ",
					person.studentId(), person.name(), person.kor(), person.eng(), person.mat()));	//sb에 값 추가
		}
		
		// substring(begin, end) begin,end는 인덱스이고, end 앞까지만 잘라온다.
		//sb.length()값은 인덱스+1이다.
		//=> sb.substring(0, sb.length()-2)
		//sql문에서 한 줄 명령 종료는 ;
		QueryTxt += sb.substring(0, sb.length()-2) + ";";		//sb에 추가된 값을 계속 추가
		return QueryTxt;	//반환
	}

	//3. 학생들 점수 랜덤 생성
	public static ArrayList<OneRec> dataSet() {
		ArrayList<OneRec> ArrayOneRec = new ArrayList<OneRec>();	//ArrayList ArrayOneRec생성
		int person = 1000;	//학생수 1000명
		
		//반복문실행해 학번, 학생이름, 국어, 영어, 수학 점수 생성
		//(Math.random()으로 각 과목 점수생성: 0~100점
		//학번은 20000 ~ 20999
		for (int i = 0; i < person; i++) {					
			String name = String.format("학생%02d", i);	
			int kor = (int)(Math.random() * 100);
			int eng = (int)(Math.random() * 100);
			int mat = (int)(Math.random() * 100);
			ArrayOneRec.add(new OneRec((20000+ i), name, kor, eng, mat));	// 하나의 OneRec클래스 생성 후 ArrayList에 집어넣음
		}
		return ArrayOneRec;		//반환
	}	//ArrayList<OneRec> dataSet()
}