package cn.dogest.api.dao;

import cn.dogest.api.model.User;

public interface UsersMapper {
    User selectByEmail(String email);
    int insertUserInfo(User user);
}
