insert into EUMOWY.APP_PARAMETERS (id, name, value) values (4, 'SUBSCRIPTIONS_PATH_BLACKPREFIX', 'black_');

insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'NOTES_TO_COA', 'justyna.bochacz@eservice.com.pl', 'justyna.bochacz@eservice.com.pl');
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_PAPER_VERSION', null , 'justyna.bochacz@eservice.com.pl');
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_TEMPLATE_VERSION', null, 'justyna.bochacz@eservice.com.pl'); --userEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_ELECTRONICAL_VERSION', null, 'justyna.bochacz@eservice.com.pl'); --userEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_ACCEPTED', null, 'justyna.bochacz@eservice.com.pl'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_REJECTED', null, 'justyna.bochacz@eservice.com.pl'); --phEmail
insert into EUMOWY.EMAIL_TEMPLATES (id, version, name, recipient, sender) values (EUMOWY.EMAIL_TEMPLATES_SEQ.NEXTVAL, 0, 'DOCUMENTS_MISSING_MAIL', 'justyna.bochacz@eservice.com.pl', 'justyna.bochacz@eservice.com.pl');