package com.forsrc.boot.batch.user;

import com.forsrc.pojo.User;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;


public class UserFieldSetMapper implements FieldSetMapper<User>{

    @Override
    public User mapFieldSet(FieldSet fieldSet) throws BindException {
        User user = new User();
        user.setUsername(fieldSet.readString(0));
        user.setEmail(fieldSet.readString(1));
        return user;
    }

}
