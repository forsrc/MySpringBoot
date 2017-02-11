package com.forsrc.boot.batch.user;

import com.forsrc.pojo.User;
import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(final User user) throws Exception {
        final String username = user.getUsername();
        final String email = user.getEmail();

        User transformed = new User();
        transformed.setUsername(username);
        transformed.setEmail(email);
        return transformed;
    }
}