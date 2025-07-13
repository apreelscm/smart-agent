declare

v_syg_source eumowy.signature.name%type;
v_syg_dest eumowy.signature.name%type;
v_ind eumowy.signature.id%type;

begin

---------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZT4/1.011/25-03-01';
v_syg_dest := 'AP/UPZT4/1.012/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT41.01225-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZT3/1.011/25-03-01';
v_syg_dest := 'AP/UPZT3/1.012/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT31.01225-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZT2/1.011/25-03-01';
v_syg_dest := 'AP/UPZT2/1.012/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT21.01225-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZT1/1.011/25-03-01';
v_syg_dest := 'AP/UPZT1/1.012/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT11.01225-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'AP/UW/1.011/25-03-01';
v_syg_dest := 'AP/UW/1.012/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUW1.01225-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'AP/UW/AWU/1.001/24-08-01';
v_syg_dest := 'AP/UW/AWU/1.002/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUWAWU1.00225-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZ/ZSNT1/1.007/24-08-01';
v_syg_dest := 'AP/UPZ/ZSNT1/1.008/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT11.00825-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZ/ZSNT2/1.007/24-08-01';
v_syg_dest := 'AP/UPZ/ZSNT2/1.008/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT21.00825-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZ/ZSNT3/1.006/24-08-01';
v_syg_dest := 'AP/UPZ/ZSNT3/1.007/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT31.00725-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'AP/UPZ/ZSNT4/1.006/24-08-01';
v_syg_dest := 'AP/UPZ/ZSNT4/1.007/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZZSNT41.00725-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'PABR+PEP/25.03/03';
v_syg_dest := 'PABR+PEP/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'PABR_PEP25-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

v_syg_source := 'RODO/PKOBP/25-02-01';
v_syg_dest := 'RODO/PKOBP/25-06-27';
v_ind := EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'Kl_RODO_PKOBP25-06-27.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
update eumowy.signature set active = 1 where name = v_syg_dest;

---------------------------------------------------------------------------------------------

end;
