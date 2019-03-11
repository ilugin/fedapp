package fidelity.service;

import java.util.List;

import fidelity.dto.Filter;
import fidelity.dto.Order;
import fidelity.dto.OrderSummary;
import fidelity.dto.Pager;

public interface OrderService {
	public List<Order> getAllOrders(Pager pager);
	public List<Order> feedOrders (List<Order> feedList);
	public Order createOrder(Order order);
	public OrderSummary getOrdersSummary(Filter filter);	
    public void calculateOrdersSummaryAsync();	
}
