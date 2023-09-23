package com.project.currenctExcDemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpResponse;

public class ConvertJsonResp {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void convert(HttpServletResponse response, Object object) throws IOException {
        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        printWriter.print(objectMapper.writeValueAsString(object));
        printWriter.flush();
    }
}
