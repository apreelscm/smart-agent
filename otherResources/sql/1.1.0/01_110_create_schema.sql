alter table  EUMOWY.PROCESS MODIFY VERSION NUMBER(3,0);

ALTER TABLE EUMOWY.POS ADD parent_pos_id number(19,0);

create sequence EUMOWY.HIRE_PAYMENT_SEQ;

create table EUMOWY.HIRE_PAYMENT (
  id number(19,0) not null,
  version number(19,0) not null,
  ADDRESS varchar2(255 char),
  CBD_ID number(10,0),
  CURRENT_PP_PAYMENT number(19,2),
  CURRENT_TERM_PAYMENT number(19,2),
  IS_CHOOSEN number(1,0),
  FULL_NAME varchar2(255 char),
  NEW_PP_PAYMENT number(19,2),
  NEW_TERM_PAYMENT number(19,2),
  POS_NUMBER varchar2(255 char),
  PP_COUNT number(10,0),
  process_id number(19,0) not null,
  TERM_COUNT number(10,0),
  TPS_ID number(10,0),
  TYPE varchar2(255 char),
  primary key (id));

  ALTER TABLE EUMOWY.HIRE_PAYMENT ADD IS_VISIBLE number(1,0);
