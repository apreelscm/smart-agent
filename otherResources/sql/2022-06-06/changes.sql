create sequence EUMOWY.REPRESENTATIVE_CBD_SEQ;

ALTER TABLE EUMOWY.REPRESENTATIVE
    ADD IS_CBD_DATA_CHANGED_MANUALLY number(1,0);

create table EUMOWY.REPRESENTATIVE_CBD_DATA
(
    ID                 number(12,0) not null,
    SALUTATION         varchar2(255),
    NAME               varchar2(255),
    SURNAME            varchar2(255),
    POSITION           varchar2(255),
    COUNTRY_CODE       varchar2(255),
    PESEL              varchar2(255),
    BIRTH_COUNTRY      varchar2(255),
    BIRTH_DATE         timestamp,
    ID_NUMBER          varchar2(255),
    ID_ISSUE_DATE      timestamp,
    ID_EXPIRATION_DATE timestamp,
    CITIZENSHIP        varchar2(255),
    STREET             varchar2(40),
    STREET_TITLE       varchar2(4),
    HOUSE_NUMBER       varchar2(6),
    FLAT_NUMBER        varchar2(4),
    CITY               varchar2(33),
    POSTAL_CODE        VARCHAR2(6),
    POST_OFFICE        VARCHAR2(33),
    COUNTRY            VARCHAR2(255),
    EMAIL              VARCHAR2(80),
    LANDLINE_PHONE     VARCHAR2(20),
    MOBILE_PHONE       VARCHAR2(20),
    CONTRACT_SIGNED    number(1,0),
    constraint PK_REPRESENTATIVE_CBD_DATA primary key (ID)
);

grant select, insert, update, delete on REPRESENTATIVE_CBD_DATA to EUMOWY_APP;

ALTER TABLE EUMOWY.REPRESENTATIVE_CBD_DATA
    ADD REPRESENTATIVE_ID number(19,0);


alter table EUMOWY.REPRESENTATIVE_CBD_DATA
    add constraint EUMOWY.REPRESENTATIVE_CBD_DATA_REPRESENTATIVE_ID_FK foreign key (REPRESENTATIVE_ID) references EUMOWY.REPRESENTATIVE;
