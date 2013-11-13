alter table  EUMOWY.SIGNATURE MODIFY TEMPLATE_PATH varchar2(255 char) null;

alter table EUMOWY.SIGNATURE add FILENAME varchar2(255 CHAR) NULL;

alter table EUMOWY.DOCUMENT add CLIENT_NAME varchar2(255 CHAR) NULL;

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

  ALTER TABLE EUMOWY.SIGNATURE ADD SEND_TO_CLIENT number(1,0);
  ALTER TABLE EUMOWY.SIGNATURE ADD SHOW_ON_PREVIEW number(1,0);
  UPDATE EUMOWY.SIGNATURE s SET s.SEND_TO_CLIENT = '1', s.SHOW_ON_PREVIEW = '1';
  UPDATE EUMOWY.SIGNATURE s SET s.SEND_TO_CLIENT = '0', s.SHOW_ON_PREVIEW = '0' where s.name = 'AP/F/DS/2.000/09-04-22';
  UPDATE EUMOWY.SIGNATURE s set s.SEND_TO_CLIENT = '0', s.SHOW_ON_PREVIEW = '0' where s.filename is null;

  ALTER TABLE EUMOWY.SUBSCRIPTION ADD UNIQUE_KEY varchar2(255 char);

-- brakujace rzeczy

alter table EUMOWY.HIRE_PAYMENT add constraint HIRE_PAYMENT_PROCESS_ID_FK foreign key (process_id) references EUMOWY.PROCESS;

  -- NEW COLUMNS
alter table EUMOWY.POS_DETAILS add (
  dialup_price_pref number(8,2),
  dialup_pp_price_pref number(8,2),
  vpn_price_pref number(8,2),
  vpn_pp_price_pref number(8,2),
  ssl_price_pref number(8,2),
  ssl_pp_price_pref number(8,2),
  gprs_price_pref number(8,2),
  gprs_pp_price_pref number(8,2),
  pin_pad_price_pref number(8,2),
  wifi_price_pref number(8,2),
  sim_card_type varchar2(255 char),
  sim_card_count number(10,0)
);

alter table
   EUMOWY.POINT_DETAILS
modify
(
   service_care1 varchar2(12 char),
   service_care2 varchar2(12 char),
   service_care3 varchar2(12 char),
   business_care varchar2(12 char)
);

