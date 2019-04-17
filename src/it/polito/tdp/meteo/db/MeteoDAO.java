package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {		
		
		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE localita = ? AND data LIKE ?";	
		
		

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, localita);
			if ( mese < 10 )
				st.setString(2, "2013-0"+mese+"%");
			else
				st.setString(2, "2013-"+mese+"%");
			

			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
    public List<Rilevamento> getAllRilevamentiMese(int mese) {		
		
		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE data BETWEEN ? AND ?";	
		
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			if ( mese < 10 ) {
				st.setString(1, "2013-0"+mese+"-00");
				st.setString(2, "2013-0"+mese+"-15");
			}
			else {
				st.setString(1, "2013-"+mese+"-00");
				st.setString(2, "2013-"+mese+"-15");
			}
			

			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {

		double umiditaMedia = 0.0;
		
		
		final String sql = "SELECT Localita, SUM(umidita) AS somma, COUNT(data) AS giorni FROM situazione WHERE localita = ? AND data LIKE ? GROUP BY localita";	
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			if ( mese < 10 )
				st.setString(2, "2013-0"+mese+"%");
			else
				st.setString(2, "2013-"+mese+"%");
			
			st.setString(1, localita);

			ResultSet rs = st.executeQuery();
			
			double somma = 0.0;
			double giorni = 0.0;

			while (rs.next()) {
				somma = rs.getInt("somma");
				giorni = rs.getInt("giorni");
			}

			conn.close();
			
			umiditaMedia = (somma/giorni);
			return umiditaMedia;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Citta> getAllCitta() {
		String sql = "SELECT DISTINCT localita FROM situazione ORDER BY localita";

		List<Citta> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Citta(res.getString("localita")));
			}

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}