package Report_Score;
//Ŭ������ �迭
//�ڹٽ�ȭ 7�� �ǽ�����

public class OneRec {

	//�� Ŭ���������� ������ �� �ְ� private ������ ����
	private String name;	 // �̸�
	private int student_id; 	// �й�
	private int kor; 	// ����
	private int eng; 	// ����
	private int mat; 	// ����
	
	//������
	//�̸�, ��������, ��������, ���������� ���ڷ� �޾� OneRecȣ��ø��� �ش� �����ͷ� �ν��Ͻ��� ����
	public OneRec(int student_id, String name, int kor, int eng, int mat) {
		this.student_id = student_id;	//���ڷ� ���� �������� private ������ ����
		this.name = name;	//���ڷ� ���� �������� private ������ ����
		this.kor = kor;		//���ڷ� ���� �������� private ������ ����
		this.eng = eng;		//���ڷ� ���� �������� private ������ ����
		this.mat = mat;		//���ڷ� ���� �������� private ������ ����
	}

	// �̸� ��ȯ�ϴ� �޼ҵ�
	public String name() { 	
		return this.name;
	}

	// �й� ��ȯ�ϴ� �޼ҵ�
	public int studentId() { 
		return this.student_id;
	}

	 // �������� ��ȯ�ϴ� �޼ҵ�
	public int kor() {
		return this.kor;
	}

	// �������� ��ȯ�ϴ� �޼ҵ�
	public int eng() {
		return this.eng;
	}

	// �������� ��ȯ�ϴ� �޼ҵ�
	public int mat() { 
		return this.mat;
	}
}