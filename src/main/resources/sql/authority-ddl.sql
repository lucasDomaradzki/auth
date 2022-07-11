CREATE TABLE auth_authority (
	authorityid NUMERIC( 6,0 ),
	sgauthority VARCHAR( 20 ) NOT NULL,
	fgactive BOOLEAN NOT NULL DEFAULT '0',
	CONSTRAINT pk_authority PRIMARY KEY( authorityid ),
	CONSTRAINT uk_authority UNIQUE ( sgauthority )
);