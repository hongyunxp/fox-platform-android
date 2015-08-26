# 简介 #

FoxDB是一个运行在Android平台上的ORM框架，对Android开发中使用到的对于SQLite数据库的访问进行了轻量级的封装，从而允许开发者通过访问对象的方式对数据库进行操作。该库的接口设计参考了大名鼎鼎的JavaEE的ORM框架Hibernate的接口设计，力求让进行过JavaEE开发的程序员能够快速的上手。同时，该库的接口设计也考虑到没有进行过JavaEE开发的人员，能够让没有使用过Hibernate的开发人员快速上手。


# 环境配置 #

  * 将foxdb.jar库文件拷贝进Android项目的libs文件夹中即可。

# 引入源代码 #

  * 下载foxdb-src项目，将该项目导入到自己的Eclipse开发环境中。
  * 在引入foxdb.jar的项目的libs文件夹中创建与foxdb.jar同名的properties文件，例如：foxdb-1.6.1-RELEASE.jar对应的properties文件的命名应该是foxdb-1.6.1-RELEASE.jar.properties。

# 实体映射 #

  * 在需要创建数据库的实体类上添加注解@Table。
  * 在需要持久化进数据库的属性上添加注解@Column。
  * 每一个实体必须有一个唯一的标识，这个标识将用于生成数据库中的主键，在主键属性上添加注解@Id。
  * 如果有属性不需要持久化进数据库，在该属性上添加注解@Transient。
  * 要标识两个实体之间一一对应的关系，在该对象属性上面添加注解@OneToOne。
  * 要标识两个实体之间一对多的关系，在该对象属性上添加注解@OneToMany。
  * 要标识两个实体之间多对一的冠以，在该对象属性上添加注解@ManyToOne，通常@OneToMany注解与@ManyToOne注解成对出现。

# 获得数据库连接和Session #

  * 创建数据库对象：FoxDB db = FoxDB.create(this, "fox.db", 1);
  * 获得session：Session session = db.getCurrentSession();

# 增删改查 #

  * 保存数据：session.save(user);
  * 删除数据：<br />User user = session.get(1, User.class);<br />session.delete(user);
  * 更新数据：<br />User user = session.get(1, User.class);
<br />user.set...();
<br />session.update(user);
  * 查询数据-简单查询：User user = session.get(1, User.class);
  * 查询数据-条件查询：User user = session.("id = ?", new Object[.md](.md){1}, User.class);<br />List\<User\> users = session.listFrom(group, "users", User.class);
  * 分页查询：Pager\<User\> pager = new Pager\<User\>(15, 2);//第一个参数为每一页显示的记录数，第二个参数为当前的页码数
<br />pager = session.query(pager, null, null, orderBy, User.class);
<br />users = pager.getContent();
  * 数据排序：LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
<br />orderBy.put("createDate", "desc");
<br />orderBy.put("id", "desc");
<br />List\<User\> users = list("height>?", new Object[.md](.md){2}, orderBy, User.class);
  * 查找父对象中的一一对应的对象：Group group = session.findObjectFrom(user, "group", Group.class);//第二个参数为需要获取的属性的名称
  * 查找父对象中的多对一映射多的一方的列表：List<User\> users = session.listFrom(group, "users", User.class);//第二个参数为需要获取的属性的名称
<br />List<User\> users = session.listFrom(-1, -1, "weight > ?", new Object[.md](.md){16}, null, group, "users", User.class);

# 使用事务 #

  * 创建事务对象：Transaction tx = session.beginTransaction();
  * 通过session进行数据操作。
  * 提交事务：tx.commit();

# 后记 #

该类库仅仅对数据库的操作进行了基本的封装，并未在数据库的性能上进行优化。
<br />如果有同学对于数据库和ORM框架的开发有爱的，请发邮件到foxchan@live.cn，我们可以一起完善这个框架。