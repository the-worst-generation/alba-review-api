CREATE TABLE user (
	id BIGINT(11) PRIMARY KEY,
	email VARCHAR(50) NOT NULL UNIQUE,
	profile_picture	VARCHAR(100) NOT NULL,
	social_type VARCHAR(15) NOT NULL,
	role VARCHAR(10) NOT NULL,
	nickname VARCHAR(20) UNIQUE,
	sex	CHAR(2),
	birth_date	TIMESTAMP,
	created_at	TIMESTAMP,
	updated_at	TIMESTAMP
);

CREATE TABLE company (
	id BIGINT(11) PRIMARY KEY,
	name VARCHAR(60) NOT NULL UNIQUE,
	position POINT NOT NULL UNIQUE,
	rating DECIMAL(1,1) NOT NULL DEFAULT(0),
	favorite INT(8)	NOT NULL DEFAULT(0),
	dislike	INT(8)	NOT NULL DEFAULT(0),
	created_at TIMESTAMP,
	updated_at TIMESTAMP
);

CREATE TABLE review (
    id BIGINT(11) PRIMARY KEY,
	user_id	BIGINT(11) NOT NULL,
	company_id BIGINT(11) NOT NULL,
	content	VARCHAR(1000) NOT NULL,
	favorite INT(8),
	created_at TIMESTAMP,
	updated_at TIMESTAMP,
	FOREIGN KEY(user_id) REFERENCES user(id) ON UPDATE CASCADE,
    FOREIGN KEY(company_id) REFERENCES company(id) ON UPDATE CASCADE
);
CREATE TABLE tag (
	id BIGINT(11) PRIMARY KEY,
	content VARCHAR(50) NOT	NULL UNIQUE
);
CREATE TABLE job_experience (
	user_id	BIGINT(11),
	company_id BIGINT(11),
	FOREIGN KEY(user_id) REFERENCES user(id) ON UPDATE CASCADE,
	FOREIGN KEY(company_id) REFERENCES company(id) ON UPDATE CASCADE
);

CREATE TABLE favorite_company (
	user_id BIGINT(11),
	company_id BIGINT(11),
	FOREIGN KEY(user_id) REFERENCES user(id) ON UPDATE CASCADE,
	FOREIGN KEY(company_id) REFERENCES company(id) ON UPDATE CASCADE
);

CREATE TABLE company_tag (
	company_id BIGINT(11),
	tag_id BIGINT(11),
	FOREIGN KEY(company_id) REFERENCES company(id) ON UPDATE CASCADE,
	FOREIGN KEY(tag_id) REFERENCES tag(id) ON UPDATE CASCADE
);

