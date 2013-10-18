-- rekordKalkulator
create or replace
type               eumowy.rekordKalkulator is object (pole varchar2(50), wartosc varchar2(255));

-- kalkulator
create or replace
type               eumowy.kalkulator is table of EUMOWY.rekordKalkulator;

-- split
create or replace
FUNCTION               split
(p_string    VARCHAR2,
	p_element   INTEGER,
	p_separator VARCHAR2)
		RETURN	    VARCHAR2
	AS
		v_string     VARCHAR2(32767);
	BEGIN
		v_string := p_string || p_separator;
			FOR i IN 1 .. p_element - 1 LOOP
				v_string := SUBSTR(v_string,INSTR(v_string,p_separator)+1);
				END LOOP;
			RETURN TRIM(SUBSTR(v_string,1,INSTR(v_string,p_separator)-1));
		END;

--  GetKalkulatorData
create or replace
function                             GetKalkulatorData(nip varchar2)
return EUMOWY.KALKULATOR
is

cursor kalKursor is
select
KSP_NAZWA as nazwa,
case
when KSP_NAZWA='E_OPLATA_LOGO_1' and KAP_WARTOSC is null THEN 'BRAK'
when KSP_NAZWA='E_OPLATA_KALK_1' and KAP_WARTOSC is null THEN 'BRAK'
else
KAP_WARTOSC
End as wartosc
from CBD_ADM.cbt_kalk kalk
join CBD_ADM.cbt_kalk_pole kp on KAP_KAK_ID=KAK_ID
join CBD_ADM.cbt_kalk_szablon_pole ksp on KAP_KSP_ID=KSP_ID
join CBD_ADM.cbt_kalk_szablon ks on KSP_KAS_ID=KAS_ID
where KAK_NIP=nip
and KAK_ID in
(
Select max(kak_id) as kak_id from CBD_ADM.cbt_kalk
where kak_nip=nip
and kak_status='Zaakceptowany'
);

kal EUMOWY.KALKULATOR := EUMOWY.KALKULATOR();
rekord REKORDKALKULATOR := EUMOWY.REKORDKALKULATOR(null,null);
ind integer;

begin
ind:=1;

FOR rec in kalKursor
   LOOP
  kal.extend;
  rekord.pole:=rec.nazwa;
  rekord.wartosc:=rec.wartosc;
  kal(ind):=rekord;
  ind:=ind+1;
   END LOOP;

return kal;

end;

--- GETKALKULATORDZIALANIE

create or replace
function                                                                       GETKALKULATORDZIALANIE(NIP varchar2)
return EUMOWY.KALKULATOR
is

cursor kalKursor is
select
L_TYP_UMOWY_NAJMU,
E_L_POS,
E_LACZNA_KWOTA_PROM_ZWOL_NAJ_1,
E_OPLATA_PINPAD_1,
E_OPLATA_POS_1,
E_OPLATA_POS_NETTO_1,
E_PROM_OBN_NAJ_1,
L_TYP_UMOWY_PLATN,
S_CZYNNOSCI_KONTRAKTOWE,
S_DCC,
S_KOSZTY_MARZA,
S_PAKIET_SERWIS_1,
S_UMOWA_PP

from
(
select
max(DECODE(nazwa,'L_TYP_UMOWY_NAJMU',wartosc))  L_TYP_UMOWY_NAJMU,
max(DECODE(nazwa,'E_L_POS',wartosc))  E_L_POS,
max(DECODE(nazwa,'E_LACZNA_KWOTA_PROM_ZWOL_NAJ_1',wartosc))  E_LACZNA_KWOTA_PROM_ZWOL_NAJ_1,
max(DECODE(nazwa,'E_OPLATA_PINPAD_1',wartosc))  E_OPLATA_PINPAD_1,
max(DECODE(nazwa,'E_OPLATA_POS_1',wartosc))  E_OPLATA_POS_1,
max(DECODE(nazwa,'E_OPLATA_POS_NETTO_1',wartosc))  E_OPLATA_POS_NETTO_1,
max(DECODE(nazwa,'E_PROM_OBN_NAJ_1',wartosc))  E_PROM_OBN_NAJ_1,
max(DECODE(nazwa,'L_TYP_UMOWY_PLATN',wartosc))  L_TYP_UMOWY_PLATN,
max(DECODE(nazwa,'S_CZYNNOSCI_KONTRAKTOWE',wartosc))  S_CZYNNOSCI_KONTRAKTOWE,
max(DECODE(nazwa,'S_DCC',wartosc))  S_DCC,
max(DECODE(nazwa,'S_KOSZTY_MARZA',wartosc))  S_KOSZTY_MARZA,
max(DECODE(nazwa,'S_PAKIET_SERWIS_1',wartosc))  S_PAKIET_SERWIS_1,
max(DECODE(nazwa,'S_UMOWA_PP',wartosc))  S_UMOWA_PP

from

(
select
KSP_NAZWA as nazwa,REPLACE(KAP_WARTOSC,'&'||'nbsp;') as wartosc
from CBD_ADM.cbt_kalk kalk
join CBD_ADM.cbt_kalk_pole kp on KAP_KAK_ID=KAK_ID
join CBD_ADM.cbt_kalk_szablon_pole ksp on KAP_KSP_ID=KSP_ID
join CBD_ADM.cbt_kalk_szablon ks on KSP_KAS_ID=KAS_ID
where KAK_ID in
(
Select max(kak_id) as kak_id from CBD_ADM.cbt_kalk where kak_nip=NIP
and kak_status='Zaakceptowany'
)
and KSP_NAZWA in
(
'L_TYP_UMOWY_NAJMU',
'E_L_POS',
'E_LACZNA_KWOTA_PROM_ZWOL_NAJ_1',
'E_OPLATA_PINPAD_1',
'E_OPLATA_POS_1',
'E_OPLATA_POS_NETTO_1',
'E_PROM_OBN_NAJ_1',
'L_TYP_UMOWY_PLATN',
'S_CZYNNOSCI_KONTRAKTOWE',
'S_DCC',
'S_KOSZTY_MARZA',
'S_PAKIET_SERWIS_1',
'S_UMOWA_PP'
)


) kalkulator
) PoPivocie;

kal EUMOWY.KALKULATOR := EUMOWY.KALKULATOR();
rekord REKORDKALKULATOR := EUMOWY.REKORDKALKULATOR(null,null);
ind integer;

begin
ind:=1;

FOR rec in kalKursor
LOOP
if rec.S_CZYNNOSCI_KONTRAKTOWE='NOWA UMOWA' AND rec.L_TYP_UMOWY_PLATN is not null AND rec.L_TYP_UMOWY_NAJMU is not null THEN
kal.extend;
-- NOWA UMOWA
rekord.pole:='1';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.E_L_POS is not null AND  rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' THEN
kal.extend;
-- DODATKOWY PUNKT
rekord.pole:='2';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if (rec.E_OPLATA_POS_1 is not null OR rec.E_OPLATA_PINPAD_1 is not null) AND rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' THEN
kal.extend;
-- DODATKOWY POS
rekord.pole:='3';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE'  THEN
kal.extend;
-- Zmiana prowizji
rekord.pole:='4';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.S_KOSZTY_MARZA='TAK' THEN
kal.extend;
-- Dodanie aneksu Koszty+
rekord.pole:='5';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.L_TYP_UMOWY_NAJMU is not null THEN
kal.extend;
--Wymiana umowy najmu
rekord.pole:='6';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.E_L_POS is not null AND rec.E_OPLATA_POS_NETTO_1 is not null THEN
kal.extend;
--Aneks
rekord.pole:='7';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND  rec.L_TYP_UMOWY_NAJMU='Umowa UNTZ – negocjowana' THEN
kal.extend;
--Zmiana tabeli opłat dodatkowych
rekord.pole:='8';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.S_UMOWA_PP='TAK' THEN
kal.extend;
--Zmiana prowizji prepaid
rekord.pole:='9';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' OR (rec.S_CZYNNOSCI_KONTRAKTOWE='NOWA UMOWA' AND rec.L_TYP_UMOWY_NAJMU='Umowa UNTZ – negocjowana') THEN
kal.extend;
--Zmiana okresu lojalnościowego
rekord.pole:='10';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.E_L_POS is not null AND rec.E_LACZNA_KWOTA_PROM_ZWOL_NAJ_1 is not null AND rec.E_PROM_OBN_NAJ_1 is not null THEN
kal.extend;
--Promocyjne obniżenie najmu
rekord.pole:='11';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.S_DCC='TAK' THEN
kal.extend;
-- Nie rozróżniamy tych warunków
--Zmiana Warunków DCC
rekord.pole:='13';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
-- Dodanie DCC
kal.extend;
rekord.pole:='16';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;

END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.S_UMOWA_PP='TAK' THEN
kal.extend;
--Dodanie prepaid
rekord.pole:='12';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if (rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' OR rec.S_CZYNNOSCI_KONTRAKTOWE='NOWA UMOWA') AND rec.S_PAKIET_SERWIS_1 ='EKONOMICZNY' THEN
kal.extend;
--Ekonomiczny
rekord.pole:='17';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if (rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' OR rec.S_CZYNNOSCI_KONTRAKTOWE='NOWA UMOWA') AND rec.S_PAKIET_SERWIS_1 ='KOMFORT' THEN
kal.extend;
--Komfort
rekord.pole:='18';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if (rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' OR rec.S_CZYNNOSCI_KONTRAKTOWE='NOWA UMOWA') AND rec.S_PAKIET_SERWIS_1 ='PRESTIŻ' THEN
kal.extend;
--Prestiż
rekord.pole:='19';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

END LOOP;

return kal;

end;

---  GETKALKULATORPREPAID

create or replace
function                                           GETKALKULATORPREPAID(nip varchar2)
return EUMOWY.KALKULATOR
is
kal EUMOWY.KALKULATOR := EUMOWY.KALKULATOR();
rekord REKORDKALKULATOR := EUMOWY.REKORDKALKULATOR(null,null);

ind integer;
wartosc varchar2(3);

begin

select
KAP_WARTOSC
into wartosc
from CBD_ADM.cbt_kalk kalk
join CBD_ADM.cbt_kalk_pole kp on KAP_KAK_ID=KAK_ID
join CBD_ADM.cbt_kalk_szablon_pole ksp on KAP_KSP_ID=KSP_ID
join CBD_ADM.cbt_kalk_szablon ks on KSP_KAS_ID=KAS_ID
where KAK_NIP=nip
and KAK_ID in
(
Select max(kak_id) as kak_id from CBD_ADM.cbt_kalk
where kak_nip=nip
and kak_status='Zaakceptowany'
)
and KSP_NAZWA in
(
'S_UMOWA_PP'
);

ind:=1;

if wartosc='TAK' THEN

  kal.extend;
  rekord.pole:='CZY_TELEPOMPKA';
  rekord.wartosc:='TAK';
  kal(ind):=rekord;
  ind:=ind+1;
    kal.extend;
  rekord.pole:='CZY_TELEKODZIK';
  rekord.wartosc:='TAK';
  kal(ind):=rekord;
  ind:=ind+1;

END IF;

if wartosc='NIE' THEN

  kal.extend;
  rekord.pole:='CZY_TELEPOMPKA';
  rekord.wartosc:='NIE';
  kal(ind):=rekord;
  ind:=ind+1;
    kal.extend;
  rekord.pole:='CZY_TELEKODZIK';
  rekord.wartosc:='NIE';
  kal(ind):=rekord;
  ind:=ind+1;

END IF;


return kal;

end;

-- GETKALKULATORPROMOCYJNE

create or replace
function                                                                GETKALKULATORPROMOCYJNE(NIP varchar2)
return EUMOWY.KALKULATOR
is


cursor kalKursor is
select L_PROM_OBN_NAJ_1,E_PROM_OBN_NAJ_1,E_LICZBA_MIES_ZWOL_NAJ_1
from
(
select
max(DECODE(nazwa,'L_PROM_OBN_NAJ_1',wartosc))  L_PROM_OBN_NAJ_1,
max(DECODE(nazwa,'E_PROM_OBN_NAJ_1',wartosc))  E_PROM_OBN_NAJ_1,
max(DECODE(nazwa,'E_LICZBA_MIES_ZWOL_NAJ_1',wartosc))  E_LICZBA_MIES_ZWOL_NAJ_1
from

(
select
KSP_NAZWA as nazwa,REPLACE(KAP_WARTOSC,'&'||'nbsp;') as wartosc
from CBD_ADM.cbt_kalk kalk
join CBD_ADM.cbt_kalk_pole kp on KAP_KAK_ID=KAK_ID
join CBD_ADM.cbt_kalk_szablon_pole ksp on KAP_KSP_ID=KSP_ID
join CBD_ADM.cbt_kalk_szablon ks on KSP_KAS_ID=KAS_ID
where KAK_ID in
(
Select max(kak_id) as kak_id from CBD_ADM.cbt_kalk where kak_nip=NIP
and kak_status='Zaakceptowany'
)
and KSP_NAZWA in
(
'L_PROM_OBN_NAJ_1',
'E_PROM_OBN_NAJ_1',
'E_LICZBA_MIES_ZWOL_NAJ_1'
)


) kalkulator
) PoPivocie;

kal EUMOWY.KALKULATOR := EUMOWY.KALKULATOR();
rekord REKORDKALKULATOR := EUMOWY.REKORDKALKULATOR(null,null);
ind integer;

begin
ind:=1;

FOR rec in kalKursor
   LOOP
if rec.L_PROM_OBN_NAJ_1='Liczba miesięcy promocyjnej obniżki ceny najmu' and rec.E_PROM_OBN_NAJ_1 is not null  THEN
kal.extend;
  rekord.pole:='E_PROM_OBN_NAJ_1';
  rekord.wartosc:=rec.E_PROM_OBN_NAJ_1;
  kal(ind):=rekord;
  ind:=ind+1;
END IF;
if rec.L_PROM_OBN_NAJ_1='Liczba miesięcy zwolnienia z najmu' and rec.E_LICZBA_MIES_ZWOL_NAJ_1 is not null THEN
kal.extend;
  rekord.pole:='E_LICZBA_MIES_ZWOL_NAJ_1';
  rekord.wartosc:=rec.E_LICZBA_MIES_ZWOL_NAJ_1;
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

END LOOP;

return kal;

end;

-- GetKalkulatorSerwis

create or replace
function                                                                       GetKalkulatorSerwis(NIP varchar2)
return EUMOWY.KALKULATOR
is

cursor kalKursor is
select
poleApreel as nazwa,
 case
when poleapreel='PAKIET_SERWIS_PRESTIZ' and KAP_WARTOSC='PRESTIŻ' then 'TAK'
when poleapreel='PAKIET_SERWIS_KOMFORT' and KAP_WARTOSC='KOMFORT' then 'TAK'
when poleapreel='PAKIET_SERWIS_EKONOMICZNY' and KAP_WARTOSC='EKONOMICZNY' then 'TAK'
else
'NIE'
end as  wartosc

from EUMOWY.mapowaniekalkulatora mk
join CBD_ADM.CBT_KALK_SZABLON_POLE ksp on mk.polekalkulator=ksp.ksp_nazwa
join CBD_ADM.CBT_KALK_SZABLON ks on ks.KAS_ID=ksp.KSP_KAS_ID
join CBD_ADM.CBT_KALK_POLE kp on kp.KAP_KSP_ID =ksp.ksp_id
join CBD_ADM.cbt_kalk k on k.kak_id=kp.KAP_KAK_ID
where KAK_ID in
(
Select max(kak_id) as kak_id from CBD_ADM.cbt_kalk where kak_nip=NIP
and kak_status='Zaakceptowany'
)
and KSP_NAZWA='S_PAKIET_SERWIS_1';

kal EUMOWY.KALKULATOR := EUMOWY.KALKULATOR();
rekord REKORDKALKULATOR := EUMOWY.REKORDKALKULATOR(null,null);
ind integer;

begin
ind:=1;

FOR rec in kalKursor
   LOOP
  kal.extend;
  rekord.pole:=rec.nazwa;
  rekord.wartosc:=rec.wartosc;
  kal(ind):=rekord;
  ind:=ind+1;
   END LOOP;

return kal;

end;

--  GetKalkulatorStawkaPlaska

create or replace
function                                                  GetKalkulatorStawkaPlaska(NIP varchar2)
return EUMOWY.KALKULATOR
is

cursor kalKursor is
select
min('E_MIN_STAWKA_PLASKA') as  nazwa,
min(kap_wartosc) as wartosc
from CBD_ADM.cbt_kalk kalk
join CBD_ADM.cbt_kalk_pole kp on KAP_KAK_ID=KAK_ID
join CBD_ADM.cbt_kalk_szablon_pole ksp on KAP_KSP_ID=KSP_ID
join CBD_ADM.cbt_kalk_szablon ks on KSP_KAS_ID=KAS_ID
where KSP_NAZWA in
(
'E_OPLATA_PLASKA_1','E_OPLATA_PLASKA_2','E_OPLATA_PLASKA_3','E_OPLATA_PLASKA_4','E_OPLATA_PLASKA_5','E_OPLATA_PLASKA_6'
)
and kap_wartosc is not null
and KAK_ID in
(
Select max(kak_id) as kak_id from CBD_ADM.cbt_kalk
where kak_nip=nip
and kak_status='Zaakceptowany'
);

kal EUMOWY.KALKULATOR := EUMOWY.KALKULATOR();
rekord REKORDKALKULATOR := EUMOWY.REKORDKALKULATOR(null,null);
ind integer;

begin
ind:=1;

FOR rec in kalKursor
   LOOP
  kal.extend;
  rekord.pole:=rec.nazwa;
  rekord.wartosc:=rec.wartosc;
  kal(ind):=rekord;
  ind:=ind+1;
   END LOOP;

return kal;

end;

-- GetkalkulatorZero

create or replace
function                                                                GetkalkulatorZero(NIP varchar2)
return EUMOWY.KALKULATOR
is

cursor kalKursor is
select
poleApreel as nazwa,
0 as  wartosc
from EUMOWY.mapowaniekalkulatora mk
where poleapreel in
(
'OPLATA_DCC_VISA_ZL',
'OPLATA_DCC_MASTERCARD_ZL',
'OPLATA_DCC_MAESTRO_ZL',
'OPLATA_MSC_11_ZL',
'OPLATA_MSC_12_ZL',
'OPLATA_MSC_13_ZL',
'OPLATA_MSC_21_ZL',
'OPLATA_MSC_22_ZL',
'OPLATA_MSC_23_ZL',
'OPLATA_MSC_41_ZL',
'OPLATA_MSC_42_ZL',
'OPLATA_MSC_43_ZL',
'OPLATA_MSC_44_ZL',
'OPLATA_MSC_51_ZL',
'OPLATA_MSC_52_ZL',
'OPLATA_MSC_53_ZL',
'OPLATA_MSC_54_ZL'
);

kal EUMOWY.KALKULATOR := EUMOWY.KALKULATOR();
rekord REKORDKALKULATOR := EUMOWY.REKORDKALKULATOR(null,null);
ind integer;
kakid integer;
begin
ind:=1;

Select max(kak_id) into kakid from CBD_ADM.cbt_kalk
where kak_nip=nip
and kak_status='Zaakceptowany';

if kakid is not null then
FOR rec in kalKursor
   LOOP
  kal.extend;
  rekord.pole:=rec.nazwa;
  rekord.wartosc:=rec.wartosc;
  kal(ind):=rekord;
  ind:=ind+1;
   END LOOP;
end if;

return kal;

end;

-- GetKosztAmortyzacji

create or replace
function                                                                       GetKosztAmortyzacji(NIP varchar2)
return Number
is

wynik Number;
begin

select
ksl_wartosc
into wynik
from  eumowy.kalkulatortypurzadzen k
join cbd_adm.cbt_sl_kalk_slownik slo on slo.ksl_kod=k.typ
where ksl_kod like 'AMORT%'
and  slownik in (
select
max(kap_wartosc) as kap_wartosc
from EUMOWY.mapowaniekalkulatora mk
join cbd_adm.CBT_KALK_SZABLON_POLE ksp on mk.polekalkulator=ksp.ksp_nazwa
join cbd_adm.CBT_KALK_SZABLON ks on ks.KAS_ID=ksp.KSP_KAS_ID
join cbd_adm.CBT_KALK_POLE kp on kp.KAP_KSP_ID =ksp.ksp_id
join cbd_adm.cbt_kalk k on k.kak_id=kp.KAP_KAK_ID
where KAK_ID in
(
Select max(kak_id) as kak_id from cbd_adm.cbt_kalk where kak_nip=NIP
and kak_status='Zaakceptowany'
)
and KSP_NAZWA='S_RODZAJ_URZADZENIA'
);

if wynik is null then
select
max(ksl_wartosc)
into wynik
from  eumowy.kalkulatortypurzadzen k
join cbd_adm.cbt_sl_kalk_slownik slo on slo.ksl_kod=k.typ
where ksl_kod like 'AMORT%';
end if;


return wynik;

end;

-- GETPROCESSDZIALANIE

create or replace
function                                                  EUMOWY.GETPROCESSDZIALANIE(idProcesu NUMBER)
return EUMOWY.KALKULATOR
is

cursor kalKursor is
select a.id as dzialanie,pA.process_activities_id as wartosc  from  EUMOWY.activity a
left outer join EUMOWY.process_Activity pA on a.id=pA.process_activities_id
left outer join eumowy.Process p on pA.process_activities_id=p.id
where a.id not in
(
14,15,20,21,22
)
and p.id=idProcesu;

kal EUMOWY.KALKULATOR := EUMOWY.KALKULATOR();
rekord REKORDKALKULATOR := EUMOWY.REKORDKALKULATOR(null,null);
ind integer;

begin
ind:=1;

FOR rec in kalKursor
LOOP
if rec.wartosc is not null THEN
if rec.dzialanie=13 or rec.dzialanie=16 THEN
kal.extend;
-- dcc 2 dzialania, te same warunki w kalkulatorze
-- trzeba przyslonic
rekord.pole:='13';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
kal.extend;
rekord.pole:='16';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
ELSE
kal.extend;
rekord.pole:=rec.dzialanie;
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;
END IF;

END LOOP;

return kal;

end;


-- SPRAWDZENIEDZIALANIA - STARA FUNKCJA
--create or replace
--function                                                         EUMOWY.SPRAWDZDZIALANIE(idProcessu NUMBER)
--return BOOLEAN
--is
--ind integer;
--nipKlienta varchar2(20);
--wynik BOOLEAN;
--liczbaNiezgodnosci integer;
--jestKlient integer;
--begin
--
--select count(*)
--into
--jestKlient
--from eumowy.Client c
--join eumowy.process p on p.client_id=c.id
--where p.id=idProcessu;
--
--if jestKlient>0 then
--select nip
--into
--nipKlienta
--from eumowy.Client c
--join eumowy.process p on p.client_id=c.id
--where p.id=idProcessu;
--else
--nipKlienta:='';
--End If;
--
--select count(*)
--into
--liczbaNiezgodnosci
--from  EUMOWY.activity a
--left outer join
--(
--select Pole from table(EUMOWY.GETPROCESSDZIALANIE(idProcessu))
--where wartosc='ok'
--group by Pole
--) kalkualtor on kalkualtor.Pole=a.id
--
--left outer join
--(
--select Pole from table(EUMOWY.GETKALKULATORDZIALANIE(nipKlienta))
--where wartosc='ok'
--group by Pole
--) proces on proces.Pole=a.id
--where COALESCE(kalkualtor.Pole,'0')<>COALESCE(proces.Pole,'0');
--
--if liczbaNiezgodnosci=0 then
--wynik:=true;
--Else
--wynik:=false;
--End if;
--
--return wynik;
--
--
--end;

-- GET_KALKULATOR_DZIALANIE
create or replace FUNCTION  "EUMOWY"."GET_KALKULATOR_DZIALANIE" (KAKID number)
return EUMOWY.KALKULATOR
is

cursor kalKursor is
select
S_TYP_UMOWY_NAJMU,
E_L_POS,
E_LACZNA_KWOTA_PROM_ZWOL_NAJ_1,
E_OPLATA_PINPAD_1,
E_OPLATA_POS_1,
E_OPLATA_POS_NETTO_1,
E_PROM_OBN_NAJ_1,
S_TYP_UMOWY_PLATN,
S_CZYNNOSCI_KONTRAKTOWE,
S_DCC,
S_KOSZTY_MARZA,
S_PAKIET_SERWIS_1,
S_UMOWA_PP

from
(
select
max(DECODE(nazwa,'S_TYP_UMOWY_NAJMU',wartosc))  S_TYP_UMOWY_NAJMU,
max(DECODE(nazwa,'E_L_POS',wartosc))  E_L_POS,
max(DECODE(nazwa,'E_LACZNA_KWOTA_PROM_ZWOL_NAJ_1',wartosc))  E_LACZNA_KWOTA_PROM_ZWOL_NAJ_1,
max(DECODE(nazwa,'E_OPLATA_PINPAD_1',wartosc))  E_OPLATA_PINPAD_1,
max(DECODE(nazwa,'E_OPLATA_POS_1',wartosc))  E_OPLATA_POS_1,
max(DECODE(nazwa,'E_OPLATA_POS_NETTO_1',wartosc))  E_OPLATA_POS_NETTO_1,
max(DECODE(nazwa,'E_PROM_OBN_NAJ_1',wartosc))  E_PROM_OBN_NAJ_1,
max(DECODE(nazwa,'S_TYP_UMOWY_PLATN',wartosc))  S_TYP_UMOWY_PLATN,
max(DECODE(nazwa,'S_CZYNNOSCI_KONTRAKTOWE',wartosc))  S_CZYNNOSCI_KONTRAKTOWE,
max(DECODE(nazwa,'S_DCC',wartosc))  S_DCC,
max(DECODE(nazwa,'S_KOSZTY_MARZA',wartosc))  S_KOSZTY_MARZA,
max(DECODE(nazwa,'S_PAKIET_SERWIS_1',wartosc))  S_PAKIET_SERWIS_1,
max(DECODE(nazwa,'S_UMOWA_PP',wartosc))  S_UMOWA_PP

from

(
select
KSP_NAZWA as nazwa,REPLACE(KAP_WARTOSC,'&'||'nbsp;') as wartosc
from CBD_ADM.cbt_kalk kalk
join CBD_ADM.cbt_kalk_pole kp on KAP_KAK_ID=KAK_ID
join CBD_ADM.cbt_kalk_szablon_pole ksp on KAP_KSP_ID=KSP_ID
join CBD_ADM.cbt_kalk_szablon ks on KSP_KAS_ID=KAS_ID
where KAK_ID =KAKID
and KSP_NAZWA in
(
'S_TYP_UMOWY_NAJMU',
'E_L_POS',
'E_LACZNA_KWOTA_PROM_ZWOL_NAJ_1',
'E_OPLATA_PINPAD_1',
'E_OPLATA_POS_1',
'E_OPLATA_POS_NETTO_1',
'E_PROM_OBN_NAJ_1',
'S_TYP_UMOWY_PLATN',
'S_CZYNNOSCI_KONTRAKTOWE',
'S_DCC',
'S_KOSZTY_MARZA',
'S_PAKIET_SERWIS_1',
'S_UMOWA_PP'
)


) kalkulator
) PoPivocie;

kal EUMOWY.KALKULATOR := EUMOWY.KALKULATOR();
rekord REKORDKALKULATOR := EUMOWY.REKORDKALKULATOR(null,null);
ind integer;

begin
ind:=1;

FOR rec in kalKursor
LOOP
if rec.S_CZYNNOSCI_KONTRAKTOWE='NOWA UMOWA' AND rec.S_TYP_UMOWY_PLATN is not null AND rec.S_TYP_UMOWY_NAJMU is not null THEN
kal.extend;
-- NOWA UMOWA
rekord.pole:='1';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.E_L_POS is not null And rec.E_L_POS>0 AND rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' THEN
kal.extend;
-- DODATKOWY PUNKT
rekord.pole:='2';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if ((rec.E_OPLATA_POS_1 is not null and rec.E_OPLATA_POS_1>0) OR rec.E_OPLATA_PINPAD_1 is not null) AND rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' THEN
kal.extend;
-- DODATKOWY POS
rekord.pole:='3';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE'  THEN
kal.extend;
-- Zmiana prowizji
rekord.pole:='4';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.S_KOSZTY_MARZA='TAK' THEN
kal.extend;
-- Dodanie aneksu Koszty+
rekord.pole:='5';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.S_TYP_UMOWY_NAJMU is not null THEN
kal.extend;
--Wymiana umowy najmu
rekord.pole:='6';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.E_L_POS is not null And rec.E_L_POS>0 AND rec.E_OPLATA_POS_NETTO_1 is not null THEN
kal.extend;
--Aneks
rekord.pole:='7';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND  rec.S_TYP_UMOWY_NAJMU='Umowa UNTZ – negocjowana' THEN
kal.extend;
--Zmiana tabeli opłat dodatkowych
rekord.pole:='8';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.S_UMOWA_PP='TAK' THEN
kal.extend;
--Zmiana prowizji prepaid
rekord.pole:='9';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' OR (rec.S_CZYNNOSCI_KONTRAKTOWE='NOWA UMOWA' AND rec.S_TYP_UMOWY_NAJMU='Umowa UNTZ – negocjowana') THEN
kal.extend;
--Zmiana okresu lojalnościowego
rekord.pole:='10';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.E_L_POS is not null And rec.E_L_POS>0 AND rec.E_LACZNA_KWOTA_PROM_ZWOL_NAJ_1 is not null AND rec.E_PROM_OBN_NAJ_1 is not null THEN
kal.extend;
--Promocyjne obniżenie najmu
rekord.pole:='11';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.S_DCC='TAK' THEN
kal.extend;
-- Nie rozróżniamy tych warunków
--Zmiana Warunków DCC
rekord.pole:='13';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
-- Dodanie DCC
kal.extend;
rekord.pole:='16';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;

END IF;

if rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' AND rec.S_UMOWA_PP='TAK' THEN
kal.extend;
--Dodanie prepaid
rekord.pole:='12';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if (rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' OR rec.S_CZYNNOSCI_KONTRAKTOWE='NOWA UMOWA') AND rec.S_PAKIET_SERWIS_1 ='EKONOMICZNY' THEN
kal.extend;
--Ekonomiczny
rekord.pole:='17';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if (rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' OR rec.S_CZYNNOSCI_KONTRAKTOWE='NOWA UMOWA') AND rec.S_PAKIET_SERWIS_1 ='KOMFORT' THEN
kal.extend;
--Komfort
rekord.pole:='18';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

if (rec.S_CZYNNOSCI_KONTRAKTOWE='NEGOCJACJE' OR rec.S_CZYNNOSCI_KONTRAKTOWE='NOWA UMOWA') AND rec.S_PAKIET_SERWIS_1 ='PRESTIŻ' THEN
kal.extend;
--Prestiż
rekord.pole:='19';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;

END LOOP;

return kal;

end;

-- GET_PROCESS_DZIALANIE
create or replace FUNCTION                  "EUMOWY"."GET_PROCESS_DZIALANIE" (v_t eumowy.dzialanie)
return EUMOWY.KALKULATOR
is
cursor kalKursor is
select a.id as dzial,a.numer_pozycji as wartosc from EUMOWY.activity a
where a.CODE in
(
select p.column_value from table (CAST(v_t as eumowy.dzialanie)) p
)
and a.numer_pozycji not in
(
14,15,20,21,22
);

kal EUMOWY.KALKULATOR := EUMOWY.KALKULATOR();
rekord REKORDKALKULATOR := EUMOWY.REKORDKALKULATOR(null,null);
ind integer;

begin
ind:=1;

FOR rec in kalKursor
LOOP
if rec.wartosc is not null THEN
if rec.dzial=13 or rec.dzial=16 THEN
kal.extend;
-- dcc 2 dzialania, te same warunki w kalkulatorze
-- trzeba przyslonic
rekord.pole:='13';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
kal.extend;
rekord.pole:='16';
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
ELSE
kal.extend;
rekord.pole:=rec.dzial;
rekord.wartosc:='ok';
  kal(ind):=rekord;
  ind:=ind+1;
END IF;
END IF;

END LOOP;

return kal;

end;

-- SPRAWDZDZIALANIE
create or replace FUNCTION                      "EUMOWY"."SPRAWDZDZIALANIE" (dzialaniaStr varchar2,KAKID number)
return BOOLEAN

is
ind integer;
wynik BOOLEAN;
liczbaNiezgodnosci integer;
jestKlient integer;

l_array dbms_utility.lname_array;
l_count binary_integer;
v_t eumowy.dzialanie := eumowy.dzialanie();

begin

dbms_utility.comma_to_table(dzialaniaStr, l_count, l_array);

for idx in 1..l_array.count
     loop
       v_t.extend;
       v_t(v_t.last) := l_array(idx);
     end loop;


select count(*)
into
jestKlient
from cbd_adm.cbt_kalk k
where k.kak_id=KAKID;

if jestKlient>0 then

select count(*)
into
liczbaNiezgodnosci
from  EUMOWY.activity a
left outer join
(
select Pole from table(EUMOWY.GET_PROCESS_DZIALANIE(v_t))
where wartosc='ok'
group by Pole
) kalkualtor on kalkualtor.Pole=a.id
left outer join
(
select Pole from table (EUMOWY.GET_KALKULATOR_DZIALANIE(KAKID))
where wartosc='ok'
group by Pole
) proces on proces.Pole=a.id
where COALESCE(kalkualtor.Pole,'0')<>COALESCE(proces.Pole,'0');

End if;

if liczbaNiezgodnosci=0 then
wynik:=true;
Else
wynik:=false;
End if;

return wynik;


End;

-- SPRAWDZDZIALANIE2 zwraca number zamiast boolean do selecta
create or replace 
FUNCTION                               "SPRAWDZDZIALANIE2" (dzialaniaStr varchar2,KAKID number)
return NUMBER
is
wynik number(1,0);
result boolean;
begin
 result := eumowy.sprawdzdzialanie(dzialaniaStr, KAKID);
 if result = true then
  wynik := 1;
 else
  wynik := 0;
 end if;
 return wynik;
end;

-- GET_NUMER_SPRZEDAZOWY
create or replace
FUNCTION                           GET_NUMER_SPRZEDAZOWY (auwId number)
return varchar2
is

wynik varchar2(255);
begin

select max(prz_numer_sprzedazowy)
into wynik
from cbd_adm.adm_uzytkownicy_web w
join cbd_adm.cbt_przedstawicieleh pz on w.auw_Prz_id=pz.prz_id
where w.auw_id=auwId;

return wynik;

end;