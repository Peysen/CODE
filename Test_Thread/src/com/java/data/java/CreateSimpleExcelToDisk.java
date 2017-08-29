package com.java.data.java;

import com.java.data.model.Member;
import org.apache.poi.hssf.usermodel.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prayyic on 2017/7/3.
 * 测试如何生成excel表
 */
public class CreateSimpleExcelToDisk {
    /**
     * @功能：手工构建一个简单格式的Excel
     */
    private static List<Member> getMember() throws Exception
    {
        List list = new ArrayList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");

        Member user1 = new Member(1, "熊大", 24, df.parse("1993-08-28"));
        Member user2 = new Member(2, "熊二", 23, df.parse("1994-08-19"));
        Member user3 = new Member(3, "熊熊", 24, df.parse("1983-11-22"));
        list.add(user1);
        list.add(user2);
        list.add(user3);

        return list;
    }

    public static void main(String[] args) throws Exception
    {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("学生表一");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("学号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("年龄");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("生日");
        cell.setCellStyle(style);

        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        List list = CreateSimpleExcelToDisk.getMember();

        for (int i = 0; i < list.size(); i++)
        {
            row = sheet.createRow((int) i + 1);
            Member stu = (Member) list.get(i);
            // 第四步，创建单元格，并设置值
            row.createCell((short) 0).setCellValue((double) stu.getCode());
            row.createCell((short) 1).setCellValue(stu.getName());
            row.createCell((short) 2).setCellValue((double) stu.getAge());
            cell = row.createCell((short) 3);
            cell.setCellValue(new SimpleDateFormat("yyyy-mm-dd").format(stu
                    .getBirth()));
        }
        // 第六步，将文件存到指定位置
        try
        {
            FileOutputStream fout = new FileOutputStream("E:/Members.xls");
            wb.write(fout);
            fout.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
