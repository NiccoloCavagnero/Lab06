package it.polito.tdp.meteo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Citta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {
	
	Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Integer> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		
		txtResult.clear();
		
		txtResult.setText("Sequenza ottima :\n");
		
		List<Citta> best = model.trovaSequenza(boxMese.getValue());
		
		for ( Citta c : best ) 
		   txtResult.appendText(c.toString()+" - ");
		
		double costo = model.punteggioSoluzione(best);
		
		txtResult.appendText("\n\nCon costo : "+costo+".");
	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		
		txtResult.clear();
		
		txtResult.setText(model.getUmiditaMedia(boxMese.getValue()));
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
		boxMese.setValue(1);
	}
	
	public void setModel(Model model) {
		this.model = model;
		boxMese.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12);
	}

}
