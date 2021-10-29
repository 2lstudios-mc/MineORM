package dev._2lstudios.mineorm.errors;

public class NotIDAnnotationException extends Exception {
    public NotIDAnnotationException(final Object obj) {
        super("Entity " + obj.getClass().getName() + " doesn't contains a valid @ID decorated property.");
    }
}
