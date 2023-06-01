package com.example.lab7_2023_1.Repository;

import com.example.lab7_2023_1.Entity.Pagos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagosRepository extends JpaRepository <Pagos, Integer> {
}
