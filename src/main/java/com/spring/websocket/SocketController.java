package com.spring.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SocketController {

    public List<String> userNameList = new ArrayList();

    @Autowired
    SimpMessagingTemplate template;


    /**
     * 登录的controller
     */
    @PostMapping("/login")
    public void login(String username, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //维护一个在线人员的列表
        userNameList.add(username);
        //把当前登录人存到session
        request.getSession().setAttribute("uname",username);
        response.sendRedirect("main.html");
    }


    /**
     * 获取当前登录的用户信息
     */
    @RequestMapping("/userInfo")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String info = request.getSession().getAttribute("uname").toString();
        return "{\"info\":\""+info+"\"}";
    }

    /**
     * server 更新在线人数列表的方法
     * 他会给所有订阅了/topic/userList的ws客户端发送消息
     */
    @RequestMapping("/userList")
    @ResponseBody
    public String userList(){
        String allUser="";
        for (String s : userNameList) {
            allUser+=s+",";
        }
        allUser =   allUser.substring(0,allUser.length()-1);
        template.convertAndSend("/topic/userList",allUser);
        return "success";
    }


    /**
     * 客户端发送消息方法
     */
    @RequestMapping("/chat")
    @ResponseBody
    public String chat(String message,String username){
        System.out.println("-------------------"+message+"---"+username);
        //convertAndSend("/user/'user3'/luban")
        template.convertAndSendToUser(username,"socket",message);//参数分别代表发送给谁，订阅地址后缀，发送信息
        return  "success";
    }

}
