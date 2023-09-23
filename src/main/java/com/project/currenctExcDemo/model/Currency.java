package com.project.currenctExcDemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    private int id;
    private String code;
    private String FullName;
    private String Sign;

    public Currency(String code, String fullName, String sign) {
        this.code = code;
        FullName = fullName;
        Sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(code, currency.code) && Objects.equals(FullName, currency.FullName) && Objects.equals(Sign, currency.Sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, FullName, Sign);
    }
}
