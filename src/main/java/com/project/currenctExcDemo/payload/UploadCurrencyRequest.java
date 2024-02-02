package com.project.currenctExcDemo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadCurrencyRequest {
    private String name;
    private String code;
    private String sign;
}
