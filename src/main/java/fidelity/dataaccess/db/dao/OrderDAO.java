package fidelity.dataaccess.db.dao;

import fidelity.dto.Order;
import fidelity.dto.Order.Side;

public class OrderDAO implements Cloneable {
	Order order = new Order();

	public OrderDAO (int orderId, Side side, String security, String fundName, int quantity, String price ) {
		this.order.setOrderId(orderId);
		this.order.setSide(side);
		this.order.setSecurity(security);
		this.order.setFundName(fundName);
		this.order.setQuantity(quantity);
		this.order.setPrice(price);
	}

	public OrderDAO (Order order) {
		this.order.setOrderId(order.getOrderId());
		this.order.setSide(order.getSide());
		this.order.setSecurity(order.getSecurity());
		this.order.setFundName(order.getFundName());
		this.order.setQuantity(order.getQuantity());
		this.order.setPrice(order.getPrice());
	}
	
	public Order getOrder() {
		return order;
	}

	/*
    public @Override Object clone() throws CloneNotSupportedException { 
    	OrderDAO cloned = new OrderDAO (this.getOrder());
    	return cloned;
	}
	*/
    

}
