-- eUmowy_ext-391 modyfikacja funkcji dla walidacji numer 2

CREATE OR REPLACE FUNCTION EUMOWY.f_convert(p_list IN VARCHAR2)
    RETURN eumowy.dzialanie
    AS
      l_string       VARCHAR2(32767) := p_list || ',';
      l_comma_index  PLS_INTEGER;
      l_index        PLS_INTEGER := 1;
      l_tab          eumowy.dzialanie := eumowy.dzialanie();
    BEGIN
     LOOP
       l_comma_index := INSTR(l_string, ',', l_index);
       EXIT WHEN l_comma_index = 0;
       l_tab.EXTEND;
       l_tab(l_tab.COUNT) := SUBSTR(l_string, l_index, l_comma_index - l_index);
       l_index := l_comma_index + 1;
     END LOOP;
     RETURN l_tab;
END f_convert;
/
CREATE OR REPLACE FUNCTION EUMOWY."SPRAWDZ_DZIALANIE" (dzialaniaStr varchar2,KAKID number,sygnaturyStr varchar2)
return NUMBER

is
ind integer;
wynik NUMBER;
liczbaNiezgodnosci integer;
jestKlient integer;

l_array dbms_utility.lname_array;
l_count binary_integer;

l_array2 dbms_utility.lname_array;
l_count2 binary_integer;

v_t eumowy.dzialanie := eumowy.dzialanie();
v_s eumowy.dzialanie := eumowy.dzialanie();
begin

dbms_utility.comma_to_table(dzialaniaStr, l_count, l_array);

for idx in 1..l_array.count
     loop
       v_t.extend;
       v_t(v_t.last) := l_array(idx);
     end loop;


v_s:=eumowy.f_convert(sygnaturyStr);

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
wynik:=1;
Else
wynik:=0;
End if;

wynik:=1;

return wynik;

End;
/
drop function  SPRAWDZDZIALANIE;
drop function  SPRAWDZDZIALANIE2;