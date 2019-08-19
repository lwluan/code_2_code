package com.cd2cd.admin.security;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cd2cd.admin.component.EntryPointUnauthorizedHandler;
import com.cd2cd.admin.component.RestAccessDeniedHandler;
import com.cd2cd.admin.domain.SysAuthority;
import com.cd2cd.admin.mapper.SysAuthorityMapper;
import com.google.common.collect.Sets;


/**
 * RBAC控制 <br>
 * 权限加载 用户的角色改变或角色的权限改变都需在更新所有登录用户的权限
 */
@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

	public static final String DONT_AUTHENTICATION_PROFIX = "/comm";
	private static final Set<String> PERMIT_ALL_URL = Sets.newHashSet(
		"/admin/**", DONT_AUTHENTICATION_PROFIX+"/**"
	);
	private static Logger logger = LoggerFactory.getLogger(ApplicationSecurity.class);

	@Autowired
	private SysAuthorityMapper sysAuthorityMapper;

	@Resource
	private JWTAuthenticationFilter jWTAuthenticationFilter;

	@Resource
	private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

	@Resource
	private RestAccessDeniedHandler restAccessDeniedHandler;

	private SessionRegistry sessionRegistry;
	@Bean
	protected SessionRegistry getSessionRegistry() {
		sessionRegistry = new SessionRegistryImpl();
		return sessionRegistry;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.headers().frameOptions().disable();
		http.csrf().disable();

		initAuthoritys(http); // 初始化权限

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests()
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		.antMatchers(PERMIT_ALL_URL.toArray(new String[]{})).permitAll()
		.anyRequest().authenticated()
		.and().headers().cacheControl();

		http.addFilterBefore(jWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.exceptionHandling()
		.authenticationEntryPoint(entryPointUnauthorizedHandler)
		.accessDeniedHandler(restAccessDeniedHandler);
		
	}

	/**
	 * 初始化系统权限内容
	 * @param http
	 */
	private void initAuthoritys(HttpSecurity http) {
		List<SysAuthority> list = sysAuthorityMapper.selectByExample(null);
		try {
			for (SysAuthority auth : list) {
				String role = auth.getGuid();
				String name = auth.getName();
				String url = auth.getUrl();
				http.authorizeRequests().antMatchers(url).hasRole(role);
				logger.info("init all Authorities, name={}, role={}, url={}", name, role, url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}