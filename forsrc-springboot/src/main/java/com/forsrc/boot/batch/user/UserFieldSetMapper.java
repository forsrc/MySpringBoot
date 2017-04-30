package com.forsrc.boot.batch.user;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.forsrc.pojo.User;

public class UserFieldSetMapper implements FieldSetMapper<User> {

    @Override
    public User mapFieldSet(FieldSet fieldSet) throws BindException {
        User user = new User();
        user.setUsername(fieldSet.readString(0));
        user.setEmail(fieldSet.readString(1));
        return user;
    }

}
