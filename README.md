# Spring Event 与 AOP 结合：优雅记录日志的艺术

>在构建现代化的应用中，日志记录是不可或缺的一环。Spring 框架为我们提供了强大的事件机制（Spring Event）和切面编程（AOP），结合使用可以实现优雅的日志记录，使得代码更加模块化和可维护。本文将介绍如何结合 Spring Event 和 AOP，以及如何在不同场景下应用这两个强大的特性。

![](https://files.mdnice.com/user/7954/d00a0d46-a4f6-4df1-b7dd-6ed57747a673.png)

**代码地址**:

[25-sprongboot-starter-test](https://github.com/bangbangzhou/learn_springboot/tree/main/day25-sprongboot-starter-test)

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

Spring Event 和 AOP，我们可以实现在系统关键操作发生时记录日志的功能。这使得日志记录变得更加灵活和可配置，而不需要在每个业务方法中硬编码日志逻辑。

## 2. 代码实现
项目结构如下:

![](https://files.mdnice.com/user/7954/1e4b00ed-d3d0-44ed-86bf-de793fa4b758.png)





























![](https://files.mdnice.com/user/7954/d00a0d46-a4f6-4df1-b7dd-6ed57747a673.png)