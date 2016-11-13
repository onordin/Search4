package search4.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.BadRequestException;

@ApplicationException(rollback=true)
public class DuplicateDataException extends BadRequestException { 
    public DuplicateDataException(String message) {
        super(message);
    }
}
