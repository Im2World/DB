#DB04강 리조트 예약1
#리조트 예약테이블 생성 및 값 입력

#1. 기초작업
use kopoctc; #kopoctc 데이터베이스 사용

#깨끗한 환경설정을 위해 기존에 form_reservation 테이블이 존재한다면, 제거한다.
drop table if exists form_reservation;

###################################################################################################

#2. 고객이 예약 폼에 입력하면 생성되는 리조트 예약테이블 form_reservation 테이블 생성
#리조트 예약테이블 form_reservation 필드 구조 : name, reserve_date, room, address, tel, depositor, memo, input_date
#primary key는 예약일과 방번호 두 개로 지정
create table form_reservation(
	name varchar(80),		#이름 => 외국인이라면, 이름이 엄청 길 수도 있어서 크기를 넉넉하게 설정 ex)아랍계 외국인이름
    reserve_date date,		#예약일자
    room int,				#예약 방타입 	=> 1,2,3 으로 구분
    address varchar(200),	#예약자 주소	=> 주소도 길이를 넉넉하게 받는다.
    tel varchar(30),		#예약자 전화번호
    depositor varchar(80),	#입금자명	=> 외국인 고려해서 크기 설정
    memo varchar(300),		#요청사항
    input_date timestamp,		#예약 폼 제출일자 => 예약 우선순위 구분을 위해 timestamp로 자료형지정
    primary key(reserve_date, room)); 	#primary key는 복합키로 지정

desc form_reservation; #테이블 구조 조회

###################################################################################################

#3. 리조트 예약테이블 form_reservation에 임의로 값 9개 입력 후 조회
#깨끗한 환경 설정 : 테이블 제거없이, 데이터만 제거하기 위해 모든 조건에 해당되는 조건으로 데이터 제거
set sql_safe_updates = 0;	#**********보안 해제해서 테이블내 데이터 삭제가능.************
delete from form_reservation where (select count(reserve_date) > 0);
 
#셰계 시차를 고려해 now() 대신 timestamp()를 사용
#예약자명, 예약일자, 룸타입, 지역, 전화번호, 입금자명, 메모, 예약폼제출시간을 순서대로 입력한다.
insert into form_reservation values ("나연", "2022-05-15",1,"서울","010-9234-1254", "지효", "최고층으로 부탁합니다.",timestampadd(hour, 9, current_timestamp()));	#한국시간 표현위해 9시간을 추가
insert into form_reservation values ("정연", "2022-05-15",2,"인천","010-5111-1237", "정연", "오션뷰로 부탁합니다.",timestampadd(hour, 9, current_timestamp()));
insert into form_reservation values ("사나", "2022-05-16",3,"경기","010-9203-1933", "나연", "조식 인원 2인 추가합니다.",timestampadd(hour, 9, current_timestamp()));
insert into form_reservation values ("모모", "2022-05-16",2,"중국","010-2395-1201", "미나", "클리닝 필요없습니다.",timestampadd(hour, 9, current_timestamp()));
insert into form_reservation values ("미나", "2022-05-19",1,"일본","010-2284-3739", "미나", "비흡연, 조용한 객실로 부탁해요.",timestampadd(hour, 9, current_timestamp()));
insert into form_reservation values ("지효", "2022-05-20",2,"서울","010-1434-8254", "나연", "저녁 룸서비스 b코스 예약합니다.",timestampadd(hour, 9, current_timestamp()));
insert into form_reservation values ("다현", "2022-06-04",2,"경상도","010-1330-0244", "다현", "엑스트라 베드 2개 추가결제예정입니다.",timestampadd(hour, 9, current_timestamp()));
insert into form_reservation values ("채영", "2022-06-07",3,"전라도","010-6534-1734", "채영", "옆 방 비어있는 곳으로 요청합니다.",timestampadd(hour, 9, current_timestamp()));
insert into form_reservation values ("쯔위", "2022-06-07",1,"대만","010-1994-1289", "쯔위", "저층, 엘리베이터 가까운 곳으로 요청드려요.",timestampadd(hour, 9, current_timestamp()));

#4. 리조트 예약테이블 form_reservation 출력
select * from form_reservation;
