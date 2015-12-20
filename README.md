说明
===
mybaits读取db的Demo工程


版本介绍
===
##v1.0 :
>
> 最原始的方法，写dao（sql查询接口），写domain（对应db中的数据结构），写mapper映射xml文件（具体的sql封装）
>
> 使用方式： 在test中利用 `SqlSession`来调用dao层接口

需要注意的是pom里面的几个依赖包，需要补全:

```
<dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>${mybatis.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>1.2.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>dbcp</artifactId>
            <version>6.0.29</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>
```

##v1.1 :
>
> 直接通过applicationContext来初始化dao接口，调用对应的方法来实现db操作
>
> 不写死mapper映射关系，采用自动扫描包来匹配。
>
> 支持注解

### 结构说明:

resources中保存的是相关的资源文件和配置文件

`sql/*.xml`  对应的事mapper文件，即写sql查询的地方， 需要注意namespace，和源码中的dao层对应

`mybatis.xml,  spring.xml` batis配置文件，里面设置了db相关，和建立映射


### v1.1.0

本版本会要求在mybatis.xml中写mapper配置文件，在spring.xml中写bean

完成基本的通过接口方式调用

batis.xml :

```
<configuration>
    <typeAliases>
        <typeAlias type="com.mogu.hui.domain.User" alias="User"/>
    </typeAliases>
    <mappers>
        <mapper resource="sql/user-mapper.xml"/>
    </mappers>
</configuration>
```

spring.xml

```
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="locations">
            <value>classpath*:conf/jdbc.properties</value>
        </property>
    </bean>
    <!--v1.0+版本，从配置文件中读sql相关信息-->
    <bean id="dataSource" class="org.apache.tomcat.dbcp.dbcp.BasicDataSource">
        <property name="driverClassName" value="${driver}"/>
        <property name="url" value="${url}"/>
        <property name="username" value="${username}"/>
        <property name="password" value="${password}"/>
    </bean>
    <!-- 指定sql会话工程类，定义dao接口的bean -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <!--configLocation属性指定mybatis的核心配置文件-->
        <property name="configLocation" value="classpath:mybatis.xml"/>
    </bean>
    <bean id="userDao" class="org.mybatis.spring.mapper.MapperFactoryBean">
        <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
        <property name="mapperInterface" value="com.mogu.hui.dao.UserDao" />
    </bean>
```

test.java

```
    private UserDao userDao;
    @Before
    public void init() throws IOException {
        ctx = new ClassPathXmlApplicationContext("classpath:spring.xml");
        userDao = ctx.getBean("userDao", UserDao.class);
    }
    @Test
    public void testDao() {
        try {
            User user =  userDao.getUserById(1);
            System.out.println(user.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
        }
    }
```

### v1.1.2

相比1.1.1而言，每次新增一个dao接口，需要重新指定mapper，新增一个bean，比较麻烦，改成自动扫描包的形式；加上自动扫描装配bean的配置

主要是针对`spring.xml` 文件中的配置进行修改，如下：

```
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <!--configLocation属性指定mybatis的核心配置文件-->
        <property name="configLocation" value="classpath:mybatis.xml"/>
        <!-- 重点关注下面这一行，用于指定mapper.xml-->
        <property name="mapperLocations" value="classpath*:sql/*.xml" />
    </bean>
    <!--以下是所有的映射, 使用方法查看GoodsTest.java-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.mogu.hui.dao" />
    </bean>
    <!-- 添加注解支持 -->
    <bean class="org.springframework.context.annotation.CommonAnnotationBeanPostProcessor" />
    <!-- 自动扫描装配bean，因此对UserService不用手动加一个bean，注意在类上加一个注解-->
    <context:component-scan base-package="com.mogu.hui" />
```

说明
===
1、在通过mybatis写入db中文数据时，发现乱码，解决方案：

  - table设置编码方式为 utf8(utf8mb4编码可以很好的存入emoj标签), 建表的时候指定

  ```
    CREATE TABLE `config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `team` varchar(255) NOT NULL DEFAULT '',
  `owner` varchar(255) NOT NULL DEFAULT '',
  `isDisable` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否隐藏 0：不隐藏，1：隐藏',
  `created` int(11) unsigned NOT NULL DEFAULT '0',
  `updated` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_isDisable` (`topology`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='拓扑配置信息';
  ```

  - 在jdbc的配置中加上

  ```
  driver=com.mysql.jdbc.Driver
  # 下面加上后缀，防止插入DB中文乱码，当然前提是需要将db的编码设置为utf8
  url=jdbc:mysql://127.0.0.1:3306/samp_db?useUnicode=true&characterEncoding=utf8
  username=root
  password=
  ```