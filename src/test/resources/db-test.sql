delete from DomainEventEntry;

insert into DomainEventEntry values
('EVAL-001', 0, 'EvalSeason', 'b112ba9d-ebf6-43ce-bcc5-adfe215205fd', '{}',
'{"evalSeasonId":"EVAL-001","name":"이름","creationDate":"2015-01-15T06:23:13.828+0000"}', null,
'net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent',
'2015-01-15T03:57:52.552Z');

delete from User;
-- 피평가자
insert into User values ('retee11', '피평가자11', '1234');
insert into User values ('ratee12', '피평가자12', '1234');
insert into User values ('ratee21', '피평가자21', '1234');
insert into User values ('ratee22', '피평가자22', '1234');
-- 피평가자의 1차 평가자
insert into User values ('rater11', '평가자11', '1234');
insert into User values ('rater12', '평가자12', '1234');
-- 피평가자의 2차 평가자
insert into User values ('rater2', '평가자2', '1234');
