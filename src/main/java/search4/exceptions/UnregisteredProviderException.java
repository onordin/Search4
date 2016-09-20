package search4.exceptions;

import javax.ws.rs.InternalServerErrorException;

public class UnregisteredProviderException extends InternalServerErrorException{

    public UnregisteredProviderException(String message) {
        super(message);
    }
}
