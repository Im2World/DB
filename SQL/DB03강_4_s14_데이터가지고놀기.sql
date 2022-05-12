#3강 슬라이드 14 - 실습 - 데이터 가지고 놀기
#반복문, concat, cast함수

#이미 프로시저 있다면, 제거
DROP PROCEDURE IF EXISTS insert_examtable;

#구분자 $$로 정의
#_cnt값을 0으로 지정
#id가 0초과면 제거 => 이 실습에서는 해당 테이블에 있는 행 삭제
#_loop: LOOP		#반복문 시작. 반복문 명칭 => _loop
#SET _cnt = _cnt + 1;		#_cnt값 재정의 => 반복1회시 1증가
#concat => 학생001, 002...  => concat은 문자열을 한문장으로 이어붙여서 출력
#SET _id = 209900 + _cnt;   #학번 1씩 증가
#학번 넣고 RAND함수로 점수 생성
#IF _cnt = _last THEN			#_cnt가 _last면,
#LEAVE _loop;		# 반복문 명이 _loop인 반복문을 빠져나가라
#END LOOP _loop; 	#반복문 종료

DELIMITER $$
CREATE PROCEDURE insert_examtable(_last integer)
	BEGIN
	DECLARE _name varchar(20);
	DECLARE _id integer;
	DECLARE _cnt integer;
	SET _cnt = 0;

	delete from examtable where id > 0;
		_loop: LOOP
			SET _cnt = _cnt + 1;
			SET _name = concat("학생", cast(_cnt as char(4)));
			SET _id = 209900 + _cnt;
			
			INSERT INTO examtable VALUE (_name, _id, rand()*100, rand()*100, rand()*100);
			
			IF _cnt = _last THEN
				LEAVE _loop;
			END IF;
		END LOOP _loop;
	END $$
DELIMITER ;

#프로시저 실행
call insert_examtable(1000);			#성적 테이블에 데이터 1000개 대입

#위에서 시행한 데이터 출력
#limit 시작행, 시작행부터출력할 개수 
select * from examtable;

#examtable 테이블에서 *로 테이블에 모든 필드 선택, kor+eng+mat라는 필드도 만들어서 출력하는데 별칭은 sum이라고 출력, (kor+eng+mat)/3라는 필드도 만들어서 출력하는데 별칭은 avg
#출력개수는 행30부터 59개 출력
select *, kor+eng+mat as sum, (kor+eng+mat)/3 as avg from examtable LIMIT 30, 59; 
