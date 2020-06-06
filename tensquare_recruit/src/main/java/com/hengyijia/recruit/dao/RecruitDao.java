package com.hengyijia.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hengyijia.recruit.pojo.Recruit;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{

    /**
     * 根据状态state查询最顶上的6个数据根据创建时间倒叙排序  1开启，0关闭，2推荐
     * @param state
     * @return
     */
    public List<Recruit> findTop6ByStateOrderByCreatetimeDesc(String state);

    /**
     * 根据状态state查询最新发布的6个可用职位根据时间倒叙排序  1开启，0关闭，2推荐   不等于0表示可用
     * @param state
     * @return
     */
    public List<Recruit> findTop6ByStateNotOrderByCreatetimeDesc(String state);
}
