#DB03강 Training (2) - 와이파이
use kopoctc;

#freewifi 테이블이 있다면, 제거
#DROP TABLE IF EXISTS freewifi;

#테이블 생성
CREATE TABLE freewifi(	#테이블명 freewifi
   number   integer,	#변수명 자료형
   inst_place      varchar(100) ,    	#설치장소명 필드
   inst_place_detail varchar(200) ,     #설치장소상세 필드   
   inst_city       varchar(50),        	#설치 시도명
   inst_country    varchar(50),    		#설치 시군구명
   inst_place_flag varchar(50),        	#설치 시설 구분
   service_provider varchar(50),      	#서비스 제공사명
   wifi_ssid       varchar(100) ,      	#와이파이ssid
   inst_date       varchar(50)  ,    	#설치년월
   place_addr_road varchar(200),       	#소재지 도로명 주소
   place_addr_land varchar(200),       	#소재지 지번주소
   manage_office     varchar(50),       #관리기관명
   manage_office_phone varchar(50),    	#관리기관 전화번호 
   latitude       double ,       		#위도      
   longitude       double,             	#경도
   write_date    date,					#데이터 기준일자
   CONSTRAINT pmk PRIMARY KEY (inst_place, inst_place_detail, wifi_ssid, inst_date, latitude, longitude));	#pk키 지정

#freewifi 테이블 출력
select * from freewifi;

# 중복 프로시저 삭제
DROP PROCEDURE IF EXISTS print_report;

# 프로시저 선언
DELIMITER $$
CREATE PROCEDURE print_report(_currentPage int, _amount int)	#print_report라는 이름의 프로시저 생성, 매개변수로 2개 받는다.
BEGIN
    declare start_number integer;		#프로시저 내에서 사용할 변수명 자료형 선언
    declare last_number integer;		#declare 안에서 생성한 변수는 모두 프로시저안에서 사용해야한다.
    declare last_page integer;			#프로시저 안의 변수는 접두사로 _를 사용해서 DB필드명과 구분!   
    declare _page integer;      	 	#integer 형 변수선언
    declare _number integer;    	   	#integer 형 변수선언
    declare lat double; 		      	#double 형 변수선언
    declare lng double; 		      	#double 형 변수선언
    
   
   
    set lat = 37.4004863;  #우리집 위도
    set lng = 126.9257784;	#우리집 경도
    set _number = _amount;   #_number는 인자로 받는 _amount 대입
    set _page = _currentPage;	#_page는 인자로 받는 _currentPage 대입
    set last_page = ceil(1000/_number);   #ceil함수로 1000/_number 한 값을 올림해 last_page에 대입
    if _page < 1 then  	#_page가 1미만이라면
      set _page = 1;	#_page를 1로 설정한다.
    end if;		#조건문 종료
    if _page > last_page then   #_page가 last_page보다 크다면,
      set _page = last_page;	#_page에 last_page 대입
    end if;		#조건문 종료
    
    set start_number = (_page - 1) * _number;   #시작 숫자는 페이지-1에 숫자를 곱셈(시작 숫자를 구해야 하기 때문)
    set last_number = _page * _number;		#last_number는 _page 곱하기 _number
    
    #번호, 주소, 위도, 경도, 분당 융기원과의 거리 출력
    #피타고라스 정리 활용
    #좌표(x1,y1), 좌표(x2,y2)인 경우 
    #루트{(x1-x2)^2+(y1-y2)^2}
	#SQRT는 루트 씌우기, POWER는 제곱표현 => 2제곱이라서 숫자 2
   select number as 번호, place_addr_road as 주소, latitude as 위도, longitude as 경도, SQRT( POWER ( latitude-lat, 2) + POWER ( longitude-lng, 2)) as 거리 from freewifi order by number limit start_number , _number;
   
END $$
DELIMITER ;

#프로시저 실행
call print_report(5, 25); #25개씩 출력할 때 5페이지를 출력