--eUmowy_ext-240 eUmowy_ext-279
delete from signature_panel where panel_id = 44 and signature_id in (2, 4, 8, 9);
insert into EUMOWY.SIGNATURE (id, version, active, name, signature_order, template_path, for_point, description) values (31, 0,1,'virtualZestawPosOdplatneUzywanie', -1, null, 0, 'Zestaw POS odplatne uzywanie - wymusza prezetacje panelu');
insert into EUMOWY.SIGNATURE_PANEL (ID,VERSION,PANEL_ID,SIGNATURE_ID) values ('220','0','44','31');
insert into EUMOWY.ACTIVITY_SIGNATURES (ID,VERSION,ACTIVITY_ID,MANDATORY,NUMBER_OF_LIST,SIGNATURE_ID) values ('51','0','6','1','0','31');
insert into EUMOWY.ACTIVITY_SIGNATURES (ID,VERSION,ACTIVITY_ID,MANDATORY,NUMBER_OF_LIST,SIGNATURE_ID) values ('52','0','7','1','0','31');

update EUMOWY.SUBSCRIPTION_DEFINITION sd set sd.subscriptionx = 330, sd.subscriptiony = 535 where sd.role = 'ZARZAD1' and sd.signature_id = (select s.id from EUMOWY.SIGNATURE s where s.name = 'AT/USU/FDU/4.004/13-05-22')
