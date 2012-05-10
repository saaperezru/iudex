CREATE TABLE  PROGRAM (
  ID_PROGRAM INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(50) NOT NULL,
  PRIMARY KEY(ID_PROGRAM),
  UNIQUE (NAME)
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
  PRIMARY KEY(ID_PROFESSOR)
);



CREATE TABLE  SUBJECT (
  ID_SUBJECT INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(100) NOT NULL,
  DESCRIPTION VARCHAR(2000) NULL,
  PRIMARY KEY(ID_SUBJECT)
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


CREATE TABLE  COURSE_RATING (
  ID_COURSE_RATING INTEGER UNSIGNED NOT NULL,
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
  ID_SUBJECT_RATING INTEGER UNSIGNED NOT NULL,
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
  ID_PROFESSOR_RATING INTEGER UNSIGNED NOT NULL,
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
  ID_USER_ INTEGER UNSIGNED NULL,
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
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2520,'ADMINISTRACIÓN DE EMPRESAS');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2529,'ANTROPOLOGÍA - PROGRAMA ESPECIAL DE ADMISION PO');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2506,'ARQUITECTURA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2507,'ARTES PLÁSTICAS');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2513,'BIOLOGÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2538,'CIENCIA POLÍTICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2508,'CINE Y TELEVISIÓN');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2521,'CONTADURÍA PÚBLICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2539,'DERECHO');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2509,'DISEÑO GRÁFICO');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2510,'DISEÑO INDUSTRIAL');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2522,'ECONOMÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2540,'ENFERMERÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2526,'ESPAÑOL Y FILOLOGÍA CLÁSICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2514,'ESTADÍSTICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2534,'ESTUDIOS LITERARIOS');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2515,'FARMACIA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2528,'FILOLOGIA E IDIOMAS - ESPAÑOL Y HUMANIDADES CL');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2524,'FILOLOGIA E IDIOMAS ALEMÁN');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2525,'FILOLOGÍA E IDIOMAS FRANCÉS');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2527,'FILOLOGÍA E IDIOMAS INGLÉS');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2530,'FILOSOFÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2516,'FÍSICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2550,'FISIOTERAPIA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2551,'FONOAUDIOLOGÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2531,'GEOGRAFÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2757,'GEOGRAFÍA - PROGRAMA ESPECIAL DE ADMISIÓN POR ');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2517,'GEOLOGÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2532,'HISTORIA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2541,'INGENIERÍA AGRÍCOLA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2505,'INGENIERÍA AGRONÓMICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2364,'INGENIERÍA AGRONÓMICA - PRO. ESPECIAL DE ADMIS');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2542,'INGENIERÍA CIVIL');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2543,'INGENIERÍA DE SISTEMAS');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2544,'INGENIERÍA ELÉCTRICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2545,'INGENIERÍA ELECTRÓNICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2546,'INGENIERÍA INDUSTRIAL');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2547,'INGENIERÍA MECÁNICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2548,'INGENIERIA MECATRÓNICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2549,'INGENIERÍA QUÍMICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2837,'LINGÜISTICA_2');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2533,'LINGÜISTICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2518,'MATEMÁTICAS');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2552,'MEDICINA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2555,'MEDICINA VETERINARIA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2511,'MÚSICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2512,'MÚSICA INSTRUMENTAL');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2553,'NUTRICIÓN Y DIETÉTICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2557,'ODONTOLOGÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2535,'PSICOLOGÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2519,'QUÍMICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2536,'SOCIOLOGÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2554,'TERAPIA OCUPACIONAL');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2537,'TRABAJO SOCIAL');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2556,'ZOOTECNIA');

--INSERT INTO USER_

INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (1,1,'Administrator','Administrator','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',1,'administrator');
INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (2,1,'Student 1','Student 1','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student1'); -- BL-3.1, BL-3.2
INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (3,0,'Student 2','Student 2','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student2'); -- BL-10.1
INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (4,0,'Student 3','Student 3','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student3'); -- BL-3.3
INSERT INTO USER_ (ID_USER_,ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (5,1,'Student 4','Student 4','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student4'); -- BL-11.1
--INSERT INTO USER_PROGRAM
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (2,2520); -- student1
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (3,2529); -- student2
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (4,2506); -- student3
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (4,2507); -- student4


INSERT INTO CONFIRMATION_KEY (ID_USER_,CONFIRMATION_KEY,EXPIRATION_DATE) VALUES (3,'1d141671f909bb21d3658372a7dbb87af521bc8d8a92088fbdada64604bf1cf1',DATEADD('DAY',1,CURRENT_DATE())); -- student2, BL-10.1
INSERT INTO CONFIRMATION_KEY (ID_USER_,CONFIRMATION_KEY,EXPIRATION_DATE) VALUES (4,'f521bc8d8a92088fbdada64604bf1cf11d141671f909bb21d3658372a7dbb87a',DATEADD('DAY',1,CURRENT_DATE())); -- student3
-- INSERT INTO PROFESSOR

INSERT into PROFESSOR values ('1','MARIO','LINARES VASQUEZ','mlinaresv@unal.edu.co','Facultad: Ingeniería Departamento: Ingeniería de Sistemas Sede: Bogotá','http://www.docentes.unal.edu.co/mlinaresv/mario8.jpg','www.docentes.unal.edu.co/mlinaresv');
INSERT into PROFESSOR values ('2','FABIO AUGUSTO','GONZALEZ OSORIO','fagonzalezo@unal.edu.co','null','null','www.docentes.unal.edu.co/fagonzalezo');
INSERT into PROFESSOR values ('3','ALVARO','CARVAJAL DORIA','acarvajald@unal.edu.co','null','null','www.docentes.unal.edu.co/acarvajald');

-- INSERT INTO SUBJECTS
INSERT into SUBJECT values ('2016702','INGENIERIA DE SOFTWARE II','');
INSERT into SUBJECT values ('2019772','INGENIERIA DE SOFTWARE AVANZADA','');
INSERT into SUBJECT values ('2016025','AUDITORIA FINANCIERA I','');
INSERT into SUBJECT values ('2019855','AUDITORIAS DE SEGURIDAD VIAL','');
INSERT into SUBJECT values ('2021814','AUDITORIA TRIBUTARIA','');

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

-- INSERT INTO COMMENT_
-- 
INSERT into COMMENT_ values ('1','1','1','Muy buen curso','2012-01-26 14:43:24','4.5','0');
INSERT into COMMENT_ values ('2','2','1','PÉSIMO curso','2012-01-26 14:43:24','2.5','1');


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