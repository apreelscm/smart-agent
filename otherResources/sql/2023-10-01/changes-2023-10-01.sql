--1. AP/UPZT1/1.007/21-07-26 - > AP/UPZT1/1.009/22-09-01

v_syg_source := 'AP/UPZT1/1.007/21-07-26';
v_syg_dest := 'AP/UPZT1/1.009/22-09-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT11.00922-09-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

--2. AP/UPZT2/1.007/21-07-26 - > AP/UPZT2/1.009/22-09-01

v_syg_source := 'AP/UPZT2/1.007/21-07-26';
v_syg_dest := 'AP/UPZT2/1.009/22-09-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT21.00922-09-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

--3. AP/UPZT3/1.007/21-07-26 - > AP/UPZT3/1.009/22-09-01

v_syg_source := 'AP/UPZT3/1.007/21-07-26';
v_syg_dest := 'AP/UPZT3/1.009/22-09-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT31.00922-09-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;

--4. AP/UPZT4/1.007/21-07-26 - > AP/UPZT4/1.009/22-09-01

v_syg_source := 'AP/UPZT4/1.007/21-07-26';
v_syg_dest := 'AP/UPZT4/1.009/22-09-01';

v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
update eumowy.signature set TEMPLATE_PATH = 'APUPZT41.00922-09-01.pdf' where name = v_syg_dest;
update eumowy.signature set active = 0 where name = v_syg_source;
