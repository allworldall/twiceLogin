/*
  先创建声明在创建存储过程体
*/

create or replace package PKG_PLMGR_LOGINTOKEN is
       --删除已使用的(状态为1)的登录token值
  PROCEDURE deletelogintoken;
end PKG_PLMGR_LOGINTOKEN;

create or replace package body PKG_PLMGR_LOGINTOKEN is
       --删除已使用的(状态为1)的登录token值
  PROCEDURE deletelogintoken IS
  BEGIN
    DELETE FROM login_token_log ltl WHERE ltl.state = 1;
    
  END;
end PKG_PLMGR_LOGINTOKEN;