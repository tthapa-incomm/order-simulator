package com.incomm.vms.tools.ordersimulator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    private Service service;

    @Value("${return.file.path}")
    private String returnFilePath;

    @Value("${shipment.file.path}")
    private String shipmentFilePath;


    @Scheduled(cron = "${cron.expression}")
    public void generateFiles() {

        log.info("Order Simulator Scheduler started ");
        List<String> orderIds = service.fetchPendingOrders();
        processFiles(orderIds);
        log.info("Shipment and response files generated successfully ");
    }

    private void processFiles(List<String> orderIds)
    {
        String returnFile = service.processAck(orderIds, returnFilePath);
        String shipmentFile = service.processShipment(orderIds, shipmentFilePath);
    }
}
