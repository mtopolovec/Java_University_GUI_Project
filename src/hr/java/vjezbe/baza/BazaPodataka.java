package hr.java.vjezbe.baza;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.Artikl;
import hr.java.vjezbe.entitet.Automobil;
import hr.java.vjezbe.entitet.Korisnik;
import hr.java.vjezbe.entitet.PoslovniKorisnik;
import hr.java.vjezbe.entitet.PrivatniKorisnik;
import hr.java.vjezbe.entitet.Prodaja;
import hr.java.vjezbe.entitet.Stan;
import hr.java.vjezbe.entitet.Stanje;
import hr.java.vjezbe.entitet.Usluga;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
/**
 * Predstavlja entitet za sve upite vezane za Bazu podataka.
 * 
 * @author Matija Topolovec
 *
 */
public class BazaPodataka {
	
	private static final Logger logger = LoggerFactory.getLogger(BazaPodataka.class);
	
	private static final String DATABASE_FILE = "src/hr/java/vjezbe/baza/config.properties";

	private static Connection spajanjeNaBazu() throws SQLException, IOException {
		
		Properties svojstva = new Properties();
		
		svojstva.load(new FileReader(DATABASE_FILE));
		
		String urlBazePodataka = svojstva.getProperty("bazaPodatakaURL");
		String korisnickoIme = svojstva.getProperty("korisnickoIme");
		String lozinka = svojstva.getProperty("lozinka");
		
		Connection veza = DriverManager.getConnection(urlBazePodataka,korisnickoIme,lozinka);
		
		return veza;
	}
	
	public static List<Automobil> dohvatiAutomobilePremaKriterijima(Automobil auto) throws BazaPodatakaException {
		
		List<Automobil> listaAutomobila = new ArrayList<>();
		
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT distinct artikl.id, naslov, opis, cijena, snaga, stanje.naziv "
			+ "FROM artikl inner join stanje on stanje.id = artikl.idStanje "
			+ "inner join tipArtikla on tipArtikla.id = artikl.idTipArtikla WHERE tipArtikla.naziv = 'Automobil'");
			if (Optional.ofNullable(auto).isEmpty() == false) {
				if (Optional.ofNullable(auto).map(Automobil::getId).isPresent())
					sqlUpit.append(" AND artikl.id = " + auto.getId());
				if (Optional.ofNullable(auto.getNaslov()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(artikl.naslov) LIKE '%" + auto.getNaslov() +"%'");
				if (Optional.ofNullable(auto.getOpis()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(artikl.opis) LIKE '%" + auto.getOpis() + "%'");
				if (Optional.ofNullable(auto).map(Automobil::getCijena).isPresent())
					sqlUpit.append(" AND artikl.cijena LIKE '%" + auto.getCijena() + "%'");
				if (Optional.ofNullable(auto).map(Automobil::getSnagaKS).isPresent())
					sqlUpit.append(" AND artikl.snaga LIKE '%" + auto.getSnagaKS() + "%'");
			}
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String naslov = resultSet.getString("naslov");
				String opis = resultSet.getString("opis");
				BigDecimal cijena = resultSet.getBigDecimal("cijena");
				BigDecimal snaga = resultSet.getBigDecimal("snaga");
				String stanje = resultSet.getString("naziv");
				Automobil newAuto = new Automobil(id, naslov, opis, cijena, snaga,Stanje.valueOf(stanje));
				listaAutomobila.add(newAuto);
			}
		} catch (SQLException | IOException e) {
		String poruka = "Došlo je do pogreške u radu s bazom podataka pri dohvatu automobila";
		logger.error(poruka, e);
		throw new BazaPodatakaException(poruka, e);
		}
		return listaAutomobila;
	}
	
	public static void pohraniNoviAutomobil(Automobil auto) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("insert into artikl(Naslov, Opis, Cijena, Snaga, idStanje, idTipArtikla) "
							+ "values (?, ?, ?, ?, ?, 1);");
			preparedStatement.setString(1, auto.getNaslov());
			preparedStatement.setString(2, auto.getOpis());
			preparedStatement.setBigDecimal(3, auto.getCijena());
			preparedStatement.setBigDecimal(4, auto.getSnagaKS());
			preparedStatement.setLong(5, (auto.getStanje().ordinal() + 1));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri upisu novog automobila";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void izmjeniAutomobil(Automobil auto, Long id) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("UPDATE artikl SET Naslov = ?, Opis = ?, Cijena = ?, Snaga = ?, idStanje = ? "
							+ "WHERE id = " + id.toString() + ";");
			preparedStatement.setString(1, auto.getNaslov());
			preparedStatement.setString(2, auto.getOpis());
			preparedStatement.setBigDecimal(3, auto.getCijena());
			preparedStatement.setBigDecimal(4, auto.getSnagaKS());
			preparedStatement.setLong(5, (auto.getStanje().ordinal() + 1));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri izmjeni automobila";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static List<Usluga> dohvatiUslugePremaKriterijima(Usluga usluga) throws BazaPodatakaException {
		
		List<Usluga> listaUsluga = new ArrayList<>();
		
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT distinct artikl.id, naslov, opis, cijena, stanje.naziv "
			+ "FROM artikl inner join stanje on stanje.id = artikl.idStanje "
			+ "inner join tipArtikla on tipArtikla.id = artikl.idTipArtikla WHERE tipArtikla.naziv = 'Usluga'");
			if (Optional.ofNullable(usluga).isEmpty() == false) {
				if (Optional.ofNullable(usluga).map(Usluga::getId).isPresent())
					sqlUpit.append(" AND artikl.id = " + usluga.getId());
				if (Optional.ofNullable(usluga.getNaslov()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(artikl.naslov) LIKE '%" + usluga.getNaslov() +"%'");
				if (Optional.ofNullable(usluga.getOpis()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(artikl.opis) LIKE '%" + usluga.getOpis() + "%'");
				if (Optional.ofNullable(usluga).map(Usluga::getCijena).isPresent())
					sqlUpit.append(" AND artikl.cijena LIKE '%" + usluga.getCijena() + "%'");
			}
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String naslov = resultSet.getString("naslov");
				String opis = resultSet.getString("opis");
				BigDecimal cijena = resultSet.getBigDecimal("cijena");
				String stanje = resultSet.getString("naziv");
				Usluga newUsluga = new Usluga(id, naslov, opis, cijena,Stanje.valueOf(stanje));
				listaUsluga.add(newUsluga);
			}
		} catch (SQLException | IOException e) {
		String poruka = "Došlo je do pogreške u radu s bazom podataka pri dohvatu usluga";
		logger.error(poruka, e);
		throw new BazaPodatakaException(poruka, e);
		}
		return listaUsluga;
	}
	
	public static void pohraniNovuUslugu(Usluga usluga) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("insert into artikl(Naslov, Opis, Cijena, idStanje, idTipArtikla) "
							+ "values (?, ?, ?, ?, 2);");
			preparedStatement.setString(1, usluga.getNaslov());
			preparedStatement.setString(2, usluga.getOpis());
			preparedStatement.setBigDecimal(3, usluga.getCijena());
			preparedStatement.setLong(4, (usluga.getStanje().ordinal() + 1));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri upisu nove usluge";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void izmjeniUslugu(Usluga usluga, Long id) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("UPDATE artikl SET Naslov = ?, Opis = ?, Cijena= ?, idStanje = ? "
							+ "WHERE id = " + id.toString() + ";");
			preparedStatement.setString(1, usluga.getNaslov());
			preparedStatement.setString(2, usluga.getOpis());
			preparedStatement.setBigDecimal(3, usluga.getCijena());
			preparedStatement.setLong(4, (usluga.getStanje().ordinal() + 1));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri izmjeni usluge";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}	
	}
	
	public static List<Stan> dohvatiStanovePremaKriterijima(Stan stan) throws BazaPodatakaException {
		
		List<Stan> listaStanova = new ArrayList<>();
		
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT distinct artikl.id, naslov, opis, cijena, kvadratura, stanje.naziv "
			+ "FROM artikl inner join stanje on stanje.id = artikl.idStanje "
			+ "inner join tipArtikla on tipArtikla.id = artikl.idTipArtikla WHERE tipArtikla.naziv = 'Stan'");
			if (Optional.ofNullable(stan).isEmpty() == false) {
				if (Optional.ofNullable(stan).map(Stan::getId).isPresent())
					sqlUpit.append(" AND artikl.id = " + stan.getId());
				if (Optional.ofNullable(stan.getNaslov()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(artikl.naslov) LIKE '%" + stan.getNaslov() +"%'");
				if (Optional.ofNullable(stan.getOpis()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(artikl.opis) LIKE '%" + stan.getOpis() + "%'");
				if (Optional.ofNullable(stan).map(Stan::getCijena).isPresent())
					sqlUpit.append(" AND artikl.cijena LIKE '%" + stan.getCijena() + "%'");
				if (Optional.ofNullable(stan).map(Stan::getKvadratura).isPresent())
					sqlUpit.append(" AND artikl.kvadratura LIKE '%" + stan.getKvadratura() + "%'");
			}
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String naslov = resultSet.getString("naslov");
				String opis = resultSet.getString("opis");
				BigDecimal cijena = resultSet.getBigDecimal("cijena");
				Integer kvadratura = resultSet.getInt("kvadratura");
				String stanje = resultSet.getString("naziv");
				Stan newStan = new Stan(id, naslov, opis, cijena, kvadratura,Stanje.valueOf(stanje));
				listaStanova.add(newStan);
			}
		} catch (SQLException | IOException e) {
		String poruka = "Došlo je do pogreške u radu s bazom podataka pri dohvatu stanova";
		logger.error(poruka, e);
		throw new BazaPodatakaException(poruka, e);
		}
		return listaStanova;
	}

	public static void pohraniNoviStan(Stan stan) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("insert into artikl(Naslov, Opis, Cijena, Kvadratura, idStanje, idTipArtikla) "
							+ "values (?, ?, ?, ?, ?, 3);");
			preparedStatement.setString(1, stan.getNaslov());
			preparedStatement.setString(2, stan.getOpis());
			preparedStatement.setBigDecimal(3, stan.getCijena());
			preparedStatement.setInt(4, stan.getKvadratura());
			preparedStatement.setLong(5, (stan.getStanje().ordinal() + 1));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri upisu novog stana";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void izmjeniStan(Stan stan, Long id) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("UPDATE artikl SET Naslov = ?, Opis = ?, Cijena = ?, Kvadratura = ?, idStanje = ? "
							+ "WHERE id = " + id.toString() + ";");
			preparedStatement.setString(1, stan.getNaslov());
			preparedStatement.setString(2, stan.getOpis());
			preparedStatement.setBigDecimal(3, stan.getCijena());
			preparedStatement.setInt(4, stan.getKvadratura());
			preparedStatement.setLong(5, (stan.getStanje().ordinal() + 1));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri izmjeni stana";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static List<PrivatniKorisnik> dohvatiPrivatneKorisnikePremaKriterijima(PrivatniKorisnik korisnik) throws BazaPodatakaException {
		
		List<PrivatniKorisnik> listaKorisnika = new ArrayList<>();
		
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT distinct korisnik.id, ime, prezime, email, telefon "
					+ "FROM korisnik inner join tipKorisnika on tipKorisnika.id = korisnik.idTipKorisnika "
					+ "WHERE tipKorisnika.naziv = 'PrivatniKorisnik'");
			if (Optional.ofNullable(korisnik).isEmpty() == false) {
				if (Optional.ofNullable(korisnik).map(PrivatniKorisnik::getId).isPresent())
					sqlUpit.append(" AND korisnik.id = " + korisnik.getId());
				if (Optional.ofNullable(korisnik.getIme()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(korisnik.ime) LIKE '%" + korisnik.getIme() +"%'");
				if (Optional.ofNullable(korisnik.getPrezime()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(korisnik.prezime) LIKE '%" + korisnik.getPrezime() + "%'");
				if (Optional.ofNullable(korisnik.getEmail()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(korisnik.email) LIKE '%" + korisnik.getEmail() + "%'");
				if (Optional.ofNullable(korisnik.getTelefon()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(korisnik.telefon) LIKE '%" + korisnik.getTelefon() + "%'");
			}
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String email = resultSet.getString("email");
				String telefon = resultSet.getString("telefon");
				PrivatniKorisnik newKorisnik = new PrivatniKorisnik(id, ime, prezime, email, telefon);
				listaKorisnika.add(newKorisnik);
			}
		} catch (SQLException | IOException e) {
		String poruka = "Došlo je do pogreške u radu s bazom podataka pri dohvatu privatnog korisnika";
		logger.error(poruka, e);
		throw new BazaPodatakaException(poruka, e);
		}
		return listaKorisnika;
	}
	
	public static void pohraniNovogPrivatnogKorisnika(PrivatniKorisnik korisnik) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("insert into korisnik(Ime, Prezime, Email, Telefon, idTipKorisnika) "
							+ "values (?, ?, ?, ?, 1);");
			preparedStatement.setString(1, korisnik.getIme());
			preparedStatement.setString(2, korisnik.getPrezime());
			preparedStatement.setString(3, korisnik.getEmail());
			preparedStatement.setString(4, korisnik.getTelefon());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri upisu novog privatnog korisnika";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void izmjeniPrivatnogKorisnika(PrivatniKorisnik korisnik, Long id) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("UPDATE korisnik SET Ime = ?, Prezime = ?, Email = ?, Telefon = ? "
							+ "WHERE id = " + id.toString() + ";");
			preparedStatement.setString(1, korisnik.getIme());
			preparedStatement.setString(2, korisnik.getPrezime());
			preparedStatement.setString(3, korisnik.getEmail());
			preparedStatement.setString(4, korisnik.getTelefon());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri izmjeni privatnog korisnika";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static List<PoslovniKorisnik> dohvatiPoslovneKorisnikePremaKriterijima(PoslovniKorisnik korisnik) throws BazaPodatakaException {
		
		List<PoslovniKorisnik> listaKorisnika = new ArrayList<>();
		
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT distinct korisnik.id, korisnik.naziv, web, email, telefon "
			+ "FROM korisnik inner join tipKorisnika on tipKorisnika.id = korisnik.idTipKorisnika "
			+ "WHERE tipKorisnika.naziv = 'PoslovniKorisnik'");
			if (Optional.ofNullable(korisnik).isEmpty() == false) {
				if (Optional.ofNullable(korisnik).map(PoslovniKorisnik::getId).isPresent())
					sqlUpit.append(" AND korisnik.id = " + korisnik.getId());
				if (Optional.ofNullable(korisnik.getNaziv()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(korisnik.naziv) LIKE '%" + korisnik.getNaziv() +"%'");
				if (Optional.ofNullable(korisnik.getWeb()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(korisnik.web) LIKE '%" + korisnik.getWeb() + "%'");
				if (Optional.ofNullable(korisnik.getEmail()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(korisnik.email) LIKE '%" + korisnik.getEmail() + "%'");
				if (Optional.ofNullable(korisnik.getTelefon()).map(String::isBlank).orElse(true)== false)
					sqlUpit.append(" AND lower(korisnik.telefon) LIKE '%" + korisnik.getTelefon() + "%'");
			}
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String naziv = resultSet.getString("naziv");
				String web = resultSet.getString("web");
				String email = resultSet.getString("email");
				String telefon = resultSet.getString("telefon");
				PoslovniKorisnik newKorisnik = new PoslovniKorisnik(id, naziv, web, email, telefon);
				listaKorisnika.add(newKorisnik);
			}
		} catch (SQLException | IOException e) {
		String poruka = "Došlo je do pogreške u radu s bazom podataka pri dohvatu poslovnog korisnika";
		logger.error(poruka, e);
		throw new BazaPodatakaException(poruka, e);
		}
		return listaKorisnika;
	}
	
	public static void pohraniNovogPoslovnogKorisnika(PoslovniKorisnik korisnik) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("insert into korisnik(Naziv, Web, Email, Telefon, idTipKorisnika) "
							+ "values (?, ?, ?, ?, 2);");
			preparedStatement.setString(1, korisnik.getNaziv());
			preparedStatement.setString(2, korisnik.getWeb());
			preparedStatement.setString(3, korisnik.getEmail());
			preparedStatement.setString(4, korisnik.getTelefon());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri upisu novog poslovnog korisnika";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void izmjeniPoslovnogKorisnika(PoslovniKorisnik korisnik, Long id) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("UPDATE korisnik SET Naziv = ?, Web = ?, Email = ?, Telefon = ? "
							+ "WHERE id = " + id.toString() + ";");
			preparedStatement.setString(1, korisnik.getNaziv());
			preparedStatement.setString(2, korisnik.getWeb());
			preparedStatement.setString(3, korisnik.getEmail());
			preparedStatement.setString(4, korisnik.getTelefon());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri izmjeni poslovnog korisnika";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void izbrisiArtikl(Artikl artikl) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("DELETE FROM artikl WHERE artikl.id = ?;");
			preparedStatement.setLong(1, artikl.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri brisanju artikla " + artikl.getNaslov();
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void izbrisiKorisnika(Korisnik korisnik) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("DELETE FROM korisnik WHERE korisnik.id = ?;");
			preparedStatement.setLong(1, korisnik.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri brisanju " + korisnik.getClass().toString() +  " korisnika." ;
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static List<Artikl> dohvatiSveArtikle() throws BazaPodatakaException {
		List<Artikl> listaArtikala = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder(
			"SELECT distinct artikl.id as idArtikla, naslov, opis, cijena, snaga,\n"+
			"kvadratura, stanje.naziv as stanje, tipArtikla.naziv as tipArtikla\n" +
			"FROM artikl inner join\n" +
			"stanje on stanje.id = artikl.idStanje inner join\n" +
			"tipArtikla on tipArtikla.id = artikl.idTipArtikla;");
			
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			
			while (resultSet.next()) {
				Artikl artikl = null;
				if(resultSet.getString("tipArtikla").equals("Automobil")) {
					artikl = new Automobil(
						resultSet.getLong("idArtikla"),
						resultSet.getString("naslov"),
						resultSet.getString("opis"),
						resultSet.getBigDecimal("cijena"),
						resultSet.getBigDecimal("snaga"),
						Stanje.valueOf(resultSet.getString("stanje"))
						);
					listaArtikala.add(artikl);
				}
				else if(resultSet.getString("tipArtikla").equals("Usluga")) {
					artikl = new Usluga(
						resultSet.getLong("idArtikla"),
						resultSet.getString("naslov"),
						resultSet.getString("opis"),
						resultSet.getBigDecimal("cijena"),
						Stanje.valueOf(resultSet.getString("stanje"))
						);
					listaArtikala.add(artikl);
				}
				else if(resultSet.getString("tipArtikla").equals("Stan")) {
					artikl = new Stan(
					resultSet.getLong("idArtikla"),
					resultSet.getString("naslov"),
					resultSet.getString("opis"),
					resultSet.getBigDecimal("cijena"),
					resultSet.getInt("kvadratura"),
					Stanje.valueOf(resultSet.getString("stanje"))
					);
					listaArtikala.add(artikl);
				}
				
			}
		} catch (SQLException | IOException e) {
		String poruka = "Došlo je do pogreške u radu s bazom podataka pri dohvatu Korisnika.";
		logger.error(poruka, e);
		throw new BazaPodatakaException(poruka, e);
		}
		return listaArtikala;
	}
	
	public static List<Korisnik> dohvatiSveKorisnike() throws BazaPodatakaException {
		List<Korisnik> listaKorisnika = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder(
			"SELECT distinct korisnik.id as idKorisnika, korisnik.naziv, web, email,\n"+
			"telefon, ime, prezime, tipKorisnika.naziv as tipKorisnika\n" +
			"from korisnik inner join\n" +
			"tipKorisnika on tipKorisnika.id = korisnik.idTipKorisnika;");
			
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			
			while (resultSet.next()) {
				Korisnik korisnik = null;
				if(resultSet.getString("tipKorisnika").equals("PrivatniKorisnik")) {
					korisnik = new PrivatniKorisnik(
						resultSet.getLong("idKorisnika"),
						resultSet.getString("ime"),
						resultSet.getString("prezime"),
						resultSet.getString("email"),
						resultSet.getString("telefon")
						);
					listaKorisnika.add(korisnik);
				}
				else if(resultSet.getString("tipKorisnika").equals("PoslovniKorisnik")) {
					korisnik = new PoslovniKorisnik(
						resultSet.getLong("idKorisnika"),
						resultSet.getString("naziv"),
						resultSet.getString("web"),
						resultSet.getString("email"),
						resultSet.getString("telefon")
						);
					listaKorisnika.add(korisnik);
				}
				
			}
		} catch (SQLException | IOException e) {
		String poruka = "Došlo je do pogreške u radu s bazom podataka pri dohvatu Oglasa.";
		logger.error(poruka, e);
		throw new BazaPodatakaException(poruka, e);
		}
		return listaKorisnika;
	}
	
	public static List<Prodaja> dohvatiProdajuPremaKriterijima(Prodaja prodaja) throws BazaPodatakaException {
		List<Prodaja> listaProdaje = new ArrayList<>();
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder(
			"SELECT distinct korisnik.id as idKorisnika, tipKorisnika.naziv as tipKorisnika, \r\n"+
			"korisnik.naziv as nazivKorisnika, web, email, telefon, \r\n" +
			"korisnik.ime, korisnik.prezime, tipArtikla.naziv as tipArtikla,\r\n" +
			"artikl.naslov, artikl.opis, artikl.cijena, artikl.kvadratura,\r\n" +
			"artikl.snaga, stanje.naziv as stanje, prodaja.datumObjave, artikl.id as idArtikla\r\n"+
			"from korisnik inner join \r\n" +
			"tipKorisnika on tipKorisnika.id = korisnik.idTipKorisnika inner join\r\n" +
			"prodaja on prodaja.idKorisnik = korisnik.id inner join\r\n" +
			"artikl on artikl.id = prodaja.idArtikl inner join\r\n" +
			"tipArtikla on tipArtikla.id = artikl.idTipArtikla inner join\r\n" +
			"stanje on stanje.id = artikl.idStanje where 1=1");
			
			if (Optional.ofNullable(prodaja).isEmpty() == false) {
				if (Optional.ofNullable(prodaja.getArtikl()).isPresent())
					sqlUpit.append(" AND prodaja.idArtikl = " +
					prodaja.getArtikl().getId());
				if (Optional.ofNullable(prodaja.getKorisnik()).isPresent())
					sqlUpit.append(" AND prodaja.idKorisnik = " +
					prodaja.getKorisnik().getId());
				if (Optional.ofNullable(prodaja.getDatumObjave()).isPresent()) {
					sqlUpit.append(" AND prodaja.datumObjave = '" +
					prodaja.getDatumObjave().format(DateTimeFormatter.ISO_DATE) + "'");
				}
			}
			
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			
			while (resultSet.next()) {
				Korisnik korisnik = null;
				if(resultSet.getString("tipKorisnika").equals("PrivatniKorisnik")) {
					korisnik = new PrivatniKorisnik(
						resultSet.getLong("idKorisnika"),
						resultSet.getString("ime"),
						resultSet.getString("prezime"),
						resultSet.getString("email"),
						resultSet.getString("telefon")
						);
				}
				else if(resultSet.getString("tipKorisnika").equals("PoslovniKorisnik")) {
					korisnik = new PoslovniKorisnik(
						resultSet.getLong("idKorisnika"),
						resultSet.getString("nazivKorisnika"),
						resultSet.getString("web"),
						resultSet.getString("email"),
						resultSet.getString("telefon")
						);
				}
				Artikl artikl = null;
				if(resultSet.getString("tipArtikla").equals("Automobil")) {
					artikl = new Automobil(
						resultSet.getLong("idArtikla"),
						resultSet.getString("naslov"),
						resultSet.getString("opis"),
						resultSet.getBigDecimal("cijena"),
						resultSet.getBigDecimal("snaga"),
						Stanje.valueOf(resultSet.getString("stanje"))
						);
				}
				else if(resultSet.getString("tipArtikla").equals("Usluga")) {
					artikl = new Usluga(
						resultSet.getLong("idArtikla"),
						resultSet.getString("naslov"),
						resultSet.getString("opis"),
						resultSet.getBigDecimal("cijena"),
						Stanje.valueOf(resultSet.getString("stanje"))
						);
				}
				else if(resultSet.getString("tipArtikla").equals("Stan")) {
					artikl = new Stan(
					resultSet.getLong("idArtikla"),
					resultSet.getString("naslov"),
					resultSet.getString("opis"),
					resultSet.getBigDecimal("cijena"),
					resultSet.getInt("kvadratura"),
					Stanje.valueOf(resultSet.getString("stanje"))
					);
				}
				Prodaja novaProdaja = new Prodaja(null,artikl, korisnik,
					resultSet.getTimestamp("datumObjave").toInstant().atZone(
					ZoneId.systemDefault()).toLocalDate());
				listaProdaje.add(novaProdaja);
			}
		} catch (SQLException | IOException e) {
		String poruka = "Došlo je do pogreške u radu s bazom podataka pri dohvatu Oglasa.";
		logger.error(poruka, e);
		throw new BazaPodatakaException(poruka, e);
		}
		return listaProdaje;
	}
	
	public static void pohraniNoviOglas(Prodaja prodaja) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("insert into prodaja(idKorisnik, idArtikl, datumObjave) "
							+ "values (?, ?, ?);");
			preparedStatement.setLong(1, prodaja.getKorisnik().getId());
			preparedStatement.setLong(2, prodaja.getArtikl().getId());
			preparedStatement.setObject(3, prodaja.getDatumObjave());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri upisu novog automobila";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void izbrisiOglas(Prodaja prodaja) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("DELETE FROM prodaja WHERE idartikl = ? AND idkorisnik = ?;");
			preparedStatement.setLong(1, prodaja.getArtikl().getId());
			preparedStatement.setLong(2, prodaja.getKorisnik().getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka pri brisanju " + prodaja.getArtikl().toString() +  " oglasa." ;
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static Prodaja dohvatiZadnjuProdaju() throws BazaPodatakaException {
		Prodaja prodaja = new Prodaja(null,null,null,null);
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder(
			"SELECT distinct korisnik.id as idKorisnika, tipKorisnika.naziv as tipKorisnika, \r\n"+
			"korisnik.naziv as nazivKorisnika, web, email, telefon, \r\n" +
			"korisnik.ime, korisnik.prezime, tipArtikla.naziv as tipArtikla,\r\n" +
			"artikl.naslov, artikl.opis, artikl.cijena, artikl.kvadratura,\r\n" +
			"artikl.snaga, stanje.naziv as stanje, prodaja.datumObjave, artikl.id as idArtikla\r\n"+
			"from korisnik inner join \r\n" +
			"tipKorisnika on tipKorisnika.id = korisnik.idTipKorisnika inner join\r\n" +
			"prodaja on prodaja.idKorisnik = korisnik.id inner join\r\n" +
			"artikl on artikl.id = prodaja.idArtikl inner join\r\n" +
			"tipArtikla on tipArtikla.id = artikl.idTipArtikla inner join\r\n" +
			"stanje on stanje.id = artikl.idStanje\n" +
			"ORDER BY datumObjave desc\n" + 
			"limit 1");
			
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			
			while (resultSet.next()) {
				Korisnik korisnik = null;
				if(resultSet.getString("tipKorisnika").equals("PrivatniKorisnik")) {
					korisnik = new PrivatniKorisnik(
						resultSet.getLong("idKorisnika"),
						resultSet.getString("ime"),
						resultSet.getString("prezime"),
						resultSet.getString("email"),
						resultSet.getString("telefon")
						);
				}
				else if(resultSet.getString("tipKorisnika").equals("PoslovniKorisnik")) {
					korisnik = new PoslovniKorisnik(
						resultSet.getLong("idKorisnika"),
						resultSet.getString("nazivKorisnika"),
						resultSet.getString("web"),
						resultSet.getString("email"),
						resultSet.getString("telefon")
						);
				}
				Artikl artikl = null;
				if(resultSet.getString("tipArtikla").equals("Automobil")) {
					artikl = new Automobil(
						resultSet.getLong("idArtikla"),
						resultSet.getString("naslov"),
						resultSet.getString("opis"),
						resultSet.getBigDecimal("cijena"),
						resultSet.getBigDecimal("snaga"),
						Stanje.valueOf(resultSet.getString("stanje"))
						);
				}
				else if(resultSet.getString("tipArtikla").equals("Usluga")) {
					artikl = new Usluga(
						resultSet.getLong("idArtikla"),
						resultSet.getString("naslov"),
						resultSet.getString("opis"),
						resultSet.getBigDecimal("cijena"),
						Stanje.valueOf(resultSet.getString("stanje"))
						);
				}
				else if(resultSet.getString("tipArtikla").equals("Stan")) {
					artikl = new Stan(
					resultSet.getLong("idArtikla"),
					resultSet.getString("naslov"),
					resultSet.getString("opis"),
					resultSet.getBigDecimal("cijena"),
					resultSet.getInt("kvadratura"),
					Stanje.valueOf(resultSet.getString("stanje"))
					);
				}
				prodaja.setArtikl(artikl);
				prodaja.setKorisnik(korisnik);
				prodaja.setDatumObjave(resultSet.getTimestamp("datumObjave").toInstant().atZone(
						ZoneId.systemDefault()).toLocalDate());
			}
		} catch (SQLException | IOException e) {
		String poruka = "Došlo je do pogreške u radu s bazom podataka pri dohvatu Oglasa.";
		logger.error(poruka, e);
		throw new BazaPodatakaException(poruka, e);
		}
		return prodaja;
	}
	
	public static Usluga dohvatiZadnjuUslugu() throws BazaPodatakaException {
		Usluga usluga = null;
		try (Connection connection = spajanjeNaBazu()) {
			StringBuilder sqlUpit = new StringBuilder(
			"SELECT distinct artikl.* " +
			"FROM artikl inner join stanje on stanje.id = artikl.idStanje " +
			"inner join tipArtikla on tipArtikla.id = artikl.idTipArtikla " +
			"WHERE tipArtikla.naziv = 'Usluga' " +
			"ORDER BY id DESC " +
			"limit 1");
			
			Statement query = connection.createStatement();
			ResultSet resultSet = query.executeQuery(sqlUpit.toString());
			
			while (resultSet.next()) {
				usluga = new Usluga(
					resultSet.getLong("idTipArtikla"),
					resultSet.getString("naslov"),
					resultSet.getString("opis"),
					resultSet.getBigDecimal("cijena"),
					Stanje.valueOf(resultSet.getString("stanje"))
					);
			}
		} catch (SQLException | IOException e) {
		String poruka = "Došlo je do pogreške u radu s bazom podataka pri dohvatu Oglasa.";
		logger.error(poruka, e);
		throw new BazaPodatakaException(poruka, e);
		}
		return usluga;
	}
	
}
