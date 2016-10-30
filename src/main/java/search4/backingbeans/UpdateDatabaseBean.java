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

    private String message;

    public String updateDatabase(Integer limit) {
        try {
            updateDatabaseEJB.updateDatabase(limit);
            message = "Database updated";
        } catch (DataNotFoundException dnfe) {
            message = "Something went wrong!";
            System.err.println(""+dnfe);
        } catch (DuplicateDataException dde) {
            message = "Something went wrong!";
            System.err.println(""+dde);
        } catch (Exception e) {
            message = "Something went wrong!";
            System.err.println(""+e);
        }
        return "admin_panel";
    }

    public String synchronizeSubscriptions() {
        try {
            updateDatabaseEJB.getMovieChanges();
            message = "Subscriptions Synchronized";
        } catch (Exception e) {
            message = "Something went wrong!";
            System.err.println(""+e);
        }
        return "admin_panel";
    }

    public String getMessage() {
        return message;
    }
}
