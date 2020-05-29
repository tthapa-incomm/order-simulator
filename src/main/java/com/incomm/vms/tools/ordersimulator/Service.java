package com.incomm.vms.tools.ordersimulator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
public class Service {

    final private Backend backend;

    public Service(Backend backend) {
        this.backend = backend;
    }

    public String processShipment(List<String> orderIds, String simulationOutDirectory) {
        DateFormat df = new SimpleDateFormat("MMddyyyy");
        log.info("Generating shipping file...");
        List<String> shipReport = backend.getShippingDetails(orderIds);

        //write ship file
        StringBuilder sbShip = new StringBuilder("CUSTOMER,SOURCE_ONE_BATCH,PARENT_ORDER_ID," +
                "CHILD_ORDER_ID,FILE_DATE,SERIALNUMBER,CARDS,PACKAGE_ID,CARD_TYPE,CONTACT_NAME," +
                "SHIP_TO,ADDRESS_1,ADDRESS_2,CITY,STATE,ZIP,TRACKING_NUMBER,SHIP_DATE,SHIPMENT_ID," +
                "SHIP_METHOD").append("\n");
        if (!CollectionUtils.isEmpty(shipReport)) {
            for (String entry : shipReport) sbShip.append(entry).append("\n");
            String fileName = String.format("CPITN_VMS_%s_BOL_000%d_SHIP.csv",
                    df.format(new Date()), System.currentTimeMillis());
            writeFile(simulationOutDirectory + fileName, sbShip.toString(), simulationOutDirectory);
            log.info("Shipping file written");
            return simulationOutDirectory + fileName;
        } else {
            throw new RuntimeException(String.format("No records found for the specified order id(s): %s while creating shipping file", orderIds));
        }
    }

    public String processAck(List<String> orderIds, String simulationOutDirectory) {
        DateFormat df = new SimpleDateFormat("MMddyyyy");

        log.info("Generating ack file...");
        List<String> ackReport = backend.getAckDetails(orderIds);

        //write ack file
        StringBuilder sbAck = new StringBuilder("CUSTOMER,SHIPSUFFIX,PARENT_ORDER_ID," +
                "CHILD_ORDER_ID,SERIALNUMBER,REJECT_CODE,REJECT_REASON,FILE_DATE," +
                "CARD_TYPE,CLIENT_ORDER_ID").append("\n");
        if (!CollectionUtils.isEmpty(ackReport)) {
            for (String entry : ackReport) sbAck.append(entry).append("\n");
            String fileName = String.format("CPI_VMS_B2B_%s_000%d_ACK.csv",
                    df.format(new Date()), System.currentTimeMillis());
            writeFile(simulationOutDirectory + fileName, sbAck.toString(), simulationOutDirectory);
            return simulationOutDirectory + fileName;
        } else {
            throw new RuntimeException(String.format("No records found for the specified order id(s): %s while creating ack file", orderIds));
        }
    }

    private static void writeFile(String fileName, String content, String simulationOutDirectory) {
        File directory = new File(String.valueOf(simulationOutDirectory));
        if (!directory.exists()) {
            Boolean created = directory.mkdir();
            log.info(String.format("%s successfully created: %s", simulationOutDirectory, created.toString()));
        }
        try {
            File file = new File(fileName);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
            log.info("File created :: " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> fetchPendingOrders()
    {
        return backend.fetchpendingOrders();
    }
}
