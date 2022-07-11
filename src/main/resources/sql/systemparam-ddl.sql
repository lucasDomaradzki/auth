CREATE TABLE auth_systemparam (
	systemparamid VARCHAR(40) NOT NULL,
	name VARCHAR(60) NOT NULL,
	param VARCHAR(40) NOT NULL,
	CONSTRAINT pk_systemparam PRIMARY KEY( systemparamid ),
	CONSTRAINT uk_systemparam UNIQUE ( systemparamid, param )
);