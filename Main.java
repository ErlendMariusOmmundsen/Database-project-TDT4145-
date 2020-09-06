package databaseprosjekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Controller controller = new Controller();

        System.out.println("\nFILMDATABASE");

        Scanner scanner = new Scanner(System.in);

        program:
        while (true) {
            System.out.println("\n----------------------------------------------------------------------------------------------");
            System.out.println("\nVelg spørringen du vil utføre ved å skrive inn nummeret på spørringen. Skriv 0 for å avslutte.");
            System.out.println("[1] Finn navnet på alle rollene en gitt skuespiller har.");
            System.out.println("[2] Finn hvilke filmer som en gitt skuespiller opptrer i.");
            System.out.println("[3] Finn hvilke filmselskap som lager flest filmer inne hver sjanger.");
            System.out.println("[4] Sett inn en ny film med regissør, manusforfattere, skuespillere og det som hører med.");
            System.out.println("[5] Sett inn ny anmeldelse av en episode av en serie.");

            System.out.print("\nVelg spørring: ");

            int query = Integer.parseInt(scanner.nextLine());

            System.out.println("\n----------------------------------------------------------------------------------------------");

            switch (query) {
                case 0:
                    break program;
                case 1:
                    System.out.println("[1] Finn navnet på alle rollene en gitt skuespiller har.");
                    System.out.println("Skuespiller: ");
                    
                    String actor1 = scanner.nextLine();
                    
                    ArrayList<String> roles = controller.getRoles(actor1);
                    
                    
                    if (roles != null) {
                        if (roles.size() > 0) {
                            System.out.println("Roller som " + actor1 + " har hatt:");
                            for (String role : roles) {
                                System.out.println(role);
                            }
                        } else {
                            System.out.println(actor1 + " har ingen roller.");
                        }
                    }
                    
                    break;

                case 2:

                    System.out.println("\n[2] Finn hvilke filmer som en gitt skuespiller opptrer i.");
                    System.out.print("Skuespiller: ");

                    String actor = scanner.nextLine();
                    ArrayList<String> movies = controller.getMovies(actor);

                    if (movies != null) {
                        if (movies.size() > 0) {
                            System.out.println("Filmer hvor " + actor + " er skuespiller:");
                            for (String movie : movies) {
                                System.out.println(movie);
                            }
                        } else {
                            System.out.println(actor + " er ikke skuespiller i noen av filmene i databasen.");
                        }
                    }

                    break;

                case 3:

                    System.out.println("\n[3] Finn hvilke filmselskap som lager flest filmer inne hver sjanger.");
                    HashMap<String, ArrayList<Object>> map = controller.getMostCommonProductionCompanyPerCategory();

                    if (map != null) {
                        map.forEach((category, info) -> System.out.println(category + " - " + info.get(0) + " (" + info.get(1) + ") "));
                    }

                    break;

                case 4:

                    System.out.println("\n[4] Sett inn en ny film med regissør, manusforfattere, skuespillere og det som hører med.");

                    ArrayList<Integer> selectedCategories = new ArrayList<>();
                    ArrayList<Integer> selectedPlatforms = new ArrayList<>();
                    String selectedProductionCompany = "";
                    ArrayList<Integer> selectedDirectors = new ArrayList<>();
                    HashMap<Integer, String> selectedActors = new HashMap<>();
                    ArrayList<Integer> selectedWriters = new ArrayList<>();
                    ArrayList<Integer> selectedMusic = new ArrayList<>();

                    System.out.println();

                    // Kategorier
                    HashMap<Integer, String> categories = controller.getCategories();

                    if (categories != null) {
                        if (categories.size() > 0) {
                            System.out.println("Velg en eksisterende kategori ved å skrive nummeret på kategorien. Skriv 0 for å gå videre.");
                            categories.forEach((categoryID, categoryName) -> System.out.println(categoryID + " " + categoryName));

                            while (true) {
                                System.out.print("Kategori: ");
                                int category = Integer.parseInt(scanner.nextLine());

                                if (category != 0) {
                                    selectedCategories.add(category);
                                } else {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Ingen eksisterende kategorier i databasen.");
                        }
                    }

                    System.out.println("Lag nye kategorier for filmen. Skriv 0 for å gå videre.");

                    while (true) {
                        System.out.print("Kategorinavn: ");
                        String categoryName = scanner.nextLine();

                        if (! categoryName.equals("0")) {
                            Integer id = controller.insertCategory(categoryName);

                            if (id != null) {
                                selectedCategories.add(id);
                            }
                        } else {
                            break;
                        }
                    }

                    System.out.println();

                    // Plattformer
                    HashMap<Integer, String> platforms = controller.getPlatforms();

                    if (platforms != null) {
                        if (platforms.size() > 0) {
                            System.out.println("Velg en eksisterende plattform ved å skrive nummeret på plattformen. Skriv 0 for å gå videre.");
                            platforms.forEach((platformID, platformName) -> System.out.println(platformID + " " + platformName));

                            while (true) {
                                System.out.print("Plattform: ");
                                int platform = Integer.parseInt(scanner.nextLine());

                                if (platform != 0) {
                                    selectedPlatforms.add(platform);
                                } else {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Ingen eksisterende plattformer i databasen.");
                        }
                    }

                    System.out.println("Lag nye plattformer for filmen. Skriv 0 for å gå videre.");

                    while (true) {
                        System.out.print("Plattform-navn: ");
                        String platformName = scanner.nextLine();

                        if (! platformName.equals("0")) {
                            Integer id = controller.insertPlatform(platformName);

                            if (id != null) {
                                selectedPlatforms.add(id);
                            }
                        } else {
                            break;
                        }
                    }

                    System.out.println();

                    // Produksjonsselskap
                    ArrayList<String> productionCompanies = controller.getProductionCompanies();

                    if (productionCompanies != null) {
                        String productionCompany = null;

                        if (productionCompanies.size() > 0) {
                            System.out.println("Velg et eksisterende produksjonsselskap ved å skrive navnet på selskapet. Skriv 0 for å lage et nytt.");
                            productionCompanies.forEach(System.out::println);

                            System.out.print("Produksjonsselskap: ");
                            productionCompany = scanner.nextLine();
                        } else {
                            System.out.println("Ingen eksisterende produksjonsselskap i databasen.");
                        }

                        if (productionCompany != null && !productionCompany.equals("0")) {
                            selectedProductionCompany = productionCompany;
                        } else {
                            System.out.println("Lag et nytt produksjonsselskap for filmen.");
                            System.out.print("Navn: ");
                            String name = scanner.nextLine();
                            System.out.print("URL: ");
                            String URL = scanner.nextLine();
                            System.out.print("Adresse: ");
                            String address = scanner.nextLine();
                            System.out.print("Land: ");
                            String country = scanner.nextLine();

                            controller.insertProductionCompany(name, URL, address, country);

                            selectedProductionCompany = productionCompany;
                        }
                    }

                    System.out.println();

                    // Produksjon
                    System.out.println("Vennligst fyll inn filmdetaljer.");
                    System.out.print("Tittel: ");
                    String title = scanner.nextLine();
                    System.out.print("Beskrivelse: ");
                    String description = scanner.nextLine();
                    System.out.print("Utgivelsesår: ");
                    Integer releaseYear = Integer.parseInt(scanner.nextLine());
                    System.out.print("Lanseringsdato (yyyy-[m]m-[d]d): ");
                    String releaseDate = scanner.nextLine();
                    System.out.print("Lengde i minutter: ");
                    Integer length = Integer.parseInt(scanner.nextLine());

                    Integer productionID = controller.insertProduction(selectedProductionCompany, title, description, releaseYear, releaseDate, length);

                    for (Integer categoryID : selectedCategories) {
                        controller.insertCategoryForProduction(productionID, categoryID);
                    }

                    for (Integer platformID : selectedPlatforms) {
                        controller.insertPlatformForProduction(productionID, platformID);
                    }

                    System.out.println();

                    // Regissør
                    HashMap<Integer, String> persons = controller.getPersons();

                    if (persons != null) {
                        if (persons.size() > 0) {
                            System.out.println("Velg en eksisterende person som regissør ved å skrive nummeret til personen. Skriv 0 for å gå videre.");
                            persons.forEach((personID, name) -> System.out.println(personID + " " + name));

                            while (true) {
                                System.out.print("Regissør: ");
                                int director = Integer.parseInt(scanner.nextLine());

                                if (director != 0) {
                                    selectedDirectors.add(director);
                                } else {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Ingen eksisterende personer i databasen.");
                        }
                    }

                    System.out.println("Lag nye personer som regissører for filmen. Skriv 0 for å gå videre.");

                    while (true) {
                        System.out.print("Navn: ");
                        String personName = scanner.nextLine();

                        if (personName.equals("0")) {
                            break;
                        }

                        System.out.print("Fødselsår: ");
                        int personBirthYear = Integer.parseInt(scanner.nextLine());

                        if (personBirthYear == 0) {
                            break;
                        }

                        System.out.println("Fødselsland: ");
                        String personBirthCountry = scanner.nextLine();

                        if (personBirthCountry.equals("0")) {
                            break;
                        }

                        Integer personID = controller.insertPerson(personName, personBirthYear, personBirthCountry);

                        if (personID != null) {
                            selectedDirectors.add(personID);
                        }

                    }

                    for (Integer personID : selectedDirectors) {
                        controller.insertDirectorForProduction(productionID, personID);
                    }

                    System.out.println();


                    // Skuespillere
                    persons = controller.getPersons();

                    if (persons != null) {
                        if (persons.size() > 0) {
                            System.out.println("Velg en eksisterende person som skuespiller ved å skrive nummeret til personen. Skriv 0 for å gå videre.");
                            persons.forEach((personID, name) -> System.out.println(personID + " " + name));

                            while (true) {
                                System.out.print("Skuespiller: ");
                                int actorID = Integer.parseInt(scanner.nextLine());

                                if (actorID == 0) {
                                    break;
                                }

                                System.out.print("Rolle: ");
                                String role = scanner.nextLine();

                                if (!role.equals("0")) {
                                    selectedActors.put(actorID, role);
                                } else {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Ingen eksisterende personer i databasen.");
                        }
                    }

                    System.out.println("Lag nye personer som skuespillere for filmen. Skriv 0 for å gå videre.");

                    while (true) {
                        System.out.print("Navn: ");
                        String personName = scanner.nextLine();

                        if (personName.equals("0")) {
                            break;
                        }

                        System.out.print("Fødselsår: ");
                        int personBirthYear = Integer.parseInt(scanner.nextLine());

                        if (personBirthYear == 0) {
                            break;
                        }

                        System.out.println("Fødselsland: ");
                        String personBirthCountry = scanner.nextLine();

                        if (personBirthCountry.equals("0")) {
                            break;
                        }

                        Integer personID = controller.insertPerson(personName, personBirthYear, personBirthCountry);

                        System.out.print("Rolle: ");
                        String role = scanner.nextLine();

                        if (role.equals("0")) {
                            break;
                        }

                        if (personID != null ) {
                            selectedActors.put(personID, role);
                        }

                    }

                    selectedActors.forEach((personID, role) -> controller.insertActorForProduction(productionID, personID, role));

                    System.out.println();


                    // Manusforfatter
                    persons = controller.getPersons();

                    if (persons != null) {
                        if (persons.size() > 0) {
                            System.out.println("Velg en eksisterende person som manusforfatter ved å skrive nummeret til personen. Skriv 0 for å gå videre.");
                            persons.forEach((personID, name) -> System.out.println(personID + " " + name));

                            while (true) {
                                System.out.print("Manusforfatter: ");
                                int writer = Integer.parseInt(scanner.nextLine());

                                if (writer != 0) {
                                    selectedWriters.add(writer);
                                } else {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Ingen eksisterende personer i databasen.");
                        }
                    }

                    System.out.println("Lag nye personer som manusforfattere for filmen. Skriv 0 for å gå videre.");

                    while (true) {
                        System.out.print("Navn: ");
                        String personName = scanner.nextLine();

                        if (personName.equals("0")) {
                            break;
                        }

                        System.out.print("Fødselsår: ");
                        int personBirthYear = Integer.parseInt(scanner.nextLine());

                        if (personBirthYear == 0) {
                            break;
                        }

                        System.out.print("Fødselsland: ");
                        String personBirthCountry = scanner.nextLine();

                        if (personBirthCountry.equals("0")) {
                            break;
                        }

                        Integer personID = controller.insertPerson(personName, personBirthYear, personBirthCountry);

                        if (personID != null) {
                            selectedWriters.add(personID);
                        }

                    }

                    for (Integer personID : selectedWriters) {
                        controller.insertWriterForProduction(productionID, personID);
                    }

                    System.out.println();

                    // Musikk
                    HashMap<Integer, String> music = controller.getMusic();

                    if (music != null) {
                        if (music.size() > 0) {
                            System.out.println("Velg eksisterende musikk ved å skrive nummeret på musikken. Skriv 0 for å gå videre.");
                            music.forEach((musicID, musicTitle) -> System.out.println(musicID + " " + musicTitle));

                            while (true) {
                                System.out.print("Musikk: ");
                                int musicID = Integer.parseInt(scanner.nextLine());

                                if (musicID != 0) {
                                    selectedMusic.add(musicID);
                                } else {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Ingen eksisterende musikk i databasen.");
                        }
                    }

                    System.out.println("Lag nye innslag med musikk for filmen. Skriv 0 for å gå videre.");

                    while (true) {
                        System.out.print("Tittel: ");
                        String musicTitle = scanner.nextLine();

                        if (musicTitle.equals("0")) {
                            break;
                        }

                        System.out.print("Komponist: ");
                        String musicComposer = scanner.nextLine();

                        if (musicComposer.equals("0")) {
                            break;
                        }

                        System.out.print("Fremført av: ");
                        String musicPerformedBy = scanner.nextLine();

                        if (musicPerformedBy.equals("0")) {
                            break;
                        }

                        Integer musicID = controller.insertMusic(musicTitle, musicComposer, musicPerformedBy);

                        if (musicID != null) {
                            selectedMusic.add(musicID);
                        }

                    }

                    for (Integer musicID : selectedMusic) {
                        controller.insertMusicForProduction(musicID, productionID);
                    }

                    System.out.println();

                    // Sammendrag
                    System.out.println("Filmen ble lagt til i databasen.");
                    System.out.println("\nSAMMENDRAG:");
                    System.out.println("---------");

                    categories = controller.getCategories();
                    System.out.println("Kategorier:");
                    for (Integer categoryID : selectedCategories) {
                        System.out.println("\t" + categories.get(categoryID));
                    }

                    platforms = controller.getPlatforms();
                    System.out.println("Plattformer:");
                    for (Integer platformID : selectedPlatforms) {
                        System.out.println("\t" + platforms.get(platformID));
                    }

                    System.out.println("Produksjonsselskap: " + selectedProductionCompany);
                    System.out.println("Tittel: " + title);
                    System.out.println("Beskrivelse: " + description);
                    System.out.println("Utgivelsesår: " + releaseYear);
                    System.out.println("Lanseringsdato: " + releaseDate);
                    System.out.println("Lengde: " + length);

                    persons = controller.getPersons();

                    System.out.println("Regissører:");
                    for (Integer personID : selectedDirectors) {
                        System.out.println("\t" + persons.get(personID));
                    }

                    System.out.println("Skuespillere:");
                    for (Map.Entry<Integer, String> entry : selectedActors.entrySet()) {
                        Integer person = entry.getKey();
                        String role= entry.getValue();
                        System.out.println("\t" + persons.get(person) + " (" + role + ")");
                    }

                    System.out.println("Manusforfattere:");
                    for (Integer personID : selectedWriters) {
                        System.out.println("\t" + persons.get(personID));
                    }

                    music = controller.getMusic();
                    System.out.println("Musikk:");
                    for (Integer musicID : selectedMusic) {
                        System.out.println("\t" + music.get(musicID));
                    }

                    break;
                    
                case 5:
                    System.out.println("[5] Sett inn ny anmeldelse av en episode av en serie.");
                    
                    System.out.println("Skriv inn brukerID");
                    Integer userID = Integer.parseInt(scanner.nextLine());

                    
                    
                    controller.printSeasons();
                    System.out.println("Velg produksjonsID:");
                    
                    Integer produksjonsID = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("Navn | Episodenummer | Sesongnummer");
                    controller.printEpisodes(produksjonsID);
                    
                    System.out.println("Velg Episode: ");
                    Integer episodeNr = Integer.parseInt(scanner.nextLine());
                    System.out.println("Velg SesongNr");
                    Integer sesNr = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("Skriv anmeldelse:");
                    String anmeldelse = scanner.nextLine();
                    
                    System.out.println("Skriv rating(1-10):");
                    Integer rating = Integer.parseInt(scanner.nextLine());
                    
                    int anmeldelseID = controller.insertReview(userID, rating, anmeldelse);
                    controller.insertReviewforEpisode(episodeNr, sesNr, produksjonsID, anmeldelseID);
                    
                	
                	break;
            }
        }
    }
}
