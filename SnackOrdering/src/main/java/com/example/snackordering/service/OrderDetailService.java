package com.example.snackordering.service;

import com.example.snackordering.entity.BranchFood;
import com.example.snackordering.entity.Order;
import com.example.snackordering.entity.OrderDetail;
import com.example.snackordering.model.orderDetail.OrderDetailRequest;
import com.example.snackordering.model.orderDetail.OrderDetailResponse;
import com.example.snackordering.repository.BranchFoodRepository;
import com.example.snackordering.repository.OrderDetailRepository;
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
public class OrderDetailService {
    private final Logger LOGGER = LoggerFactory.getLogger(OrderDetailService.class);
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final BranchFoodRepository branchFoodRepository;

    public List<OrderDetailResponse> findAll() {
        LOGGER.info("Find all order details");
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        if (orderDetails.isEmpty()) {
            LOGGER.warn("No order details were found!");
        }

        return orderDetails.stream()
                .map(this::orderDetailResponseGenerator)
                .collect(Collectors.toList());
    }

    public OrderDetailResponse findById(Integer id) {
        LOGGER.info("Find order detail with id " + id);
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
        if (orderDetail.isEmpty()) {
            LOGGER.warn("No order detail was found!");
            return null;
        }
        return orderDetail.map(this::orderDetailResponseGenerator).get();
    }

    public OrderDetailResponse save(OrderDetailRequest orderDetailRequest) {
        OrderDetail orderDetail;
        Optional<Order> order = orderRepository.findById(orderDetailRequest.getOrderId());
        Optional<BranchFood> branchFood = branchFoodRepository.findById(orderDetailRequest.getBranchFoodId());
        if (order.isEmpty() || branchFood.isEmpty()) {
            throw new CustomValidationException(List.of("No order or branch food was found!"));
        }

        if (orderDetailRequest.getOrderDetailId() != null) {
            LOGGER.info("Update order detail with id " + orderDetailRequest.getOrderDetailId());
            checkExist(orderDetailRequest.getOrderDetailId());
            orderDetail = orderDetailRepository.findById(orderDetailRequest.getOrderDetailId()).get();
            updateOrderDetail(orderDetail, orderDetailRequest);
            orderDetailRepository.save(orderDetail);
        } else {
            LOGGER.info("Create new order detail");
            orderDetail = createOrderDetail(orderDetailRequest, order.get(), branchFood.get());
            orderDetailRepository.save(orderDetail);
        }
        return orderDetailResponseGenerator(orderDetail);
    }

    public void delete(Integer id) {
        if (id != null) {
            LOGGER.info("Delete order detail with id " + id);
            checkExist(id);
            OrderDetail orderDetail = orderDetailRepository.findById(id).get();
            orderDetailRepository.delete(orderDetail);
        }
    }

    private OrderDetail createOrderDetail(OrderDetailRequest request, Order order, BranchFood branchFood) {
        OrderDetail orderDetail = new OrderDetail();
        setCommonFields(orderDetail, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderDetail.setOrder(order);
        orderDetail.setBranchFood(branchFood);
        orderDetail.setCreatedBy(authentication.getName());
        return orderDetail;
    }

    private void updateOrderDetail(OrderDetail orderDetail, OrderDetailRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setCommonFields(orderDetail, request);
        orderDetail.setOrder(orderRepository.findById(request.getOrderId()).get());
        orderDetail.setBranchFood(branchFoodRepository.findById(request.getBranchFoodId()).get());
        orderDetail.setUpdatedBy(authentication.getName());
    }

    private void setCommonFields(OrderDetail orderDetail, OrderDetailRequest request) {
        orderDetail.setQuantity(request.getQuantity());
    }

    private OrderDetailResponse orderDetailResponseGenerator(OrderDetail orderDetail) {
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setOrderDetailId(orderDetail.getOrderDetailId());
        orderDetailResponse.setOrderId(orderDetail.getOrder().getOrderId());
        orderDetailResponse.setBranchFoodId(orderDetail.getBranchFood().getBranchFoodId());
        orderDetailResponse.setQuantity(orderDetail.getQuantity());
        orderDetailResponse.setCreatedBy(orderDetail.getCreatedBy());
        orderDetailResponse.setCreatedDate(orderDetail.getCreatedDate());
        orderDetailResponse.setUpdatedDate(orderDetail.getUpdatedDate());
        orderDetailResponse.setUpdatedBy(orderDetail.getUpdatedBy());
        return orderDetailResponse;
    }

    private void checkExist(Integer id) {
        if (orderDetailRepository.findById(id).isEmpty()) {
            LOGGER.error("No order detail was found!");
            throw new CustomValidationException(List.of("No order detail was found!"));
        }
    }
}
