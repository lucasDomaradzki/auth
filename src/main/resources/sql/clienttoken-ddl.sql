CREATE TABLE auth_clienttoken (
	clienttokenid int AUTO_INCREMENT,
	vltoken VARCHAR(600) NOT NULL,
	clientid int NOT NULL,
	dtissued DATETIME NOT NULL,
	dtexpiration DATETIME NOT NULL,
	fgactive BOOLEAN NOT NULL DEFAULT '0',
	CONSTRAINT pk_clienttoken PRIMARY KEY( clienttokenid )
);