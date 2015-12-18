说明
===
mybaits读取db的Demo工程


版本介绍
===
v1.0 :
>
> 最原始的方法，写dao（sql查询接口），写domain（对应db中的数据结构），写mapper映射xml文件（具体的sql封装）
>
> 使用方式： 在test中利用 `SqlSession`来调用dao层接口

v2.0 :
>
> 直接通过applicationContext来初始化dao接口，调用对应的方法来实现db操作
>
> 不写死mapper映射关系，采用自动扫描包来匹配。
>
> 支持注解