package fidelity.dataaccess.db;

import java.util.List;

import fidelity.dataaccess.db.dao.OrderDAO;
import fidelity.dataaccess.db.dao.OrderSummaryDAO;
import fidelity.dto.Filter;
import fidelity.dto.Order;
import fidelity.dto.Pager;

public interface OrderDatabase {

	public List<OrderDAO> setPersistentOrders (List<Order> orderList);
	public List<OrderDAO> getAllOrders (Pager pager);
	public OrderDAO createPersistenOrder (Order order);
	public void calculateOrdersSummaryAsync ();	
	public OrderSummaryDAO getPersistentOrderSummary (Filter filter);

}
