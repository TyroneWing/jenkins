package com.tinckay.security;

import com.google.gson.Gson;
import com.tinckay.common.ResponseObj;
import com.tinckay.domain.base.Opt;
import com.tinckay.domain.jpa.OptRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by root on 1/19/17.
 */
@Component
public class ResponseExtFilter implements Filter {
    private Logger logger =  Logger.getLogger(this.getClass());

    @Autowired
    OptRepo optRepo;

    OptLogRunner optLogRunner;

    private Queue<Opt> optQueue = new LinkedBlockingDeque<>(1000);

    public void destroy() {
        logger.info("destory");
    }



    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
//        CoverResponseWrapper crw = new CoverResponseWrapper((HttpServletResponse)response);
//        fc.doFilter(request, crw);
        //处理替换
        //String content = cr.getContent();
        //content = content.replace("test", "actual");
        //response.getWriter().print(content);
//        String content = crw.getContent();
//        logger.info(content);


        CoverResponseWrapper responseWrapper = new CoverResponseWrapper((HttpServletResponse) response);
        try {
            fc.doFilter(request, responseWrapper);
        } catch (ServletException e) {
            logger.info(e.getMessage());
            //e.printStackTrace();
        }

        byte[] output = responseWrapper.getDataStream();
        String responseContent = new String(output);


        if (((HttpServletResponse) response).getStatus() == 200 && (responseContent.startsWith("[") || responseContent.startsWith("{"))){
            HttpServletRequest req = (HttpServletRequest)request;

            String procName = req.getServletPath();
            String action = "";
            byte optFlag = 0;
            if (procName.contains("login")) {
                action = "登录";
                optFlag = 1;
            }
            else if (procName.contains("add")) {
                action = "新增";
                optFlag = 2;
            }
            else if (procName.contains("modify")) {
                action = "修改";
                optFlag = 3;
            }
            else if (procName.contains("delete")) {
                action = "删除";
                optFlag = 4;
            }
            else if (procName.contains("logout")) {
                action = "注销";
                optFlag = 5;
            }
            else if (procName.contains("start")) {
                action = "启动";
                optFlag = 6;
            }
            else if (procName.contains("cancel")) {
                action = "取消";
                optFlag = 7;
            }
            else if (procName.contains("pause")) {
                action = "暂停";
                optFlag = 8;
            }
            else if (procName.contains("finish")) {
                action = "完结";
                optFlag = 9;
            }

            if(!action.equals("")) {
                logger.info(action + ":" + procName);
                String userName = req.getRemoteUser();
                String host = req.getRemoteHost();
                if(null == userName || "".equals(userName)){
                    userName = "Admin";
                }

                if(null == host || "".equals(host)){
                    host = "192.168.0.21";
                }

                logger.info(req.getMethod());
                Map<String, String[]> paramMap = req.getParameterMap();
                if (paramMap != null) {
                    logger.info("Parameters : ");
                    for (String key : paramMap.keySet()) {
                        logger.info(key + " : " + paramMap.get(key));
                    }

                }
                Gson gson = new Gson();
                ResponseObj obj = gson.fromJson(responseContent,ResponseObj.class);
                //logger.info();
                if(null != obj && obj.getErr() == 0) {
                    Opt opt = new Opt();
                    opt.setIp(req.getRemoteHost());
                    opt.setCreateTime(new Date());
                    opt.setUserName(userName);
                    //1,登陆日志
                    if (action.equals("登录")) {


                        String[] names = paramMap.get("username");
                        String name = "";
                        for (String s : names)
                            name += s;
                        if (obj.getErr() == 0) {
                            opt.setFlag((byte) optFlag);
                            opt.setResume("使用帐户名【" + name + "】成功登陆系统。");
                        } else {
                            opt.setFlag((byte) -optFlag);
                            opt.setResume("使用帐户名【" + name + "】登陆系统失败。");
                            opt.setUserName("未知");
                        }
                    }
                    //新增，删除信息
                    if (action.equals("删除") || action.equals("新增") || action.equals("修改")) {
                        //opt.setFlag((byte) ((obj.getErr() == 0)? optFlag : -optFlag));
                        opt.setFlag(optFlag);
                        opt.setUserName(userName);
                        opt.setResume(obj.getMsg());
                        opt.setIp(host);
                        Object entity = obj.getObj();
                        if (null != entity) {

                            if (entity instanceof Long) {
                                Long id = (Long) entity;
                                if (procName.contains("/Project/")) {
                                    opt.setPjtId(id);
                                }
                                if (procName.contains("/Task/")) {
                                    if (procName.contains("modifyPlanDate"))
                                        opt.setEventId(id);
                                    else
                                        opt.setTaskId(id);
                                }
                            }

                        }

                    }
                    logger.info(opt.getResume());
                    optQueue.offer(opt);
                    logger.info("日志队列数量：" + optQueue.size());
                    logger.info(responseContent);
                    checkThread();

                }

            }
        }
        response.getOutputStream().write(output);

//        RestResponse fullResponse = new RestResponse(205, "OK-MESSAGE",responseContent);
//        byte[] responseToSend = restResponseBytes(fullResponse);
//        response.getOutputStream().write(responseToSend);
    }

//    private byte[] restResponseBytes(RestResponse response) throws IOException {
//        　　　　String serialized = new ObjectMapper().writeValueAsString(response);
//        　　　　return serialized.getBytes("UTF-8");
//
//
//    }

    public void init(FilterConfig arg0) throws ServletException {

    }

    //检查线程是否启动
    private void checkThread() {
        if(optLogRunner != null ) {
            return;
        }

        optLogRunner = new OptLogRunner();
        new Thread(optLogRunner).start();
        logger.info("new OptLogRunner");
    }

    private class OptLogRunner implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    //synchronized (this) {
                        doSaveOptLogInfo();
                    //}
                }
            } catch (Exception e) {
                //
            }
        }
    }

    @Transactional
    private void doSaveOptLogInfo(){
        if(optQueue.size() > 0){
            Opt opt = optQueue.poll();
            if(null != opt){
                optRepo.save(opt);
                //return true;
            }
        }
        //return false;
    }
}
