package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.seriea.model.Punteggi;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////7
	public List<Punteggi> getPunteggiStagioni(Team t) {
		String sql = "SELECT m.Season, COUNT(*)*3 AS peso "
				+"FROM matches m, teams t1 "
				+"WHERE ((m.HomeTeam = t1.team AND m.FTR = 'H') " 
				+"OR (m.AwayTeam = t1.team AND m.FTR = 'A')) "
				+"AND t1.team = ? "
				+"GROUP BY m.Season";
	
		List<Punteggi> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Punteggi p = new Punteggi (t, res.getInt("peso"), res.getInt("m.Season"));
				result.add(p);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String sql2 = "SELECT m.Season, COUNT(*) AS peso "
				+"FROM matches m, teams t1 "
				+"WHERE (m.HomeTeam = t1.team "
				+"OR m.AwayTeam =t1.team) "
				+"AND t1.team = ? "
				+"AND m.FTR = 'D' "
				+"GROUP BY m.Season";

		try {
			PreparedStatement st = conn.prepareStatement(sql2);
			st.setString(1, t.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				int stagione = res.getInt("m.Season");
				
				for (Punteggi pp : result) {
					if (pp.getStagione() == stagione) {
						int punto = pp.getPunteggi();
				        pp.setPunteggi(punto+res.getInt("peso"));
					}
				}
				Punteggi p = new Punteggi (t, res.getInt("peso"), res.getInt("m.Season"));
				result.add(p);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<Team> getlistTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}

