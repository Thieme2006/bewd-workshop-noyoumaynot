CREATE TABLE user_Table
(
    username VARCHAR(30) PRIMARY KEY,
    password VARCHAR(250),
    role     VARCHAR(100)
);

CREATE TABLE Movie
(
    movie_id VARCHAR(100) PRIMARY KEY,
    name     VARCHAR(200)
);

CREATE TABLE HIDF
(
    hid  varchar(30),
    salt varchar(300),
    FOREIGN KEY (hid) REFERENCES user_Table (username)
)




