package fidelity.dto;

import java.util.List;

public class OrderList {
	private int count;
	private List<Order> orders;
	private Pager pager;
	
	public OrderList() {
	}
	public int getCount() {
		return orders.size();
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Pager getPager() {
		return pager;
	}
	public void setPager(Pager pager) {
		this.pager = pager;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

}
