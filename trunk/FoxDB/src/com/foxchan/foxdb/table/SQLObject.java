package com.foxchan.foxdb.table;

import java.util.LinkedList;

import com.foxchan.foxutils.data.StringUtils;

/**
 * SQL操作对象
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class SQLObject {
	
	/** SQL语句 */
	private String sql;
	/** 参数列表 */
	private LinkedList<Object> params;
	
	public SQLObject(){}
	
	public SQLObject(StringBuilder sql, Object param){
		this.sql = sql.toString();
		if(StringUtils.isEmpty(this.params)) this.params = new LinkedList<Object>();
		this.params.add(param);
	}
	
	public SQLObject(StringBuilder sql, LinkedList<Object> params) {
		this.sql = sql.toString();
		this.params = params;
	}
	
	public SQLObject(StringBuilder sql, Object[] params) {
		this.sql = sql.toString();
		this.params = new LinkedList<Object>();
		if(!StringUtils.isEmpty(params)){
			for(int i = 0; i < params.length; i++){
				this.params.add(params[i]);
			}
		}
	}
	
	public SQLObject(String sql, Object param){
		this.sql = sql;
		if(StringUtils.isEmpty(params)) params = new LinkedList<Object>();
		this.params.add(param);
	}

	public SQLObject(String sql, LinkedList<Object> params) {
		this.sql = sql;
		this.params = params;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public LinkedList<Object> getParams() {
		return params;
	}

	public void setParams(LinkedList<Object> params) {
		this.params = params;
	}
	
	public String toString(){
		String str = "params:";
		for(Object o : params){
			str = StringUtils.concat(new Object[]{
					str, o, ", "
			});
		}
		return str;
	}
	
	public String[] getBindArgsAsStringArray(){
		if(!StringUtils.isEmpty(params)){
			String[] args = new String[params.size()];
			for(int i = 0; i < params.size(); i++){
				args[i] = params.get(i).toString();
			}
			return args;
		}
		return null;
	}

}