CREATE DATABASE IF NOT EXISTS url_shortener;
DROP TABLE IF EXISTS store_url;

CREATE TABLE store_url(
id int(20) NOT NULL AUTO_INCREMENT,
original_url varchar(1000) NOT NULL,
created_on TIMESTAMP,
PRIMARY KEY (id)
);