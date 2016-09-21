package search4.backingbeans;

import search4.ejb.UpdateDatabaseEJB;

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
        updateDatabaseEJB.updateDatabase();
        return "updated";
    }
}
