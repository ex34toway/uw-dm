package uw.dm.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

import uw.dm.DataEntity;
import uw.dm.annotation.ColumnMeta;
import uw.dm.annotation.TableMeta;

/**
 * mscPerm实体类。
 * 
 * 
 * @author axeon
 * 
 * @version $Revision: 1.00 $ $Date: 2016-12-05 23:42:03
 */
@TableMeta(tableName="msc_perm",tableType="TABLE")
public class MscPerm implements DataEntity,Serializable{
	
	private static final long serialVersionUID = 1L;
	

	/**
	 * 
	 */
	@ColumnMeta(columnName="id",dataType="long",dataSize=19,nullable=false,primaryKey=true)
	private long id;

	/**
	 * 菜单所属的上级ID
	 */
	@ColumnMeta(columnName="parent_id",dataType="long",dataSize=19,nullable=true)
	private long parentId;

	/**
	 * 0.是菜单项，1是api
	 */
	@ColumnMeta(columnName="perm_type",dataType="int",dataSize=10,nullable=true)
	private int permType;

	/**
	 * 权限的名称
	 */
	@ColumnMeta(columnName="perm_name",dataType="String",dataSize=100,nullable=true)
	private String permName;

	/**
	 * 权限的描述
	 */
	@ColumnMeta(columnName="perm_desc",dataType="String",dataSize=1000,nullable=true)
	private String permDesc;

	/**
	 * 拥有子元素的个数
	 */
	@ColumnMeta(columnName="child_num",dataType="int",dataSize=10,nullable=true)
	private int childNum;

	/**
	 * 权限执行的url目录
	 */
	@ColumnMeta(columnName="action_url",dataType="String",dataSize=120,nullable=true)
	private String actionUrl;

	/**
	 * 单位目录层次深度
	 */
	@ColumnMeta(columnName="menu_layer",dataType="int",dataSize=10,nullable=true)
	private int menuLayer;

	/**
	 * 单位层次路径
	 */
	@ColumnMeta(columnName="menu_path",dataType="String",dataSize=50,nullable=true)
	private String menuPath;

	/**
	 * 创建时间
	 */
	@ColumnMeta(columnName="create_date",dataType="java.util.Date",dataSize=19,nullable=true)
	private java.util.Date createDate;

	/**
	 * 最后一次的修改时间
	 */
	@ColumnMeta(columnName="modify_date",dataType="java.util.Date",dataSize=19,nullable=true)
	private java.util.Date modifyDate;

	/**
	 * 状态
	 */
	@ColumnMeta(columnName="status",dataType="int",nullable=true)
	private int status;

	/*
	 * 轻量级状态下更新列表list
	 */
	public Set<String> _UPDATED_COLUMN = null;
		
	/*
	 * 变更信息
	 */
	private StringBuilder _UPDATED_INFO = null;

		
	/**
	 * 获得更改的字段列表.
	 */
	public Set<String> GET_UPDATED_COLUMN() {
		return _UPDATED_COLUMN;
	}

	/**
	 * 得到_UPDATED_INFO
	 */
	public String GET_UPDATED_INFO() {
		if (this._UPDATED_INFO == null) {
			return null;
		} else {
			return this._UPDATED_INFO.toString();
		}
	}
	
	/**
	 * 初始化set相关的信息
	 */
	private void _INIT_UPDATE_INFO() {
		this._UPDATED_COLUMN = new HashSet<String>();
		this._UPDATED_INFO = new StringBuilder("表msc_perm主键\"" + this.id + "\"更新为:\r\n");
	}	


	/**
	 * 获得。
	 */
	public long getId(){
		return this.id;
	}

	/**
	 * 获得菜单所属的上级ID。
	 */
	public long getParentId(){
		return this.parentId;
	}

	/**
	 * 获得0.是菜单项，1是api。
	 */
	public int getPermType(){
		return this.permType;
	}

	/**
	 * 获得权限的名称。
	 */
	public String getPermName(){
		return this.permName;
	}

	/**
	 * 获得权限的描述。
	 */
	public String getPermDesc(){
		return this.permDesc;
	}

	/**
	 * 获得拥有子元素的个数。
	 */
	public int getChildNum(){
		return this.childNum;
	}

	/**
	 * 获得权限执行的url目录。
	 */
	public String getActionUrl(){
		return this.actionUrl;
	}

	/**
	 * 获得单位目录层次深度。
	 */
	public int getMenuLayer(){
		return this.menuLayer;
	}

	/**
	 * 获得单位层次路径。
	 */
	public String getMenuPath(){
		return this.menuPath;
	}

	/**
	 * 获得创建时间。
	 */
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 * 获得最后一次的修改时间。
	 */
	public java.util.Date getModifyDate(){
		return this.modifyDate;
	}

	/**
	 * 获得状态。
	 */
	public int getStatus(){
		return this.status;
	}


	/**
	 * 设置。
	 */
	public void setId(long id){
		if ((!String.valueOf(this.id).equals(String.valueOf(id)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("id");
			this._UPDATED_INFO.append("id由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.id = id;
		}
	}

	/**
	 * 设置菜单所属的上级ID。
	 */
	public void setParentId(long parentId){
		if ((!String.valueOf(this.parentId).equals(String.valueOf(parentId)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("parent_id");
			this._UPDATED_INFO.append("parent_id由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.parentId = parentId;
		}
	}

	/**
	 * 设置0.是菜单项，1是api。
	 */
	public void setPermType(int permType){
		if ((!String.valueOf(this.permType).equals(String.valueOf(permType)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("perm_type");
			this._UPDATED_INFO.append("perm_type由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.permType = permType;
		}
	}

	/**
	 * 设置权限的名称。
	 */
	public void setPermName(String permName){
		if ((!String.valueOf(this.permName).equals(String.valueOf(permName)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("perm_name");
			this._UPDATED_INFO.append("perm_name由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.permName = permName;
		}
	}

	/**
	 * 设置权限的描述。
	 */
	public void setPermDesc(String permDesc){
		if ((!String.valueOf(this.permDesc).equals(String.valueOf(permDesc)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("perm_desc");
			this._UPDATED_INFO.append("perm_desc由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.permDesc = permDesc;
		}
	}

	/**
	 * 设置拥有子元素的个数。
	 */
	public void setChildNum(int childNum){
		if ((!String.valueOf(this.childNum).equals(String.valueOf(childNum)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("child_num");
			this._UPDATED_INFO.append("child_num由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.childNum = childNum;
		}
	}

	/**
	 * 设置权限执行的url目录。
	 */
	public void setActionUrl(String actionUrl){
		if ((!String.valueOf(this.actionUrl).equals(String.valueOf(actionUrl)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("action_url");
			this._UPDATED_INFO.append("action_url由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.actionUrl = actionUrl;
		}
	}

	/**
	 * 设置单位目录层次深度。
	 */
	public void setMenuLayer(int menuLayer){
		if ((!String.valueOf(this.menuLayer).equals(String.valueOf(menuLayer)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("menu_layer");
			this._UPDATED_INFO.append("menu_layer由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.menuLayer = menuLayer;
		}
	}

	/**
	 * 设置单位层次路径。
	 */
	public void setMenuPath(String menuPath){
		if ((!String.valueOf(this.menuPath).equals(String.valueOf(menuPath)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("menu_path");
			this._UPDATED_INFO.append("menu_path由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.menuPath = menuPath;
		}
	}

	/**
	 * 设置创建时间。
	 */
	public void setCreateDate(java.util.Date createDate){
		if ((!String.valueOf(this.createDate).equals(String.valueOf(createDate)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("create_date");
			this._UPDATED_INFO.append("create_date由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.createDate = createDate;
		}
	}

	/**
	 * 设置最后一次的修改时间。
	 */
	public void setModifyDate(java.util.Date modifyDate){
		if ((!String.valueOf(this.modifyDate).equals(String.valueOf(modifyDate)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("modify_date");
			this._UPDATED_INFO.append("modify_date由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.modifyDate = modifyDate;
		}
	}

	/**
	 * 设置状态。
	 */
	public void setStatus(int status){
		if ((!String.valueOf(this.status).equals(String.valueOf(status)))) {
			if (this._UPDATED_COLUMN == null) {
				_INIT_UPDATE_INFO();
			}
			this._UPDATED_COLUMN.add("status");
			this._UPDATED_INFO.append("status由\"" + this.menuPath + "\"改为\"" + menuPath + "\"\r\n");
			this.status = status;
		}
	}

	/**
	 * 重载toString方法
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("表msc_perm信息为:\r\n");
		sb.append("id=" + this.id + "\r\n");
		sb.append("parent_id=" + this.parentId + "\r\n");
		sb.append("perm_type=" + this.permType + "\r\n");
		sb.append("perm_name=" + this.permName + "\r\n");
		sb.append("perm_desc=" + this.permDesc + "\r\n");
		sb.append("child_num=" + this.childNum + "\r\n");
		sb.append("action_url=" + this.actionUrl + "\r\n");
		sb.append("menu_layer=" + this.menuLayer + "\r\n");
		sb.append("menu_path=" + this.menuPath + "\r\n");
		sb.append("create_date=" + this.createDate + "\r\n");
		sb.append("modify_date=" + this.modifyDate + "\r\n");
		sb.append("status=" + this.status + "\r\n");
		return sb.toString();
	}



}
