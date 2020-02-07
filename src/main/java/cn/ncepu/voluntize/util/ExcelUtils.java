package cn.ncepu.voluntize.util;

import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author 李光光(编码小王子) https://blog.csdn.net/u011900448/article/details/53097382
 * @author Ge Hanchen 添加数据缓冲，自动表头
 */
public class ExcelUtils<T> {

    public XSSFWorkbook exportExcel(List<T> dataset, String fileName) {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(fileName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 产生表格标题行
        Class<T> tClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Field[] fields = tClass.getDeclaredFields();
        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            XSSFCell cell = row.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(fields[i].getName());
            cell.setCellValue(text);
        }
        try {
            for (int j = 1; j < dataset.size(); j++) {
                row = sheet.createRow(j);
                T t = dataset.get(j - 1);
                for (short i = 0; i < fields.length; i++) {
                    XSSFCell cell = row.createCell(i);
                    Field field = fields[i];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method getMethod = t.getClass().getMethod(getMethodName, t.getClass());
                    Object value = getMethod.invoke(t);
                    String textValue = null;
                    if (value != null && value != "") textValue = value.toString();
                    if (textValue != null) {
                        XSSFRichTextString richString = new XSSFRichTextString(textValue);
                        cell.setCellValue(richString);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    public void writeToStream(XSSFWorkbook workbook, OutputStream outputStream) throws IOException {
//            String fileName = name + ".xlsx";
//            response.setContentType("application/x-msdownload");
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
//            fos = new BufferedOutputStream(response.getOutputStream());
        workbook.write(outputStream);
        outputStream.close();
    }
}
