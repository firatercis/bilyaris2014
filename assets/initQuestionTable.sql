CREATE TABLE QUESTIONS(qID integer primary key,
text varchar(255),
difficulty integer,
category varchar(30),
choice1 varchar(100),
choice2 varchar(100),
choice3 varchar(100),
choice4 varchar(100),
correctanswer integer,
author varchar(50),date datetime);

