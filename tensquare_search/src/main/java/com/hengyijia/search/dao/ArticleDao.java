package com.hengyijia.search.dao;
import com.hengyijia.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleDao extends ElasticsearchRepository<Article,String> {
    /**
     * 根据标题或者内容模糊查询（关键字搜索）
     * @return
     */
    public Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);
}
