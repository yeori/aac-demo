package github.yeori.sheet;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import github.yeori.aac.dao.A2cContext;
import github.yeori.aac.dao.CategoryDao;

public class ExcelTest {

//	static A2cContext ctx ;
	CategoryDao dao;
	
//	@BeforeClass
//	public void ready() {
//		ctx = new A2cContext();
//	}
	
	@Before
	public void init() {
		A2cContext ctx = new A2cContext();
		dao = new CategoryDao(ctx);
		
	}
	@Test
	public void test_category() {
		
		Object[][] data = Excel.readSheet("E:/DB_Test.xlsx", "category", true);
		printGrid(data);
	}
	
	@Test
	public void test_words() {
		Object[][] data = Excel.readSheet("E:/DB_Test.xlsx", "main", true);
		printGrid(data);
	}
	
	@Test
	public void resetCategory() {
		dao.resetCategories();
		assertTrue(true);
	}

	@Test
	public void test_dbl_fraction() {
		assertTrue( 2.3 % 1 != 0 );
		assertTrue( 122.0 % 1 == 0.0 );
		assertTrue( 10.02 % 1 != 0.0 );
	}
	void printGrid(Object[][] data) {
		for (int ir = 0; ir < data.length; ir++) {
			System.out.println(Arrays.toString(data[ir]));
		}
	}
}
