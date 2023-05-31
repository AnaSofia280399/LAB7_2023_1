package com.example.lab7_2023_1.Repository;

import com.example.lab7_2023_1.Entity.Solicitudes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudesRepository  extends JpaRepository<Solicitudes, Integer> {
}
