package com.example.lab7_2023_1.Repository;

import com.example.lab7_2023_1.Entity.Acciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccionesRepository extends JpaRepository<Acciones, Integer> {
}
