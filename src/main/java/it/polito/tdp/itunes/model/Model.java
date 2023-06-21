package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	ItunesDAO dao ; 
	private SimpleGraph<Track, DefaultEdge> graph;  // SEMPLICE, PESATO, NON ORIENTATO

	private List<Track> allTrack; // VERTICI 

	
	
	public Model() {
		
		this.dao= new ItunesDAO() ;
		this.graph= new SimpleGraph<>(DefaultEdge.class);
		this.allTrack = new ArrayList <>(); 

	}
	
	
		
	
	 public void creaGrafo( int min, int max, Genre gg ) {
		 
		 allTrack= dao.getTrack(min, max, gg)  ; 
	 		

	    	Graphs.addAllVertices(this.graph, allTrack);
	 		System.out.println("vertici GRAFO: " +this.graph.vertexSet().size());
	 		
	 		/* 
	 		 
 Ogni coppia di vertici è collegata da un arco, se e solo se il numero 
 distinto di playlist in cui ciascuno dei brani corrispondenti ai 
 due vertici   ---> è uguale.
	 		 
	 		 */
	 		
	 		
	 		
	 		for(Track s1: allTrack) {
	 			for(Track s2: allTrack) {
	 				
	 				if(s1.getTrackId().compareTo(s2.getTrackId())!=0) { 
	 		
	 					
	 				boolean apposto= elementiComuni(s1,s2); 
	 				if(apposto)  {
 						Graphs.addEdgeWithVertices(this.graph, s1, s2);
 						//this.listPesi.add(peso); //abbastanza inutile, usa i GRAFI
	 				}
	 			}
	 				
	 		  }
	 		}
			System.out.println("\nnumero ARCHI: "+ this.graph.edgeSet().size());
 
	 
	 }	
	//dato una track, prendo il numero di playlist associate ad essa.
	private boolean elementiComuni(Track s1, Track s2) {
		
		int sizePlayList1= dao.getSizePlayList(s1).size();
		int sizePlayList2= dao.getSizePlayList(s2).size();
		
		if(sizePlayList1==sizePlayList2)
			return true; 
				
		return false;
	}
	
	public List<Set<Track>> getComponentiConnesse() {  //return list di componenti connesse
		ConnectivityInspector <Track, DefaultEdge> ci= new ConnectivityInspector<>(this.graph);
		return ci.connectedSets();
	}
	
	
	// in input una list dei vertex di quella comp. connessa
    public int getNumPlayLisDistinct(Set<Track> setTrack) { 
 // trova il num playlist distinte che corrispondono agli archi di tale componente.
		
    List<Integer> temp = new ArrayList <>(); //qui metto id di tutte le playlist DISTINTI.

    	for(Track tt: setTrack) { 
    		for(Integer ii: dao.getSizePlayList(tt)) {  // mi da una list di id playlist
    			if(!temp.contains(ii)) {
    				temp.add(ii); 
    			}
    		}
    	}
    	return temp.size(); 
	}
	

/*****************  RICORSIONE:  ******************/ 
	
	/* 
       calcolare l’insieme costituito dal 
       
       1. maggior possibile numero di brani, appartenenti alla componente connessa più numerosa, 
       
       2. che complessivamente abbia una durata  minore o uguale a dTOT .
     */
    
    
    List <Track> bestPercorso; 
    int bestSize; 
   //  double countDuratams; 
    
	public TrackTime getPercorso ( int durataTotMSec) { // input in ms  ...
		
		List <Track> parziale = new ArrayList<>() ; 
		Set<Track> compConnessa= this.getComponentiConnessaMax(); 
		
		this.bestPercorso= new ArrayList<>() ; 
		this.bestSize = 0; 
		
		
		Track tMin= trackMin(compConnessa); 
		
		parziale.add(tMin);
		int countDuratams = tMin.getMilliseconds(); 
		ricorsione(parziale,compConnessa, durataTotMSec, countDuratams ); 
		
		
		
		TrackTime trt= new TrackTime(bestPercorso,countDuratams); 
		
		return trt;
		
	}
    
	private Track trackMin(Set<Track> compConnessa) { 
		int tMin=100000000; //  milli sec
		Track trackMin=null; 
		
		for(Track tt: compConnessa) {
			if(tt.getMilliseconds() < tMin) {
				tMin= tt.getMilliseconds(); 
				trackMin= tt; 
				
			}
			
		}
		return trackMin;
	}




	private void ricorsione(List<Track> parziale, Set<Track> compConnessa, int durataTotMSec, int countDuratams) {
		
		Track current = parziale.get(parziale.size()-1);
		compConnessa.remove(current); 

		/** condizione uscita **/ 
		if(countDuratams > durataTotMSec) {
			return; 
		}
		
		// se non ha sforato, posso settare l'eventuale nuovo best percorso.
		if(parziale.size() > bestSize) {
			bestSize= parziale.size(); 
			bestPercorso= new ArrayList<>(parziale); 
			
		}
		
		
		
	     List<Track> successori= Graphs.successorListOf(graph, current);
		
		/** continuo ad aggiungere elementi in parziale **/ 
		for(Track tt : successori) {
			
			if(compConnessa.contains(tt)) {
				
				parziale.add(tt);
				countDuratams+=tt.getMilliseconds(); 
				ricorsione(parziale, compConnessa,durataTotMSec,countDuratams); 
				
				parziale.remove(tt);
				countDuratams-=tt.getMilliseconds(); 
				// "countDuratams" me lo sto portando dietro in modo correyyo? 
				
				
			}
			
		}
		
	}




	public List<Genre> getGeneres() {
		return dao.getAllGenres();
	}
	public Track getMin(Genre g) {
		return dao.getMinTrack(g);
	}

	public Set<Track> getComponentiConnessaMax() {  //return list di componenti connesse
      
		List<Set<Track>> listOfSet =getComponentiConnesse();
		
		Set<Track> compMax = new HashSet<>();
		int sizeMax=0; 
    	
    	for(Set<Track> setTrack: listOfSet) {
    		if(setTrack.size()>sizeMax) {
    			sizeMax= setTrack.size(); 
    			compMax= new HashSet<>(setTrack);;
    		}
	}
    	
    	System.out.println("\n dimensione componente connessa max "+compMax.size());
		return compMax;

	}
	
	
	
	
	
	
	
	


	
	
	
}
