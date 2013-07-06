package com.foxchan.foxdb.annotation;

/**
 * 实体的获取策略
 * @author foxchan@live.cn
 * @create 2013-4-24
 */
public enum FetchType {
	
	/** 积极的获取方式，在该方式下，对象将总是会尝试去获取相关的对象信息 */
	EAGER,
	/** 校级的获取方式，在该方式下，对象将不会主动去获取相关对象的信息 */
	LAZY;

}
