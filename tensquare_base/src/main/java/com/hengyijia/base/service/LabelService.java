package com.hengyijia.base.service;

import com.hengyijia.base.dao.LabelDao;
import com.hengyijia.base.pojo.Label;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll(){
        return labelDao.findAll();
    }

    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    public void save(Label label){
        //ID生成器
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    //save既能做保存也能做更新
    public void update(Label label){
        labelDao.save(label);
    }
    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    /**
     * 条件查询
     * @param label
     * @return
     */
    public List<Label> findSearch(Label label){
       return labelDao.findAll(new Specification<Label>() {
           /**
            * @param root 根对象，也就是要把条件封装到哪个对象中。
            * @param cq   封装的都是查询条件关键字
            * @param cb   封装条件对象，如果直接返回null，则表示不需要任何条件
            * @return
            */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                //用一个集合，来存放所有的条件
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(label.getLabelname())){
                    //相当于 where labelname like "%#{labename}%"
                    Predicate predicate = cb.like(root.get("labelname").as(String.class),"%" + label.getLabelname() + "%");
                    list.add(predicate);
                }

                if(!StringUtils.isBlank(label.getState())){
                    //相当于 state = #{state}
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());
                    list.add(predicate);
                }
                //用一个数组来作为最终返回值的条件
                Predicate[] predicates = new Predicate[list.size()];
                //list转成数组
                list.toArray(predicates);
                //and相当于where
                return cb.and(predicates);
            }
        });
    }

    /**
     * 根据条件和分页查询
     * @param label
     * @param page
     * @param size
     * @return
     */
    public Page<Label> pageQuery(Label label, Integer page, Integer size) {

        //封装一个分页对象，page指定-1，是因为默认从0开始的
        Pageable pageable = PageRequest.of(page-1, size);

        return labelDao.findAll(new Specification<Label>() {
            /**
             * @param root 根对象，也就是要把条件封装到哪个对象中。
             * @param cq   封装的都是查询条件关键字
             * @param cb   封装条件对象，如果直接返回null，则表示不需要任何条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                //用一个集合，来存放所有的条件
                List<Predicate> list = new ArrayList<>();
                if(!StringUtils.isBlank(label.getLabelname())){
                    //相当于 where labelname like "%#{labename}%"
                    Predicate predicate = cb.like(root.get("labelname").as(String.class),"%" + label.getLabelname() + "%");
                    list.add(predicate);
                }

                if(!StringUtils.isBlank(label.getState())){
                    //相当于 state = #{state}
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());
                    list.add(predicate);
                }
                //用一个数组来作为最终返回值的条件
                Predicate[] predicates = new Predicate[list.size()];
                //list转成数组
                list.toArray(predicates);
                //and相当于where
                return cb.and(predicates);
            }
        },pageable);

    }
}
