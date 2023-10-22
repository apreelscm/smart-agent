--1. AP/UPZT1/1.007/21-07-26 - > AP/UPZT1/1.009/22-09-01

v_syg_source := 'AP/UPZT1/1.007/21-07-26';
v_syg_dest := 'AP/UPZT1/1.009/22-09-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT11.00922-09-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

delete from EUMOWY.subscription_definition
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 6, 380, 485, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 6, 300, 675, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 6, 300, 645, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 6, 300, 615, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 6, 300, 580, 59, 28);

--2. AP/UPZT2/1.007/21-07-26 - > AP/UPZT2/1.009/22-09-01

v_syg_source := 'AP/UPZT2/1.007/21-07-26';
v_syg_dest := 'AP/UPZT2/1.009/22-09-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT21.00922-09-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

delete from EUMOWY.subscription_definition
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 6, 380, 485, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 6, 300, 675, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 6, 300, 645, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 6, 300, 615, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 6, 300, 580, 59, 28);

--3. AP/UPZT3/1.007/21-07-26 - > AP/UPZT3/1.009/22-09-01

v_syg_source := 'AP/UPZT3/1.007/21-07-26';
v_syg_dest := 'AP/UPZT3/1.009/22-09-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT31.00922-09-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

delete from EUMOWY.subscription_definition
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 6, 380, 485, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 6, 300, 675, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 6, 300, 645, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 6, 300, 615, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 6, 300, 580, 59, 28);

--4. AP/UPZT4/1.007/21-07-26 - > AP/UPZT4/1.009/22-09-01

v_syg_source := 'AP/UPZT4/1.007/21-07-26';
v_syg_dest := 'AP/UPZT4/1.009/22-09-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT41.00922-09-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

delete from EUMOWY.subscription_definition
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 6, 380, 485, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 6, 300, 675, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 6, 300, 645, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 6, 300, 615, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 6, 300, 580, 59, 28);
