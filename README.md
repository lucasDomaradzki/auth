# Authentication application - Auth

This is a service application responsible for system authentication, user roles and authorities management, along with token/credentials generation which can be used along side another application providing information about users attempting to login to Auth or other systems using Auth as a service.

For the key token, the JWT (JSON Web Token) is used along with SHA-512 encryption to store user's password as well user's information in the token key.

## System requirements

* Java 8+
* Maven 3.5+
* MySQL Server 8+

## First install

* Download and project run **mvn clean install** to download the system's dependencies and create the target .jar file
* Having Mysql Server properly installed, first create the database structure to connect to the system with the commands bellow:
	 1. create database dbaludplandesenv;
	 2. create user 'aluddesenv'@'localhost' identified by 'aluddesenv';
	 3. grant all privileges to 'aluddesenv'@'localhost' identified by 'aluddesenv';

* After that run the **DDL** and **DML** queries files to have the proper tables created to connect to the system. They can be found on the project folder at: ***~/auth/src/main/resources/sql***
* Once the database is created and the system dependencies are installed go ahead and run the system with the generated **.jar** file on **~/auth/target/** by running the command: **java -jar jarfilename.jar**

## API's Documentation

The API's documentation can be accessed through the address: http://localhost:9090/auth/, once a **Authorization access key token is provided** by on the header.
Header example:

|Header name|Header value|
|--|--|
|Authorization|token provided by the system|

A new token will be provided by the address: http://localhost:9090/auth/login, when a user's clientlogin (email) and clientpassword are provided on the request header (More info on the API's documentation)

## Using the system

To use the system a new user should be created by the address: http://localhost:9090/auth/client/new
Example of body request to create new client:

    {
		"login": "user@email.com",
		"name": "username",
		"password": "userpassword",
		"role": "CLIENTE",
		"cpf": "01234567899",
		"origin": "SYSTEM_ADMIN",
		"nickname": "nickname"
	}

## Considerations
* New clients can only have CLIENTE role attached to body request, a new user can only became a GERENTE or SYSTEM_ADMIN when the logged user making the service call is a user with role SYSTEM_ADMIN;
* Users are created by a unique constraint of name and email;
* All new user are created with a new CONSULTAR_USUARIO authority which grants this newly created user when logged in to the system call for his/her own informations which includes: id, login, etc; which can be used by a front-end service to display user's info at a profile page for example;
* Another endpoints can be found in the API's documentation mentioned above;