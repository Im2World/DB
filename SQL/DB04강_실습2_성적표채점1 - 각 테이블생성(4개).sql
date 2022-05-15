#DB04강 실습하기2-시험_정답오답_정답률_난이도_1 <테이블생성>
#성적표채점 테이블 구성 : 1. 정답테이블 Answer // 2. 학생 시험 데이터 테이블 Testing // 3. 채점테이블 Scoring // 4. 채점리포트테이블 Reporttable 

#1. 정답테이블 생성
drop table if exists Answer; #Answer테이블이 존재한다면 제거
create table Answer (
subjectID int not null primary key,		#subjectID가 pk
a01 int,a02 int,a03 int,a04 int,a05 int,a06 int,a07 int,a08 int,a09 int,a10 int,	#문제수 20개 = 답안수 20개
a11 int,a12 int,a13 int,a14 int,a15 int,a16 int,a17 int,a18 int,a19 int,a20 int
);

################################################################################################################

#2. 학생이 제출한 답안 데이터를 저장하는 테이블 생성
drop table if exists Testing; #Testing 테이블이 존재한다면 제거
create table Testing (
subjectID int not null,		#null값을 허용하지 않는다.
stu_name varchar(20),
stu_id int not null,		#null값을 허용하지 않는다.
a01 int,a02 int,a03 int,a04 int,a05 int,a06 int,a07 int,a08 int,a09 int,a10 int,	#문제수 20개 = 답안수 20개
a11 int,a12 int,a13 int,a14 int,a15 int,a16 int,a17 int,a18 int,a19 int,a20 int,
primary key(subjectID, stu_id)	#pk는 subjectID, stu_id
);

################################################################################################################

#3. 점수 계산 테이블 생성
drop table if exists Scoring; #Scoring 테이블이 존재한다면 제거
create table Scoring (
subjectID int not null,		#null값을 허용하지 않는다.
stu_name varchar(20),
stu_id int not null,		#null값을 허용하지 않는다.
a01 int,a02 int,a03 int,a04 int,a05 int,a06 int,a07 int,a08 int,a09 int,a10 int,	#문제수 20개 = 답안수 20개
a11 int,a12 int,a13 int,a14 int,a15 int,a16 int,a17 int,a18 int,a19 int,a20 int,
score int,
primary key(subjectID, stu_id)	#pk는 subjectID, stu_id
);

################################################################################################################

#4. 최종 리포트테이블 생성
drop table if exists Reporttable; #최종 리포트 테이블이 존재한다면 제거
create table Reporttable (
stu_name varchar(20),	#가변길이 20바이트인 필드 생성
stu_id int not null primary key,		#pk이며, null값을 허용하지 않는다.
kor int, eng int, mat int);		#국어, 영어, 수학 점수 필드

################################################################################################################

#5. 각 테이블의 자료구조 출력
desc Answer;	#정답지
desc Testing;	#학생의 제출 답안
desc Scoring;	#학생의 제출 답안 채점지
desc Reporttable;	#한 반의 시험결과




