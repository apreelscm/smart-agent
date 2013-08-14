-- CREATE TABLES
create table EUMOWY.ACTIVITY (id number(19,0) not null, version number(19,0) not null, code varchar2(255 char) unique, numer_pozycji number(10,0) unique, primary key (id));
create table EUMOWY.ACTIVITY_SIGNATURES (id number(19,0) not null, version number(19,0) not null, activity_id number(19,0), mandatory number(1,0) not null, number_of_list number(10,0), signature_id number(19,0), primary key (id));
create table EUMOWY.ATTACHMENT (id number(19,0) not null, date_created timestamp not null, filename varchar2(255 char), last_updated timestamp not null, process_id number(19,0), attachments_idx number(10,0), primary key (id));
create table EUMOWY.CALCFIELD (id number(19,0) not null, version number(19,0) not null, name varchar2(255 char) unique, primary key (id));
create table EUMOWY.CALCFIELD_SIGNATURE (id number(19,0) not null, version number(19,0) not null, calc_field_id number(19,0), signature_id number(19,0), primary key (id));
create table EUMOWY.CLIENT (id number(19,0) not null, version number(19,0) not null, cbd_id varchar2(255 char) unique, name varchar2(255 char) unique, nip varchar2(255 char) unique, primary key (id));
create table EUMOWY.DOCUMENT (id number(19,0) not null, date_created timestamp not null, filename varchar2(255 char), last_updated timestamp not null, process_id number(19,0), documents_idx number(10,0), primary key (id));
create table EUMOWY.EMAIL_TEMPLATES (id number(19,0) not null, version number(19,0) not null, name varchar2(255 char), recipent varchar2(255 char), sender varchar2(255 char), primary key (id));
create table EUMOWY.PANEL (id number(19,0) not null, version number(19,0) not null, name varchar2(255 char) unique, order_no number(10,0), primary key (id));
create table EUMOWY.PROCESS (id number(19,0) not null, version number(19,0) not null, calc_number varchar2(255 char), client_id number(19,0), date_created timestamp not null, last_updated timestamp not null, ph_first_name varchar2(255 char), ph_number varchar2(255 char), ph_surname varchar2(255 char), sale_section varchar2(255 char), status varchar2(255 char), primary key (id));
create table EUMOWY.SIGNATURE (id number(19,0) not null, version number(19,0) not null, active number(1,0), name varchar2(255 char) unique, primary key (id));
create table EUMOWY.SIGNATURE_PANEL (id number(19,0) not null, version number(19,0) not null, panel_id number(19,0), signature_id number(19,0), primary key (id));
create table EUMOWY.SUBSCRIPTION (id number(19,0) not null, version number(19,0) not null, content long, primary key (id));
create table EUMOWY.process_activity (process_activities_id number(19,0), activity_id number(19,0), activities_idx number(10,0));
create table EUMOWY.process_signature (process_signatures_id number(19,0), signature_id number(19,0), signatures_idx number(10,0));
create table EUMOWY.process_subscription (process_subscriptions_id number(19,0), subscription_id number(19,0), subscriptions_idx number(10,0));
create table sec_role (id number(19,0) not null, version number(19,0) not null, authority varchar2(255 char) unique, primary key (id));
create table sec_user (id number(19,0) not null, version number(19,0) not null, account_expired number(1,0) not null, account_locked number(1,0) not null, enabled number(1,0) not null, name varchar2(255 char), "password" varchar2(255 char), password_expired number(1,0) not null, username varchar2(255 char) unique, primary key (id));
create table sec_user_sec_role (sec_role_id number(19,0), sec_user_id number(19,0), primary key (sec_role_id, sec_user_id));

-- CONSTRAINTS
-- TODO do poprawy nazwy FK
alter table EUMOWY.ACTIVITY_SIGNATURES add constraint FK19F8ADEBC1A12403 foreign key (activity_id) references EUMOWY.ACTIVITY;
alter table EUMOWY.ACTIVITY_SIGNATURES add constraint FK19F8ADEBED846B31 foreign key (signature_id) references EUMOWY.SIGNATURE;
alter table EUMOWY.ATTACHMENT add constraint FKA7E14523C7962D91 foreign key (process_id) references EUMOWY.PROCESS;
alter table EUMOWY.CALCFIELD_SIGNATURE add constraint FK5E9E7C7EF3FEB546 foreign key (calc_field_id) references EUMOWY.CALCFIELD;
alter table EUMOWY.CALCFIELD_SIGNATURE add constraint FK5E9E7C7EED846B31 foreign key (signature_id) references EUMOWY.SIGNATURE;
alter table EUMOWY.DOCUMENT add constraint FK6202C11BC7962D91 foreign key (process_id) references EUMOWY.PROCESS;
alter table EUMOWY.PROCESS add constraint FK1858AA4FD030B0C3 foreign key (client_id) references EUMOWY.CLIENT;
alter table EUMOWY.SIGNATURE_PANEL add constraint FK688D4B7D84182B31 foreign key (panel_id) references EUMOWY.PANEL;
alter table EUMOWY.SIGNATURE_PANEL add constraint FK688D4B7DED846B31 foreign key (signature_id) references EUMOWY.SIGNATURE;
alter table EUMOWY.process_activity add constraint FKD3BAD0D9C1A12403 foreign key (activity_id) references EUMOWY.ACTIVITY;
alter table EUMOWY.process_signature add constraint FKD76A742EED846B31 foreign key (signature_id) references EUMOWY.SIGNATURE;
alter table EUMOWY.process_subscription add constraint FK992D41E74E3344C3 foreign key (subscription_id) references EUMOWY.SUBSCRIPTION;
alter table EUMOWY.SEC_USER_SEC_ROLE add constraint FKC78D322AA9AFAA0F foreign key (sec_user_id) references EUMOWY.SEC_USER;
alter table EUMOWY.SEC_USER_SEC_ROLE add constraint FKC78D322A484E62F foreign key (sec_role_id) references EUMOWY.SEC_ROLE;

-- SEQUENCES
create sequence ACTIVITY_SEQ;
create sequence ACTIVITY_SIGNATURES_SEQ;
create sequence ADM_U_WEB_SEQ;
create sequence ATTACHMENT_CONTENT_SEQ;
create sequence ATTACHMENT_SEQ;
create sequence CALCFIELD_SEQ;
create sequence CALCFIELD_SIGNATURE_SEQ;
create sequence CLIENT_SEQ;
create sequence DOCUMENT_SEQ;
create sequence EMAIL_TEMPLATES_SEQ;
create sequence PANEL_SEQ;
create sequence PROCESS_SEQ;
create sequence SIGNATURE_PANEL_SEQ;
create sequence SIGNATURE_SEQ;
create sequence SUBSCRIPTION_SEQ;