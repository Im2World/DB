#test
#중복값 테스트

#1. 테이블 생성
use kopoctc;

create table test0514(
	num1 int,
	num2 int,
	num3 int);
desc test0514;

##############################################################################
#2. 테이블에 값 대입
insert into test0514 values (1,1,1);
insert into test0514 values (2,2,2);
insert into test0514 values (3,3,3);
insert into test0514 values (4,4,4);
insert into test0514 values (5,5,5);

select * from test0514;

##############################################################################
#3. 테이블 출력
#1) 중복값 출력 a=b and b=c 		=> 출력5			=> 해당 칼럼의 데이터 값을 한 행안에서 비교
select count(*) as 3중복 from test0514 where num1=num2 and num2 = num3;


#2) 중복값 출력 오류 a=b=c    => 출력 1   => 1,1,1 인 애를 찾는다. 즉 2,2,2    3,3,3  5,5,5 얘네는 카운트 안함
select count(*) as 3중복 from test0514 where num1=num2=num3;

##############################################################################
##############################################################################

#1. 테이블 생성2
create table test0514_2(
	num1 int,
	num2 int,
	num3 int);
desc test0514_2;

##############################################################################

#2. 테이블에 값 대입2
insert into test0514_2 values (1,1,1);
insert into test0514_2 values (1,1,1);
insert into test0514_2 values (1,1,1);
insert into test0514_2 values (4,4,4);
insert into test0514_2 values (5,5,5);

select * from test0514_2;

##############################################################################
#3. 테이블 출력2
#1) 중복값 출력 a=b and b=c 	=> 출력5
select count(*) as 3중복 from test0514_2 where num1=num2 and num2 = num3;


#2) 중복값 출력 오류 a=b=c    => 출력 3   => 1,1,1 인 레코드 개수만 출력하고 4,4,4   5,5,5는 해당 조건에 포함시키지 않는다.
select count(*) as 3중복 from test0514_2 where num1=num2=num3;

