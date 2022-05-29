package controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import interceptor.Login;
import model.*;

import java.time.temporal.Temporal;
import java.util.List;

@Before(Login.class)
public class CustomerController extends Controller {
    public void index() {
        renderFreeMarker("mainPage.ftl");
    }

    public void appointmentHaircut() {
        renderFreeMarker("appointmentHaircut.ftl");
    }

    public void doAppointmentHaircut() {
        String type = getPara("type");
        Task task = new Task();
        Customer customer = (Customer) getSessionAttr("user");

        int customerId = customer.getId();
        System.out.println("用户ID=" + customerId);
        task.setCustomerId(customerId);
        task.setType(type);

        boolean success = false;
        try {
            task.save();
            success = true;
        } catch (Exception e) {
            LogKit.error("预约失败，原因是：" + e.getMessage());
        }
        String message = success ? "预约成功" : "预约失败";
        Kv result = Kv.by("success", success).set("message", message);
        renderJson(result);
    }

    public void informationSearch() {

        Customer customer = (Customer) getSessionAttr("user");


        List<Record> list = Db.find("select task.*,staff.`name` as staffName from task LEFT JOIN staff on task.staffId=staff.id where staffId !=0 and customerId="+customer.getId());

        int count = list.size();
        System.out.println("fsdfsdfsd"+count+" "+customer.getId());
        setAttr("list", list);
        setAttr("count", count);

        List<Record> records = Db.find("select id,name from staff ");

        setAttr("staff", records);

        renderFreeMarker("informationSearch.ftl");

    }



    public static String select;
    public static String querySql;

//    select task.*,staff.`name` as staffName from task LEFT JOIN staff on task.staffId=staff.id where staffId !=0 and customerId=

    public void queryHistory() {
        Customer customer = (Customer) getSessionAttr("user");
        String type = getPara("type", "null");
        String staffId = getPara("staffId", "null");
        select = "select task.*,staff.`name` as staffName   ";
        querySql = "from task LEFT JOIN staff on task.staffId=staff.id where staffId !=0 and customerId= "+customer.getId();
        if (!type.equals("null")) {
            querySql = querySql + " and task.type like '%" + type + "%' ";
        }
        if (!staffId.equals("null")) {
                querySql = querySql + " and task.staffId=" + staffId ;
        }
        boolean success = false;
        try {
            success = true;
        } catch (Exception e) {
            LogKit.error("查询失败，原因是：" + e.getMessage());
        }
        String message = success ? "查询成功" : "查询失败";
        Kv result = Kv.by("success", success).set("message", message);
        renderJson(result);
    }

    public void queryHistoryResult() {
        Integer pageNumber = getParaToInt("page", 1);
        Page<Task> page = Task.dao.paginate(pageNumber, 10, select, querySql);
        int count = page.getList().size();
        setAttr("list", page.getList());
        setAttr("count", count);
        List<Record> records = Db.find("select id,name from staff ");
        setAttr("staff", records);
        renderFreeMarker("informationSearch.ftl");
    }
}
