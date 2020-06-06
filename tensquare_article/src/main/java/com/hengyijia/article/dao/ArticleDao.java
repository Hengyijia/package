package com.hengyijia.article.dao;

import com.hengyijia.article.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    /**
     * 通过id更新审核状态，1为审核，0为未审核
     * @param id
     */
    @Modifying
    @Query(value = "UPDATE tb_article SET state = 1 WHERE id = ?",nativeQuery = true)
    public void updateState(String id);

    /**
     * 通过id更新点赞数，点一次加1    #数据库必须给初始值 null不能累加
     * @param id
     */
    @Modifying
    @Query(value = "UPDATE tb_article SET thumbup = thumbup+1 WHERE id = ?",nativeQuery = true)
    public void updateThumbup(String id);
}
