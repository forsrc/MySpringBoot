
SELECT 1;

-- t_user
-- INSERT INTO t_user (id, username, email, is_admin, version, status) VALUES (1, 'admin', 'admin@forsrc.com', 1, 0, 1);
-- INSERT INTO t_user (id, username, email, is_admin, version, status) VALUES (2, 'user',  'user@forsrc.com',  0, 0, 1);
-- INSERT INTO t_user (id, username, email, is_admin, version, status) VALUES (3, 'test',  'test@forsrc.com',  0, 0, 1); 

INSERT INTO t_user (id, username, email, is_admin, version, status) SELECT * FROM (SELECT 1 id, 'admin' username, 'admin@forsrc.com' email, 1 is_admin, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_user WHERE id = 1);
INSERT INTO t_user (id, username, email, is_admin, version, status) SELECT * FROM (SELECT 2 id, 'user'  username, 'user@forsrc.com'  email, 0 is_admin, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_user WHERE id = 2); 
INSERT INTO t_user (id, username, email, is_admin, version, status) SELECT * FROM (SELECT 3 id, 'test'  username, 'test@forsrc.com'  email, 0 is_admin, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_user WHERE id = 3);





-- t_user_privacy
-- INSERT INTO t_user_privacy (id, user_id, username, password, version, status) VALUES (1, 1, 'admin', '43774362f02a1f598f5f4a78bea10219', 0, 1);
-- INSERT INTO t_user_privacy (id, user_id, username, password, version, status) VALUES (2, 2, 'user',  '85316ec12f98340533da198c42c38a00', 0, 1);
-- INSERT INTO t_user_privacy (id, user_id, username, password, version, status) VALUES (3, 3, 'test',  '264856fe25533de5d0bf5597155070e0', 0, 1);

INSERT INTO t_user_privacy (id, user_id, username, password, version, status) SELECT * FROM (SELECT 1 id, 1 user_id, 'admin' username, '43774362f02a1f598f5f4a78bea10219' password, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_user_privacy WHERE id = 1);
INSERT INTO t_user_privacy (id, user_id, username, password, version, status) SELECT * FROM (SELECT 2 id, 2 user_id, 'user'  username, '85316ec12f98340533da198c42c38a00' password, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_user_privacy WHERE id = 2);
INSERT INTO t_user_privacy (id, user_id, username, password, version, status) SELECT * FROM (SELECT 3 id, 3 user_id, 'test'  username, '264856fe25533de5d0bf5597155070e0' password, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_user_privacy WHERE id = 3);



-- t_role
-- INSERT INTO t_role (id, name, version, status) VALUES (1, 'ROLE_ADMIN', 0, 1);
-- INSERT INTO t_role (id, name, version, status) VALUES (2, 'ROLE_USER',  0, 1);
-- INSERT INTO t_role (id, name, version, status) VALUES (3, 'ROLE_TEST',  0, 1);

INSERT INTO t_role (id, name, version, status) SELECT * FROM (SELECT 1 id, 'ROLE_ADMIN' name, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_role WHERE id = 1);
INSERT INTO t_role (id, name, version, status) SELECT * FROM (SELECT 2 id, 'ROLE_USER'  name, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_role WHERE id = 2);
INSERT INTO t_role (id, name, version, status) SELECT * FROM (SELECT 3 id, 'ROLE_TEST'  name, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_role WHERE id = 3);


-- t_user_role
-- INSERT INTO t_user_role (id, user_id, role_id, version, status) VALUES (1, 1, 1, 0, 1);
-- INSERT INTO t_user_role (id, user_id, role_id, version, status) VALUES (2, 2, 2, 0, 1);
-- INSERT INTO t_user_role (id, user_id, role_id, version, status) VALUES (3, 3, 3, 0, 1); 

INSERT INTO t_user_role (id, user_id, role_id, version, status) SELECT * FROM (SELECT 1 id, 1 user_id, 1 role_jd, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_user_role WHERE id = 1);
INSERT INTO t_user_role (id, user_id, role_id, version, status) SELECT * FROM (SELECT 2 id, 2 user_id, 2 role_jd, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_user_role WHERE id = 2);
INSERT INTO t_user_role (id, user_id, role_id, version, status) SELECT * FROM (SELECT 3 id, 3 user_id, 3 role_jd, 0 version, 1 status) AS T WHERE NOT EXISTS (SELECT id FROM t_user_role WHERE id = 3);



----
INSERT INTO CLIENTDETAILS 
    (APPID, RESOURCEIDS, APPSECRET , SCOPE, GRANTTYPES, REDIRECTURL, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY, ADDITIONALINFORMATION, AUTOAPPROVESCOPES)
    SELECT * FROM (SELECT 'forsrc' APPID, 'forsrc' RESOURCEIDS, 'forsrc' APPSECRET, 'read, write' SCOPE, 'password, authorization_code, refresh_token, implicit' GRANTTYPES,
        '/' REDIRECTURL, 'ROLE_USER, ROLE_ADMIN' AUTHORITIES, 36000 ACCESS_TOKEN_VALIDITY, 36000 REFRESH_TOKEN_VALIDITY, '' ADDITIONALINFORMATION, '' AUTOAPPROVESCOPES) AS T
    WHERE NOT EXISTS (SELECT APPID FROM CLIENTDETAILS WHERE APPID = 'forsrc')
;

INSERT INTO oauth_client_details
    (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
    SELECT * FROM (SELECT 'forsrc' client_id, 'forsrc' resource_ids, 'forsrc' client_secret, 'read, write' SCOPE, 'password, authorization_code, refresh_token, implicit' authorized_grant_types, null web_server_redirect_uri,
        'ROLE_USER, ROLE_ADMIN' authorities, 36000 access_token_validity, 36000 refresh_token_validity, null additional_information, true autoapprove) AS T
    WHERE NOT EXISTS (SELECT client_id FROM oauth_client_details WHERE client_id = 'forsrc')
;






