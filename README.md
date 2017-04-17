[TOC]

# 简介
uw-dm包是一个封装数据库操作的类库，并一致化管理数据库连接池。

# 主要特性
1. 支持多数据库连接，支持mysql/oracle（其他的也支持），支持基于表名的访问规则配置，便于分库分表。
2. 为了适配多数据库连接而改进的连接池，线程数少且节省资源，同时支持对于异常SQL的监控，便于整体控制数据库连接数。内测比druid更利索一些。
3. 非常类似hibernate的jpa的CRUD操作，以及非常类似mybatis的SQL映射实现，调用更加简单和直接。以上基于反射实现，已经使用缓存来保证效率了，木有泄漏。
4. 更直接和爽快的事务支持和批量更新支持，但是用起来要小心点哦，必须要用try.catch.finally规范处理异常。
5. 运维特性支持，可以监控每一条sql的执行情况，各种报表都可以做，比如slow-query，bad-query等等。。
6. 内部有一个CodeGen用于直接从数据库生成entity类，方便。

# 如何在项目中引入um-dm库

```
<dependency>
	<groupId>com.umtone</groupId>
	<artifactId>uw-dm</artifactId>
	<version>3.0.8</version>
</dependency>
```

# 配置文件
connectionPool.xml中配置数据库连接池

```
<!-- 提供给dao模块使用的系统连接池，主要用于框架的系统服务 -->
		<poolSys>
			<!-- 数据库类型，当前仅允许mysql/oracle -->
			<dbType>mysql</dbType>
			<!-- 服务器连接地址 -->
			<server>jdbc:mysql://192.168.88.88:3306/pms?characterEncoding=utf-8&amp;useSSL=false&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true</server>
			<!-- 登录用户名 -->
			<username>root</username>
			<!-- 登录密码 -->
			<password></password>
			<!-- 最小连接数 -->
			<minConns>3</minConns>
			<!-- 最大连接数 -->
			<maxConns>10</maxConns>
		</poolSys>
```

```
<pool name="pms">
			<!-- 数据库类型，当前仅允许mysql/oracle -->
			<dbType>mysql</dbType>
			<!-- 服务器连接地址 -->
			<server>jdbc:mysql://192.168.88.88:3306/pms?characterEncoding=utf-8&amp;useSSL=false&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true</server>
			<!-- 登录用户名 -->
			<username>root</username>
			<!-- 登录密码 -->
			<password></password>
			<!-- 最小连接数 -->
			<minConns>3</minConns>
			<!-- 最大连接数 -->
			<maxConns>100</maxConns>
		</pool>
```
connectionRouter.xml用于配置数据库访问路由设定，现在用的很少了。。

```
	<!-- 基于table表名的数据库访问连接路由配置 -->
	<mapList>
		<!-- 
		table：关联的表名，支持正则表达式 
		access:访问方法，当前支持all/write/read
		       all是所有访问方法，一般情况下会先匹配write/read方法，找不到的情况下才会匹配all方法。
		       write是和写有关的方法，如insert,update,delete;read是和读有关的方法，如select
		database:连接池/组名
		
		书写配置的时候应该注意，匹配是按照顺序匹配的，所以会匹配到最后一条符合的规则上。
		-->
		<map table="(.*)" access="all" database="test" />
	</mapList>
```


# 所有功能入口

```
DAOFactory dao = DAOFactory.getInstance();
```
所有的数据库访问操作，都从dao开始。
在不使用事务的情况下，dao是可以共用的，也支持多线程访问。

# 实体类代码生成


```
public static void main(String[] args) throws Exception {
		//需要生成代码的包名
		CodeGen.PACKAGE_NAME="zentao.pms.entity";
		//需要生成代码的位置
		CodeGen.SOURCECODE_PATH="D:/work_zowoyoo/zentao-pms/src/";
		//需要生成表列表，用","分割，如果留空则生成所有表。
		CodeGen.TABLE_LIST = "";
		//指定数据库连接名，留空则使用默认连接。
		CodeGen.CONN_NAME = "pms";
		//执行生成代码。
		CodeGen.main(args);
	}
```

# 实体类操作
实体类操作一般会有多个参数重载，要求实体类必须是DataEntity类型。
可以指定连接名和表名，这样是为了提高灵活性。
对于大多数项目来说，可以通过ConnectionRouter来指定连接名。

## 插入一条新纪录


```
/**
	 * 保存一个Entity实例，等效于insert。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param entity 要更新的对象
	 * @param tableName 指定表名
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> T save(String connName, T entity, String tableName)
			throws TransactionException;
```


## 修改一条记录

```
/**
	 * 根据主键更新一个Entity实例，等效于update。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param entity 要更新的对象
	 * @param tableName 指定表名
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> int update(String connName, T entity, String tableName)
			throws TransactionException;
```

## 删除一条记录

```
/**
	 * 根据主键删除一个Entity实例，等效于delete。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param entity 要更新的对象
	 * @param tableName 指定表名
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T extends DataEntity> int delete(String connName, T entity, String tableName)
			throws TransactionException;
```

## 载入一条记录

```
/**
	 * 根据指定的主键ID载入一个Entity实例。
	 * @param cls 要映射的对象类型
	 * @param tableName 指定表名
	 * @param id 主键数值
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T load(Class<T> cls, String tableName, Serializable id) throws TransactionException;
```


## 查询

```
/**
	 * 根据指定的映射类型，返回一个DataList列表。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> DataList<T> list(String connName, Class<T> cls, String selectsql, Object[] paramList,
			int startIndex, int resultNum, boolean autoCount) throws TransactionException;
```

## 执行返回单个对象的查询

```
/**
	 * 查询单个对象（单行数据）。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的对象类型
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T queryForSingleObject(String connName, Class<T> cls, String selectsql, Object... paramList)
			throws TransactionException;
```

# 一般SQL操作
## 执行返回DataSet（多行多列）的查询

```
/**
	 * 返回一个DataSet数据列表。
	 * 相比较DataList列表，这不是一个强类型列表，但是更加灵活。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的绑定参数
	 * @param startIndex 开始位置，默认为0
	 * @param resultNum 结果集大小，默认为0，获取全部数据
	 * @param autoCount 是否统计全部数据（用于分页算法），默认为false。
	 * @return
	 * @throws TransactionException
	 */
	public abstract DataSet queryForDataSet(String connName, String selectsql, Object[] paramList, int startIndex,
			int resultNum, boolean autoCount) throws TransactionException;
```
## 执行返回List（多行单列）的查询

```
	/**
	 * 查询单个基本数值列表（多行单个字段）。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的基础类型，如int.class,long.class,String.class,Date.class
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> List<T> queryForSingleList(String connName, Class<T> cls, String sql, Object... paramList)
			throws TransactionException;
```


## 执行返回单个基本数值的查询

```
	/**
	 * 查询单个基本数值（单个字段）。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param cls 要映射的基础类型，如int.class,long.class,String.class,Date.class
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return
	 * @throws TransactionException
	 */
	public abstract <T> T queryForSingleValue(String connName, Class<T> cls, String sql, Object... paramList)
			throws TransactionException;
```


## 执行任意sql语句

```
/**
	 * 执行一条SQL语句。
	 * @param connName 连接名，如设置为null，则根据sql语句或表名动态路由确定
	 * @param selectsql 查询的SQL
	 * @param paramList 查询SQL的参数
	 * @return 影响的行数
	 * @throws TransactionException
	 */
	public abstract int executeCommand(String connName, String sql, Object... paramList) throws TransactionException;
```

# 获得Sequence序列
为了在集群环境下使用，需要由统一位置获得sequence。
sequence由poolSys下的sys_sequence表维护，对于插入频繁的表，请注意提升increment的数值到100，可以提高sequence性能。

```
/**
	 * 根据表名来获得seq序列。
	 * 此序列通过一个系统数据库来维护，可以保证在分布式下的可用性。
	 * @param tablename
	 * @return
	 */
	public abstract long getSequenceId(String tablename);
```

# DataList VS DataSet
DataList用于DataEntity，优先使用。
DataSet用于兼容代码，性能略低于DataList。
