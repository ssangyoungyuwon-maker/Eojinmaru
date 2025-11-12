/* 
-----------------------------------------------------------------------------------------------------  
-- 김어진 
CREATE TABLE publisher(
    publisher_id VARCHAR2(50) PRIMARY KEY,
    publisher_name VARCHAR2(50) NOT NULL,
    publisher_address VARCHAR2(50) NOT NULL,
    publisher_email VARCHAR2(50) NOT NULL,
    publisher_tel VARCHAR2(50) NOT NULL
); 

CREATE TABLE bookinfo(
    ISBN VARCHAR2(20) PRIMARY KEY,
    category_id NUMBER(4),
    publisher_id VARCHAR2(50) NOT NULL,
    bookname VARCHAR2(50) NOT NULL,
    publish_date DATE NOT NULL
);

CREATE TABLE bookcategory(
    category_id NUMBER(4) PRIMARY KEY,
    category_name VARCHAR2(50) NOT NULL
);

SELECT * FROM PUBLISHER;
SELECT * FROM bookinfo;
SELECT * FROM PUBLISHER;

-----------------------------------------------------------------------------------------------------
-- 김초원

CREATE TABLE admin (
    admin_code NUMBER PRIMARY KEY,
    admin_id VARCHAR2(50) NOT NULL,
    admin_pwd VARCHAR2(50) NOT NULL
);

CREATE TABLE notice (
    notice_id NUMBER PRIMARY KEY,
    notice_title VARCHAR2(4000) NOT NULL,
    notice_content VARCHAR2(4000) NOT NULL,
    notice_date DATE
);

CREATE TABLE author (
    author_id VARCHAR2(50) NOT NULL,
    isbn VARCHAR2(20),
    book_code NUMBER(4),
    PRIMARY KEY(author_id)
);

-----------------------------------------------------------------------------------------------------
-- 함형서

-- 도서 대출 테이블 
CREATE TABLE Loan (
    loan_code NUMBER(4) PRIMARY KEY,
    book_code NUMBER(4),
    user_code NUMBER(4),
    checkout_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    isExtended VARCHAR2(1)
);


-- 도서 테이블
CREATE TABLE Book (
    book_code NUMBER(4) PRIMARY KEY,
    ISBN VARCHAR2(20),
    book_condition VARCHAR2(50)
);

-- 도서 신청 목록 테이블
CREATE TABLE sincheong (
    sincheong_code NUMBER PRIMARY KEY,
    sincheong_name VARCHAR2(1000),
    sincheong_status VARCHAR2(50)
);

-----------------------------------------------------------------------------------------------------
-- 서유원
-- 데이터 저장하는 쿼리 작성 (회원, 대출 예약)

-- userinfo 테이블
CREATE TABLE userinfo (
    user_code           NUMBER(4)       PRIMARY KEY,
    user_id             VARCHAR2(50)    NOT NULL UNIQUE,
    user_pwd            VARCHAR2(50)    NOT NULL,
    user_name           VARCHAR2(50)    NOT NULL,
    user_birth          DATE            NOT NULL,
    user_tel            VARCHAR2(50)    NOT NULL,
    user_email          VARCHAR2(50)    NOT NULL,
    user_address        VARCHAR2(50)    NOT NULL,
    loan_renewaldate    DATE            NOT NULL
);


-- userinfo 테이블 제약조건
    -- PRIMARY KEY, NOT NULL 제약조건 외 별도 제약조건 없음


-- loanreservation 테이블
CREATE TABLE loanreservation (
    user_code               NUMBER(4)       NOT NULL,
    book_code               NUMBER(4)       NOT NULL,
    reservation_date        DATE
);



----------------------------------------------------------------------------
-- 제약조건


-- loanreservation 테이블 제약조건
ALTER TABLE loanreservation add constraint fk_loanreservation_userinfo foreign key (user_code) REFERENCES userinfo (user_code) ;
ALTER TABLE loanreservation add constraint fk_loanreservation_bookcode foreign key (book_code) REFERENCES book (book_code) ;

-- BookInfo 테이블 제약조건
ALTER TABLE author add  constraint fk_author_isbn FOREIGN KEY (ISBN) REFERENCES BookInfo(ISBN);
 

-- author 테이블 제약조건
CONSTRAINT fk_at_isbn FOREIGN KEY(isbn) REFERENCES BookInfo(isbn);
CONSTRAINT fk_at_bookcode FOREIGN KEY(book_code) REFERENCES book(book_code);

-- Loan 테이블 제약조건
ALTER TABLE Loan add constraint fk_loan_book foreign key (book_code) REFERENCES Book (book_code);
ALTER TABLE Loan add constraint fk_loan_userinfo foreign key (user_code) REFERENCES user

info (user_code);

-- Book 테이블 제약조건
ALTER TABLE Book add constraint fk_book_bookinfo foreign key (ISBN) REFERENCES BookInfo (ISBN);


select * from tab;
DROP TABLE BOOKINFO PURGE;

----------------------------------------------------------------------------
-- 시퀀스 (초기화 하려면 삭제 후 다시 생성)
  -- 1000번부터 시작하고, 1씩 증가하는 시퀀스(자동 번호 생성기)를 만듭니다. 
CREATE SEQUENCE user_seq
START WITH 1000
INCREMENT BY 1;

DROP SEQUENCE user_seq;


----------------------------------------------------------------------------
-- 명령어
select * from userinfo;

DROP TABLE userinfo PURGE;

-- 유저 삭제
DELETE FROM userinfo
WHERE user_code = 1000;
COMMIT;



----------------------------------------------------------------------------
-- 데이터 (관리자 화면에서 loan_renewaldate 의 SYSDATE는 추후에 연체된 날짜로 변경해야함)

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test01', 'pwd001!', '김민수', '1990-05-15', '010-1234-5678', 'kim.ms@example.com', '서울특별시 강남구 테헤란로 123', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test02', 'pwd002!', '이서연', '1993-11-20', '010-9876-5432', 'lee.sy@example.com', '부산광역시 해운대구 마린시티로 45', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test03', 'pwd003!', '박지훈', '1985-02-10', '010-3333-7777', 'park.jh@example.com', '대구광역시 수성구 동대구로 789', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test04', 'pwd004!', '최유진', '1998-07-25', '010-4444-8888', 'choi.yj@example.com', '인천광역시 연수구 송도동 101', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test05', 'pwd005!', '정우성', '1978-04-01', '010-5555-9999', 'jung.ws@example.com', '광주광역시 서구 상무대로 202', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test06', 'pwd006!', '한예슬', '2000-12-30', '010-6666-0000', 'han.ys@example.com', '대전광역시 유성구 도룡동 303', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test07', 'pwd007!', '윤지민', '1995-08-08', '010-7777-1111', 'yoon.jm@example.com', '울산광역시 남구 삼산로 404', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test08', 'pwd008!', '장동건', '1982-03-17', '010-8888-2222', 'jang.dk@example.com', '경기도 수원시 영통구 광교중앙로 505', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test09', 'pwd009!', '신민아', '1991-06-03', '010-9999-3333', 'shin.ma@example.com', '강원특별자치도 춘천시 중앙로 606', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test10', 'pwd010!', '송강호', '1975-01-22', '010-1111-4444', 'song.kh@example.com', '충청북도 청주시 흥덕구 707', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test11', 'pwd011!', '고아라', '1996-10-10', '010-2222-5555', 'go.ar@example.com', '충청남도 천안시 서북구 808', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test12', 'pwd012!', '류승룡', '1980-09-05', '010-3333-6666', 'ryu.sr@example.com', '전라북도 전주시 완산구 909', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test13', 'pwd013!', '김하늘', '1994-04-12', '010-4444-7777', 'kim.hn@example.com', '전라남도 목포시 항동 111', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test14', 'pwd014!', '하정우', '1979-11-29', '010-5555-8888', 'ha.jw@example.com', '경상북도 포항시 남구 222', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test15', 'pwd015!', '손예진', '1983-07-01', '010-6666-9999', 'son.yj@example.com', '경상남도 창원시 성산구 333', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test16', 'pwd016!', '현빈', '1988-02-14', '010-7777-0000', 'hyun.b@example.com', '제주특별자치도 제주시 첨단로 444', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test17', 'pwd017!', '수지', '1994-10-10', '010-8888-1111', 'suzi@example.com', '세종특별자치시 한누리대로 555', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test18', 'pwd018!', '조인성', '1981-03-28', '010-9999-2222', 'jo.is@example.com', '서울특별시 마포구 월드컵북로 666', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test19', 'pwd019!', '김태리', '1990-04-24', '010-1212-3434', 'kim.tr@example.com', '부산광역시 수영구 광안해변로 777', SYSDATE);

INSERT INTO userinfo (user_code, user_id, user_pwd, user_name, user_birth, user_tel, user_email, user_address, loan_renewaldate) 
VALUES (user_seq.NEXTVAL, 'user_test20', 'pwd020!', '공유', '1979-07-10', '010-5656-7878', 'gong.y@example.com', '경기도 성남시 분당구 판교역로 888', SYSDATE);

































































*/
