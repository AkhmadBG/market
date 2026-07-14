package ru.yandex.practicum.market.exception;

public class ItemInCartNotFoundException extends RuntimeException {
    public ItemInCartNotFoundException(String message) {
        super(message);
    }
}