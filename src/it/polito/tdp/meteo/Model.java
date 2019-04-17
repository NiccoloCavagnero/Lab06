package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	protected List<Citta> listaCitta;
	protected List<Citta> best;
	private MeteoDAO meteo;

	public Model() {
		meteo = new MeteoDAO();
		listaCitta = new ArrayList<Citta>(meteo.getAllCitta());
	}

	public String getUmiditaMedia(int mese) {
		
		String risultato = "";
		
		List<String> listaCitta = new ArrayList<String>();
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>(meteo.getAllRilevamenti());
		
		for ( Rilevamento r : rilevamenti ) {
			if ( !listaCitta.contains(r.getLocalita()) )
				listaCitta.add(r.getLocalita());
		}
		
		for ( String citta : listaCitta ) {
			risultato = risultato + citta + " Umidità media : "+meteo.getAvgRilevamentiLocalitaMese(mese, citta)+"\n";
		}

		return risultato;
	}

	public List<Citta> trovaSequenza(int mese) {
		
		List<Citta> parziale = new ArrayList<Citta>();
		this.best = null;
		
		for ( Citta c : listaCitta ) {
			c.setRilevamenti(meteo.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		ricorsione(parziale,0);
	
		return best;
	}
	
	private void ricorsione(List<Citta> parziale, int level) {
		
		if ( level == NUMERO_GIORNI_TOTALI ) {
			
			double costo = punteggioSoluzione(parziale);
			
			if ( best == null || costo < punteggioSoluzione(best) ) {
				best = new ArrayList<Citta>(parziale);
			}
		}
		else {
			
			for ( Citta c : listaCitta ) {
				if ( controllaParziale(parziale,c) ) {
					
					parziale.add(c);
					ricorsione(parziale,level+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
	}

	public Double punteggioSoluzione(List<Citta> soluzioneCandidata) {

		double score = 0.0;
		
		for ( int i=0; i<NUMERO_GIORNI_TOTALI; i++) {
			Citta c = soluzioneCandidata.get(i);
			
			double umidita = c.getRilevamenti().get(i).getUmidita();
			score += umidita;
		}
		
		for ( int i=0;i<NUMERO_GIORNI_TOTALI-1; i++ ) {
			if ( !soluzioneCandidata.get(i).equals(soluzioneCandidata.get(i+1))) {
				score += COST;
			}
		}
		return score;
	}

	private boolean controllaParziale(List<Citta> parziale, Citta candidata) {
		
		int count = 0;
		
		for ( Citta c : parziale )  
				if ( c.equals(candidata) )
					count++;
		if ( count >= NUMERO_GIORNI_CITTA_MAX )
			return false;
		
		
		if ( parziale.size() == 0 )
			return true;
		else if ( parziale.size() == 1 || parziale.size() == 2 ) 
			return parziale.get(parziale.size()-1).equals(candidata);
		else if ( parziale.get(parziale.size()-1).equals(candidata) )
			return true;
		else if ( parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) 
				&& parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-3)) ) 
			return true;
		else 
		    return false;
	}

}
