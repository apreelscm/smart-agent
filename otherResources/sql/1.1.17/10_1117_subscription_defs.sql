update eumowy.subscription_definition 
set scalex=94, scaley=63 
where role = 'ACCEPTANT1' or role = 'ACCEPTANT2';

update eumowy.subscription_definition 
set scalex=84, scaley=53 
where role = 'PH';