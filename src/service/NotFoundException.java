package service;

public class NotFoundException extends Throwable {
    public NotFoundException(String message) {
        super(message);
    }
}