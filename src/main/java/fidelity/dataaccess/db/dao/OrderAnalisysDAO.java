package fidelity.dataaccess.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderAnalisysDAO {

	Map<String, OrderSummaryDAO> securityMap = new HashMap<String, OrderSummaryDAO>();
	Map<String, OrderSummaryDAO> fundMap = new HashMap<String, OrderSummaryDAO>();
	Map<String, OrderSummaryDAO> allMap = new HashMap<String, OrderSummaryDAO>();
	public Map<String, OrderSummaryDAO> getSecurityMap() {
		return securityMap;
	}
	public void setSecurityMap(Map<String, OrderSummaryDAO> securityMap) {
		this.securityMap = securityMap;
	}
	public Map<String, OrderSummaryDAO> getFundMap() {
		return fundMap;
	}
	public void setFundMap(Map<String, OrderSummaryDAO> fundMap) {
		this.fundMap = fundMap;
	}
	public Map<String, OrderSummaryDAO> getAllMap() {
		return allMap;
	}
	public void setAllMap(Map<String, OrderSummaryDAO> allMap) {
		this.allMap = allMap;
	}
	
	
}
