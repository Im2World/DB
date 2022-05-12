#03강 실습하기 - Basic Training1
#examtable100개 데이터 생성

use kopoctc;	#데이터베이스 사용

#제거 모음 : 잘못생성하면 엉키기때문에 먼저 기존 애들을 제거해준다.
drop table if exists examtable100;	#이미 테이블 있으면 제거
DROP PROCEDURE IF EXISTS insert_examtable100;	# 같은 이름의 프로시져가 존재한다면 삭제
DROP FUNCTION IF EXISTS f_get_ranking; #함수가 이미 있다면 삭제
#delete from examtable100 where id > 0; #테이블의 기존값들 제거

########################
#실습1 : 테이블 생성
create table examtable100(
	id int not null primary key,	#id가 pk
	name varchar(20),
    kor int, eng int, mat int);
desc examtable100;	#examtable 구조 출력
select * from examtable100;		#테이블 출력

###########################
#실습2 : examtable100 테이블에 학생 랜덤성적 대입
#이름은 [학생+문자4바이트] => 학생연번 + 숫자 4자까지 ex)학생1 학생2 ... 학생999, 학생 1000
#integer를 cast로 형변환해서 [학생]에 concat
DELIMITER $$
CREATE PROCEDURE insert_examtable100(_last integer)		#인자가 1개인 프로시저
	BEGIN
	DECLARE _name varchar(20);		#declare에 선언한 변수들은 모두 프로시저 내에서 사용되어야만 한다.
	DECLARE _id integer;		#프로시저 내에서 사용할 변수들은 필드명과의 구분을 위해 _ 를 접두사로 사용한다.
	DECLARE _cnt integer;
	SET _cnt = 0;		#_cnt 값을 0으로 세팅

		_loop: LOOP		#반복문 시작 반복문 이름은 _loop
			SET _cnt = _cnt + 1;	#_cnt는 반복시마다 1씩 증가
 			SET _id = 202200 + _cnt;    #학번을 생성한다       
			SET _name = concat("학생", cast(_cnt as char(4)));		#cast함수로 integer형인 _cnt를 char형으로 형변환, concat으로 학생과 _cnt를 한 단어로 연결해준다.
			
			INSERT INTO examtable100 VALUE (_id, _name, rand()*100, rand()*100, rand()*100);	#examtable100에 5개 값을 넣어준다. kor, eng, mat 성적은 0~100점 사이 랜덤값
			
			IF _cnt = _last THEN		#if조건문. => _cnt가 _last와 같다면,
				LEAVE _loop;	#반복문 종료.
			END IF;		#if문 종료
		END LOOP _loop;
	END $$
DELIMITER ;

#프로시저 실행
call insert_examtable100(100);			#성적 테이블에 데이터 100개 대입

#위에서 시행한 데이터 출력
select * from examtable100;
select count(*) from examtable100;		#examtable100 레코드수 출력

#실습1-2 : 학번, 이름, 국어, 영어, 수학, 총점, 평균, 등수를 출력
select *, kor+eng+mat as sum, (kor+eng+mat) / 3 as avg from examtable100;

#실습1-3 : 등수를 기준으로 오름차순 출력
#합계가 > 파라미터로 전달된 합계치보다 큰 곳을 찾아 전부 count를 해주고 자기자신을 포함시켜 순위를 작성.
select *, b.kor+b.eng+b.mat as sum,(b.kor+b.eng+b.mat)/3 as avg,(select count(*)+1 from examtable100 as a where (a.kor+a.eng+a.mat) > (b.kor+b.eng+b.mat)) as ranking
from examtable100 as b order by ranking;

####################################################
#등수 출력 함수 생성
#Error Code : 1418 해결 위해 OFF를 ON으로 변경
show global variables like 'log_bin_trust_function_creators';
SET Global log_bin_trust_function_creators = 'ON';

DELIMITER $$
create function f_get_ranking(_id integer)		#f_get_ranking라는 함수생성 => integer를 인자로 받는다.
	returns integer
	begin
		declare _ranking integer;	#_ranking 라는 변수 선언
		select (select count(*)+1 from examtable100 as a where (a.kor+a.eng+a.mat) > (b.kor+b.eng+b.mat)) into _ranking		#3) examtable100을 a라고 별칭, a테이블의 과목 총합이 b테이블 과목 총합보다 크다면, 전체레코드수 +1 을 _ranking에 대입
		from examtable100 as b		#1) examtable100이라는 테이블을 b라고 별칭
		where id=_id;		#2) 필드 id가 함수 매개인자로 받는 _id와 같다면,
	return _ranking;	#4) _ranking을 반환한다.
end $$
DELIMITER ;

#위에서 생성한 함수 이용방법 => 함수 실행결과 확인
#실습1-3
#examtable100 테이블에서 전체 필드 출력, 과목 총합을 sum이라는 별칭으로 출력, 과목 평균을 avg라는 별칭으로 출력, f_get_ranking(id)반환 결과를 ranking으로 출력하고 ranking으로 오름차순 출력
select *, kor+eng+mat as sum, (kor+eng+mat) / 3 as avg, f_get_ranking(id) as ranking from examtable100 order by ranking;
