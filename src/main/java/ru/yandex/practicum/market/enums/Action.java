package ru.yandex.practicum.market.enums;

public enum Action {

    MINUS, PLUS;

    public static Action checkActionFromString(String action) {
        try {
            return Action.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неизвестное действие: " + action);
        }
    }

}
