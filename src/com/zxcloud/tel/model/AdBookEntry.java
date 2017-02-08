package com.zxcloud.tel.model;

import java.util.List;

import com.zxcloud.tel.jsondata.AdBookInfo;

/**
 * @author xu.jian
 * 
 */
public class AdBookEntry extends BaseRequest {
	public AdBookEntry() {
		super(true);
	}

	public class AdBookReq {
		private boolean isFirst;
		private String updateDate;

		public boolean isFirst() {
			return isFirst;
		}

		public void setFirst(boolean isFirst) {
			this.isFirst = isFirst;
		}

		public String getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(String updateDate) {
			this.updateDate = updateDate;
		}

	}

	public class AdBookResp {
		private List<AdBookInfo> departmentList;
		// isNew 代表本地数据源是否要更新
		// isNew = true 本地已经是最新
		// isNew = false 本地不是最新，需要更新
		private boolean isNew;

		public List<AdBookInfo> getDepartmentList() {
			return departmentList;
		}

		public void setDepartmentList(List<AdBookInfo> departmentList) {
			this.departmentList = departmentList;
		}

		public boolean isNew() {
			return isNew;
		}

		public void setNew(boolean isNew) {
			this.isNew = isNew;
		}

	}

}
