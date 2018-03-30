package com.ezshipp.api.controllers;

import com.ezshipp.api.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

//import javax.ws.rs.core.Response;

//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;

/**
 * Created by srinivasseri on 2/26/18.
 */
@RestController
@Api(value = "/api/v1/downloads", description = "a rest service")
@RequestMapping(path = "/api/v1/downloads")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class ExportController implements ControllerConstants {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/orders/excel", method = RequestMethod.GET)
    public Resource downloadXLS(HttpServletResponse response) throws Exception    {

        //logger.info("downloading xls sheet...");

//        String reportName = "Orders_Central_Publications.xlsx";
//
//        List<Order> dataModel = orderService.centralPublicationOrders(false).getDocumentList();
//
//        Workbook workbook = new ExcelGeneratorChild().createXLS(dataModel);
//        response.setHeader("Content-Type","application/vnd.ms-excel");
//        response.setHeader("Content-Disposition", "attachment; filename=" + reportName);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        workbook.write(bos);
//        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
//        return new InputStreamResource(inputStream);

        return null;
    }

}
