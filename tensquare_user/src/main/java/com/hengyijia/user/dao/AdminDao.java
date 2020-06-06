package com.hengyijia.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hengyijia.user.pojo.Admin;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface AdminDao extends JpaRepository<Admin,String>,JpaSpecificationExecutor<Admin>{

    public Admin findByLoginname(String loginname);
}
