create tablespace EUMOWY datafile '/oradata/cbd01out/EUMOWY_01.dbf' size 1G autoextend on next 1G;
create tablespace EUMOWY_I datafile '/oradata/cbd01out/EUMOWY_I_01.dbf' size 100M autoextend on next 100M;

create user eumowy identified by V7S1947nK89O DEFAULT TABLESPACE EUMOWY;
ALTER USER "eumowy" PROFILE SERVICE_CBD;
GRANT RESOURCE TO eumowy;
GRANT CONNECT TO eumowy;

create user eumowy_app identified by eumowy_app DEFAULT TABLESPACE EUMOWY;
ALTER USER "eumowy_app" PROFILE SERVICE_CBD;
GRANT CONNECT, CREATE SYNONYM TO eumowy_app;
