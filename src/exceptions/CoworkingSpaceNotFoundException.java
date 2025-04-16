package exceptions;

public class CoworkingSpaceNotFoundException extends RuntimeException{
    public CoworkingSpaceNotFoundException(String message) {
        super(message);
    }
}
