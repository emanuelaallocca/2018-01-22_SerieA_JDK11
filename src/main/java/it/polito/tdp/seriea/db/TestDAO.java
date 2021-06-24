package it.polito.tdp.seriea.db;

import java.util.List;

import it.polito.tdp.seriea.model.Punteggi;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class TestDAO {

	public static void main(String[] args) {
		SerieADAO dao = new SerieADAO();

		List<Season> seasons = dao.listAllSeasons();
		System.out.println(seasons);
		System.out.println("Seasons # rows: " + seasons.size());

		List<Team> teams = dao.getlistTeams();
		System.out.println(teams);
		System.out.println("Teams # rows: " + teams.size());
		
		Team t = new Team("Juventus");
		List <Punteggi> p = dao.getPunteggiStagioni(t);
		System.out.println(p);
	}

}
