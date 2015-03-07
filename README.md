# MeD Regatas

Aplicación de escritorio para la gestión de Regatas, creada en la asignatura de Metodoloxias de Desenvolvemento.

### Configuración

Arranca la Base de Datos:

	sudo service mysql start

o bien:
	
	mysqld

Creamos el usuario de BD:

	mysqladmin -u root create pojo

Creamos la BD:

	mysql -u root --password
	create database regatas;
	GRANT ALL PRIVILEGES ON regatas.* to pojo@localhost IDENTIFIED BY 'pojo';

### Descarga

Para descargar la aplicación:

	git clone https://github.com/GrupoMeD/MeD_Regatas

### QuickStart

Compilamos la aplicación:

	cd MeD_Regatas
	mvn install

Y la ejecutamos:

	mvn exec:java
