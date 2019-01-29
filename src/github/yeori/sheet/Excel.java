package github.yeori.sheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;

import github.yeori.aac.dao.Picture;

public class Excel {

	/**
	 * 
	 * @param absfilePath excel 파일 경로
	 * @param sheetName 데이터를 읽어올 sheet name
	 * @param skipBlankRow true이면 값이 아무것도 없는 empty row를 제외합니다.
	 * @return
	 */
	public static Object[][] readSheet(String absfilePath, String sheetName, boolean skipBlankRow ) {
		Workbook wbook = null;
		try {
			List<Object[]> data = new ArrayList<>();
			wbook = WorkbookFactory.create(new File(absfilePath));
			Sheet sheet = wbook.getSheet(sheetName);
			
			int limit = sheet.getPhysicalNumberOfRows();
			for(int ir = sheet.getFirstRowNum() ; ir < limit ; ir++) {
				Row row = sheet.getRow(ir);
				int ic = row.getFirstCellNum();
				int colSize = row.getLastCellNum();
				
				if ( ic == -1 && colSize == -1 ) {
					// data.add(null);
					continue;
				}
				
				Object[] rowData = new Object[colSize];
				for( ; ic < colSize ; ic++ ) {
					Cell cell = row.getCell(ic);
//						CellType type = cell.getCellType();
					rowData[ic] = valueOf(cell);
				}
				if ( !isNullRow(rowData)) {
					data.add(rowData);					
				}
			}
			return norm(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			release( wbook );
		}
		
	}
	
	static boolean isNullRow(Object[] row) {
		for (int i = 0; i < row.length; i++) {
			if ( row[i] != null ) {
				return false;
			}
		}
		return true;
	}

	static void release(Workbook wbook) {
		if ( wbook != null) {
			try { wbook.close();} catch (IOException e) {}
		}
	}

	/*
	 * 각 row의 길이가 일정하지 않을 수 있음
	 * 가장 긴 row의 길이를 조사해서 모든 row들의 길이를 동일하게 맞춰줌
	 */
	static Object[][] norm(List<Object[]> data) {
		int colLen ;
		colLen = data.stream()
				     .mapToInt(row-> row == null ? 0 : row.length)
				     .max().getAsInt();
		
		Object[][] grid = new Object[data.size()][];
		for (int ir = 0; ir < grid.length; ir++) {
			Object [] rowData = data.get(ir);
			if ( rowData.length < colLen) {
				Object [] tmp = new Object[colLen];
				System.arraycopy(rowData, 0, tmp, 0, rowData.length);
				rowData = tmp;
			}
			grid[ir] = rowData;
		}
		return grid;
	}

	static Object valueOf(Cell cell) {
		if ( cell == null) {
			return null;
		}
		switch(cell.getCellType()) {
			case BLANK :
				return null;
			case STRING :
				return cell.getStringCellValue();
			case NUMERIC :
				double v = cell.getNumericCellValue();
				// ref : https://stackoverflow.com/questions/9898512/how-to-test-if-a-double-is-an-integer
				if (v % 1 == 0) {
					return (int)v;
				} else {
					return v;
				}
		default:
			return "??(" + cell.getCellType() + ")";
		}
	}

	public static List<Picture> readPics(String absfilePath, String sheetName) {
		Workbook wbook = null;
		try {
			wbook = WorkbookFactory.create(new File(absfilePath));
			Sheet sheet = wbook.getSheet(sheetName);
			
			
			Drawing<?> drawings = sheet.getDrawingPatriarch();
			if ( drawings == null) {
				return Collections.emptyList();
			}
			XSSFDrawing dw = (XSSFDrawing) drawings;
			List<Picture> pics = new ArrayList<>();
			for(XSSFShape shape : dw.getShapes() ) {
				if ( !(shape instanceof XSSFPicture) ) {
					continue;
				}
				XSSFPicture pic = (XSSFPicture) shape;
				XSSFClientAnchor anchor = pic.getClientAnchor();
				
				Picture picData = new Picture(pic);
				pics.add(picData);
			}
			return pics;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			release( wbook );
		}
	}
}
