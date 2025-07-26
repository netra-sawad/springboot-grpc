package com.fwu.ns.grpcservice;

import com.fwu.ns.entity.Stock;
import com.fwu.ns.grpc.StockRequest;
import com.fwu.ns.grpc.StockResponse;
import com.fwu.ns.grpc.StockTradingServiceGrpc;
import com.fwu.ns.repository.StockRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {

  private final StockRepository stockRepository;
    @Override
    public void getStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
//        StockName -> DB -> Map response -> return
        String stockSymbol = request.getStockSymbol();

        Stock stock = stockRepository.findByStockSymbol(stockSymbol);

       StockResponse stockResponse=  StockResponse.newBuilder()
                .setStockSymbol(stock.getStockSymbol())
                .setPrice(stock.getPrice())
                .setTimestamp(stock.getUpdatedAt().toString())
                .build();

        responseObserver.onNext(stockResponse);
        responseObserver.onCompleted();
    }
}
