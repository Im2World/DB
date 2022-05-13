#DB04강 이해하기 소스코드 
#join, select안에 select, view, insert안에 select

use kopoctc;	#kopoctc 데이터베이스 사용

drop table if exists hubo; #해당 테이블 있다면 제거

#테이블 생성1
create table hubo(
	kiho int not null,
    name varchar(10),
    gongyak varchar(50),
    primary key(kiho),
    index(kiho));
desc hubo;	#구조출력

################################################################################################################
drop table if exists tupyo; 	#해당 테이블 있다면 제거

#테이블 생성2
create table tupyo(
	kiho int,
    age int,
    foreign key(kiho) references hubo(kiho));  #두 테이블이 종속적 관계에 있으면 외래키를 걸어서 사용한다. 외래키 걸어두면 해당 데이터를 함부로 삭제불가.
desc tupyo;			#구조 출력

################################################################################################################
#hubo테이블에 값입력
#hubo테이블은 kiho, name, gongyak 필드 순서다.
#모든 필드에 값을 넣으면, 아래처럼 필드면 지정없이 한번에 쭉 쓰면 된다.
delete from hubo where kiho>0; #테이블 내 존재하는 데이터 삭제 후 아래 값 insert
insert into hubo values (1, "나연", "아름다운 세상 만들기");		#순서대로 kiho, name, gongyak 필드에 값 입력
insert into hubo values (2, "정연", "환경 보호");		#순서대로 kiho, name, gongyak 필드에 값 입력
insert into hubo values (3, "모모", "빙하가 녹고있어요, 지구를 지킵시다");		#순서대로 kiho, name, gongyak 필드에 값 입력
insert into hubo values (4, "사나", "수면시간 9시간 보장");		#순서대로 kiho, name, gongyak 필드에 값 입력
insert into hubo values (5, "지효", "모바일 결제 수수료 지원");		#순서대로 kiho, name, gongyak 필드에 값 입력
insert into hubo values (6, "미나", "교통비 매달 20만원 지원");		#순서대로 kiho, name, gongyak 필드에 값 입력
insert into hubo values (7, "다현", "매달 커피 구독권 30매 제공");		#순서대로 kiho, name, gongyak 필드에 값 입력
insert into hubo values (8, "채영", "주3일 근무제시행");		#순서대로 kiho, name, gongyak 필드에 값 입력
insert into hubo values (9, "쯔위", "여가활동비 매년 500만원 지원");		#순서대로 kiho, name, gongyak 필드에 값 입력
select kiho as 기호, name as 성명, gongyak as 공약 from hubo; 	#각 값에 별칭을 주어 select

################################################################################################################
#프로시저 실행 전 tupyo테이블 데이터 삭제
delete from tupyo where kiho>0;

#프로시저가 이미 존재한다면, 제거!
drop procedure if exists insert_tupyo;

#integer를 인자로 받는 프로시져 생성
delimiter $$
create procedure insert_tupyo(_limit integer)
begin
declare _cnt integer;	#_cnt변수 선언
set _cnt = 0;	#변수 초기화
	_loop: loop			#_loop라는 이름의 반복문 시작
		set _cnt = _cnt + 1;	#루프문에서 변수 + 1
        insert into tupyo value (rand()*8+1, rand()*8+1);	#tupyo테이블 kiho, age필드에 1~9까지 랜덤값 입력
        if _cnt = _limit then	#만약 _cnt가 _limit과 같으면
			leave _loop;	#반복문 종료
		end if;		#if종료
	end loop _loop;		#반복문 종료
end $$
delimiter ;

#프로시저 호출해 1000개 데이터 생성
call insert_tupyo(1000);
select * from tupyo;	#tupyo테이블 출력 => 필드 kiho age // 레코드 7 4 

#1000개 데이터 생성 결과 출력
select kiho as 기호, name as 성명, gongyak as 공약 from hubo;
select kiho as 투표한기호, age as 투표자연령대 from tupyo; #결과 select
#select count(*) from tupyo; #insert된 행수 출력
select * from hubo;		#hubo 테이블 형식 => 필드: kiho, name, gongyak //레코드 1		나연		아름다운 세상만들기

################################################################################################################
#JOIN , 슬라이드 11
#슬라이드11 과 슬라이드12는 같은 결과를 출력한다.
#join => kiho필드를 기준으로 합친다.
#hubo테이블을 b, tupyo테이블은 a라고 하고. 공통 필드인 kiho로 join해서 이름 – 공약 – 투표수 출력
select kiho, count(*) from tupyo group by kiho;		#tupyo테이블에서 kiho, 레코드 개수 출력하되 kiho를 기준으로 그룹화해서 출력
select b.name, b.gongyak, count(a.kiho)	 #(4) b테이블의 name필드, b테이블의 gongyak필드, a테이블의 kiho의 레코드 개수카운트 한 값을 필드로 출력
	from tupyo as a, hubo as b	# (1) hubo테이블을 b, tupyo테이블은 a라고 하고
	where a.kiho = b.kiho		# (2) a,b의 kiho가 같으면
    group by a.kiho;		# (3) a의 kiho로 그룹화 => 1,2,3
    
 #SELECT안에 SELECT사용해서 테이블 합치기, 슬라이드12
 #select안에 (2),(3),(4)가 결과 필드로 들어가는 것이다.
 #동일한 kiho의 name을 출력하는 것이 포인트!
 select
	(select name from hubo where kiho = a.kiho),		#(3) hubo테이블에서 kiho가 a테이블 kiho와 같으면 hubo테이블의 name출력
    (select gongyak from hubo where kiho = a.kiho),		#(4) hubo테이블에서 kiho가 a테이블 kiho와 같으면 hubo테이블의 gongyak 출력
    count(a.kiho)	#(5) a테이블의 kiho칼럼 레코드개수 출력
    from tupyo as a		#(1) tupyo테이블을 a라고 한다.
    group by a.kiho;	#(2) a테이블의 kiho로 그룹화한다.
    
################################################################################################################
#슬라이드13 - 호감도 투표2
#한 번에 3명씩 투표하는 테이블과 insert 프로시져 생성
#호감도 투표2 - 1 : tupyo2 테이블 생성 (hubo테이블은 앞과 동일)
#tupyo2테이블은 좋아하는 사람기호를 3개 받는다.

#1) tupyo2테이블 생성
drop table if exists tupyo2; 	#한번에 세사람을 뽑은 테이블 생성
create table tupyo2(	
	kiho1 int,			#kiho1,2,3, age 필드 생성 => 좋아하는 멤버를 기호1~9까지로 받는다.
    kiho2 int,
    kiho3 int,
    age int);
desc tupyo2; 			#테이블 구조 확인

#2) tupyo2테이블에 데이터삽입 하는 insert_tupyo2 프로시져 생성
set sql_safe_updates = 0;	#**********보안 해제해서 테이블내 데이터 삭제가능.************
delete from tupyo2 where kiho1>0;		#테이블 내 데이터 삭제
drop procedure if exists insert_tupyo2;		#프로시저가 존재한다면, 제거!

#인티저 타입을 인자로 받는 프로시저 생성
delimiter $$
create procedure insert_tupyo2(_limit integer)		#프로시져명 insert_tupyo2
begin
declare _cnt integer;		#루프에서 쓰일 카운트 변수 선언
set _cnt = 0;				#카운트 초기화
	_loop: loop		#반복문 시작 => 반복문이름은 _loop
		set _cnt = _cnt + 1;	#루프 돌며 변수 + 1, 테이블에 값 삽입
        insert into tupyo2 value (rand()*8+1, rand()*8+1, rand()*8+1, rand()*8+1);	#tupyo2에 1~9까지 랜덤값 대입
        if _cnt = _limit then	#만약 _cnt가 _limit과 같다면,
			leave _loop;		#루프탈출
		end if;		#if문 종료
	end loop _loop; 	#반복문도 종료
end $$
delimiter ;

call insert_tupyo2(1000);	#프로시저 호출하여 투표 데이터 1000개 생성
select * from tupyo2;		#전체 조회 => tupyo2 테이블 형식 => 필드: kiho1, kiho2, kiho3, age // 레코드 1		7	  8		1
select count(*) from tupyo2;	#전체 데이터 행수 조회

################################################################################################################
#슬라이드14 : join과 select안에 select 이용한 쿼리문 작성
#3) join으로 kiho1, kiho2, kiho3, age => 1,7,8,1 이렇게 나오던 것을 숫자에 따른 이름으로 변경해준다.
	#tupyo2 테이블 형식 => 필드: kiho1, kiho2, kiho3, age // 레코드 1		7	  8		1
	#hubo 테이블 형식 => 필드: kiho, name, gongyak //레코드 1		나연		아름다운 세상만들기

#hubo 테이블과 tupyo2 테이블 합치는 방법 2가지
#3-1) join 이용하여 기호를 이름으로 나타냄
select a.age, h1.name as 투표1, h2.name as 투표2, h3.name as 투표3	#(3) a테이블의 age필드, h1의 name을 투표1, h2의 name을 투표2, h3의 name을 투표3으로 출력
	from tupyo2 as a, hubo as h1, hubo as h2, hubo as h3		#(1) tupyo2테이블을 a, hubo테이블을 h1, h2, h3라고 지칭
	where a.kiho1=h1.kiho and a.kiho2=h2.kiho and a.kiho3=h3.kiho;	#(2) 테이블 a의 kiho1과 테이블 h1의 kiho가 같고, 테이블 a의 kiho2과 테이블 h2의 kiho가 같고, 테이블 a의 kiho3과 테이블 h3의 kiho이 같다면 => 참 and 참 and 참 이라면!

 #또는
 
#3-2) 셀렉트 안에 셀렉트를 사용하여 기호를 이름으로 나타냄
#*****포인트1 : 동일한 kiho의 name을 출력하는 것이 포인트!*****
#*****포인트2 : hubo테이블을 3번이나 별칭 선언! ******
select a.age,		#(2) a테이블의 age필드 출력
	(select name from hubo where a.kiho1 = kiho) as "투표1",			#(3) a테이블의 kiho1과 hubo테이블의 kiho가 동일하면 hubo테이블의 name필드를 투표1이라는 필드명으로 출력
    (select name from hubo where a.kiho2 = kiho) as "투표2",			#(4) a테이블의 kiho2과 hubo테이블의 kiho가 동일하면 hubo테이블의 name필드를 투표1이라는 필드명으로 출력
	(select name from hubo where a.kiho3 = kiho) as "투표3"			#(5) a테이블의 kiho3과 hubo테이블의 kiho가 동일하면 hubo테이블의 name필드를 투표1이라는 필드명으로 출력
    from tupyo2 as a;	#(1) tupyo2테이블을 a라고 지칭

################################################################################################################
#슬라이드15 : 각 멤버의 투표수 출력

#1. or문으로만 하면 중복 값 처리가 되지 않는다. 중복 값까지 합쳐주어야 전체 투표수가 나옴
select
	(select count(*) from tupyo2 where kiho1=1 or kiho2=1 or kiho3=1) as "나연",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 1인 것의 레코드 개수를 나연 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=2 or kiho2=2 or kiho3=2) as "정연",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 2인 것의 레코드 개수를 정연 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=3 or kiho2=3 or kiho3=3) as "모모",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 3인 것의 레코드 개수를 모모 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=4 or kiho2=4 or kiho3=4) as "사나",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 4인 것의 레코드 개수를 사나 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=5 or kiho2=5 or kiho3=5) as "지효",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 5인 것의 레코드 개수를 지효 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=6 or kiho2=6 or kiho3=6) as "미나",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 6인 것의 레코드 개수를 미나 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=7 or kiho2=7 or kiho3=7) as "다현",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 7인 것의 레코드 개수를 다현 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=8 or kiho2=8 or kiho3=8) as "채영",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 8인 것의 레코드 개수를 채영 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=9 or kiho2=9 or kiho3=9) as "쯔위";		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 9인 것의 레코드 개수를 쯔위 이라는 필드명으로 출력

#2. 중복값까지 출력
select
	(select count(*) from tupyo2 where kiho1=1 or kiho2=1 or kiho3=1) as "나연",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 1인 것의 레코드 개수를 나연 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=2 or kiho2=2 or kiho3=2) as "정연",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 2인 것의 레코드 개수를 정연 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=3 or kiho2=3 or kiho3=3) as "모모",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 3인 것의 레코드 개수를 모모 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=4 or kiho2=4 or kiho3=4) as "사나",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 4인 것의 레코드 개수를 사나 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=5 or kiho2=5 or kiho3=5) as "지효",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 5인 것의 레코드 개수를 지효 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=6 or kiho2=6 or kiho3=6) as "미나",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 6인 것의 레코드 개수를 미나 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=7 or kiho2=7 or kiho3=7) as "다현",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 7인 것의 레코드 개수를 다현 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=8 or kiho2=8 or kiho3=8) as "채영",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 8인 것의 레코드 개수를 채영 이라는 필드명으로 출력
    (select count(*) from tupyo2 where kiho1=9 or kiho2=9 or kiho3=9) as "쯔위",		#tupyo2 테이블에서 kiho1, kiho2, kiho3 중 하나라도 9인 것의 레코드 개수를 쯔위 이라는 필드명으로 출력
	(select 나연 + 정연 + 모모 + 사나 + 지효 + 미나 + 다현 + 채영 + 쯔위) as "총합",			#각 멤버의 투표수를 더한 필드를 총합이라는 필드명으로 출력
    
    #총 투표수 계산 위해 중복표 계산 => 위 or 조건에서는 중복표를 카운트하지 못한다.
    (select count(*) from tupyo2 where kiho1=kiho2 or kiho1=kiho3 or kiho2=kiho3) as "2중복",	#총 3표 중 2표를 같은 멤버에게 투표한 중복표 개수 출력
    (select count(*) from tupyo2 where kiho1=kiho2 and kiho2=kiho3) as "3중복",	#총 3표 중 3표를 같은 멤버에게 투표한 중복표 개수 출력
    
    #개별 멤버 투표수 + 2중복 + 3중복 투표수 =  진짜 투표합계 => 1000명이 3표씩 투표했으니까 총 3천표
    (select 총합 + 2중복 + 3중복) as "최종투표합계";
