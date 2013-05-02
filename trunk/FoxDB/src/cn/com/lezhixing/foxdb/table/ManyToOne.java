package cn.com.lezhixing.foxdb.table;

import cn.com.lezhixing.foxdb.annotation.CascadeType;
import cn.com.lezhixing.foxdb.annotation.FetchType;
import cn.com.lezhixing.foxdb.engine.TableInfo;
import cn.com.lezhixing.foxdb.exception.FoxDbException;

import com.foxchan.foxutils.data.StringUtils;

/**
 * 多对一
 * @author foxchan@live.cn
 * @create 2013-4-24
 */
public class ManyToOne extends Column {
	
	/** 对的一方的类 */
	private Class<?> manyClass;
	/** 级联的策略数组 */
	private CascadeType[] cascadeTypes;
	/** 获取数据的策略 */
	private FetchType fetchType;
	/** 是否是可选的 */
	private boolean isOptional;

	public Class<?> getManyClass() {
		return manyClass;
	}

	public void setManyClass(Class<?> manyClass) {
		this.manyClass = manyClass;
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

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	@Override
	public KeyValue toKeyValue(Object target) {
		KeyValue kv = null;
		String manyColumn = TableInfo.buildForeignKeyName(getName());
		Object manyObject = getValue(target);
		if (manyObject != null) {
			Object manyValue = TableInfo.getInstance(manyObject.getClass())
					.getId().getValue(manyObject);
			if (!StringUtils.isEmpty(manyColumn)
					&& !StringUtils.isEmpty(manyValue)) {
				kv = new KeyValue(manyColumn, manyValue);
			} else {
				throw new FoxDbException("[" + target.getClass() + "]的["
						+ getFieldName() + "]属性设置了非空属性，当前值为空，请检查实体的配置信息或者为属性["
						+ getFieldName() + "]设置一个值。");
			}
			if (isOptional == false) {
				throw new FoxDbException("[" + target.getClass() + "]的["
						+ getFieldName() + "]属性设置了非空属性，当前值为空，请检查实体的配置信息或者为属性["
						+ getFieldName() + "]设置一个值。");
			}
		}
		return kv;
	}

}
