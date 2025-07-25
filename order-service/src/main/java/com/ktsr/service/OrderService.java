package com.ktsr.service;

import com.ktsr.DTO.OrderLineItemsDto;
import com.ktsr.DTO.OrderRequest;
import com.ktsr.model.Order;
import com.ktsr.model.OrderLineItems;
import com.ktsr.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order= new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems= orderRequest.getOrderLineItemsDto()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItems(orderLineItems);
        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
         OrderLineItems orderLineItems=new OrderLineItems();

        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
         orderLineItems.setPrice(orderLineItemsDto.getPrice());
         orderLineItems.setQuantity(orderLineItemsDto.getQuantity());

         return orderLineItems;
    }
}
