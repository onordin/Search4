package search4.backingbeans;

import search4.ejb.UpdateDatabaseEJB;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.DuplicateDataException;
import search4.ejb.interfaces.LocalUpdateDatabase;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value="updateDatabaseBean") //if we want to use another name to call this from the JSF file
@SessionScoped
public class UpdateDatabaseBean implements Serializable{

	private static final long serialVersionUID = 5248188089139817907L;

	@EJB
    private LocalUpdateDatabase updateDatabaseEJB;

    public String updateDatabase() {
        try {
            updateDatabaseEJB.updateDatabase();
        } catch (DataNotFoundException dnfe) {
            System.err.println(""+dnfe);
        } catch (DuplicateDataException dde) {
            System.err.println(""+dde);
        } catch (Exception e) {
            System.err.println(""+e);
        }
        return "updated";
    }
}
