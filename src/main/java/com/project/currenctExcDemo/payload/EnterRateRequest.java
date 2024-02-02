package com.project.currenctExcDemo.payload;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EnterRateRequest {
    BigDecimal rate;
}
