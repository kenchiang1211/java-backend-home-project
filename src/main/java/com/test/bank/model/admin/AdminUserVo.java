package com.test.bank.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;

import java.security.Principal;
import java.util.Set;

public class AdminUserVo implements Principal {

    @JsonIgnore
    @Override
    public String getName() {
        return null;
    }

    private Integer id;

    private String account;

    private String password;

    private Set<String> roleSet = Sets.newHashSet();

    private String salt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<String> roleSet) {
        this.roleSet = roleSet;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
