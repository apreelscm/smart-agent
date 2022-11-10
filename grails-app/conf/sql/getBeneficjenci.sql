SELECT cbv.MID, cbv.TYP, cbv.prefix, cbv.imie, cbv.nazwisko,  cbv.pesel, cbv.mid, cbv.data_urodzenia AS "dataUrodzenia", cbv.obywatelstwo
FROM
    CBV_DANE_BENEFICJENCI cbv
        JOIN CBD_ADM.cbt_klienci m
             ON m.KLN_MID  = cbv.mid
WHERE m.KLN_NIP  = :nip
