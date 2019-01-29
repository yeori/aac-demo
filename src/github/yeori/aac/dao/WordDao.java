package github.yeori.aac.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import github.yeori.sheet.Excel;

public class WordDao {

	A2cContext ctx;
	
	public WordDao(A2cContext ctx) {
		this.ctx = ctx;
	}
	
	public void clear(Connection con) {
		boolean initConn = con == null;
		con = initConn ? ctx.getConnection() : con;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement("delete from words");
			int ndel = stmt.executeUpdate();
			// con.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			ctx.release(initConn ? con: null, stmt, rs);
		}
	}
	
	public List<Picture> readPictures(){
		List<Picture> pics = Excel.readPics(ctx.getInputFilePath(), "main");
		return pics;
	}
	
	public void resetWord() {
		Connection con = ctx.getConnection();
		clear(con);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		Object[][] words = Excel.readSheet(ctx.getInputFilePath(), "main", true);
		
		
	}
}
