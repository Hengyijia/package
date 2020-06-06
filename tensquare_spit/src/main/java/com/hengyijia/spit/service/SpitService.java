package com.hengyijia.spit.service;

import com.hengyijia.spit.dao.SpitDao;
import com.hengyijia.spit.pojo.Spit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询所有
     * @return
     */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    /**
     * 根据id查询实体
     * @param id
     * @return
     */
    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    /**
     * 添加
     * @param spit
     * @return
     */
    public void add(Spit spit){
        spit.set_id(idWorker.nextId() + "");
        spit.setPublishtime(new Date()); //发布日期
        spit.setVisits(0);  //浏览量初始化为0
        spit.setShare(0);   //分享数初始化为0
        spit.setThumbup(0); //点赞数初始化为0
        spit.setComment(0); //回复数初始化为0
        spit.setState("1"); //状态初始化为1

        //如果当前的吐槽，有父节点，那么父节点的吐槽回复数要加一
        if(!StringUtils.isBlank(spit.getParentid())){

            spit.setComment(spit.getComment() + 1);
         /*   Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));

            Update update = new Update();
            update.inc("comment",2);

            mongoTemplate.updateFirst(query, update, "spit");*/
        }
        spitDao.save(spit);
    }

    /**
     * 根据id删除
     * @param id
     */
    public void deleteById(String id){
        spitDao.deleteById(id);
    }

    /**
     * 更新
     * @param spit
     */
    public void update(Spit spit){
        spitDao.save(spit);
    }

    /**
     * 根据parentid分页查询
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentid(String parentid, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page -1, size);
        return spitDao.findByParentid(parentid,pageable);
    }

    /**
     *根据id点赞
     * @param spitId
     */
    public void thumbup(String spitId){

        //方式一，效率不高
        //Spit spit = spitDao.findById(spitId).get();

        //如果点赞数为null，就设置为0
        /*if(spit.getThumbup() == null){
            spit.setThumbup(0);
        }
        spit.setThumbup(spit.getThumbup() + 1);
        spitDao.save(spit);*/

        //方法二，使用原生mongo命令实现自增
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");
    }
}
