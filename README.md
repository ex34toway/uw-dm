# 老树新花
* 这个项目的原始版本是十几年前做的，那时候还没有hibernate、mybatis，更没有spring jdbc，在那个年代，这玩意就是神器了！
* 今天重新捡起来，最开始想在spring jdbc上做一些封装，彻底让它退休。
* 老树新花是因为业务上千奇百怪的需要和习惯，更是为了兼容已有的代码，进行了针对性改进，看上去貌似时髦了一些。
* 今天把它开源，最直接的原因。。。是为了便于maven引用，其次呢，也希望喜欢的人能帮忙改改。

# 主要特性
1. 支持多数据库连接，支持mysql/oracle（其他的也支持），支持基于表名的访问规则配置，便于分库分表。
2. 为了适配多数据库连接而改进的连接池，线程数少且节省资源，同时支持对于异常SQL的监控，便于整体控制数据库连接数。内测比druid更利索一些。
3. 非常类似hibernate的jpa的CRUD操作，以及非常类似mybatis的SQL映射实现，调用更加简单和直接。以上基于反射实现，已经使用缓存来保证效率了，木有泄漏。
4. 更直接和爽快的事务支持和批量更新支持，但是用起来要小心点哦，必须要用try.catch.finally规范处理异常。
5. 运维特性支持，可以监控每一条sql的执行情况，各种报表都可以做，比如slow-query，bad-query等等。。
6. 内部有一个CodeGen用于直接从数据库生成entity类，方便。