declare
v_syg_source varchar2(100);
v_syg_dest varchar2(100);
v_ind integer;

--2. Z AP/UW/1.008/21-01-01 -> AP/UW/1.009/21-07-26
begin
v_syg_source := 'AP/UW/1.008/21-01-01';
v_syg_dest := 'AP/UW/1.009/21-07-26';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'AP/UW/1.009/21-07-26.pdf', show_on_preview = 1, should_be_merged = 1 where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

--3. Z AP/UPZT1/1.006/21-01-01 -> AP/UPZT1/1.007/21-07-26

v_syg_source := 'AP/UPZT1/1.006/21-01-01';
v_syg_dest := 'AP/UPZT1/1.007/21-07-26';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'AP/UPZT1/1.007/21-07-26.pdf', show_on_preview = 1, should_be_merged = 1 where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

--4. Z AP/UPZT2/1.006/21-01-01 -> AP/UPZT2/1.007/21-07-26

v_syg_source := 'AP/UPZT2/1.006/21-01-01';
v_syg_dest := 'AP/UPZT2/1.007/21-07-26';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'AP/UPZT2/1.007/21-07-26.pdf', show_on_preview = 1, should_be_merged = 1 where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;


--5. Z AP/UPZT3/1.006/21-01-01 -> AP/UPZT3/1.007/21-07-26

v_syg_source := 'AP/UPZT3/1.006/21-01-01';
v_syg_dest := 'AP/UPZT3/1.007/21-07-26';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'AP/UPZT3/1.007/21-07-26.pdf', show_on_preview = 1, should_be_merged = 1 where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

--6. Z AP/UPZT4/1.006/21-01-01 -> AP/UPZT4/1.007/21-07-26

v_syg_source := 'AP/UPZT4/1.006/21-01-01';
v_syg_dest := 'AP/UPZT4/1.007/21-07-26';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'AP/UPZT4/1.007/21-07-26.pdf', show_on_preview = 1, should_be_merged = 1 where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

--7. Z AP-AG/F/DF/2.008/21-01-01 -> AP/FA/2.000/21-07-26

v_syg_source := 'AP-AG/F/DF/2.008/21-01-01';
v_syg_dest := 'AP/FA/2.000/21-07-26';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'AP/FA/2.000/21-07-26.pdf', show_on_preview = 1, should_be_merged = 1 where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

--8. Z AP-AG/F/DP/2.006/16-10-11 -> AP/FDP/2.000/21-07-26

v_syg_source := 'AP-AG/F/DP/2.006/16-10-11';
v_syg_dest := 'AP/FDP/2.000/21-07-26';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'AP/FDP/2.000/21-07-26.pdf', show_on_preview = 1, should_be_merged = 1 where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

--9. Z AP-AG/F/PABR/1.003/20-11-09 -> AP/PABR+PEP/1.004/21-07-26

v_syg_source := 'AP-AG/F/PABR/1.003/20-11-09';
v_syg_dest := 'AP/PABR+PEP/1.004/21-07-26';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'AP/PABR+PEP/1.004/21-07-26.pdf', show_on_preview = 1, should_be_merged = 1 where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

--Oświadczenie PEP sygn AP-AG/F/PEP/1.002/18-07-01 - do usunięcia
update eumowy.signature set show_on_preview = 0, active = 0 where name = 'AP-AG/F/PEP/1.002/18-07-01';

--10 – Oświadczenie żądania uruchomienia usługi o sygn = AP/UW_OŻWU/1.000/21-07-26 - nowa sygnatura
begin
v_syg_source := 'AP-AG/F/PEP/1.001/18-07-01';
v_syg_dest := 'AP/UW_OŻWU/1.000/21-07-26';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUW_OŻWU1.00021-07-26.pdf', show_on_preview = 0, should_be_merged = 1, SIGNATURE_ORDER = 47, FILENAME = 'Oswiadczenie zadania rozpoczecia wykonania usługi.pdf',
                            DESCRIPTION = 'Oświadczenie żądania rozpoczęcia wykonania usługi' where name = v_syg_dest;

ALTER TABLE EUMOWY.SIGNATURE ADD SHOULD_BE_MERGED number(1,0);

end;

commit;
-- podmiana sygnatury

declare
    v_syg_source varchar2(100);
    v_syg_dest varchar2(100);
    v_ind integer;

--1. Z 'AP/F/DS/2.001/16-10-11' na'AP/FDS/2.003/21-07-26';
begin
    v_syg_source := 'AP/F/DS/2.001/16-10-11';
    v_syg_dest := 'AP/FDS/2.003/21-07-26';

    v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
    update eumowy.signature set TEMPLATE_PATH = 'APFDS2.00321-07-26.pdf', show_on_preview = 1, should_be_merged = 1  where name = v_syg_dest;
    update eumowy.signature set active = 0 where name = v_syg_source;

end;

commit;

-- ADD MID CBD TO REPRESENTATIVE TO KNOW IF IT IS REPRESENTATIVE FROM CBD
ALTER TABLE EUMOWY.REPRESENTATIVE ADD MID_CBD varchar(15);

--- ADD SUBSCRIBTION DEFINITION
INSERT INTO EUMOWY.SUBSCRIPTION_DEFINITION (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) VALUES(EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (SELECT ID FROM EUMOWY.SIGNATURE WHERE TEMPLATE_PATH = 'APPABRPEP1.00421-07-26.pdf'), 'ACCEPTANT4', NULL, 1, 460, 85, 54, 24);
INSERT INTO EUMOWY.SUBSCRIPTION_DEFINITION (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) VALUES(EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (SELECT ID FROM EUMOWY.SIGNATURE WHERE TEMPLATE_PATH = 'APPABRPEP1.00421-07-26.pdf'), 'PH', NULL, 2, 440, 261, 59, 28);
INSERT INTO EUMOWY.SUBSCRIPTION_DEFINITION (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) VALUES(EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (SELECT ID FROM EUMOWY.SIGNATURE WHERE TEMPLATE_PATH = 'APPABRPEP1.00421-07-26.pdf'), 'PH', NULL, 2, 440, 103, 59, 28);
INSERT INTO EUMOWY.SUBSCRIPTION_DEFINITION (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) VALUES(EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (SELECT ID FROM EUMOWY.SIGNATURE WHERE TEMPLATE_PATH = 'APPABRPEP1.00421-07-26.pdf'), 'ACCEPTANT1', NULL, 2, 460, 485, 59, 28);
INSERT INTO EUMOWY.SUBSCRIPTION_DEFINITION (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) VALUES(EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (SELECT ID FROM EUMOWY.SIGNATURE WHERE TEMPLATE_PATH = 'APPABRPEP1.00421-07-26.pdf'), 'ACCEPTANT2', NULL, 2, 460, 455, 59, 28);
INSERT INTO EUMOWY.SUBSCRIPTION_DEFINITION (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) VALUES(EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (SELECT ID FROM EUMOWY.SIGNATURE WHERE TEMPLATE_PATH = 'APPABRPEP1.00421-07-26.pdf'), 'ACCEPTANT3', NULL, 2, 460, 425, 59, 28);
INSERT INTO EUMOWY.SUBSCRIPTION_DEFINITION (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY) VALUES(EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NEXTVAL, 0, (SELECT ID FROM EUMOWY.SIGNATURE WHERE TEMPLATE_PATH = 'APPABRPEP1.00421-07-26.pdf'), 'ACCEPTANT4', NULL, 2, 460, 395, 59, 28);

--Add nowa umowa signature
INSERT INTO EUMOWY.ACTIVITY_SIGNATURES(ID, VERSION, ACTIVITY_ID, MANDATORY, NUMBER_OF_LIST, SIGNATURE_ID, REQUIRED_ACTIVITIES) VALUES (EUMOWY.ACTIVITY_SIGNATURES_SEQ.nextval, 0, 1, 1, 0, (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APPABRPEP1.00421-07-26.pdf'), null);

--Add informacje dodatkowe panels
--- Dodatkowy punkt, Dodatkowy POS, Wykaz i cena najmu Terminali POS
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'informacjeDodatkowe'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUWRWT1.00521-01-01.pdf'));
--- Zmien PrePaid
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'informacjeDodatkowe'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUWDED1.00221-01-01.pdf'));
--- Zmien DCC, Zmiana prowizji, Zmiana cashback, Nowy zał Nr 3
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'informacjeDodatkowe'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUPZZSNT11.00621-01-01.pdf'));
--- Wymiana umowy platniczej, Wymiana Umowy najmu na Umowę współpracy
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'informacjeDodatkowe'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUWAWU1.00021-01-01.pdf'));
--- Nowy zał Nr 3 Koszty+
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'informacjeDodatkowe'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUPZZSNT21.00621-01-01.pdf'));
--- Usługi dodatkowe (logokalkulator)
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'informacjeDodatkowe'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUWUD1.00421-01-01.pdf'));
--- Wymiana modelowa
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'informacjeDodatkowe'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APFZMP3.00016-10-11.pdf'));

--Add beneficjenci panels
--- Dodatkowy punkt, Dodatkowy POS, Wykaz i cena najmu Terminali POS
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'osobaUprawnionaDoPodpisaniaUmowy'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUWRWT1.00521-01-01.pdf'));
--- Zmien PrePaid
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'osobaUprawnionaDoPodpisaniaUmowy'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUWDED1.00221-01-01.pdf'));
--- Zmien DCC, Zmiana prowizji, Zmiana cashback, Nowy zał Nr 3
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'osobaUprawnionaDoPodpisaniaUmowy'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUPZZSNT11.00621-01-01.pdf'));
--- Wymiana umowy platniczej, Wymiana Umowy najmu na Umowę współpracy
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'osobaUprawnionaDoPodpisaniaUmowy'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUWAWU1.00021-01-01.pdf'));
--- Nowy zał Nr 3 Koszty+
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'osobaUprawnionaDoPodpisaniaUmowy'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUPZZSNT21.00621-01-01.pdf'));
--- Usługi dodatkowe (logokalkulator)
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'osobaUprawnionaDoPodpisaniaUmowy'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APUWUD1.00421-01-01.pdf'));
--- Wymiana modelowa
INSERT INTO EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) VALUES (EUMOWY.SIGNATURE_PANEL_SEQ.nextval, 0, (SELECT ID FROM EUMOWY.PANEL p WHERE p.NAME = 'osobaUprawnionaDoPodpisaniaUmowy'), (SELECT ID FROM EUMOWY.SIGNATURE s WHERE s.TEMPLATE_PATH = 'APFZMP3.00016-10-11.pdf'));

---Drop dla bazy EUMOWY.REPRESENTATIVE_CBD_DATA
DROP TABLE EUMOWY.REPRESENTATIVE_CBD_DATA