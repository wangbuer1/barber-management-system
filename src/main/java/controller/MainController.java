package controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;
import interceptor.LoginValidator;
import model.*;

import java.util.List;


public class MainController extends Controller {


    public void index() {
        renderFreeMarker("login.ftl");
    }
    public void login() {
        renderFreeMarker("login.ftl");
    }
    public void register() {
        renderFreeMarker("registerCustomer.ftl");
    }



    public void doRegisterCustomer() {
        String name = getPara("name");
        String sex = getPara("sex");
        String password = getPara("password");
        String email = getPara("email");
        String tel = getPara("tel");
        String address = getPara("address");


        Customer customer = new Customer();
        customer.setAddress(address);
        customer.setEmail(email);
        customer.setSex(sex);
        customer.setName(name);
        customer.setPassword(password);
        customer.setTel(tel);


        boolean success = false;
        try {
            customer.save();
            success = true;
        } catch (Exception e) {
            LogKit.error("注册失败，原因是：" + e.getMessage());
        }
        String message = success ? "注册成功" : "注册失败";
        Kv result = Kv.by("success", success).set("message", message);
        renderJson(result);
    }




    @Before(LoginValidator.class)
    public void loginCheck() {
        String username = getPara("username");
        String password = getPara("password");
        String usertype = getPara("usertype");
        boolean success = false;
        int flag = 0;
        if (usertype.equals("1")){
            String sql_customer = Db.getSql("check_login_customer");
            List<Customer> customers = Customer.dao.find(sql_customer, username, password);
            if (customers.size() != 0) {
                setSessionAttr("user", customers.get(0));
                success = true;
                flag = 1;
            } else {
                    setAttr("errmsg", "用户名或密码错误");
                }
        }else if(usertype.equals("2")){
            String sql_manager = Db.getSql("check_login_manager");
            List<Manager> Managers = Manager.dao.find(sql_manager, username, password);
            if (Managers.size() != 0) {
                setSessionAttr("user", Managers.get(0));
                success = true;
                flag = 2;
            } else {
                setAttr("errmsg", "用户名或密码错误");
            }

        }
        String message = success ? "登录成功" : "登录失败,密码或者用户名错误";

        Kv result = Kv.by("success", success).set("message", message).set("flag", flag);
        renderJson(result);
    }

    public void logout() {
        removeSessionAttr("users");
        redirect("/login");
    }


}
