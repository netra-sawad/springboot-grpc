package com.fwu.ns.service;

import com.fwu.ns.grpc.TradeStatus;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {

//    1. Unary client call
    /*@GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub stockClient;

    public com.fwu.ns.grpc.StockResponse getStock(String symbol) {
        com.fwu.ns.grpc.StockRequest request = StockRequest.newBuilder().setStockSymbol(symbol).build();
        return stockClient.getStockPrice(request);
    }*/


//    2. Server Streaming Client call
    /*
    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceStub stockClient;

    public void subscribeStockPrice(String symbol) {
        StockRequest request = StockRequest.newBuilder()
                .setStockSymbol(symbol)
                .build();
        stockClient.subscribeStockPrice(request, new StreamObserver<StockResponse>() {
            @Override
            public void onNext(StockResponse stockResponse) {
                System.out.println("Stock Price Update: " + stockResponse.getPrice() +
                        " Price: " + stockResponse.getPrice() + " " +
                        "Time :" + stockResponse.getTimestamp());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Stock Price Update: Completed");
            }
        });
    }*/

//    3. Client Streaming Client Call

/*
@GrpcClient("stockService")
private StockTradingServiceGrpc.StockTradingServiceStub stockClient;

public void placeBulkOrders(){
    StreamObserver<com.fwu.ns.grpc.OrderSummary> responseObserver = new StreamObserver<OrderSummary>() {
        @Override
        public void onNext(OrderSummary orderSummary) {
            System.out.println("Order Summary Received from Server : ");
            System.out.println("Total Orders: "+ orderSummary.getTotalOrders());
            System.out.println("Successful Orders: " + orderSummary.getSuccessCount());
            System.out.println("Total Amount: $" + orderSummary.getTotalAmount());
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("Error Occured : "+ throwable.getMessage());
        }

        @Override
        public void onCompleted() {
            System.out.println("Stream Completed");
        }
    };
   StreamObserver<com.fwu.ns.grpc.StockOrder> requestObserver = stockClient.bulkStockOrder(responseObserver);

//    send multiple stream of stock order message/request
    try{
        requestObserver.onNext(com.fwu.ns.grpc.StockOrder.newBuilder()
                        .setOrderId("1")
                        .setStockSymbol("NBL")
                        .setPrice(149.3)
                        .setQuantity(10)
                        .build());
        requestObserver.onNext(com.fwu.ns.grpc.StockOrder.newBuilder()
                .setOrderId("2")
                .setStockSymbol("GBL")
                .setPrice(158)
                .setQuantity(10)
                .build());
        requestObserver.onNext(com.fwu.ns.grpc.StockOrder.newBuilder()
                .setOrderId("3")
                .setStockSymbol("FBL")
                .setPrice(205.6)
                .setQuantity(10)
                .build());
        requestObserver.onNext(com.fwu.ns.grpc.StockOrder.newBuilder()
                .setOrderId("5")
                .setStockSymbol("RBL")
                .setPrice(205.9)
                .setQuantity(10)
                .build());
//        done sending orders
requestObserver.onCompleted();
    } catch (Exception ex){
  requestObserver.onError(ex);
    }
}
*/

//    4. Bidirectional streaming client call

    @GrpcClient("stockService")
    private com.fwu.ns.grpc.StockTradingServiceGrpc.StockTradingServiceStub stockClient;

    public void startLiveTrading(){
      StreamObserver<com.fwu.ns.grpc.StockOrder> requestObserver=   stockClient.liveTrading(new StreamObserver<TradeStatus>() {

//            for every response this onNext will be called
            @Override
            public void onNext(TradeStatus tradeStatus) {
                System.out.println("Server Response: " + tradeStatus);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Server Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Server Completed");
            }
        });

//      Sending multiple order request from client

        for (int i=1; i<=10; i++){
            com.fwu.ns.grpc.StockOrder stockOrder = com.fwu.ns.grpc.StockOrder.newBuilder()
                    .setOrderId("ORDER-"+i)
                    .setStockSymbol("APPL")
                    .setQuantity(i*10)
                    .setPrice(150.0 +i)
                    .setOrderType("BUY")
                    .build();

            requestObserver.onNext(stockOrder);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        requestObserver.onCompleted();
    }

}
