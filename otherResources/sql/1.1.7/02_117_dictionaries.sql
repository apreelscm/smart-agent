-- eUmowy_ext-410 nowa tresc maila dla procesow innych niz nowaUmowa
insert into eumowy.email_templates (id, version, name, recipient, sender) values (email_templates_seq.nextval, 0, 'DOCUMENTS_NOT_NEW_AGGREMENT_ELECTRONICAL_VERSION', 'sprzedaz@eservice.com.pl', 'sprzedaz@eservice.com.pl')
