/*
	修改以下存储过程函数
*/ 



create or replace package PKG_PLMGR_LIANYUN_CHARGE is
----------------------------------------------------------------------返回值
  -- 参数有误
  E_PARAM_ERROR constant integer :=                 -1001;

  -- 名（名称）重复
  E_REPEAT      constant integer :=                 -1002;

  --数据有误
  E_DATA_ERROR  constant integer :=                 -1003;

  --对应gameCode配置为空
  E_GAMECODE_NULL constant integer :=               -1004;

  --对应渠道的action配置为空
  E_ACTION_NULL constant integer :=                 -1005;

  --对应渠道的cp编码配置为空
  E_CPCODE_NULL constant integer :=                 -1006;
-----------------------------------------------------------------------变量
 --预支付地址action
 PRE_CHARGE_ACTION constant varchar2(50) :=         'preCharge';


/*************************************************联运渠道配置************************************************/
--获取所有符合cpID的联运渠道配置（分页\模糊查询）
  procedure getAllLianyunChannelConfig(
                               n_cp_name    in varchar2,
                               n_page_size  in number,
                               n_page_index in number,
                               n_total_size out integer,
                               cur_result   out sys_refcursor
                               );
--根据ID获取联运渠道配置
  procedure getLianyunChannelConfigByCpId(
                               n_cp_id      in integer,
                               cur_result   out sys_refcursor
                              );
/*获取所有渠道信息
   返回
       1   成功
*/
   function getAllCPInfo(
                             ur_result      out sys_refcursor
                             )return integer;
/*增加联运渠道配置
  返回：  1  成功
         -1  增加数据失败
      -1001  参数错误
      -1002  渠道名重复
*/
  function insertLianyunChannelConfig(
                                n_cp_name      in varchar2,
                                n_state        in number,
                                n_cp_code      in varchar2,
                                n_action_name  in varchar2
                               	)return integer;
/*修改渠道配置

    返回：
          1  成功
         -1  修改数据失败
      -1001  参数错误
      -1002  渠道名重复
      -1003  不存在需要修改的渠道
*/
  function updateLianyunChannelConfig(
                                n_cp_id        in number,
                                n_cp_name      in varchar2,
                                n_state        in number,
                                n_cp_code      in varchar2,
                                n_action_name  in varchar2
                               	)return integer;
 /*************************************************联运上线参数名配置************************************************/
/*获取所有符合Key的联运参数名信息（分页\模糊查询）     UC_CONFIG_INFO
   返回：
          1  成功
         -1  失败
*/
  function getAllLianyunParamConfig(
                               n_key        in varchar2,
                               n_page_size  in number,
                               n_page_index in number,
                               n_total_size out integer,
                               cur_result   out sys_refcursor
                               )return integer;
/*获取所有联运参数名信息
   返回：
          1  成功
         -1  失败
*/
  function getAllLianyunParam(
                               cur_result   out sys_refcursor
                               )return integer;
/*根据key获取相应的联运参数名信息
   返回：
          1  成功
         -1  失败
*/
 function getLianyunParamConfigByKey(
                               n_key         in varchar2,
                               cur_result    out sys_refcursor
                               )return integer;
/*增加联运参数名配置
  返回：  1  成功
         -1  增加数据失败
      -1001  参数错误
      -1002  参数名重复
*/
  function insertLianyunParamConfig(
                                n_key           in varchar2,
                                n_name          in varchar2,
                                n_default_value in varchar2,
                                n_is_default    in varchar2,
                                n_type          in number
                               	)return integer;
/*修改联运参数名配置

    返回：
          1  成功
         -1  修改数据失败
      -1001  参数错误
      -1002  参数名重复
      -1003  不存在需要修改的参数名
*/
  function updateLianyunParamConfig(
                                n_old_key      in varchar2,
                                n_key          in varchar2,
                                n_name         in varchar2,
                                n_default_value in varchar2,
                                n_is_default    in varchar2,
                                n_type          in number
                               	)return integer;
/*删除联运参数名配置
  返回：
          1  成功
         -1  删除数据失败
      -1003  不存在需要删除的参数名
*/
  function deleteLianyunParamConfig(
                                n_key      in varchar2
                               	)return integer;
/*********************************************************** 渠道游戏信息配置 **************************************************/
/*获取所有渠道游戏配置信息
    返回：
           1  成功
          -1  失败
*/
  function getAllLianyunConfig(
                               n_game_id    in number,   --游戏ID
                               n_cp_game_id in number,   --合作伙伴游戏ID
                               n_cp_id      in number,   --合作伙伴ID
                               n_page_size  in number,
                               n_page_index in number,
                               n_total_size out integer,
                               cur_result   out sys_refcursor
                               )return integer;
/*增加渠道游戏配置
    返回：
           1  成功
          -1  操作失败
*/
   function addLianyunConfig(
                               n_game_id      in number,    --游戏ID
                               n_cp_game_id   in number,    --合作伙伴游戏ID]
                               n_cp_game_name in varchar2,  --合作后伴游戏name
                               n_cp_id        in number,    --合作伙伴ID
                               n_sys_ip       in varchar2,  --预支付地址和回调地址的ip前缀
                               n_state        in number,    --是否生效
                               n_sysIPID      in number     --IP前缀ID
                               )return integer;
/*修改渠道游戏配置
    返回：
           1  成功
          -1  操作失败
       -1001  参数有误
       -1002  数据重复
       -1003  数据有误
*/
   function updateLianyunConfig(
                               n_old_game_id      in number,    --修改前——游戏ID
                               n_old_cp_game_id   in number,    --修改前——合作伙伴游戏ID
                               n_old_cp_id        in number,    --修改前——合作伙伴ID
                               n_game_id          in number,    --游戏ID
                               n_cp_game_id       in number,    --合作伙伴游戏ID]
                               n_cp_game_name     in varchar2,  --合作后伴游戏name
                               n_cp_id            in number,    --合作伙伴ID
                               n_sys_ip           in varchar2,  --预支付地址和回调地址的ip前缀
                               n_state            in number,    --是否生效
                               n_sysIPID          in number     --IP前缀ID
                               )return integer;
/*更新uc_cp_charge_config中cp_callback_address和pre_charge_url，因渠道cpCode actionCode 改变，或因游戏 gameCode改变
      返回 ：
           1  成功
          -1  操作失败
       -1004  对应gameCode配置为空
       -1005  对应渠道的action配置为空
       -1006  对应渠道的cp编码配置为空
*/
   function updateCPURLForGameOrCpUpd(
                                  n_game_id    in number,  --gameCode改变的game_id
                                  n_cp_id      in number   --cpCode actionCode 改变的cp_id
                                  )return integer;
/**根据信息获取拼接的URL
    返回：
           1  成功
          -1  操作失败
       -1004  对应gameCode配置为空
       -1005  对应渠道的action配置为空
       -1006  对应渠道的cp编码配置为空

*/
    function getURLById(
                        n_game_id    in number,
                        n_cp_id      in number,
                        n_url        in varchar2,       --一个带IP前缀的完整的uc_cp_charge_config中的URL
                        v_ip_address in varchar2,
                        n_pre_charge_url         out varchar2,
                        n_cp_callback_address    out varchar2
                        )return integer;
/*删除渠道游戏配置
   返回 ：
      1    成功
     -1    失败
  -1001    参数有误
  -1003    不存在需要操作的数据
*/
   function deleteLianyunConfig(
                               n_game_id      in number,    --游戏ID
                               n_cp_game_id   in number,    --合作伙伴游戏ID
                               n_cp_id        in number     --合作伙伴ID
                               ) return integer;
/*更新渠道游戏配置————是否生效
      1    成功
     -1    失败
  -1001    参数有误
  -1003    不存在需要操作的数据
*/
   function updateLianyunConfigState(
                                       n_game_id      in number,    --游戏ID
                                       n_cp_game_id   in number,    --合作伙伴游戏ID
                                       n_cp_id        in number,    --合作伙伴ID
                                       n_state        in number     --是否生效
                                     )return integer;
/*根据ID获取游戏value配置信息
   返回
         1    成功
        -1    失败
     -1001    参数有误
*/
   function getLianyunConfigDetailByID(
                                  n_cp_game_id   in  number,
                                  ur_result      out sys_refcursor
                                  )return integer;
/*删除游戏value配置信息
 返回
         1    成功
        -1    失败
     -1001    参数有误
*/
   function deleteLianyunConfigDetail(
                                      n_cp_game_id   in  number,
                                      n_key          in  varchar2
                                      )return integer;

/*增加游戏value配置信息
 返回
         1    成功
        -1    失败
     -1001    参数有误
     -1003    不存在key
*/
   function addLianyunConfigDetail(
                                    n_cp_game_id   in  number,
                                    n_key          in  varchar2,
                                    n_name         in  varchar2,
                                    n_value        in  varchar2
                                   )return integer;

/*判断输入的game_id 和 game_name是否正常
    返回
       1   正常
   -1001   不正常
   -1003   数据有误
 */
   function checkCpGameInfo(
                            n_cp_game_id    in number,
                            n_cp_game_name  in varchar2
                            )return integer;
 /*获取所有联运游戏
   返回
       1   成功
*/
   function getAllCPGameInfo(
                             ur_result      out sys_refcursor
                             )return integer;
 /*获取所有使用的Ip前缀
   返回
       1   成功
*/
   function getAllUseSysIP(
                             ur_result      out sys_refcursor
                             )return integer;
/*获取所有Ip前缀
   返回
       1   成功
*/
   function getAllSysIP(
                             ur_result      out sys_refcursor
                             )return integer;                            
/*增加一个IP前缀
   -1001   参数错误
   -1002   ip重复
   1       成功
*/
   function addSysIP(
                        v_sys_ip    varchar2,
                        v_comments  varchar2,
                        n_is_use    number
                        )return integer;
 /*修改一个IP前缀
   -1001   参数错误
   -1002   ip重复
   -1003   操作的数据不存在
   1       成功
*/  
   function updateSysIP(
                        n_sys_ip_id number,
                        v_sys_ip    varchar2,
                        v_comments  varchar2,
                        n_is_use    number
                        )return integer;       
 /*删除IP前缀
   -1001   参数错误
   1       成功
*/
   function deleteSysIP(
                        n_sys_ip_id number
      )return integer;                                       
end PKG_PLMGR_LIANYUN_CHARGE;










 create or replace package body PKG_PLMGR_LIANYUN_CHARGE is
/*************************************************联运渠道配置************************************************/
/*获取所有符合cpID的联运渠道配置（分页\模糊查询）*/
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
/*根据ID获取联运渠道配置*/
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
  
/*获取所有渠道信息
   返回 
       1   成功
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
/*增加联运渠道配置 
  返回：  1  成功
         -1  增加数据失败
      -1001  参数错误
      -1002  渠道名重复
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
    --判断参数
    if((n_cp_name is null) or (n_state is null)) then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
                goto ExitError;
    end if;    
    --判断渠道名是否重复
    select count(*) into s_count from UC_CP_CONFIG where cp_name = n_cp_name;
    if(s_count >0 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
                goto ExitError;
    end if;
    
    --增加配置
    begin
    insert into UC_CP_CONFIG(cp_id,cp_name,config_time,state,cp_code,action_name) values(sequen_sys_project_id.nextval,n_cp_name,sysdate,n_state,n_cp_code,n_action_name);
    goto  ExitOK;
    
    exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
    end;
    --返回
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
    return n_result;
  end;
  /*修改渠道配置
  
    返回：  
          1  成功
         -1  修改数据失败
      -1001  参数错误
      -1002  渠道名重复
      -1003  不存在需要修改的渠道
      -1004  对应gameCode配置为空
      -1005  对应渠道的action配置为空
      -1006  对应渠道的cp编码配置为空  
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
    --判断参数
    if((n_cp_name is null) or (n_state is null)) then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
                goto ExitError;
    end if;   
    --判断是否存在要修改的渠道
    select count(*) into s_count from UC_CP_CONFIG where cp_id = n_cp_id;
    if(s_count <1 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
                goto ExitError;
    end if;
     
    --判断渠道名是否重复
    select count(*) into s_count from UC_CP_CONFIG where cp_name = n_cp_name and cp_id <> n_cp_id;
    if(s_count >0 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
                goto ExitError;
    end if;
    
    begin
    
    --根据条件修改uc_cp_charge_config表中对应的URL
    --查询是否修改了cp_code 或 action_name
    select cp_code,action_name into s_cp_code,s_action_name from UC_CP_CONFIG where cp_id = n_cp_id;
    if(s_cp_code <> n_cp_code or s_action_name <> n_action_name or (n_cp_code is null and s_cp_code is not null) or (n_action_name is null and s_action_name is not null)) then
         --如果是，查询是否已经有该渠道的cp_charge_config的配置
         select count(*) into s_count from uc_cp_charge_config where cp_id = n_cp_id;
         if(s_count > 0) then
             --如果是，则不允许cp_code或action_name修改为null
             if(n_cp_code is null)then
                 n_result := PKG_PLMGR_LIANYUN_CHARGE.E_CPCODE_NULL;
                 goto ExitError;
             end if;
             if(n_action_name is null)then
                 n_result := PKG_PLMGR_LIANYUN_CHARGE.E_ACTION_NULL;
                 goto ExitError;
             end if;
            --修改渠道配置
            update UC_CP_CONFIG set cp_name = n_cp_name,state = n_state,cp_code = n_cp_code,action_name = n_action_name
            where cp_id = n_cp_id;
             --并且需要将对应的uc_cp_charge_config 的地址刷新
             n_result := updateCPURLForGameOrCpUpd(null,n_cp_id);
             if(n_result < 0) then
                  goto ExitError;
             end if;
          else
             --修改渠道配置
            update UC_CP_CONFIG set cp_name = n_cp_name,state = n_state,cp_code = n_cp_code,action_name = n_action_name
            where cp_id = n_cp_id;
          end if;
    else
       --修改渠道配置
      update UC_CP_CONFIG set cp_name = n_cp_name,state = n_state,cp_code = n_cp_code,action_name = n_action_name
      where cp_id = n_cp_id;
    end if;
    
    
   
    
    exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
    end;
    --返回
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
    return n_result;
  end;
 /*************************************************联运上线参数名配置************************************************/  
/*获取所有符合Key的联运参数名信息（分页\模糊查询）、
   返回：  
          1  成功
         -1  失败
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
    --分页查询
    pkg_plmgr_util.getPageCursorBySql(s_sql,n_page_size,n_page_index,n_total_size,cur_result);
    goto ExitOK;
      
    exception when others then
      goto ExitError;
    end;
    
    --返回
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
     n_result := pkg_plmgr_util.COMMON_ERROR;
    return n_result;
  end;   
/*获取所有联运参数名信息
   返回：  
          1  成功
         -1  失败
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
    
    --返回
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
     n_result := pkg_plmgr_util.COMMON_ERROR;
    return n_result;
  end;   
/*根据key获取相应的联运参数名信息
   返回：  
          1  成功
         -1  失败
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
    
    --返回
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
     n_result := pkg_plmgr_util.COMMON_ERROR;
    return n_result;
  end;     
/*增加联运参数名配置 
  返回：  1  成功
         -1  增加数据失败
      -1001  参数错误
      -1002  参数名重复
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
    --判断参数
    if((n_key is null) or (n_name is null) or(n_is_default is null) or(n_is_default = 1 and n_default_value is null) or(n_type is null)) then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
                goto ExitError;
    end if;    
    --判断联运参数名是否重复
    select count(*) into s_count from UC_CONFIG_INFO where key = n_key;
    if(s_count >0 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
                goto ExitError;
    end if;
    
    --增加配置
    begin
    --insert into UC_CONFIG_INFO(key,name,default_value,is_default,UC_CONFIG_INFO.TYPE) values(n_key,n_name,n_default_value,n_is_default,n_type);
    insert into UC_CONFIG_INFO(key,name,default_value,is_default,type) values(n_key,n_name,n_default_value,n_is_default,n_type);

    goto  ExitOK;
    
    exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
    end;
    --返回
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
    return n_result;
  end;    
/*修改联运参数名配置
  
    返回：  
          1  成功
         -1  修改数据失败
      -1001  参数错误
      -1002  参数名重复
      -1003  不存在需要修改的参数名
      -1004  此为默认key，key值不可修改
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
    --判断参数
    if((n_key is null) or (n_name is null) or (n_type is null) or (n_old_key  is null) or(n_is_default is null) or(n_is_default = 1 and n_default_value is null) ) then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
                goto ExitError;
    end if;   
    --判断是否存在要修改的渠道
    select count(*) into s_count from UC_CONFIG_INFO where key = n_old_key;
    if(s_count <1 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
                goto ExitError;
    end if;
     
    --判断渠道名是否重复
    select count(*) into s_count from UC_CONFIG_INFO where key = n_key and key <> n_old_key;
    if(s_count >0 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
                goto ExitError;
    end if;
    
    --修改配置
    begin
    
   -- update UC_CONFIG_INFO  set key = n_key, name = n_name, default_value = n_default_value, is_default = n_is_default, type = n_type
    update UC_CONFIG_INFO  set key = n_key, name = n_name, default_value = n_default_value, is_default = n_is_default, type = n_type

    where key = n_old_key;--修改KeyName
    
    update uc_cp_charge_detail set key = n_key
    where key = n_old_key;--修改已配置的OldKey值改为key
    
    exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
    end;
    --返回
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
    return n_result;
  end; 
/*删除联运参数名配置
  返回：
          1  成功
         -1  删除数据失败
      -1003  不存在需要删除的参数名                           
*/

  function deleteLianyunParamConfig(
                                n_key      in varchar2
                                 )return integer is
  n_result  integer  := pkg_plmgr_util.COMMON_SUCCESS; 
  s_count   integer;                            
  begin
    --判断参数
    if(n_key is null) then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
                goto ExitError;
    end if;   
    --判断是否存在要删除的渠道
    select count(*) into s_count from UC_CONFIG_INFO where key = n_key;
    if(s_count <1 )then
                n_result := PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
                goto ExitError;
    end if;
    
    --删除配置
    begin
    delete UC_CONFIG_INFO where key = n_key;
    
    delete uc_cp_charge_detail where key = n_key;
    
    exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
    end;
    --返回
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
    return n_result;
  end;          
  /*********************************************************** 渠道游戏信息配置 **************************************************/
/*获取所有渠道游戏配置信息
    返回：
           1  成功
          -1  失败
*/
  function getAllLianyunConfig(
                               n_game_id    in number,   --游戏ID
                               n_cp_game_id in number,   --合作伙伴游戏ID
                               n_cp_id      in number,   --合作伙伴Id
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
    --分页查询
    pkg_plmgr_util.getPageCursorBySql(s_sql,n_page_size,n_page_index,n_total_size,cur_result);
    goto ExitOK;
      
    exception when others then
      goto ExitError;
    end;
    
    --返回
    <<ExitOK>>
    n_result := pkg_plmgr_util.COMMON_SUCCESS;   
    return n_result;
    <<ExitError>>
     n_result := pkg_plmgr_util.COMMON_ERROR;
    return n_result;
  end;    
/*增加渠道游戏配置
    返回：
           1  成功
          -1  操作失败
       -1001  参数有误
       -1002  数据重复 
       -1003  数据有误
       -1004  对应gameCode配置为空
       -1005  对应渠道的action配置为空
       -1006  对应渠道的cp编码配置为空
*/                               
   function addLianyunConfig(
                               n_game_id      in number,    --游戏ID
                               n_cp_game_id   in number,    --合作伙伴游戏ID]
                               n_cp_game_name in varchar2,  --合作后伴游戏name
                               n_cp_id        in number,    --合作伙伴ID
                               n_sys_ip       in varchar2,  --预支付地址和回调地址的ip前缀
                               n_state        in number,    --是否生效
                               n_sysIPID      in number     --IP前缀ID
                               )return integer is
   n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS;    
   s_uccc_count          integer;
   n_action              varchar2(200);
   n_game_code           varchar2(50);
   n_cp_code             varchar2(50);
   n_pre_charge_url      varchar2(100);
   n_cp_callback_address varchar2(100);
   begin
      --判断参数是否正确
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
      --判断是否已经有重复的数据 
      select count(*)  into s_uccc_count  from uc_cp_charge_config uccc  
      where uccc.cp_game_id = n_cp_game_id and uccc.game_id = n_game_id and uccc.cp_id = n_cp_id;
     
      --数据重复
      begin
        if(s_uccc_count > 0 ) then
            n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
            goto ExitError;            
        else
            --获取地址配置
            select sg.game_code into n_game_code from sys_game sg where sg.game_id = n_game_id; --获取gameCode
            if(n_game_code is null) then
                 n_result := PKG_PLMGR_LIANYUN_CHARGE.E_GAMECODE_NULL;
                 goto ExitError;            
            end if;
            select ucc.action_name ,ucc.cp_code into n_action,n_cp_code from uc_cp_config ucc where ucc.cp_id = n_cp_id;--获取cpCode,action
            if(n_action is null)then
                   n_result := PKG_PLMGR_LIANYUN_CHARGE.E_ACTION_NULL;
                   goto ExitError;          
            end if;
            if(n_cp_code is null)then
                    n_result := PKG_PLMGR_LIANYUN_CHARGE.E_CPCODE_NULL;
                    goto ExitError;  
            end if;
            --拼接地址  ip/action/gameCode/cpCode
            n_pre_charge_url :=  n_sys_ip|| '/preCharge/'|| n_cp_code;
            n_cp_callback_address :=  n_sys_ip|| '/' || n_action || '/' || n_game_code || '/' || n_cp_code;
            --增加配置
            insert into uc_cp_charge_config (cp_id,cp_game_id,cp_game_name,game_id,pre_charge_url,cp_callback_address,state,config_time,sys_ip_id)
                             values(n_cp_id,n_cp_game_id,n_cp_game_name,n_game_id,''||n_pre_charge_url||'',''||n_cp_callback_address||'', n_state,sysdate,n_sysIPID);
             
             --配置默认key配置
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
      
      --返回
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
      
   end;   
/*修改渠道游戏配置
    返回：
           1  成功
          -1  操作失败
       -1001  参数有误
       -1002  数据重复 
       -1003  数据有误
       -1004  对应gameCode配置为空
       -1005  对应渠道的action配置为空
       -1006  对应渠道的cp编码配置为空
*/                               
   function updateLianyunConfig(
                               n_old_game_id      in number,    --修改前——游戏ID
                               n_old_cp_game_id   in number,    --修改前——合作伙伴游戏ID
                               n_old_cp_id        in number,    --修改前——合作伙伴ID
                               n_game_id          in number,    --游戏ID
                               n_cp_game_id       in number,    --合作伙伴游戏ID]
                               n_cp_game_name     in varchar2,  --合作后伴游戏name
                               n_cp_id            in number,    --合作伙伴ID
                               n_sys_ip           in varchar2,  --预支付地址和回调地址的ip前缀
                               n_state            in number,    --是否生效
                               n_sysIPID          in number     --IP前缀ID
                               )return integer is
   n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS;    
   s_uccc_count          integer default 0;
   n_action              varchar2(200);
   n_game_code           varchar2(50);
   n_cp_code             varchar2(50);
   n_pre_charge_url      varchar2(100);
   n_cp_callback_address varchar2(100);
   begin
      --判断参数是否正确
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
      --判断是否已经有重复的数据 ,如果基础信息和之前一样，就不用判断了
      if((n_game_id <> n_old_game_id) or (n_cp_id <> n_old_cp_id) or (n_cp_game_id <> n_old_cp_game_id)) then
          select count(*)  into s_uccc_count  from uc_cp_charge_config uccc  
          where uccc.cp_game_id = n_cp_game_id and uccc.game_id = n_game_id and uccc.cp_id = n_cp_id;
      end if;
      --数据重复
      begin
        if(s_uccc_count > 0 ) then
            n_result := PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
            goto ExitError;            
        else
            --获取地址配置
            select sg.game_code into n_game_code from sys_game sg where sg.game_id = n_game_id; --获取gameCode
            if(n_game_code is null) then
                 n_result := PKG_PLMGR_LIANYUN_CHARGE.E_GAMECODE_NULL;
                 goto ExitError;            
            end if;
            select ucc.action_name ,ucc.cp_code into n_action,n_cp_code from uc_cp_config ucc where ucc.cp_id = n_cp_id;--获取cpCode,action
            if(n_action is null)then
                   n_result := PKG_PLMGR_LIANYUN_CHARGE.E_ACTION_NULL;
                   goto ExitError;          
            end if;
            if(n_cp_code is null)then
                    n_result := PKG_PLMGR_LIANYUN_CHARGE.E_CPCODE_NULL;
                    goto ExitError;  
            end if;
            --拼接地址  ip/action/gameCode/cpCode
            n_pre_charge_url :=  n_sys_ip|| '/preCharge/'|| n_cp_code;
            n_cp_callback_address :=  n_sys_ip|| '/' || n_action || '/' || n_game_code || '/' || n_cp_code;
            --更新配置
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
      
      --返回
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
      
   end;   
/*更新uc_cp_charge_config中cp_callback_address和pre_charge_url，因渠道cpCode actionCode 改变，或因游戏 gameCode改变
      返回 ：
           1  成功
          -1  操作失败
       -1004  对应gameCode配置为空
       -1005  对应渠道的action配置为空
       -1006  对应渠道的cp编码配置为空                                                                 
*/
   function updateCPURLForGameOrCpUpd(
                                  n_game_id    in number,  --gameCode改变的game_id
                                  n_cp_id      in number   --cpCode actionCode 改变的cp_id
                                  )return integer is
    n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS; 
    s_address               varchar2(200);
    s_url                   varchar2(200);
    begin
      begin
          if(n_game_id is not null) then
                --更新对应gameID的URL
                declare 
                 cursor uccc_value_for_game_id is
                     SELECT uccc.game_id,uccc.cp_game_id,uccc.cp_id,uccc.cp_callback_address
                     FROM uc_cp_charge_config uccc 
                     WHERE uccc.game_id = n_game_id;
                 begin
                     for x in uccc_value_for_game_id loop
                         --获取修改后的地址，如果有异常，抛出
                         n_result := getURLById(x.game_id,x.cp_id, x.cp_callback_address,'',s_url,s_address);
                         if(n_result < 0) then
                             goto ExitError;
                         end if;
                         --更新url
                         update uc_cp_charge_config set cp_callback_address = s_address,pre_charge_url = s_url
                         where game_id = x.game_id and cp_game_id = x.cp_game_id and cp_id = x.cp_id;
                     end loop;
                 end;     
          end if;
          
          if(n_cp_id is not null) then
                --更新对应cpID的URL
                declare 
                 cursor uccc_value_for_cp_id is
                     SELECT uccc.game_id,uccc.cp_game_id,uccc.cp_id,uccc.cp_callback_address
                     FROM uc_cp_charge_config uccc 
                     WHERE uccc.cp_id = n_cp_id;
                 begin
                     for x in uccc_value_for_cp_id loop
                         --获取修改后的地址，如果有异常，抛出
                         n_result := getURLById(x.game_id,x.cp_id, x.cp_callback_address,'',s_url,s_address);
                         if(n_result < 0) then
                             goto ExitError;
                         end if;
                         --更新url
                         update uc_cp_charge_config set cp_callback_address = s_address,pre_charge_url = s_url
                         where game_id = x.game_id and cp_game_id = x.cp_game_id and cp_id = x.cp_id;
                     end loop;
                 end;     
          end if;
      
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
      
      --返回
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
      
    end;   
/**根据信息获取拼接的URL
    返回：
           1  成功
          -1  操作失败
       -1004  对应gameCode配置为空
       -1005  对应渠道的action配置为空
       -1006  对应渠道的cp编码配置为空  
    
*/                           
    function getURLById(
                        n_game_id    in number,        
                        n_cp_id      in number,
                        n_url        in varchar2,       --一个带IP前缀的完整的uc_cp_charge_config中的URL
                        v_ip_address in varchar2,       --IP 前缀
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
                
                  --获取ip前缀
                  s_ip := SUBSTR (n_url,  0,  INSTR (n_url, '/', -1) - 1);
                  s_ip := SUBSTR (s_ip,   0,  INSTR (s_ip,  '/', -1) - 1);
                  s_ip := SUBSTR (s_ip,   0,  INSTR (s_ip,  '/', -1) - 1);
              end if;    
               --获取地址配置
                select sg.game_code into n_game_code from sys_game sg where sg.game_id = n_game_id; --获取gameCode
                if(n_game_code is null) then
                     n_result := PKG_PLMGR_LIANYUN_CHARGE.E_GAMECODE_NULL;
                     goto ExitError;            
                end if;
                select ucc.action_name ,ucc.cp_code into n_action,n_cp_code from uc_cp_config ucc where ucc.cp_id = n_cp_id;--获取cpCode,action
                if(n_action is null)then
                       n_result := PKG_PLMGR_LIANYUN_CHARGE.E_ACTION_NULL;
                       goto ExitError;          
                end if;
                if(n_cp_code is null)then
                        n_result := PKG_PLMGR_LIANYUN_CHARGE.E_CPCODE_NULL;
                        goto ExitError;  
                end if;
                --拼接地址  ip/action/gameCode/cpCode
                n_pre_charge_url :=  s_ip|| '/preCharge/'|| n_cp_code;
                n_cp_callback_address :=  s_ip|| '/' || n_action || '/' || n_game_code || '/' || n_cp_code;
                goto ExitOK;
          exception when others then
            n_result := pkg_plmgr_util.COMMON_ERROR;
            goto ExitError;
          end;
            
          --返回
          <<ExitOK>>
             n_result := pkg_plmgr_util.COMMON_SUCCESS;   
             return n_result;
          <<ExitError>>    
             return n_result;
     end;                              
/*删除渠道游戏配置
   返回 ：
      1    成功
     -1    失败
  -1001    参数有误
  -1003    不存在需要操作的数据
*/   
   function deleteLianyunConfig(
                               n_game_id      in number,    --游戏ID
                               n_cp_game_id   in number,    --合作伙伴游戏ID
                               n_cp_id        in number     --合作伙伴ID
                               ) return integer is
   s_count               integer;
   n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS; 
   begin 
     --验证参数
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
     --删除数据
     begin
       delete uc_cp_charge_config where game_id = n_game_id and cp_game_id = n_cp_game_id and cp_id = n_cp_id;
       delete uc_cp_charge_detail where cp_game_id = n_cp_game_id;
       goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
     --返回
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
   end;
/*更新渠道游戏配置————是否生效
      1    成功
     -1    失败
  -1001    参数有误
  -1003    不存在需要操作的数据
*/
   function updateLianyunConfigState(
                                       n_game_id      in number,    --游戏ID
                                       n_cp_game_id   in number,    --合作伙伴游戏ID
                                       n_cp_id        in number,    --合作伙伴ID
                                       n_state        in number     --是否生效
                                     )return integer is
   s_count               integer;
   n_result              integer  := pkg_plmgr_util.COMMON_SUCCESS;                                      
   begin
     --验证参数
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
     --更新状态
     begin
         update uc_cp_charge_config set state = n_state where game_id = n_game_id and cp_game_id = n_cp_game_id and cp_id = n_cp_id;
         goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
     --返回
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
   end;
/*根据ID获取游戏value配置信息
   返回
         1    成功
        -1    失败
     -1001    参数有误
*/
   function getLianyunConfigDetailByID(
                                  n_cp_game_id   in  number,
                                  ur_result      out sys_refcursor
                                  )return integer is
   n_result       integer  := pkg_plmgr_util.COMMON_SUCCESS;  
   s_sql varchar(200);                           
   begin
     --判断参数
     if(n_cp_game_id is null) then
         n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
         goto ExitError;
     end if;
     --查询数据
     begin
       s_sql := 'select uccd.name,uccd.key,uccd.value from uc_cp_charge_detail uccd where uccd.cp_game_id =' || n_cp_game_id ;
       open ur_result for s_sql;
       goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
      --返回
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;
   end;   
/*删除游戏value配置信息
 返回 
         1    成功
        -1    失败
     -1001    参数有误
*/
   function deleteLianyunConfigDetail(
                                      n_cp_game_id   in  number,
                                      n_key          in  varchar2
                                      )return integer is
   n_result       integer  := pkg_plmgr_util.COMMON_SUCCESS;        
   begin
     --判断参数
     if((n_cp_game_id is null) or (n_key is null))then
        n_result := PKG_PLMGR_LIANYUN_CHARGE.E_PARAM_ERROR;
        goto ExitError;
     end if;
     --删除数据
     begin 
         delete uc_cp_charge_detail where cp_game_id = n_cp_game_id and key = n_key;
         goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
      --返回
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;   
   end;
/*增加游戏value配置信息
 返回 
         1    成功
        -1    失败
     -1001    参数有误
     -1003    不存在key
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
    --判断参数
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
     --增加数据
     begin 
         insert into uc_cp_charge_detail (cp_game_id,name,key,value,config_time)
                          values(n_cp_game_id,n_name,n_key,n_value,sysdate);
         goto ExitOK;
     exception when others then
      n_result := pkg_plmgr_util.COMMON_ERROR;
      goto ExitError;
     end;
      --返回
      <<ExitOK>>
       n_result := pkg_plmgr_util.COMMON_SUCCESS;   
       return n_result;
      <<ExitError>>
       return n_result;     
   end;
 /*判断输入的game_id 和 game_name是否正常
    返回 
       1   正常
   -1001   不正常
   -1003   数据有误
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
/*获取所有联运游戏
   返回 
       1   成功
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
/*获取所有使用的Ip前缀
   返回 
       1   成功
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
/*获取所有Ip前缀
   返回 
       1   成功
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
/*增加一个IP前缀
   -1001   参数错误
   -1002   ip重复
   1       成功
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
     --检查是否重复
     select count(1) into n_count  from uc_sys_ip_address where sys_ip = v_sys_ip;
     if (n_count > 0 ) then
       return PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
     end if;
     --设置主键
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
 /*修改一个IP前缀
   -1001   参数错误
   -1002   ip重复  
   -1003   操作的数据不存在
   1       成功
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
     --检查是否存在需要修改的数据
     select count(1) into n_count  from uc_sys_ip_address where sys_ip_id = n_sys_ip_id;
     if(n_count < 1) then
       return PKG_PLMGR_LIANYUN_CHARGE.E_DATA_ERROR;
     end if;
     --检查是否重复
     select count(1) into n_count  from uc_sys_ip_address where sys_ip = v_sys_ip and sys_ip_id != n_sys_ip_id;
     if (n_count > 0 ) then
       return PKG_PLMGR_LIANYUN_CHARGE.E_REPEAT;
     end if;
     
     --修改已经使用该Ip前缀的数据
     select sys_ip into v_old_IP from uc_sys_ip_address where  sys_ip_id = n_sys_ip_id;
     if(v_old_IP <> v_sys_ip) then
                --更新对应cpID的URL
                declare 
                 cursor uccc_value_for_sys_ip_id is
                     SELECT uccc.game_id,uccc.cp_game_id,uccc.cp_id,uccc.pre_charge_url
                     FROM uc_cp_charge_config uccc 
                     WHERE uccc.sys_ip_id = n_sys_ip_id;
                 begin
                     for x in uccc_value_for_sys_ip_id loop
                         --获取修改后的地址，如果有异常，抛出
                         n_result := getURLById(x.game_id,x.cp_id, v_sys_ip,v_sys_ip,s_url,s_address);
                         if(n_result < 0) then
                             return pkg_plmgr_util.COMMON_ERROR;
                         end if;
                         --更新url
                         update uc_cp_charge_config set cp_callback_address = s_address,pre_charge_url = s_url
                         where game_id = x.game_id and cp_game_id = x.cp_game_id and cp_id = x.cp_id and sys_ip_id = n_sys_ip_id;
                     end loop;
                 end;     
     
     end if;
     
     --更新数据
     update uc_sys_ip_address set sys_ip = v_sys_ip,comments = v_comments,is_use = n_is_use
     where sys_ip_id = n_sys_ip_id;

     return pkg_plmgr_util.COMMON_SUCCESS;
   end; 
/*删除IP前缀
   -1001   参数错误
   1       成功
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
