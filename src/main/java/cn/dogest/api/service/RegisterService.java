package cn.dogest.api.service;

import cn.dogest.api.dao.AppInfoMapper;
import cn.dogest.api.dao.RegisterEmailMapper;
import cn.dogest.api.dao.UsersMapper;
import cn.dogest.api.model.AppInfo;
import cn.dogest.api.model.RegisterEmail;
import cn.dogest.api.model.StatusCode;
import cn.dogest.api.model.User;
import cn.dogest.api.model.mail.Mail;
import cn.dogest.api.model.mail.RegisterMail;
import cn.dogest.api.utils.SecurityUtils;
import cn.dogest.api.utils.MailUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class RegisterService {

    @Resource
    private AppInfoMapper appInfoMapper;
    @Resource
    private RegisterEmailMapper registerEmailMapper;
    @Resource
    private UsersMapper usersMapper;

    public Map<String, Object> verify(String rid, String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查参数
            if(rid == null || rid.trim().equals("")) {
                result.put("message", "参数[rid]不能为空！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            // 验证token
            AppInfo appInfo = appInfoMapper.getAppInfo("sdut_sqs");
            String origToken = "rid=" + rid + "&appkey=" + appInfo.getAppkey();
            String srvToken = SecurityUtils.encodeMD5String(origToken);
            if(!srvToken.equals(token)) {
                result.put("message", "token不合法");
                result.put("code", StatusCode.TOKEN_ERR.ordinal());
                result.put("status", StatusCode.TOKEN_ERR);
                return result;
            }

            RegisterEmail email = registerEmailMapper.getItemByRid(rid);
            if(email == null) {
                result.put("message", "链接参数非法！");
                result.put("status", StatusCode.RID_ERR);
                result.put("code", StatusCode.RID_ERR.ordinal());
                return result;
            }
            RegisterEmail emailUser = registerEmailMapper.getItemByEmail(email.getEmail());
            Date nowDate = new Date();
            Date linkDate = emailUser.getCreateAt();
            long differ = nowDate.getTime() - linkDate.getTime();
            if(!emailUser.getRid().equals(rid) || (differ > 1000 * 60 * 60 * 24) || emailUser.getFlag() == 1) {
                result.put("message", "链接已过期！");
                result.put("status", StatusCode.EXP_ERR);
                result.put("code", StatusCode.EXP_ERR.ordinal());
                return result;
            }

            User user = new User();
            user.setInfoByRegisterEmail(emailUser);
            emailUser.setFlag(1);

            usersMapper.insertUserInfo(user);
            registerEmailMapper.updateEmailFlag(emailUser);

            Map<String, String> data = new HashMap<>();
            data.put("email", user.getEmail());

            result.put("data", data);
            result.put("message", "success");
            result.put("status", StatusCode.OK);
            result.put("code", StatusCode.OK.ordinal());
        } catch (Exception e) {
            result.put("message", "服务器异常，请稍后再试！");
            result.put("status", StatusCode.OK);
            result.put("code", StatusCode.OK.ordinal());
        }
        return result;
    }

    public Map<String, Object> register(String user, String password, String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查参数
            if((user == null || user.trim().equals("")) || (password == null || password.trim().equals(""))) {
                result.put("message", "参数[user]和[password]均为必需！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            // 检查邮件地址合法性
            String emailRegex = "\\w+@\\w+(\\.\\w+)+";
            if(!user.trim().replaceAll(emailRegex, "").equals("")) {
                result.put("message", "email format error");
                result.put("status", StatusCode.EMAIL_ERR);
                result.put("code", StatusCode.EMAIL_ERR.ordinal());
                return result;
            }
            // 验证token
            AppInfo appInfo = appInfoMapper.getAppInfo("sdut_sqs");
            String origToken = "user=" + user +"&password=" + password + "&appkey=" + appInfo.getAppkey();
            String srvToken = SecurityUtils.encodeMD5String(origToken);
            if(!srvToken.equals(token)) {
                result.put("message", "token不合法");
                result.put("status", StatusCode.TOKEN_ERR);
                result.put("code", StatusCode.TOKEN_ERR.ordinal());
                return result;
            }
            // 检查用户是否已注册
            User existUser = usersMapper.selectByEmail(user);
            if(null != existUser) {
                result.put("message", "用户已存在！");
                result.put("status", StatusCode.USER_EXIST);
                result.put("code", StatusCode.USER_EXIST.ordinal());
                return result;
            }

            Date date = new Date();
            String origStr = user + password + date.toString() + new Random().nextInt();
            String rid = SecurityUtils.encodeMD5String(origStr);
            RegisterEmail registerEmail = new RegisterEmail();
            registerEmail.setEmail(user);
            registerEmail.setPassword(password);
            registerEmail.setRid(rid);
            registerEmail.setCreateAt(date);
            registerEmail.setFlag(0);

            registerEmailMapper.insertEmail(registerEmail);


            Map<String, String> params = new HashMap<>();
            String host = SecurityUtils.getProperty(appInfo.getExtra(), "app.register.host");
            params.put("link", host + rid);

            Mail mail = new RegisterMail(appInfo, user, params);

            MailUtils.send(mail);

            Map<String, Object> data = new HashMap<>();
            data.put("email", user);
            data.put("rid", rid);

            result.put("data", data);
            result.put("message", "success");
            result.put("status", StatusCode.OK);
            result.put("code", StatusCode.OK.ordinal());
        } catch (Exception e) {
            result.put("message", "system error");
            result.put("status", StatusCode.SYS_ERR);
            result.put("code", StatusCode.SYS_ERR.ordinal());
        }
        return result;
    }

}
