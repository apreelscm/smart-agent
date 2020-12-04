-- AP-AG/F/PABR/1.003/20-11-09
DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/PABR/1.003/20-11-09');

INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/PABR/1.003/20-11-09'), 'ACCEPTANT1', null, 2, 460, 477, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/PABR/1.003/20-11-09'), 'ACCEPTANT2', null, 2, 460, 447, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/PABR/1.003/20-11-09'), 'ACCEPTANT3', null, 2, 460, 417, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/PABR/1.003/20-11-09'), 'ACCEPTANT4', null, 2, 460, 387, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/PABR/1.003/20-11-09'), 'PH', null, 2, 440, 253, 59, 28);
INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((SELECT max(id) + 1 FROM eumowy.subscription_definition), 0, (SELECT id FROM eumowy.signature WHERE name = 'AP-AG/F/PABR/1.003/20-11-09'), 'PH1', null, 2, 440, 85, 59, 28);
--