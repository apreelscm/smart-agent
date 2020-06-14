insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP-AG/F/DF/2.006/18-07-01', 'APAGFDF2.00618-07-01.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP-AG/F/DF/2.005/18-07-01';
update eumowy.signature set active = 0 where name = 'AP-AG/F/DF/2.005/18-07-01';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP-AG/F/DF/2.006/18-07-01'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP-AG/F/DF/2.005/18-07-01');

insert into eumowy.calcfield_signature (ID, VERSION, CALC_FIELD_ID, SIGNATURE_ID)
select eumowy.calcfield_signature_seq.nextval, 0, calc_field_id, (select id from eumowy.signature where name = 'AP-AG/F/DF/2.006/18-07-01')
from eumowy.calcfield_signature where signature_id = (select id from eumowy.signature where name = 'AP-AG/F/DF/2.005/18-07-01');

insert into eumowy.signature_panel (ID, VERSION, PANEL_ID, SIGNATURE_ID)
select eumowy.signature_panel_seq.nextval, 0, panel_id, (select id from eumowy.signature where name = 'AP-AG/F/DF/2.006/18-07-01')
from eumowy.signature_panel where signature_id = (select id from eumowy.signature where name = 'AP-AG/F/DF/2.005/18-07-01');

insert into eumowy.signature_signature_detail (SIGNATURE_SIGNATURE_DETAILS_ID, SIGNATURE_DETAIL_ID)
select (select id from eumowy.signature where name = 'AP-AG/F/DF/2.006/18-07-01'), signature_detail_id
from eumowy.signature_signature_detail where SIGNATURE_SIGNATURE_DETAILS_ID = (select id from eumowy.signature where name = 'AP-AG/F/DF/2.005/18-07-01');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP-AG/F/DP/2.006/16-10-11', 'APAGFDP2.00616-10-11.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP-AG/F/DP/2.005/16-10-11';
update eumowy.signature set active = 0 where name = 'AP-AG/F/DP/2.005/16-10-11';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP-AG/F/DP/2.006/16-10-11'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP-AG/F/DP/2.005/16-10-11');

insert into eumowy.calcfield_signature (ID, VERSION, CALC_FIELD_ID, SIGNATURE_ID)
select eumowy.calcfield_signature_seq.nextval, 0, calc_field_id, (select id from eumowy.signature where name = 'AP-AG/F/DP/2.006/16-10-11')
from eumowy.calcfield_signature where signature_id = (select id from eumowy.signature where name = 'AP-AG/F/DP/2.005/16-10-11');

insert into eumowy.signature_panel (ID, VERSION, PANEL_ID, SIGNATURE_ID)
select eumowy.signature_panel_seq.nextval, 0, panel_id, (select id from eumowy.signature where name = 'AP-AG/F/DP/2.006/16-10-11')
from eumowy.signature_panel where signature_id = (select id from eumowy.signature where name = 'AP-AG/F/DP/2.005/16-10-11');

insert into eumowy.signature_signature_detail (SIGNATURE_SIGNATURE_DETAILS_ID, SIGNATURE_DETAIL_ID)
select (select id from eumowy.signature where name = 'AP-AG/F/DP/2.006/16-10-11'), signature_detail_id
from eumowy.signature_signature_detail where SIGNATURE_SIGNATURE_DETAILS_ID = (select id from eumowy.signature where name = 'AP-AG/F/DP/2.005/16-10-11');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP-AG/F/PABR/1.002/18-07-01', 'APAGFPABR1.00218-07-01.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP-AG/F/PABR/1.001/18-07-01';
update eumowy.signature set active = 0 where name = 'AP-AG/F/PABR/1.001/18-07-01';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.002/18-07-01'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.001/18-07-01');

insert into eumowy.calcfield_signature (ID, VERSION, CALC_FIELD_ID, SIGNATURE_ID)
select eumowy.calcfield_signature_seq.nextval, 0, calc_field_id, (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.002/18-07-01')
from eumowy.calcfield_signature where signature_id = (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.001/18-07-01');

insert into eumowy.signature_panel (ID, VERSION, PANEL_ID, SIGNATURE_ID)
select eumowy.signature_panel_seq.nextval, 0, panel_id, (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.002/18-07-01')
from eumowy.signature_panel where signature_id = (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.001/18-07-01');

insert into eumowy.signature_signature_detail (SIGNATURE_SIGNATURE_DETAILS_ID, SIGNATURE_DETAIL_ID)
select (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.002/18-07-01'), signature_detail_id
from eumowy.signature_signature_detail where SIGNATURE_SIGNATURE_DETAILS_ID = (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.001/18-07-01');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 0, 1, 'AP-AG/F/PEP/1.002/18-07-01', 'APAGFPEP1.00218-07-01.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'AP-AG/F/PEP/1.001/18-07-01';
update eumowy.signature set active = 0 where name = 'AP-AG/F/PEP/1.001/18-07-01';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.002/18-07-01'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.001/18-07-01');

insert into eumowy.calcfield_signature (ID, VERSION, CALC_FIELD_ID, SIGNATURE_ID)
select eumowy.calcfield_signature_seq.nextval, 0, calc_field_id, (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.002/18-07-01')
from eumowy.calcfield_signature where signature_id = (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.001/18-07-01');

insert into eumowy.signature_panel (ID, VERSION, PANEL_ID, SIGNATURE_ID)
select eumowy.signature_panel_seq.nextval, 0, panel_id, (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.002/18-07-01')
from eumowy.signature_panel where signature_id = (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.001/18-07-01');

insert into eumowy.signature_signature_detail (SIGNATURE_SIGNATURE_DETAILS_ID, SIGNATURE_DETAIL_ID)
select (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.002/18-07-01'), signature_detail_id
from eumowy.signature_signature_detail where SIGNATURE_SIGNATURE_DETAILS_ID = (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.001/18-07-01');

insert into eumowy.signature (ID, VERSION, ACTIVE, NAME, TEMPLATE_PATH, FOR_POINT, SIGNATURE_ORDER, DESCRIPTION, FILENAME, SEND_TO_CLIENT, SHOW_ON_PREVIEW, FOR_POS, SHOW_ON_ZRD)
select eumowy.signature_seq.nextval, 1, 1, 'RODO/20-07-18', 'KI_RODO20-07-18.pdf', os.FOR_POINT, os.SIGNATURE_ORDER, os.DESCRIPTION, os.FILENAME, os.SEND_TO_CLIENT, os.SHOW_ON_PREVIEW, os.FOR_POS, os.SHOW_ON_ZRD from eumowy.signature os where os.name = 'RODO/18-05-25';
update eumowy.signature set active = 0 where name = 'RODO/18-05-25';
insert into eumowy.activity_signatures (ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) SELECT eumowy.activity_signatures_seq.nextval, 0, oas.activity_id, oas.mandatory, oas.number_of_list, (select id from eumowy.signature where name = 'RODO/20-07-18'), oas.REQUIRED_ACTIVITIES FROM eumowy.activity_signatures oas WHERE oas.signature_id = (select id from eumowy.signature where name = 'RODO/18-05-25');

insert into eumowy.calcfield_signature (ID, VERSION, CALC_FIELD_ID, SIGNATURE_ID)
select eumowy.calcfield_signature_seq.nextval, 0, calc_field_id, (select id from eumowy.signature where name = 'RODO/20-07-18')
from eumowy.calcfield_signature where signature_id = (select id from eumowy.signature where name = 'RODO/18-05-25');

insert into eumowy.signature_panel (ID, VERSION, PANEL_ID, SIGNATURE_ID)
select eumowy.signature_panel_seq.nextval, 0, panel_id, (select id from eumowy.signature where name = 'RODO/20-07-18')
from eumowy.signature_panel where signature_id = (select id from eumowy.signature where name = 'RODO/18-05-25');

insert into eumowy.signature_signature_detail (SIGNATURE_SIGNATURE_DETAILS_ID, SIGNATURE_DETAIL_ID)
select (select id from eumowy.signature where name = 'RODO/20-07-18'), signature_detail_id
from eumowy.signature_signature_detail where SIGNATURE_SIGNATURE_DETAILS_ID = (select id from eumowy.signature where name = 'RODO/18-05-25');



insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/DF/2.006/18-07-01'), 'PH', null, 2, 230, 125, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/DF/2.006/18-07-01'), 'ACCEPTANT1', null, 2, 405, 296, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/DF/2.006/18-07-01'), 'ACCEPTANT2', null, 2, 405, 267, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/DF/2.006/18-07-01'), 'ACCEPTANT3', null, 2, 405, 239, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/DF/2.006/18-07-01'), 'ACCEPTANT4', null, 2, 405, 210, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/DP/2.006/16-10-11'), 'PH', null, 1, 95, 45, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/DP/2.006/16-10-11'), 'ACCEPTANT1', null, 1, 405, 144, 54, 24);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/DP/2.006/16-10-11'), 'ACCEPTANT2', null, 1, 460, 125, 54, 24);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/DP/2.006/16-10-11'), 'ACCEPTANT3', null, 1, 405, 104, 54, 24);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/DP/2.006/16-10-11'), 'ACCEPTANT4', null, 1, 460, 85, 54, 24);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.002/18-07-01'), 'PH', null, 2, 440, 261, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.002/18-07-01'), 'PH1', null, 2, 440, 103, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.002/18-07-01'), 'ACCEPTANT1', null, 2, 460, 485, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.002/18-07-01'), 'ACCEPTANT2', null, 2, 460, 455, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.002/18-07-01'), 'ACCEPTANT3', null, 2, 460, 425, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/PABR/1.002/18-07-01'), 'ACCEPTANT4', null, 2, 460, 395, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.002/18-07-01'), 'ACCEPTANT1', null, 1, 445, 335, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.002/18-07-01'), 'ACCEPTANT2', null, 1, 500, 315, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.002/18-07-01'), 'ACCEPTANT3', null, 1, 445, 292, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'AP-AG/F/PEP/1.002/18-07-01'), 'ACCEPTANT4', null, 1, 500, 270, 59, 28);

insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'RODO/20-07-18'), 'ACCEPTANT1', null, 2, 350, 357, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'RODO/20-07-18'), 'ACCEPTANT2', null, 2, 350, 307, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'RODO/20-07-18'), 'ACCEPTANT3', null, 2, 350, 258, 59, 28);
insert into EUMOWY.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
VALUES ((select max(id) + 1 from EUMOWY.subscription_definition), 0, (select id from eumowy.signature where name = 'RODO/20-07-18'), 'ACCEPTANT4', null, 2, 350, 209, 59, 28);