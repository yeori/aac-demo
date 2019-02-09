package github.yeori.aac.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
		List<Cate> categories = buildCategories(cate);
		try {
			String query = "insert into cate(seq, cate_name, lvl, parent_cate) values ( ?, ?, ?, ? )";
			stmt = con.prepareStatement(query);
			for (int ir = 0; ir < categories.size(); ir++) {
				Cate c = categories.get(ir);
				/*
				int pk = (Integer)cate[ir][0];
				String cateName = (String) cate[ir][1];
				*/
				stmt.setInt(1, c.seq.intValue());
				stmt.setString(2, c.cateName);
				stmt.setInt(3, c.level);
				if ( c.isRoot() ) {
					stmt.setNull(4, Types.INTEGER);
				} else {
					stmt.setInt(4, c.parent.seq);
				}
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

	List<Cate> buildCategories(Object[][] data) {
		Stack<Cate> stack = new Stack<>();
		List<Cate> categories = new ArrayList<>();
		int ir = 1; // skip labels at index 0
		for (; ir < data.length; ir++) {
			Object[] row = data[ir]; // [SEQ, LVL0, LVL1, LVL2]
			if(Util.stringsOf(row, "NULL")) {
				break;
			}
			Cate c = Cate.convert(row);
			if(c.isRoot()) {
				stack.clear();
				stack.push(c);
				categories.add(c);
				continue;
			}
			
			Cate top = stack.peek();
			if( c.level == top.level ){
				c.parent = top.parent;
			} else if( c.level > top.level ){
				c.parent = top;
				stack.push(c);
			} else {
				stack.pop();
				top = stack.peek();
				c.parent = top.parent;
				stack.push(c);
			}
			categories.add(c);
		}
		return categories;
	}
	


	static class Cate{
		Integer seq;
		String cateName;
		Cate parent;
		int level;
		/**
		 * root category with level:=0, no parent category
		 * @param seq
		 * @param cateName
		 */
		public Cate(Integer seq, String cateName) {
			super();
			this.seq = seq;
			this.cateName = cateName;
			this.level = 0;
		}
		public static Cate convert(Object[] data) {
			// [SEQ, LVL0, LVL1, LVL2]
			int seq = Util.toInt(data[0]);
			String name = null;
			int level = 0;
			if(data[1] != null) {
				level = 0;
			} else if(data[2] != null) {
				level = 1;
			} else if(data[3] != null) {
				level = 2;
			} else {
				throw new RuntimeException("category 이름이 존재하지 않습니다.SEQ : " + seq + ", LVL0, LVL1, LVL2 중 하나가 있어야 함");
			}
			name = Util.strictStr(data[level+1]);
			
			return new Cate(seq, name, level);
		}
		/**
		 * create sub category from the given parent cate
		 * @param seq
		 * @param cateName
		 * @param parent
		 */
		public Cate(int seq, String cateName, int level) {
			super();
			this.seq = seq;
			this.cateName = cateName;
			this.level = level;
		}
		
		boolean isRoot() {
			return this.level == 0;
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			Cate c = this;
			while ( c != null) {
				sb.insert(0, " > " + c.cateName);
				c = c.parent;
			}
			return sb.toString();
		}
		
		
		
	}
}
