#데이터베이스 03강 3. procedure, function, order by, group by
use kopoctc;

#실습1] order by => 정렬 수행
drop table if exists examtable;	#테이블 있으면 지워라
#show tables;

create table examtable(
	name varchar(20),
    id int not null primary key,	#id가 pk
    kor int, eng int, mat int);
desc examtable;	#examtable 구조 출력

#데이터 입력
#데이터 입력 전에 테이블 자료들 전부 비우기
delete from examtable where id > 0;
#이름, kor, eng, mat 점수 랜덤 입력
insert into examtable value ("나연", 209901, rand()*100, rand()*100, rand()*100);
insert into examtable value ("정연", 209902, rand()*100, rand()*100, rand()*100);
insert into examtable value ("모모", 209903, rand()*100, rand()*100, rand()*100);
insert into examtable value ("사나", 209904, rand()*100, rand()*100, rand()*100);
insert into examtable value ("지효", 209905, rand()*100, rand()*100, rand()*100);
insert into examtable value ("미나", 209906, rand()*100, rand()*100, rand()*100);
insert into examtable value ("다현", 209907, rand()*100, rand()*100, rand()*100);
insert into examtable value ("채영", 209908, rand()*100, rand()*100, rand()*100);
insert into examtable value ("쯔위", 209909, rand()*100, rand()*100, rand()*100);
insert into examtable value ("임영웅", 209910, rand()*100, rand()*100, rand()*100);
insert into examtable value ("나연", 209911, rand()*100, rand()*100, rand()*100);
insert into examtable value ("정연", 209912, rand()*100, rand()*100, rand()*100);
insert into examtable value ("모모", 209913, rand()*100, rand()*100, rand()*100);
insert into examtable value ("사나", 209914, rand()*100, rand()*100, rand()*100);
insert into examtable value ("지효", 209915, rand()*100, rand()*100, rand()*100);
insert into examtable value ("미나", 209916, rand()*100, rand()*100, rand()*100);
insert into examtable value ("다현", 209917, rand()*100, rand()*100, rand()*100);
insert into examtable value ("채영", 209918, rand()*100, rand()*100, rand()*100);
insert into examtable value ("쯔위", 209919, rand()*100, rand()*100, rand()*100);
insert into examtable value ("임영웅", 209920, rand()*100, rand()*100, rand()*100);

#테이블 출력
select * from examtable;

#order by 필드 => 필드를 기준으로 오름차순 정렬 , 필드1,필드2 => 필드1로 정렬하되 필드1에 동점있으면 필드2로 정렬
select * from examtable order by kor;
select * from examtable order by eng;
select * from examtable order by kor, eng;
select * from examtable order by kor asc;		#오름차순 정렬
select * from examtable order by kor desc;		#내림차순 정렬

#이름의 역순으로 정렬
select * from examtable order by name desc;

#수학의 역순으로 정렬
select * from examtable order by mat desc;

#총점, 평균 구하고 총점의 역순으로 정렬
#select * 은 필드를 모두 선택한다는 의미
#,는 여러 조건을 의미 => , kor+eng+mat, (kor+eng+mat)/3 => 실제 테이블에는 영향미치지 않는 가상의 view출력
select *, kor+eng+mat, (kor+eng+mat)/3 from examtable;
select *, kor+eng+mat, (kor+eng+mat)/3 from examtable order by kor+eng+mat desc;


#실습2] as 별칭 주기
select * from examtable;
select *, kor+eng+mat, (kor+eng+mat)/3 from examtable;
select *, kor+eng+mat, (kor+eng+mat)/3 from examtable order by kor+eng+mat desc;
select *, kor+eng+mat as total, (kor+eng+mat)/3 as average from examtable order by total; #과목합을 total, 평균을 average로 별칭

#name필드는 이름, id는 학벅, kor는 국어, eng는 영어, mat은 수학, kor+eng+mat는 합계, (kor+eng+mat)/3는 평균이라 별칭
#정렬 순서는 합계를 기준으로 내림차순
select name as 이름, id as 학번, kor as 국어, eng as 영어, mat as 수학, kor+eng+mat as 합계,
(kor+eng+mat)/3 as 평균 from examtable order by 합계 desc;


#실습3] select 필드명 from 테이블명 group by 필드명 => 같은 항목 있으면 동일한 레코드로 취급 => 묶어서 한 테이블로 출력
#group by 조건절은 having
select * from examtable group by name;		#에러! group by 는 집계함수와 사용. name은 집계불가
select name, count(name) from examtable group by name; #이름필드를 선택, 이름필드를 그룹으로 묶어 같은 이름을 하나로 만들고, 해당이름 카운트
select * from examtable group by kor; #해당 테이블을 kor기준으로 오름차순 정렬 => 에러! group bys는 select *, 로 표시
select kor,count(kor) from examtable group by kor;
select kor,count(kor) from examtable group by eng;  #그룹화한 필드와 select한 필드가 다르면 안된다. => 에러!
select kor,count(kor),eng,count(eng) from examtable group by kor,eng; #국어 기준으로 그룹화 후 해당 그룹에 따른 영어점수 기준으로 그룹 =>ex)경기도 여성, 경기도 남성
select eng,count(eng) from examtable group by eng;		#eng로 그룹화

#이름, 학번, 국어, 영어, 수학 점수 입력 => 수학점수만 랜덤 생성!
insert into examtable value ("펭수",209921, 100, 90, rand()*100); 
insert into examtable value ("펭수",209922, 100, 90, rand()*100);

select kor, count(kor), eng, count(eng) from examtable group by kor, eng; #국어, 영어 그룹화 후 count도 출력
select name, count(name), kor, count(kor), eng, count(eng) from examtable group by name, kor, eng; # select필드와 group by 필드일치!
select *, name,count(name), kor, count(kor), eng, count(eng) from examtable group by name, kor, eng; #select필드와 group by 필드 불일치 => 에러


#고급스러운 문장이다. select안에 select => 튜닝이라고 표현함.
#(count(eng)/(select count(*) from examtable))*100 => 얘는 없는 행이라 새로 생성된다.
select eng,count(eng),(count(eng)/(select count(*) from examtable))*100 from examtable group by eng;

#group by에 조건을 주려면 having을 사용.
select eng, count(eng) from examtable group by eng having count(eng)>1;		#eng 로 그룹화하고, 그 수가 1 초과하는 레코드만 출력