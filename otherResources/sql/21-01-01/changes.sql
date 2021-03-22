--15. AP-AG/F/DF/2.005/18-07-01 -> AP-AG/F/DF/2.006/18-07-01

v_syg_source := 'AP-AG/F/DF/2.007/21-01-01';
v_syg_dest := 'AP-AG/F/DF/2.008/21-01-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APAGFDF2.00821-01-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 2, 230, 95, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 2, 405, 261, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 2, 405, 232, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 2, 405, 205, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 2, 405, 175, 59, 28);
