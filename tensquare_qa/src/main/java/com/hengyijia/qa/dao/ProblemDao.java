package com.hengyijia.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hengyijia.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    /**
     * 根据标签ID分页查询问题列表 并用回复时间倒叙排序
     * @param labelId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM tb_problem,tb_pl WHERE id = problemid AND labelid = ? ORDER BY replytime DESC",nativeQuery = true)
    public Page<Problem> findNewList(String labelId, Pageable pageable);

    /**
     * 根据标签ID分页查询问题列表并用 回复数量 进行倒叙排序(热门问题)
     * @param
     *
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM tb_problem,tb_pl WHERE id = problemid AND labelid = ? ORDER BY reply DESC",nativeQuery = true)
    public Page<Problem> findHotList(String labelId, Pageable pageable);

    /**
     * 根据标签ID分页查询 待回复 的问题列表  并用创建时间倒叙排序
     * @param labelId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT * FROM tb_problem,tb_pl WHERE id = problemid AND labelid = ? AND reply = 0 ORDER BY createtime DESC",nativeQuery = true)
    public Page<Problem> findWaitList(String labelId, Pageable pageable);

}
