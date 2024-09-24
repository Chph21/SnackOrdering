package com.example.snackordering.service;

import com.example.snackordering.entity.AccountEntity;
import com.example.snackordering.entity.Order;
import com.example.snackordering.entity.Shipments;
import com.example.snackordering.model.shipments.ShipmentsRequest;
import com.example.snackordering.model.shipments.ShipmentsResponse;
import com.example.snackordering.repository.AccountRepository;
import com.example.snackordering.repository.OrderRepository;
import com.example.snackordering.repository.ShipmentsRepository;
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
public class ShipmentService {
    private final Logger LOGGER = LoggerFactory.getLogger(ShipmentService.class);
    private final ShipmentsRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;

    public List<ShipmentsResponse> findAll() {
        LOGGER.info("Find all shipments");
        List<Shipments> shipments = shipmentRepository.findAll();
        if (shipments.isEmpty()) {
            LOGGER.warn("No shipments were found!");
        }

        return shipments.stream()
                .map(this::shipmentResponseGenerator)
                .collect(Collectors.toList());
    }

    public ShipmentsResponse findById(Integer id) {
        LOGGER.info("Find shipment with id " + id);
        Optional<Shipments> shipment = shipmentRepository.findById(id);
        if (shipment.isEmpty()) {
            LOGGER.warn("No shipment was found!");
            return null;
        }
        return shipment.map(this::shipmentResponseGenerator).get();
    }

    public ShipmentsResponse save(ShipmentsRequest shipmentRequest) {
        Shipments shipment;
        Optional<Order> order = orderRepository.findById(shipmentRequest.getOrderId());
        Optional<AccountEntity> account = accountRepository.findById(String.valueOf(shipmentRequest.getAccountId()));
        if (order.isEmpty() || account.isEmpty()) {
            LOGGER.error("Order or account was not found!");
            throw new CustomValidationException(List.of("Order or account was not found!"));
        }

        if (shipmentRequest.getShipmentId() != null) {
            LOGGER.info("Update shipment with id " + shipmentRequest.getShipmentId());
            checkExist(shipmentRequest.getShipmentId());
            shipment = shipmentRepository.findById(shipmentRequest.getShipmentId()).get();
            updateShipment(shipment, shipmentRequest);
            shipmentRepository.save(shipment);
        } else {
            LOGGER.info("Create new shipment");
            shipment = createShipment(shipmentRequest, order.get(), account.get());
            shipmentRepository.save(shipment);
        }
        return shipmentResponseGenerator(shipment);
    }

    public void delete(Integer id) {
        if (id != null) {
            LOGGER.info("Delete shipment with id " + id);
            checkExist(id);
            Shipments shipment = shipmentRepository.findById(id).get();
            shipmentRepository.delete(shipment);
        }
    }

    private Shipments createShipment(ShipmentsRequest request, Order order, AccountEntity account) {
        Shipments shipment = new Shipments();
        setCommonFields(shipment, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        shipment.setOrder(order);
        shipment.setAccount(account);
        shipment.setCreatedBy(authentication.getName());
        return shipment;
    }

    private void updateShipment(Shipments shipment, ShipmentsRequest request) {
        setCommonFields(shipment, request);
        shipment.setOrder(orderRepository.findById(request.getOrderId()).get());
        shipment.setAccount(accountRepository.findById(String.valueOf(request.getAccountId())).get());
    }

    private void setCommonFields(Shipments shipment, ShipmentsRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        shipment.setShipmentDate(request.getShipmentDate());
        shipment.setUpdatedBy(authentication.getName());
    }

    private ShipmentsResponse shipmentResponseGenerator(Shipments shipment) {
        ShipmentsResponse shipmentResponse = new ShipmentsResponse();
        shipmentResponse.setShipmentId(shipment.getShipmentId());
        shipmentResponse.setOrderId(shipment.getOrder().getOrderId());
        shipmentResponse.setShipmentDate(shipment.getShipmentDate());
        shipmentResponse.setCreatedBy(shipment.getCreatedBy());
        shipmentResponse.setCreatedDate(shipment.getCreatedDate());
        shipmentResponse.setUpdatedDate(shipment.getUpdatedDate());
        shipmentResponse.setUpdatedBy(shipment.getUpdatedBy());
        return shipmentResponse;
    }

    private void checkExist(Integer id) {
        if (shipmentRepository.findById(id).isEmpty()) {
            LOGGER.error("No shipment was found!");
            throw new CustomValidationException(List.of("No shipment was found!"));
        }
    }
}