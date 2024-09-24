package com.example.snackordering.service;

import com.example.snackordering.entity.*;
import com.example.snackordering.model.order.OrderRequest;
import com.example.snackordering.model.order.OrderResponse;
import com.example.snackordering.repository.AccountRepository;
import com.example.snackordering.repository.BranchRepository;
import com.example.snackordering.repository.OrderRepository;
import com.example.snackordering.util.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final BranchRepository branchRepository;
    private final AccountRepository accountRepository;

    public List<OrderResponse> findAll() {
        LOGGER.info("Find all orders");
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            LOGGER.warn("No orders were found!");
        }

        return orders.stream()
                .map(this::orderResponseGenerator)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        LOGGER.info("Find order with id " + id);
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            LOGGER.warn("No order was found!");
            return null;
        }
        return order.map(this::orderResponseGenerator).get();
    }

    public OrderResponse save(OrderRequest orderRequest) {
        Order order;
        Optional<Branch> branch = branchRepository.findById(orderRequest.getBranchId());
        Optional<AccountEntity> account = accountRepository.findById(String.valueOf(orderRequest.getAccountId()));
        if (branch.isEmpty() || account.isEmpty()) {
            throw new CustomValidationException(List.of("No branch or food was found!"));
        }

        if (orderRequest.getOrderId() != null) {
            LOGGER.info("Update order with id " + orderRequest.getOrderId());
            checkExist(orderRequest.getOrderId());
            order = orderRepository.findById(orderRequest.getOrderId()).get();
            updateOrder(order, orderRequest);
            orderRepository.save(order);
        } else {
            LOGGER.info("Create new order");
            order = createOrder(orderRequest, branch.get(), account.get());
            orderRepository.save(order);
        }
        return orderResponseGenerator(order);
    }

    public void delete(Integer id) {
        if (id != null) {
            LOGGER.info("Delete order with id " + id);
            checkExist(id);
            Order order = orderRepository.findById(id).get();
            orderRepository.delete(order);
        }
    }

    private Order createOrder(OrderRequest request, Branch branch, AccountEntity account) {
        Order order = new Order();
        setCommonFields(order, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        order.setCreatedBy(authentication.getName());
        order.setBranch(branch);
        order.setAccount(account);
        return order;
    }

    private void updateOrder(Order order, OrderRequest request) {
        setCommonFields(order, request);
        Branch branch = branchRepository.findById(request.getBranchId()).get();
        AccountEntity account = accountRepository.findById(String.valueOf(request.getAccountId())).get();
        order.setBranch(branch);
        order.setAccount(account);
    }

    private void setCommonFields(Order order, OrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        order.setTotalMoney(request.getTotalMoney());
        order.setStatus(request.getStatus());
        order.setUpdatedBy(authentication.getName());
        order.setUpdatedDate(new Date());
    }

    private OrderResponse orderResponseGenerator(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setTotalMoney(order.getTotalMoney());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setCreatedBy(order.getCreatedBy());
        orderResponse.setCreatedDate(order.getCreatedDate());
        orderResponse.setUpdatedDate(order.getUpdatedDate());
        orderResponse.setUpdatedBy(order.getUpdatedBy());
        orderResponse.setAccountId(order.getAccount().getAccountId());
        orderResponse.setBranchId(order.getBranch().getBranchId());
        return orderResponse;
    }

    private void checkExist(Integer id) {
        if (orderRepository.findById(id).isEmpty()) {
            LOGGER.error("No order was found!");
            throw new CustomValidationException(List.of("No order was found!"));
        }
    }
}