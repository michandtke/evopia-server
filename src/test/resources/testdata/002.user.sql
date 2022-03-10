INSERT INTO useraccount (id, first_name, last_name, email, password)
VALUES (2, 'Bruce', 'Wayne', 'Batman@waynecorp.com', '$2a$11$FwSqOARBYch53gfIev15ie9Sk0zC9i/gPJd/D1mLdwaeg13ui0NsG'); -- pw: test, with profile

INSERT INTO userroles (user_id, role_id)
VALUES (2, 1);

INSERT INTO userroles (user_id, role_id)
VALUES (2, 2);

INSERT INTO useraccount (id, first_name, last_name, email, password)
VALUES (3, 'Dick', 'Grayson', 'Robin@waynecorp.com', '$2a$11$FwSqOARBYch53gfIev15ie9Sk0zC9i/gPJd/D1mLdwaeg13ui0NsG'); -- pw: test, without profile

INSERT INTO userroles (user_id, role_id)
VALUES (3, 2); -- only user, not admin