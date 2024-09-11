package com.pfa.repository;

import com.pfa.models.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Long> {
    // You can add custom query methods here if needed
}
