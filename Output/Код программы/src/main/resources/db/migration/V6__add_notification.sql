Insert Into user_notification (id, message, url, url_text, user_id)
VALUES
    (1,'Product: MEAT was added to you bucket','','',1),
    (2,'Order with number 1 was created','','',1),
    (3,'Product: CHEESE was added to you bucket','','',1),
    (4,'Order with number 78 was created','','',1),
    (5,'Order with number 56 was created','','',1)

;
ALTER SEQUENCE user_notification_seq RESTART with 8;
