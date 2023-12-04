INSERT INTO eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, SHOW_ON_ZRD)
VALUES (eumowy.signature_seq.nextval, 0, 1, 'AP/FDP/2.001/21-07-26', 'APFDP2.00121-07-26.pdf', true, 3, 'Formularz Danych Punktu', 'Formularz Danych Punktu.pdf', true, true, true);

INSERT INTO eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID)
VALUES (eumowy.activity_signatures_seq.nextval, 0, (SELECT id FROM eumowy.activity WHERE code = 'dodatkowyPunkt'), 1, 0, (SELECT id FROM eumowy.signature WHERE name = 'AP/FDP/2.001/21-07-26'));

INSERT INTO eumowy.signature_panel (ID, VERSION, PANEL_ID, SIGNATURE_ID)
VALUES (eumowy.signature_panel_seq.nextval, 0, (SELECT id FROM eumowy.panel WHERE name = 'danePunktu'), (SELECT id FROM eumowy.signature WHERE name = 'AP/FDP/2.001/21-07-26'));

INSERT INTO eumowy.signature_panel (ID, VERSION, PANEL_ID, SIGNATURE_ID)
VALUES (eumowy.signature_panel_seq.nextval, 0, (SELECT id FROM eumowy.panel WHERE name = 'dodajPunkt'), (SELECT id FROM eumowy.signature WHERE name = 'AP/FDP/2.001/21-07-26'));

INSERT INTO eumowy.signature_signature_detail (SIGNATURE_SIGNATURE_DETAILS_ID, SIGNATURE_DETAIL_ID)
VALUES ((SELECT id FROM eumowy.signature WHERE name = 'AP/FDP/2.001/21-07-26'), (SELECT id FROM eumowy.signature_detail WHERE typ = 'POINT'));

INSERT INTO eumowy.signature_additional_info(ID, SIGNATURE_ID, DATE_CALC_METHOD, TRANSFER_DAYS, TYPE, CZY_UMOWA_VAT_MSC, CZY_UPUST_DCC, CZY_OPLATA_KALK, CZY_OPLATA_GPRS)
VALUES (eumowy.signature_additional_info_seq.nextval,(SELECT id FROM eumowy.signature WHERE name = 'AP/FDP/2.001/21-07-26'),0,0,'F','N','N','T','N');

DELETE FROM eumowy.activity_signatures
WHERE activity_id = (SELECT id FROM eumowy.activity WHERE code = 'dodatkowyPunkt')
  AND signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP/FDP/2.000/21-07-26');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) 
VALUES (eumowy.subscription_definition_seq.nextval, 0, (SELECT id FROM EUMOWY.SIGNATURE WHERE template_path = 'APFDP2.00121-07-26.pdf'), 'PH', NULL, 1, 440, 103, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) 
VALUES (eumowy.subscription_definition_seq.nextval, 0, (SELECT id FROM EUMOWY.SIGNATURE WHERE template_path = 'APFDP2.00121-07-26.pdf'), 'ACCEPTANT1', NULL, 1, 405, 225, 54, 24);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) 
VALUES (eumowy.subscription_definition_seq.nextval, 0, (SELECT id FROM EUMOWY.SIGNATURE WHERE template_path = 'APFDP2.00121-07-26.pdf'), 'ACCEPTANT2', NULL, 1, 460, 210, 54, 24);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) 
VALUES (eumowy.subscription_definition_seq.nextval, 0, (SELECT id FROM EUMOWY.SIGNATURE WHERE template_path = 'APFDP2.00121-07-26.pdf'), 'ACCEPTANT3', NULL, 1, 405, 187, 54, 24);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) 
VALUES (eumowy.subscription_definition_seq.nextval, 0, (SELECT id FROM EUMOWY.SIGNATURE WHERE template_path = 'APFDP2.00121-07-26.pdf'), 'ACCEPTANT4', NULL, 1, 460, 165, 54, 24);
