INSERT INTO useraccount (id, first_name, last_name, email, password)
VALUES (2, 'Bruce', 'Wayne', 'Batman@waynecorp.com', '$2a$11$FwSqOARBYch53gfIev15ie9Sk0zC9i/gPJd/D1mLdwaeg13ui0NsG'); -- pw: test

INSERT INTO userroles (user_id, role_id)
VALUES (2, 1);

INSERT INTO userroles (user_id, role_id)
VALUES (2, 2);