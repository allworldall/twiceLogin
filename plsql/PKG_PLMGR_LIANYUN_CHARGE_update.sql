/*
	�޸����´洢���̺���
*/ 



create or replace package PKG_PLMGR_LIANYUN_CHARGE is
----------------------------------------------------------------------����ֵ
  -- ��������
  E_PARAM_ERROR constant integer :=                 -1001;

  -- �������ƣ��ظ�
  E_REPEAT      constant integer :=                 -1002;

  --��������
  E_DATA_ERROR  constant integer :=                 -1003;

  --��ӦgameCode����Ϊ��
  E_GAMECODE_NULL constant integer :=               -1004;

  --��Ӧ������action����Ϊ��
  E_ACTION_NULL constant integer :=                 -1005;

  --��Ӧ������cp��������Ϊ��
  E_CPCODE_NULL constant integer :=                 -1006;
-----------------------------------------------------------------------����
 --Ԥ֧����ַaction
 PRE_CHARGE_ACTION constant varchar2(50) :=         'preCharge';


/*************************************************������������************************************************/
--��ȡ���з���cpID�������������ã���ҳ\ģ����ѯ��
  procedure getAllLianyunChannelConfig(
                               n_cp_name    in varchar2,
                               n_page_size  in number,
                               n_page_index in number,
                               n_total_size out integer,
                               cur_result   out sys_refcursor
                               );
--����ID��ȡ������������
  procedure getLianyunChannelConfigByCpId(
                               n_cp_id      in integer,
                               cur_result   out sys_refcursor
                              );
/*��ȡ����������Ϣ
   ����
       1   �ɹ�
*/
   function getAllCPInfo(
                             ur_result      out sys_refcursor
                             )return integer;
/*����������������
  ���أ�  1  �ɹ�
         -1  ��������ʧ��
      -1001  ��������
      -1002  �������ظ�
*/
  function insertLianyunChannelConfig(
                                n_cp_name      in varchar2,
                                n_state        in number,
                                n_cp_code      in varchar2,
                                n_action_name  in varchar2
                               	)return integer;
/*�޸���������

    ���أ�
          1  �ɹ�
         -1  �޸�����ʧ��
      -1001  ��������
      -1002  �������ظ�
      -1003  ��������Ҫ�޸ĵ�����
*/
  function updateLianyunChannelConfig(
                                n_cp_id        in number,
                                n_cp_name      in varchar2,
                                n_state        in number,
                                n_cp_code      in varchar2,
                                n_action_name  in varchar2
                               	)return integer;
 /*************************************************�������߲���������************************************************/
/*��ȡ���з���Key�����˲�������Ϣ����ҳ\ģ����ѯ��     UC_CONFIG_INFO
   ���أ�
          1  �ɹ�
         -1  ʧ��
*/
  function getAllLianyunParamConfig(
                               n_key        in varchar2,
                               n_page_size  in number,
                               n_page_index in number,
                               n_total_size out integer,
                               cur_result   out sys_refcursor
                               )return integer;
/*��ȡ�������˲�������Ϣ
   ���أ�
          1  �ɹ�
         -1  ʧ��
*/
  function getAllLianyunParam(
                               cur_result   out sys_refcursor
                               )return integer;
/*����key��ȡ��Ӧ�����˲�������Ϣ
   ���أ�
          1  �ɹ�
         -1  ʧ��
*/
 function getLianyunParamConfigByKey(
                               n_key         in varchar2,
                               cur_result    out sys_refcursor
                               )return integer;
/*�������˲���������
  ���أ�  1  �ɹ�
         -1  ��������ʧ��
      -1001  ��������
      -1002  �������ظ�
*/
  function insertLianyunParamConfig(
                                n_key           in varchar2,
                                n_name          in varchar2,
                                n_default_value in varchar2,
                                n_is_default    in varchar2,
                                n_type          in number
                               	)return integer;
/*�޸����˲���������

    ���أ�
          1  �ɹ�
         -1  �޸�����ʧ��
      -1001  ��������
      -1002  �������ظ�
      -1003  ��������Ҫ�޸ĵĲ�����
*/
  function updateLianyunParamConfig(
                                n_old_key      in varchar2,
                                n_key          in varchar2,
                                n_name         in varchar2,
                                n_default_value in varchar2,
                                n_is_default    in varchar2,
                                n_type          in number
                               	)return integer;
/*ɾ�����˲���������
  ���أ�
          1  �ɹ�
         -1  ɾ������ʧ��
      -1003  ��������Ҫɾ���Ĳ�����
*/
  function deleteLianyunParamConfig(
                                n_key      in varchar2
                               	)return integer;
/*********************************************************** ������Ϸ��Ϣ���� **************************************************/
/*��ȡ����������Ϸ������Ϣ
    ���أ�
           1  �ɹ�
          -1  ʧ��
*/
  function getAllLianyunConfig(
                               n_game_id    in number,   --��ϷID
                               n_cp_game_id in number,   --���������ϷID
                               n_cp_id      in number,   --�������ID
                               n_page_size  in number,
                               n_page_index in number,
                               n_total_size out integer,
                               cur_result   out sys_refcursor
                               )return integer;
/*����������Ϸ����
    ���أ�
           1  �ɹ�
          -1  ����ʧ��
*/
   function addLianyunConfig(
                               n_game_id      in number,    --��ϷID
                               n_cp_game_id   in number,    --���������ϷID]
                               n_cp_game_name in varchar2,  --���������Ϸname
                               n_cp_id        in number,    --�������ID
                               n_sys_ip       in varchar2,  --Ԥ֧����ַ�ͻص���ַ��ipǰ׺
                               n_state        in number,    --�Ƿ���Ч
                               n_sysIPID      in number     --IPǰ׺ID
                               )return integer;
/*�޸�������Ϸ����
    ���أ�
           1  �ɹ�
          -1  ����ʧ��
       -1001  ��������
       -1002  �����ظ�
       -1003  ��������
*/
   function updateLianyunConfig(
                               n_old_game_id      in number,    --�޸�ǰ������ϷID
                               n_old_cp_game_id   in number,    --�޸�ǰ�������������ϷID
                               n_old_cp_id        in number,    --�޸�ǰ�����������ID
                               n_game_id          in number,    --��ϷID
                               n_cp_game_id       in number,    --���������ϷID]
                               n_cp_game_name     in varchar2,  --���������Ϸname
                               n_cp_id            in number,    --�������ID
                               n_sys_ip           in varchar2,  --Ԥ֧����ַ�ͻص���ַ��ipǰ׺
                               n_state            in number,    --�Ƿ���Ч
                               n_sysIPID          in number     --IPǰ׺ID
                               )return integer;
/*����uc_cp_charge_config��cp_callback_address��pre_charge_url��������cpCode actionCode �ı䣬������Ϸ gameCode�ı�
      ���� ��
           1  �ɹ�
          -1  ����ʧ��
       -1004  ��ӦgameCode����Ϊ��
       -1005  ��Ӧ������action����Ϊ��
       -1006  ��Ӧ������cp��������Ϊ��
*/
   function updateCPURLForGameOrCpUpd(
                                  n_game_id    in number,  --gameCode�ı��game_id
                                  n_cp_id      in number   --cpCode actionCode �ı��cp_id
                                  )return integer;
/**������Ϣ��ȡƴ�ӵ�URL
    ���أ�
           1  �ɹ�
          -1  ����ʧ��
       -1004  ��ӦgameCode����Ϊ��
       -1005  ��Ӧ������action����Ϊ��
       -1006  ��Ӧ������cp��������Ϊ��

*/
    function getURLById(
                        n_game_id    in number,
                        n_cp_id      in number,
                        n_url        in varchar2,       --һ����IPǰ׺��������uc_cp_charge_config�е�URL
                        v_ip_address in varchar2,
                        n_pre_charge_url         out varchar2,
                        n_cp_callback_address    out varchar2
                        )return integer;
/*ɾ��������Ϸ����
   ���� ��
      1    �ɹ�
     -1    ʧ��
  -1001    ��������
  -1003    ��������Ҫ����������
*/
   function deleteLianyunConfig(
                               n_game_id      in number,    --��ϷID
                               n_cp_game_id   in number,    --���������ϷID
                               n_cp_id        in number     --�������ID
                               ) return integer;
/*����������Ϸ���á��������Ƿ���Ч
      1    �ɹ�
     -1    ʧ��
  -1001    ��������
  -1003    ��������Ҫ����������
*/
   function updateLianyunConfigState(
                                       n_game_id      in number,    --��ϷID
                                       n_cp_game_id   in number,    --���������ϷID
                                       n_cp_id        in number,    --�������ID
                                       n_state        in number     --�Ƿ���Ч
                                     )return integer;
/*����ID��ȡ��Ϸvalue������Ϣ
   ����
         1    �ɹ�
        -1    ʧ��
     -1001    ��������
*/
   function getLianyunConfigDetailByID(
                                  n_cp_game_id   in  number,
                                  ur_result      out sys_refcursor
                                  )return integer;
/*ɾ����Ϸvalue������Ϣ
 ����
         1    �ɹ�
        -1    ʧ��
     -1001    ��������
*/
   function deleteLianyunConfigDetail(
                                      n_cp_game_id   in  number,
                                      n_key          in  varchar2
                                      )return integer;

/*������Ϸvalue������Ϣ
 ����
         1    �ɹ�
        -1    ʧ��
     -1001    ��������
     -1003    ������key
*/
   function addLianyunConfigDetail(
                                    n_cp_game_id   in  number,
                                    n_key          in  varchar2,
                                    n_name         in  varchar2,
                                    n_value        in  varchar2
                                   )return integer;

/*�ж������game_id �� game_name�Ƿ�����
    ����
       1   ����
   -1001   ������
   -1003   ��������
 */
   function checkCpGameInfo(
                            n_cp_game_id    in number,
                            n_cp_game_name  in varchar2
                            )return integer;
 /*��ȡ����������Ϸ
   ����
       1   �ɹ�
*/
   function getAllCPGameInfo(
                             ur_result      out sys_refcursor
                             )return integer;
 /*��ȡ����ʹ�õ�Ipǰ׺
   ����
       1   �ɹ�
*/
   function getAllUseSysIP(
                             ur_result      out sys_refcursor
                             )return integer;
/*��ȡ����Ipǰ׺
   ����
       1   �ɹ�
*/
   function getAllSysIP(
                             ur_result      out sys_refcursor
                             )return integer;                            
/*����һ��IPǰ׺
   -1001   ��������
   -1002   ip�ظ�
   1       �ɹ�
*/
   function addSysIP(
                        v_sys_ip    varchar2,
                        v_comments  varchar2,
                        n_is_use    number
                        )return integer;
 /*�޸�һ��IPǰ׺
   -1001   ��������
   -1002   ip�ظ�
   -1003   ���������ݲ�����
   1       �ɹ�
*/  
   function updateSysIP(
                        n_sys_ip_id number,
                        v_sys_ip    varchar2,
                        v_comments  varchar2,
                        n_is_use    number
                        )return integer;       
 /*ɾ��IPǰ׺
   -1001   ��������
   1       �ɹ�
*/
   function deleteSysIP(
                        n_sys_ip_id number
      )return integer;                                       
end PKG_PLMGR_LIANYUN_CHARGE;










 create or replace package body PKG_PLMGR_LIANYUN_CHARGE is
/*************************************************������������************************************************/
/*��ȡ���з���cpID�������������ã���ҳ\ģ����ѯ��*/
 procedure getAllLianyunChannelConfig(
                               n_cp_name    in varchar2,
                               n_page_size  in number,
                               n_page_index in number,
                               n_total_size out integer,
                               cur_result   out sys_refcursor
                               )is
     
  s_sql varchar(200);
  begin
    if (n_cp_name is null)then
       s_sql := 'select ucc.cp_id,ucc.cp_name,ucc.config_time,ucc.state,ucc.cp_code,ucc.action_name from UC_CP_CONFIG ucc order by ucc.cp_id ';
    else 
       s_sql := 'select ucc.cp_id,ucc.cp_name,ucc.config_time,ucc.state,ucc.cp_code,ucc.action_name from UC_CP_CONFIG ucc where ucc.cp_name like ''%' || n_cp_name ||'%'' order by ucc.cp_id  ';
    end if;
    pkg_plmgr_util.getPageCursorBySql(s_sql,n_page_size,n_page_index,n_total_size,cur_result);
  end;
/*����ID��ȡ������������*/
  procedure getLianyunChannelConfigByCpId(
                               n_cp_id      in integer,
                               cur_result   out sys_refcursor
                               )is
   s_sql VARCHAR2(2000);
   begin
     s_sql := 'select ucc.cp_id,ucc.cp_name,ucc.config_time,ucc.state,ucc.cp_code,ucc.action_name 
               from UC_CP_CONFIG ucc
               where ucc.cp_id = ' || n_cp_id;
     OPEN cur_result FOR s_sql;
   end;
  
/*��ȡ����������Ϣ
   ���� 
       1   �ɹ�
*/
   function getAllCPInfo(
                             ur_result      out sys_refcursor
                             )return integer is
   n_result       integer  := pkg_plmgr_util.COMMON_SUCCESS;    
   n_sql          varchar(200);  
   begin
     n_sql := 'select ucc.cp_id,ucc.cp_name from uc_cp_config ucc order by ucc.cp_id';
     open ur_result for n_sql;
     return n_result;
   end;     
/*���������������� 
  ���أ�  1  �ɹ�
         -1  ��������ʧ��
      -1001  ��������
      -1002  �������ظ�
*/                              
  function insertLianyunChannelConfig(
                                n_cp_name      in varchar2,
                                n_state        in number,
                                n_cp_code      in varchar2,
                                n_action_name  in varchar2
                               	)return integer is
  n_result  integer  := pkg_plmgr_util.COMMON_SUCCESS; 
  s_count   integer;                            
  begin
    --�жϲ���
    if((n_cp_name is null) or (n_state is null)) then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
                goto ExitError;
    end if;    
    --�ж��������Ƿ��ظ�
    select count(*) into s_count from UC_CP_CONFIG where cp_name = n_cp_name;
    if(s_count >0 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
                goto ExitError;
    end if;
    
    --��������
    begin
    insert into UC_CP_CONFIG(cp_id,cp_name,config_time,state,cp_code,action_name) values(sequen_sys_project_id.nextval,n_cp_name,sysdate,n_state,n_cp_code,n_action_name);
    goto  ExitOK;
    
    exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
    end;
    --����
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
    return n_result;
  end;
  /*�޸���������
  
    ���أ�  
          1  �ɹ�
         -1  �޸�����ʧ��
      -1001  ��������
      -1002  �������ظ�
      -1003  ��������Ҫ�޸ĵ�����
      -1004  ��ӦgameCode����Ϊ��
      -1005  ��Ӧ������action����Ϊ��
      -1006  ��Ӧ������cp��������Ϊ��  
  */
   function updateLianyunChannelConfig(
                                n_cp_id        in number,
                                n_cp_name      in varchar2,
                                n_state        in number,
                                n_cp_code      in varchar2,
                                n_action_name  in varchar2
                               	)return integer is
  n_result      integer  := pkg_plmgr_util.COMMON_SUCCESS; 
  s_count       integer;  
  s_cp_code     varchar2(100);
  s_action_name varchar(100) ;                         
  begin
    --�жϲ���
    if((n_cp_name is null) or (n_state is null)) then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
                goto ExitError;
    end if;   
    --�ж��Ƿ����Ҫ�޸ĵ�����
    select count(*) into s_count from UC_CP_CONFIG where cp_id = n_cp_id;
    if(s_count <1 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
                goto ExitError;
    end if;
     
    --�ж��������Ƿ��ظ�
    select count(*) into s_count from UC_CP_CONFIG where cp_name = n_cp_name and cp_id <> n_cp_id;
    if(s_count >0 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
                goto ExitError;
    end if;
    
    begin
    
    --���������޸�uc_cp_charge_config���ж�Ӧ��URL
    --��ѯ�Ƿ��޸���cp_code �� action_name
    select cp_code,action_name into s_cp_code,s_action_name from UC_CP_CONFIG where cp_id = n_cp_id;
    if(s_cp_code <> n_cp_code or s_action_name <> n_action_name or (n_cp_code is null and s_cp_code is not null) or (n_action_name is null and s_action_name is not null)) then
         --����ǣ���ѯ�Ƿ��Ѿ��и�������cp_charge_config������
         select count(*) into s_count from uc_cp_charge_config where cp_id = n_cp_id;
         if(s_count > 0) then
             --����ǣ�������cp_code��action_name�޸�Ϊnull
             if(n_cp_code is null)then
                 n_result := PKG_PLMGR_LIANYUN_CHARGE.E_CPCODE_NULL;
                 goto ExitError;
             end if;
             if(n_action_name is null)then
                 n_result := PKG_PLMGR_LIANYUN_CHARGE.E_ACTION_NULL;
                 goto ExitError;
             end if;
            --�޸���������
            update UC_CP_CONFIG set cp_name = n_cp_name,state = n_state,cp_code = n_cp_code,action_name = n_action_name
            where cp_id = n_cp_id;
             --������Ҫ����Ӧ��uc_cp_charge_config �ĵ�ַˢ��
             n_result := updateCPURLForGameOrCpUpd(null,n_cp_id);
             if(n_result < 0) then
                  goto ExitError;
             end if;
          else
             --�޸���������
            update UC_CP_CONFIG set cp_name = n_cp_name,state = n_state,cp_code = n_cp_code,action_name = n_action_name
            where cp_id = n_cp_id;
          end if;
    else
       --�޸���������
      update UC_CP_CONFIG set cp_name = n_cp_name,state = n_state,cp_code = n_cp_code,action_name = n_action_name
      where cp_id = n_cp_id;
    end if;
    
    
   
    
    exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
    end;
    --����
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
    return n_result;
  end;
 /*************************************************�������߲���������************************************************/  
/*��ȡ���з���Key�����˲�������Ϣ����ҳ\ģ����ѯ����
   ���أ�  
          1  �ɹ�
         -1  ʧ��
*/   
  function getAllLianyunParamConfig(
                               n_key        in varchar2,
                               n_page_size  in number,
                               n_page_index in number,
                               n_total_size out integer,
                               cur_result   out sys_refcursor
                               )return integer is
  n_result  integer  := pkg_plmgr_util.COMMON_SUCCESS;    
  s_sql varchar(200);
  begin
    
    if (n_key is null)then
       s_sql := 'select uci.key,uci.name,uci.DEFAULT_VALUE,uci.IS_DEFAULT,uci.type from UC_CONFIG_INFO uci order by uci.key ';
    else 
       s_sql := 'select uci.key,uci.name,uci.DEFAULT_VALUE,uci.IS_DEFAULT,uci.type from UC_CONFIG_INFO uci where uci.key like ''%' || n_key ||'%'' order by uci.key  ';
    end if;
    
    begin
    --��ҳ��ѯ
    pkg_plmgr_util.getPageCursorBySql(s_sql,n_page_size,n_page_index,n_total_size,cur_result);
    goto ExitOK;
      
    exception when others then
      goto ExitError;
    end;
    
    --����
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
     n_result := pkg_plmgr_util.COMMON_ERROR;
    return n_result;
  end;   
/*��ȡ�������˲�������Ϣ
   ���أ�  
          1  �ɹ�
         -1  ʧ��
*/   
  function getAllLianyunParam(
                               cur_result   out sys_refcursor
                               )return integer is
  n_result  integer  := pkg_plmgr_util.COMMON_SUCCESS;    
  s_sql varchar(200);
  begin
       s_sql := 'select uci.key,uci.name,uci.type from UC_CONFIG_INFO uci order by uci.key ';
    begin
      open cur_result for s_sql;
      goto ExitOK;
      
    exception when others then
      goto ExitError;
    end;
    
    --����
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
     n_result := pkg_plmgr_util.COMMON_ERROR;
    return n_result;
  end;   
/*����key��ȡ��Ӧ�����˲�������Ϣ
   ���أ�  
          1  �ɹ�
         -1  ʧ��
*/

 function getLianyunParamConfigByKey(
                               n_key         in varchar2,
                               cur_result    out sys_refcursor
                               )return integer is
  n_sql       varchar2(200) ;                            
  n_result  integer  := pkg_plmgr_util.COMMON_SUCCESS;    
  begin
    begin
       n_sql := 'select uci.key,uci.name,uci.DEFAULT_VALUE,uci.IS_DEFAULT,uci.type from UC_CONFIG_INFO uci where uci.key = ''' || n_key || '''order by uci.key'  ;
    open cur_result for n_sql;
    goto ExitOK;
      
    exception when others then
      goto ExitError;
    end;
    
    --����
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
     n_result := pkg_plmgr_util.COMMON_ERROR;
    return n_result;
  end;     
/*�������˲��������� 
  ���أ�  1  �ɹ�
         -1  ��������ʧ��
      -1001  ��������
      -1002  �������ظ�
*/                                               
  function insertLianyunParamConfig(
                                n_key           in varchar2,
                                n_name          in varchar2,
                                n_default_value in varchar2,
                                n_is_default    in varchar2,
                                n_type          in number
                                 )return integer is
  n_result  integer  := pkg_plmgr_util.COMMON_SUCCESS; 
  s_count   integer;                            
  begin
    --�жϲ���
    if((n_key is null) or (n_name is null) or(n_is_default is null) or(n_is_default = 1 and n_default_value is null) or(n_type is null)) then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
                goto ExitError;
    end if;    
    --�ж����˲������Ƿ��ظ�
    select count(*) into s_count from UC_CONFIG_INFO where key = n_key;
    if(s_count >0 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
                goto ExitError;
    end if;
    
    --��������
    begin
    --insert into UC_CONFIG_INFO(key,name,default_value,is_default,UC_CONFIG_INFO.TYPE) values(n_key,n_name,n_default_value,n_is_default,n_type);
    insert into UC_CONFIG_INFO(key,name,default_value,is_default,type) values(n_key,n_name,n_default_value,n_is_default,n_type);

    goto  ExitOK;
    
    exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
    end;
    --����
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
    return n_result;
  end;    
/*�޸����˲���������
  
    ���أ�  
          1  �ɹ�
         -1  �޸�����ʧ��
      -1001  ��������
      -1002  �������ظ�
      -1003  ��������Ҫ�޸ĵĲ�����
      -1004  ��ΪĬ��key��keyֵ�����޸�
*/                               
  function updateLianyunParamConfig(
                                n_old_key  in varchar2,
                                n_key      in varchar2,
                                n_name     in varchar2,
                                n_default_value in varchar2,
                                n_is_default    in varchar2,
                                n_type          in number
                                 )return integer  is
  n_result  integer  := pkg_plmgr_util.COMMON_SUCCESS; 
  s_count   integer;                         
  begin
    --�жϲ���
    if((n_key is null) or (n_name is null) or (n_type is null) or (n_old_key  is null) or(n_is_default is null) or(n_is_default = 1 and n_default_value is null) ) then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
                goto ExitError;
    end if;   
    --�ж��Ƿ����Ҫ�޸ĵ�����
    select count(*) into s_count from UC_CONFIG_INFO where key = n_old_key;
    if(s_count <1 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
                goto ExitError;
    end if;
     
    --�ж��������Ƿ��ظ�
    select count(*) into s_count from UC_CONFIG_INFO where key = n_key and key <> n_old_key;
    if(s_count >0 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
                goto ExitError;
    end if;
    
    --�޸�����
    begin
    
   -- update UC_CONFIG_INFO  set key = n_key, name = n_name, default_value = n_default_value, is_default = n_is_default, type = n_type
    update UC_CONFIG_INFO  set key = n_key, name = n_name, default_value = n_default_value, is_default = n_is_default, type = n_type

    where key = n_old_key;--�޸�KeyName
    
    update uc_cp_charge_detail set key = n_key
    where key = n_old_key;--�޸������õ�OldKeyֵ��Ϊkey
    
    exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
    end;
    --����
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
    return n_result;
  end; 
/*ɾ�����˲���������
  ���أ�
          1  �ɹ�
         -1  ɾ������ʧ��
      -1003  ��������Ҫɾ���Ĳ�����                           
*/

  function deleteLianyunParamConfig(
                                n_key      in varchar2
                                 )return integer is
  n_result  integer  := pkg_plmgr_util.COMMON_SUCCESS; 
  s_count   integer;                            
  begin
    --�жϲ���
    if(n_key is null) then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
                goto ExitError;
    end if;   
    --�ж��Ƿ����Ҫɾ��������
    select count(*) into s_count from UC_CONFIG_INFO where key = n_key;
    if(s_count <1 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
                goto ExitError;
    end if;
    
    --ɾ������
    begin
    delete UC_CONFIG_INFO where key = n_key;
    
    delete uc_cp_charge_detail where key = n_key;
    
    exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
    end;
    --����
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
    return n_result;
  end;          
  /*********************************************************** ������Ϸ��Ϣ���� **************************************************/
/*��ȡ����������Ϸ������Ϣ
    ���أ�
           1  �ɹ�
          -1  ʧ��
*/
  function getAllLianyunConfig(
                               n_game_id    in number,   --��ϷID
                               n_cp_game_id in number,   --���������ϷID
                               n_cp_id      in number,   --�������Id
                               n_page_size  in number,   
                               n_page_index in number,
                               n_total_size out integer,
                               cur_result   out sys_refcursor
                               )return integer is
  n_result  integer  := pkg_plmgr_util.COMMON_SUCCESS;    
  s_sql varchar(2000);
  begin
    s_sql := 'SELECT uccc.game_id,sg.game_name, uccc.cp_game_id,uccc.cp_game_name, uccc.cp_id ,ucc.cp_name, uccc.pre_charge_url, uccc.cp_callback_address, uccc.state, uccc.config_time,uccc.sys_ip_id  
              FROM uc_cp_charge_config uccc 
                   LEFT JOIN  uc_cp_config         ucc ON uccc.cp_id = ucc.cp_id
                   LEFT JOIN  sys_game              sg ON uccc.game_id = sg.game_id 
              WHERE 1=1 ';
    if (n_cp_game_id is not null) then
       s_sql := s_sql || 'AND uccc.cp_game_id = ' || n_cp_game_id;
    end if;
    if (n_game_id is not null) then
      s_sql := s_sql || 'AND uccc.game_id = ' || n_game_id;
    end if;
    if (n_cp_id is not null)then
      s_sql := s_sql || 'AND uccc.cp_id = ' || n_cp_id;
    end if;
        s_sql := s_sql || 'order by uccc.game_id';
    begin
    --��ҳ��ѯ
    pkg_plmgr_util.getPageCursorBySql(s_sql,n_page_size,n_page_index,n_total_size,cur_result);
    goto ExitOK;
      
    exception when others then
      goto ExitError;
    end;
    
    --����
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
     n_result := pkg_plmgr_util.COMMON_ERROR;
    return n_result;
  end;    
/*����������Ϸ����
    ���أ�
           1  �ɹ�
          -1  ����ʧ��
       -1001  ��������
       -1002  �����ظ� 
       -1003  ��������
       -1004  ��ӦgameCode����Ϊ��
       -1005  ��Ӧ������action����Ϊ��
       -1006  ��Ӧ������cp��������Ϊ��
*/                               
   function addLianyunConfig(
                               n_game_id      in number,    --��ϷID
                               n_cp_game_id   in number,    --���������ϷID]
                               n_cp_game_name in varchar2,  --���������Ϸname
                               n_cp_id        in number,    --�������ID
                               n_sys_ip       in varchar2,  --Ԥ֧����ַ�ͻص���ַ��ipǰ׺
                               n_state        in number,    --�Ƿ���Ч
                               n_sysIPID      in number     --IPǰ׺ID
                               )return integer is
   n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS;    
   s_uccc_count          integer;
   n_action              varchar2(200);
   n_game_code           varchar2(50);
   n_cp_code             varchar2(50);
   n_pre_charge_url      varchar2(100);
   n_cp_callback_address varchar2(100);
   begin
      --�жϲ����Ƿ���ȷ
      if((n_game_id is null) or 
         (n_cp_game_id is null) or
         (n_cp_game_name is null) or 
         (n_cp_id is null) or 
         (n_state is null) or (n_state <> 1 and n_state <> 2) or
         (n_sysIPID is null)) then
          n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
          goto ExitError;
      end if;
      n_result := checkCpGameInfo(n_cp_game_id,n_cp_game_name);
      if(n_result < 1) then
        goto ExitError; 
      end if;
      --�ж��Ƿ��Ѿ����ظ������� 
      select count(*)  into s_uccc_count  from uc_cp_charge_config uccc  
      where uccc.cp_game_id = n_cp_game_id and uccc.game_id = n_game_id and uccc.cp_id = n_cp_id;
     
      --�����ظ�
      begin
        if(s_uccc_count > 0 ) then
            n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
            goto ExitError;            
        else
            --��ȡ��ַ����
            select sg.game_code into n_game_code from sys_game sg where sg.game_id = n_game_id; --��ȡgameCode
            if(n_game_code is null) then
                 n_result := PKG_PLMGR_LIANYUN_CHARGE.E_GAMECODE_NULL;
                 goto ExitError;            
            end if;
            select ucc.action_name ,ucc.cp_code into n_action,n_cp_code from uc_cp_config ucc where ucc.cp_id = n_cp_id;--��ȡcpCode,action
            if(n_action is null)then
                   n_result := PKG_PLMGR_LIANYUN_CHARGE.E_ACTION_NULL;
                   goto ExitError;          
            end if;
            if(n_cp_code is null)then
                    n_result := PKG_PLMGR_LIANYUN_CHARGE.E_CPCODE_NULL;
                    goto ExitError;  
            end if;
            --ƴ�ӵ�ַ  ip/action/gameCode/cpCode
            n_pre_charge_url :=  n_sys_ip|| '/preCharge/'|| n_cp_code;
            n_cp_callback_address :=  n_sys_ip|| '/' || n_action || '/' || n_game_code || '/' || n_cp_code;
            --��������
            insert into uc_cp_charge_config (cp_id,cp_game_id,cp_game_name,game_id,pre_charge_url,cp_callback_address,state,config_time,sys_ip_id)
                             values(n_cp_id,n_cp_game_id,n_cp_game_name,n_game_id,''||n_pre_charge_url||'',''||n_cp_callback_address||'', n_state,sysdate,n_sysIPID);
             
             --����Ĭ��key����
            declare 
             cursor uci_value is
               select uci.key,uci.name,uci.default_value 
               from uc_config_info uci 
               where uci.is_default = 1;
             begin
                 for x in uci_value loop
                   insert into uc_cp_charge_detail (cp_game_id,name,key,value,config_time)
                          values(n_cp_game_id,''||x.name||'',''||x.key||'',''|| x.default_value||'',sysdate);
                 end loop;
             end;
        end if;
        goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
      
      --����
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
      
   end;   
/*�޸�������Ϸ����
    ���أ�
           1  �ɹ�
          -1  ����ʧ��
       -1001  ��������
       -1002  �����ظ� 
       -1003  ��������
       -1004  ��ӦgameCode����Ϊ��
       -1005  ��Ӧ������action����Ϊ��
       -1006  ��Ӧ������cp��������Ϊ��
*/                               
   function updateLianyunConfig(
                               n_old_game_id      in number,    --�޸�ǰ������ϷID
                               n_old_cp_game_id   in number,    --�޸�ǰ�������������ϷID
                               n_old_cp_id        in number,    --�޸�ǰ�����������ID
                               n_game_id          in number,    --��ϷID
                               n_cp_game_id       in number,    --���������ϷID]
                               n_cp_game_name     in varchar2,  --���������Ϸname
                               n_cp_id            in number,    --�������ID
                               n_sys_ip           in varchar2,  --Ԥ֧����ַ�ͻص���ַ��ipǰ׺
                               n_state            in number,    --�Ƿ���Ч
                               n_sysIPID          in number     --IPǰ׺ID
                               )return integer is
   n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS;    
   s_uccc_count          integer default 0;
   n_action              varchar2(200);
   n_game_code           varchar2(50);
   n_cp_code             varchar2(50);
   n_pre_charge_url      varchar2(100);
   n_cp_callback_address varchar2(100);
   begin
      --�жϲ����Ƿ���ȷ
      if((n_game_id is null) or 
         (n_cp_game_id is null) or
         (n_cp_game_name is null) or 
         (n_cp_id is null) or
         (n_state is null) or
         (n_sysIPID is null)) then
          n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
          goto ExitError;
      end if;
      if(n_cp_game_id <> n_old_cp_game_id) then
                 n_result := checkCpGameInfo(n_cp_game_id,n_cp_game_name);
      end if;
      if(n_result < 1) then
        goto ExitError; 
      end if;
      --�ж��Ƿ��Ѿ����ظ������� ,���������Ϣ��֮ǰһ�����Ͳ����ж���
      if((n_game_id <> n_old_game_id) or (n_cp_id <> n_old_cp_id) or (n_cp_game_id <> n_old_cp_game_id)) then
          select count(*)  into s_uccc_count  from uc_cp_charge_config uccc  
          where uccc.cp_game_id = n_cp_game_id and uccc.game_id = n_game_id and uccc.cp_id = n_cp_id;
      end if;
      --�����ظ�
      begin
        if(s_uccc_count > 0 ) then
            n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
            goto ExitError;            
        else
            --��ȡ��ַ����
            select sg.game_code into n_game_code from sys_game sg where sg.game_id = n_game_id; --��ȡgameCode
            if(n_game_code is null) then
                 n_result := PKG_PLMGR_LIANYUN_CHARGE.E_GAMECODE_NULL;
                 goto ExitError;            
            end if;
            select ucc.action_name ,ucc.cp_code into n_action,n_cp_code from uc_cp_config ucc where ucc.cp_id = n_cp_id;--��ȡcpCode,action
            if(n_action is null)then
                   n_result := PKG_PLMGR_LIANYUN_CHARGE.E_ACTION_NULL;
                   goto ExitError;          
            end if;
            if(n_cp_code is null)then
                    n_result := PKG_PLMGR_LIANYUN_CHARGE.E_CPCODE_NULL;
                    goto ExitError;  
            end if;
            --ƴ�ӵ�ַ  ip/action/gameCode/cpCode
            n_pre_charge_url :=  n_sys_ip|| '/preCharge/'|| n_cp_code;
            n_cp_callback_address :=  n_sys_ip|| '/' || n_action || '/' || n_game_code || '/' || n_cp_code;
            --��������
            update uc_cp_charge_config 
            set cp_game_id = n_cp_game_id,cp_game_name = n_cp_game_name,cp_id= n_cp_id,
                game_id = n_game_id,pre_charge_url = n_pre_charge_url,
                cp_callback_address = n_cp_callback_address,state = n_state,sys_ip_id = n_sysIPID 
            where cp_game_id = n_old_cp_game_id and game_id = n_old_game_id and cp_id = n_old_cp_id;
            
            update uc_cp_charge_detail set cp_game_id = n_cp_game_id where cp_game_id = n_old_cp_game_id ;
         
        end if;
        goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
      
      --����
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
      
   end;   
/*����uc_cp_charge_config��cp_callback_address��pre_charge_url��������cpCode actionCode �ı䣬������Ϸ gameCode�ı�
      ���� ��
           1  �ɹ�
          -1  ����ʧ��
       -1004  ��ӦgameCode����Ϊ��
       -1005  ��Ӧ������action����Ϊ��
       -1006  ��Ӧ������cp��������Ϊ��                                                                 
*/
   function updateCPURLForGameOrCpUpd(
                                  n_game_id    in number,  --gameCode�ı��game_id
                                  n_cp_id      in number   --cpCode actionCode �ı��cp_id
                                  )return integer is
    n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS; 
    s_address               varchar2(200);
    s_url                   varchar2(200);
    begin
      begin
          if(n_game_id is not null) then
                --���¶�ӦgameID��URL
                declare 
                 cursor uccc_value_for_game_id is
                     SELECT uccc.game_id,uccc.cp_game_id,uccc.cp_id,uccc.cp_callback_address
                     FROM uc_cp_charge_config uccc 
                     WHERE uccc.game_id = n_game_id;
                 begin
                     for x in uccc_value_for_game_id loop
                         --��ȡ�޸ĺ�ĵ�ַ��������쳣���׳�
                         n_result := getURLById(x.game_id,x.cp_id, x.cp_callback_address,'',s_url,s_address);
                         if(n_result < 0) then
                             goto ExitError;
                         end if;
                         --����url
                         update uc_cp_charge_config set cp_callback_address = s_address,pre_charge_url = s_url
                         where game_id = x.game_id and cp_game_id = x.cp_game_id and cp_id = x.cp_id;
                     end loop;
                 end;     
          end if;
          
          if(n_cp_id is not null) then
                --���¶�ӦcpID��URL
                declare 
                 cursor uccc_value_for_cp_id is
                     SELECT uccc.game_id,uccc.cp_game_id,uccc.cp_id,uccc.cp_callback_address
                     FROM uc_cp_charge_config uccc 
                     WHERE uccc.cp_id = n_cp_id;
                 begin
                     for x in uccc_value_for_cp_id loop
                         --��ȡ�޸ĺ�ĵ�ַ��������쳣���׳�
                         n_result := getURLById(x.game_id,x.cp_id, x.cp_callback_address,'',s_url,s_address);
                         if(n_result < 0) then
                             goto ExitError;
                         end if;
                         --����url
                         update uc_cp_charge_config set cp_callback_address = s_address,pre_charge_url = s_url
                         where game_id = x.game_id and cp_game_id = x.cp_game_id and cp_id = x.cp_id;
                     end loop;
                 end;     
          end if;
      
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
      
      --����
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
      
    end;   
/**������Ϣ��ȡƴ�ӵ�URL
    ���أ�
           1  �ɹ�
          -1  ����ʧ��
       -1004  ��ӦgameCode����Ϊ��
       -1005  ��Ӧ������action����Ϊ��
       -1006  ��Ӧ������cp��������Ϊ��  
    
*/                           
    function getURLById(
                        n_game_id    in number,        
                        n_cp_id      in number,
                        n_url        in varchar2,       --һ����IPǰ׺��������uc_cp_charge_config�е�URL
                        v_ip_address in varchar2,       --IP ǰ׺
                        n_pre_charge_url         out varchar2,
                        n_cp_callback_address    out varchar2
                        )return integer is
      n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS;  
      n_action              varchar2(200);
      n_game_code           varchar2(50);
      n_cp_code             varchar2(50);    
      s_ip                  varchar2(100); 
     begin
         begin
              if((v_ip_address is not null)) then
                  s_ip := v_ip_address;
              else
                
                  --��ȡipǰ׺
                  s_ip := SUBSTR (n_url,  0,  INSTR (n_url, '/', -1) - 1);
                  s_ip := SUBSTR (s_ip,   0,  INSTR (s_ip,  '/', -1) - 1);
                  s_ip := SUBSTR (s_ip,   0,  INSTR (s_ip,  '/', -1) - 1);
              end if;    
               --��ȡ��ַ����
                select sg.game_code into n_game_code from sys_game sg where sg.game_id = n_game_id; --��ȡgameCode
                if(n_game_code is null) then
                     n_result := PKG_PLMGR_LIANYUN_CHARGE.E_GAMECODE_NULL;
                     goto ExitError;            
                end if;
                select ucc.action_name ,ucc.cp_code into n_action,n_cp_code from uc_cp_config ucc where ucc.cp_id = n_cp_id;--��ȡcpCode,action
                if(n_action is null)then
                       n_result := PKG_PLMGR_LIANYUN_CHARGE.E_ACTION_NULL;
                       goto ExitError;          
                end if;
                if(n_cp_code is null)then
                        n_result := PKG_PLMGR_LIANYUN_CHARGE.E_CPCODE_NULL;
                        goto ExitError;  
                end if;
                --ƴ�ӵ�ַ  ip/action/gameCode/cpCode
                n_pre_charge_url :=  s_ip|| '/preCharge/'|| n_cp_code;
                n_cp_callback_address :=  s_ip|| '/' || n_action || '/' || n_game_code || '/' || n_cp_code;
                goto ExitOK;
          exception when others then
            n_result := pkg_plmgr_util.COMMON_ERROR;
            goto ExitError;
          end;
            
          --����
          <<ExitOK>>
             n_result := pkg_plmgr_util.COMMON_SUCCESS;   
             return n_result;
          <<ExitError>>    
             return n_result;
     end;                              
/*ɾ��������Ϸ����
   ���� ��
      1    �ɹ�
     -1    ʧ��
  -1001    ��������
  -1003    ��������Ҫ����������
*/   
   function deleteLianyunConfig(
                               n_game_id      in number,    --��ϷID
                               n_cp_game_id   in number,    --���������ϷID
                               n_cp_id        in number     --�������ID
                               ) return integer is
   s_count               integer;
   n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS; 
   begin 
     --��֤����
     if((n_game_id is null) or 
        (n_cp_game_id is null) or 
        (n_cp_id is null)) then
         n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
         goto ExitError;
     end if;
     select count(*) into s_count from uc_cp_charge_config where game_id = n_game_id and cp_game_id = n_cp_game_id and cp_id = n_cp_id;
     if(s_count < 1) then
         n_result := PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
         goto ExitError;
     end if;
     --ɾ������
     begin
       delete uc_cp_charge_config where game_id = n_game_id and cp_game_id = n_cp_game_id and cp_id = n_cp_id;
       delete uc_cp_charge_detail where cp_game_id = n_cp_game_id;
       goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
     --����
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
   end;
/*����������Ϸ���á��������Ƿ���Ч
      1    �ɹ�
     -1    ʧ��
  -1001    ��������
  -1003    ��������Ҫ����������
*/
   function updateLianyunConfigState(
                                       n_game_id      in number,    --��ϷID
                                       n_cp_game_id   in number,    --���������ϷID
                                       n_cp_id        in number,    --�������ID
                                       n_state        in number     --�Ƿ���Ч
                                     )return integer is
   s_count               integer;
   n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS;                                      
   begin
     --��֤����
     if((n_game_id is null) or 
        (n_cp_game_id is null) or 
        (n_cp_id is null) or
        (n_state is null)) then
         n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
         goto ExitError;
     end if;
     select count(*) into s_count from uc_cp_charge_config where game_id = n_game_id and cp_game_id = n_cp_game_id and cp_id = n_cp_id;
     if(s_count < 1) then
         n_result := PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
         goto ExitError;
     end if;
     --����״̬
     begin
         update uc_cp_charge_config set state = n_state where game_id = n_game_id and cp_game_id = n_cp_game_id and cp_id = n_cp_id;
         goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
     --����
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
   end;
/*����ID��ȡ��Ϸvalue������Ϣ
   ����
         1    �ɹ�
        -1    ʧ��
     -1001    ��������
*/
   function getLianyunConfigDetailByID(
                                  n_cp_game_id   in  number,
                                  ur_result      out sys_refcursor
                                  )return integer is
   n_result       integer  := pkg_plmgr_util.COMMON_SUCCESS;  
   s_sql varchar(200);                           
   begin
     --�жϲ���
     if(n_cp_game_id is null) then
         n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
         goto ExitError;
     end if;
     --��ѯ����
     begin
       s_sql := 'select uccd.name,uccd.key,uccd.value from uc_cp_charge_detail uccd where uccd.cp_game_id =' || n_cp_game_id ;
       open ur_result for s_sql;
       goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
      --����
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
   end;   
/*ɾ����Ϸvalue������Ϣ
 ���� 
         1    �ɹ�
        -1    ʧ��
     -1001    ��������
*/
   function deleteLianyunConfigDetail(
                                      n_cp_game_id   in  number,
                                      n_key          in  varchar2
                                      )return integer is
   n_result       integer  := pkg_plmgr_util.COMMON_SUCCESS;        
   begin
     --�жϲ���
     if((n_cp_game_id is null) or (n_key is null))then
        n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
        goto ExitError;
     end if;
     --ɾ������
     begin 
         delete uc_cp_charge_detail where cp_game_id = n_cp_game_id and key = n_key;
         goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
      --����
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;   
   end;
/*������Ϸvalue������Ϣ
 ���� 
         1    �ɹ�
        -1    ʧ��
     -1001    ��������
     -1003    ������key
*/   
   function addLianyunConfigDetail(
                                    n_cp_game_id   in  number,
                                    n_key          in  varchar2,
                                    n_name         in  varchar2,
                                    n_value        in  varchar2 
                                   )return integer is
   n_result       integer  := pkg_plmgr_util.COMMON_SUCCESS;      
   s_count        integer; 
   begin
    --�жϲ���
     if((n_cp_game_id is null) or (n_key is null) or
        (n_name is null) or (n_value is null) )then
        n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
        goto ExitError;
     end if;
     select count(*) into s_count from uc_config_info uci where uci.key = n_key and uci.name = n_name;
     if(s_count < 1) then
        n_result := PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
        goto ExitError;        
     end if;
     --��������
     begin 
         insert into uc_cp_charge_detail (cp_game_id,name,key,value,config_time)
                          values(n_cp_game_id,n_name,n_key,n_value,sysdate);
         goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
      --����
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;     
   end;
 /*�ж������game_id �� game_name�Ƿ�����
    ���� 
       1   ����
   -1001   ������
   -1003   ��������
 */
   function checkCpGameInfo(
                            n_cp_game_id    in number,
                            n_cp_game_name  in varchar2
                            )return integer is
   n_result       integer  := pkg_plmgr_util.COMMON_SUCCESS;      
   s_count        integer;
   s_cp_game_name varchar2(50);
   begin
        select count(*) into s_count from uc_cp_charge_config uci where uci.cp_game_id = n_cp_game_id;    
        if(s_count >1) then   
          n_result := PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
        else 
          if(s_count > 0) then
             select uci.cp_game_name into s_cp_game_name from uc_cp_charge_config uci where uci.cp_game_id = n_cp_game_id;
             if(n_cp_game_name <> s_cp_game_name)then
               n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
             end if;
           end if;
        end if;
        return n_result;
   end;
/*��ȡ����������Ϸ
   ���� 
       1   �ɹ�
*/
   function getAllCPGameInfo(
                             ur_result      out sys_refcursor
                             )return integer is
   n_result       integer  := pkg_plmgr_util.COMMON_SUCCESS;    
   n_sql          varchar(200);  
   begin
     n_sql := 'select uccc.cp_game_id,uccc.cp_game_name from uc_cp_charge_config uccc order by uccc.cp_game_id';
     open ur_result for n_sql;
     return n_result;
   end;  
/*��ȡ����ʹ�õ�Ipǰ׺
   ���� 
       1   �ɹ�
*/   
   function getAllUseSysIP(
                             ur_result      out sys_refcursor
                             )return integer is
   n_result       integer  := pkg_plmgr_util.COMMON_SUCCESS;    
   n_sql          varchar(200);  
   begin
     n_sql := 'select usia.sys_ip ,usia.sys_ip_id from uc_sys_ip_address usia where usia.is_use = 1 order by usia.sys_ip' ;
     open ur_result for n_sql;
     return n_result;
   end; 
/*��ȡ����Ipǰ׺
   ���� 
       1   �ɹ�
*/   
   function getAllSysIP(
                             ur_result      out sys_refcursor
                             )return integer is
   n_result       integer  := pkg_plmgr_util.COMMON_SUCCESS;    
   n_sql          varchar(200);  
   begin
     n_sql := 'select usia.sys_ip_id,usia.sys_ip,usia.comments,usia.is_use from uc_sys_ip_address usia order by usia.sys_ip' ;
     open ur_result for n_sql;
     return n_result;
   end;
/*����һ��IPǰ׺
   -1001   ��������
   -1002   ip�ظ�
   1       �ɹ�
*/
   function addSysIP(
                        v_sys_ip    varchar2,
                        v_comments  varchar2,
                        n_is_use    number
      )return integer is
   n_sys_ip_id    integer  :=0; 
   n_count        integer  :=0;
   begin
     if((v_sys_ip is null) or 
         (n_is_use is null) or (n_is_use <> 1 and n_is_use <> 2)) then
          return PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
     end if;
     --����Ƿ��ظ�
     select count(1) into n_count  from uc_sys_ip_address where sys_ip = v_sys_ip;
     if (n_count > 0 ) then
       return PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
     end if;
     --��������
     select t.sys_ip_id into n_sys_ip_id from (
            select sys_ip_id ,rownum
            from uc_sys_ip_address
            order by sys_ip_id desc
            ) t where rownum=1;
     if(n_sys_ip_id is null) then
          n_sys_ip_id := 1;
     else
          n_sys_ip_id := n_sys_ip_id + 1;
     end if;
     
     insert into uc_sys_ip_address (sys_ip_id,sys_ip,comments,is_use)
       values(n_sys_ip_id,v_sys_ip,v_comments,n_is_use);
     return pkg_plmgr_util.COMMON_SUCCESS;
   end; 
 /*�޸�һ��IPǰ׺
   -1001   ��������
   -1002   ip�ظ�  
   -1003   ���������ݲ�����
   1       �ɹ�
*/  
   function updateSysIP(
                        n_sys_ip_id number,
                        v_sys_ip    varchar2,
                        v_comments  varchar2,
                        n_is_use    number
      )return integer is
   n_count        integer       :=0;
   v_old_IP       varchar2(200) :='';
   n_result       integer       := pkg_plmgr_util.COMMON_SUCCESS;   
    s_address     varchar2(200);
    s_url         varchar2(200);
   begin
     if((n_sys_ip_id is null) or
         (v_sys_ip is null) or 
         (n_is_use is null) or (n_is_use <> 1 and n_is_use <> 2)) then
          return PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
     end if;
     --����Ƿ������Ҫ�޸ĵ�����
     select count(1) into n_count  from uc_sys_ip_address where sys_ip_id = n_sys_ip_id;
     if(n_count < 1) then
       return PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
     end if;
     --����Ƿ��ظ�
     select count(1) into n_count  from uc_sys_ip_address where sys_ip = v_sys_ip and sys_ip_id != n_sys_ip_id;
     if (n_count > 0 ) then
       return PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
     end if;
     
     --�޸��Ѿ�ʹ�ø�Ipǰ׺������
     select sys_ip into v_old_IP from uc_sys_ip_address where  sys_ip_id = n_sys_ip_id;
     if(v_old_IP <> v_sys_ip) then
                --���¶�ӦcpID��URL
                declare 
                 cursor uccc_value_for_sys_ip_id is
                     SELECT uccc.game_id,uccc.cp_game_id,uccc.cp_id,uccc.pre_charge_url
                     FROM uc_cp_charge_config uccc 
                     WHERE uccc.sys_ip_id = n_sys_ip_id;
                 begin
                     for x in uccc_value_for_sys_ip_id loop
                         --��ȡ�޸ĺ�ĵ�ַ��������쳣���׳�
                         n_result := getURLById(x.game_id,x.cp_id, v_sys_ip,v_sys_ip,s_url,s_address);
                         if(n_result < 0) then
                             return pkg_plmgr_util.COMMON_ERROR;
                         end if;
                         --����url
                         update uc_cp_charge_config set cp_callback_address = s_address,pre_charge_url = s_url
                         where game_id = x.game_id and cp_game_id = x.cp_game_id and cp_id = x.cp_id and sys_ip_id = n_sys_ip_id;
                     end loop;
                 end;     
     
     end if;
     
     --��������
     update uc_sys_ip_address set sys_ip = v_sys_ip,comments = v_comments,is_use = n_is_use
     where sys_ip_id = n_sys_ip_id;

     return pkg_plmgr_util.COMMON_SUCCESS;
   end; 
/*ɾ��IPǰ׺
   -1001   ��������
   1       �ɹ�
*/
   function deleteSysIP(
                        n_sys_ip_id number
      )return integer is
   begin
     if( n_sys_ip_id is null) then
          return PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
     end if;
      delete uc_sys_ip_address where  sys_ip_id = n_sys_ip_id;

     return pkg_plmgr_util.COMMON_SUCCESS;
   end;    
end PKG_PLMGR_LIANYUN_CHARGE;
