package github.yeori.sheet;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import github.yeori.aac.dao.A2cContext;
import github.yeori.aac.dao.Picture;
import github.yeori.aac.dao.WordDao;

public class ImagePathTest {

	@Test
	public void test_read_images() {
		A2cContext ctx = new A2cContext("E:\\tmp\\aac\\demo-img.xlsx");
		WordDao dao = new WordDao(ctx);
		List<Picture> pics = dao.readPictures("main");
		for(Picture each : pics) {
			each.getRowIndex();
			each.getPicteBytes();
			System.out.println(each);
		}
	}
	
	@Test
	public void test_image_save () {
		A2cContext ctx = new A2cContext("E:\\tmp\\aac\\DB_Test.xlsx");
		WordDao dao = new WordDao(ctx);
		dao.resetWord();
		
	}
	

}
