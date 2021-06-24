package it.polito.tdp.seriea.model;

public class Punteggi {
	Team t;
	int punteggi;
	int stagione;
	public Punteggi(Team t, int punteggi, int stagione) {
		super();
		this.t = t;
		this.punteggi = punteggi;
		this.stagione = stagione;
	}
	public Team getT() {
		return t;
	}
	public void setT(Team t) {
		this.t = t;
	}
	public int getPunteggi() {
		return punteggi;
	}
	public void setPunteggi(int punteggi) {
		this.punteggi = punteggi;
	}
	public int getStagione() {
		return stagione;
	}
	public void setStagione(int stagione) {
		this.stagione = stagione;
	}
	@Override
	public String toString() {
		return "Punteggi [t=" + t + ", punteggi=" + punteggi + ", stagione=" + stagione + "]";
	}
	
	

}
