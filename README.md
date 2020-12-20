# aboluomall
~~~
各服务端口号
amall-user-service  port:8070
amall-user-web port:8080

amall-manage-service port:8071
amall-manage-web port:8081

amall-item-service port:8072
amall-item-web port:8082

amall-search-service port:8073
amall-search-web port:8083
~~~
##常见异常解决

~~~
1.{ 
java.lang.NoClassDefFoundError: com/alibaba/spring/beans/factory/annotation/AnnotationInjectedBeanPostProcessor
解决方法：添加依赖：  <dependency>
                      <groupId>com.alibaba.spring</groupId>
                      <artifactId>spring-context-support</artifactId>
                      <version>1.0.2</version>
                  </dependency>
}
~~~
~~~
2.{
java.lang.NoClassDefFoundError: io/netty/channel/EventLoopGroup
解决方法：添加依赖：    <dependency>
                        <groupId>io.netty</groupId>
                        <artifactId>netty-all</artifactId>
                        <version>4.1.32.Final</version>
                    </dependency>
}
~~~
~~~
3.{
com.alibaba.dubbo.rpc.RpcException: No provider available from registry 192.168.213.132:2181 for service com.aboluo.amall.api.service.CatalogService on consumer 192.168.213.1 use dubbo version 2.6.5, please check status of providers(disabled, not registered or in blacklist)
解决方法：启动类上加 MapperScan @MapperScan(basePackages = "com.aboluo.amall.manage.mapper")
}
四.{
跨域问题
在Controller加入 @CrossOrigin注解
}
~~~
~~~
4.{
service端无法注册dubbo
解决方法：在serviceImpl添加 @Service 注解(这里要注意是添加dubbo的@Service注解！！)
}
~~~
~~~
5.web端无法注册dubbo
  解决方法：在Controller中注入Service层时应使用 @Reference 注解而非 @Autoware 注解
~~~
~~~
1. 如果是 GET POST 等请求方式 则用参数接收 例如
    //请求路径为 http://127.0.0.1:8081/getAttrValueList?attrId=12
    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){
        return attrService.attrInfoList(catalog3Id);
    }
2. 如果是 OPTIONS 方式请求 则用实体类接收封装json数据 例如
    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        return null;
    }
~~~