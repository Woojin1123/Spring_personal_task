create database if not exists schedule;
use schedule;
create table schedule
(
	todo varchar(100),
    manager varchar(20),
    pwd varchar(10),
    registerDate datetime,
    updateDate datetime
);

-- id컬럼을 primary key로 설정하고 auto_increment적용
alter table schedule add id int primary key auto_increment;

-- pwd값을 최대 64자리로 변경
ALTER TABLE schedule MODIFY pwd varchar(64);

CREATE TABLE manager
(
    id int primary key auto_increment,
    name VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    registerDate DATETIME,
    updateDate DATETIME
);
ALTER TABLE schedule ADD COLUMN manager_id int;
-- schedule에서 이름 대신 ID번호로 일정 저장
ALTER TABLE schedule DROP COLUMN manager;