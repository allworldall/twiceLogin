/*
  �ȴ��������ڴ����洢������
*/

create or replace package PKG_PLMGR_LOGINTOKEN is
       --ɾ����ʹ�õ�(״̬Ϊ1)�ĵ�¼tokenֵ
  PROCEDURE deletelogintoken;
end PKG_PLMGR_LOGINTOKEN;

create or replace package body PKG_PLMGR_LOGINTOKEN is
       --ɾ����ʹ�õ�(״̬Ϊ1)�ĵ�¼tokenֵ
  PROCEDURE deletelogintoken IS
  BEGIN
    DELETE FROM login_token_log ltl WHERE ltl.state = 1;
    
  END;
end PKG_PLMGR_LOGINTOKEN;