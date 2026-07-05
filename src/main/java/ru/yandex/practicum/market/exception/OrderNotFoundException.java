package ru.yandex.practicum.market.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {super(message);}
}
