package fidelity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fidelity.dto.Filter;
import fidelity.dto.Filter.FilterType;
import fidelity.dto.Order;
import fidelity.dto.OrderList;
import fidelity.dto.OrderSummary;
import fidelity.dto.Pager;
import fidelity.service.OrderService;

@RestController
public class OrderController {
	@Autowired
	OrderService service;
	
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleException() {
        return null;
    }	

	/*
	 * Get a single order localhost:8080/fdapp/servive/0.1.0/orders/1
	 */

	// TOTO: Implement this methos

	/*
	 * Get all orders localhost:8080/fdapp/servive/0.1.0/orders
	 */

	@GetMapping("/servive/{version}/orders")
	OrderList getAllOrders(@PathVariable String version,
			@RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
			@RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize) {
		OrderList list = new OrderList();

		Pager pager = new Pager(offset, pagesize);
		List<Order> orders = service.getAllOrders(pager);
		list.setOrders(orders);
		list.setPager(pager);
		return list;
	}

	/*
	 * Create an order localhost:8080/fdapp/servive/0.1.0/orders
	 * 
	 * Request body:
	 * {"side":"BUY","security":"GOOG","fundName":"CONT","quantity":1000,"price":
	 * "700000000"}
	 */

	@PostMapping("/servive/{version}/orders")
	Order createOrder(@RequestBody Order order, @PathVariable String version) {
		Order createdOrder = service.createOrder(order);
		return createdOrder;
	}

	/*
	 * Feed orders localhost:8080/fdapp/servive/0.1.0/orders/feed
	 * 
	 * Headers: Content-Type: application/json
	 * 
	 * Request body:
	 * 
	 * 
	 * [ { "orderId": 1, "side": "BUY", "security": "AAPL", "fundName": "MAG",
	 * "quantity": 10000, "price": "100" }, { "orderId": 2, "side": "BUY",
	 * "security": "GOOG", "fundName": "CONT", "quantity": 1000, "price": "700" } ]
	 */

	@PostMapping("/servive/{version}/orders/feed")
	List<Order> feedOrders(@RequestBody List<Order> feed, @PathVariable String version) {
		return service.feedOrders(feed);
	}

	// Analytics must be placed in a different service
	/*
	 * Get order summary 
	 * localhost:8080/fdapp/servive/0.1.0/orders/summary
	 * localhost:8080/fdapp/servive/0.1.0/orders/summary?type=security&value=blah
	 */

	@GetMapping("/servive/{version}/orders/summary")
	OrderSummary getOrdersSummary(
			@PathVariable String version,
			@RequestParam(value = "type", defaultValue = "all", required = false) FilterType type,
			@RequestParam(value = "value", required = false) String value) {
		Filter filter = new Filter(type, value);
		OrderSummary orderSummary = service.getOrdersSummary(filter);
		return orderSummary;
	}

}
