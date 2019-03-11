package fidelity.dataaccess.db.dao;

import java.util.ArrayList;
import java.util.List;

import fidelity.dto.Order;
import fidelity.dto.OrderSummary;

public class OrderSummaryDAO {
	OrderSummary orderSummary = new OrderSummary();

	public void setTotaNumberOfOrders(int totaNumberOfOrders) {
		this.orderSummary.setTotaNumberOfOrders(totaNumberOfOrders);;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.orderSummary.setTotalQuantity(totalQuantity);
	}

	public void setAveragePrice(String averagePrice) {
		this.orderSummary.setAveragePrice(averagePrice);
	}

	public OrderSummary getOrderSummary() {
		return orderSummary;
	}

	public void setOrderSummary(OrderSummary orderSummary) {
		this.orderSummary = orderSummary;
	}
	
	public void setTotalNumberOfCombinableOrders(int totalNumberOfCombinableOrders) {
		this.orderSummary.setTotalNumberOfCombinableOrders(totalNumberOfCombinableOrders);
	}

	
}
