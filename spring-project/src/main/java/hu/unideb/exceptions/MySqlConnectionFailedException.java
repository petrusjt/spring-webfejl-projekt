package hu.unideb.exceptions;

public class MySqlConnectionFailedException extends Exception{
    public MySqlConnectionFailedException(String message)
    {
        super(message);
    }
}
