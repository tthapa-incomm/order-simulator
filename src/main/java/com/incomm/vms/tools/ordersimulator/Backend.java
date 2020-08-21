package com.incomm.vms.tools.ordersimulator;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;

public class Backend {

    final private NamedParameterJdbcTemplate jdbcTemplate;

    public Backend(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getShippingDetails(List<String> orderIds) {
        String SHIP_SQL = "  select 'Incomm'||','||'1454123'||','||vod_parent_oid||','||vod_order_id||','||TO_CHAR(sysdate, 'MM/DD/YYYY')||','||cap_serial_number||','||'1'||','||vol_package_id||','||'GC '||''||','||'Incomm'||','||'Incomm'||','||'250 Williams Street Suite 500'||','||','||'Atlanta'||','||'GA'||','||'30303'||','||'122816215025810'||','||TO_CHAR(sysdate, 'MM/DD/YYYY')||','||'N/A'||',1' as details\n" +
                "  from vmscms.VMS_ORDER_DETAILS,vmscms.VMS_ORDER_LINEITEM,vmscms.VMS_LINE_ITEM_DTL,vmscms.cms_appl_pan,VMSCMS.VMS_SHIPMENT_TRAN_MAST\n" +
                "  where vod_order_id=vol_order_id\n" +
                "  and vod_partner_id=vol_partner_id\n" +
                "  and vod_order_id=vli_order_id\n" +
                "  and vod_partner_id=vli_partner_id\n" +
                "  and vol_order_id=vli_order_id\n" +
                "  and vol_line_item_id=vli_lineitem_id\n" +
                "  and vol_partner_id=vli_partner_id\n" +
                "  and vod_parent_oid=vol_parent_oid\n" +
                "  and cap_pan_code=vli_pan_code\n" +
                "  and vod_shiPPING_METHOD=VSM_SHIPMENT_KEY\n" +
                "  and vod_order_id IN (:orderIds)\n" +
                "  order by cap_serial_number asc";

        return jdbcTemplate.queryForList(SHIP_SQL, new MapSqlParameterSource("orderIds", orderIds), String.class);
    }

    public List<String> getAckDetails(List<String> orderIds) {
        String ACK_SQL = "select 'INCOMM'||','||'123456'||','||vod_parent_oid||','||vod_order_id||','||cap_serial_number||','||'1'||','||'VALID'||','||TO_CHAR(sysdate, 'MM/DD/YYYY')||','||'AMEX' as Details\n" +
                "  from vmscms.VMS_ORDER_DETAILS od,vmscms.VMS_ORDER_LINEITEM,vmscms.VMS_LINE_ITEM_DTL dtl,vmscms.cms_appl_pan pp\n" +
                "  where vod_order_id=vol_order_id\n" +
                "  and vod_partner_id=vol_partner_id\n" +
                "  and vod_order_id=vli_order_id\n" +
                "  and vod_partner_id=vli_partner_id\n" +
                "  and vol_order_id=vli_order_id\n" +
                "  and vol_line_item_id=vli_lineitem_id\n" +
                "  and vol_partner_id=vli_partner_id\n" +
                "  and vod_parent_oid=vol_parent_oid\n" +
                "  and cap_pan_code=vli_pan_code\n" +
                "  and vod_order_id IN (:orderIds)\n" +
                "  order by cap_serial_number asc";

        return jdbcTemplate.queryForList(ACK_SQL, new MapSqlParameterSource("orderIds", orderIds), String.class);
    }

    public List<String> fetchpendingOrders() {

        String PENDING_ORDER_ID_SQL = "SELECT VOD_ORDER_ID FROM VMSCMS.VMS_ORDER_DETAILS WHERE VOD_ORDER_STATUS = 'CCF_Generated'";

        return jdbcTemplate.queryForList(PENDING_ORDER_ID_SQL, new MapSqlParameterSource(Collections.emptyMap()), String.class);
    }
}
