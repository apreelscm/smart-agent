declare

v_syg_source eumowy.signature.name%type;
v_syg_dest eumowy.signature.name%type;
v_ind eumowy.signature.id%type;

begin

v_syg_source := 'RODO/20-07-18';
v_syg_dest := 'RODO/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'KI_RODO24-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
   (SELECT max(id) + 1 FROM eumowy.subscription_definition),
   0,
   (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
   null,
   'ACCEPTANT1', 2, 210, 170, 59, 28
);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 2, 210, 130, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 2, 210, 95, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 2, 210, 55, 59, 28
       );

v_syg_source := 'AP/UW/UD/1.004/21-01-01';
v_syg_dest := 'AP/UW/UD/1.005/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUWUD1.00524-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 385, 225, 55, 24
       );

v_syg_source := 'AP/UW/RWT/1.005/21-01-01';
v_syg_dest := 'AP/UW/RWT/1.006/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUWRWT1.00624-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 380, 215, 55, 24
       );

v_syg_source := 'AP/UW/PON/1.004/21-01-01';
v_syg_dest := 'AP/UW/PON/1.005/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUWPON1.00524-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 380, 150, 55, 24
       );

v_syg_source := 'AP/UW/DED/1.002/21-01-01';
v_syg_dest := 'AP/UW/DED/1.003/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUWDED1.00324-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 3, 380, 235, 59, 28
       );

v_syg_source := 'AP/UW/AWU/1.000/21-01-01';
v_syg_dest := 'AP/UW/AWU/1.001/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUWAWU1.00124-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 1, 215, 360, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 1, 215, 340, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 1, 215, 320, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 1, 215, 300, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 375, 130, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1_1', 5, 200, 375, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2_1', 5, 200, 340, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3_1', 5, 200, 305, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4_1', 5, 200, 265, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH_1', 5, 375, 85, 59, 28
       );

v_syg_source := 'AP/UW/1.009/21-07-26';
v_syg_dest := 'AP/UW/1.010/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUW1.01024-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 4, 190, 375, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 4, 190, 340, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 4, 190, 305, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 4, 190, 265, 59, 28
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 4, 375, 85, 59, 28
       );

v_syg_source := 'AP/UW_OZWU/1.000/21-07-26';
v_syg_dest := 'AP/UW_OZWU/1.001/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUW_OZWU1.00124-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);

v_syg_source := 'AP/UPZT4/1.009/22-09-01';
v_syg_dest := 'AP/UPZT4/1.010/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT41.01024-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 5, 195, 245, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 5, 195, 225, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 5, 195, 205, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 5, 195, 185, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 5, 380, 45, 59, 28
       );

v_syg_source := 'AP/UPZT3/1.009/22-09-01';
v_syg_dest := 'AP/UPZT3/1.010/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT31.01024-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 5, 195, 245, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 5, 195, 225, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 5, 195, 205, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 5, 195, 185, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 5, 380, 45, 59, 28
       );

v_syg_source := 'AP/UPZT2/1.009/22-09-01';
v_syg_dest := 'AP/UPZT2/1.010/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT21.01024-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 5, 195, 245, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 5, 195, 225, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 5, 195, 205, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 5, 195, 185, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 5, 380, 45, 59, 28
       );

v_syg_source := 'AP/UPZT1/1.009/22-09-01';
v_syg_dest := 'AP/UPZT1/1.010/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT11.01024-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 5, 195, 245, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 5, 195, 225, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 5, 195, 205, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 5, 195, 185, 30, 14
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 5, 380, 45, 59, 28
       );

v_syg_source := 'AP/UPZ/ZSNT4/1.005/21-01-01';
v_syg_dest := 'AP/UPZ/ZSNT4/1.006/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT41.00624-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 175, 160, 59, 28
       );

v_syg_source := 'AP/UPZ/ZSNT3/1.005/21-01-01';
v_syg_dest := 'AP/UPZ/ZSNT3/1.006/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT31.00624-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 395, 125, 59, 28
       );

v_syg_source := 'AP/UPZ/ZSNT2/1.006/21-01-01';
v_syg_dest := 'AP/UPZ/ZSNT2/1.007/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT21.00724-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 250, 150, 59, 28
       );

v_syg_source := 'AP/UPZ/ZSNT1/1.006/21-01-01';
v_syg_dest := 'AP/UPZ/ZSNT1/1.007/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT11.00724-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 395, 125, 59, 28
       );

v_syg_source := 'AP/UPZ/RWP/1.003/21-01-01';
v_syg_dest := 'AP/UPZ/RWP/1.004/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZRWP1.00424-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 80, 460, 59, 28
       );

v_syg_source := 'AP/FDS/2.003/21-07-26';
v_syg_dest := 'AP/FDS/2.004/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APFDS2.00424-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);

v_syg_source := 'AP/FA/2.000/21-07-26';
v_syg_dest := 'AP/FA/2.001/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APFA2.00124-08-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);

v_syg_source := 'AP/FDP/2.000/21-07-26';
v_syg_dest := 'AP/FDP/2.003/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APFDP2.00324-08-01_bez_podpisu_Akceptanta.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);

v_syg_dest := 'virtualPoint';
update eumowy.signature set TEMPLATE_PATH = 'APFDP2.00324-08-01_bez_podpisu_Akceptanta.pdf' where name = v_syg_dest;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);

v_syg_source := 'AP/FDP/2.001/21-07-26';
v_syg_dest := 'AP/FDP/2.002/24-08-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APFDP2.00224-08-01_z_podpisem_Akceptanta.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT1', 1, 290, 145, 27, 12
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT2', 1, 290, 130, 27, 12
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT3', 1, 290, 110, 27, 12
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'ACCEPTANT4', 1, 290, 95, 27, 12
       );
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, FILE_NAME, ROLE, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES (
           (SELECT max(id) + 1 FROM eumowy.subscription_definition),
           0,
           (SELECT id FROM eumowy.signature WHERE name = v_syg_dest),
           null,
           'PH', 1, 170, 40, 59, 28
       );

end;