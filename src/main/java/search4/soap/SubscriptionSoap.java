package search4.soap;

import search4.ejb.SubscriptionEJB;
import search4.entities.DisplaySubscriptionEntity;
import search4.entities.DisplayUserEntity;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.List;

@WebService(serviceName="subscriptionSoap")
public class SubscriptionSoap {

    @EJB
    private SubscriptionEJB subscriptionEJB;

    public List<DisplaySubscriptionEntity> getUserById(Integer userId) throws Exception{
        return subscriptionEJB.getAllFor(userId);
    }
}
