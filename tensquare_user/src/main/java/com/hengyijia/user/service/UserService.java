package com.hengyijia.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import com.hengyijia.user.dao.UserDao;
import com.hengyijia.user.pojo.User;
import util.JwtUtil;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class UserService {


	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private HttpServletRequest servletRequest;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );	//ID
		user.setPassword(encoder.encode(user.getPassword()));//密码加密
		user.setFanscount(0);				//粉丝数
		user.setFollowcount(0);				//关注数
		user.setOnline(0L);					//在线时长
		user.setRegdate(new Date());		//注册日期
		user.setUpdatedate(new Date());		//更新日期
		user.setLastdate(new Date());		//最后登录日期
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除，必须是admin才能删除用户
	 * @param id
	 */
	public void deleteById(String id) {
		String token = (String) servletRequest.getAttribute("claims_admin");
		if(StringUtils.isBlank(token)){
			throw new RuntimeException("权限不足！");
		}

		//通过拦截器，省略以下代码
		/*String header = servletRequest.getHeader("Authorization");
		//如果请求头为空，则权限不足
		if(header == null || "".equals(header)){
			throw new RuntimeException("权限不足！");
		}
		//如果不是Bearer + 空格，则权限不足
		if(!header.startsWith("Bearer ")){
			throw new RuntimeException("权限不足！");
		}
		//从请求头7位后面开始截取，得到token
		String token = header.substring(7);

		//这里token有效时间过了可能会出异常
		try {
			Claims claims = jwtUtil.parseJWT(token);
			//获取用户类型
			String roles = (String) claims.get("roles");
			//如果类型为空，或者类似不是admin，则权限不足
			if(roles == null || !roles.equals("admin")){
				throw new RuntimeException("权限不足！");
			}

		} catch (Exception e){
			throw new RuntimeException("权限不足！");
		}*/
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

    public void sendSms(String mobile) {
		//生成6位随机数字验证码
		String checkCode = RandomStringUtils.randomNumeric(6);

		//缓存中放一份
		redisTemplate.opsForValue().set("checkCode_"+mobile,checkCode,6,TimeUnit.HOURS);	//6小时后失效

		//给用户发一份
		Map<String,String> map = new HashMap<>();
		map.put("mobile", mobile);
		map.put("checkCode", checkCode);
		rabbitTemplate.convertAndSend("sms",map);
		//控制台打印一份（方便测试）
		System.out.println("验证码：" + checkCode);
    }

	/**
	 * 通过手机号和密码 用户登陆
	 * @param mobile
	 * @param password
	 * @return
	 */
	public User login(String mobile, String password) {
		User userLogin = userDao.findByMobile(mobile);
		if(userLogin != null && encoder.matches(password, userLogin.getPassword())){
			return userLogin;
		}
		return null;
	}

	/**
	 * 更新粉丝数和关注数    此方法是friend模块调用的
	 * @param x
	 * @param userid
	 * @param friendid
	 */
	public void updateFanscountAndFollowcount(Integer x, String userid, String friendid) {
		//好友的粉丝+1
		userDao.updateFanscount(x, friendid);
		//自己的关注数+1
		userDao.updateFollowcount(x, userid);
	}
}
