DECLARE

v_syg_source eumowy.signature.name%type;
v_syg_dest eumowy.signature.name%type;
v_ind eumowy.signature.id%type;

BEGIN

--1. AP/UW/1.006/18-07-20 - > AP/UW/1.007/20-02-28

v_syg_source := 'AP/UW/1.006/18-07-20';
v_syg_dest := 'AP/UW/1.007/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUW1.00720-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 2, 460, 80, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 2, 375, 287, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 2, 375, 247, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 2, 375, 207, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 2, 375, 167, 59, 28);

--2. AP/UPZT1/1.004/18-07-20 -> AP/UPZT1/1.005/20-02-28

v_syg_source := 'AP/UPZT1/1.004/18-07-20';
v_syg_dest := 'AP/UPZT1/1.005/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT11.00520-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 4, 185, 260, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 4, 185, 525, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 4, 185, 475, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 4, 185, 425, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 4, 185, 375, 59, 28);

--3. AP/UPZT2/1.004/18-07-20 -> AP/UPZT2/1.005/20-02-28

v_syg_source := 'AP/UPZT2/1.004/18-07-20';
v_syg_dest := 'AP/UPZT2/1.005/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT21.00520-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 4, 185, 285, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 4, 185, 515, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 4, 185, 472, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 4, 185, 429, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 4, 185, 387, 59, 28);

--4. AP/UPZT3/1.004/18-07-20 -> AP/UPZT3/1.005/20-02-28

v_syg_source := 'AP/UPZT3/1.004/18-07-20';
v_syg_dest := 'AP/UPZT3/1.005/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT31.00520-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 4, 185, 218, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 4, 185, 457, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 4, 185, 405, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 4, 185, 355, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 4, 185, 315, 59, 28);

--5. AP/UPZT4/1.004/18-07-20 -> AP/UPZT4/1.005/20-02-28

v_syg_source := 'AP/UPZT4/1.004/18-07-20';
v_syg_dest := 'AP/UPZT4/1.005/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT41.00520-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 4, 185, 260, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 4, 185, 525, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 4, 185, 475, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 4, 185, 425, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 4, 185, 375, 59, 28);

--6. AP/UPZ/ZSNT1/1.004/18-07-20 -> AP/UPZ/ZSNT1/1.005/20-02-28

v_syg_source := 'AP/UPZ/ZSNT1/1.004/18-07-20';
v_syg_dest := 'AP/UPZ/ZSNT1/1.005/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT11.00520-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 1, 476, 90, 59, 28);

--7. AP/UPZ/ZSNT2/1.004/18-07-20 -> AP/UPZ/ZSNT2/1.005/20-02-28

v_syg_source := 'AP/UPZ/ZSNT2/1.004/18-07-20';
v_syg_dest := 'AP/UPZ/ZSNT2/1.005/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT21.00520-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 1, 260, 109, 59, 28);

--8. AP/UPZ/ZSNT3/1.003/18-07-20 -> AP/UPZ/ZSNT3/1.004/20-02-28

v_syg_source := 'AP/UPZ/ZSNT3/1.003/18-07-20';
v_syg_dest := 'AP/UPZ/ZSNT3/1.004/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT31.00420-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 1, 480, 93, 59, 28);

--9. AP/UPZ/ZSNT4/1.003/18-07-20 -> AP/UPZ/ZSNT4/1.00420-02-28

v_syg_source := 'AP/UPZ/ZSNT4/1.003/18-07-20';
v_syg_dest := 'AP/UPZ/ZSNT4/1.00420-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT41.00420-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 1, 260, 89, 59, 28);

--10. AP/UW/OOL/1.003/18-07-20 -> AP/UW/OOL/1.004/20-02-28

v_syg_source := 'AP/UW/OOL/1.003/18-07-20';
v_syg_dest := 'AP/UW/OOL/1.004/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUWOOL1.00420-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 1, 285, 380, 55, 24);

--11. AP/UW/PON/1.002/18-07-20 -> AP/UW/PON/1.003/20-02-28

v_syg_source := 'AP/UW/PON/1.002/18-07-20';
v_syg_dest := 'AP/UW/PON/1.003/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUWPON1.00320-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 1, 275, 125, 55, 24);

--12. AP/UW/RWT/1.003/18-07-20 -> AP/UW/RWT/1.004/20-02-28

v_syg_source := 'AP/UW/RWT/1.003/18-07-20';
v_syg_dest := 'AP/UW/RWT/1.004/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUWRWT1.00420-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 1, 260, 160, 55, 24);

--13. AP/UW/UD/1.002/18-07-20 -> AP/UW/UD/1.003/20-02-28

v_syg_source := 'AP/UW/UD/1.002/18-07-20';
v_syg_dest := 'AP/UW/UD/1.003/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUWUD1.00320-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 1, 285, 175, 55, 24);

--14. AP/UPZ/RWP/1.001/17-10-01 - > AP/UPZ/RWP/1.002/20-02-28

v_syg_source := 'AP/UPZ/RWP/1.001/17-10-01';
v_syg_dest := 'AP/UPZ/RWP/1.002/20-02-28';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZRWP1.00220-02-28.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 1, 275, 490, 59, 28);

--15. AP-AG/F/DF/2.005/18-07-01 -> AP-AG/F/DF/2.006/18-07-01

v_syg_source := 'AP-AG/F/DF/2.005/18-07-01';
v_syg_dest := 'AP-AG/F/DF/2.006/18-07-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APAGFDF2.00618-07-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 2, 230, 125, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 2, 405, 296, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 2, 405, 267, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 2, 405, 239, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 2, 405, 210, 59, 28);

--16. AP-AG/F/DP/2.005/16-10-11 -> AP-AG/F/DP/2.006/16-10-11

v_syg_source := 'AP-AG/F/DP/2.005/16-10-11';
v_syg_dest := 'AP-AG/F/DP/2.006/16-10-11';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APAGFDP2.00616-10-11.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 1, 95, 45, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 1, 405, 144, 54, 24);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 1, 460, 125, 54, 24);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 1, 405, 104, 54, 24);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 1, 460, 85, 54, 24);

--17. AP-AG/F/PABR/1.001/18-07-01 - > AP-AG/F/PABR/1.002/18-07-01

v_syg_source := 'AP-AG/F/PABR/1.001/18-07-01';
v_syg_dest := 'AP-AG/F/PABR/1.002/18-07-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APAGFPABR1.00218-07-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH', null, 2, 440, 261, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'PH1', null, 2, 440, 103, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 2, 460, 485, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 2, 460, 455, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 2, 460, 425, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 2, 460, 395, 59, 28);

--18. AP-AG/F/PEP/1.001/18-07-01 - > AP-AG/F/PEP/1.002/18-07-01

v_syg_source := 'AP-AG/F/PEP/1.001/18-07-01';
v_syg_dest := 'AP-AG/F/PEP/1.002/18-07-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APAGFPEP1.00218-07-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 1, 445, 335, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 1, 500, 315, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 1, 445, 292, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 1, 500, 270, 59, 28);

--19. RODO/18-05-25 - > RODO/20-07-18

v_syg_source := 'RODO/18-05-25';
v_syg_dest := 'RODO/20-07-18';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'KI_RODO20-07-18.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
delete from EUMOWY.subscription_definition 
where signature_id = (select id from eumowy.signature where name = v_syg_dest);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT1', null, 2, 350, 357, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT2', null, 2, 350, 307, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT3', null, 2, 350, 258, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (select id from eumowy.signature where name = v_syg_dest), 'ACCEPTANT4', null, 2, 350, 209, 59, 28);

END;




ORA-00001: naruszono więzy unikatowe (EUMOWY.SYS_C0045847)
ORA-06512: przy "EUMOWY.EUM_HELPER", linia 110
ORA-06512: przy "EUMOWY.EUM_HELPER", linia 573
ORA-06512: przy linia 14