package github.yeori.aac.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class A2cContext {

	/**
	 * 데이터를 읽어들일 excel 파일 경로
	 * input.file.path
	 */
	private String inputFilePath = "E:/tmp/aac/DB_Test.xlsx";
	/**
	 * excel 파일이 저장될 데이터베이스 경로
	 */
	private String dbUrl = "jdbc:sqlite:D:\\db-runtime\\sqlite\\aacdb.db";
	/**
	 * 이미지 폴더 경로
	 */
	private String imageDir = "E:/tmp/aac/img/";
	
	public A2cContext() {}
	
	public A2cContext(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}
	
	public String getInputFilePath() {
		return inputFilePath;
	}
	public String getImageDir() {
		return imageDir;
	}
	
	public Connection getConnection() {
		Connection con;
		try {
			con = DriverManager.getConnection(dbUrl);
			return con ;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void release(Connection con, PreparedStatement stmt, ResultSet rs) {
		if ( con!= null) {
			try { con.close();}catch(SQLException e) {}
		}
		if ( stmt!= null) {
			try { stmt.close();}catch(SQLException e) {}
		}
		if ( rs!= null) {
			try { rs.close();}catch(SQLException e) {}
		}
	}
}
