package search4.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.BadRequestException;

@ApplicationException(rollback=true)
public class DuplicateDataException extends BadRequestException { //TODO BadRequest or InternalServerError?

    public DuplicateDataException(String message) {
        super(message);
    }
}
