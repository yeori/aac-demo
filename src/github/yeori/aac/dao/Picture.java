package github.yeori.aac.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFPicture;

public class Picture {
	/*
	 * excel에 등록된 이미지들은 특정 셀을 기준으로 등록됨
	 * cell 좌표 정보를 이용해서 row마다 등록된 이미지들을 가져올 수 있음
	 */
	Integer rowIndex;
	Integer colIndex;
	String pictureName;
	byte [] picteBytes;
	
	XSSFPicture target;
	
	public Picture(XSSFPicture pic) {
		target = pic;
	}
	private void readProps() {
		XSSFClientAnchor anchor = target.getClientAnchor();
		rowIndex = anchor.getRow1();
		colIndex = (int)anchor.getCol1();
		pictureName = target.getShapeName();
	}

	public Integer getRowIndex() {
		if ( rowIndex == null) {
			readProps();
		}
		return rowIndex;
	}


	public Integer getColIndex() {
		if ( rowIndex == null) {
			readProps();
		}
		return colIndex;
	}

	public String getPictureName() {
		if ( rowIndex == null) {
			readProps();
		}
		return pictureName;
	}

	public byte[] getPicteBytes() {
		if(picteBytes == null) {
			readBytes();
		}
		return picteBytes;
	}

	private void readBytes() {
		this.picteBytes = target.getPictureData().getData();
	}
	public XSSFPicture getTarget() {
		return target;
	}
	@Override
	public String toString() {
		return "Picture [(" + rowIndex + ", " + colIndex + ") " + pictureName
				+ ", " + (picteBytes == null? "unknown" : picteBytes.length + "bytes") +   "]";
	}
	public void saveTo(String dir, String pictureName) {
		File imgFile = new File(dir, pictureName);
		if ( !imgFile.exists()) {
			try {
				imgFile.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException("fail to create picture file: " + pictureName + " to " + imgFile.getAbsolutePath(), e);
			}
		}
		try {
			Files.write(
					imgFile.toPath(), 
					this.picteBytes, 
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("fail to save picture: " + pictureName + " to " + imgFile.getAbsolutePath(), e);
		}
		
	}
	
	
}
