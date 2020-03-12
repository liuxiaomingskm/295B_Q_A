package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by nowcoder on 2016/7/3.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String ticket = null;
        if (httpServletRequest.getCookies() != null) { //一个request里面有很多cookies，需要一个一个判断是不是该页面的cookie
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {// 找到ticket对应的数据库记录
                //然后判断是否存在该记录 是否过期，状态是否无效 1有效 0无效
                return true; //这里为什么return true 而不是false？ 3月11日感悟：虽然查不到用户身份，但还是允许用户继续访问和注册身份的，所以返回true
            }

            User user = userDAO.selectById(loginTicket.getUserId()); // 排除上诉无cookie，cookie过期等情况后 该cookie必然有对应的user，所以从数据库读取该用户
            hostHolder.setUser(user); //在拦截器最早的时候，截获这个user 并把它放进hostholder中，从而在之后其他地方可以访问
        }
        return true;
    }

    @Override //postHandle渲染之前
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser()); // 在所有的页面渲染之前 把user加入到model中，感觉就省了点代码量，可加可不加。。。
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
