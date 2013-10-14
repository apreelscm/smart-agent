insert into EUMOWY.APP_PARAMETERS (id, name, value) values (4, 'SUBSCRIPTIONS_PATH_BLACKPREFIX', 'black_');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (7, 'MANAGEMENT_SUBSCRIPTION1_SCALE_X', '85');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (8, 'MANAGEMENT_SUBSCRIPTION1_SCALE_Y', '58');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (9, 'MANAGEMENT_SUBSCRIPTION2_SCALE_X', '56');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (10, 'MANAGEMENT_SUBSCRIPTION2_SCALE_Y', '59');

insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'NOTES_TO_COA', 'apreel.eUmowy@gmail.com', 'atest@apreel.com'); --coa@eservice.com.pl
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_PAPER_VERSION', '', 'atest@apreel.com'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_TEMPLATE_VERSION', '', 'atest@apreel.com'); --userEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_ELECTRONICAL_VERSION', '', 'atest@apreel.com'); --userEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_ACCEPTED', '', 'atest@apreel.com'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_REJECTED', '', 'atest@apreel.com'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_MISSING_MAIL', 'apreel.eUmowy@gmail.com', 'atest@apreel.com'); --coa@eservice.com.pl
