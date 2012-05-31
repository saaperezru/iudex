CREATE TABLE  PROGRAM (
  ID_PROGRAM INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(50) NOT NULL,
  CODE INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(ID_PROGRAM),
  UNIQUE (NAME),
  UNIQUE (CODE)
);

CREATE TABLE  USER_ (
  ID_USER_ INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  FIRST_NAMES VARCHAR(50) NOT NULL,
  LAST_NAMES VARCHAR(50) NOT NULL,
  USER_NAME VARCHAR(20) NOT NULL,
  PASSWORD_ VARCHAR(64) NOT NULL,
  ACTIVE BIT NOT NULL DEFAULT 0,
  ROL TINYINT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY(ID_USER_)
);

ALTER TABLE USER_ ADD UNIQUE ( USER_NAME );

CREATE TABLE CONFIRMATION_KEY (
  ID_CONFIRMATION_KEY INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ID_USER_ INTEGER UNSIGNED NOT NULL,
  EXPIRATION_DATE DATE NOT NULL,
  CONFIRMATION_KEY VARCHAR(64) NOT NULL,
  PRIMARY KEY(ID_CONFIRMATION_KEY),
  FOREIGN KEY(ID_USER_)
    REFERENCES USER_(ID_USER_)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);

CREATE INDEX CONFIRMATION_KEY_FKIndex1 ON CONFIRMATION_KEY(ID_USER_);

CREATE TABLE  USER_PROGRAM (
  ID_USER_PROGRAM INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ID_USER_ INTEGER UNSIGNED NOT NULL,
  ID_PROGRAM INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(ID_USER_PROGRAM),
  FOREIGN KEY(ID_USER_)
    REFERENCES USER_(ID_USER_)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  FOREIGN KEY(ID_PROGRAM)
    REFERENCES PROGRAM(ID_PROGRAM)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);

CREATE INDEX  USER_COURSE_FKIndex1 ON USER_PROGRAM(ID_USER_);
CREATE INDEX  USER_COURSE_FKIndex2 ON USER_PROGRAM(ID_PROGRAM);


CREATE TABLE  PROFESSOR (
  ID_PROFESSOR INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  FIRST_NAMES VARCHAR(50) NOT NULL,
  LAST_NAMES VARCHAR(50) NOT NULL,
  E_MAIL VARCHAR(50) NULL,
  DESCRIPTION VARCHAR(2000) NULL,
  URL_IMAGE VARCHAR(255) NULL,
  URL_WEB VARCHAR(255) NULL,
  PRIMARY KEY(ID_PROFESSOR),
  UNIQUE(E_MAIL), 
  UNIQUE(URL_WEB),
  UNIQUE(FIRST_NAMES, LAST_NAMES)
);



CREATE TABLE  SUBJECT (
  ID_SUBJECT INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(100) NOT NULL,
  DESCRIPTION VARCHAR(2000) NULL,
  CODE INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(ID_SUBJECT),
  UNIQUE (CODE)
);



CREATE TABLE  PERIOD_ (
  ID_PERIOD INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  YEAR_ INT NOT NULL,
  SEMESTER INT NOT NULL,
  PRIMARY KEY(ID_PERIOD), 
  UNIQUE(YEAR_, SEMESTER)
);


CREATE TABLE  COURSE (
  ID_COURSE INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ID_PROFESSOR INTEGER UNSIGNED NOT NULL,
  ID_SUBJECT INTEGER UNSIGNED NOT NULL,
  ID_PERIOD INTEGER UNSIGNED NOT NULL,
  AVERAGE FLOAT NOT NULL DEFAULT 0,
  RATINGCOUNT INTEGER UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY(ID_COURSE),
  FOREIGN KEY(ID_PROFESSOR)
    REFERENCES PROFESSOR(ID_PROFESSOR)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  FOREIGN KEY(ID_SUBJECT)
    REFERENCES SUBJECT(ID_SUBJECT)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  FOREIGN KEY(ID_PERIOD)
    REFERENCES PERIOD_(ID_PERIOD)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);
CREATE INDEX  TEACHER_SUBJECT_PERIOD_FKIndex2 ON COURSE(ID_SUBJECT);
CREATE INDEX  TEACHER_SUBJECT_PERIOD_FKIndex3 ON COURSE(ID_PERIOD);
CREATE INDEX  PROFESSOR_SUBJECT_PERIOD_FKIndex3 ON COURSE(ID_PROFESSOR);
ALTER TABLE COURSE ADD UNIQUE (ID_PROFESSOR,ID_SUBJECT,ID_PERIOD);


CREATE TABLE  COURSE_RATING (
  ID_COURSE_RATING INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ID_COURSE INTEGER UNSIGNED NOT NULL,
  ID_USER_ INTEGER UNSIGNED NOT NULL,
  RATING FLOAT NOT NULL DEFAULT 0,
  PRIMARY KEY(ID_COURSE_RATING),
  FOREIGN KEY(ID_USER_)
    REFERENCES USER_(ID_USER_)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  FOREIGN KEY(ID_COURSE)
    REFERENCES COURSE(ID_COURSE)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);

CREATE INDEX  USER__has_COURSE_FKIndex1 ON COURSE_RATING(ID_USER_);
CREATE INDEX  USER__has_COURSE_FKIndex2 ON COURSE_RATING(ID_COURSE);

CREATE TABLE  SUBJECT_RATING (
  ID_SUBJECT_RATING INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ID_SUBJECT INTEGER UNSIGNED NOT NULL,
  ID_USER_ INTEGER UNSIGNED NOT NULL,
  RATING TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY(ID_SUBJECT_RATING),
  FOREIGN KEY(ID_USER_)
    REFERENCES USER_(ID_USER_)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  FOREIGN KEY(ID_SUBJECT)
    REFERENCES SUBJECT(ID_SUBJECT)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);

CREATE INDEX  USER__has_SUBJECT_FKIndex1 ON SUBJECT_RATING(ID_USER_);
CREATE INDEX  USER__has_SUBJECT_FKIndex2 ON SUBJECT_RATING(ID_SUBJECT);

CREATE TABLE  PROFESSOR_RATING (
  ID_PROFESSOR_RATING INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ID_PROFESSOR INTEGER UNSIGNED NOT NULL,
  ID_USER_ INTEGER UNSIGNED NOT NULL,
  RATING TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY(ID_PROFESSOR_RATING),
  FOREIGN KEY(ID_USER_)
    REFERENCES USER_(ID_USER_)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  FOREIGN KEY(ID_PROFESSOR)
    REFERENCES PROFESSOR(ID_PROFESSOR)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);

CREATE INDEX  USER__has_PROFESSOR_FKIndex1 ON PROFESSOR_RATING(ID_USER_);
CREATE INDEX  USER__has_PROFESSOR_FKIndex2 ON PROFESSOR_RATING(ID_PROFESSOR);

CREATE TABLE COMMENT_ (
  ID_COMMENT_ INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ID_USER_ INTEGER UNSIGNED NOT NULL,
  ID_COURSE INTEGER UNSIGNED NOT NULL,
  CONTENT VARCHAR(2000) NOT NULL,
  DATE_COMMENT DATETIME NOT NULL,
  RATING FLOAT NOT NULL,
  ANONYMOUS CHAR NOT NULL DEFAULT 0,
  PRIMARY KEY(ID_COMMENT_),
  FOREIGN KEY(ID_COURSE)
    REFERENCES COURSE(ID_COURSE)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  FOREIGN KEY(ID_USER_)
    REFERENCES USER_(ID_USER_)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);

CREATE  INDEX COMENT_FKIndex2 ON COMMENT_(ID_COURSE);
CREATE  INDEX COMMENT__FKIndex2 ON COMMENT_(ID_USER_);

CREATE TABLE COMMENT_RATING (
  ID_COMMENT_RATING INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ID_COMMENT_ INTEGER UNSIGNED NOT NULL,
  ID_USER_ INTEGER UNSIGNED NOT NULL,
  RATING INTEGER NOT NULL DEFAULT 0,
  UNIQUE (ID_COMMENT_,ID_USER_),
  PRIMARY KEY(ID_COMMENT_RATING),
  FOREIGN KEY(ID_USER_)
    REFERENCES USER_(ID_USER_)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  FOREIGN KEY(ID_COMMENT_)
    REFERENCES COMMENT_(ID_COMMENT_)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);

CREATE  INDEX USER__has_COMMENT__FKIndex1 ON COMMENT_RATING(ID_USER_);
CREATE  INDEX USER__has_COMMENT__FKIndex2 ON COMMENT_RATING(ID_COMMENT_);

CREATE TABLE TYPE_FEEDBACK (
  ID_TYPE_FEEDBACK INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(20) NOT NULL,
  PRIMARY KEY(ID_TYPE_FEEDBACK)
);

ALTER TABLE TYPE_FEEDBACK ADD UNIQUE ( NAME );

CREATE TABLE FEEDBACK (
  ID_FEEDBACK INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  ID_TYPE_FEEDBACK INTEGER UNSIGNED NOT NULL,
  CONTENT BLOB NOT NULL,
  DATE_FEEDBACK DATETIME NOT NULL,
  PRIMARY KEY(ID_FEEDBACK),
  FOREIGN KEY(ID_TYPE_FEEDBACK)
    REFERENCES TYPE_FEEDBACK(ID_TYPE_FEEDBACK)
      ON DELETE RESTRICT
      ON UPDATE NO ACTION
);

CREATE INDEX FEEDBACK_has_TYPE_FKIndex1 ON FEEDBACK(ID_TYPE_FEEDBACK);

--INSERT INTO PERIOD_

INSERT INTO PERIOD_ VALUES ('1','2008','1');
INSERT INTO PERIOD_ VALUES ('2','2008','2');
INSERT INTO PERIOD_ VALUES ('3','2009','1');
INSERT INTO PERIOD_ VALUES ('4','2009','2');
INSERT INTO PERIOD_ VALUES ('5','2010','1');
INSERT INTO PERIOD_ VALUES ('6','2010','2');
INSERT INTO PERIOD_ VALUES ('7','2011','1');
INSERT INTO PERIOD_ VALUES ('8','2011','2');
INSERT INTO PERIOD_ VALUES ('9','2012','1');

-- INSERT INTO PROGRAM
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2520,'ADMINISTRACIÓN DE EMPRESAS',2520);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2529,'ANTROPOLOGÍA - PROGRAMA ESPECIAL DE ADMISION PO',2529);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2506,'ARQUITECTURA',2506);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2507,'ARTES PLÁSTICAS',2507);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2513,'BIOLOGÍA',2513);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2538,'CIENCIA POLÍTICA',2538);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2508,'CINE Y TELEVISIÓN',2508);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2521,'CONTADURÍA PÚBLICA',2521);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2539,'DERECHO',2539);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2509,'DISEÑO GRÁFICO',2509);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2510,'DISEÑO INDUSTRIAL',2510);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2522,'ECONOMÍA',2522);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2540,'ENFERMERÍA',2540);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2526,'ESPAÑOL Y FILOLOGÍA CLÁSICA',2526);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2514,'ESTADÍSTICA',2514);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2534,'ESTUDIOS LITERARIOS',2534);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2515,'FARMACIA',2515);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2528,'FILOLOGIA E IDIOMAS - ESPAÑOL Y HUMANIDADES',2528);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2524,'FILOLOGIA E IDIOMAS ALEMÁN',2524);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2525,'FILOLOGÍA E IDIOMAS FRANCÉS',2525);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2527,'FILOLOGÍA E IDIOMAS INGLÉS',2527);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2530,'FILOSOFÍA',2530);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2516,'FÍSICA',2516);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2550,'FISIOTERAPIA',2550);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2551,'FONOAUDIOLOGÍA',2551);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2531,'GEOGRAFÍA',2531);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2757,'GEOGRAFÍA - PROGRAMA ESPECIAL DE ADMISIÓN POR',2757);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2517,'GEOLOGÍA',2517);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2532,'HISTORIA',2532);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2541,'INGENIERÍA AGRÍCOLA',2541);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2505,'INGENIERÍA AGRONÓMICA',2505);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2364,'INGENIERÍA AGRONÓMICA - PRO. ESPECIAL DE ADMI',2364);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2542,'INGENIERÍA CIVIL',2542);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2543,'INGENIERÍA DE SISTEMAS',2543);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2544,'INGENIERÍA ELÉCTRICA',2544);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2545,'INGENIERÍA ELECTRÓNICA',2545);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2546,'INGENIERÍA INDUSTRIAL',2546);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2547,'INGENIERÍA MECÁNICA',2547);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2548,'INGENIERIA MECATRÓNICA',2548);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2549,'INGENIERÍA QUÍMICA',2549);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2837,'LINGÜISTICA_2',2837);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2533,'LINGÜISTICA',2533);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2518,'MATEMÁTICAS',2518);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2552,'MEDICINA',2552);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2555,'MEDICINA VETERINARIA',2555);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2511,'MÚSICA',2511);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2512,'MÚSICA INSTRUMENTAL',2512);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2553,'NUTRICIÓN Y DIETÉTICA',2553);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2557,'ODONTOLOGÍA',2557);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2535,'PSICOLOGÍA',2535);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2519,'QUÍMICA',2519);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2536,'SOCIOLOGÍA',2536);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2554,'TERAPIA OCUPACIONAL',2554);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2537,'TRABAJO SOCIAL',2537);
INSERT INTO PROGRAM (ID_PROGRAM,NAME,CODE) VALUES (2556,'ZOOTECNIA',2556);

--INSERT INTO USER_
--All the passwords are 123456789
INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (1,1,'Administrator','Administrator','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',1,'administrator');
INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (2,1,'Student 1','Student 1','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student1'); -- BL-3.1, BL-3.2
INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (3,0,'Student 2','Student 2','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student2'); -- BL-10.1
INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (4,0,'Student 3','Student 3','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student3'); -- BL-3.3
INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (5,1,'Student 4','Student 4','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student4'); -- BL-11.1
INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (6,1,'Student 5','Student 5','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student5'); -- BL-2.3

--INSERT INTO USER_PROGRAM
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (2,2520); -- student1
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (3,2529); -- student2
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (3,2506); -- student3
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (4,2507); -- student4
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (5,2554); -- student5

--INSERT INTO CONFIRMATION_KEY
INSERT INTO CONFIRMATION_KEY (ID_USER_,CONFIRMATION_KEY,EXPIRATION_DATE) VALUES (3,'1d141671f909bb21d3658372a7dbb87af521bc8d8a92088fbdada64604bf1cf1',DATEADD('DAY',1,CURRENT_DATE())); -- student2, BL-10.1
INSERT INTO CONFIRMATION_KEY (ID_USER_,CONFIRMATION_KEY,EXPIRATION_DATE) VALUES (4,'f521bc8d8a92088fbdada64604bf1cf11d141671f909bb21d3658372a7dbb87a',DATEADD('DAY',1,CURRENT_DATE())); -- student3

-- INSERT INTO PROFESSOR
INSERT into PROFESSOR values ('1','MARIO','LINARES VASQUEZ','mlinaresv@unal.edu.co','Facultad: Ingeniería Departamento: Ingeniería de Sistemas Sede: Bogotá','http://www.docentes.unal.edu.co/mlinaresv/mario8.jpg','www.docentes.unal.edu.co/mlinaresv');
INSERT into PROFESSOR values ('2','FABIO AUGUSTO','GONZALEZ OSORIO','fagonzalezo@unal.edu.co','null','null','www.docentes.unal.edu.co/fagonzalezo');
INSERT into PROFESSOR values ('3','ALVARO','CARVAJAL DORIA','acarvajald@unal.edu.co','null','null','www.docentes.unal.edu.co/acarvajald');

-- INSERT INTO SUBJECTS
INSERT into SUBJECT values ('2016702','INGENIERIA DE SOFTWARE II','',2016702);
INSERT into SUBJECT values ('2019772','INGENIERIA DE SOFTWARE AVANZADA','',2019772);
INSERT into SUBJECT values ('2016025','AUDITORIA FINANCIERA I','',2016025);
INSERT into SUBJECT values ('2019855','AUDITORIAS DE SEGURIDAD VIAL','',2019855);
INSERT into SUBJECT values ('2021814','AUDITORIA TRIBUTARIA','',2021814);
INSERT into SUBJECT values ('2033859','CATEDRA EDITABLE','',2033859);
INSERT into SUBJECT values ('2039461','SEMINARIO EDITABLE','',2039461);
INSERT into SUBJECT values ('2039372','TALLER EDITABLE','',2039372);
INSERT into SUBJECT values ('2023859','CATEDRA ELIMINABLE','',2023859);
INSERT into SUBJECT values ('2029461','SEMINARIO ELIMINABLE','',2029461);
INSERT into SUBJECT values ('2029372','TALLER ELIMINABLE','',2029372);
INSERT into SUBJECT values ('2044356','SEMINARIO CALIFICABLE','',2044356);
INSERT into SUBJECT values ('2042911','TALLER CALIFICABLE','',2042911);

-- INSERT INTO COURSES
--MARIO COURSES
INSERT into COURSE values ('1','1','2016702','1','4.3','4');
INSERT into COURSE values ('2','1','2019772','1','4.2','4');
INSERT into COURSE values ('3','1','2016025','1','3.1','4');
--FABIO COURSES
INSERT into COURSE values ('4','2','2019772','1','3.7','4');
--ALVARO COURSES
INSERT into COURSE values ('5','3','2016025','1',0,0);
INSERT into COURSE values ('6','3','2019855','1','4.0','4');
INSERT into COURSE values ('7','3','2021814','1','4.2','4');

-- INSERT INTO PROFESSOR_RATING
--MARIO RATINGS 
INSERT into PROFESSOR_RATING values ('1','1','1',1);
INSERT into PROFESSOR_RATING values ('2','1','2',1);
INSERT into PROFESSOR_RATING values ('3','1','3',1);
INSERT into PROFESSOR_RATING values ('4','1','4',-1);
--FABIO RATINGS 
INSERT into PROFESSOR_RATING values ('5','2','1',1);
INSERT into PROFESSOR_RATING values ('6','2','2',1);
INSERT into PROFESSOR_RATING values ('7','2','3',1);
INSERT into PROFESSOR_RATING values ('8','2','4',0);
--ALVARO RATINGS 
INSERT into PROFESSOR_RATING values ('9','3','1',1);
INSERT into PROFESSOR_RATING values ('10','3','2',1);
INSERT into PROFESSOR_RATING values ('11','3','3',-1);
INSERT into PROFESSOR_RATING values ('12','3','4',-1);

-- INSERT INTO SUBJECT_RATING
--IS2 2016702 RATINGS
INSERT into SUBJECT_RATING values ('1','2016702','1',1);
INSERT into SUBJECT_RATING values ('2','2016702','2',1);
INSERT into SUBJECT_RATING values ('3','2016702','3',1);
INSERT into SUBJECT_RATING values ('4','2016702','4',1);
--ISA 2019772 RATINGS
INSERT into SUBJECT_RATING values ('5','2019772','1',1);
INSERT into SUBJECT_RATING values ('6','2019772','2',1);
INSERT into SUBJECT_RATING values ('7','2019772','3',-1);
INSERT into SUBJECT_RATING values ('8','2019772','4',-1);
--AFI 2016025 RATINGS
INSERT into SUBJECT_RATING values ('9','2016025','1',1);
INSERT into SUBJECT_RATING values ('10','2016025','2',1);
INSERT into SUBJECT_RATING values ('11','2016025','3',1);
INSERT into SUBJECT_RATING values ('12','2016025','4',-1);
--AT 2021814 RATINGS
INSERT into SUBJECT_RATING values ('13','2021814','1',1);
INSERT into SUBJECT_RATING values ('14','2021814','2',1);
INSERT into SUBJECT_RATING values ('15','2021814','3',0);
INSERT into SUBJECT_RATING values ('16','2021814','4',-1);
--ASV 2019855 RATINGS
INSERT into SUBJECT_RATING values ('17','2019855','1',-1);
INSERT into SUBJECT_RATING values ('18','2019855','2',-1);
INSERT into SUBJECT_RATING values ('19','2019855','3',-1);
INSERT into SUBJECT_RATING values ('20','2019855','4',-1);
--SC 2044356 RATINGS
INSERT into SUBJECT_RATING values ('21','2044356','1',0);
INSERT into SUBJECT_RATING values ('22','2044356','2',-1);
INSERT into SUBJECT_RATING values ('23','2044356','3',1);
INSERT into SUBJECT_RATING values ('24','2044356','4',1);
--TC 2042911 RATINGS
INSERT into SUBJECT_RATING values ('25','2042911','1',-1);
INSERT into SUBJECT_RATING values ('26','2042911','2',-1);
INSERT into SUBJECT_RATING values ('27','2042911','3',0);
INSERT into SUBJECT_RATING values ('28','2042911','4',1);
-- INSERT INTO COMMENT_
-- 
INSERT into COMMENT_ values ('1','1','1','Muy buen curso','2012-01-26 14:43:24','4.5','0');
INSERT into COMMENT_ values ('2','2','1','PÉSIMO curso','2012-01-26 14:43:24','2.5','0');
INSERT into COMMENT_ values ('3','2','1','áño <script> for (;;){alert(1);}</script>','2012-01-26 14:43:24','2.5','0');
INSERT into COMMENT_ values ('4','1','1','áño <script> for (;;){alert(1);}</script>','2012-01-26 14:43:24','4.5','1');


-- INSERT INTO COMMENT_RATING
--RATINGS OF GOOD COMMENT ABOUT IS2 LINARES
INSERT into COMMENT_RATING values ('1','1','1','-1');
INSERT into COMMENT_RATING values ('2','1','2','1');
INSERT into COMMENT_RATING values ('3','1','3','1');
INSERT into COMMENT_RATING values ('4','1','4','-1');


-- INSERT INTO FEEDBACK TYPE

insert into TYPE_FEEDBACK values ('1','Comentario General');
insert into TYPE_FEEDBACK values ('2','Error');
insert into TYPE_FEEDBACK values ('3','Sugerencia');

-- INSERT INTO FEEDBACK 

insert into FEEDBACK values ('1','2',RAWTOHEX('FEEDBACK 1'),'2012-01-21 11:38:23');
insert into FEEDBACK values ('2','3',RAWTOHEX('FEEDBACK 2'),'2012-01-26 18:38:47');
insert into FEEDBACK values ('3','3',RAWTOHEX('FEEDBACK 3'),'2012-03-14 20:34:38');
insert into FEEDBACK values ('4','2',RAWTOHEX('FEEDBACK 4'),'2012-01-29 20:16:17');
insert into FEEDBACK values ('5','1',RAWTOHEX('FEEDBACK 5'),'2012-02-03 22:43:57');
insert into FEEDBACK values ('6','1',RAWTOHEX('FEEDBACK 6'),'2012-03-30 16:46:56');
insert into FEEDBACK values ('7','1',RAWTOHEX('FEEDBACK 7'),'2012-04-06 09:23:16');
insert into FEEDBACK values ('8','2',RAWTOHEX('FEEDBACK 8'),'2012-03-05 04:16:27');
insert into FEEDBACK values ('9','1',RAWTOHEX('FEEDBACK 9'),'2012-01-31 20:48:08');
