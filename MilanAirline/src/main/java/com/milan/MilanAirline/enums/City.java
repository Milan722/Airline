package com.milan.MilanAirline.enums;


import lombok.Getter;

@Getter
public enum City {

    LAGOS(Country.NIGERIA),
    ABUJA(Country.NIGERIA),

    DENVER(Country.USA),
    DALAS(Country.USA),

    LONDON(Country.UK),
    MANCHESTER(Country.UK);

    private final Country country;

    City(Country country) {
        this.country = country;
    }
}
