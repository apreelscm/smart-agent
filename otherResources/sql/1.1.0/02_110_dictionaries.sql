--eUmowy_ext-240
delete from signature_panel where panel_id = 44 and signature_id in (2, 4, 8, 9);

insert into EUMOWY.SIGNATURE (id, version, active, name, signature_order, template_path, for_point, description) values (31, 0,1,'virtualZestawPosOdplatneUzywanie', -1, null, 0, 'Zestaw POS odplatne uzywanie - wymusza prezetacje panelu');
update EUMOWY.SIGNATURE s set s.filename = 'Formularz Akceptanta.pdf' where s.name = 'AP-AG/F/DF/2.003/12-02-06';
update EUMOWY.SIGNATURE s set s.filename = 'Formularz Danych Punktu.pdf' where s.name = 'AP-AG/F/DP/2.003/13-05-10';
update EUMOWY.SIGNATURE s set s.filename = 'Formularz Scoringowy.pdf' where s.name = 'AP/F/DS/2.000/09-04-22';
update EUMOWY.SIGNATURE s set s.filename = 'UMOWA O PRZYJMOWANIE ZAPŁATY PRZY UŻYCIU KART PŁATNICZYCH.pdf' where s.name = 'AP/UPZBS/2.001/13-08-06';
update EUMOWY.SIGNATURE s set s.filename = 'UMOWA O PRZYJMOWANIE ZAPŁATY PRZY UŻYCIU KART PŁATNICZYCH.pdf' where s.name = 'AP/UPZIF/2.001/13-08-06';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY O PRZYJMOWANIE ZAPŁATY PRZY UŻYCIU KART PŁATNICZYCH.pdf' where s.name = 'AP/UPZ/IF/2.002/13-08-06';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY O PRZYJMOWANIE ZAPŁATY PRZY UŻYCIU KART PŁATNICZYCH.pdf' where s.name = 'AP/UPZ2/ACB/1.000/13-02-15';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY O PRZYJMOWANIE ZAPŁATY PRZY UŻYCIU KART PŁATNICZYCH.pdf' where s.name = 'AP/UPZ2/DCC/1.000/13-02-15';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY O PRZYJMOWANIE ZAPŁATY PRZY UŻYCIU KART PŁATNICZYCH.pdf' where s.name = 'AP/UPZBS/AIKO/1.000/13-03-25';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY O PRZYJMOWANIE ZAPŁATY PRZY UŻYCIU KART PŁATNICZYCH.pdf' where s.name = 'AP/UPZ/ACB/2.003/13-02-15';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY O PRZYJMOWANIE ZAPŁATY PRZY UŻYCIU KART PŁATNICZYCH.pdf' where s.name = 'AP/UPZ/AWNZBS/1.001/13-08-06';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY O PRZYJMOWANIE ZAPŁATY PRZY UŻYCIU KART PŁATNICZYCH.pdf' where s.name = 'AP/UPZ/DCC/2.003/13-02-15';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY O PRZYJMOWANIE ZAPŁATY PRZY UŻYCIU KART PŁATNICZYCH.pdf' where s.name = 'AP/UPZ/DCCZ/1.002/13-02-15';
update EUMOWY.SIGNATURE s set s.filename = 'UMOWA NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTZ/2.003/12-01-16';
update EUMOWY.SIGNATURE s set s.filename = 'UMOWA NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTSS/1.003/12-01-16';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTSZ/APOO/3.002/12-01-16';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTSZ/AWNZ/3.002/12-01-16';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTSZ/APOU/3.002/12-01-16';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTSZ/OKOD/2.003/12-01-16';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTSZ/DCCZ/1.001/12-10-05';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTSZ/DCC/2.002/12-01-16';
update EUMOWY.SIGNATURE s set s.filename = 'Umowa o współpracy w zakresie sprzedaży i dystrybucji elektronicznych doładowań telePOMPKA i teleKODZIK.pdf' where s.name = 'AT/USU/5.004/13-05-22';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do Umowy o współpracy w zakresie sprzedaży i dystrybucji elektronicznych doładowań telePOMPKA i teleKODZIK.pdf' where s.name = 'AT/USU/FDU/4.004/13-05-22';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTW/AGOP/1.002/12-01-16';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTW/AGOK/1.002/12-01-16';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTW/AGON/1.002/12-01-16';
update EUMOWY.SIGNATURE s set s.filename = 'Aneks do UMOWY NAJMU ZESTAWU POS.pdf' where s.name = 'AP/UNTW/ANOD/1.003/12-01-16';


insert into EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) values ('220','0','44','31');

insert into EUMOWY.ACTIVITY_SIGNATURES (ID,VERSION,ACTIVITY_ID,MANDATORY,NUMBER_OF_LIST,SIGNATURE_ID) values ('51','0','6','1','0','31');
insert into EUMOWY.ACTIVITY_SIGNATURES (ID,VERSION,ACTIVITY_ID,MANDATORY,NUMBER_OF_LIST,SIGNATURE_ID) values ('52','0','7','1','0','31');

update EUMOWY.SUBSCRIPTION_DEFINITION sd set sd.subscriptionx = 330, sd.subscriptiony = 535 where sd.role = 'ZARZAD1' and sd.signature_id = (select s.id from EUMOWY.SIGNATURE s where s.name = 'AT/USU/FDU/4.004/13-05-22')
