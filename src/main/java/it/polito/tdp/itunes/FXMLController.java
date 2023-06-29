/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import it.polito.tdp.itunes.model.TrackTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPlaylist"
    private Button btnPlaylist; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtDTOT"
    private TextField txtDTOT; // Value injected by FXMLLoader

    @FXML // fx:id="txtMax"
    private TextField txtMax; // Value injected by FXMLLoader

    @FXML // fx:id="txtMin"
    private TextField txtMin; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    
    @FXML
    void doCalcolaPlaylist(ActionEvent event) {

    	int durataTot; 
    	try {
       	 durataTot= Integer.parseInt(this.txtDTOT.getText()); // IN minuti
       	 
       	}catch(NumberFormatException e) {
   		txtResult.setText("Il valore deve essere un  numero");
   		return;
   	}
    	
    	 int duratta= durataTot*60000; 
     	System.out.println("\n duratta: "+duratta);

    	 TrackTime trt= model.getPercorso(duratta);  // ore è in ms
    	 
       	txtResult.appendText("\nPercorso Migliore, che dura un totale di minuti pari a : "+trt.getDurata()/60000+" è : ");
       	txtResult.appendText("\n "+trt.getPercorso());

    	
    	
    	
    	
    }
    
    

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	int minInput; 
    	int maxInput; 
    	
    	Genre gg= this.cmbGenere.getValue();  // da input
    	
    	int minConsentito= (model.getMin(gg).getMilliseconds())/1000;  // verifica min in SECONDI
    	
    	try {
    	 minInput= Integer.parseInt(txtMin.getText()); // IN SECONDI
    	 maxInput= Integer.parseInt(txtMax.getText()); // IN SECONDI
    	
    	}catch(NumberFormatException e) {
		txtResult.setText("Il valore minimo e massimo devono essere dei numeri");
		return;
	}
    	
    	if(minInput<minConsentito) {
    		txtResult.setText("Il valore minimo deve essere maggiore di"+ minConsentito); 
    		return; 
    	}
    	
    	model.creaGrafo(minInput, maxInput, gg);
    	
    	
    	List<Set<Track>> listOfSet =model.getComponentiConnesse();
    	
    	for(Set<Track> setTrack: listOfSet) {
           this.txtResult.appendText("\nComponente connessa con un numero di vertici pari a: " + setTrack.size() );
           this.txtResult.appendText("  --  inseriti in  " + model.getNumPlayLisDistinct(setTrack)+" playlist" );
		}
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPlaylist != null : "fx:id=\"btnPlaylist\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDTOT != null : "fx:id=\"txtDTOT\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMax != null : "fx:id=\"txtMax\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMin != null : "fx:id=\"txtMin\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.setCmbx(); 
    }

	private void setCmbx() {
	  this.cmbGenere.getItems().addAll(model.getGeneres()); 
		
	}

}
