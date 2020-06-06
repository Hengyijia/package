package com.hengyijia.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 在请求前pre或者请求后post执行
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 多个过滤器的顺序，数字越小越先执行
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 当前过滤器是否开启，true表示开启
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的操作 return任何object的值都表示继续执行
     * setSendZuulResponse(false)表示不再继续执行    null会继续执行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //先得到request上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //request域
        HttpServletRequest request = currentContext.getRequest();
        //OPTIONS请求直接放行
        if(request.getMethod().equals("OPTIONS")){
            return null;
        }
        //如果请求的路径中带login 表示是登陆请求直接放行
        if(request.getRequestURI().indexOf("login") > 0){
            return null;
        }
        //得到头信息
        String header = request.getHeader("Authorization");
        //判断是否有头信息
        if(StringUtils.isNotBlank(header)){
            //如果包含有Bearer + 空格才取得token
            if(header.startsWith("Bearer ")){
                //从请求头第7位开始截取取得token令牌
                String token = header.substring(7);
                try {
                    //对令牌进行解析验证
                    Claims claims = jwtUtil.parseJWT(token);
                    //获得角色类型
                    String roles = (String) claims.get("roles");
                    if(roles.equals("admin")){
                        //如果是admin，转发头信息并且放行
                        currentContext.addZuulRequestHeader("Authorization", header);
                        return null;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    currentContext.setSendZuulResponse(false);  //终止运行
                }
            }
        }
        currentContext.setSendZuulResponse(false);  //终止运行
        currentContext.setResponseStatusCode(403);  //错误码提示
        currentContext.setResponseBody("权限不足");  //错误信息
        currentContext.getResponse().setContentType("text/html;charset=utf-8"); //设置ResponseBody编码
        return null;
    }
}
