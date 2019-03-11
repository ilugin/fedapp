package fidelity.dataaccess.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import fidelity.dataaccess.db.dao.OrderAnalisysDAO;
import fidelity.dataaccess.db.dao.OrderDAO;
import fidelity.dataaccess.db.dao.OrderSummaryDAO;
import fidelity.dto.Filter;
import fidelity.dto.Filter.FilterType;
import fidelity.dto.Order;
import fidelity.dto.Order.Side;
import fidelity.service.OrderServiceImpl;
import fidelity.dto.Pager;

import com.google.gson.Gson;



@Component
public class OrderDatabaseImpl /* implements OrderDatabase */ {
	List<OrderDAO> persistentOrders = new ArrayList<OrderDAO>();	
	OrderAnalisysDAO persistentOrderAnalisysDAO = new OrderAnalisysDAO();
	
	
	private static final Logger log = LoggerFactory.getLogger(OrderDatabaseImpl.class);

// Initial data set
	public OrderDatabaseImpl() {
		this.persistentOrders.add(new OrderDAO(1, Side.BUY, "AAPL", "MAG", 10000, "100"));
		this.persistentOrders.add(new OrderDAO(2, Side.BUY, "GOOG", "CONT", 1000, "700"));
		this.persistentOrders.add(new OrderDAO(3, Side.BUY, "VAN", "FP1", 1000, "10"));
		this.persistentOrders.add(new OrderDAO(4, Side.BUY, "AAPL", "MAG", 2000, "100"));
		this.persistentOrders.add(new OrderDAO(5, Side.SELL, "T", "F2", 1000, "30"));
		this.persistentOrders.add(new OrderDAO(6, Side.BUY, "VZ", "CANA", 1000, "50"));
		this.persistentOrders.add(new OrderDAO(7, Side.BUY, "GOOG", "CANA", 1000, "700"));
		this.persistentOrders.add(new OrderDAO(8, Side.SELL, "VAN", "FBSC", 1000, "10"));
		this.persistentOrders.add(new OrderDAO(9, Side.BUY, "AAPL", "FBIO", 2000, "100"));
		this.persistentOrders.add(new OrderDAO(9, Side.SELL, "T", "F2", 1000, "30"));
	}

	
	public List<OrderDAO> getPersistentOrders() {
		return persistentOrders;
	}

	public OrderAnalisysDAO getPersistentOrderAnalisysDAO() {
		return persistentOrderAnalisysDAO;
	}

	public void setPersistentOrderAnalisysDAO(OrderAnalisysDAO persistentOrderAnalisysDAO) {
		this.persistentOrderAnalisysDAO = persistentOrderAnalisysDAO;
	}

	// Called from a service
	public synchronized List<OrderDAO> setPersistentOrders (List<Order> orderList) {
		this.persistentOrders.clear();
		for (Order orderDTO: orderList) {
			OrderDAO orderDAO = new OrderDAO (orderDTO);
			this.persistentOrders.add(orderDAO);
		}
		return this.persistentOrders;
	}

	// Called from a service
	public synchronized List<OrderDAO> getAllOrders (Pager pager) {
		return this.persistentOrders;
	}

	// Called from a service
	public synchronized OrderDAO createPersistenOrder (Order order) {
		OrderDAO orderDAO = new OrderDAO (order);
		
		// Set a new order ID
		Random ran = new Random();
		int orderId = ran.nextInt(100) + 5;
		orderDAO.getOrder().setOrderId(orderId);
		
		this.persistentOrders.add(orderDAO);
		return orderDAO;
	}
		

	// Rendering not a calculation
	public OrderSummaryDAO getPersistentOrderSummary (Filter filter) {
		OrderSummaryDAO orderSummaryDAO = null;
		synchronized (this.persistentOrderAnalisysDAO) {
			switch (filter.getType()) {
				case security:
				{
					Map<String, OrderSummaryDAO> map = this.persistentOrderAnalisysDAO.getSecurityMap();
					orderSummaryDAO = map.get(filter.getValue());
					break;
				}
				case fund:
				{
					Map<String, OrderSummaryDAO> map = this.persistentOrderAnalisysDAO.getFundMap();
					orderSummaryDAO = map.get(filter.getValue());
					break;
				}
				case all:
				{
					Map<String, OrderSummaryDAO> map = this.persistentOrderAnalisysDAO.getAllMap();
					orderSummaryDAO = map.get(filter.getType().toString());
					break;
				}
			
			}
		}
		return orderSummaryDAO;
	}	
	
	
	public synchronized List<OrderDAO> queryPersistentOrders () {
		// Creates a deep copy
		List<OrderDAO> snapShotOrders = new ArrayList<OrderDAO>();
		for (OrderDAO order: this.persistentOrders) {
			OrderDAO orderDAO = new OrderDAO (order.getOrder());
			snapShotOrders.add(orderDAO);
		}		
		return snapShotOrders;
	}
		
}

