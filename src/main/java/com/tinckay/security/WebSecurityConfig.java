package com.tinckay.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by root on 1/9/17.
 */
@Configuration
//@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{



    @Autowired
    private CustomUserDetailsService userDetailsService;
    //@Autowired
    //SecurityContextHolderAwareRequestFilter logFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(provider);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.addFilter(logFilter);
        http
                .authorizeRequests().antMatchers("/", "/index.html","/module/*.*").authenticated()
                .and()
                .csrf().disable().formLogin().loginPage("/login.html").failureUrl("/capi/User/loginFailure")
                .loginProcessingUrl("/capi/User/loginCheck").permitAll()
                .and()
                // logout
                .logout().logoutUrl("/capi/User/logout").logoutSuccessUrl("/login.html").invalidateHttpSession(true)
                .and()
                .sessionManagement().sessionFixation().changeSessionId();
    }

   /**
    @Autowired
    private MyUserDetailsService myUserDetailsService;//自定义用户服务


    //http://localhost:8080/login 输入正确的用户名密码 并且选中remember-me 则登陆成功，转到 index页面
    //再次访问index页面无需登录直接访问
    //访问http://localhost:8080/home 不拦截，直接访问，
    //访问http://localhost:8080/hello 需要登录验证后，且具备 “ADMIN”权限hasAuthority("ADMIN")才可以访问

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/").permitAll()//访问：/home 无需登录认证权限
            .anyRequest().authenticated()//其他所有资源都需要认证，登陆后访问
                //.antMatchers("/hello").hasAuthority("ADMIN") //登陆后之后拥有“ADMIN”权限才可以访问/hello方法，否则系统会出现“403”权限不足的提示
            .and()
                .formLogin()
                .loginPage("/login.html")//指定登录页是”/login”
                .failureUrl("/loginfailure")
                .loginProcessingUrl("/capi/User/loginCheck")
                .permitAll()
                //.successHandler(loginSucessHandler())//登录成功后可使用loginSuccessHandler()存储用户信息，可选。
            .and()
                .logout()
                .logoutUrl("/capi/User/logout")
                .logoutSuccessUrl("/login.html")//退出登录后的默认网址是”/”
                .permitAll()
                .invalidateHttpSession(true);
            //.and()
            //    .rememberMe()//登录后记住用户，下次自动登录,数据库中必须存在名为persistent_logins的表
            //    .tokenValiditySeconds(1209600);

        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER");
//        auth.userDetailsService(userDetailsService());

        //将验证过程交给自定义验证工具
        //auth.authenticationProvider(myAuthenticationProvider);

        //指定密码加密所使用的加密器为passwordEncoder()
        //需要将密码加密后写入数据库
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(false);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

//    @Bean
//    public LoginSuccessHandler loginSuccessHandler(){
//        return new LoginSuccessHandler();
//    }
    */
}
