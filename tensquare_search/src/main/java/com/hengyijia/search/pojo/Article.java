package com.hengyijia.search.pojo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
@Data
@Document(indexName = "tensquare_article",type = "article")
public class Article implements Serializable {

    @Id
    private String id;      //id

    //是否索引，就是看该域是否能被搜索
    //是否分词，就表示搜索的时候是整体匹配，还是单词匹配
    //是否存储，是否在页面上显示
    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String title;   //标题

    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String content; //内容

    private String state;   //审核状态
}
