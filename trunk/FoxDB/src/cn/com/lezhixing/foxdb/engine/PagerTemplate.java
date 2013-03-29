package cn.com.lezhixing.foxdb.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页的辅助对象
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-13
 */
public class PagerTemplate<T> {
	
	/** 获得的分页的具体数据 **/
	private List<T> pageData = new ArrayList<T>();
	/** 获得的总记录数 **/
	private long totalRecordsCount;

	public List<T> getPageData() {
		return pageData;
	}

	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}

	public void setTotalRecordsCount(long totalRecordsCount) {
		this.totalRecordsCount = totalRecordsCount;
	}

	public long getTotalRecordsCount() {
		return totalRecordsCount;
	}
	
}
