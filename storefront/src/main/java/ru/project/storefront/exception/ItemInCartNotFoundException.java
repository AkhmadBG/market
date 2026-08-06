package ru.project.storefront.exception;

public class ItemInCartNotFoundException extends RuntimeException {
    public ItemInCartNotFoundException(String message) {
        super(message);
    }
}