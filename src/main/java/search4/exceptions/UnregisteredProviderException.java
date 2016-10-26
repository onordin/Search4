package search4.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.InternalServerErrorException;

@ApplicationException(rollback=true)
public class UnregisteredProviderException extends InternalServerErrorException{

    public UnregisteredProviderException(String message) {
        super(message);
    }
}
