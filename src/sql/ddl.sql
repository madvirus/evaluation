drop table SnapshotEventEntry;
drop table DomainEventEntry;

create table SnapshotEventEntry (
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

create table DomainEventEntry (
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

create table User (
    user_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id)
) character set utf8;