insert into EUMOWY.APP_PARAMETERS (id, name, value) values (4, 'SUBSCRIPTIONS_PATH_BLACKPREFIX', 'black_');

insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'NOTES_TO_COA', 'apreel.eUmowy@gmail.com', 'atest@apreel.com'); --coa@eservice.com.pl
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_PAPER_VERSION', null, 'atest@apreel.com'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_TEMPLATE_VERSION', null, 'atest@apreel.com'); --userEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_ELECTRONICAL_VERSION', null, 'atest@apreel.com'); --userEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_ACCEPTED', null, 'atest@apreel.com'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_REJECTED', null , 'atest@apreel.com'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_MISSING_MAIL', 'apreel.eUmowy@gmail.com', 'atest@apreel.com'); --coa@eservice.com.pl

