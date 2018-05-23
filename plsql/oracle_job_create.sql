

/*
  创建job，10分钟运行一次
*/


begin
  sys.dbms_job.change(job => 1,
                      what => 'PKG_PLMGR_LOGINTOKEN.deletelogintoken;',
                      next_date => sysdate,
                      interval => 'sysdate + 10/24*60');
  commit;
end;
/

/*
  运行job
*/
begin
   dbms_job.run(1);--和select * from user_jobs; 中的job值对应，看what对应的过程
end;