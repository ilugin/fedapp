package fidelity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import fidelity.dataaccess.db.OrderDatabase;
import fidelity.dataaccess.db.OrderDatabaseImpl;
import fidelity.dataaccess.db.dao.OrderAnalisysDAO;
import fidelity.dataaccess.db.dao.OrderDAO;
import fidelity.dataaccess.db.dao.OrderSummaryDAO;
import fidelity.dto.Filter;
import fidelity.dto.Order;
import fidelity.dto.OrderSummary;
import fidelity.dto.Pager;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderDatabaseImpl database;

	private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);	
	/*
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	*/
	
	public List<Order> getAllOrders(Pager pager) {
		List<Order> ordersDTO = new ArrayList<Order>();

		// Translate DAO to DTO
		List<OrderDAO> orders = database.getAllOrders(pager);
		for (OrderDAO order : orders) {
			ordersDTO.add(order.getOrder());
		}
		pager.setTotalresults(orders.size());
		return ordersDTO;
	}

	public List<Order> feedOrders(List<Order> feedList) {
		List<Order> ordersDTO = new ArrayList<Order>();
		// Translate DAO to DTO
		List<OrderDAO> ordersDAO = database.setPersistentOrders(feedList);
		for (OrderDAO orderDAO : ordersDAO) {
			ordersDTO.add(orderDAO.getOrder());
		}

		return ordersDTO;
	}

	public Order createOrder(Order order) {
		OrderDAO orderDAO = database.createPersistenOrder(order);
		return orderDAO.getOrder();
	}

	public OrderSummary getOrdersSummary(Filter filter) {
		OrderSummaryDAO summaryDAO = database.getPersistentOrderSummary(filter);
		// Translate DAO to DTO
		return summaryDAO.getOrderSummary();
	}

	@Scheduled(fixedRate = 5000)
	public void calculateOrdersSummaryAsync() {
		// This is a result of analysis 
		OrderAnalisysDAO orderAnalisysDAO = new OrderAnalisysDAO ();

		// Get a deep copy of the existing orders
		List<OrderDAO> copyOfPersistentOrderList = database.queryPersistentOrders ();

		// 1. populate OrderAnalisysDAO internal maps, e.g all, security and funds
		// Those maps reflect search criteria 
		for (OrderDAO orderDAO: copyOfPersistentOrderList) {
			orderAnalisysDAO.getFundMap().put(orderDAO.getOrder().getFundName(), new OrderSummaryDAO());
			orderAnalisysDAO.getSecurityMap().put(orderDAO.getOrder().getSecurity(), new OrderSummaryDAO());
			orderAnalisysDAO.getAllMap().put(Filter.FilterType.all.toString(), new OrderSummaryDAO());
		}

		// 2. Calculate order summary for all possible security combinations
		orderAnalisysDAO.setSecurityMap(
				calulateOrderSummaryForSearchCritaria(
						copyOfPersistentOrderList, 
						Filter.FilterType.security, 
						orderAnalisysDAO.getSecurityMap()));
		// 3. Calculate order summary for all possible fund combinations
		orderAnalisysDAO.setFundMap(
				calulateOrderSummaryForSearchCritaria(
						copyOfPersistentOrderList,
						Filter.FilterType.fund, 
						orderAnalisysDAO.getFundMap()));

		// 4. Calculate order summary for all combinations
		orderAnalisysDAO.setAllMap(
				calulateOrderSummaryForSearchCritaria(
						copyOfPersistentOrderList,
						Filter.FilterType.all, 
						orderAnalisysDAO.getAllMap()));	

		// 5. Record final result
		database.setPersistentOrderAnalisysDAO(orderAnalisysDAO);
	}

	private Map<String, OrderSummaryDAO> calulateOrderSummaryForSearchCritaria (
			List<OrderDAO> copyOfPersistentOrderList,
			Filter.FilterType filterType,
			Map<String, OrderSummaryDAO> imputMap) {
		Map<String, OrderSummaryDAO> resultMap = imputMap;
		Iterator<Entry<String, OrderSummaryDAO>> iterator = resultMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, OrderSummaryDAO> entry = iterator.next();
			String searchKey = entry.getKey();
			
			// Calculate orders to be processed
			List<OrderDAO> ordersToBeProccessed = calculateOrdersToBeProccessed(
					copyOfPersistentOrderList,
					filterType, 
					searchKey);
			
			// Calculate calulateOrderSummary
			OrderSummaryDAO orderSummaryDAO = calulateOrderSummary(ordersToBeProccessed);
			
			
			// Update calculated value for the key.
			entry.setValue(orderSummaryDAO);
		}
		return resultMap;
	}	

	private List<OrderDAO> calculateOrdersToBeProccessed (
			List<OrderDAO> copyOfPersistentOrderList, 
			Filter.FilterType filterType,
			String searchKey) {
		List<OrderDAO> ordersToBeProccessed = new ArrayList<OrderDAO>();
		for (OrderDAO orderDAO: copyOfPersistentOrderList) {	
			switch (filterType) {
			case security:
				if(orderDAO.getOrder().getSecurity().equals(searchKey) == false) {
					continue;
				}
				break;
			case fund:
				if(orderDAO.getOrder().getFundName().equals(searchKey) == false) {
					continue;
				}
				break;
			case all:
				break;
				}		
			ordersToBeProccessed.add(orderDAO);
		}
		return ordersToBeProccessed;
	}	

	private OrderSummaryDAO calulateOrderSummary(List<OrderDAO> ordersToBeProccessed) {
		OrderSummaryDAO orderSummaryDAO = new OrderSummaryDAO();
		Map<String, ArrayList<OrderDAO>> feedOrdersMap = new HashMap<String, ArrayList<OrderDAO>>();
		
		Double averagePrice = 0.00;
		int totalQuantity = 0;
		
		for (OrderDAO orderDAO: ordersToBeProccessed) {			
			averagePrice += Double.valueOf(orderDAO.getOrder().getPrice());
			totalQuantity += Double.valueOf(orderDAO.getOrder().getQuantity());

			// Construct key (side, security and fund)
			String key = orderDAO.getOrder().getSide() + "+" + orderDAO.getOrder().getSecurity() + "+" + orderDAO.getOrder().getFundName();
			
			ArrayList<OrderDAO> ordersByKey = feedOrdersMap.get(key);
			if (ordersByKey == null) {
				ordersByKey = new ArrayList<OrderDAO>();
			}
			ordersByKey.add(orderDAO);

			feedOrdersMap.put(key, ordersByKey);
		}
		
		// Iterate through the Map
		int totalNumberOfCombinableOrders = 0;
		Iterator<Entry<String, ArrayList<OrderDAO>>> iterator = feedOrdersMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, ArrayList<OrderDAO>> entry = iterator.next();
			int count = entry.getValue().size();
			if (count > 1) {
				totalNumberOfCombinableOrders ++;
				orderSummaryDAO.getOrderSummary().getCombynedCritarias().add(entry.getKey());
			}
		}		
		
		orderSummaryDAO.setTotaNumberOfOrders(ordersToBeProccessed.size());
		orderSummaryDAO.setTotalQuantity(totalQuantity);
		averagePrice = averagePrice / ordersToBeProccessed.size();
		orderSummaryDAO.setAveragePrice(averagePrice.toString());
		orderSummaryDAO.setTotalNumberOfCombinableOrders(totalNumberOfCombinableOrders);

		// Log
		Gson gson = new Gson();
		String json = gson.toJson(orderSummaryDAO);
		log.info("Summary has been calculated {}", json);
		return orderSummaryDAO;
	}
	
}
