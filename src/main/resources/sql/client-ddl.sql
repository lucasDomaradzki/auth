CREATE TABLE auth_client (
	clientid int AUTO_INCREMENT,
	name VARCHAR(60) NOT NULL,
	email VARCHAR(60) NOT NULL,
	password VARCHAR(600) NOT NULL,
	passwordexpiration TIMESTAMP NOT NULL,
	dtissued DATETIME NOT NULL,
	dtupdate DATETIME,
	dtbirthday DATETIME,
	role VARCHAR(15) NOT NULL,
	fgactive BOOLEAN NOT NULL DEFAULT '0',
	udpdatedby NUMERIC (6,0),
	nickname VARCHAR(20),
	cpf VARCHAR(14),
	origin VARCHAR(20) NOT NULL,
	CONSTRAINT pk_client PRIMARY KEY( clientid ),
	CONSTRAINT uk_client UNIQUE ( name, email )
);