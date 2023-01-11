package excleReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class UtilityClass {
	public String excleReader(String sheet,int row,int cell) throws EncryptedDocumentException, IOException {
		String path=System.getProperty("user.dir")+"//FilesForOutput.xlsx";
		FileInputStream file=new FileInputStream(path);

		String ok1=WorkbookFactory.create(file).getSheet(sheet).getRow(row).getCell(cell).getStringCellValue();
	
		return ok1;
	}
}
