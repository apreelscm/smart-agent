-- CREATE TABLES
create table EUMOWY.APP_PARAMETERS (
  id number(12,0) not null,
  name varchar2(255 char),
  value varchar2(512 char),
  constraint PK_APP_PARAMETERS primary key (id));

create table EUMOWY.ACTIVITY (
  id number(12,0) not null,
  version number(2,0) not null,
  code varchar2(255 char),
  numer_pozycji number(10,0),
  constraint PK_ACTIVITY primary key (id));

create table EUMOWY.ACTIVITY_SIGNATURES (
  id number(12,0) not null,
  version number(2,0) not null,
  activity_id number(12,0),
  mandatory number(1,0) not null,
  number_of_list number(10,0),
  signature_id number(12,0),
  constraint PK_ACTIVITY_SIGNATURES primary key (id));

create table EUMOWY.ATTACHMENT (
  id number(12,0) not null,
  version number(2, 0) not null,
  date_uploaded timestamp(6),
  downloads number(4, 0),
  file_size number(12, 0),
  extension varchar2(120 CHAR),
  name varchar2(255 CHAR),
  process_id number(12, 0),
  attachments_idx number(10,0),
  constraint PK_ATTACHMENT primary key (id));

create table EUMOWY.ATTACHMENT_CONTENT (
  id number(12,0) not null,
  version number(2,0) not null,
  attachment_id number(12,0) not null,
  content BLOB not null,
  constraint PK_ATTACHMENT_CONTENT primary key (id));

create table EUMOWY.CALCFIELD (
  id number(12,0) not null,
  version number(2,0) not null,
  name varchar2(255 char),
  constraint PK_CALCFIELD primary key (id));

create table EUMOWY.CALCFIELD_SIGNATURE (
  id number(12,0) not null,
  version number(12,0) not null,
  calc_field_id number(12,0),
  signature_id number(12,0),
  constraint PK_CALCFIELD_SIGNATURE primary key (id));

create table EUMOWY.CLIENT (
  id number(12,0) not null,
  version number(2,0) not null,
  cbd_id varchar2(20 char),
  nip varchar2(20 char),
  mid varchar2(15 char),
  name varchar2(255 char),
  constraint PK_CLIENT primary key (id));

create table EUMOWY.DOCUMENT (
  id number(12,0) not null,
  version number(2,0) not null,
  process_id number(12,0),
  signature_id number(12,0),
  date_created timestamp,
  last_updated timestamp,
  pages_count number(10,0) not null,
  name varchar2(255 char) not null,
  documents_idx number(12,0),
  constraint PK_DOCUMENT primary key (id));

create table EUMOWY.DOCUMENT_CONTENT (
  id number(12,0) not null,
  version number(2,0) not null,
  document_id number(12,0) not null,
  content blob not null,
  constraint PK_DOCUMENT_CONTENT primary key (id));

create table EUMOWY.EMAIL_TEMPLATES (
  id number(12,0) not null,
  version number(2,0) not null,
  name varchar2(255 char),
  recipient varchar2(150 char),
  sender varchar2(150 char),
  constraint PK_EMAIL_TEMPLATES primary key (id));

  CREATE TABLE EUMOWY.EUM_SYGNATURA_FUNKCJA
  (
    ESF_MAP_ID        NUMBER NOT NULL ENABLE,
    ESF_SYGNATURA     VARCHAR2(255 BYTE),
    ESF_NAZWA_SERWISU VARCHAR2(255 BYTE),
    ESF_NAZWA_FUNKCJI VARCHAR2(255 BYTE),
    ESF_KOLEJNOSC     NUMBER,
    ESF_ADRES_SERWISU VARCHAR2(255 BYTE),
    CONSTRAINT PK_EUM_SYGNATURA_FUNKCJA PRIMARY KEY (ESF_MAP_ID)
  );

  CREATE TABLE EUMOWY.KALKULATORTYPURZADZEN
  (
    TYP     VARCHAR2(50 BYTE),
    SLOWNIK VARCHAR2(50 BYTE),
    SMT_ID  NUMBER(10,0),
    ID      NUMBER(10,0) NOT NULL ENABLE,
    MEDIUM  VARCHAR2(20 BYTE),
    CONSTRAINT PK_KALKULATORTYPURZADZEN PRIMARY KEY (ID)
  );

create table EUMOWY.PANEL (
  id number(12,0) not null,
  version number(2,0) not null,
  name varchar2(120 char),
  order_no number(3,0),
  constraint PK_PANEL primary key (id));

CREATE TABLE EUMOWY.PANEL_ADDITIONAL_INFO
  (
    ID            NUMBER,
    NAZWA_SERWISU VARCHAR2(50 BYTE),
    ADRES_SERWISU VARCHAR2(50 BYTE),
    NAZWA_FUNKCJI VARCHAR2(255 BYTE),
    CZY_SYNCHRO   VARCHAR2(1 BYTE),
    CONSTRAINT PK_PANEL_ADDITIONAL_INFO PRIMARY KEY (ID)
);

CREATE TABLE EUMOWY.PROCESS_PANEL
(
  PROCESS_PANELS_ID NUMBER(12,0) not null ,
  PANEL_ID          NUMBER(12,0) not null,
  PANELS_IDX        NUMBER(10,0),
  constraint PK_PROCESS_PANEL primary key (PROCESS_PANELS_ID,PANEL_ID)
);

-- todo do weryfikacji not null
create table EUMOWY.POINT (
  id number(12,0) not null,
  version number(2,0) not null,
  process_id number(12,0),
  cbd_id number(10,0) not null,
  nip varchar2(20 char) not null,
  name varchar2(120 char),
  pos_count number(10,0),
  postal_code varchar2(10 char) not null,
  city varchar2(120 char) not null,
  home_number varchar2(20 char) not null,
  flat_number varchar2(20 char) not null,
  post_office varchar2(80 char) not null,
  street varchar2(80 char) not null,
  payment_title number(1,0),
  cash_system number(1,0),
  uta number(1,0),
  is_selected_card_accept number(1,0),
  is_selected_range number(1,0),
  constraint PK_POINT primary key (id));

-- todo do weryfikacji not null
create table EUMOWY.POINT_DETAILS (
  id number(12,0) not null,
  version number(2,0) not null,
  point_id number(12,0) not null,
  nip varchar2(20 char) not null,
  mcc_code varchar2(20 char) not null,
  terminal_count number(3,0) not null,
  bank_account_number varchar2(40 char) not null,
  bank_name varchar2(255 char) not null,
  bank_id number(10,0),
  contact_at_point_email varchar2(80 char) not null,
  contact_addr_streettype varchar2(40 char) not null,
  contact_at_point_fax varchar2(20 char) not null,
  contact_at_point_title varchar2(20 char) not null,
  contact_at_point_firstname varchar2(40 char) not null,
  contact_at_point_lastname varchar2(80 char) not null,
  contact_at_point_mobilephone varchar2(20 char) not null,
  contact_at_point_phone varchar2(20 char) not null,
  contact_addr_postalcode varchar2(20 char) not null,
  contact_addr_home_number varchar2(20 char) not null,
  contact_addr_flat_number varchar2(20 char) not null,
  contact_addr_post_office varchar2(255 char) not null,
  contact_addr_street varchar2(120 char) not null,
  contact_addr_city varchar2(80 char) not null,
  name_print_posterminal varchar2(255 char) not null,
  name_search_engine varchar2(255 char) not null,
  business_care number(6,2) not null,
  service_care1 number(6,2) not null,
  service_care2 number(6,2) not null,
  service_care3 number(6,2) not null,
  ph_gain varchar2(12 char),
  print_addr_postal_code varchar2(20 char) not null,
  print_addr_city varchar2(80 char) not null,
  print_addr_home_number varchar2(20 char) not null,
  print_addr_flat_number varchar2(20 char) not null,
  print_addr_post_office varchar2(255 char) not null,
  print_addr_street varchar2(120 char) not null,
  print_addressstreet_type varchar2(40 char) not null,
  print_otherdata_terminal1 varchar2(255 char) not null,
  print_otherdata_terminal2 varchar2(255 char) not null,
  bussiness_type_in_practice varchar2(255 char) not null,
  constraint PK_POINT_DETAILS primary key (id));

create table EUMOWY.POS (
  id number(19,0) not null,
  version number(19,0) not null,
  is_selected number(1,0),
  date_to timestamp,
  date_from timestamp,
  pos_set_number number(10,0),
  point_id number(19,0),
  tps_id number(10,0),
  price_count number(19,2),
  pos_datas_idx number(10,0),
  constraint PK_POS primary key (id));

-- todo do weryfikacji not null
create table EUMOWY.POS_DETAILS (
  id number(12,0) not null,
  version number(2,0) not null,
  pos_id number(12,0) not null,
  set_analysis number(1,0),
  base_count number(10,0),
  noreturnfunction number(1,0),
  statdevice_ip varchar2(40 char),
  statdevice_gateway varchar(40 char),
  card_reader_price number(8,2),
  card_reader_count number(10,0),
  card_reader_type varchar2(60 char),
  dialup_price number(8,2),
  dialup_count number(6,0),
  dialup_pp_price number(8,2),
  dialup_pp_count number(6,0),
  dialup_type varchar2(255 char),
  gprs_price number(8,2),
  gprs_count number(6,0),
  gprs_pp_price number(8,2),
  gprs_pp_count number(6,0),
  gprs_type varchar2(60 char),
  dynamicdevicesupp_name varchar2(255 char),
  statdevicesupp_contactname varchar2(255 char),
  other_additional_device varchar2(255 char),
  otheradditionaldevice_price number(8,2),
  otheradditionaldevice_gprs number(1,0),
  otheradditionaldevice_count number(6,0),
  otheradditionaldevice_ssl number(1,0),
  otheradditionaldevice_type varchar2(60 char),
  cashmachine_systemintegration number(1,0),
  gift_card number(1,0),
  dynamicdevicesupp_contact varchar2(255 char),
  statdevicesupp_contact varchar2(255 char),
  log_beforeeverytransaction number(1,0),
  log_everychange number(1,0),
  statdevice_mask varchar2(255 char),
  tip1 number(1,0),
  pin_pad_price number(8,2),
  pin_pad_count number(6,0),
  pin_pad_type varchar2(60 char),
  planned_installation_date timestamp,
  preauthorization number(1,0),
  router_price number(8,2),
  router_count number(6,0),
  router_type varchar2(60 char),
  ssl_price number(8,2),
  ssl_count number(6,0),
  ssl_pp_price number(8,2),
  ssl_pp_count number(6,0),
  ssl_type varchar2(60 char),
  tele_kodzik number(1,0),
  tele_pompka number(1,0),
  dynamicdevicesupp_title varchar2(255 char) not null,
  statdevicesupp_title varchar2(255 char) not null,
  vpn_price number(8,2),
  vpn_count number(6,0),
  vpn_pp_price number(8,2),
  vpn_pp_count number(6,0),
  vpn_type varchar2(60 char),
  wifi_price number(8,2),
  wifi_count number(6,0),
  wifi_pp_price number(8,2),
  wifi_pp_count number(6,0),
  wifi_type varchar2(60 char),
  day_close_to varchar2(10 char),
  day_close_from varchar2(10 char),
  return_with_password number(1,0),
  returnIKO number(1,0),
  dynamicdevicesupp_surname varchar2(255 char),
  statdevicesupp_contactsurname varchar2(255 char),
  additional_notes varchar2(1000 char),
  constraint PK_POS_DETAILS primary key (id));

create table EUMOWY.PROCESS (
  id number(12,0) not null,
  version number(2,0) not null,
  client_id number(12,0),
  status varchar2(40 char),
  calc_number varchar2(20 char),
  date_created timestamp not null,
  last_updated timestamp not null,
  observed number(1,0),
  ph_number varchar2(12 char),
  ph_first_name varchar2(40 char),
  ph_surname varchar2(100 char),
  ph_email varchar2(120 char),
  sale_section varchar2(120 char),
  notes_to_coa varchar2(1000 char),
  notes_from_zrd varchar2(300 char),
  constraint PK_PROCESS primary key (id));

create table EUMOWY.PROCESS_DATA (
  id number(12,0) not null,
  process_id number(12,0),
  version number(2,0) not null,
  name varchar2(255 char),
  value varchar2(255 char),
  data_idx number(10,0),
  constraint PK_PROCESS_DATA primary key (id));

create table EUMOWY.SIGNATURE (
  id number(12,0) not null,
  version number(2,0) not null,
  active number(1,0) not null,
  name varchar2(255 char) not null,
  for_point number(1,0) not null,
  ph_subscription_page_number number(6,0),
  ph_subscriptionx number(6,0),
  ph_subscriptiony number(6,0),
  subscription_page_number number(6,0),
  subscriptionx number(6,0),
  subscriptiony number(6,0),
  management_subscription1 varchar2(255 char) not null,
  management_subscription2 varchar2(255 char) not null,
  template_path varchar2(255 char) not null,
  description varchar2(1024 char),
  signature_order number(6,0) not null,
  constraint PK_SIGNATURE primary key (id));

CREATE TABLE EUMOWY.SIGNATURE_ADDITIONAL_INFO (
  ID               NUMBER NOT NULL ENABLE,
  SIGNATURE_ID     NUMBER,
  DATE_CALC_METHOD VARCHAR2(20 BYTE),
  TRANSFER_DAYS    NUMBER,
  "TYPE"           VARCHAR2(20 BYTE),
  NAZWA_CENNIKA    VARCHAR2(255 BYTE),
  CONSTRAINT PK_SIGNATURE_ADDITIONAL_INFO PRIMARY KEY (ID));

create table EUMOWY.SIGNATURE_PANEL (
  id number(12,0) not null,
  version number(2,0) not null,
  panel_id number(12,0),
  signature_id number(12,0),
  constraint PK_SIGNATURE_PANEL primary key (id));

create table EUMOWY.SUBSCRIPTION (
  id number(12,0) not null,
  version number(2,0) not null,
  process_id number(12,0),
  name varchar2(255 char) not null,
  person_role varchar2(40 char),
  sign_date timestamp not null,
  surname varchar2(255 char) not null,
  content long not null, -- todo
  constraint PK_SUBSCRIPTION primary key (id));

create table EUMOWY.PROCESS_ACTIVITY (
  process_activities_id number(12,0),
  activity_id number(12,0),
  activities_idx number(10,0),
  constraint PK_PROCESS_ACTIVITY primary key (process_activities_id, activity_id));

create table EUMOWY.PROCESS_SIGNATURE (
  process_signatures_id number(12,0),
  signature_id number(12,0),
  signatures_idx number(10,0),
  constraint PK_PROCESS_SIGNATURE primary key (process_signatures_id, signature_id));

create table EUMOWY.PROCESS_SUBSCRIPTION (
  process_subscriptions_id number(12,0),
  subscription_id number(12,0),
  subscriptions_idx number(10,0),
  constraint PK_PROCESS_SUBSCRIPTION primary key (process_subscriptions_id,subscription_id));

CREATE TABLE EUMOWY.KALKULATORTYPURZADZEN (
    TYP VARCHAR2(50 BYTE),
    SLOWNIK VARCHAR2(50 BYTE),
    SMT_ID NUMBER(10,0),
    ID NUMBER(10,0));

CREATE TABLE EUMOWY.MAPOWANIEKALKULATORA (
  POLEKALKULATOR VARCHAR2(255 BYTE),
  POLEAPREEL     VARCHAR2(255 BYTE),
  DZIALANIE      VARCHAR2(20 BYTE),
  constraint PK_MAPOWANIEKALKULATORA primary key (POLEKALKULATOR,POLEAPREEL,DZIALANIE));

create table EUMOWY.LOGS (
  login varchar2(100 char),
  log_date varchar2(80 char),
  log_message varchar2(1000 char));

create table EUMOWY.SUBSCRIPTION_DEFINITION (
  id number(19,0) not null,
  version number(19,0) not null,
  file_name varchar2(255 char),
  role varchar2(255 char),
  scalex number(10,0),
  scaley number(10,0),
  signature_id number(19,0) not null,
  subscription_page_number number(10,0),
  subscriptionx number(10,0),
  subscriptiony number(10,0),
  constraint PK_SUBSCRIPTION_DEFINITION primary key (id));

-- CONSTRAINTS
alter table EUMOWY.ACTIVITY_SIGNATURES add constraint ACTIVITY_SIGNATURES_ACT_ID_FK foreign key (activity_id) references EUMOWY.ACTIVITY;
alter table EUMOWY.ACTIVITY_SIGNATURES add constraint ACTIVITY_SIGNATURES_SIGN_ID_FK foreign key (signature_id) references EUMOWY.SIGNATURE;
alter table EUMOWY.ATTACHMENT add constraint ATTACHMENT_PROCESS_ID_FK foreign key (process_id) references EUMOWY.PROCESS;
alter table EUMOWY.CALCFIELD_SIGNATURE add constraint CALCFIELD_SIGNATURE_CLC_ID_FK foreign key (calc_field_id) references EUMOWY.CALCFIELD;
alter table EUMOWY.CALCFIELD_SIGNATURE add constraint CALCFIELD_SIGNATURE_SIG_ID foreign key (signature_id) references EUMOWY.SIGNATURE;
alter table EUMOWY.DOCUMENT add constraint DOCUMENT_SIGNATURE_ID_FK foreign key (signature_id) references EUMOWY.SIGNATURE;
alter table EUMOWY.DOCUMENT add constraint DOCUMENT_PROCESS_ID_FK foreign key (process_id) references EUMOWY.PROCESS;
alter table EUMOWY.DOCUMENT_CONTENT add constraint DOCUMENT_CONTENT_DOC_ID_FK foreign key (document_id) references EUMOWY.DOCUMENT;
alter table EUMOWY.POINT add constraint POINT_PROCESS_ID_FK foreign key (process_id) references EUMOWY.PROCESS;
alter table EUMOWY.POINT_DETAILS add constraint POINT_DETAILS_POINT_ID_FK foreign key (point_id) references EUMOWY.POINT;
alter table EUMOWY.POS add constraint POS_POINT_ID_FK foreign key (point_id) references EUMOWY.POINT;
alter table EUMOWY.POS_DETAILS add constraint POS_DETAILS_POS_ID_FK foreign key (pos_id) references EUMOWY.POS;
alter table EUMOWY.PROCESS add constraint PROCESS_CLIENT_ID_FK foreign key (client_id) references EUMOWY.CLIENT;
alter table EUMOWY.PROCESS_DATA add constraint PROCESS_DATA_PROCESS_ID_FK foreign key (process_id) references EUMOWY.PROCESS;
alter table EUMOWY.SIGNATURE_PANEL add constraint SIGNATURE_PANEL_PANEL_ID_FK foreign key (panel_id) references EUMOWY.PANEL;
alter table EUMOWY.SIGNATURE_PANEL add constraint SIGNATURE_PANEL_SIGN_ID foreign key (signature_id) references EUMOWY.SIGNATURE;
alter table EUMOWY.PROCESS_ACTIVITY add constraint PROCESS_ACTIVITY_ACT_ID_FK foreign key (activity_id) references EUMOWY.ACTIVITY;
alter table EUMOWY.PROCESS_ACTIVITY add constraint PROCESS_ACTIVITY_PROCESS_ID_FK foreign key (process_activities_id) references EUMOWY.PROCESS;
alter table EUMOWY.PROCESS_SIGNATURE add constraint PROCESS_SIGNATURE_SIGN_ID_FK foreign key (signature_id) references EUMOWY.SIGNATURE;
alter table EUMOWY.PROCESS_SIGNATURE add constraint PROCESS_SIGNATURE_PRO_ID_FK foreign key (process_signatures_id) references EUMOWY.PROCESS;
alter table EUMOWY.PROCESS_SUBSCRIPTION add constraint PROCCESS_SUBSCRIPTION_SU_ID_FK foreign key (subscription_id) references EUMOWY.SUBSCRIPTION;
alter table EUMOWY.PROCESS_SUBSCRIPTION add constraint PROCCESS_SUBSCRIPTION_PR_ID_FK foreign key (process_subscriptions_id) references EUMOWY.PROCESS;
alter table EUMOWY.PROCESS_PANEL add constraint PROCESS_PANEL_PANEL_ID_FK FOREIGN KEY (PANEL_ID) REFERENCES EUMOWY.PANEL(ID);
alter table EUMOWY.SUBSCRIPTION_DEFINITION add constraint SUBSCRIPTION_DEF_SIGNATURE_FK foreign key (signature_id) references EUMOWY.SIGNATURE;
alter table EUMOWY.SIGNATURE_ADDITIONAL_INFO add constraint SIGNATURE_ADD_INF_SIGN_FK foreign key (signature_id) references EUMOWY.SIGNATURE;

-- INDEXES
CREATE INDEX ACTIVITY_SIGN_ACTIVITY_ID_IDX ON EUMOWY.ACTIVITY_SIGNATURES(activity_id) TABLESPACE EUMOWY_I;
CREATE INDEX ACTIVITY_SIGN_SIGNATURE_ID_IDX ON EUMOWY.ACTIVITY_SIGNATURES(signature_id) TABLESPACE EUMOWY_I;
CREATE INDEX ATTACHMENT_PROCESS_ID_IDX ON EUMOWY.ATTACHMENT(process_id) TABLESPACE EUMOWY_I;
CREATE INDEX CALCF_SIG_CALC_FIELD_ID_IDX ON EUMOWY.CALCFIELD_SIGNATURE(calc_field_id) TABLESPACE EUMOWY_I;
CREATE INDEX CALCF_SIG_SIGNATURE_ID_IDX ON EUMOWY.CALCFIELD_SIGNATURE(signature_id) TABLESPACE EUMOWY_I;
CREATE INDEX CLIENT_NIP_IDX ON EUMOWY.CLIENT(nip) TABLESPACE EUMOWY_I;
CREATE INDEX DOCUMENT_SIGNATURE_ID_IDX ON EUMOWY.DOCUMENT(signature_id) TABLESPACE EUMOWY_I;
CREATE INDEX DOCUMENT_PROCESS_ID_IDX ON EUMOWY.DOCUMENT(process_id) TABLESPACE EUMOWY_I;
CREATE INDEX LOGS_LOGIN_IDX ON EUMOWY.LOGS(LOGIN) TABLESPACE EUMOWY_I;
--CREATE INDEX DOCUMENT_CONTENT_DOC_ID_IDX ON EUMOWY.DOCUMENT_CONTENT(document_id) TABLESPACE EUMOWY_I;
CREATE INDEX POINT_PROCESS_ID_IDX ON EUMOWY.POINT(process_id) TABLESPACE EUMOWY_I;
--CREATE INDEX POINT_DETAILS_POINT_ID_IDX ON EUMOWY.POINT_DETAILS(point_id) TABLESPACE EUMOWY_I;
--CREATE INDEX POS_POINT_ID_IDX ON EUMOWY.POS(point_id) TABLESPACE EUMOWY_I;
--CREATE INDEX POS_DETAILS_POS_ID_IDX ON EUMOWY.POS_DETAILS(pos_id) TABLESPACE EUMOWY_I;
CREATE INDEX PROCESS_CLIENT_ID_IDX ON EUMOWY.PROCESS(client_id) TABLESPACE EUMOWY_I;
CREATE INDEX PROCESS_DATA_PROCESS_ID_IDX ON EUMOWY.PROCESS_DATA(process_id) TABLESPACE EUMOWY_I;
CREATE INDEX SIGNATURE_PANEL_PANEL_ID_IDX ON EUMOWY.SIGNATURE_PANEL(panel_id) TABLESPACE EUMOWY_I;
CREATE INDEX SIGNATURE_PANEL_SIGN_ID_IDX ON EUMOWY.SIGNATURE_PANEL(signature_id) TABLESPACE EUMOWY_I;
CREATE INDEX PROCESS_ACTIVITY_ACTIV_ID_IDX ON EUMOWY.PROCESS_ACTIVITY(activity_id) TABLESPACE EUMOWY_I;
CREATE INDEX PROCESS_ACTIVITY_PR_ACT_ID_IDX ON EUMOWY.PROCESS_ACTIVITY(process_activities_id) TABLESPACE EUMOWY_I;
CREATE INDEX PROCESS_SIGNATURE_SIGN_ID_IDX ON EUMOWY.PROCESS_SIGNATURE(signature_id) TABLESPACE EUMOWY_I;
CREATE INDEX PROCESS_SIGN_PR_SIG_ID_IDX ON EUMOWY.PROCESS_SIGNATURE(process_signatures_id) TABLESPACE EUMOWY_I;
CREATE INDEX PROCESS_SUBSCR_SUB_ID_IDX ON EUMOWY.PROCESS_SUBSCRIPTION(subscription_id) TABLESPACE EUMOWY_I;
CREATE INDEX PROCESS_SUBSCR_PR_SUB_ID_IDX ON EUMOWY.PROCESS_SUBSCRIPTION(process_subscriptions_id) TABLESPACE EUMOWY_I;
CREATE INDEX PROCESS_PANEL_PANEL_ID_IDX ON EUMOWY.PROCESS_PANEL(PANEL_ID) TABLESPACE EUMOWY_I;
CREATE INDEX SUBSCRIPTION_DEF_SIGN_ID_IDX ON EUMOWY.SUBSCRIPTION_DEFINITION(signature_id) TABLESPACE EUMOWY_I;

CREATE UNIQUE INDEX UN_APP_PARAMETERS_NAME ON APP_PARAMETERS("NAME") TABLESPACE EUMOWY_I;
CREATE UNIQUE INDEX UN_ACTIVITY_CODE ON ACTIVITY(CODE) TABLESPACE EUMOWY_I;
CREATE UNIQUE INDEX UN_ACTIVITY_NUMER_POZYCJI ON ACTIVITY(NUMER_POZYCJI) TABLESPACE EUMOWY_I;
CREATE UNIQUE INDEX UN_ATTACHMENT_CONTENT_ATT_ID ON ATTACHMENT_CONTENT(ATTACHMENT_ID) TABLESPACE EUMOWY_I;
CREATE UNIQUE INDEX UN_CALCFIELD_NAME ON CALCFIELD("NAME") TABLESPACE EUMOWY_I;
CREATE UNIQUE INDEX UN_PANEL_NAME ON PANEL("NAME") TABLESPACE EUMOWY_I;
CREATE UNIQUE INDEX UN_POINT_DETAILS_POINT_ID ON POINT_DETAILS(POINT_ID) TABLESPACE EUMOWY_I;
CREATE UNIQUE INDEX UN_POS_DETAILS_POS_ID ON POS_DETAILS(POS_ID) TABLESPACE EUMOWY_I;
CREATE UNIQUE INDEX UN_SIGNATURE_NAME ON SIGNATURE("NAME") TABLESPACE EUMOWY_I;
CREATE UNIQUE INDEX UN_SIGNATURE_ADD_INF_SIGN_ID ON SIGNATURE(SIGNATURE_ID) TABLESPACE EUMOWY_I;
CREATE UNIQUE INDEX UN_DOCUMENT_CONTENT_DOC_ID ON DOCUMENT_CONTENT(DOCUMENT_ID) TABLESPACE EUMOWY_I;



-- SEQUENCES
create sequence EUMOWY.ACTIVITY_SEQ;
create sequence EUMOWY.ACTIVITY_SIGNATURES_SEQ;
create sequence EUMOWY.ADM_U_WEB_SEQ; -- TODO to raczej do wywalenia
create sequence EUMOWY.ATTACHMENT_CONTENT_SEQ;
create sequence EUMOWY.ATTACHMENT_SEQ;
create sequence EUMOWY.CALCFIELD_SEQ;
create sequence EUMOWY.CALCFIELD_SIGNATURE_SEQ;
create sequence EUMOWY.CLIENT_SEQ;
create sequence EUMOWY.DOCUMENT_SEQ;
create sequence EUMOWY.DOCUMENT_CONTENT_SEQ;
create sequence EUMOWY.EMAIL_TEMPLATES_SEQ;
create sequence EUMOWY.PANEL_SEQ;
create sequence EUMOWY.POINT_DETAILS_SEQ;
create sequence EUMOWY.POINT_SEQ;
create sequence EUMOWY.POS_DETAILS_SEQ;
create sequence EUMOWY.POS_SEQ;
create sequence EUMOWY.PROCESS_SEQ;
create sequence EUMOWY.PROCESS_DATA_SEQ;
create sequence EUMOWY.SIGNATURE_PANEL_SEQ;
create sequence EUMOWY.SIGNATURE_SEQ;
create sequence EUMOWY.SUBSCRIPTION_SEQ;
create sequence EUMOWY.APP_PARAMETERS_SEQ;
create sequence EUMOWY.SUBSCRIPTION_DEFINITION_SEQ;

-- TYPES
CREATE OR REPLACE TYPE "EUMOWY"."DZIALANIE" as table of varchar2(4000);
