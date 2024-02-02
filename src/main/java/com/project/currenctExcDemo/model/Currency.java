package com.project.currenctExcDemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "currencies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "currency_code")
    private String code;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "currency_sign")
    private String Sign;

    public Currency(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        Sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(code, currency.code) && Objects.equals(fullName, currency.fullName) && Objects.equals(Sign, currency.Sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, fullName, Sign);
    }
}
