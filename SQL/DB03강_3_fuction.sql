#3강 function 
#sql문장에서 Procedure형식 => call procedure명()
#declare - set - sql 문장 순이다.

#1. 기존에 생성한 함수 제거
drop function if exists f_get_sum; 	#이미 함수가 있다면, 제거

#2. DELIMITER로 구분자 %%로 정의
#returns integer => 리턴값 정의
#declare => integer 형인 _id 변수를 받아서 integer로 반환
#where id=_id; => 함수에서 실행할 sql문장 => kor+eng+mat를 _sum에 넣는다.
#end $$ => 리턴값 보내기

#Error Code : 1418 해결 위해 OFF를 ON으로 변경
show global variables like 'log_bin_trust_function_creators';
SET Global log_bin_trust_function_creators = 'ON';

DELIMITER $$
create function f_get_sum(_id integer)
	returns integer
	begin
		declare _sum integer;
		select kor+eng+mat into _sum
		from examtable
		where id=_id;
	return _sum;
end $$
DELIMITER ;

#위에서 생성한 함수 이용방법 => 함수 실행결과 확인
select *, f_get_sum(id) as sum from examtable;




