ALTER TABLE EUMOWY.POINT_DETAILS
    ADD IS_ACCEPTED_CARD_TRANSACTIONS number(1,0);

ALTER TABLE EUMOWY.POINT_DETAILS
    ADD IS_PRIVATE_APARTMENT number(1,0);

ALTER TABLE EUMOWY.POINT_DETAILS
    ADD IS_ACCEPTED_PREPAYMENTS number(1,0);

ALTER TABLE EUMOWY.POINT_DETAILS
    ADD MONTHLY_CASH_TURNOVER varchar2(240 CHAR) NULL;

ALTER TABLE EUMOWY.POINT_DETAILS
    ADD MONTHLY_TURNOVER_INSTITUTION varchar2(240 CHAR) NULL;

ALTER TABLE EUMOWY.POINT_DETAILS
    ADD AVERAGE_BILL varchar2(240 CHAR) NULL;

ALTER TABLE EUMOWY.POINT_DETAILS
    ADD HIGHEST_CASH_TRANSACTION varchar2(240 CHAR) NULL;

ALTER TABLE EUMOWY.POINT_DETAILS
    ADD NUMBER_OF_DAILY_TRANSACTIONS varchar2(240 CHAR) NULL;

ALTER TABLE EUMOWY.POINT_DETAILS
    ADD PERCENTAGE_OF_PREPAYMENTS varchar2(240 CHAR) NULL;

ALTER TABLE EUMOWY.POINT_DETAILS
    ADD AVERAGE_DELIVERY_TIME varchar2(240 CHAR) NULL;

ALTER TABLE EUMOWY.POINT_DETAILS
    ADD MAXIMUM_DELIVERY_TIME varchar2(240 CHAR) NULL;

UPDATE eumowy.signature s SET show_on_preview = 1 WHERE id = 3;

// podmiana sygnatury

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


--2. Z AP/UW/1.008/21-01-01 -> AP/UW/1.009/21-07-26

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

--10 – Oświadczenie żądania uruchomienia usługi o sygn = AP/UW_OŻWU/1.000/21-07-26
INSERT INTO EUMOWY.SIGNATURE (ID,VERSION,ACTIVE,NAME,TEMPLATE_PATH,FOR_POINT,SIGNATURE_ORDER,DESCRIPTION,FILENAME,SEND_TO_CLIENT,SHOW_ON_PREVIEW,FOR_POS,SHOW_ON_ZRD,SHOULD_BE_MERGED) VALUES (select eumowy.signature_seq.nextval,0,1,'AP/UW_OŻWU/1.000/21-07-26','APUW_OŻWU1.00021-07-26.pdf',0,47,'Oświadczenie żądania rozpoczęcia wykonania usługi','Oswiadczenie zadania rozpoczecia wykonania usługi.pdf',1,0,NULL,1,1);

ALTER TABLE EUMOWY.SIGNATURE ADD SHOULD_BE_MERGED number(1,0);

end;

commit;
