create database compeval CHARACTER SET utf8;
create database compeval4test CHARACTER SET utf8;

CREATE USER 'evaluser'@'localhost' IDENTIFIED BY 'evalpass!!';
CREATE USER 'evaluser'@'%' IDENTIFIED BY 'evalpass!!';

GRANT ALL PRIVILEGES ON compeval.* TO 'evaluser'@'localhost';
GRANT ALL PRIVILEGES ON compeval.* TO 'evaluser'@'%';
GRANT ALL PRIVILEGES ON compeval4test.* TO 'evaluser'@'localhost';
GRANT ALL PRIVILEGES ON compeval4test.* TO 'evaluser'@'%';

drop table snapshot_event;
drop table domain_event;
drop table user;
drop table authority;
drop table user_directory;
drop table user_directory_config;

create table snapshot_event (
    aggregateIdentifier VARCHAR(255) NOT NULL,
    sequenceNumber  BIGINT       NOT NULL,
    type            VARCHAR(255) NOT NULL,
    eventIdentifier VARCHAR(255) NOT NULL,
    metaData        TEXT,
    payload         TEXT         NOT NULL,
    payloadRevision VARCHAR(255),
    payloadType     VARCHAR(255) NOT NULL,
    timeStamp       VARCHAR(255) NOT NULL,
    PRIMARY KEY (aggregateIdentifier, sequenceNumber, type)
) character set utf8;

create table domain_event (
    aggregateIdentifier varchar(255) not null,
    sequenceNumber bigint not null,
    type varchar(255) not null,
    eventIdentifier varchar(255) not null,
    metaData text,
    payload text not null,
    payloadRevision varchar(255),
    payloadType varchar(255) not null,
    timeStamp varchar(255) not null,
    primary key (aggregateIdentifier, sequenceNumber, type)
) character set utf8;

create table user (
    user_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_directory_id int,
    PRIMARY KEY (user_id)
) character set utf8;

create table authority (
    user_id VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role)
) character set utf8;

create table user_directory (
    user_directory_id int not null AUTO_INCREMENT,
    directory_type varchar(255),
    name varchar(255),
    internal varchar(1),
    primary key(user_directory_id)
) character set utf8;

create table user_directory_config (
    user_directory_id int not null,
    name varchar(255),
    value varchar(255),
    primary key(user_directory_id, name)
) character set utf8;