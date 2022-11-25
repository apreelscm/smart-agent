declare
v_syg_source varchar2(100);
v_syg_dest varchar2(100);
v_ind integer;

begin
--1. Z AP/PABR+PEP/1.004/21-07-26 -> AP/PABR+PEP/1.005/21-01-14

v_syg_dest := 'AP/PABR+PEP/1.004/21-07-26';
v_syg_source := 'AP/PABR+PEP/1.005/21-01-14';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APAGFPABRPEP1.00521-01-14.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition
where signature_id = (select id from eumowy.signature where name = v_syg_dest);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 2, 460, 328, 40, 22);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 2, 460, 185, 40, 22);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 2, 375, 400, 20, 22);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 2, 375, 412, 20, 22);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 2, 375, 424, 20, 22);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 2, 375, 446, 20, 22);

end;

commit;