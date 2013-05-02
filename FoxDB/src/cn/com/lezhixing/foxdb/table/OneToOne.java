package cn.com.lezhixing.foxdb.table;

import cn.com.lezhixing.foxdb.annotation.CascadeType;
import cn.com.lezhixing.foxdb.annotation.FetchType;
import cn.com.lezhixing.foxdb.engine.TableInfo;
import cn.com.lezhixing.foxdb.exception.FoxDbException;

import com.foxchan.foxutils.data.StringUtils;

/**
 * 一对一
 * @author foxchan@live.cn
 * @create 2013-4-26
 */
public class OneToOne extends Column {
	
	/** 一的一方的类 */
	private Class<?> oneClass;
	/** 级联的策略数组 */
	private CascadeType[] cascadeTypes;
	/** 获取数据的策略 */
	private FetchType fetchType;
	/** 控制反转所映射的类，即多那一方的类 */
	private String mappedBy;

	public Class<?> getOneClass() {
		return oneClass;
	}

	public void setOneClass(Class<?> oneClass) {
		this.oneClass = oneClass;
	}

	public CascadeType[] getCascadeTypes() {
		return cascadeTypes;
	}

	public void setCascadeTypes(CascadeType[] cascadeTypes) {
		this.cascadeTypes = cascadeTypes;
	}

	public FetchType getFetchType() {
		return fetchType;
	}

	public void setFetchType(FetchType fetchType) {
		this.fetchType = fetchType;
	}

	public String getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}
	
	@Override
	public KeyValue toKeyValue(Object target){
		KeyValue kv = null;
		String oneColumn = mappedBy;
		if(StringUtils.isEmpty(oneColumn)){
			oneColumn = TableInfo.buildForeignKeyName(getName());
		} else {
			oneColumn = TableInfo.buildForeignKeyName(mappedBy);
		}
		Object oneObject = getValue(target);
		if(oneObject != null){
			Object oneValue = TableInfo.getInstance(oneObject.getClass())
					.getId().getValue(oneObject);
			if(StringUtils.isEmpty(oneValue)){
				throw new FoxDbException("错误原因，缺少主键。[" + target.getClass() + "]的["
						+ getFieldName() + "]对象还没有进行持久化，没有找到主键值。");
			}
			if(!StringUtils.isEmpty(oneColumn) && !StringUtils.isEmpty(oneValue)){
				kv = new KeyValue(oneColumn, oneValue);
			} else {
				throw new FoxDbException("[" + target.getClass() + "]的["
						+ getFieldName() + "]属性设置了非空属性，当前值为空，请检查实体的配置信息或者为属性["
						+ getFieldName() + "]设置一个值。");
			}
		}
		return kv;
	}

}
