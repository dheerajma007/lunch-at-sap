package dheeraj.lunchsap;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by I320939 on 22/07/2016.
 */

public class ReadExcel {
    private MenuTable menuTable;
    Context context;
    public ReadExcel(Context context)
    {
        menuTable = new MenuTable();
        this.context = context;
    }

    public MenuTable getMenuTable()
    {
        readTables();
        return menuTable;
    }

    void readTables()
    {
        File file = new File(Environment.getExternalStorageDirectory() + "/Menu.xlsx"); //context.getFilesDir() + "/Menu.xls");
        XSSFWorkbook workbook;
        try
        {
            XSSFColor colorWhite = new XSSFColor();
            colorWhite.setIndexed(IndexedColors.AUTOMATIC.getIndex());
            workbook = new XSSFWorkbook(new FileInputStream(file));
            outer: for(int sheetNum = 0; sheetNum < 3; sheetNum++)
            {
                System.out.println ("\n\nSheet " + sheetNum);
                XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                int startCol = 0, startRow = 4;
                if(sheetNum == 1) {
                    startCol = -1;
                    startRow = 3;
                }
                else if (sheetNum == 2)
                    startCol = 1;
                if(sheetNum != 1)
                    inner: for(int row = startRow; row < sheet.getLastRowNum() ; row++)  //row < numRows
                    {
                        //System.out.println("Row = "+row+" Col = "+startCol);
                        if(sheet.getRow(row) == null)
                            break inner;
                        XSSFCell cell = sheet.getRow(row).getCell(startCol);//sheet.getCell(startCol, row);
                        String cellValue = "";
                        if(cell != null) {
                            cellValue = cell.getStringCellValue();
                            //System.out.println("Color = " + cell.getCellStyle().getFillForegroundColor() + "White = " + IndexedColors.AUTOMATIC.getIndex());
                        }
                        if(true)//cell.getCellFormat().getBackgroundColour() == Colour.WHITE)
                        {
                            System.out.println(cellValue);
                            if(cellValue.startsWith("Calories are"))  //changed
                                break inner;
                            else
                                menuTable.menus[sheetNum].categoryList.add(cellValue.trim());
                        }
                    }

                inner: for(int col = startCol+1; col <= startCol+10; col+=2)
                {
                    System.out.println("Col "+ col);
                    ArrayList<String> list = new ArrayList<>();
                    inner2: for(int row = startRow; row < sheet.getLastRowNum(); row++)
                    {
                        if(sheet.getRow(row) == null)
                            break inner2;
                        XSSFCell cell = sheet.getRow(row).getCell(col); //sheet.getCell(col, row);
                        String cellValue = "";
                        if(cell != null) {
                            cellValue = cell.getStringCellValue();
                            //System.out.println("Color = " + cell.getCellStyle().getFillForegroundColorColor() + "White = " + colorWhite);//IndexedColors.AUTOMATIC.getIndex());
                        }
                        if(true)//cell != null && cell.getCellStyle().getFillForegroundColor() == IndexedColors.AUTOMATIC.getIndex()) //cell.getCellFormat().getBackgroundColour() == Colour.WHITE)
                        {
                            System.out.println(cellValue);
                            if(cellValue.startsWith("Calories are"))
                                break inner2;
                            else
                                list.add(cellValue.trim());
                        }
                    }

                    menuTable.menus[sheetNum].menuList.add(list);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}