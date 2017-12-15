package com.cd2cd.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.cd2cd.domain.SysAuthority;
import com.cd2cd.domain.SysUser;
import com.cd2cd.domain.SysUserRoleRel;
import com.cd2cd.domain.gen.SysUserCriteria;
import com.cd2cd.domain.gen.SysUserRoleRelCriteria;
import com.cd2cd.mapper.SysAuthorityMapper;
import com.cd2cd.mapper.SysUserMapper;
import com.cd2cd.mapper.SysUserRoleRelMapper;

@Component
public class MyUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysAuthorityMapper sysAuthorityMapper;
	
	@Autowired
	private SysUserRoleRelMapper sysUserRoleRelMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		
		SysUserCriteria example = new SysUserCriteria();
		example.createCriteria()
		.andUsernameEqualTo(username);
		
		List<SysUser> users = sysUserMapper.selectByExample(example);
		
		boolean hasUser = ! users.isEmpty();
				
		logger.info("hasUser={}", hasUser);
		
		if( hasUser ) {
			
			SysUser sysUser = users.get(0);
			Integer userId = sysUser.getId();
			boolean enabled = "enable".equals(sysUser.getStatus()) ? true : false;
			
			logger.info("userId={}, enabled={}", userId, enabled);
			if( enabled ) {
				// 查询用户所有权限
				List<SysAuthority> list = sysAuthorityMapper.userAuthority(sysUser.getId());
				for(SysAuthority auth: list) {
					String role = auth.getGuid();
					String name = auth.getName();
					String url = auth.getUrl();
					
					logger.info("Authorities username={}, name={}, role={}, url={}", username, name, role, url);
					
					authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
				}
			}
			
			String password = sysUser.getPassword();
			LoginUser user = new LoginUser(
	                username, 
	                password,
	                enabled, // 是否可用
	                true, // 是否过期
	                true, // 证书不过期为true
	                true, // 账户未锁定为true
                authorities);
			
			// - - - - - 设置用户角色 - - - - - 
			SysUserRoleRelCriteria condition = new SysUserRoleRelCriteria();
			condition.createCriteria().andUserIdEqualTo(userId);
			List<SysUserRoleRel> urList = sysUserRoleRelMapper.selectByExample(condition);
			List<Integer> roleIds = new ArrayList<Integer>();
			for(SysUserRoleRel userRole: urList) {
				Integer roleId = userRole.getRoleId();
				roleIds.add(roleId);
			}
			user.setRoleIds(roleIds);
			
			return user;
		} else {
			throw new UsernameNotFoundException("用户名或密码不正确"); 
		}
	}
}
