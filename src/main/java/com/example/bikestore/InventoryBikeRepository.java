package com.example.bikestore;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryBikeRepository extends JpaRepository<InventoryBike,Long> {
}
