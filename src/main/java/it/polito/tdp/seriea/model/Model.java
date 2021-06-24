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
		List <Punteggi> p = new ArrayList<>(dao.getPunteggiStagioni(t));
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
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
}
