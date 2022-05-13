#DB04강 이해하기 소스코드 - 슬라이드17
#View와 insert안에 select
#view는 임시 테이블 => 테이블이 아니라서 쿼리, 수정, 삭제 제한됨.

#슬라이드 17 설정 테이블 생성
#1. 성적 테이블 생성
use kopoctc;		#kopoctc데이터베이스 사용
drop table if exists examtableDB04;	#examtableDB04 있다면 제거

#examtableDB04 테이블 생성
create table examtableDB04(
	name varchar(20),		#변수명 타입지정	
    id int not null primary key,	#id를 pk로 지정
    kor int, eng int, mat int);	#성적 변수는 모두 int
desc examtableDB04;	#테이블 구조 출력

#2. 성적 테이블 입력 프로시져 생성
delete from examtableDB04 where id > 0;	#examtableDB04에 데이터 존재한다면 제거
drop procedure if exists insert_examtableDB04;	#insert_examtableDB04 프로시저가 있다면 제거

#성적입력 프로시저 생성
delimiter $$
create procedure insert_examtableDB04(_limit integer)	#프로시저명 insert_examtableDB04 선언, 인자로 integer형 받음
begin
	declare _name varchar(20);	#프로시저에서 사용할 변수 생성
    declare _id integer;	#declare에 선언한 변수들은 프로시저 안에서 모두 사용되야한다.
    declare _cnt integer;
    set _cnt = 0;		#_cnt의 값은 0으로 설정
	_loop: LOOP			#반복문 시작, 반복문명 _loop
		set _cnt = _cnt+1;		#반복문 1회 시행시 _cnt값 1증가
		set _name = concat("혜임", cast(_cnt as char(4))) ;		#_cnt를 char형으로 형변환 후, concat함수를 이용해 접두사로 혜임을 붙여 혜임1, 혜임2, 혜임3 ... 반복
		set _id = 202200 + _cnt;		#학번에는 202200에 _cnt값을 더한다.		
		
		insert into examtableDB04 value (_name, _id, rand()*100, rand()*100, rand()*100);		#프로시저내에서 테이블 examtableDB04 값 대입 => 과목 점수는 0~100사이의 랜덤값 대입 

		if _cnt = _limit then	#만약 _cnt가 _limit값과 같아지면,
			leave _loop;		#반복 탈출~~~!!!!
		end if;		#if문도 종료
	end LOOP _loop;		#반복문 종료
end $$

#3. 프로시저 호출 => call 프로시저명
call insert_examtableDB04(1000);
select * from examtableDB04;	#examtableDB04의 모든 필드, 레코드 출력

################################################################################################################

#슬라이드18 - view 생성
#임시 테이블 역할하는 examview 생성
drop view if exists examview;		#기존에 view가 존재한다면, 제거한다.

#1. view 생성 - examtableDB04테이블을 데이터를 기반으로 임시로 보여주기만 하는 실체가 없는 것이다.
create view examview(name, id, kor, eng, mat, tot, ave, ran)			#view의 필드 8개
as select *, #(2) 테이블 b의 모든 필드(이름,학번, 국어, 영어, 수학) 출력
	b.kor + b.eng + b.mat,	#(3) 과목 총점 출력 (여기에서 만드는 것)
    (b.kor + b.eng + b.mat)/3,	#(4) 과목 평균 출력 (여기에서 만드는 것)
    ( select count(*)+1 from examtableDB04 as a where (a.kor + a.eng + a.mat) > (b.kor + b.eng + b.mat))	#(5) 테이블 examtableDB04를 a라고 별칭
		#테이블 a의 과목총점이 테이블b의 과목총점보다 크다면, 레코드수 + 1 출력 => 등수가 된다.
	from examtableDB04 as b;		#(1) examtableDB04을 b라고 별칭

#2. view 출력
select * from examview;		#view examview의 모든 필드, 레코드 출력		
select name, ran from examview;		#view examview에서 name, ran 필드만 출력
select * from examview where ran > 5;		#view examview에서 필드 ran이 5초과하는 레코드 전부 출력 => examview에는 name, id, kor, eng, mat, tot, ave, ran로 필드가 8개가 들어있다.
insert into examview values ("나연", 309933, 100, 100, 100, 300, 100, 1); #에러! => view는 값 수정, 삭제 제한됨. => view의 한계!!!

################################################################################################################

#슬라이드 20
#view와 비교하기 위해 examtableEX생성 => 테이블이라서 view와 달리 값 수정, 입력 등 가능 
drop table if exists examtableEX;	#examtableEX이 있다면 제거

#1. 테이블 examtableEX 생성
create table examtableEX(
	name varchar(20),		#필드명 타입 지정
    id int not null primary key,		#id가 pk
    kor int, eng int, mat int, sum int, ave double, ranking int);	#kor, eng, mat과목 성적, 과목성적합, 과목평균, 등수 필드 생성

#2. examtableEX 구조 출력
desc examtableEX;

#****************************************중요예제********************************************
#3. #examtableEX 에 원본테이블의 데이터 삽입
#기존 insert구문 : insert into 테이블명 (테이블 항목, 항목2 ...) values (자료형에 따라 넣을 값 지정);
#이 예제에서는 insert안에 select, from사용해서 examtableDB04의 데이터를 가져와서 examtableEX에 넣는다.
#insert부분에서 examtableDB04테이블의 기존 필드 5개 값 전부 가져옴 + 총합값 생성 + 평균값 생성 + 등수값 생성 
insert into examtableEX
		select *, b.kor+b.eng+b.mat,(b.kor+b.eng+b.mat)/3,		#(2) examtableDB04의 모든 필드, 과목총점, 과목평균,
        (select count(*)+1 from examtableDB04 as a where (a.kor+a.eng+a.mat) > (b.kor+b.eng+b.mat))		#(3) examtableDB04를 a라 별칭, a의 과목 총점이 b보다 크다면, a의 레코드수 + 1 => 등수 
        from examtableDB04 as b;	#(1) examtableDB04를 b라고 별칭

#4. 테이블 examtableEX을 ranking 기준으로 내림차순 정렬해서 출력
select * from examtableEX order by ranking desc;
