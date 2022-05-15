#DB04강 실습하기2-시험_정답오답_정답률_난이도_4
#과목별, 문제별 득점자 수 와 득점률 리포트 작성
#함수 호출 및 전체조회

#1. 리포트 테이블에 데이터 넣을 함수 생성 및 호출
use kopoctc;   #데이터베이스 사용
SET GLOBAL log_bin_trust_function_creators = 1;      #함수 생성을 위해 value를 on으로 변경

drop function if exists f_getReport;    #기존에 함수 존재한다면, 제거

#리포트테이블에 데이터 넣을 함수 생성시작!
delimiter $$
create function f_getReport (_id integer)       #리포트테이블에 데이터 넣을 함수 f_getReport 
returns boolean         #반환값 자료형은 boolean
begin
	declare _kor, _eng, _mat integer;                      #함수내에서 사용할 과목 변수선언
    declare _name varchar(20);                         #stu_name에 해당하는 name변수를 varchar 20크기로 선언

    select stu_name into _name from Scoring where stu_id = _id and subjectID = 1;	#채점 테이블인 Scoring테이블의 stu_id가 함수 인자로 받은 _id와 같고, subjectID가 1이면 stu_name를 _name에 저장=>학생 이름은 과목별로도 순서동일
    select score into _kor from Scoring where stu_id = _id and subjectID = 1; 		#채점 테이블인 Scoring테이블의 stu_id가 함수 인자로 받은 _id와 같고, subjectID가 1이면 score를 _kor에 저장 =>해당학생의 국어점수 입력
    select score into _eng from Scoring where stu_id = _id and subjectID = 2;	#채점 테이블인 Scoring테이블의 stu_id가 함수 인자로 받은 _id와 같고, subjectID가 2이면 score를 _eng 에 저장 =>해당학생의 영어점수 입력
    select score into _mat from Scoring where stu_id = _id and subjectID = 3;	#채점 테이블인 Scoring테이블의 stu_id가 함수 인자로 받은 _id와 같고, subjectID가 3이면 score를 _mat 에 저장 =>해당학생의 수학점수 입력

    insert into Reporttable values(_name, _id, _kor, _eng, _mat);	#채점결과 출력할 Reporttable에 
    return true;         #true 값 리턴
end $$
delimiter ;

#함수호출
delete from Reporttable where stu_id > 0;                      #함수시행전 테이블의 기존 데이터 제거
select f_getReport(stu_id) from Scoring where subjectID = 1;   #id중복 없이 집어넣기 위해 과목번호 지정하여 id조회 및 function 호출
select * from Reporttable;                                     #값이 잘 들어갔는지 확인위해 Reporttable출력

###########################################################################################

#2. 리포트 테이블의 전체 내용 조회
select *, (kor+eng+mat) as sum, (kor+eng+mat)/3 as ave,      #2) Reporttable테이블의 모든 필드 출력, 과목총점, 평균
        (select count(ave) + 1 from Reporttable as b where (a.kor+a.eng+a.mat) < (b.kor+b.eng+b.mat)) as ranking 
#3) Reporttable을 b라고 칭한뒤 등수계산 후 필드명을 ranking이라 별칭
    from Reporttable as a  #1) Reporttable 테이블을 a라 칭하고,
order by ranking;   # 4) rangking 기준 오름차순 출력

###########################################################################################

#3. 과목별, 문제별 득점자 수 및 득점률 계산 프로시져
#프로시져기능
#1) 상세리포트테이블 (reportDetail)생성
#2) reportDetail테이블에 값 입력
#3) reportDetail테이블 조회1 - 과목별로 문제번호순으로 오름차순
#4) reportDetail테이블 조회2 - 과목별로 정답률 높은순으로 출력

drop procedure if exists input_reportDetail;	#프로시져 실행전 기존에 있던 프로시져 제거

#프로시져 생성 시작
delimiter $$
create procedure input_reportDetail()      		 #프로시져이름 input_reportDetail
begin
declare _cnt integer;                      		 #과목 번호 변수선언 => 과목 3개라서 1,2,3 까지만 카운트
declare _stuCnt integer;						 #학생 수 변수

drop table if exists reportDetail;         		 #상세 리포트 테이블 생성

create table reportDetail (					#reportDetail 테이블 구조 : subjectID, qnID, scorer(해당문제 득점자 수), answerRate(해당문제 정답률 퍼센트)
    subjectID integer not null,				#subjectID 필드 생성, null값 비허용, pk임.
    qnID varchar(10) not null,				#qnID 는 questionId (문제번호)를 의미
    scorer integer,							#해당문제 득점자 수
    answerRate double,							#해당문제 정답률은 double형!
    primary key(subjectID, qnID));			#pk는 복합pk로, subjectID,  qnID 설정
    
    set _cnt = 0;                           #subjectID 구분위한 _cnt변수, 초기값 0으로 세팅
    set _stuCnt = 1000;						#학생 수 지정
    
    _loop: loop                           	#반복문 시작, 반복문명 : _lopp
            set _cnt = _cnt + 1;            #첫 시행시 subjectID 1에 해당하는 값 부터 실행 => 반복문 돌면 1씩 증가하면서, [subjectID, qnID, scorer, answerRate]데이터를  reportDetail 테이블에 입력
            #문항별로 정답자수와 정답률 확인
            #reportDetail 테이블 구조 : subjectID, qnID, scorer(해당문제 득점자 수), answerRate(해당문제 정답률 퍼센트)
            #1) insert into reportDetail => reportDetail 테이블에 값을 넣는다.
            #2) values (_cnt,	=> 첫번째 값은 subjectID
            #3) "a01",			=> 두번째 값은 qnID
            #4) (select sum(a01) from Scoring where subjectID = _cnt),
				#4-1) 세번째 값은 채점결과 테이블 Scoring 테이블에서 subjectID가 프로시져과목변수인 _cnt와 동일하다면,
                #4-2) a01번 문제의 모든학생 정답 수인 sum(a01)을 출력
            #5) (select (sum(a01)/1000)*100 from Scoring where subjectID = _cnt)
				#5-1) 네번째 값은 채점결과 테이블 Scoring 테이블에서 subjectID가 프로시져과목변수인 _cnt와 동일하다면,
				#5-2) (해당문제 정답수/총학생수)*10 즉, 정답률 출력
            #6) 이 과정을 문제 1번부터 문제20번까지 반복
            insert into reportDetail values (_cnt, "a01", (select sum(a01) from Scoring where subjectID = _cnt), (select (sum(a01)/_stuCnt)*100 from Scoring where subjectID = _cnt));	#위에서 설명함.
            insert into reportDetail values (_cnt, "a02", (select sum(a02) from Scoring where subjectID = _cnt), (select (sum(a02)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a03", (select sum(a03) from Scoring where subjectID = _cnt), (select (sum(a03)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a04", (select sum(a04) from Scoring where subjectID = _cnt), (select (sum(a04)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a05", (select sum(a05) from Scoring where subjectID = _cnt), (select (sum(a05)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a06", (select sum(a06) from Scoring where subjectID = _cnt), (select (sum(a06)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a07", (select sum(a07) from Scoring where subjectID = _cnt), (select (sum(a07)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a08", (select sum(a08) from Scoring where subjectID = _cnt), (select (sum(a08)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a09", (select sum(a09) from Scoring where subjectID = _cnt), (select (sum(a09)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a10", (select sum(a10) from Scoring where subjectID = _cnt), (select (sum(a10)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a11", (select sum(a11) from Scoring where subjectID = _cnt), (select (sum(a11)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a12", (select sum(a12) from Scoring where subjectID = _cnt), (select (sum(a12)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a13", (select sum(a13) from Scoring where subjectID = _cnt), (select (sum(a13)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a14", (select sum(a14) from Scoring where subjectID = _cnt), (select (sum(a14)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a15", (select sum(a15) from Scoring where subjectID = _cnt), (select (sum(a15)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a16", (select sum(a16) from Scoring where subjectID = _cnt), (select (sum(a16)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a17", (select sum(a17) from Scoring where subjectID = _cnt), (select (sum(a17)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a18", (select sum(a18) from Scoring where subjectID = _cnt), (select (sum(a18)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a19", (select sum(a19) from Scoring where subjectID = _cnt), (select (sum(a19)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            insert into reportDetail values (_cnt, "a20", (select sum(a20) from Scoring where subjectID = _cnt), (select (sum(a20)/_stuCnt)*100 from Scoring where subjectID = _cnt));
            
            if _cnt = 3 then   	#만약, 과목번호 변수인 _cnt가 3이라면 3과목을 모두 입력했으므로
				leave _loop;    #반복문 종료
			end if;      		#조건문 종료
        end loop _loop;			#반복문 종료
    #############################################################################
	 #reportDetail테이블 조회1 - 과목별로 문제번호순으로 오름차순
	select * from reportDetail where subjectID = 1 order by qnID;	#kor 성적
	select * from reportDetail where subjectID = 2 order by qnID;	#eng 성적
	select * from reportDetail where subjectID = 3 order by qnID;	#mat 성적
    
	#reportDetail테이블 조회2 - 과목별로 정답률 높은순으로 출력
	select * from reportDetail where subjectID = 1 order by answerRate desc;	#kor 성적
	select * from reportDetail where subjectID = 2 order by answerRate desc;	#eng 성적
	select * from reportDetail where subjectID = 3 order by answerRate desc;	#mat 성적

end $$
delimiter ;

call input_reportDetail();  #프로시저 호출


