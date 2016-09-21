package search4.backingbeans;

import search4.ejb.UpdateDatabaseEJB;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.DuplicateDataException;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value="updateDatabaseBean") //if we want to use another name to call this from the JSF file
@SessionScoped
public class UpdateDatabaseBean implements Serializable{

    @EJB
    private UpdateDatabaseEJB updateDatabaseEJB;

    public String updateDatabase() {
        try {
            updateDatabaseEJB.updateDatabase();
        } catch (DataNotFoundException dnfe) {
            System.err.println(""+dnfe);
        } catch (DuplicateDataException dde) { //TODO can this happen?
            System.err.println(""+dde);
        } catch (Exception e) { //TODO more specfic cases?
            System.err.println(""+e);
        }
        return "updated";
    }
}
