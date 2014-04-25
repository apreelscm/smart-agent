SELECT KTU_SLOWNIK AS "value",
        CASE
        WHEN KTU_RODZAJ LIKE 'przen%' THEN 1
        ELSE 0
        END AS "is_mobile"
FROM eumowy.kalkulator_typ_urzadzen
WHERE ktu_medium=:medium