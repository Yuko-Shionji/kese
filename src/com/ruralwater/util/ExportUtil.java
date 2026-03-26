package com.ruralwater.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 数据导出工具类（支持 Excel）
 */
public class ExportUtil {
    
    /**
     * 导出表格数据到 Excel
     * @param frame 父窗口
     * @param columnNames 列名
     * @param data 数据
     * @param sheetName 工作表名称
     * @return 是否导出成功
     */
    public static boolean exportToExcel(JFrame frame, String[] columnNames, 
                                        Object[][] data, String sheetName) {
        try {
            // 创建文件选择器
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("保存 Excel 文件");
            fileChooser.setSelectedFile(new File("导出数据.xlsx"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Excel 文件", "xlsx");
            fileChooser.setFileFilter(filter);
            
            int userSelection = fileChooser.showSaveDialog(frame);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                // 确保文件名以 .xlsx 结尾
                String filePath = file.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }
                
                // 创建工作簿
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet(sheetName);
                
                // 创建标题行样式
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                headerStyle.setFont(headerFont);
                
                // 创建标题行
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < columnNames.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnNames[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                // 填充数据
                for (int i = 0; i < data.length; i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < data[i].length; j++) {
                        Cell cell = row.createCell(j);
                        if (data[i][j] != null) {
                            cell.setCellValue(data[i][j].toString());
                        }
                    }
                }
                
                // 自动调整列宽
                for (int i = 0; i < columnNames.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                // 写入文件
                FileOutputStream fos = new FileOutputStream(filePath);
                workbook.write(fos);
                fos.close();
                workbook.close();
                
                JOptionPane.showMessageDialog(frame, 
                    "导出成功！\n文件位置：" + filePath, 
                    "成功", 
                    JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, 
                "导出失败：" + e.getMessage(), 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return false;
    }
    
    /**
     * 导出表格数据到 Excel（从 JTable）
     * @param frame 父窗口
     * @param table JTable 组件
     * @param sheetName 工作表名称
     * @return 是否导出成功
     */
    public static boolean exportTableToExcel(JFrame frame, JTable table, String sheetName) {
        try {
            int rowCount = table.getRowCount();
            int colCount = table.getColumnCount();
            
            // 获取列名
            String[] columnNames = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                columnNames[i] = table.getColumnName(i);
            }
            
            // 获取数据
            Object[][] data = new Object[rowCount][colCount];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    data[i][j] = table.getValueAt(i, j);
                }
            }
            
            return exportToExcel(frame, columnNames, data, sheetName);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
