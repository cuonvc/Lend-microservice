package com.lender.authservice.config;

import com.lender.authservice.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public class CustomerUserDetail implements UserDetails {

    private String email;
    private String password;
    private Set<Role> roles;

    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public CustomerUserDetail(String email, String password, Set<Role> roles, Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
