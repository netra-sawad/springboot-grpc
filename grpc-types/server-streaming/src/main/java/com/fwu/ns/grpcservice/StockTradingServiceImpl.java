package com.fwu.ns.grpcservice;

import com.fwu.ns.grpc.StockRequest;
import com.fwu.ns.grpc.StockResponse;
import com.fwu.ns.grpc.StockTradingServiceGrpc;
import com.fwu.ns.repository.StockRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@GrpcService
@RequiredArgsConstructor
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {
  private final StockRepository stockRepository;
    @Override
    public void subscribeStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        String symbol = request.getStockSymbol();

        try {
        for (int i = 0; i<=10; i++) {
            StockResponse response = StockResponse.newBuilder()
                    .setStockSymbol(symbol)
                    .setPrice(new Random().nextDouble(200))
                    .setTimestamp(Instant.now().toString())
                    .build();
            responseObserver.onNext(response);
            TimeUnit.SECONDS.sleep(1);
        }
               responseObserver.onCompleted();
            } catch (InterruptedException e) {
                responseObserver.onError(e);
            }
    }
}
