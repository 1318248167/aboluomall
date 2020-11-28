# aboluomall
提交测试
常见异常解决
一.{
java.lang.NoClassDefFoundError: com/alibaba/spring/beans/factory/annotation/AnnotationInjectedBeanPostProcessor
解决方法：添加依赖：  <dependency>
                      <groupId>com.alibaba.spring</groupId>
                      <artifactId>spring-context-support</artifactId>
                      <version>1.0.2</version>
                  </dependency>
}
二.{
java.lang.NoClassDefFoundError: io/netty/channel/EventLoopGroup
解决方法：添加依赖：    <dependency>
                        <groupId>io.netty</groupId>
                        <artifactId>netty-all</artifactId>
                        <version>4.1.32.Final</version>
                    </dependency>
}
三.{
com.alibaba.dubbo.rpc.RpcException: No provider available from registry 192.168.213.132:2181 for service com.aboluo.amall.api.service.CatalogService on consumer 192.168.213.1 use dubbo version 2.6.5, please check status of providers(disabled, not registered or in blacklist)
解决方法：启动类上加 MapperScan @MapperScan(basePackages = "com.aboluo.amall.manage.mapper")
}
四.{
跨域问题
在Controller加入 @CrossOrigin注解
}