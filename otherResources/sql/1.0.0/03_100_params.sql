insert into EUMOWY.APP_PARAMETERS (id, name, value) values (0, 'TEMP_PDFIMAGE_STORAGE_PATH', '');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (1, 'TEMP_PDFIMAGE_STORAGE_URI', '');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (2, 'PDF_TEMPLATE_PATH', '');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (3, 'SUBSCRIPTIONS_PATH', '');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (4, 'SUBSCRIPTIONS_PATH_BLACKPREFIX', 'black_');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (5, 'TEMPLATE_NAME_FOR_NEW_POINT', '');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (6, 'TEMPLATE_NAME_FOR_NEW_POS', '');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (7, 'MANAGEMENT_SUBSCRIPTION1_SCALE_X', '85');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (8, 'MANAGEMENT_SUBSCRIPTION1_SCALE_Y', '58');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (9, 'MANAGEMENT_SUBSCRIPTION2_SCALE_X', '56');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (10, 'MANAGEMENT_SUBSCRIPTION2_SCALE_Y', '59');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (11, 'FONT_URI', '');
insert into EUMOWY.APP_PARAMETERS (id, name, value) values (12, 'TEMP_PDFPREVIEW_STORAGE_PATH', '');

insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'NOTES_TO_COA', 'apreel.eUmowy@gmail.com', 'atest@apreel.com'); --coa@eservice.com.pl
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_PAPER_VERSION', '', 'atest@apreel.com'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_TEMPLATE_VERSION', '', 'atest@apreel.com'); --userEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_ELECTRONICAL_VERSION', '', 'atest@apreel.com'); --userEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_ACCEPTED', '', 'atest@apreel.com'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_REJECTED', '', 'atest@apreel.com'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_MISSING_MAIL', 'apreel.eUmowy@gmail.com', 'atest@apreel.com'); --coa@eservice.com.pl
