package ru.yandex.practicum.market.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleItemNotFoundException(final ItemNotFoundException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND,
                "Товар не найден",
                e.getMessage()
        );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleOrderNotFoundException(final OrderNotFoundException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND,
                "Заказ не найден",
                e.getMessage()
        );
    }

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCartNotFoundException(final CartNotFoundException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND,
                "Корзина не найден",
                e.getMessage()
        );
    }

    @ExceptionHandler(ItemInCartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleItemInCartNotFoundException(final ItemInCartNotFoundException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND,
                "Товар не найден в корзине",
                e.getMessage()
        );
    }

    @ExceptionHandler(CartIsEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCartIsEmptyException(final CartIsEmptyException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND,
                "Корзина пуста",
                e.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(final Exception e) {
        return new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Произошла ошибка",
                Arrays.toString(e.getStackTrace())
        );
    }

}