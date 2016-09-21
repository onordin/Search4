package search4.exceptions;

import javax.ws.rs.BadRequestException;

public class DuplicateDataException extends BadRequestException { //TODO BadRequest or InternalServerError?

    public DuplicateDataException(String message) {
        super(message);
    }
}
