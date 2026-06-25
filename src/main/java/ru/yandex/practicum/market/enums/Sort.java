package ru.yandex.practicum.market.enums;

public enum Sort {

    NO, ALPHA, PRICE;

    public static Sort checkSortFromString(String sort) {
        try {
            return Sort.valueOf(sort.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неизвестный тип сортировки: " + sort);
        }
    }

}