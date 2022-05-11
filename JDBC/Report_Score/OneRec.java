package Report_Score;
//클래스의 배열
//자바심화 7강 실습응용

public class OneRec {

	//이 클래스에서만 접근할 수 있게 private 변수로 선언
	private String name;	 // 이름
	private int student_id; 	// 학번
	private int kor; 	// 국어
	private int eng; 	// 영어
	private int mat; 	// 수학
	
	//생성자
	//이름, 국어점수, 영어점수, 수학점수를 인자로 받아 OneRec호출시마다 해당 데이터로 인스턴스를 생성
	public OneRec(int student_id, String name, int kor, int eng, int mat) {
		this.student_id = student_id;	//인자로 받은 변수들을 private 변수에 대입
		this.name = name;	//인자로 받은 변수들을 private 변수에 대입
		this.kor = kor;		//인자로 받은 변수들을 private 변수에 대입
		this.eng = eng;		//인자로 받은 변수들을 private 변수에 대입
		this.mat = mat;		//인자로 받은 변수들을 private 변수에 대입
	}

	// 이름 반환하는 메소드
	public String name() { 	
		return this.name;
	}

	// 학번 반환하는 메소드
	public int studentId() { 
		return this.student_id;
	}

	 // 국어점수 반환하는 메소드
	public int kor() {
		return this.kor;
	}

	// 영어점수 반환하는 메소드
	public int eng() {
		return this.eng;
	}

	// 수학점수 반환하는 메소드
	public int mat() { 
		return this.mat;
	}
}