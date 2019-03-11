package fidelity.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderSummary {
	int totaNumberOfOrders;
	int totalQuantity;
	String averagePrice;
	int totalNumberOfCombinableOrders; 
	private List<String> combynedCritarias = new ArrayList<String>();
	
	public int getTotaNumberOfOrders() {
		return totaNumberOfOrders;
	}
	public void setTotaNumberOfOrders(int totaNumberOfOrders) {
		this.totaNumberOfOrders = totaNumberOfOrders;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public String getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(String averagePrice) {
		this.averagePrice = averagePrice;
	}
	public int getTotalNumberOfCombinableOrders() {
		return totalNumberOfCombinableOrders;
	}
	public void setTotalNumberOfCombinableOrders(int totalNumberOfCombinableOrders) {
		this.totalNumberOfCombinableOrders = totalNumberOfCombinableOrders;
	}
	public List<String> getCombynedCritarias() {
		return this.combynedCritarias;
	}
	public void setCombynedCritarias(List<String> combynedCritarias) {
		this.combynedCritarias = combynedCritarias;
	}
}
