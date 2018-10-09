package com.cd2cd.admin.security;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import com.cd2cd.admin.domain.SysAuthority;
import com.cd2cd.admin.mapper.SysAuthorityMapper;

/**
 * RBAC控制 <br>
 * 权限加载 用户的角色改变或角色的权限改变都需在更新所有登录用户的权限
 */
@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

	private static Logger logger = LoggerFactory.getLogger(ApplicationSecurity.class);

	@Resource
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private SysAuthorityMapper sysAuthorityMapper;

	private SessionRegistry sessionRegistry;
	@Bean
	protected SessionRegistry getSessionRegistry() { 
		sessionRegistry = new SessionRegistryImpl();
		return sessionRegistry;
	}
	
	@Override  
    public void configure(WebSecurity web) throws Exception {  
        // 设置不拦截规则  
//        web.ignoring().antMatchers("/");  
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.headers().frameOptions().disable();
		http.csrf().disable(); // 禁用 CSRF
		http.authorizeRequests()
				.antMatchers("/css/**", "/js/**", "/images/**" // 静态资源
				).permitAll();

		// 获取全部权限
		List<SysAuthority> list = sysAuthorityMapper.selectByExample(null);

		// 初始化系统权限内容
		for (SysAuthority auth : list) {
			String role = auth.getGuid();
			String name = auth.getName();
			String url = auth.getUrl();

			http.authorizeRequests().antMatchers(url).hasRole(role);

			logger.info("init all Authorities, name={}, role={}, url={}", name, role, url);
		}

		http.authorizeRequests().anyRequest().fullyAuthenticated().and()
				.formLogin().loginPage("/html/login.html")
				.failureUrl("/html/login.html?error=true")
				.usernameParameter("app_username")
				.passwordParameter("app_password")
				.defaultSuccessUrl("/html/index.html").permitAll().and().logout()
				.permitAll();

		// 无访问权限返回
		MyAccessDeniedHandler accessDeniedHandler = new MyAccessDeniedHandler();
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
		http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry);
		
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService).passwordEncoder(
				new MyMd5PasswordEncoder());

	}
}