CREATE TABLE auth_clientauthority (
	clientauthorityid int AUTO_INCREMENT,
	authorityid int NOT NULL,
	clientid int NOT NULL,
	fgactive BOOLEAN NOT NULL DEFAULT '0',
	CONSTRAINT pk_clientauthority PRIMARY KEY( clientauthorityid ),
	CONSTRAINT uk_clientauthority UNIQUE( authorityid, clientid )
);