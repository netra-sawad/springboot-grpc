package com.fwu.ns.grpcservice;

import com.fwu.ns.grpc.OrderSummary;
import com.fwu.ns.grpc.StockOrder;
import com.fwu.ns.grpc.StockTradingServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {
    @Override
    public StreamObserver<StockOrder> bulkStockOrder(StreamObserver<OrderSummary> responseObserver) {
        return new StreamObserver<>() {
            private int totalOrders = 0;
            private double totalAmount = 0;
            private int successCount=0;
            @Override
            public void onNext(StockOrder stockOrder) {
                totalOrders++;
                totalAmount+=stockOrder.getPrice()*stockOrder.getQuantity();
                successCount++;
                System.out.println("Received order: " + stockOrder);

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
               OrderSummary orderSummary= OrderSummary.newBuilder().setTotalOrders(totalOrders).setTotalAmount(totalAmount).setSuccessCount(successCount).build();
                responseObserver.onNext(orderSummary);
                responseObserver.onCompleted();
            }
        };
    }
}
