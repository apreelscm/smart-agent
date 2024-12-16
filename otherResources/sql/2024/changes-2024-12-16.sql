update EUMOWY.SIGNATURE s SET
    s.SHOULD_BE_MERGED = 1
where s.TEMPLATE_PATH in (
    'KI_RODO24-08-01.pdf',
    'APUW1.01024-08-01.pdf',
    'APUW_OZWU1.00124-08-01.pdf',
    'APUPZT41.01024-08-01.pdf',
    'APUPZT31.01024-08-01.pdf',
    'APUPZT21.01024-08-01.pdf',
    'APUPZT11.01024-08-01.pdf',
    'APFDS2.00424-08-01.pdf',
    'APFDP2.00324-08-01_bez_podpisu_Akceptanta.pdf',
    'APFDP2.00224-08-01_z_podpisem_Akceptanta.pdf',
    'APFA2.00124-08-01.pdf'
);
