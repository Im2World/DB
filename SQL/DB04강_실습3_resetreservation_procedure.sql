#DB04강 리조트 예약2

#문제조건 : 오늘로부터 30일간의 리조트 예약상황 출력
#한달 간의 예약상황 보여주는 테이블, 프로시져 생성

#1. 기초작업
use kopoctc;	#kopoctc 데이터베이스 사용

#깨끗한 환경설정을 위해 기존에 reservation 테이블이 존재한다면, 제거한다.
drop procedure if exists cal_reservation_stat;

###################################################################################################

#2. 예약상황 계산 프로시져 생성
#예약상태 나타내는 reservation_stat 테이블 생성 후 반복문으로 reservation_stat 테이블에 데이터 입력

delimiter $$
create procedure cal_reservation_stat()		#프로시져 명 cal_reservation_stat
begin
	declare _date date;					#예약 날짜
    declare _cnt integer;				#카운트 변수
    declare _room1 varchar(20);			#방번호 1번 변수 문자열로 선언
    declare _room2 varchar(20);			#방번호 2번 변수 문자열로 선언
    declare _room3 varchar(20);			#방번호 3번 변수 문자열로 선언
    
    set _date = now();					#현재 일자를 세팅
    set _cnt = 0;						#카운트는 0으로 세팅
    
    ################################################
    #1) 예약상태 나타내는 reservation_stat 테이블 생성
    drop table if exists reservation_stat; 	#(1) 깨끗한 환경설정을 위해 기존에 reservation_stat테이블 있다면 제거.
    create table reservation_stat(
		reserve_date date not null,			#(2) 예약날짜 reserve_date 변수
        room1 varchar(20),					#(3) 방타입 1 변수, varchar타입
        room2 varchar(20),					#(4) 방타입 2 변수, varchar타입
        room3 varchar(20),					#(5) 방타입 3 변수, varchar타입
        primary key(reserve_date));			#(6) primary key는 예약날짜로 설정
        
     ################################################   
	#2) 반복문으로 reservation_stat 테이블에 데이터 입력
    # 예약일에 해당 방번호로 이름을 조회했을 때 0이면 예약 가능으로 셋팅, 0보다 크면 해당 이름으로 셋팅
    _loop: loop		#반복시작, 반복문명 _loop

    ###고객 예약인 form_reservation 테이블의 숫자변수를 문자로 변경해주기

		if (select count(name) from form_reservation where reserve_date=_date and room=1) = 0 then	
				#만약, form_reservation테이블의  reserve_date이 프로시져예약일 _date와 같고, room이 1인 것의 name수가 0이면 => 해당일자에 room 1의 예약이 없다
			set _room1 = "예약가능";	#예약가능 출력
        else	#그렇지 않다면, => 해당일자에 room 1의 예약이 있다면,
			set _room1 = (select name from form_reservation where reserve_date=_date and room=1);	#해당일자에 room 1의 예약자 name을 출력
		end if;	#조건문 종료
        
        if (select count(name) from form_reservation where reserve_date=_date and room=2) = 0 then
				#만약, form_reservation테이블의  reserve_date이 프로시져예약일 _date와 같고, room이 2인 것의 name수가 0이면 => 해당일자에 room 2의 예약이 없다
			set _room2 = "예약가능";	#예약가능 출력
        else	#그렇지 않다면, => 해당일자에 room 2의 예약이 있다면,
			set _room2 = (select name from form_reservation where reserve_date=_date and room=2);	#해당일자에 room 2의 예약자 name을 출력
		end if;	#조건문 종료
        
        if (select count(name) from form_reservation where reserve_date=_date and room=3) = 0 then
				#만약, form_reservation테이블의  reserve_date이 프로시져예약일 _date와 같고, room이 3인 것의 name수가 0이면 => 해당일자에 room 3의 예약이 없다
			set _room3 = "예약가능";	#예약가능 출력
        else	#그렇지 않다면, => 해당일자에 room 3의 예약이 있다면,
			set _room3 = (select name from form_reservation where reserve_date=_date and room=3);	#해당일자에 room 3의 예약자 name을 출력
		end if;	#조건문 종료
        
        ################################################
		#3) reservation_stat테이블에 예약일자와 각 룸타입의 예약상태 입력(예약가능 or 예약자명)
        insert into reservation_stat values(_date, _room1, _room2, _room3);
     
        set _cnt = _cnt + 1;	#_cnt는 1 증가 => 예약일자가 하루씩 증가된다.
        set _date = date_add(now(), interval +_cnt day);	#_date에는 현재일자에 _cnt일을 더한 값 세팅 => <형식> date_add(now(), interval N day);
 
		################################################
        #4) 시간이 지금으로부터 한 달이 차이가 나면 반복문 탈출 => 문제조건 : 한 달간의 예약현황만 출력
        if TIMESTAMPDIFF(MONTH, now(), _date) = 1  then		#TIMESTAMPDIFF(unit, interval, 일자) => 일자에서 해당 유닛으로 interval만큼 지난 날짜 출력 
			leave _loop;	#반복문 탈출
		end if;		#조건문종료
	end loop _loop;	#반복문 종료
end $$
delimiter ;

call cal_reservation_stat(); #프로시져 호출
select * from reservation_stat; #예약상황 테이블에 값이 잘 입력됐나 테이블 출력해서 데이터 확인

#조건 줘서 예약상황 조회 _ 2022-05-15 예약자 2명
select name, room from form_reservation where reserve_date="2022-05-15";

