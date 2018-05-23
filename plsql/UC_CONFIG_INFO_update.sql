
/*
	对plmgr_app用户下的UC_CONFIG_INFO表添加type字段
*/
alter table "PLMGR"."UC_CONFIG_INFO" add "TYPE" NUMBER(2,0) DEFAULT 1 NOT NULL ENABLE;





