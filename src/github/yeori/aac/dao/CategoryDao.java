package github.yeori.aac.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import github.yeori.sheet.Excel;

public class CategoryDao {

	A2cContext ctx;
	
	public CategoryDao ( A2cContext ctx) {
		this.ctx = ctx;
	}
	
	public void clear(Connection con) {
		boolean initConn = con == null;
		con = initConn ? ctx.getConnection() : con;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement("delete from cate");
			int ndel = stmt.executeUpdate();
			// con.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			ctx.release(initConn ? con: null, stmt, rs);
		}
	}
	public void resetCategories() {
		Connection con = ctx.getConnection();
		clear(con);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Object[][] cate = Excel.readSheet(ctx.getInputFilePath(), "category", true);
		try {
			String query = "insert into cate(seq, cate_name) values ( ?, ? )";
			stmt = con.prepareStatement(query);
			for (int ir = 0; ir < cate.length; ir++) {
				int pk = (Integer)cate[ir][0];
				String cateName = (String) cate[ir][1];
				stmt.setInt(1, pk);
				stmt.setString(2, cateName);
				stmt.executeUpdate();
				stmt.clearParameters();
			}
			// con.commit();
		}catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			ctx.release(con, stmt, rs);
		}
	}
}
