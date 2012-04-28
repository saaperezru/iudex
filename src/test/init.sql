CREATE TABLE PROGRAM (
  ID_PROGRAM INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(50) NOT NULL,
  PRIMARY KEY(ID_PROGRAM)
);

CREATE TABLE USER_ (
  ID_USER_ INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  FIRST_NAMES VARCHAR(50) NOT NULL,
  LAST_NAMES VARCHAR(50) NOT NULL,
  USER_NAME VARCHAR(20) NOT NULL,
  PASSWORD_ VARCHAR(64) NOT NULL,
  ACTIVE BIT NOT NULL DEFAULT 0,
  ROL TINYINT UNSIGNED NOT NULL DEFAULT 1,
  PRIMARY KEY(ID_USER_)
);

ALTER TABLE USER_ ADD UNIQUE ( USER_NAME );

CREATE TABLE CONFIRMATION_KEY (
  ID_USER_ INTEGER UNSIGNED NOT NULL,
  EXPIRATION_DATE DATE NOT NULL,
  CONFIRMATION_KEY VARCHAR(64) NOT NULL,
  PRIMARY KEY(ID_USER_),
  FOREIGN KEY(ID_USER_)
    REFERENCES USER_(ID_USER_)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);

CREATE TABLE USER_PROGRAM (
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

CREATE INDEX USER_COURSE_FKIndex1 ON USER_PROGRAM(ID_USER_);
CREATE INDEX USER_COURSE_FKIndex2 ON USER_PROGRAM(ID_PROGRAM);

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
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2528,'FILOLOGIA E IDIOMAS - ESPAÑOL Y HUMANIDADES CLA...');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2524,'FILOLOGIA E IDIOMAS ALEMÁN');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2525,'FILOLOGÍA E IDIOMAS FRANCÉS');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2527,'FILOLOGÍA E IDIOMAS INGLÉS');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2530,'FILOSOFÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2516,'FÍSICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2550,'FISIOTERAPIA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2551,'FONOAUDIOLOGÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2531,'GEOGRAFÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2757,'GEOGRAFÍA - PROGRAMA ESPECIAL DE ADMISIÓN POR Á...');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2517,'GEOLOGÍA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2532,'HISTORIA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2541,'INGENIERÍA AGRÍCOLA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2505,'INGENIERÍA AGRONÓMICA');
INSERT INTO PROGRAM (ID_PROGRAM,NAME) VALUES (2364,'INGENIERÍA AGRONÓMICA - PRO. ESPECIAL DE ADMISI...');
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

-- INSERT INTO USER

INSERT INTO USER_ (ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (1,'Administrator','Administrator','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',1,'administrator');
INSERT INTO USER_ (ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (1,'Student 1','Student 1','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student1');
INSERT INTO USER_ (ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (0,'Student 2','Student 2','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student2');
INSERT INTO USER_ (ACTIVE,FIRST_NAMES,LAST_NAMES,PASSWORD_,ROL,USER_NAME) VALUES (0,'Student 3','Student 3','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225',0,'student3');

-- INSERT INTO USER_PROGRAM

INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (2,2520);
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (3,2529);
INSERT INTO USER_PROGRAM (ID_USER_,ID_PROGRAM) VALUES (4,2506);

-- INSERT INTO CONFIRMATION_KEY

INSERT INTO CONFIRMATION_KEY (ID_USER_,CONFIRMATION_KEY,EXPIRATION_DATE) VALUES (3,'1d141671f909bb21d3658372a7dbb87af521bc8d8a92088fbdada64604bf1cf1',DATEADD('DAY',1,CURRENT_DATE()));
INSERT INTO CONFIRMATION_KEY (ID_USER_,CONFIRMATION_KEY,EXPIRATION_DATE) VALUES (4,'f521bc8d8a92088fbdada64604bf1cf11d141671f909bb21d3658372a7dbb87a',DATEADD('DAY',1,CURRENT_DATE()));