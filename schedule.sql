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