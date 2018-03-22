package cn.dogest.api.dao;

import cn.dogest.api.model.RegisterEmail;

public interface RegisterEmailMapper {

    RegisterEmail getItemByRid(String rid);
    RegisterEmail getItemByEmail(String email);
    int insertEmail(RegisterEmail email);
    int updateEmailFlag(RegisterEmail email);
}
