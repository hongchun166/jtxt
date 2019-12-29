
package com.linkb.jstx.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 小程序
 */
@DatabaseTable(tableName = "t_lvxin_microapp")
public class MicroApp implements Serializable {

	private transient static final long serialVersionUID = 1L;
	@DatabaseField(id = true)
	public String code;

	@DatabaseField
	public String name;

	@DatabaseField
	public String url;

	@DatabaseField
	public String color;

	@DatabaseField
	public long timestamp;

}
