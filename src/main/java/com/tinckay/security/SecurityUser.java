package com.tinckay.security;

import com.tinckay.domain.base.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by root on 1/18/17.
 */
public class SecurityUser extends User implements UserDetails{

    public SecurityUser(User user) {
        if(user != null)
        {
            this.setId(user.getId());
            this.setCode(user.getCode());
            this.setNum(user.getNum());
            this.setName(user.getName());
            this.setEmail(user.getEmail());
            this.setMobile(user.getMobile());
            this.setPassword(user.getPassword());
            this.setRoleId(user.getRoleId());
            this.setRoleName(user.getRoleName());


        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if(!this.getRoleName().equals(""))
        {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.getRoleName());
            authorities.add(authority);
        }
        return authorities;
    }



    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
