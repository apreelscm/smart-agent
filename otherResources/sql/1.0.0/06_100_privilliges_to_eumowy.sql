grant select, insert, update, delete on ACTIVITY to EUMOWY_APP;
grant select, insert, update, delete on ACTIVITY_SIGNATURES to EUMOWY_APP;
grant select, insert, update, delete on ATTACHMENT to EUMOWY_APP;
grant select, insert, update, delete on ATTACHMENT_CONTENT to EUMOWY_APP;
grant select, insert, update, delete on CALCFIELD to EUMOWY_APP;
grant select, insert, update, delete on CALCFIELD_SIGNATURE to EUMOWY_APP;
grant select, insert, update, delete on CLIENT to EUMOWY_APP;
grant select, insert, update, delete on DOCUMENT to EUMOWY_APP;
grant select, insert, update, delete on EMAIL_TEMPLATES to EUMOWY_APP;
grant select, insert, update, delete on PANEL to EUMOWY_APP;
grant select, insert, update, delete on PROCESS to EUMOWY_APP;
grant select, insert, update, delete on PROCESS_DATA to EUMOWY_APP;
grant select, insert, update, delete on SIGNATURE to EUMOWY_APP;
grant select, insert, update, delete on SIGNATURE_PANEL to EUMOWY_APP;
grant select, insert, update, delete on SUBSCRIPTION to EUMOWY_APP;
grant select, insert, update, delete on process_activity to EUMOWY_APP;
grant select, insert, update, delete on process_signature to EUMOWY_APP;
grant select, insert, update, delete on process_subscription to EUMOWY_APP;
grant select, insert, update, delete on process_panel to EUMOWY_APP;
grant select, insert, update, delete on sec_role to EUMOWY_APP;
grant select, insert, update, delete on sec_user to EUMOWY_APP;
grant select, insert, update, delete on sec_user_sec_role to EUMOWY_APP;
grant select, insert, update, delete on LOGS to EUMOWY_APP;
grant select, insert, update, delete on APP_PARAMETERS to EUMOWY_APP;
grant select, insert, update, delete on process_attachment to EUMOWY_APP;

grant select on  dual to EUMOWY_APP
grant select on  ACTIVITY_SEQ to EUMOWY_APP;
grant select on  ACTIVITY_SIGNATURES_SEQ to EUMOWY_APP;
grant select on  ADM_U_WEB_SEQ to EUMOWY_APP;
grant select on  ATTACHMENT_CONTENT_SEQ to EUMOWY_APP;
grant select on  ATTACHMENT_SEQ to EUMOWY_APP;
grant select on  CALCFIELD_SEQ to EUMOWY_APP;
grant select on  CALCFIELD_SIGNATURE_SEQ to EUMOWY_APP;
grant select on  CLIENT_SEQ to EUMOWY_APP;
grant select on  DOCUMENT_SEQ to EUMOWY_APP;
grant select on  EMAIL_TEMPLATES_SEQ to EUMOWY_APP;
grant select on  PANEL_SEQ to EUMOWY_APP;
grant select on  PROCESS_SEQ to EUMOWY_APP;
grant select on  PROCESS_DATA_SEQ to EUMOWY_APP;
grant select on  SIGNATURE_PANEL_SEQ to EUMOWY_APP;
grant select on  SIGNATURE_SEQ to EUMOWY_APP;
grant select on  DOCUMENT_CONTENT_SEQ to EUMOWY_APP;

grant select on mapowaniekalkulatora to EUMOWY_APP;
grant execute on GetKalkulatorSerwis to EUMOWY_APP;
grant execute on GetKalkulatorStawkaPlaska to EUMOWY_APP;
grant execute on GetKalkulatorZero to EUMOWY_APP;

