package com.fwu.ns;

import com.fwu.ns.service.StockClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.SortedMap;

@SpringBootApplication
public class StockTradingClientApplication implements CommandLineRunner {
	private StockClientService stockClientService;

    public StockTradingClientApplication(StockClientService stockClientService) {
        this.stockClientService = stockClientService;
    }

    public static void main(String[] args) {
		SpringApplication.run(StockTradingClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/***
//		1. Call for the unary
		System.out.println("Grpc server response " + stockClientService.getStock("Nbl"));
		****/
//		2. call for server streaming
/*
		stockClientService.subscribeStockPrice("NBL");
*/
//		3. call client streaming
//		stockClientService.placeBulkOrders();

//		4. Bidirectional streaming
		stockClientService.startLiveTrading();
	}
}
