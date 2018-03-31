CREATE DATABASE IF NOT EXISTS url_shortener;
DROP TABLE IF EXISTS url_analytic;
DROP TABLE IF EXISTS store_url;


CREATE TABLE store_url(
id bigint(20) NOT NULL AUTO_INCREMENT,
original_url varchar(1000) NOT NULL,
created_on TIMESTAMP,
PRIMARY KEY (id)
);

CREATE TABLE url_analytic(
id bigint(20) NOT NULL AUTO_INCREMENT,
count int (20) NOT NULL,
PRIMARY KEY (id),
storeurl_id bigint (20) NOT NULL,
FOREIGN KEY(storeurl_id) REFERENCES store_url(id)
);

