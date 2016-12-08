package uw.dm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnMeta {

	/**
	 * 列名
	 * 
	 * @return
	 */
	public String columnName() default "";

	/**
	 * 数据类型
	 * 
	 * @return
	 */
	public String dataType() default "";

	/**
	 * 最大长度
	 * 
	 * @return
	 */
	public int dataSize() default 0;

	/**
	 * 是否主键
	 * 
	 * @return
	 */
	public boolean primaryKey() default false;

	/**
	 * 是否可以为空？
	 * 
	 * @return
	 */
	public boolean nullable() default true;

	/**
	 * 是否是自动递增字段？
	 */
	public boolean autoIncrement() default false;
}
