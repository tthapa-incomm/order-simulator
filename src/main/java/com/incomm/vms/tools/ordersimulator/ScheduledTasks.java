package com.incomm.vms.tools.ordersimulator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {

    private final Service service;

    @Value("${return.file.path}")
    private String returnFilePath;

    @Value("${shipment.file.path}")
    private String shipmentFilePath;

    public ScheduledTasks(Service service) {
        this.service = service;
    }


    @Scheduled(cron = "${cron.expression}")
    public void generateFiles() {

        log.info("Order Simulator Scheduler started ");
        List<String> orderIds = service.fetchPendingOrders();
        processFiles(orderIds);
        log.info("Shipment and response files generated successfully ");
    }

    private void processFiles(List<String> orderIds) {
        service.processAck(orderIds, returnFilePath);
        service.processShipment(orderIds, shipmentFilePath);
    }
}
