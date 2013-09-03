create tablespace EUMOWY_DATA datafile '/oradata/cbd01out/EUMOWY_DATA_01.dbf' size 1G autoextend on next 1G;
create tablespace EUMOWY_INDX datafile '/oradata/cbd01out/EUMOWY_INDX_01.dbf' size 100M autoextend on next 100M;

create user eumowy identified by V7S1947nK89O DEFAULT TABLESPACE EUMOWY_DATA;
GRANT RESOURCE TO eumowy;
GRANT CONNECT TO eumowy;

create user eumowy_app identified by eumowy_app DEFAULT TABLESPACE EUMOWY_DATA;
GRANT CONNECT, CREATE SYNONYM TO eumowy_app;