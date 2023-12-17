# Spring Event 与 AOP 结合：优雅记录日志的艺术

>在构建现代化的应用中，日志记录是不可或缺的一环。Spring 框架为我们提供了强大的事件机制（Spring Event）和切面编程（AOP），结合使用可以实现优雅的日志记录，使得代码更加模块化和可维护。本文将介绍如何结合 Spring Event 和 AOP，以及如何在不同场景下应用这两个强大的特性。

今日内容介绍，大约花费9分钟

![](https://files.mdnice.com/user/7954/4cb18c3d-9331-477a-b549-a088dd98b9ed.png)
![](https://files.mdnice.com/user/7954/d00a0d46-a4f6-4df1-b7dd-6ed57747a673.png)

**代码地址**:
```
https://github.com/bangbangzhou/spring-boot-event-log-demo.git
```

## 1.Spring Event 与 AOP 简介

### 1.1. Spring Event
**Spring Event是Spring的事件通知机制，可以将相互耦合的代码解耦**，从而方便功能的修改与添加。Spring Event是监听者模式的一个具体实现。

监听者模式包含了**监听者Listener、事件Event、事件发布者EventPublish，过程就是EventPublish发布一个事件**，被监听者捕获到，然后执行事件相应的方法。

### 1.2. AOP
AOP（Aspect-Oriented Programming）是一种编程范式，它允许我们通过切面（Aspect）将横切关注点（Cross-Cutting Concerns）模块化。切面是一个模块，它定义了在程序中的何处执行横切关注点逻辑。

**AOP作用**:在不修改原始代码的基础上对其进行增强

**应用场景**：

- 事务处理
- 日志记录
- 用户权限
- ......


![](https://files.mdnice.com/user/7954/9dc54e3b-39c1-453d-90b1-7c05c81de75e.png)

[Spring AOP概念全面解析](https://mp.weixin.qq.com/s?__biz=MzIzMjIyNTYwNg==&mid=2247485746&idx=1&sn=206a112b21415170ac44126890f55450&chksm=e899671bdfeeee0da1d1b90e39b75d2242973e1b047c9fda5a12395f67301d0b375618768518#rd)

[SpringBoot-自定义配置类-实现日志记录](https://mp.weixin.qq.com/s?__biz=MzIzMjIyNTYwNg==&mid=2247486073&idx=1&sn=a0bf611fa035c181232baf19be2edec7&chksm=e8996450dfeeed46c9a3cc6b86688e9044e8b98475e4213243950957540588289b132528e196#rd)
Spring Event 和 AOP，我们可以实现在系统关键操作发生时记录日志的功能。这使得日志记录变得更加灵活和可配置，而不需要在每个业务方法中硬编码日志逻辑。

## 2. 代码实现
项目结构如下:

![](https://files.mdnice.com/user/7954/1e4b00ed-d3d0-44ed-86bf-de793fa4b758.png)

### 【步骤0】：创建maven工程`spring-boot-event-log-demo`并配置pom.xml文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <artifactId>spring-boot-starter-parent</artifactId>
        <groupId>org.springframework.boot</groupId>
        <version>2.7.15</version>
    </parent>


    <groupId>com.zbbmeta</groupId>
    <artifactId>spring-boot-event-log-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--        aop-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <!--        fastjson2-->
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
            <version>2.0.35</version>
        </dependency>

        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.20</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
### 【步骤一】：配置application.yml
配置项目信息
```yml
server:
  port: 8890
```


### 【步骤二】：创建OptLogDTO类，用于封装操作日志信息
在`com.zbbmeta.dto`包下创建`OptLogDTO`类
```java
@Data
@Accessors(chain = true)
public class OptLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日志类型
     */
    private String type;

    /**
     * 日志标题
     */
    private String title;
    /**
     * 操作内容
     */
    private String operation;
    /**
     * 执行方法
     */

    private String method;

    /**
     * 请求路径
     */
    private String url;
    /**
     * 参数
     */
    private String params;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 耗时
     */
    private Long executeTime;
    /**
     * 地区
     */
    private String location;
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date startTime;
    /**
     * 更新时间
     */
    private Date endTime;


    /**
     * 异常信息
     */

    private String exception;
}

```

### 【步骤三】：定义事件类
在`com.zbbmeta.event`包下创建事件类`SysLogEvent`
```java
/**
 * 定义系统日志事件
 */
public class SysLogEvent extends ApplicationEvent {
    public SysLogEvent(OptLogDTO optLogDTO) {
        super(optLogDTO);
    }
}

```

### 【步骤四】：定义事件监听器
在`com.zbbmeta.listener`包下创建监听器类`SysLogListener`

**在监听器中可以将日志输出到数据库**
```java

/**
 * 异步监听日志事件
 */
@Component
public class SysLogListener {
    @Async//异步处理
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        OptLogDTO sysLog = (OptLogDTO) event.getSource();
        long id = Thread.currentThread().getId();
        //TODO 可以输出日志到数据库
        System.out.println("监听到日志操作事件：" + sysLog + " 线程id：" + id);
        //将日志信息保存到数据库...
    }
}
```

### 【步骤五】：定义切面
>定义切入点表达式、配置切面(绑定切入点与通知关系)，用于记录每次发送请求时**方法名，参数，时间等信息**

在`com.zbbmeta.aspect`包下创建`LogAspect`类
```java
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private final ApplicationContext applicationContext;


    public LogAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Pointcut("execution(* *..*Controller.*(..))")
    public void pointcut() {
    }

    /**
     * 环绕通知，使用Pointcut()上注册的切入点
     * @param point
     * @return
     */
    @Around("pointcut()")
    public Object recordLog(ProceedingJoinPoint point) throws Throwable {
        Object result = new Object();

        //　获取request
        HttpServletRequest request = RequestHolder.getHttpServletRequest();


        // 判断为空则直接跳过执行
        if (ObjectUtils.isEmpty(request)){
            return point.proceed();
        }
        //　获取注解里的value值
        Method targetMethod = resolveMethod(point);
        // 打印执行时间
        Date now = DateUtil.date();
        // 请求方法
        String method = request.getMethod();
        String url = request.getRequestURI();

        // 获取IP和地区
        String ip = RequestHolder.getHttpServletRequestIpAddress();
        String region = IPUtil.getCityInfo(ip);

        //获取请求参数
        // 参数
        Object[] args = point.getArgs();
        String requestParam = getArgs(args, request);
        Date end = null;
        // 计算耗时
        long tookTime = 0L;
        try {
            result = point.proceed();
        } finally {
            end = DateUtil.date();

            tookTime = DateUtil.between(now, end, DateUnit.SECOND);
        }
        //　如果是登录请求，则不获取用户信息
        String userName = "springboot葵花宝典";
        //　封装optLogDTO
        OptLogDTO optLogDTO = new OptLogDTO();
        optLogDTO.setIp(ip)
                .setCreateBy(userName)
                .setMethod(method)
                .setUrl(url)
                .setStartTime(now)
                .setEndTime(end)
                .setType("1")
                .setOperation(String.valueOf(result))
                .setLocation(StrUtil.isEmpty(region) ? "本地" : region)
                .setExecuteTime(tookTime)
                .setParams(JSON.toJSONString(requestParam));


        ApplicationEvent event = new SysLogEvent(optLogDTO);

        //发布事件
        applicationContext.publishEvent(event);

        long id = Thread.currentThread().getId();
        System.out.println("发布事件,线程id：" + id);


        return result;
    }

    /**
     * 配置异常通知
     *
     * @param point join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint point, Throwable e) {
        // 打印执行时间
        long startTime = System.nanoTime();

        Date now = DateUtil.date();

        OptLogDTO optLogDTO = new OptLogDTO();

        // 获取IP和地区
        String ip = RequestHolder.getHttpServletRequestIpAddress();
        String region = IPUtil.getCityInfo(ip);


        //　获取request
        HttpServletRequest request = RequestHolder.getHttpServletRequest();

        // 请求方法
        String method = request.getMethod();
        String url = request.getRequestURI();

        //　获取注解里的value值
        Method targetMethod = resolveMethod((ProceedingJoinPoint) point);

        optLogDTO.setExecuteTime( DateUtil.between(now, DateUtil.date(), DateUnit.SECOND))
                .setIp(ip)
                .setLocation(region)
                .setMethod(method)
                .setUrl(url)
                .setType("2")
                .setException(getStackTrace(e));
        // 发布事件
        log.info("Error Result: {}", optLogDTO);
        ApplicationEvent event = new SysLogEvent(optLogDTO);

        //发布事件
        applicationContext.publishEvent(event);

        long id = Thread.currentThread().getId();
        System.out.println("发布事件,线程id：" + id);
    }

    private Method resolveMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();

        Method method = getDeclaredMethod(targetClass, signature.getName(),
                signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("无法解析目标方法: " + signature.getMethod().getName());
        }
        return method;
    }

    /**
     * 获取堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    private Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethod(superClass, name, parameterTypes);
            }
        }
        return null;
    }

    /**
     * 获取请求参数
     * @param args
     * @param request
     * @return
     */
    private String getArgs(Object[] args, HttpServletRequest request) {
        String strArgs = StrUtil.EMPTY;

        try {
            if (!request.getContentType().contains("multipart/form-data")) {
                strArgs = JSONObject.toJSONString(args);
            }
        } catch (Exception e) {
            try {
                strArgs = Arrays.toString(args);
            } catch (Exception ex) {
                log.warn("解析参数异常", ex);
            }
        }
        return strArgs;
    }
}

```
<font color="red" size="4">注意:指令使用到了IPUtil和RequestHolder工具类，就不具体实现了，可以带代码仓获取代码进行查看</font>

### 【步骤六】：创建Controller

```java
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ApplicationContext applicationContext;
    @GetMapping("/getUser")
    public String getUser(){
        return "OK";
    }

    @GetMapping("/name")
    public String getName(String name){
        return "OK";
    }
}
```

### 【步骤七】：创建启动类
```java
@SpringBootApplication
@EnableAsync//启用异步处理
public class EventListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventListenerApplication.class,args);
    }
}
```

## 3.测试
启动项目并访问Controller可以发现监听器触发了
**使用postman发送请求**:`http://localhost:8890/user/name?name="张三"`

![](https://files.mdnice.com/user/7954/51923861-c501-4539-8fe5-f1ca878beb22.png)

**在控制台显示如下信息，也可以自己将日志输出到你想输出的地方，比如mysql**

![](https://files.mdnice.com/user/7954/e94353a3-ef0d-49a6-92c7-b3589efdda8d.png)

```
https://github.com/bangbangzhou/spring-boot-event-log-demo.git
```

![](https://files.mdnice.com/user/7954/d00a0d46-a4f6-4df1-b7dd-6ed57747a673.png)