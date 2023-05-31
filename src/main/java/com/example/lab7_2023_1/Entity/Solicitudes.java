package com.example.lab7_2023_1.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
@Getter
@Setter
public class Solicitudes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "solicitud_producto",  length = 45)
    private String solicitud_producto;

    @Column(name = "solicitud_monto")
    private Double solicitud_monto;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime solicitud_fecha;

    @ManyToOne
    @JoinColumn(name = "usuarios_id", nullable = false)
    private Usuarios usuarios_id;

    @Column(name = "solicitud_estado",  length = 45)
    private String solicitud_estado;


}
