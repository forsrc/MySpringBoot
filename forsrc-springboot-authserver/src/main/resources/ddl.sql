

-- t_user
INSERT INTO t_user (id, username, email, is_admin, version, status) VALUES (1, 'admin', 'admin@forsrc.com', 1, 0, 1);
INSERT INTO t_user (id, username, email, is_admin, version, status) VALUES (2, 'user',  'user@forsrc.com',  0, 0, 1);
INSERT INTO t_user (id, username, email, is_admin, version, status) VALUES (3, 'test',  'test@forsrc.com',  0, 0, 1);


-- t_user_privacy
INSERT INTO t_user_privacy (id, user_id, username, password, version, status) VALUES (1, 1, 'admin', '43774362f02a1f598f5f4a78bea10219', 0, 1);
INSERT INTO t_user_privacy (id, user_id, username, password, version, status) VALUES (2, 2, 'user',  '85316ec12f98340533da198c42c38a00', 0, 1);
INSERT INTO t_user_privacy (id, user_id, username, password, version, status) VALUES (3, 3, 'test',  '264856fe25533de5d0bf5597155070e0', 0, 1);


-- t_role
INSERT INTO t_role (id, name, version, status) VALUES (1, 'ROLE_ADMIN', 0, 1);
INSERT INTO t_role (id, name, version, status) VALUES (2, 'ROLE_USER',  0, 1);
INSERT INTO t_role (id, name, version, status) VALUES (3, 'ROLE_TEST',  0, 1);



-- t_user_role
INSERT INTO t_user_role (id, user_id, role_id, version, status) VALUES (1, 1, 1, 0, 1);
INSERT INTO t_user_role (id, user_id, role_id, version, status) VALUES (2, 2, 2, 0, 1);
INSERT INTO t_user_role (id, user_id, role_id, version, status) VALUES (3, 3, 3, 0, 1);

