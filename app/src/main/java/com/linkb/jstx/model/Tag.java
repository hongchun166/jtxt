
package com.linkb.jstx.model;

import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 用户标签
 */
@DatabaseTable(tableName = "t_lvxin_tag")
public class Tag implements Serializable {
	private transient static final long serialVersionUID = 4733464888738356502L;

	@DatabaseField(id = true)
	public long id;

	@DatabaseField
	public String source;

	@DatabaseField
	public String account;

	@DatabaseField
	public String name;

	@Override
	public int hashCode() {
		return getClass().getName().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Tag) {
			Tag target = (Tag) o;
			return id == target.id;
		}
		return false;
	}
	public int getAccountCount(){
		 return getAccountList().size();
	}

	public void addAccountList(List<String> accountList) {
		List<String> list = getAccountList();
		list.addAll(accountList);
		this.account = TextUtils.join(",",list);
	}

	public void delAccountList(List<String> accountList) {
		List<String> list = getAccountList();
		list.removeAll(accountList);
		if (list.isEmpty()){
			account = null;
		}else {
			this.account = TextUtils.join(",",list);
		}
	}

	public List<String> getAccountList(){
		LinkedList<String> linkedList = new LinkedList<>();
		if (TextUtils.isEmpty(account)){
			return linkedList;
		}
		linkedList.addAll(Arrays.asList(account.split(",")));
		return linkedList;
	}

}
