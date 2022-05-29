package controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.TxByActionKeyRegex;
import interceptor.Login;
import model.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Before(Login.class)
public class ManagerController extends Controller {
    public void index() {
        renderFreeMarker("mainPage.ftl");
    }

    public void listCustomer() {
        SqlPara adminFindAllCustomer = Db.getSqlPara("adminFindAllCustomer");
        Integer pageNumber = getParaToInt("page", 1);
        Page<Manager> page = Manager.dao.paginate(pageNumber, 10, adminFindAllCustomer);
        setAttr("page", page);

        renderFreeMarker("listCustomer.ftl");
    }

    public static String select;
    public static String querySql;

    public void queryCustomer() {
        String name = getPara("name", "null");
        String tel = getPara("tel", "null");
        select = "select * ";
        querySql = "from customer ";

        if (!name.equals("null")) {
            querySql = querySql + " where name = '" + name + "'";
        }
        if (!tel.equals("null")) {
            if (!name.equals("null")) {
                querySql = querySql + " and tel = \"" + tel + "\"";
            } else {
                querySql = querySql + "where tel = \"" + tel + "\"";
            }
        }

        System.out.println("sql查询语句为=" + querySql);

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

    public void queryCustomerResult() {
        Integer pageNumber = getParaToInt("page", 1);
        Page<Customer> page = Customer.dao.paginate(pageNumber, 10, select, querySql);
        setAttr("page", page);
        renderFreeMarker("listCustomer.ftl");
    }

    public void modifyCustomer() {
        Integer customerId = getParaToInt(0, -1);
        List<Customer> customer = Customer.dao.find("select * from customer where id =? ", customerId);
        setAttr("customer", customer.get(0));
        renderFreeMarker("modifyCustomer.ftl");
    }

    public void doModifyCustomer() {
        Integer id = getParaToInt("id");
        System.out.println("id=" + id);
        String name = getPara("name");
        String password = getPara("password");
        String tel = getPara("tel");
        String email = getPara("email");
        String sex = getPara("sex");
        String address = getPara("address");

        boolean success = false;
        try {
            int update = Db.update("update customer set name=?,password=?,tel=?,email=?,sex=?,address=? where id=?", name, password, tel, email, sex, address, id);
            success = true;
            System.out.println(update);

        } catch (Exception e) {
            LogKit.error("修改失败，原因是：" + e.getMessage());
        }
        String message = success ? "修改成功" : "修改失败";
        Kv result = Kv.by("success", success).set("message", message);
        System.out.print(success);
        renderJson(result);
    }

    public void deleteCustomer() {
        Integer paraToInt = getParaToInt(0, -1);
        try {
            Db.delete("DELETE FROM customer where id=?", paraToInt);
        } catch (Exception e) {
            LogKit.error("删除失败，原因是：" + e.getMessage());
        }
        redirect("/manager/listCustomer");
    }

    public void addCustomer() {
        renderFreeMarker("addCustomer.ftl");
    }

    public void doAddCustomer() {
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
            LogKit.error("添加失败，原因是：" + e.getMessage());
        }
        String message = success ? "添加成功" : "添加失败";
        Kv result = Kv.by("success", success).set("message", message);
        renderJson(result);

    }


    public void listStaff() {
        SqlPara adminFindAllStaff = Db.getSqlPara("adminFindAllStaff");
        Integer pageNumber = getParaToInt("page", 1);
        Page<Staff> staff = Staff.dao.paginate(pageNumber, 10, adminFindAllStaff);
        setAttr("page", staff);

        renderFreeMarker("listStaff.ftl");
    }

    public static String selectStaff;
    public static String querySqlStaff;

    public void queryStaff() {
        String name = getPara("name", "null");
        String tel = getPara("tel", "null");
        selectStaff = "select * ";
        querySqlStaff = "from staff ";

        if (!name.equals("null")) {
            querySqlStaff = querySqlStaff + " where name = '" + name + "'";
        }
        if (!tel.equals("null")) {
            if (!name.equals("null")) {
                querySqlStaff = querySqlStaff + " and tel = \"" + tel + "\"";
            } else {
                querySqlStaff = querySqlStaff + "where tel = \"" + tel + "\"";
            }
        }

        System.out.println("sql查询语句为=" + querySqlStaff);

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

    public void queryStaffResult() {
        Integer pageNumber = getParaToInt("page", 1);
        Page<Staff> page = Staff.dao.paginate(pageNumber, 10, selectStaff, querySqlStaff);
        setAttr("page", page);
        renderFreeMarker("listStaff.ftl");
    }


    public void modifyStaff() {
        Integer staffId = getParaToInt(0, -1);
        List<Record> Staff = Db.find("select * from staff where id =? ", staffId);
        setAttr("staff", Staff.get(0));

        List<Type> types = Type.dao.find("select * from type");
        setAttr("types", types);

        renderFreeMarker("modifyStaff.ftl");
    }

    public void doModifyStaff() {
        Integer id = getParaToInt("id");
        System.out.println("ID========" + id);
        String name = getPara("name");
        String skill = getPara("skill");
        String tel = getPara("tel");
        String email = getPara("email");
        String sex = getPara("sex");
        String address = getPara("address");

        boolean success = false;
        try {
            int update = Db.update("update staff set name=?,skill=?,tel=?,email=?,sex=?,address=? where id=?", name, skill, tel, email, sex, address, id);
            success = true;
            System.out.println(update);

        } catch (Exception e) {
            LogKit.error("修改失败，原因是：" + e.getMessage());
        }
        String message = success ? "修改成功" : "修改失败";
        Kv result = Kv.by("success", success).set("message", message);
        System.out.print(success);
        renderJson(result);
    }

    public void deleteStaff() {
        Integer paraToInt = getParaToInt(0, -1);
        try {
            Db.delete("DELETE FROM staff where id=?", paraToInt);
        } catch (Exception e) {
            LogKit.error("删除失败，原因是：" + e.getMessage());
        }
        redirect("/manager/listStaff");
    }

    public void addStaff() {
        renderFreeMarker("addStaff.ftl");
    }

    public void doAddStaff() {
        String name = getPara("name");
        String sex = getPara("sex");
        String skill = getPara("skill");
        String email = getPara("email");
        String tel = getPara("tel");
        String address = getPara("address");


        Staff staff = new Staff();
        staff.setAddress(address);
        staff.setEmail(email);
        staff.setSex(sex);
        staff.setName(name);
        staff.setSkill(skill);
        staff.setTel(tel);

        boolean success = false;
        try {
            staff.save();
            success = true;
        } catch (Exception e) {
            LogKit.error("添加失败，原因是：" + e.getMessage());
        }
        String message = success ? "添加成功" : "添加失败";
        Kv result = Kv.by("success", success).set("message", message);
        renderJson(result);

    }

    public void listType() {
        SqlPara adminFindAllType = Db.getSqlPara("adminFindAllType");
        Integer pageNumber = getParaToInt("page", 1);
        Page<Type> Types = Type.dao.paginate(pageNumber, 10, adminFindAllType);
        setAttr("page", Types);
        renderFreeMarker("listType.ftl");
    }

    public void modifyType() {
        Integer typeId = getParaToInt(0, -1);
        List<Record> type = Db.find("select * from type where id =? ", typeId);
        setAttr("type", type.get(0));


        renderFreeMarker("modifyType.ftl");
    }

    public void doModifyType() {
        Integer id = getParaToInt("id");
        System.out.println("ID========" + id);
        String type = getPara("type");

        boolean success = false;
        try {
            int update = Db.update("update type set type=? where id=?", type, id);
            success = true;
            System.out.println(update);

        } catch (Exception e) {
            LogKit.error("修改失败，原因是：" + e.getMessage());
        }
        String message = success ? "修改成功" : "修改失败";
        Kv result = Kv.by("success", success).set("message", message);
        System.out.print(success);
        renderJson(result);
    }

    public void deleteType() {
        Integer paraToInt = getParaToInt(0, -1);
        try {
            Db.delete("DELETE FROM type where id=?", paraToInt);
        } catch (Exception e) {
            LogKit.error("删除失败，原因是：" + e.getMessage());
        }
        redirect("/manager/listType");
    }

    public void addType() {
        renderFreeMarker("addType.ftl");
    }

    public void doAddType() {
        String t = getPara("type");
        Type type = new Type();
        type.setType(t);

        boolean success = false;
        try {
            type.save();
            success = true;
        } catch (Exception e) {
            LogKit.error("添加失败，原因是：" + e.getMessage());
        }
        String message = success ? "添加成功" : "添加失败";
        Kv result = Kv.by("success", success).set("message", message);
        renderJson(result);

    }

    public void listRecharge() {
        SqlPara adminFindAllType = Db.getSqlPara("adminFindAllRecharge");
        Integer pageNumber = getParaToInt("page", 1);
        Page<Recharge> rechargePage = Recharge.dao.paginate(pageNumber, 10, adminFindAllType);

        List<Record> records = Db.find("select customer.id,customer.name,tel , ifnull(amountMoney,0)as money from customer left join recharge on customer.id=recharge.customerId");

        setAttr("page", records);

        renderFreeMarker("listRecharge.ftl");
    }

    public void recharge() {
        renderFreeMarker("recharge.ftl");
    }

    public void modifyRecharge() {
        Integer customerId = getParaToInt(0, -1);
        setAttr("customerId", customerId);

        renderFreeMarker("modifyRecharge.ftl");
    }

    public void doModifyRecharge() {
        Integer ConsumerId = getParaToInt("id");
        Integer ConsumptionAmount = getParaToInt("ConsumptionAmount");
        List<Record> records = Db.find("select amountMoney from recharge where customerId=" + ConsumerId);
        int realMoney = records.get(0).get("amountMoney");
        int res = realMoney - ConsumptionAmount;

        System.out.println("res=" + res);
        boolean success = false;
        if (res > 0) {
            try {
                Db.update("update recharge set amountMoney=" + res + " where customerId=" + ConsumerId);
                success = true;
            } catch (Exception e) {
                LogKit.error("添加失败，原因是：" + e.getMessage());
            }
        }
        String message = success ? "扣减成功" : "扣减失败";
        Kv result = Kv.by("success", success).set("message", message);
        renderJson(result);
    }

    public void doAddRecharge() {
        Integer id = getParaToInt("id");
        Integer money = getParaToInt("money");
        boolean success = false;
        List<Record> records = Db.find("select * from recharge where customerId=" + id);
        if (records.size() == 0) {
            Recharge recharge = new Recharge();
            recharge.setAmountMoney(money);
            recharge.setCustomerId(id);
            try {
                recharge.save();
                success = true;
            } catch (Exception e) {
                LogKit.error("添加失败，原因是：" + e.getMessage());
            }
        }else{
            int curMoney = records.get(0).get("amountMoney");
            int finalMoney = curMoney + money;
            try {
                Db.update("update recharge set amountMoney =" + finalMoney + " where customerId=" + id);
                success = true;
            } catch (Exception e) {
                LogKit.error("添加失败，原因是：" + e.getMessage());
            }
        }
        String message = success ? "充值成功" : "充值失败";
        Kv result = Kv.by("success", success).set("message", message);
        renderJson(result);
    }

    public void listHaircutTask() {
        SqlPara adminFindAllType = Db.getSqlPara("adminFindAllTask");
        Integer pageNumber = getParaToInt("page", 1);

        Page<Task> taskPage = Task.dao.paginate(pageNumber, 10, adminFindAllType);
        setAttr("page", taskPage);
        renderFreeMarker("listTask.ftl");
    }


    //    分配员工
    public void assignEmployees() {
        Integer taskId = getParaToInt(0, -1);

        SqlPara viewIdleEmployees = Db.getSqlPara("viewIdleEmployees");
        List<Record> records = Db.find(viewIdleEmployees.getSql());
        setAttr("page", records);
        setAttr("taskId", taskId);
        renderFreeMarker("assignEmployees.ftl");

    }

    public void doAssignEmployees() {
        Integer staffId = getParaToInt("staffId", -1);
        Integer taskId = getParaToInt("taskId");

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String time = format.format(new Date());


        boolean success = false;
        try {

            Db.update("UPDATE task SET staffId =" + staffId + " ,`status`=1,date ='"+time +"' where id=" + taskId);
            success = true;
        } catch (Exception e) {
            LogKit.error("失败，原因是：" + e.getMessage());
        }
        String message = success ? "分配成功" : "分配失败";
        Kv result = Kv.by("success", success).set("message", message);
        renderJson(result);



    }

    public void finish() {
        Integer taskId = getParaToInt(0, -1);
        Db.update("update task set status = 0 where id="+taskId );

        SqlPara adminFindAllType = Db.getSqlPara("adminFindAllTask");
        Integer pageNumber = getParaToInt("page", 1);

        Page<Task> taskPage = Task.dao.paginate(pageNumber, 10, adminFindAllType);
        setAttr("page", taskPage);
        renderFreeMarker("listTask.ftl");

    }

}

