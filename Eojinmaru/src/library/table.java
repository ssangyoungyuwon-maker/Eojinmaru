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
    author_id VARCHAR2(50) primary key, 
    isbn VARCHAR2(20),
    author_name varchar2(50)
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

-- 도서 폐기 테이블 
CREATE TABLE disposedbook (
    book_code NUMBER(4) NOT NULL,
    dispose_date DATE DEFAULT SYSDATE,
    ISBN VARCHAR2(20),
    dispose_reason  VARCHAR2(200)
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
ALTER TABLE loanreservation add constraint fk_loanreservation_usercode foreign key (user_code) REFERENCES userinfo (user_code); -- 완료
ALTER TABLE loanreservation add constraint fk_loanreservation_bookcode foreign key (book_code) REFERENCES book (book_code) ;  -- 완료

-- BookInfo 테이블 제약조건
ALTER TABLE BookInfo add constraint fk_publiser_publisher_id FOREIGN KEY (PUBLISHER_ID) REFERENCES publisher (PUBLISHER_ID);    -- 완료
ALTER TABLE BookInfo add constraint fk_bookcategory_category_id FOREIGN KEY (category_id) REFERENCES BOOKCATEGORY(category_id); -- 완료
 

-- author 테이블 제약조건
ALTER TABLE AUTHOR ADD CONSTRAINT fk_author_isbn FOREIGN KEY(isbn) REFERENCES BookInfo(isbn); -- 완료

-- Loan 테이블 제약조건
ALTER TABLE Loan add constraint fk_loan_book foreign key (book_code) REFERENCES Book (book_code); -- 완료
ALTER TABLE Loan add constraint fk_loan_userinfo foreign key (user_code) REFERENCES userinfo (user_code); -- 완료


-- Book 테이블 제약조건
ALTER TABLE BOOK add constraint fk_book_bookinfo foreign key (ISBN) REFERENCES BookInfo(isbn); -- 완료

----------------------------------------------------------------------------
-- 시퀀스 (초기화 하려면 삭제 후 다시 생성)
  -- 1000번부터 시작하고, 1씩 증가하는 시퀀스(자동 번호 생성기)를 만듭니다. 
CREATE SEQUENCE user_seq
START WITH 1000
INCREMENT BY 1;

DROP SEQUENCE user_seq;
































*/
