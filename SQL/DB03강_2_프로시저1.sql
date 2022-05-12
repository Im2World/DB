#3강 procedure
#sql문장에서 Procedure형식 => call procedure명()
#정식 명칭 : 스토어드 프로시저
#declare - set - sql 문장 순이다.
#examtable에 성적 들어있다.

# 같은 이름의 프로시져가 존재한다면 삭제
DROP PROCEDURE IF EXISTS get_sum;

#문장구분을 쉽게 하기 위해 구분자를 ;에서 $$로 변경, DELIMITER $$ 뒤에 주석 금지!
#1. 프로시저 생성 : IN, OUT을 정의
# 프로시저 생성 : IN으로 파라미터 정의, OUT으로 테이블에서 가져올 값 정의

#2. BEGIN 프로시저 시작 : 프로시저 안에서 사용되는 변수 지정
#변수 정의할 때 DECLARE ~ set으로 정의하고 값넣기
#set으로 값 변수 값 초기화
#select는 sql에서 필요한 문장 => DB필드명 => id값으로 셀렉한 내용을 변수에 저장 => id는 DB의 필드명, _id는 라인 7의 파라미터
DELIMITER $$
CREATE PROCEDURE get_sum (
	IN _id integer,
    OUT _name varchar(20),
    OUT _sum integer
)
	BEGIN
		DECLARE _kor integer;
		DECLARE _eng integer;
		DECLARE _mat integer;
		set _kor = 0;
		set _eng = 0;
		set _mat = 0;
		
		select name, kor, eng, mat 
			into _name, _kor, _eng, _mat from examtable where studentid = _id;
			
		set _sum = _kor + _eng + _mat;
	END $$
DELIMITER ; #구분자 되돌리기

#프로시저 호출 : call
#@는 프로시저에서 사용하는 문법
call get_sum(209901, @name, @sum); #프로시저 호출하여 저장=> 형식: @변수명 
select @name, @sum; #결과값 출력 => @를 쳐야한다.