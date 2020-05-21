insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UW/1.007/20-02-28', 'APUW1.00720-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UW/1.006/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UW/1.006/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UW/1.007/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UW/1.006/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UPZT1/1.005/20-02-28', 'APUPZT11.00520-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UPZT1/1.004/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UPZT1/1.004/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UPZT1/1.005/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UPZT1/1.004/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UPZT2/1.005/20-02-28', 'APUPZT21.00520-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UPZT2/1.004/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UPZT2/1.004/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UPZT2/1.005/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UPZT2/1.004/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UPZT3/1.005/20-02-28', 'APUPZT31.00520-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UPZT3/1.004/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UPZT3/1.004/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UPZT3/1.005/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UPZT3/1.004/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 1, 1, 'AP/UPZT4/1.005/20-02-28', 'APUPZT41.00520-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UPZT4/1.004/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UPZT4/1.004/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UPZT4/1.005/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UPZT4/1.004/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UPZ/ZSNT1/1.005/20-02-28', 'APUPZZSNT11.00520-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UPZ/ZSNT1/1.004/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UPZ/ZSNT1/1.004/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UPZ/ZSNT1/1.005/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UPZ/ZSNT1/1.004/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UPZ/ZSNT2/1.005/20-02-28', 'APUPZZSNT21.00520-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UPZ/ZSNT2/1.004/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UPZ/ZSNT2/1.004/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UPZ/ZSNT2/1.005/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UPZ/ZSNT2/1.004/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UPZ/ZSNT3/1.004/20-02-28', 'APUPZZSNT31.00420-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UPZ/ZSNT3/1.003/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UPZ/ZSNT3/1.003/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UPZ/ZSNT3/1.004/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UPZ/ZSNT3/1.003/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UPZ/ZSNT4/1.00420-02-28', 'APUPZZSNT41.00420-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UPZ/ZSNT4/1.003/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UPZ/ZSNT4/1.003/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UPZ/ZSNT4/1.00420-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UPZ/ZSNT4/1.003/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UW/OOL/1.004/20-02-28', 'APUWOOL1.00420-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UW/OOL/1.003/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UW/OOL/1.003/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UW/OOL/1.004/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UW/OOL/1.003/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UW/PON/1.003/20-02-28', 'APUWPON1.00320-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UW/PON/1.002/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UW/PON/1.002/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UW/PON/1.003/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UW/PON/1.002/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UW/RWT/1.004/20-02-28', 'APUWRWT1.00420-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UW/RWT/1.003/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UW/RWT/1.003/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'new'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UW/RWT/1.003/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP/UW/UD/1.003/20-02-28', 'APUWUD1.00320-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UW/UD/1.002/18-07-20';
update eumowy.signature set active = 0 where name = 'AP/UW/UD/1.002/18-07-20';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'new'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UW/UD/1.002/18-07-20');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 1, 1, 'AP/UPZ/RWP/1.002/20-02-28', 'APUPZRWP1.00220-02-28.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP/UPZ/RWP/1.001/17-10-01';
update eumowy.signature set active = 0 where name = 'AP/UPZ/RWP/1.001/17-10-01';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP/UPZ/RWP/1.002/20-02-28'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP/UPZ/RWP/1.001/17-10-01');



insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UW/1.007/20-02-28'), 'PH', null, 2, 460, 80, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UW/1.007/20-02-28'), 'ACCEPTANT1', null, 2, 375, 287, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UW/1.007/20-02-28'), 'ACCEPTANT2', null, 2, 375, 247, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UW/1.007/20-02-28'), 'ACCEPTANT3', null, 2, 375, 207, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UW/1.007/20-02-28'), 'ACCEPTANT4', null, 2, 375, 167, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT1/1.005/20-02-28'), 'PH', null, 4, 185, 260, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT1/1.005/20-02-28'), 'ACCEPTANT1', null, 4, 185, 525, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT1/1.005/20-02-28'), 'ACCEPTANT2', null, 4, 185, 475, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT1/1.005/20-02-28'), 'ACCEPTANT3', null, 4, 185, 425, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT1/1.005/20-02-28'), 'ACCEPTANT4', null, 4, 185, 375, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT2/1.005/20-02-28'), 'PH', null, 4, 185, 285, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT2/1.005/20-02-28'), 'ACCEPTANT1', null, 4, 185, 515, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT2/1.005/20-02-28'), 'ACCEPTANT2', null, 4, 185, 472, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT2/1.005/20-02-28'), 'ACCEPTANT3', null, 4, 185, 429, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT2/1.005/20-02-28'), 'ACCEPTANT4', null, 4, 185, 387, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT3/1.005/20-02-28'), 'PH', null, 4, 185, 218, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT3/1.005/20-02-28'), 'ACCEPTANT1', null, 4, 185, 457, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT3/1.005/20-02-28'), 'ACCEPTANT2', null, 4, 185, 405, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT3/1.005/20-02-28'), 'ACCEPTANT3', null, 4, 185, 355, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT3/1.005/20-02-28'), 'ACCEPTANT4', null, 4, 185, 315, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT4/1.005/20-02-28'), 'PH', null, 4, 185, 260, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT4/1.005/20-02-28'), 'ACCEPTANT1', null, 4, 185, 525, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT4/1.005/20-02-28'), 'ACCEPTANT2', null, 4, 185, 475, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT4/1.005/20-02-28'), 'ACCEPTANT3', null, 4, 185, 425, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZT4/1.005/20-02-28'), 'ACCEPTANT4', null, 4, 185, 375, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZ/ZSNT1/1.005/20-02-28'), 'PH', null, 1, 476, 90, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZ/ZSNT2/1.005/20-02-28'), 'PH', null, 1, 260, 109, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZ/ZSNT3/1.004/20-02-28'), 'PH', null, 1, 480, 93, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZ/ZSNT4/1.00420-02-28'), 'PH', null, 1, 260, 89, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UW/OOL/1.004/20-02-28'), 'PH', null, 1, 285, 380, 55, 24);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UW/PON/1.003/20-02-28'), 'PH', null, 1, 275, 125, 55, 24);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UW/RWT/1.004/20-02-28'), 'PH', null, 1, 260, 160, 55, 24);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UW/UD/1.003/20-02-28'), 'PH', null, 1, 285, 175, 55, 24);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP/UPZ/RWP/1.002/20-02-28'), 'PH', null, 1, 275, 490, 59, 28);
