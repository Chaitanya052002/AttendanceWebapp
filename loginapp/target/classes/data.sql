INSERT INTO dbo.user_master (phoneno, email, loggedinstatus, password, role, username)
SELECT * FROM (
    SELECT '234234' AS phoneno, 'admin@gmail.com' AS email, 0 AS loggedinstatus, '$2a$10$cUlPNTd24y3CxNrVUYFVOuDnVj9jFrqhZeu3FbDnUWn1Jka8ZTzkm' AS password, 'ROLE_ADMIN' AS role, 'admin' AS username
) AS tmp
WHERE NOT EXISTS (
    SELECT phoneno FROM dbo.user_master WHERE phoneno = '234234'
);

INSERT INTO dbo.user_master (phoneno, email, loggedinstatus, password, role, username)
SELECT * FROM (
    SELECT '77897897' AS phoneno, 'chaita05@gmail.com' AS email, 0 AS loggedinstatus, '$2a$10$cUlPNTd24y3CxNrVUYFVOuDnVj9jFrqhZeu3FbDnUWn1Jka8ZTzkm' AS password, 'ROLE_USER' AS role, 'Cugug' AS username
) AS tmp
WHERE NOT EXISTS (
    SELECT phoneno FROM dbo.user_master WHERE phoneno = '77897897'
);
