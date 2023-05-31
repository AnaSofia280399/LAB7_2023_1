package com.example.lab7_2023_1.Controller;

import com.example.lab7_2023_1.Entity.Pagos;
import com.example.lab7_2023_1.Entity.Usuarios;
import com.example.lab7_2023_1.Repository.PagosRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //me añade el @ResponseBody
@RequestMapping("pagos")
public class PagosController {
    final PagosRepository pagosRepository;

    public PagosController(PagosRepository pagosRepository){this.pagosRepository = pagosRepository;}
    @GetMapping("/listarPagos")  //como no hay vista
    public List<Pagos> listarPagos(){
        List<Pagos> list = pagosRepository.findAll();
        return list;
    }
}
