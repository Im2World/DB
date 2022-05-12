#DB03강 Training(1)
#examtable 1000개 데이터 만들기
#Call print_report(5,25) # 25개씩 출력할때 5번째 페이지의 결과 테이블을 만드시오

use kopoctc;		#DB사용

#제거 모음
drop table if exists examtable1000;	#테이블 있으면 지워라
drop procedure if exists insert_examtable1000;	# 같은 이름의 프로시져가 존재한다면 삭제
drop procedure if exists print_report;	# 같은 이름의 프로시져가 존재한다면 삭제


#실습1 : 테이블 생성
create table examtable1000(		#테이블명 examtable1000
	id int not null primary key,	#id가 pk
	name varchar(20),		#name 필드는 가변크기 20
    kor int, eng int, mat int);		#각 과목 필드는 int형으로 선언
desc examtable1000;	#examtable 구조 출력
select * from examtable1000;		#테이블 출력


#실습2 : examtable1000 테이블에 학생 랜덤성적 대입
#이름은 [학생+문자4바이트] => 학생연번 + 숫자 4자까지 ex)학생1 학생2 ... 학생999, 학생 1000
#integer를 cast로 형변환해서 [학생]에 concat
DELIMITER $$
CREATE PROCEDURE insert_examtable1000(_last integer)		#insert_examtable1000라는 이름의 integer인자를 받는 프로시져 생성
	BEGIN
	DECLARE _name varchar(20);		#프로시저 내에서 사용할 변수명 자료형 선언
	DECLARE _id integer;			#declare 안에서 생성한 변수는 모두 프로시저안에서 사용해야한다.
	DECLARE _cnt integer;			#프로시저 안의 변수는 접두사로 _를 사용해서 DB필드명과 구분! 
	SET _cnt = 0;		#_cnt값을 0으로 셋팅.

		_loop: LOOP						#반복문 시작, 반복문명 : _loop
			SET _cnt = _cnt + 1;		#반복문 시작하면, _cnt값 1증가
 			SET _id = _cnt; 			#_id변수에 _cnt값 대입          
			SET _name = concat("학생", cast(_cnt as char(4)));	#integer형인 _cnt를 char형으로 형변환 후, concat함수로 학생 이라는 단어와 연결해 한단어로 출력
			
			INSERT INTO examtable1000 VALUE (_id, _name, rand()*100, rand()*100, rand()*100);	#examtable1000 테이블에 _id, _name, 각과목 점수(0~100까지 랜덤생성) 대입
			
			IF _cnt = _last THEN		#만약 _cnt가 인자로 받은 _last와 같다면
				LEAVE _loop;			#반복문 종료
			END IF;			#조건문 종료
		END LOOP _loop;		#반복문 완전 탈출!!!	
	END $$	
DELIMITER ;

#프로시저 실행
call insert_examtable1000(1000);			#성적 테이블에 데이터 1000개 대입

#테이블 출력
select * from examtable1000;


#실습3 : 성적집계표 출력 프로시저 => select 문 1 = 현재페이지, select 문 2 = 누적페이지 출력
#print_report(출력할 해당 페이지, 페이지당 출력인원) => (3, 40) => 40명씩 출력했을 때 3번 째 페이지 출력
#해당 프로시저 안에서 select문 3개 수행
DELIMITER $$
create procedure print_report(_page integer,_printNum integer)		#print_report라는 이름의 프로시저 생성, 매개변수로 2개를 받는다.
	begin
    	declare _inputPage integer;			#프로시저 내에서 사용할 변수명 자료형 선언		
        declare _printPersonNum integer;	#declare 안에서 생성한 변수는 모두 프로시저안에서 사용해야한다.
		declare _startLine integer;			#프로시저 안의 변수는 접두사로 _를 사용해서 DB필드명과 구분!        
		declare _lastPage integer;       	#integer 형 변수선언
		declare _lastline integer;       	#integer 형 변수선언
    
		set _inputPage = _page;				#_inputPage에 _page대입
        set _printPersonNum = _printNum;	#_startLine에 _printNum대입
		set _startLine = (_inputPage - 1) * _printPersonNum;	#_startLine에는 (_inputPage - 1) * _printPersonNum대입
		set _lastPage = ceil(1000 / _printPersonNum);			#_lastPage는 1000 / _printPersonNum를 올림한 값 대입
		set _lastline = _inputPage * _printPersonNum;			#_lastline에는 _inputPage와 _printPersonNum의 곱 대입
    
		if _inputPage <= 1 then		#만약, _inputPage가 1과 같거나 미만이라면,
			set _startLine = 0;		#_startLine를 0으로 설정
		end if;    		#조건문 종료
		
        if _inputPage >= _lastPage then		#만약, _inputPage가 _lastPage보다 크거나 같다면,
			set _inputPage = _lastPage;		#_inputPage는 _lastPage로 설정
            set _lastline = 999;			#_lastline은 999로 설정 => limit으로 출력 개수를 설정하기 때문에 총 데이터 1000개에서 -1한 값이 마지막 라인이다.
		end if;    		#조건문 종료            
        
        #츨력할 테이블들
        #출력1 - 해당 페이지 출력
        #등수에서 count(*)+1을 하는 이유는, 나보다 성적높은애가 있다면, 내 등수가 밀리기때문
		select id as 번호, name as 이름, kor as 국어, eng as 영어, mat as 수학, b.kor+b.eng+b.mat as 총점,(b.kor+b.eng+b.mat)/3 as 평균,	#2) examtable1000테이블의 각 칼럼을 번호, 이름, 국어, 영어, 수학, 총점, 평균이라고 별칭
			(select count(*)+1 from examtable1000 as a where (a.kor+a.eng+a.mat) > (b.kor+b.eng+b.mat)) as 등수	#3) examtable1000테이블을 a라고 별칭, a테이블 과목총점이 b테이블 총점보다 크다면, 전체 필드의 레코드수 + 1 한 값을 등수라고 별칭
            from examtable1000 as b			#1) examtable1000테이블을 b라고 별칭
            order by id		#4) id로 오름차순 정렬
            limit _startLine, _printPersonNum;		#5) examtable1000테이블을 _startLine 부터 _printPersonNum개 출력
		
        #출력2	- 해당 페이지 계산
		select sum(kor) as 국어총점, sum(eng) as 영어총점, sum(mat) as 수학총점, sum(kor+eng+mat) as 총점, sum((kor+eng+mat)/3) as 평균합,		#2) 각 항목에 별칭을 부여해 사용자가 테이블을 보기 편하게한다.
			avg(kor) as 국어평균, avg(eng) as 영어평균, avg(mat) as 수학평균, avg(kor+eng+mat) as 총점의평균, avg((kor+eng+mat)/3) as 평균의평균		#3) sum()은 ()안의 총합 계산, avg()는 ()안의 평균을 계산하는 내장함수다.
            from (select kor, eng, mat from examtable1000 limit _startLine, _printPersonNum) as 현재페이지; 		#1) examtable1000에서 kor, eng, mat를 _startLine부터 _printPersonNum개 지정한 범위에서 2), 3) 계산
            
        #출력3   	- 해당 페이지까지 누적값 계산 
		select sum(kor) as 국어누적총점, sum(eng) as 영어누적총점, sum(mat) as 수학누적총점, sum(kor+eng+mat) as 누적총점, sum((kor+eng+mat)/3) as 평균누적합,		#2) 각 항목에 별칭을 부여해 사용자가 테이블을 보기 편하게한다.
			avg(kor) as 국어평균, avg(eng) as 영어평균, avg(mat) as 수학평균, avg(kor+eng+mat) as 총점의평균, avg((kor+eng+mat)/3) as 평균의평균		#3) sum()은 ()안의 총합 계산, avg()는 ()안의 평균을 계산하는 내장함수다.
            from (select kor, eng, mat from examtable1000 limit 0, _lastline) as 누적페이지;       		#1) examtable1000에서 kor, eng, mat를 0부터 _lastline까지 지정한 범위에서 2), 3) 계산 => 누적출력이라 0부터 시작이다.
	end $$
DELIMITER ;

#프로시저 실행
call print_report(3, 499);		#한 페이지당 499명씩 출력했을 때, 3번째 페이지를 출력

