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
	
	public List<Picture> readPictures(String sheetName){
		List<Picture> pics = Excel.readPics(ctx.getInputFilePath(), sheetName);
		return pics;
	}
	
	public void resetWord() {
		Connection con = ctx.getConnection();
		clear(con);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		List<Picture> pics = readPictures("main");
		Object[][] words = Excel.readSheet(ctx.getInputFilePath(), "main", true);
		String imgRootDir = ctx.getImageDir();
		for( int rowIndex = 1; rowIndex < words.length ; rowIndex ++) {
			String imageName = (String) words[rowIndex][1];
			String w1 = (String)words[rowIndex][2];
			String w2 = (String)words[rowIndex][3];
			String w3 = (String)words[rowIndex][4];
			System.out.printf("%s %s %s %s(%s)", w1, w2, w3,words[rowIndex][5],  words[rowIndex][5].getClass().getName());
			int cateSeq = toInt(words[rowIndex][5]);
			Picture pic = findByLoc(pics, rowIndex, 0);
			pic.saveTo(imgRootDir, imageName);
			
		}
		
		
	}

	private int toInt(Object val) {
		if ( val.getClass() == String.class) {
			String s = (String) val;
			return Integer.parseInt(s.trim());
		} else if ( val.getClass() == Integer.class) {
			return (Integer)val;
		} else {
			throw new RuntimeException("what is this? " + val + " , " + val.getClass().getName());
		}
	}

	Picture findByLoc(List<Picture> pics, int rowIndex, int colIndex) {
		return pics.stream()
				   .filter(p-> 
				   		p.getRowIndex().intValue() == rowIndex 
				   		&& p.getColIndex().intValue() == colIndex)
		           .findFirst().get();
	}
}
