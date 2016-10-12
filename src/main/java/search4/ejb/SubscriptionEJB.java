package search4.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import search4.daobeans.SubscriptionDAOBean;

@Stateless
public class SubscriptionEJB {

	@EJB
	private SubscriptionDAOBean subsctiptionDAOBean;
	
	
	
}
