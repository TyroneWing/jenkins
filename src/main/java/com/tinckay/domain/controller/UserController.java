package com.tinckay.domain.controller;


import com.tinckay.common.ErrorHandlerController;
import com.tinckay.security.SecurityUser;
import com.tinckay.common.BeanRefUtil;
import com.tinckay.common.ResponseObj;
import com.tinckay.domain.base.User;
import com.tinckay.domain.jpa.UserRepo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by root on 1/6/17.
 */
@Controller
@RequestMapping("/capi/User")
public class UserController extends ErrorHandlerController {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepo userRepo;

    private Logger logger = Logger.getLogger(this.getClass());

    private ResponseObj checkFieldInfo(User user){
        String num = user.getNum();
        if(null == num || num.trim().equals(""))
            return new ResponseObj(-1,"人员【工号】不能为空，操作失败。", null);


        String name = user.getName();
        if(null == name || name.trim().equals(""))
            return new ResponseObj(-1,"人员【名称】不能为空，操作失败。", null);

        String code = user.getCode();
        if(null == code || code.trim().equals(""))
            return new ResponseObj(-1,"人员【简称】不能为空，操作失败。", null);

        String mobile = user.getMobile();
        if(null == mobile || mobile.trim().equals(""))
            return new ResponseObj(-1,"人员【手机号码】不能为空，操作失败。", null);
        String email = user.getEmail();
        if(null == email || email.trim().equals(""))
            return new ResponseObj(-1,"人员【邮箱地址】不能为空，操作失败。", null);
//        String password = user.getPassword();
//        if(null == password || password.trim().equals(""))// || password.length() <= 5)
//            return new ResponseObj(-1,"人员【密码】不能为空，操作失败。", null);

        return null;
    }
    /**
     * 新增人员信息
     * @param user
     * @return
     */



    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @Transactional
    public @ResponseBody ResponseObj add(@RequestBody User user) {


        if(null == user)
            return new ResponseObj(-1,"无法获取新增人员信息，操作失败。", null);


        ResponseObj responseObj = checkFieldInfo(user);
        if(null != responseObj)
            return responseObj;

        //user.setDepartmentId(1);
        user.setLeave(false);
        //user.setRoleId(1);
        user.setCreateTime(new Date());
        userRepo.save(user);
        return new ResponseObj("新增人员【" + user.getName() + "】成功",user);
    }


    /**
     * 编辑任务信息
     * @param user
     * @return 成功：projectVo
     */
    @RequestMapping(method = RequestMethod.POST, value = "/modify")
    public @ResponseBody ResponseObj modify(@RequestBody User user) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{

        if(null == user)
            return new ResponseObj(-1,"人员编辑【信息】为空，操作失败。", null);

        User editUser  = userRepo.findOne(user.getId());
        if(null == editUser)
            return new ResponseObj(-1,"无法从数据库获取对应的人员信息，操作失败。", null);


        ResponseObj responseObj = checkFieldInfo(user);
        if(null != responseObj)
            return responseObj;

        BeanRefUtil.copyProperty(editUser,user);

//        //修改机制的确定，项目管理员的变更（编号/名称/管理员/计划时间/实际时间/状态变更）
//
//

//        if(!editUser.getNum().equals(user.getNum()))
//            editUser.setNum(user.getNum());
//
//        if(!editUser.getCode().equals(user.getCode()))
//            editUser.setCode(user.getCode());
//

        userRepo.save(editUser);
        return new ResponseObj("用户【" + editUser.getName() + "】信息修改成功。",editUser);
    }

    /**
     * 删除人员信息
     * @param id（user 的 key id）
     * @return
     */
    @RequestMapping(value = "/delete/{id}")
    @Transactional
    public @ResponseBody ResponseObj delete(@PathVariable int id) {

        User user  = userRepo.findOne(id);
        if(null != user) {
            String name = user.getName();
            userRepo.delete(user);
            return new ResponseObj("人员【" + name  + "】删除成功");
        }
        else{
            return new ResponseObj(-1,"未找到Id【" + id + "】对应的人员，无法删除。", null);
        }

    }





    /**
     * 获取人员信息
     * @param id
     * @return 成功：对应的用户信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/get/{id}")
    public @ResponseBody User get(@PathVariable int id) {
        return userRepo.findOne(id);
    }




//    @RequestMapping(value = "/login")
//    public @ResponseBody ResponseObj login(@RequestParam String userName, @RequestParam String passWord, HttpServletRequest request){
//        User user = userRepo.findByName(userName);
//        if(null == user)
//            return new ResponseObj(-1,"用户【" + userName + "】不存在。",null);
//
//        if(user.getPassword().equals(passWord.trim())){
//            //用户名，密码组合成新的token实例
//            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName,passWord);
//
//            //验证实例
//            Authentication authentication = authenticationManager.authenticate(authRequest);
//
//            //建实例传递至安全上文
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            //将
//            HttpSession session = request.getSession();
//            session.setAttribute("SPRING_SECURITY_CONTEXT",SecurityContextHolder.getContext());
//
//            return new ResponseObj("用户【" + userName + "】登陆成功。");
//        }
//        else
//            return new ResponseObj(-1,"用户【" + userName + "】的登陆密码错误。",null);
//
//
//    }



    /**
     * 查询用户列表
     * @param page
     * @param size
     * @return 成功：用户分页列表信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public @ResponseBody
    Page<User> listUsersPage(@RequestParam("page") int page, @RequestParam("size") int size) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC,"num");

        return userRepo.findAll(pageable);
    }


    /**
     * 查询用户列表

     * @return 成功：所有用户列表信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/listAll")
    public @ResponseBody
    List<User> listAll() {
//        List<Object[]> objectList = (List<Object[]>)userRepo.getAllUserList();
//        List<User> result = null;
//        if(null != objectList) {
//            result = new ArrayList<>();
//            for(Object[] object:objectList){
//                User user = (User)object[0];
//                user.setDeptName(object[1].toString());
//                result.add(user);
//            }
//        }
        List<User> result = userRepo.findAll();
        return result;
    }


    /**
     * 登录严重
     * @param username 用户名
     * @param passwd 用户密码
     * @param request
     * @return
     */
    @RequestMapping(value = "/loginCheck")
    public @ResponseBody ResponseObj loginCheck(@RequestParam("username") String username,
                                                @RequestParam("passwd") String passwd,
                                                HttpServletRequest request) {
        if(username.trim().equals(""))
            return new ResponseObj(-1,"用户名称不能为空。",null);

        if(passwd.trim().equals(""))
            return new ResponseObj(-1,"登陆密码不能为空。",null);


        HttpSession session = request.getSession();
//        // 检查验证码
//        String verifyCode = userLoginVo.getVerifyCode().trim();
//        String vcode = (String)session.getAttribute("verifyCode");
//        if(null == verifyCode ||  verifyCode.equals("") || !vcode.equalsIgnoreCase(verifyCode)){
//            return new ResponseObj(-1,"验证码错误",null);
//        }

        logger.info(username + " use passwd:" + passwd + " logining.");
        // 生成token
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, passwd);
        try {
            // 鉴权,密码校验
            Authentication authentication = authManager.authenticate(authRequest);
            // 将鉴权结果放入SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 将SecurityContext放入session
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            session.setAttribute("user", user);
            logger.info(username + " login success,user level is ［" + user.getRoleName()+"］");
            return new ResponseObj("用户通过验证。",user);
        } catch (AuthenticationException ex) {
            // 密码错误
            String msg = ex.getMessage();
            if (msg.contains("锁定"))
                return new ResponseObj(-1, "locked", null);
            return new ResponseObj(-1, "error", null);
        }


    }
    @RequestMapping(method = RequestMethod.GET,value = "/loginFailure")
    public @ResponseBody ResponseObj loginFailure(){
        return new ResponseObj(-1,"登陆失败。",null);
    }

    /**
     * 退出系统
     */
    @RequestMapping("/logout")
    public void logout() {
        // ....
    }

    /**
     * 生成验证码
     * @param request
     * @param response
     * @throws IOException
     */
    //generate a varify code
    @RequestMapping("/verifyCode")
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] cs = new char[4];
        for (int i = 0; i < cs.length; i++) {
            if(random.nextInt(2) == 0){
                char c = (char) (random.nextInt(26) + 'A');
                cs[i] = c;
            }
            else{
                char c = (char) (random.nextInt(26) + 'a');
                cs[i] = c;
            }
        }
        String randomCode = new String(cs);

        HttpSession session = request.getSession();
        session.setAttribute("verifyCode", randomCode);

        BufferedImage image = new BufferedImage(70, 25,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 75, 25);

        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 23));

        Color fontColor = new Color(random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
        g2d.setColor(fontColor);
        g2d.drawString(randomCode, 5, 21);

        ImageIO.write(image, "png", out);
        out.flush();
    }

}
