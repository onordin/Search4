package search4.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.BadRequestException;

@ApplicationException(rollback=true)
public class InvalidInputException extends BadRequestException {//TODO Right type?

    public InvalidInputException(String message) {
        super(message);
    }
}
