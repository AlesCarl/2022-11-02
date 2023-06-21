package it.polito.tdp.itunes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Artist;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.MediaType;
import it.polito.tdp.itunes.model.Playlist;
import it.polito.tdp.itunes.model.Track;

public class ItunesDAO {
	
	public List<Album> getAllAlbums(){
		final String sql = "SELECT * FROM Album";
		List<Album> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Album(res.getInt("AlbumId"), res.getString("Title")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Artist> getAllArtists(){
		final String sql = "SELECT * FROM Artist";
		List<Artist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Artist(res.getInt("ArtistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Playlist> getAllPlaylists(){
		final String sql = "SELECT * FROM Playlist";
		List<Playlist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Playlist(res.getInt("PlaylistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Track> getAllTracks(){
		final String sql = "SELECT * FROM Track";
		List<Track> result = new ArrayList<Track>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice")));
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	
	public List<Genre> getAllGenres(){
		
		final String sql = "SELECT distinct * "
				+ "FROM Genre g "
				+ "order by g.`Name` asc ";
		List<Genre> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Genre(res.getInt("GenreId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
public Track getMinTrack( Genre t){ // in secondi
		
		final String sql = "SELECT  t.* "
				+ "FROM genre g, track t  "
				+ "where g.`GenreId`= t.`GenreId` and g.`GenreId`= ? "
				+ "order by t.`Milliseconds` asc ";
		
		Track min = null; 
		List<Track> result = new LinkedList<>();
		
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, t.getGenreId());
			ResultSet res = st.executeQuery();


			while (res.next()) {
				
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice")));
			
			}
			
			
			min= result.get(0); 
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return min;
	}

public List<Track> getTrack( int min, int max, Genre t){ // in secondi,  per i vertici 
	
	final String sql = "SELECT  t.* "
			+ "FROM genre g, track t  "
			+ "where g.`GenreId`= t.`GenreId` and t.`Milliseconds`> (1000*?)  and t.`Milliseconds`< (1000*?) "
			+ "and t.`GenreId`=  ? "
			+ "order by t.`Milliseconds` asc ";
	
	
	List<Track> result = new LinkedList<>();
	
	
	try {
		Connection conn = DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		
		st.setInt(1, min);
		st.setInt(2, max);
		st.setInt(3, t.getGenreId());
		
		ResultSet res = st.executeQuery();


		while (res.next()) {
			
			result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
					res.getString("Composer"), res.getInt("Milliseconds"), 
					res.getInt("Bytes"),res.getDouble("UnitPrice")));
		
		}
		
		
		conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException("SQL Error");
	}
	return  result;
}
	

	
	public List<MediaType> getAllMediaTypes(){
		final String sql = "SELECT * FROM MediaType";
		List<MediaType> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new MediaType(res.getInt("MediaTypeId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<Integer> getSizePlayList(Track s1) {
		
	
			final String sql = "SELECT distinct  pt.`PlaylistId` "
					+ "FROM track t  , playlisttrack pt  "
					+ "where t.`TrackId`= ? and pt.`TrackId`= t.`TrackId`";
			
			List<Integer> result = new LinkedList<>();
			
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1, s1.getTrackId());
				ResultSet res = st.executeQuery();

				while (res.next()) {
					result.add(res.getInt("PlaylistId"));
				}
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("SQL Error");
			}
			return result;
	}

	
	
}
