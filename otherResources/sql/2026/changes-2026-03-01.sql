-- ESEU-383

declare

v_syg_source eumowy.signature.name%type;
v_syg_dest eumowy.signature.name%type;
v_ind eumowy.signature.id%type;

begin

v_syg_source := 'AP/UW/1.012/25-06-27';
v_syg_dest := 'AP/UW/1.013/26-03-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUW1.01326-03-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

-----------------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZT1/1.012/25-06-27';
v_syg_dest := 'AP/UPZT1/1.013/26-03-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT11.01326-03-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

-----------------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZT2/1.012/25-06-27';
v_syg_dest := 'AP/UPZT2/1.013/26-03-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT21.01326-03-01' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

-----------------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZT3/1.012/25-06-27';
v_syg_dest := 'AP/UPZT3/1.013/26-03-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT31.01326-03-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

-----------------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZT4/1.012/25-06-27';
v_syg_dest := 'AP/UPZT4/1.013/26-03-01';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT41.01326-03-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

-----------------------------------------------------------------------------------------------------

end;
