-- Panel 'Kategoria Ryzyka Klienta'
INSERT INTO EUMOWY.PANEL (ID, VERSION, NAME, ORDER_NO) VALUES(EUMOWY.PANEL_SEQ.nextval, 0, 'kategoriaRyzykaKlienta', 245);

INSERT INTO EUMOWY.SIGNATURE_PANEL(ID, VERSION, PANEL_ID, SIGNATURE_ID)
VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL WHERE NAME = 'kategoriaRyzykaKlienta'), (SELECT ID FROM EUMOWY.SIGNATURE WHERE NAME = 'virtualNowaUmowa'));

ALTER TABLE EUMOWY.POINT_DETAILS ADD risk VARCHAR2(20);
--

-- Panel 'Oświadczenie żądania rozpoczęcia wykonania usługi'
INSERT INTO EUMOWY.PANEL (ID, VERSION, NAME, ORDER_NO) VALUES(EUMOWY.PANEL_SEQ.nextval, 0, 'oswiadczenieZadaniaRozpoczeciaWykonaniaUslugi', 485);

INSERT INTO EUMOWY.SIGNATURE_PANEL(ID, VERSION, PANEL_ID, SIGNATURE_ID)
VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL WHERE NAME = 'oswiadczenieZadaniaRozpoczeciaWykonaniaUslugi'), (SELECT ID FROM EUMOWY.SIGNATURE WHERE NAME = 'virtualNowaUmowa'));
--

-- Ukrycie działania „Odnowienie lojalności”;
UPDATE eumowy.activity SET active = 0 WHERE code = 'odnowienieLojalnosci';
UPDATE eumowy.signature SET active = 0 WHERE name = 'AP/UW/OOL/1.004/20-02-28';
--

-- AP-AG/F/DF/2.006/18-07-01 -> AP-AG/F/DF/2.007/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/DF/2.007/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/DF/2.007/21-01-01'), 'ACCEPTANT1', null, 2, 405, 281, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/DF/2.007/21-01-01'), 'ACCEPTANT2', null, 2, 405, 252, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/DF/2.007/21-01-01'), 'ACCEPTANT3', null, 2, 405, 225, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/DF/2.007/21-01-01'), 'ACCEPTANT4', null, 2, 405, 195, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/DF/2.007/21-01-01'), 'PH', null, 2, 230, 115, 59, 28);
--

-- AP/UPZ/RWP/1.002/20-02-28 -> AP/UPZ/RWP/1.003/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/RWP/1.003/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/RWP/1.003/21-01-01'), 'PH', null, 1, 390, 382, 59, 28);
--

-- AP/UPZT1/1.005/20-02-28 -> AP/UPZT1/1.006/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT1/1.006/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT1/1.006/21-01-01'), 'ACCEPTANT1', null, 5, 185, 610, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT1/1.006/21-01-01'), 'ACCEPTANT2', null, 5, 185, 560, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT1/1.006/21-01-01'), 'ACCEPTANT3', null, 5, 185, 510, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT1/1.006/21-01-01'), 'ACCEPTANT4', null, 5, 185, 460, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT1/1.006/21-01-01'), 'PH', null, 5, 390, 355, 59, 28);
--

-- AP/UPZT2/1.005/20-02-28 -> AP/UPZT2/1.006/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT2/1.006/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT2/1.006/21-01-01'), 'ACCEPTANT1', null, 5, 185, 610, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT2/1.006/21-01-01'), 'ACCEPTANT2', null, 5, 185, 560, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT2/1.006/21-01-01'), 'ACCEPTANT3', null, 5, 185, 510, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT2/1.006/21-01-01'), 'ACCEPTANT4', null, 5, 185, 460, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT2/1.006/21-01-01'), 'PH', null, 5, 390, 345, 59, 28);
--

-- AP/UPZT3/1.005/20-02-28 -> AP/UPZT3/1.006/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT3/1.006/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT3/1.006/21-01-01'), 'ACCEPTANT1', null, 5, 185, 610, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT3/1.006/21-01-01'), 'ACCEPTANT2', null, 5, 185, 560, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT3/1.006/21-01-01'), 'ACCEPTANT3', null, 5, 185, 510, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT3/1.006/21-01-01'), 'ACCEPTANT4', null, 5, 185, 460, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT3/1.006/21-01-01'), 'PH', null, 5, 390, 345, 59, 28);
--

-- AP/UPZT4/1.005/20-02-28 -> AP/UPZT4/1.006/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT4/1.006/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT4/1.006/21-01-01'), 'ACCEPTANT1', null, 5, 185, 610, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT4/1.006/21-01-01'), 'ACCEPTANT2', null, 5, 185, 560, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT4/1.006/21-01-01'), 'ACCEPTANT3', null, 5, 185, 510, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT4/1.006/21-01-01'), 'ACCEPTANT4', null, 5, 185, 460, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZT4/1.006/21-01-01'), 'PH', null, 5, 390, 335, 59, 28);
--

-- AP/UPZ/ZSNT1/1.005/20-02-28 -> AP/UPZ/ZSNT1/1.006/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/ZSNT1/1.006/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/ZSNT1/1.006/21-01-01'), 'PH', null, 1, 460, 92, 59, 28);
--

-- AP/UPZ/ZSNT2/1.005/20-02-28 -> AP/UPZ/ZSNT2/1.006/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/ZSNT2/1.006/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/ZSNT2/1.006/21-01-01'), 'PH', null, 1, 200, 115, 59, 28);
--

-- AP/UPZ/ZSNT3/1.006/21-01-01;
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/ZSNT3/1.006/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/ZSNT3/1.006/21-01-01'), 'PH', null, 1, 460, 92, 59, 28);
--

-- AP/UPZ/ZSNT4/1.006/21-01-01;
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/ZSNT4/1.006/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UPZ/ZSNT4/1.006/21-01-01'), 'PH', null, 1, 200, 117, 59, 28);
--

-- AP/UW/1.007/20-02-28 -> AP/UW/1.008/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01'), 'ACCEPTANT1', null, 4, 135, 585, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01'), 'ACCEPTANT2', null, 4, 135, 535, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01'), 'ACCEPTANT3', null, 4, 135, 490, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01'), 'ACCEPTANT4', null, 4, 135, 440, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01'), 'ACCEPTANT1_APUW_ZAL4', null, 6, 110, 330, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01'), 'ACCEPTANT2_APUW_ZAL4', null, 6, 110, 280, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01'), 'ACCEPTANT3_APUW_ZAL4', null, 6, 110, 230, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01'), 'ACCEPTANT4_APUW_ZAL4', null, 6, 110, 180, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01'), 'PH', null, 4, 375, 288, 59, 28);
--

-- AP/UW/AWU/1.000/21-01-01
DELETE FROM eumowy.activity_signatures WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01') AND activity_id = (SELECT id FROM eumowy.activity WHERE code = 'nowaUmowa');
DELETE FROM eumowy.activity_signatures WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/1.008/21-01-01') AND activity_id = (SELECT id FROM eumowy.activity WHERE code = 'wymianaUmowyNajmu');

UPDATE eumowy.signature SET description = 'Aneks do wymiany Umowy Współpracy', filename = 'Aneks do wymiany Umowy Współpracy.pdf' WHERE name = 'AP/UW/AWU/1.000/21-01-01';

DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01'), 'ACCEPTANT1', null, 1, 185, 425, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01'), 'ACCEPTANT2', null, 1, 185, 372, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01'), 'ACCEPTANT3', null, 1, 185, 322, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01'), 'ACCEPTANT4', null, 1, 185, 270, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01'), 'PH', null, 1, 375, 150, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01'), 'ACCEPTANT1_1', null, 5, 150, 605, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01'), 'ACCEPTANT2_1', null, 5, 150, 562, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01'), 'ACCEPTANT3_1', null, 5, 150, 513, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01'), 'ACCEPTANT4_1', null, 5, 150, 465, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/AWU/1.000/21-01-01'), 'PH_1', null, 5, 375, 310, 59, 28);
--

-- AP/UW/DED/1.001/17-10-01 -> AP/UW/DED/1.002/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/DED/1.002/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/DED/1.002/21-01-01'), 'PH', null, 3, 370, 270, 59, 28);
--

-- AP/UW/PON/1.003/20-02-28 -> AP/UW/PON/1.004/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/PON/1.004/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/PON/1.004/21-01-01'), 'PH', null, 1, 277, 126, 55, 24);
--

-- AP/UW/RWT/1.004/20-02-28 -> AP/UW/RWT/1.005/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/RWT/1.005/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/RWT/1.005/21-01-01'), 'PH', null, 1, 380, 265, 55, 24);
--

-- AP/UW/UD/1.003/20-02-28 -> AP/UW/UD/1.004/21-01-01
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/UD/1.004/21-01-01');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/UW/UD/1.004/21-01-01'), 'PH', null, 1, 285, 225, 55, 24);
--

UPDATE eumowy.subscription_definition SET role = 'PH_1' WHERE role = 'PH1';

DELETE FROM eumowy.activity_signatures WHERE activity_id = (SELECT id FROM eumowy.activity WHERE code = 'dodaniePrepaid');
INSERT INTO eumowy.activity_signatures(ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES)
VALUES (EUMOWY.ACTIVITY_SIGNATURES_SEQ.nextval, 0, (SELECT id FROM eumowy.activity WHERE code = 'dodaniePrepaid'), 0, 1, (SELECT id FROM EUMOWY.SIGNATURE WHERE name = 'AP/UW/DED/1.002/21-01-01'), null);