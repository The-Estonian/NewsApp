package ee.news.app.infrastructure.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String errorMessage) {
        super(errorMessage);
    }
}

