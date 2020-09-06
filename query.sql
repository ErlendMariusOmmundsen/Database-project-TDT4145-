# Finne navnet på alle rollene en gitt skuespiller har.
SELECT SIF.Rolle AS 'Rolle'
FROM Person AS P
	INNER JOIN SkuespillerIFilm AS SIF
		ON P.PersonID = SIF.PersonID
WHERE P.Navn = 'Navn'
UNION
SELECT SIE.Rolle AS 'Rolle'
FROM Person AS P
	INNER JOIN SkuespillerIEpisode AS SIE
		ON P.PersonID = SIE.PersonID
WHERE P.Navn = 'Navn';

# Finne hvilke filmer som en gitt skuespiller opptrer i.
SELECT PROD.Tittel AS 'Film'
FROM Person AS P
    INNER JOIN SkuespillerIFilm AS SIF
        ON P.PersonID = SIF.PersonID
    INNER JOIN Produksjon AS PROD
        ON SIF.ProduksjonID = PROD.ProduksjonID
WHERE P.Navn = 'Navn';

# Finne hvilke filmselskap som lager flest filmer innen hver sjanger
SELECT Kategori, Produksjonsselskap, AntallFilmer
FROM (SELECT Q1.Kategori, Q1.Produksjonsselskap, MAX(Q1.AntallFilmer) AS AntallFilmer
      FROM (SELECT K.Navn AS Kategori, P.Produksjonsselskap, COUNT(K.Navn) AS AntallFilmer
            FROM Kategori AS K
                     INNER JOIN Produksjonskategori AS PK
                                ON K.KategoriID = PK.KategoriID
                     INNER JOIN Produksjon AS P
                                ON PK.ProduksjonID = P.ProduksjonID
            GROUP BY K.Navn, P.Produksjonsselskap) AS Q1
      GROUP BY Q1.Kategori, Q1.Produksjonsselskap) as S1
         NATURAL JOIN
     (SELECT Q1.Kategori, MAX(Q1.AntallFilmer) AS AntallFilmer
      FROM (SELECT K.Navn AS Kategori, COUNT(K.Navn) AS AntallFilmer
            FROM Kategori AS K
                     INNER JOIN Produksjonskategori AS PK
                                ON K.KategoriID = PK.KategoriID
                     INNER JOIN Produksjon AS P
                                ON PK.ProduksjonID = P.ProduksjonID
            GROUP BY K.Navn, P.Produksjonsselskap) AS Q1
      GROUP BY Q1.Kategori) as S2;

# Sette inn en ny film med regissør, manusforfattere, skuespillere og det som hører med.
INSERT INTO Kategori VALUES (1, 'Komedie');
INSERT INTO Plattform VALUES (1, 'Kino');
INSERT INTO Produksjonsselskap VALUES ('Disney', 'http://www.disney.com', '500 South Buena Vista Street, Burbank, California, United States', 'USA');
INSERT INTO Produksjon VALUES (1, 'Disney', 'Toy Story 4', 'Dataanimert komediefilm', 2019, '2019-06-20', 'F', 120);
INSERT INTO Produksjonsplattform VALUES (1, 1);
INSERT INTO Produksjonskategori VALUES (1, 1);

INSERT INTO Person VALUES (1, 'Josh Cooley', 1980, 'USA');
INSERT INTO RegissørAvFilm VALUES (1, 1);

INSERT INTO Person VALUES (2, 'Tom Hanks', 1956, 'USA');
INSERT INTO Person VALUES (3, 'Tim Allen', 1953, 'USA');
INSERT INTO Person VALUES (4, 'Annie Potts', 1952, 'USA');
INSERT INTO SkuespillerIFilm VALUES (2, 1, 'Woody');
INSERT INTO SkuespillerIFilm VALUES (3, 1, 'Buzz Lightyear');
INSERT INTO SkuespillerIFilm VALUES (4, 1, 'Bo Peep');

INSERT INTO Person VALUES (5, 'John Lasseter', 1957, 'USA');
INSERT INTO ManusforfatterAvFilm VALUES (5, 1);

INSERT INTO Musikk VALUES (1, 'You''ve Got a Friend in Me', 'Randy Newman', 'Randy Newman');
INSERT INTO MusikkIFilm VALUES (1, 1);

# Sette inn ny anmeldelse av en episode av en serie.
INSERT INTO Anmeldelse VALUES (1, 1, 10, 'ThE bEsT ePiSoDe I HaVe SeEn xd');
INSERT INTO AnmeldelseForEpisode VALUES (2, 1, 1, 1);





