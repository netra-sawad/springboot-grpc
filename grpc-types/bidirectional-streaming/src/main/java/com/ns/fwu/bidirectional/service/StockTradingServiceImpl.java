package com.ns.fwu.bidirectional.service;

import com.fwu.ns.grpc.StockOrder;
import com.fwu.ns.grpc.StockTradingServiceGrpc;
import com.fwu.ns.grpc.TradeStatus;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {

    @Override
    public StreamObserver<StockOrder> liveTrading(StreamObserver<TradeStatus> responseObserver) {
        return new StreamObserver<StockOrder>() {
            @Override
            public void onNext(StockOrder stockOrder) {
                System.out.println("Received stock order: " + stockOrder);

                String status =  "EXECUTED";
                String message = "Order placed successfully";

                if (stockOrder.getQuantity()<=0){
                    status =  "FAILED";
                    message = "Invalid stock order quantity";
                }
               TradeStatus tradeStatus= TradeStatus.newBuilder()
                        .setOrderId(stockOrder.getOrderId())
                        .setStatus(status).setMessage(message)
                        .setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
                        .build();
                responseObserver.onNext(tradeStatus);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("error " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
