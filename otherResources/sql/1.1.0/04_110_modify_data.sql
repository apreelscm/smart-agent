update EUMOWY.document set client_name = name where client_name is null;

--eUmowy_ext-240 eUmowy_ext-279 rozdzielenie paneli

create or replace
procedure remoZestawPosOdpUzyPanels AS

  cursor processes is
    select pp.process_panels_id as procId, pp.panels_idx as order_no
    from process_panel pp
    where pp.panel_id = 44
    --and pp.process_panels_id = 151
    and pp.process_panels_id not in
      (select distinct pa.process_activities_id
      from process_activity pa
      where pa.activity_id in (6,7)
      );

  begin

  --dbms_output.put_line('updating records');

  FOR rec in processes loop
    dbms_output.put_line(rec.procId);
    update process_panel set panels_idx = panels_idx - 1
    where process_panels_id = rec.procId
    and panels_idx > rec.order_no;

  END loop;
  --rollback;
END remoZestawPosOdpUzyPanels;

execute remoZestawPosOdpUzyPanels();
drop function remoZestawPosOdpUzyPanels;

delete from process_panel pp
where pp.panel_id = 44
and pp.process_panels_id not in
  (select distinct pa.process_activities_id
  from process_activity pa
  where pa.activity_id in (6,7));

commit;