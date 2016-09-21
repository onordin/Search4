package search4.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.BadRequestException;

@ApplicationException(rollback=true)
public class DataNotFoundException extends BadRequestException{

    public DataNotFoundException(String message) {
        super(message);
    }
}
