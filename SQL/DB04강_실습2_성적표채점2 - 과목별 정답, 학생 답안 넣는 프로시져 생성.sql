#DB04강 실습하기2-시험_정답오답_정답률_난이도_2
#성적표채점 테이블 구성 : 1. 정답테이블 Answer // 2. 학생 시험 데이터 테이블 Testing // 3. 채점테이블 Scoring // 4. 채점리포트테이블 Reporttable 
use kopoctc;

#1. 과목별 정답 데이터 넣는 procedure 생성 및 호출
delete from Answer where subjectID > 0;		#테이블에 값 대입 전, Answer 테이블에 있는 모든 값 제거
drop procedure if exists inputAnswer;	#inputAnswer 프로시져가 있다면 제거한다. => 프로시져가 잘못 생성됐다면, 제거하고 다시만드는게 빠르다.

#정답입력 프로시져 생성시작
#문제는 5지선다(1,2,3,4,5)로 답한다.
delimiter $$
create procedure inputAnswer()		#프로시져명 inputAnswer
begin
declare _cnt1 integer;				#반복문에서 쓰일 카운트 변수 선언 => 과목수 카운트용
set _cnt1 = 0;						#변수 초기화
   _loop: loop						#반복문 시작, 반복문명 _loop	=> DB필드명과 구분위해 프로시져 변수는 접두사 _ 사용
      set _cnt1 = _cnt1 + 1;			#반복문 시작하면 _cnt1 값 1 증가
      insert into Answer values 	#Answer테이블에 총 20개의 난수(1~5범위) 대입 => 20문제니까 답도 20개
         (_cnt1, (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), 
			  (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1),
			  (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), 
			  (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1));
      if _cnt1 = 3 then		#kor, eng, mat => 총 3과목이기 때문에 답안도 총 3개 => _cnt1가 3이되면
         leave _loop;				#카운트 변수가 3이되면 루프 탈출!!!
      end if;	#if문 종료
   end loop _loop;	#반복문 탈출!!!!!!		
end $$
delimiter ;

call inputAnswer();		#프로시져 호출
select * from Answer;	#결과물 확인

################################################################################################################
set sql_safe_updates = 0;	#**********보안 해제해서 테이블내 데이터 삭제가능.************
#2. 학생이 제출한 답안 넣는 procedure 생성 및 호출
#문제 조건 : 대입할 데이터는 총 1000건
delete from Testing where stu_id > 0;		#테이블에 값 대입 전, Testing 테이블에 있는 모든 값 제거
drop procedure if exists inputStuAnswer;	#기존에 inputStuAnswer 프로시져 있다면, 제거

# 1) 프로시져 생성시작
#학생은 5지선다(1,2,3,4,5)로 답한다.
#프로시져 구조 
#외부 반복문에서 subjectID는 1,2,3 까지만 반복
#내부 반복문에서 subjectID가 1일 때 1000건 데이터 입력, subjectID가 2일 때 1000건 데이터 입력, subjectID가 3일 때 1000건 데이터 입력
#*****과목변수 _cnt1 과 학생수 변수 _cnt2로 해당 프로시져를 통제한다.*****
delimiter $$
create procedure inputStuAnswer(_stuAnswer integer)
begin
declare _name varchar(20);		#이름 변수 선언
declare stu_id int;				#학번 변수 선언
declare _cnt1 int;				#루프문에서 쓰일 카운트 변수 선언 => 과목번호를 의미 => 1은 kor, 2는 eng, 3은 mat
declare _cnt2 int;				#이중루프문에 쓰일 변수 선언
set _cnt1 = 0;					#변수 초기화
	_loop: loop					#반복문1 , 반복문명 _loop
		set _cnt2 = 0;		#반복문2에 쓰일 변수 선언 및 초기화 => 과목당 답안 입력개수 => 1과목 1천개, 2과목 1천개, 3과목 1천개
		set _cnt1 = _cnt1 + 1;	#카운트 변수 루프 돌면서 + 1
		_loop2: loop				#반복문2 , 반복문명 _loop2
			set _cnt2 = _cnt2 + 1;		#이중 루프에서 돌면서 변수에 +1
			set stu_id = 20220000 + _cnt2;	#학번 20220000 + cnt2변수  => _cnt2변수로 학생수 통제 => 매개변수 _stuAnswer과 같아지면 반복문 종료할 것임. 
			set _name = concat("혜임", cast(_cnt2 as char(4)));		# 1) int형 변수 _cnt2을 char로 type cast 후, 2) concat으로 접두사 혜임과 _cnt2 한 단어로 연결
			
            #학생이 제출한 답안 데이터를 저장하는 테이블인 Testing 테이블에 값 대입 => 1~5 난수 생성
            insert into Testing values(_cnt1, _name, stu_id,		#과목번호, 학생이름, 학번, 20개 답 => 첫 시행시 1과목에 대한 데이터를 매개변수로 받은 숫자만큼 입력 , 다음시행시 2과목에대한, 그 다음시행시 3과목에대한 성적 입력
										(rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), 
										(rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1),
										(rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), 
										(rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1), (rand()*4+1));
                                        
            if _cnt2 = _stuAnswer then	#_cnt2변수가 인자로 받아온 integer값과 같아지면
				leave _loop2; 			#이중루프문(내부 반복문) 탈출 => 과목당 입력한 학생답안수에 도달하면 내부반복문 종료 => 다음 과목에 대한 값을 또 반복해서 입력한다.
			end if;				# if문 종료
		end loop _loop2;		# 내부반복문 종료
	if _cnt1 = 3 then	# 과목이 3개다 => _cnt1가 3이되면 (== 1,2,3과목에 대한 학생 1000명의 제출답안 데이터를 다 입력하면)
		leave _loop; 			# 루프문 완전 탈출
	end if;					# if문 종료
	end loop _loop;				# 반 복 문 완 전 종 료!!!!!!!!!!!!!!
end $$
delimiter ;

# 2) 프로시저 호출
call inputStuAnswer(1000); 		#학생 1000명의 답안을 입력한다.

# 3) 데이터 개수 확인 => 학생1인당 3개행이 생성된다 => 학생1000명이면, 3000행 생성
select count(*) from Testing; 
 
# 4) Testing 테이블 값 확인용 출력
#limit 0 , 9 => 인덱스 0번(1행)부터 9행 읽음. => 혜임123456789, 혜임10
#limit 1,10; => 혜임23456789,10,11 => 인덱스1포함 10개 출력 => 2행 포함 10개출력
#limit 10, 10; => 혜임 11,12, .... 혜임20 => 인덱스 10포함 10개 출력 => 11행 포함 10개 출력
select * from Testing limit 0, 10;
	#인덱스 0포함 10개 출력 => 1행 포함 10개 출력 => 1행~10행 출력 => 출력결과 : subjectID는 1/ stu_name은 혜임1, 혜임2... 혜임10 / stu_id는 20220001, 20220002, ...20220010
    #1행~1000행 : 1과목 학생1~학생1000 의 제출성적 출력
    
select * from Testing limit 1000, 10;
	#인덱스 1000포함 10개 출력 => 1001행 포함 10개 출력 => 1001행~1010행 출력 => 출력결과 : subjectID는 2/ stu_name은 혜임1, 혜임2... 혜임10 / stu_id는 20220001, 20220002, ...20220010
    #1001행~2000행: 2과목 학생1~학생1000 의 제출성적 출력
    
select * from Testing limit 2000, 10;
	#인덱스 2000포함 10개 출력 => 2001행 포함 10개 출력 => 2001행~2010행 출력 => 출력결과 : subjectID는 3/ stu_name은 혜임1, 혜임2... 혜임10 / stu_id는 20220001, 20220002, ...20220010
    #2001행~3000행: 3과목 학생1~학생1000 의 제출성적 출력