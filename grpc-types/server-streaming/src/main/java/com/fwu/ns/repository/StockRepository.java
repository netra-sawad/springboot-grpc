package com.fwu.ns.repository;

import com.fwu.ns.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockRepository extends JpaRepository<Stock, Long> , JpaSpecificationExecutor<Stock> {
}
