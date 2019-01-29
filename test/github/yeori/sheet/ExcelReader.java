package github.yeori.sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFOddHeader;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
/**
 * ref : http://poi.apache.org/components/spreadsheet/quick-guide.html
 * 
 * @author chmin.seo
 *
 */
public class ExcelReader {

	public static void main(String[] args) throws IOException, InvalidFormatException {
//		String filePath = "E:/DB_Test.xlsx";
		String filePath = "E:\\\\tmp\\\\aac\\\\demo-img.xlsx";
		FileInputStream fin = new FileInputStream(filePath);
//		HSSFWorkbook wbook = new HSSFWorkbook(fin);
		
		OPCPackage pkg = OPCPackage.open(fin);
		
		Workbook wbook = WorkbookFactory.create(new File(filePath));
//		XSSFWorkbook wbook = new XSSFWorkbook(pkg);
		Sheet cateSheet = wbook.getSheet("category");
//		readCategory(cateSheet);
		readPics(wbook.getSheet("main"));
		wbook.close();
		
	}

	static void readPics(Sheet sheet) {
		Drawing<?> drawings = sheet.getDrawingPatriarch();
		if ( drawings == null) {
			System.out.println("no shape");
		}
		XSSFDrawing dw = (XSSFDrawing) drawings;
		for(XSSFShape shape : dw.getShapes() ) {
			if ( shape instanceof XSSFPicture) {
				XSSFPicture pic = (XSSFPicture) shape;
				XSSFClientAnchor anchor = pic.getClientAnchor();
				CTMarker from = anchor.getFrom();
				CTMarker to = anchor.getTo();
				anchor.getCol2();
				System.out.printf("%s [%s, (from:%d,%d), (to:%d,%d),(X:%d,%d,%d) (Y:%d,%d,%d)]\n",
						pic.getShapeName(), 
						anchor.getAnchorType(),
						from.getRow(), from.getCol(), 
						to.getRow(), to.getCol(),
						anchor.getDx1(), anchor.getDx2(), anchor.getDx2()-anchor.getDx1(),
						anchor.getDy1(), anchor.getDy2(), anchor.getDy2()-anchor.getDy1()
				);
				
			}
		}
		// List<? extends PictureData> pics = excel.getAllPictures();
		
		/*for (PictureData pic : pics) {
			XSSFPictureData pd = (XSSFPictureData) pic;
			PackagePart pkg = pd.getPackagePart();
			System.out.println(pic.getMimeType() + ":" + pic.suggestFileExtension());
		}*/
	}

	static void readCategory(Sheet sheet){
		int [] cnt = {0};
		System.out.println(sheet.getLastRowNum());
//		XSSFOddHeader header = (XSSFOddHeader) cateSheet.getHeader();
//		System.out.println(header.getText());
		int limit = sheet.getPhysicalNumberOfRows();
		for(int ir = sheet.getFirstRowNum() ; ir < limit ; ir++) {
			Row row = sheet.getRow(ir);
			System.out.print(ir);
			row.forEach(col->{
				CellType type = col.getCellType();
				System.out.printf(" %s(%s) | ", valueOf(col), type ); 
			});
			System.out.println();
		}
	}
	
	static String valueOf(Cell cell) {
		switch(cell.getCellType()) {
			case BLANK :
				return "";
			case STRING :
				return cell.getStringCellValue();
			case NUMERIC :
				return "" + cell.getNumericCellValue();
		default:
			return "??(" + cell.getCellType() + ")";
		}
	}
}
