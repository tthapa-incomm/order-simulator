package com.incomm.vms.tools.ordersimulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    private Service service;

    @Value("${return.file.path}")
    private String returnFilePath;

    @Value("${shipment.file.path}")
    private String shipmentFilePath;

    @PostMapping(value = "/tools/b2b-orders/complete",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> processAck(@RequestBody List<String> orderIds) {

        if (null == orderIds || orderIds.isEmpty()) {
            throw new RuntimeException("Order IDs missing");
        }

        String returnFile = service.processAck(orderIds, returnFilePath);
        String shipmentFile = service.processShipment(orderIds, shipmentFilePath);

        return ResponseEntity.ok(Arrays.asList(returnFile, shipmentFile));
    }
}
