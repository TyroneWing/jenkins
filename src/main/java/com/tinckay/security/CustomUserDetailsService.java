package com.tinckay.security;

import com.tinckay.domain.base.Role;
import com.tinckay.domain.base.User;
import com.tinckay.domain.jpa.RoleRepo;
import com.tinckay.domain.jpa.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by root on 1/18/17.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;


    /**
     * 校验用户角色
     */
    @Override
    public SecurityUser loadUserByUsername(String username) throws AuthenticationException {
        //User user = userRepoDao.findOneByName(username);
        User user = userRepo.findTopByNumOrCodeOrNameOrMobileOrEmail(username,username,username,username,username);
        if (user != null) {
            if(user.isLeave())
                throw new LockedException("用户【" + username + "】 已离职，账户被锁定。");
            Role role = roleRepo.findOne(user.getRoleId());
            if(null == role)
                throw new UsernameNotFoundException("用户【" + username + "】 未设定角色。");
            user.setRoleName(role.getName());

            return new SecurityUser(user);
        }
        throw new UsernameNotFoundException("用户【" + username + "】 不存在。");
    }
}
