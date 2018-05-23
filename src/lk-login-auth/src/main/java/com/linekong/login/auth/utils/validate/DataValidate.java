package com.linekong.login.auth.utils.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据校验注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
public @interface DataValidate {
	//是否可以为空
	boolean 	nullable()			default false;
	//最大长度
	int			maxLength()			default Integer.MAX_VALUE;
	//最小长度
	int			minLength()			default 0;
	//提供常用的正则表达式验证
	RegexType	regexType()			default RegexType.NONE;
	//自定义正则验证
	String		regexExpression()	default "";
	//参数字段描述，为显示友好的异常信息
	String		description()		default "";

}
