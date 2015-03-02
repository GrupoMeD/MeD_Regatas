--Creamos las regatas
INSERT INTO  Regata (idRegata,nombre,descripcion)values(1,"regata1","desccripcionregata1");
INSERT INTO  Regata (idRegata,nombre,descripcion)values(2,"regata2","desccripcionregata2");

--Creamos los tipos
INSERT INTO  Tipo (idTipo,nombre,descripcion,compiteTmpReal)values(1,"Catamaran","descripcion Catamaran",false);
INSERT INTO  Tipo (idTipo,nombre,descripcion,compiteTmpReal)values(2,"Crucero","descripcion Crucero",false);
INSERT INTO  Tipo (idTipo,nombre,descripcion,compiteTmpReal)values(3,"Vela ligera","descripcion Vela Ligera",true);

--Creamos los barcos
INSERT INTO  Barco (idBarco,vela,nombre,idTipo,gph,modelo)values(1,1234,"Juan Sebastian El Cano",1,1.5,"Lagoon 421");
INSERT INTO  Barco (idBarco,vela,nombre,idTipo,gph,modelo)values(2,2345,"El Holandes Errante",2,2.5,"SWAN 66 FD");
INSERT INTO  Barco (idBarco,vela,nombre,idTipo,gph,modelo)values(3,3456,"La Perla Negra",2,1.5,"X6");
INSERT INTO  Barco (idBarco,vela,nombre,idTipo,gph,modelo)values(4,4567,"Venus",3,null,"Laser Estandar");
INSERT INTO  Barco (idBarco,vela,nombre,idTipo,gph,modelo)values(5,5678,"Apolo",3,null,"Laser Radial");

--Inscribimos los barcos en la regata 1
INSERT INTO Inscripcion (idInscripcion, idBarco,idRegata,patron) VALUES (1,1,1,"Samuel Paredes");
INSERT INTO Inscripcion (idInscripcion, idBarco,idRegata,patron) VALUES (2,2,1,"Iago Suarez");
INSERT INTO Inscripcion (idInscripcion, idBarco,idRegata,patron) VALUES (3,3,1,"Adrian Pallas");
INSERT INTO Inscripcion (idInscripcion, idBarco,idRegata,patron) VALUES (4,4,1,"Jesus Lopez");
INSERT INTO Inscripcion (idInscripcion, idBarco,idRegata,patron) VALUES (5,5,1,"Diego Bascoy");

--Inscribimos un barco en la regata 2
INSERT INTO Inscripcion (idInscripcion, idBarco,idRegata,patron) VALUES (6,5,2,"Diego Bascoy");

--Creamos 3 mangas para la regata 1
INSERT INTO  Manga (idManga,fecha,millas,idRegata)values(1,STR_TO_DATE('10,11,2014,01,10','%d,%m,%Y,%H,%i'),15,1);
INSERT INTO  Manga (idManga,fecha,millas,idRegata)values(2,STR_TO_DATE('10,11,2014,01,10','%d,%m,%Y,%H,%i'),10,1);
INSERT INTO  Manga (idManga,fecha,millas,idRegata)values(3,STR_TO_DATE('11,11,2014,03,10','%d,%m,%Y,%H,%i'),5,1);

--Posiciones para la manga 1 sin penalizaciones
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(1,3600,0,1,1,2,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(2,3660,0,1,2,3,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(3,5400,0,1,3,5,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(4,4500,0,1,4,4,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(5,3540,0,1,5,1,0);

--Posiciones para la manga 2
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(6,3400,0,2,1,3,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(7,3600,0,2,2,4,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(8,3950,0,2,3,5,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(9,3200,0,2,4,2,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(10,3100,0,2,5,1,0);

--Posiciones para la manga 3
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(11,3600,0,3,1,2,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(12,3660,0,3,2,3,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(13,5400,0,3,3,5,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(14,4500,0,3,4,4,0);
INSERT INTO  Posicion (idPosicion,segTiempo,penal,idManga,idBarco,puntos,segPenalizacion)values(15,3540,0,3,5,1,0);


