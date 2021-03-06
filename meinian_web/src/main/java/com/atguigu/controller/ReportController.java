package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.contant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.MemberService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    ReportService ReportService;

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            List<String> months = new ArrayList<>();
            List<Integer> memberCount = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -12);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH, 1);
                Date time = calendar.getTime();
                String month = simpleDateFormat.format(time);
                months.add(month);
            }
            memberCount = memberService.findMemberCountByMonth(months);


            HashMap<String, Object> map = new HashMap<>();
            map.put("months", months);
            map.put("memberCount", memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {

            List<String> setmealNames = new ArrayList<>();
            // List<Long> values = new ArrayList<>();
            List<Map> setmealCount = setmealService.getSetmealReport();
            for (Map map : setmealCount) {
                String setmealName = (String) map.get("name");
                Long value = (Long) map.get("value");
                setmealNames.add(setmealName);
                //    values.add(value);
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("setmealNames", setmealNames);
            //  map.put("setmealCount",values);
            map.put("setmealCount", setmealCount);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);

        }

    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map map = ReportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);

        }
    }

    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream outputStream=null;
        Workbook workbook=null;
        try {
            //1.?????????
            Map result = ReportService.getBusinessReportData();

//?????????????????????????????????????????????????????????Excel?????????
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            //2.??????????????????

            String filepath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
             workbook = new XSSFWorkbook(new FileInputStream(new File(filepath)));
            //3.?????????
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//???????????????????????????
            row.getCell(7).setCellValue(totalMember);//????????????

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//?????????????????????
            row.getCell(7).setCellValue(thisMonthNewMember);//?????????????????????

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//???????????????
            row.getCell(7).setCellValue(todayVisitsNumber);//???????????????

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//???????????????
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//???????????????

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//???????????????
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//???????????????

            int rowNum = 12;
            for (Map map : hotSetmeal) {
                //name:'??????7???6????????????',setmeal_count:200,proportion:0.222},
                row = sheet.getRow(rowNum);
                row.getCell(4).setCellValue((String) map.get("name"));
                row.getCell(5).setCellValue((Long) map.get("setmeal_count"));
                row.getCell(6).setCellValue(((BigDecimal) map.get("proportion")).doubleValue());
                rowNum++;
            }
            //4.????????????,??????????????????,???????????????
            outputStream = response.getOutputStream();
            //?????????????????????????????????????????????
            // ????????????????????????excel?????????
            response.setContentType("application/vnd.ms-excel");
            // ??????????????????(???????????????????????????)
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

            workbook.write(outputStream);//???????????????
            //??????
            outputStream.flush();



        } catch (Exception e) {
            e.printStackTrace();
            try {
                request.getRequestDispatcher("/pages/error/downloadError.html").forward(request,response);
            } catch (ServletException servletException) {
                servletException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }finally {
            //5.?????????
            if (outputStream != null) {

                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
