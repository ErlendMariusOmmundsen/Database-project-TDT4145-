package databaseprosjekt;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller extends DBConn {
	
	//[1] Finn navnet på alle rollene en gitt skuespiller har.
	public ArrayList<String> getRoles(String actor){
		PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("SELECT SIF.Rolle AS 'Rolle'\n" + 
            		"FROM Person AS P\n" + 
            		"	INNER JOIN SkuespillerIFilm AS SIF\n" + 
            		"		ON P.PersonID = SIF.PersonID\n" + 
            		"WHERE P.Navn = (?) \n" + 
            		"UNION\n" + 
            		"SELECT SIE.Rolle AS 'Rolle'\n" + 
            		"FROM Person AS P\n" + 
            		"	INNER JOIN SkuespillerIEpisode AS SIE\n" + 
            		"		ON P.PersonID = SIE.PersonID\n" + 
            		"WHERE P.Navn = (?) ;");
            
            statement.setString(1, actor);
            statement.setString(2, actor);
            
            ResultSet rs = statement.executeQuery();
            
            ArrayList<String> roles = new ArrayList<String>();
            
            while (rs.next()) {
                String role = rs.getString("Rolle");
                roles.add(role);
            }
            
            return roles;
            
        }catch (Exception e) {
			System.out.println(e);
		}
		
		return null;
	}
	

    // [2] Finn hvilke filmer som en gitt skuespiller opptrer i.
    public ArrayList<String> getMovies(String actor) {
        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("SELECT PROD.Tittel AS 'Film' " +
                    "FROM Person AS P " +
                    "    INNER JOIN SkuespillerIFilm AS SIF " +
                    "        ON P.PersonID = SIF.PersonID " +
                    "    INNER JOIN Produksjon AS PROD " +
                    "        ON SIF.ProduksjonID = PROD.ProduksjonID " +
                    "WHERE P.Navn = (?)");

            statement.setString(1, actor);

            ResultSet rs = statement.executeQuery();

            ArrayList<String> movies = new ArrayList<>();

            while (rs.next()) {
                String movie = rs.getString("Film");
                movies.add(movie);
            }

            return movies;

        } catch (Exception e) {
            return null;
        }

    }

    // [3] Finn hvilke filmselskap som lager flest filmer inne hver sjanger.
    public HashMap<String, ArrayList<Object>> getMostCommonProductionCompanyPerCategory() {
        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("SELECT Kategori, Produksjonsselskap, AntallFilmer\n" +
                    "FROM (SELECT Q1.Kategori, Q1.Produksjonsselskap, MAX(Q1.AntallFilmer) AS AntallFilmer\n" +
                    "      FROM (SELECT K.Navn AS Kategori, P.Produksjonsselskap, COUNT(K.Navn) AS AntallFilmer\n" +
                    "            FROM Kategori AS K\n" +
                    "                     INNER JOIN Produksjonskategori AS PK\n" +
                    "                                ON K.KategoriID = PK.KategoriID\n" +
                    "                     INNER JOIN Produksjon AS P\n" +
                    "                                ON PK.ProduksjonID = P.ProduksjonID\n" +
                    "            GROUP BY K.Navn, P.Produksjonsselskap) AS Q1\n" +
                    "      GROUP BY Q1.Kategori, Q1.Produksjonsselskap) as S1\n" +
                    "         NATURAL JOIN\n" +
                    "     (SELECT Q1.Kategori, MAX(Q1.AntallFilmer) AS AntallFilmer\n" +
                    "      FROM (SELECT K.Navn AS Kategori, COUNT(K.Navn) AS AntallFilmer\n" +
                    "            FROM Kategori AS K\n" +
                    "                     INNER JOIN Produksjonskategori AS PK\n" +
                    "                                ON K.KategoriID = PK.KategoriID\n" +
                    "                     INNER JOIN Produksjon AS P\n" +
                    "                                ON PK.ProduksjonID = P.ProduksjonID\n" +
                    "            GROUP BY K.Navn, P.Produksjonsselskap) AS Q1\n" +
                    "      GROUP BY Q1.Kategori) as S2");

            ResultSet rs = statement.executeQuery();

            HashMap<String, ArrayList<Object>> map = new HashMap<>();

            while (rs.next()) {
                String genre = rs.getString("Kategori");
                String company = rs.getString("Produksjonsselskap");
                Integer movieCount = rs.getInt("AntallFilmer");
                ArrayList<Object> info = new ArrayList<>();
                info.add(company);
                info.add(movieCount);

                map.put(genre, info);
            }

            return map;

        } catch (Exception e) {
            return null;
        }

    }

    // [4] Sett inn en ny film med regissør, manusforfattere, skuespillere og det som hører med.
    public HashMap<Integer, String> getCategories() {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("SELECT * FROM Kategori");

            ResultSet rs = statement.executeQuery();

            HashMap<Integer, String> categories = new HashMap<>();

            while (rs.next()) {
                int id  = rs.getInt("KategoriID");
                String name = rs.getString("Navn");
                categories.put(id, name);
            }

            return categories;

        } catch (Exception e) {
            return null;
        }

    }

    public Integer insertCategory(String categoryName) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO Kategori (Navn) VALUES ( (?) )", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, categoryName);
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public HashMap<Integer, String> getPlatforms() {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("SELECT * FROM Plattform");

            ResultSet rs = statement.executeQuery();

            HashMap<Integer, String> platforms = new HashMap<>();

            while (rs.next()) {
                int id  = rs.getInt("PlattformID");
                String name = rs.getString("Navn");
                platforms.put(id, name);
            }

            return platforms;

        } catch (Exception e) {
            return null;
        }

    }

    public Integer insertPlatform(String platformName) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO Plattform (Navn) VALUES ( (?) )", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, platformName);
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public ArrayList<String> getProductionCompanies() {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("SELECT Navn FROM Produksjonsselskap");

            ResultSet rs = statement.executeQuery();

            ArrayList<String> productionCompanies = new ArrayList<>();

            while (rs.next()) {
                String name = rs.getString("Navn");
                productionCompanies.add(name);
            }

            return productionCompanies;

        } catch (Exception e) {
            return null;
        }

    }

    public void insertProductionCompany(String name, String URL, String address, String country) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO Produksjonsselskap VALUES ( (?), (?), (?), (?) )");
            statement.setString(1, name);
            statement.setString(2, URL);
            statement.setString(3, address);
            statement.setString(4, country);

            statement.execute();

        } catch (Exception ignored) {
        }

    }

    public Integer insertProduction(String productionCompany, String title, String description, Integer releaseYear, String releaseDate, Integer length) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO " +
                    "Produksjon (Produksjonsselskap, Tittel, Beskrivelse, Utgivelsesår, Lanseringdato, Type, Lengde) " +
                    "VALUES ((?), (?), (?), (?), (?), 'F', (?))", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, productionCompany);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setInt(4, releaseYear);
            statement.setDate(5, Date.valueOf(releaseDate));
            statement.setInt(6, length);
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public void insertCategoryForProduction(Integer productionID, Integer categoryID) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO Produksjonskategori VALUES ((?), (?))");
            statement.setInt(1, productionID);
            statement.setInt(2, categoryID);
            statement.execute();

        } catch (Exception ignored) {
        }

    }

    public void insertPlatformForProduction(Integer productionID, Integer platformID) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO Produksjonsplattform VALUES ((?), (?))");
            statement.setInt(1, productionID);
            statement.setInt(2, platformID);
            statement.execute();

        } catch (Exception ignored) {
        }

    }

    public HashMap<Integer, String> getPersons() {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("SELECT PersonID, Navn FROM Person");

            ResultSet rs = statement.executeQuery();

            HashMap<Integer, String> persons = new HashMap<>();

            while (rs.next()) {
                int id  = rs.getInt("PersonID");
                String name = rs.getString("Navn");
                persons.put(id, name);
            }

            return persons;

        } catch (Exception e) {
            return null;
        }

    }

    public Integer insertPerson(String personName, Integer personBirthYear, String personBirthCountry) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO Person (Navn, Fødselsår, Fødselsland) VALUES ( (?), (?), (?) )", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, personName);
            statement.setInt(2, personBirthYear);
            statement.setString(3, personBirthCountry);
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public void insertDirectorForProduction(Integer productionID, Integer personID) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO RegissørAvFilm VALUES ((?), (?))");
            statement.setInt(1, personID);
            statement.setInt(2, productionID);
            statement.execute();

        } catch (Exception ignored) {
        }
    }

    public void insertActorForProduction(Integer productionID, Integer personID, String role) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO SkuespillerIFilm VALUES ((?), (?), (?))");
            statement.setInt(1, personID);
            statement.setInt(2, productionID);
            statement.setString(3, role);
            statement.execute();

        } catch (Exception ignored) {
        }
    }

    public void insertWriterForProduction(Integer productionID, Integer personID) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO ManusforfatterAvFilm VALUES ((?), (?))");
            statement.setInt(1, personID);
            statement.setInt(2, productionID);
            statement.execute();

        } catch (Exception ignored) {
        }
    }

    public HashMap<Integer, String> getMusic() {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("SELECT MusikkID, Tittel FROM Musikk");

            ResultSet rs = statement.executeQuery();

            HashMap<Integer, String> music = new HashMap<>();

            while (rs.next()) {
                int id  = rs.getInt("MusikkID");
                String title = rs.getString("Tittel");
                music.put(id, title);
            }

            return music;

        } catch (Exception e) {
            return null;
        }

    }

    public Integer insertMusic(String title, String composer, String performedBy) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO Musikk (Tittel, Komponist, FremførtAv) VALUES ( (?), (?), (?) )", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, title);
            statement.setString(2, composer);
            statement.setString(3, performedBy);
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public void insertMusicForProduction(Integer musicID, Integer productionID) {

        PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("INSERT INTO MusikkIFilm VALUES ((?), (?))");
            statement.setInt(1, musicID);
            statement.setInt(2, productionID);
            statement.execute();

        } catch (Exception ignored) {
        }

    }
    
    
    public void printSeasons() {
    	
    	PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("select Produksjon.Tittel as 'navn', Produksjon.ProduksjonID as 'id'\n" + 
            		"from Produksjon\n" + 
            		"where Produksjon.type = 'S';");
            
            ResultSet rs = statement.executeQuery();
            
            
            while (rs.next()) {
                String name = rs.getString("navn");
                int id = rs.getInt("id");
                System.out.println(name + " | " + id);
            }
            
            
        }catch (Exception e) {
			System.out.println(e);
		}
    	
    }
    
    public void printEpisodes(Integer produksjonsID) {
    	
    	PreparedStatement statement;

        try {
            connect();

            statement = conn.prepareStatement("select Episode.Episodenavn as epName, Episode.EpisodeNr as epNr, Episode.SesongNr as sesNr\n" + 
            		"from Episode\n" + 
            		"where Episode.ProduksjonID = (?);");
            
            statement.setInt(1, produksjonsID);
            
            ResultSet rs = statement.executeQuery();
            
            
            while (rs.next()) {
                String epName = rs.getString("epName");
                int epNr = rs.getInt("epNr");
                int sesNr = rs.getInt("sesNr");
                System.out.println(epName + " | " + epNr + " | " + sesNr);
                
            }
            
            
        }catch (Exception e) {
			System.out.println(e);
		}
    	
    	
    }
    
    
    public Integer insertReview(Integer userID, Integer rating, String anmeldelse) {
    	PreparedStatement statement;
    	
    	try {
			connect();
			
			statement = conn.prepareStatement("insert into Anmeldelse(brukerID, Rating, Kommentar) values ((?), (?), (?));", Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, userID);
			statement.setInt(2, rating);
			statement.setString(3, anmeldelse);
			statement.execute();
			
			ResultSet rs = statement.getGeneratedKeys();
			
			while(rs.next()) {
				return rs.getInt(1);
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
    	
    }
    
    public void insertReviewforEpisode(Integer episodeNr, Integer sesongNr, Integer produkjsonsID, Integer anmeldelseID) {
    	PreparedStatement statement;
    	
    	try {
			connect();
			
			statement = conn.prepareStatement("insert into AnmeldelseForEpisode values ((?), (?), (?), (?));");
			statement.setInt(1, produkjsonsID);
			statement.setInt(2, sesongNr);
			statement.setInt(3, episodeNr);
			statement.setInt(4, anmeldelseID);
			statement.execute();
			
		} catch (Exception e) {
			System.out.println(e);
		}
    	
    	
    }
    
    
    
    
    
    
    
    
    
    
}
