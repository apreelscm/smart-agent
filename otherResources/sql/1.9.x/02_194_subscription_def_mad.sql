DECLARE

v_syg_source eumowy.signature.name%type;
v_syg_dest eumowy.signature.name%type;
v_ind eumowy.signature.id%type;
v_ile number;

BEGIN

    --1. AP-AG/F/PABR/1.002/18-07-01 - > AP-AG/F/PABR/1.003/20-11-09
    
    v_syg_source := 'AP-AG/F/PABR/1.002/18-07-01';
    v_syg_dest := 'AP-AG/F/PABR/1.003/20-11-09';
    
    select count(*) into v_ile from eumowy.signature where name = v_syg_dest;
    
    if v_ile = 0 then
    
        v_ind:= EUMOWY.EUM_HELPER.fkopiuj_sygnature(v_syg_source,v_syg_dest);
        update eumowy.signature set TEMPLATE_PATH = 'APAGFPABR1.00320-11-09.pdf' where name = v_syg_dest;
        update eumowy.signature set active = 0 where name = v_syg_source;
    
        -- AP-AG/F/PABR/1.003/20-11-09
        DELETE FROM eumowy.subscription_definition WHERE signature_id = (SELECT id FROM eumowy.signature WHERE name = v_syg_dest);
        
        INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
        VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NextVal, 0, (SELECT id FROM eumowy.signature WHERE name = v_syg_dest), 'ACCEPTANT1', null, 2, 460, 477, 59, 28);
        INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
        VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NextVal, 0, (SELECT id FROM eumowy.signature WHERE name = v_syg_dest), 'ACCEPTANT2', null, 2, 460, 447, 59, 28);
        INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
        VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NextVal, 0, (SELECT id FROM eumowy.signature WHERE name = v_syg_dest), 'ACCEPTANT3', null, 2, 460, 417, 59, 28);
        INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
        VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NextVal, 0, (SELECT id FROM eumowy.signature WHERE name = v_syg_dest), 'ACCEPTANT4', null, 2, 460, 387, 59, 28);
        INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
        VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NextVal, 0, (SELECT id FROM eumowy.signature WHERE name = v_syg_dest), 'PH', null, 2, 440, 253, 59, 28);
        INSERT INTO eumowy.subscription_definition (ID, VERSION, SIGNATURE_ID, ROLE, FILE_NAME, SUBSCRIPTION_PAGE_NUMBER, SUBSCRIPTIONX, SUBSCRIPTIONY, SCALEX, SCALEY)
        VALUES (EUMOWY.SUBSCRIPTION_DEFINITION_SEQ.NextVal, 0, (SELECT id FROM eumowy.signature WHERE name = v_syg_dest), 'PH1', null, 2, 440, 85, 59, 28);

    End if;

End;

commit;