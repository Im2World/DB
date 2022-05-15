#DB04강 실습하기2-시험_정답오답_정답률_난이도_3
#채점 함수 호출 및 전체조회
#조건 : 문제는 총 20문제, 문제는 5지선다형 (답 번호 1,2,3,4,5) , 문제 1개당 5점 배점

#1. 기본설정
use kopoctc;	#데이터베이스 사용

SET GLOBAL log_bin_trust_function_creators = 1; #함수 생성 전 생성자 신뢰 true로 변경 => 에러 방지!
set sql_safe_updates = 0;		#보안 해제해서 테이블내 데이터 삭제가능.
################################################################################################################

#2. 함수 선언!!
#과목번호, 학생 이름, 학번을 인자로 받고 integer를 반환하는 함수 f_grading 생성
#교수님 힌트! : 과목번호, 문제번호, 학생쓴답을 인자로 받아라. => 문제번호대신 학생 이름으로 받기.

drop function if exists f_grading; #기존에 함수 f_grading가 존재하면 제거

delimiter $$
create function f_grading(_subject integer, _name varchar(20), _id integer)	#학생제출답안 Testing테이블의 subjectID는 _subject, 매개변수로 stu_name은 _name, stu_id는 _id
returns integer		#integer형 반환
begin
declare _a01, _a02, _a03, _a04, _a05, _a06, _a07, _a08, _a09, _a10,		#문제 번호 변수 선언	
   _a11, _a12, _a13, _a14, _a15, _a16, _a17, _a18, _a19, _a20 integer;	#문제 번호 변수 선언		
declare _sum integer; #점수 합계 변수 선언

#where절 해석시 주의! => a and b and c => a가 참, b가 참, c가 참 이어야 참!
#******************************함수 알고리즘******************************
#해당 문항이 정답이면 count(*)이 올라간다. 각 문항 count(*)는 0 또는 1
#20개문항의 총 카운트 개수를 더하고, 그 값에 *5를 하면 해당 과목, 해당 학생의 점수가 계산된다.
#정답 테이블인 Answer구조 : subjectID(과목종류)-문항별 정답 => pk는 subjectID
#학생 제출 답안 테이블인 Testing 구조 : subjectID(과목종류)-stu_name(학생이름)-stu_id(학번)-문항별 정답 => pk는 subjectID, stu_id
#함수 내에서 _name을 계산에 사용하지는 않지만, 채점 테이블인 Scoring테이블에 학생이름을 집어넣기위해서 매개변수로 받는다.

	#1) from Testing as a => 학생이 제출한 답인 테이블인 Testing을 a라 별칭하고 
	#2) where (select a01 from Answer where subjectID = _subject) = a.a01 and a.subjectID = _subject and a.stu_id = _id
		#2-1) from Answer	=> 정답 테이블인 Answer 테이블에서 
		#2-2) where subjectID = _subject 	=> subjectID가 매개변수로 받은 과목번호와 같다면
		#2-3) select a01	=> 1번 정답 필드를 꺼내오고
			#즉, (select a01 from Answer where subjectID = _subject) 는 Answer에서 해당 과목의 a01의 정답
		#2-4) = a.a01	=> 그 값이 Testing테이블의 (=학생이 제출한 1번 문항 답)과 같고,
		#2-5) and a.subjectID = _subject		=> Testing테이블의 stu_id(학번)과 매개변수로 받은 학번과 같고,
		#2-6) and a.stu_id = _id	=> Testing테이블의 subjectID(과목번호)와 매개변수로 받은 과목번호가 같으면!
	#3) select count(*)	=> 그 수를 세서 => 0 또느 1
	#4) 함수의 변수 _a01 에 대입!!!!!!!!!!!!!!!
	#이 과정을 _a01부터 _a20까지 실행

#and a.subjectID = _sub 조건이 없으면 생기는 일
#1과목 a12번 정답 2
#1과목 혜임1의 a12번 제츨답2, 2과목 혜임1의 a12번 제츨답2, 3과목 혜임1의 a12번 제츨답2
#select count(*) into _a01 from TestingTest as a where (select a12 from AnswerTest where subjectID = _sub) = a.a01 and a.subjectID = _subject and a.subjectID = _sub; 시행시 1과목 a12번 카운트 3 이된다.
#where (select a01 from AnswerTest where subjectID = _sub) = a.a01 이 조건을 가지고 3과목을 전부 돌렸는데, 우연히 혜임1의 1과목, 2과목, 3과목 제출답이 2이고, 1과목 정답도 2이기 때문.

select count(*) into _a01 from Testing as a where (select a01 from Answer where subjectID = _subject) = a.a01 and a.subjectID = _subject and a.stu_id = _id;	#자세한 설명을 위해 위에서 설명했다.
select count(*) into _a02 from Testing as a where (select a02 from Answer where subjectID = _subject) = a.a02 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a03 from Testing as a where (select a03 from Answer where subjectID = _subject) = a.a03 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a04 from Testing as a where (select a04 from Answer where subjectID = _subject) = a.a04 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a05 from Testing as a where (select a05 from Answer where subjectID = _subject) = a.a05 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a06 from Testing as a where (select a06 from Answer where subjectID = _subject) = a.a06 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a07 from Testing as a where (select a07 from Answer where subjectID = _subject) = a.a07 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a08 from Testing as a where (select a08 from Answer where subjectID = _subject) = a.a08 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a09 from Testing as a where (select a09 from Answer where subjectID = _subject) = a.a09 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a10 from Testing as a where (select a10 from Answer where subjectID = _subject) = a.a10 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a11 from Testing as a where (select a11 from Answer where subjectID = _subject) = a.a11 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a12 from Testing as a where (select a12 from Answer where subjectID = _subject) = a.a12 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a13 from Testing as a where (select a13 from Answer where subjectID = _subject) = a.a13 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a14 from Testing as a where (select a14 from Answer where subjectID = _subject) = a.a14 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a15 from Testing as a where (select a15 from Answer where subjectID = _subject) = a.a15 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a16 from Testing as a where (select a16 from Answer where subjectID = _subject) = a.a16 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a17 from Testing as a where (select a17 from Answer where subjectID = _subject) = a.a17 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a18 from Testing as a where (select a18 from Answer where subjectID = _subject) = a.a18 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a19 from Testing as a where (select a19 from Answer where subjectID = _subject) = a.a19 and a.subjectID = _subject and a.stu_id = _id;
select count(*) into _a20 from Testing as a where (select a20 from Answer where subjectID = _subject) = a.a20 and a.subjectID = _subject and a.stu_id = _id;

set _sum = _a01+_a02+_a03+_a04+_a05+_a06+_a07+_a08+_a09+_a10+_a11+_a12+_a13+_a14+_a15+_a16+_a17+_a18+_a19+_a20; #합계 계산

#Scoring테이블 구조 : subjectID, stu_name, stu_id,a01,a02 ... a20, score
insert into Scoring values(_subject, _name, _id,
							_a01, _a02, _a03, _a04, _a05, _a06, _a07, _a08, _a09, _a10, 
							_a11, _a12, _a13, _a14, _a15, _a16, _a17, _a18, _a19, _a20, _sum*5); #점수 테이블에 구한 값들 insert, 점수는 합계 * 5
return _sum*5; #점수 반환

end $$
delimiter ;


################################################################################################################
#3. 함수 사용해 데이터 출력!!!

delete from Scoring where stu_id>0;	# 점수 계산 테이블인 Scoring 테이블에 값이 있다면 모두 제거
select f_grading(subjectID, stu_name, stu_id) from Testing; #학생 답안과 정답 비교해서 채점하는 함수 호출
select * from Scoring;	#Scoring테이블의 모든 행 출력