delete from domain_event;
delete from snapshot_event;

insert into domain_event values
('EVAL-001', 0, 'EvalSeason', 'b112ba9d-ebf6-43ce-bcc5-adfe215205fd', '{}',
'{"evalSeasonId":"EVAL-001","name":"이름","creationDate":"2015-01-15T06:23:13.828+0000"}', null,
'net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent',
'2015-01-15T03:57:52.552Z');

delete from user;
-- 피평가자
insert into user values ('ratee11', '피평가자11', '27bb8f62c974e28d2d1590618a0f9e4546169bd5525df9503e9ce77728f48837c0182e2bbb5f20d3', 1);
insert into user values ('ratee12', '피평가자12', '3740842f977468bbf0b744e654869979b377a32eb73536914ec61f99d614a53ba94b6db452251120', 1);
insert into user values ('ratee21', '피평가자21', 'b942c1105fdf7254ede6fa80b4465e27aaadab6468d14b777dffbd0b2fe66b168407bdd0ccd127a8', 1);
insert into user values ('ratee22', '피평가자22', 'f19196a0225cd5698564358467c731c1c37640841c7c41ad94ec2a75f9d3dbb47879c80310576434', 1);
-- 피평가자의 1차 평가자
insert into user values ('rater11', '평가자11', '5ef036fc8bd277a12d3d339b3d379f6fb64be5b3b49acee3d859b0cb40b6a3c9f3cc3559271685c6', 1);
insert into user values ('rater12', '평가자12', 'd4689820ae410d6df748b0f111fd9fa4e51f033e8b56e9ac968c050d9a22f26f5c1fef1a37b0527d', 1);
-- 피평가자의 2차 평가자
insert into user values ('rater2', '평가자2', '637ba899826234e4565656734b62cf0c4b688ea39bda6f3021050b71c0d5e726cd2f90ab984c5982', 1);
-- 시스템 관리자
insert into user values ('systemadmin', '시스템관리자', '22ed110c90b226a1c42b59a06e72b69f1406d742136eb27047d54d34f568b9d590fbdc0ee267d493', 1);

-- AD 테스트 대상
insert into user values ('bkchoi', '최범균', '', 2);

delete from authority;
insert into authority values ('systemadmin', 'ROLE_SYSTEMADMIN');

delete from user_directory;
insert into user_directory values (1, 'INTERNAL', 'internal', 'T');
insert into user_directory values (2, 'AD', 'Active Directory', 'F');

delete from user_directory_config;
insert into user_directory_config values (2, 'host', 'ldap.corpdomain.co.kr');
insert into user_directory_config values (2, 'port', '389');
insert into user_directory_config values (2, 'baseDN', 'DC=corp,DC=corpdomain,DC=co,DC=kr');
insert into user_directory_config values (2, 'user', 'ldap');
insert into user_directory_config values (2, 'password', 'ldappass');
insert into user_directory_config values (2, 'userNameAttr', 'sAMAccountName');
