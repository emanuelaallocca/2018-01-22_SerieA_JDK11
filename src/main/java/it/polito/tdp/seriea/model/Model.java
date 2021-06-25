package it.polito.tdp.seriea.model;

import it.polito.tdp.seriea.db.SerieADAO;


import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class Model {
	
	private Graph <Integer, DefaultWeightedEdge> grafo;
	
	private SerieADAO dao;
	private List <Punteggi> p;
	
	public Model() {
		dao = new SerieADAO();
	}
	
	public List<Team> getListTeams() {
		return dao.getlistTeams();
	}

	public List<Punteggi> getPunteggi(Team t){
		return dao.getPunteggiStagioni(t);
	}
	
	public void creaGrafo(Team t) {
		p = new ArrayList<>(dao.getPunteggiStagioni(t));
		
		grafo = new SimpleDirectedWeightedGraph(DefaultWeightedEdge.class);
		
		for (Punteggi pp : p)
			this.grafo.addVertex(pp.getStagione());
		
		for (int i=0; i<p.size(); i++) {
			for (int j =0; j<p.size(); j++) {
				
				if(p.get(i).getStagione()!=(p.get(j).getStagione())) {
					
				if (p.get(i).getPunteggi()<p.get(j).getPunteggi()) {
					
					int diff = p.get(j).getPunteggi()-p.get(i).getPunteggi();
					Graphs.addEdgeWithVertices(this.grafo, p.get(i).getStagione(), p.get(j).getStagione(), (double) diff);
					
				}
				else if (p.get(i).getPunteggi()>p.get(j).getPunteggi()){
					
					int diff = p.get(i).getPunteggi()-p.get(j).getPunteggi();
					Graphs.addEdgeWithVertices(this.grafo, p.get(j).getStagione(), p.get(i).getStagione(), (double) diff);
					
				}
			 }
			}
		}
	}
	
	public Punteggi getAnnataOro(Team t) {
		Punteggi migliore = null;
		
		double diffMax=0.0;
		
		for (Integer i : this.grafo.vertexSet()) {
			
			List <Integer> in = Graphs.predecessorListOf(this.grafo, i);
			List <Integer> out = Graphs.successorListOf(this.grafo, i);
			
			double sum =0.0;
			
			for (Integer is : in ) {
				DefaultWeightedEdge e = this.grafo.getEdge(is, i);
				sum = sum + this.grafo.getEdgeWeight(e);
						}
			
			for (Integer it : out ) {
				DefaultWeightedEdge e = this.grafo.getEdge(i, it);
				sum = sum - this.grafo.getEdgeWeight(e);
						}
			
			if (sum>diffMax) {
				migliore = new Punteggi(t, i, (int) sum);
			}
		}
		return migliore;
		
	}
	List<Integer> parziale;
	List<Integer> soluzione;
	double pesoSol;
	
	public List<Integer> getPercorso(Team t){
		parziale = new ArrayList<Integer>();
		soluzione = new ArrayList<Integer>();
		pesoSol = 0;
	
		//parziale.add(p.get(0).getStagione());
		cerca(parziale, 0);
		return soluzione;
	}
	
	private void cerca(List<Integer> parziale, int livello) {
		double peso = calcolaPeso(parziale);
		
		//condizione di terminazione
		// --> livello fine della lista delle stagioni 
		//sono consecutive!!
		if (livello == p.size()) {
			return;
		}
		//ho soluzione migliore?
		if(peso > pesoSol) {
			soluzione = new ArrayList<Integer>(parziale);
			pesoSol = peso;
		}
		
		//trovare percorso
		parziale.add(p.get(livello).getStagione());
		List <Integer> out = Graphs.successorListOf(this.grafo, parziale.get(livello));
		
		for (Integer it : out ) {
			if((livello+1)<p.size()) {
			if(it == p.get(livello+1).getStagione()) {
		       //parziale.add(p.get(livello).getStagione());
		          cerca(parziale, livello+1);
		            parziale.remove(parziale.size()-1);
			 }
			}
			else {
				return;
			}
		}
		
		parziale.remove(parziale.size()-1);
		cerca(parziale, livello+1);
		
		
	}

	private double calcolaPeso(List<Integer> parziale) {
		double sum = 0.0;

		for (int i=1; i<parziale.size() && parziale.size()>1; i++) {
			
			DefaultWeightedEdge e = this.grafo.getEdge(parziale.get(i-1), parziale.get(i));
			sum = sum + this.grafo.getEdgeWeight(e);
			
		}
		return sum;
	}

	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
}
