# 第8次作业

不限制用户自定义业务场景使用，需要公用一个 HttpSecurity 因此多个WebSecurityConfigurerAdapter在创建的时候使用同一个，但首先创建的adapter需要给定最大范围的配置，后续的HttpSecurity无法覆盖configure(HttpSecurity http)进行配置

`org.geektimes.projects.user.web.security.WebSecurityConfigurer
org.geektimes.projects.user.web.security.WebSecurityConfigurerBeanPostProcessor`
