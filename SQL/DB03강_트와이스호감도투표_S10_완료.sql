#이거 사용하기!!
use kopoctc;

#tupyo테이블 있으면 제거하고 시작!
drop table if exists tupyo;

create table tupyo(name int, age int); #테이블 생성
#age 1은 10대, 2는 20대, 3은 30대, ... 8은 80대, 9는 90대

#rand() 함수 점수 랜덤 생성
#sql 랜덤함수  0<= r <= 1
#delete from tupyo where age>0;		#값 삭제

select * from tupyo;  #테이블 출력

#이름, 연령대 => 랜덤투표 (숫자 1~9 랜덤으로 생성)
#100회 반복
insert into tupyo values(rand()*8 + 1, rand()*8 + 1); #인당 데이터 삽입 쿼리문 입력 
insert into tupyo values(rand()*8 + 1, rand()*8 + 1); #인당 데이터 삽입 쿼리문 입력 
insert into tupyo values(rand()*8 + 1, rand()*8 + 1); #인당 데이터 삽입 쿼리문 입력 
insert into tupyo values(rand()*8 + 1, rand()*8 + 1); #인당 데이터 삽입 쿼리문 입력 
insert into tupyo values(rand()*8 + 1, rand()*8 + 1); #인당 데이터 삽입 쿼리문 입력 
insert into tupyo values(rand()*8 + 1, rand()*8 + 1); #인당 데이터 삽입 쿼리문 입력 
insert into tupyo values(rand()*8 + 1, rand()*8 + 1); #인당 데이터 삽입 쿼리문 입력 
insert into tupyo values(rand()*8 + 1, rand()*8 + 1); #인당 데이터 삽입 쿼리문 입력 
insert into tupyo values(rand()*8 + 1, rand()*8 + 1); #인당 데이터 삽입 쿼리문 입력 

#100개 확인을 위한 카운트 함수 => tupyo 테이블에 있는 레코드개수 카운트!
select count(*) from tupyo; 

select * from tupyo;  #테이블 출력

#name 필드의 자료형 변경 => int에서 string으로!
alter table tupyo modify name varchar(20);
set sql_safe_updates = 0;	#보안 해제해서 자료형변환해준다.

#숫자 1~9인 데이터를 순서대로 이름으로 변경
update tupyo set name = '나연' where name = '1';
update tupyo set name = '정연' where name = '2';
update tupyo set name = '모모' where name = '3';
update tupyo set name = '사나' where name = '4';
update tupyo set name = '지효' where name = '5';
update tupyo set name = '미나' where name = '6';
update tupyo set name = '다현' where name = '7';
update tupyo set name = '채영' where name = '8';
update tupyo set name = '쯔위' where name = '9';

select count(*) from tupyo; #100개 확인을 위한 카운트 함수 => tupyo 테이블에 있는 레코드개수 카운트!

select * from tupyo;  #테이블 출력

#이름으로 그룹화 => 9명 => (해당이름 개수 / 투표 테이블의 레코드 개수 ) *100 => 득표율  => 이 필드를 rate라고 별칭
#십습1-1] 멤버별 득표수, 득표율 현황 => 이름필드 가나다 순으로 정렬
select name as 이름, count(name) as 득표수, count(name)/(select count(*) from tupyo)*100 as 득표율 from tupyo group by name order by 이름;

#십습1-1] 멤버별 득표수, 득표율 현황 => 득표수 내림차순으로 정렬
select name as 이름, count(name) as 득표수, count(name)/(select count(*) from tupyo)*100 as 득표율 from tupyo group by name order by 득표수 desc;

#실습1-2] 나연 연령대별 득표수, 득표현황 => 연령대로 오름차순 정렬
select age*10 as 연령대, count(age) as 득표수, count(age)/(select count(*) from tupyo where name="나연")*100 as 득표율 from tupyo where name="나연" group by age order by 연령대;

#실습1-3] 정연 연령대별 득표수, 득표현황 => 득표수로 내림차순 정렬
select age*10 as 연령대, count(age) as 득표수, count(age)/(select count(*) from tupyo where name="정연")*100 as 득표율 from tupyo where name="정연" group by age order by 득표수 desc;

