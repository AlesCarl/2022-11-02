package it.polito.tdp.itunes.model;

import java.util.List;

public class TrackTime {
	
	List<Track> percorso; 
	double durata;
	
	public TrackTime(List<Track> percorso, double durata) {
		
		this.percorso = percorso;
		this.durata = durata;
	}
	

	public List<Track> getPercorso() {
		return percorso;
	}

	public void setPercorso(List<Track> percorso) {
		this.percorso = percorso;
	}

	public double getDurata() {
		return durata;
	}

	public void setDurata(double durata) {
		this.durata = durata;
	} 
	

}
