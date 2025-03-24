declare

v_syg_source eumowy.signature.name%type;
v_syg_dest eumowy.signature.name%type;
v_ind eumowy.signature.id%type;

begin

v_syg_source := 'RODO/24-08-01';
v_syg_dest := 'RODO/25-02-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'KI_RODO25-02-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);

------------------------------------------------------------------

v_syg_source := 'AP/UPZT1/1.010/24-08-01';
v_syg_dest := 'AP/UPZT1/1.011/25-03-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT11.01125-03-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 5, 195, 255, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 5, 195, 235, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 5, 195, 215, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 5, 195, 195, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 5, 380, 50, 59, 28
       );

--------------------------------------------------------------------------------------------------


v_syg_source := 'AP/UPZT2/1.010/24-08-01';
v_syg_dest := 'AP/UPZT2/1.011/25-03-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT21.01125-03-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 5, 195, 260, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 5, 195, 240, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 5, 195, 220, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 5, 195, 200, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 5, 380, 55, 59, 28
       );

----------------------------------------------------------------------------------------------------


v_syg_source := 'AP/UPZT3/1.010/24-08-01';
v_syg_dest := 'AP/UPZT3/1.011/25-03-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT31.01125-03-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 5, 195, 255, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 5, 195, 235, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 5, 195, 215, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 5, 195, 195, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 5, 380, 55, 59, 28
       );

----------------------------------------------------------------------------------------------------


v_syg_source := 'AP/UPZT4/1.010/24-08-01';
v_syg_dest := 'AP/UPZT4/1.011/25-03-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT41.01125-03-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 5, 195, 260, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 5, 195, 240, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 5, 195, 220, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 5, 195, 200, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 5, 380, 55, 59, 28
       );

-----------------------------------------------------------------------------------------------------

v_syg_source := 'AP/UW/1.010/24-08-01';
v_syg_dest := 'AP/UW/1.011/25-03-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUW1.01125-03-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 4, 190, 360, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 4, 190, 325, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 4, 190, 290, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 4, 190, 250, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 4, 375, 85, 59, 28
       );


-------------------------------------------------------------------------------------------------

v_syg_dest := 'AP/UPZ/ZSNT2/1.007/24-08-01';
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT21.00724-08-01_final.pdf' where name = v_syg_dest;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 385, 150, 59, 28
       );

-------------------------------------------------------------------------------------------------

v_syg_dest := 'AP/UPZ/ZSNT4/1.006/24-08-01';
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT41.00624-08-01_final.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 385, 160, 59, 28
       );

-------------------------------------------------------------------------------------------------

v_syg_dest := 'AP/PABR+PEP/1.005/21-01-14';
v_syg_source := 'PABR_PEP/25.03/03';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'PABR_PEP25.0303.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition
where signature_id = (select id from eumowy.signature where name = v_syg_dest);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 2, 465, 435, 40, 22);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 2, 115, 435, 40, 22);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 2, 375, 570, 20, 22);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 2, 375, 590, 20, 22);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 2, 375, 610, 20, 22);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 2, 375, 630, 20, 22);

------------------------------------------------------------------------------------------------------

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD, SHOULD_BE_MERGED)
values (eumowy.signature_seq.nextval, 0, 1, 'RODO/PKOBP/25-02-01', 'Kl_RODO_PKOBP25-02-01.pdf', 0, (select max(signature_order) + 1 from eumowy.signature), 'Klauzula Informacyjna RODO PKOBP', 'Klauzula_RODO_PKOBP.pdf', 1, 1, 0, 1, 1);

insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES)
VALUES (eumowy.activity_signatures_seq.nextval, 0, 1, 1, 0, (select id from eumowy.signature where name = 'RODO/PKOBP/25-02-01'), null);

end;
