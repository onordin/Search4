package search4.exceptions;

import javax.ws.rs.BadRequestException;

public class DataNotFoundException extends BadRequestException{

    public DataNotFoundException(String message) {
        super(message);
    }
}
