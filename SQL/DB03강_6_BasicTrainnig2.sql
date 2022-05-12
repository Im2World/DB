#DB03강 Basic Training(2)
#프로시저와 함수의 콜라보레이션!
#트와이스 멤버선호도 조사하기 위한 데이터입력 "프로시저" + 선호도 계산하는 "함수"

#기존 테이블 제거
drop table if exists twiceData;	

#테이블 생성
create table twiceData(
	name varchar(20),		#name 필드는 varchar(20) 사이즈
    age int);				#age 필드는 int 자료형
desc twiceData;	#twiceData 구조 출력

##################################################################################################

#실습1] 데이터 입력 프로시저 생성
#프로시져 내에서 사용하는 변수명은 DB필드명과 구분하기 위해 접두사로 _ 사용
#프로시저 내에서 랜덤숫자로 생성한 _name을 숫자에 따라 다시 한글로 변경한다.
 
#같은 이름의 프로시져가 존재한다면 삭제
drop procedure if exists input_twiceData;

delimiter $$
create procedure input_twiceData (_dataNum integer)		#프로시저명 input_twiceData
	begin
		declare _name varchar(20);		#프로시저 내에서 사용할 변수명 자료형 선언
		declare _age integer;			#declare 안에서 생성한 변수는 모두 프로시저안에서 사용해야한다.
		declare _cnt integer;
		declare _rand integer;
		set _cnt = 0;					#_cnt 값을 0으로 세팅

	delete from twiceData where age > 0;	#twiceData 테이블의 모든 레코드에 적용되는 조건으로, 해당 테이블에 있는 데이터를 모두 제거한다.
    
	_loop : loop						#반복문 시작, 반복문명 : _loop
		set _cnt = _cnt + 1;			#반복문 시작하면, _cnt값 1증가
        set _rand = rand()*8 + 1;		#rand함수로 1~9까지 랜덤 숫자 생성
		if _rand = 1 then  				#만약, _rand가 1이라면,
				set _name = "나연";		#_name에 나연을 대입
			elseif _rand = 2 then 		#만약, _rand가 2라면,
				set _name = "정연";		#_name에 정연을 대입
			elseif _rand = 3 then  		#만약, _rand가 3라면,
				set _name = "모모";		#_name에 모모를 대입
			elseif _rand = 4 then 		#만약, _rand가 4라면,
				set _name = "사나";		#_name에 사나를 대입
            elseif _rand = 5 then 		#만약, _rand가 5라면, 
				set _name = "지효";		#_name에 지효룰 대입
            elseif _rand = 6 then  		#만약, _rand가 6라면,
				set _name = "미나";		#_name에 미나를 대입
            elseif _rand = 7 then 		#만약, _rand가 7라면, 
				set _name = "다현";		#_name에 다현을 대입
            elseif _rand = 8 then 		#만약, _rand가 8라면, 
				set _name = "채영";		#_name에 채영을 대입
			elseif _rand = 9 then 		#만약, _rand가 9라면, 
				set _name = "쯔위";		#_name에 쯔위를 대입
		end if;							#if 조건문 종료!
		set _age = (rand()*8) + 1;		#_age도 랜덤함수로 1~9까지 생성
        
        insert into twiceData value (_name, _age);		#twiceData테이블에 프로시저에서 생성한 _name, _age를 값으로 대입
        
        if _cnt = _dataNum then		#_cnt가 _dataNum과 같으면
			leave _loop;			#반복문 종료
		end if;						#if문 종료
	end loop _loop;			#반복문 종료
	end $$
delimiter ;

#데이터 입력 프로시저 실행
set sql_safe_updates = 0; #세이프모드로 인해 데이터 업데이트가 되지 않아 실행시에만 일시적으로 off

#프로시저 호출해서 데이터 1000개 생성
call input_twiceData(1000);

#테이블 twiceData의 데이터 수 조회
select count(*) from twiceData;

#데이터 전체 출력
select * from twiceData; #전체 데이터 조회

##################################################################################################

#실습2] 선호도비율계산 함수
#기존 선호도비율계산 함수 제거
drop function if exists preference;

#선호도 비율 계산
#	1) name 종류가 9개 => ( 해당 name 개수 / 전체 레코드수(투표수) ) *100 = 해당 name의 득표율
#	2) twiceData 테이블에서 name을 그룹화하고 그 name이 매개변수로 받는 _name과 같다면, 1)값을 _ratio변수에 대입
#	3) 이 함수는 결과 값으로 double형의 _ratio를 반환 
delimiter $$
create function preference(_name varchar(20))		#함수명 preference, 인자로 varchar타입의 _name 받는다.
	returns double		#반환 값은 double형
	begin
	declare _ratio double; #비율 저장할 변수 더블로 선언
		select count(name)/(select count(*) from twiceData)*100	into _ratio	#2)  (twiceData의 name필드 레코드수 / twiceData의 레코드수) * 100 한 값을 => _ratio에 대입
			from twiceData group by name having name = _name; #1) twiceData테이블에서 name = _name이라면 name으로 그룹화하고, 
	return _ratio;#group by 절에서 따로 조건을 주는 having 사용하여 카운팅 및 선호도 계산 후 변수에 저장한 값리턴
end $$
delimiter ;

#선호도 비율 계산 함수 실행!!!!!
#이름으로 그룹 묶어 레코드 9개로 만들고 선호도 비율로 내림차순 정렬
select name as 이름, count(*) as 득표수, preference(name) as 선호도비율 	#2) name필드는 이름, 전체 레코드 수는 득표수. preference함수 시행 결과는 선호도 비율이라고 별칭
		from twiceData group by name	#1) twiceData 테이블을 name으로 그룹화하고,
        order by 선호도비율 desc;			#3) 선호도비율로 내림차순 정렬 
