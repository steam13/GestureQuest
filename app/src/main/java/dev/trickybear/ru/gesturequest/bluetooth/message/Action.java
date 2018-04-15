package dev.trickybear.ru.gesturequest.bluetooth.message;

// TODO: 22/05/2017 Move to BillFold Core
//!!!Warning!!!
//Should be same with ru.studiomobile.billfold.monitor.model.entity.MonitorMessage from monitor project
public enum Action {
    TIPS_SIZE,
    ORDER_DATA,
    CLEAR_ORDER,
    ORDER_ITEM,
    PAYMENT_STATUS,
    EVENT_DATA,
    MONITOR_READY,
    ORDER_FORMED,
    ORDER_FORMED_QR,
    BACK_TO_ORDER,
    TRY_AGAIN_PAYMENT,
    BANNER_DATA,
    LAST_BANNER,
    RFID_DATA,
    UNKNOWN,
    DRINK_TICKET_DATA //action for message with drink ticket uid and pin
}
