package com.example.lab7_2023_1.Controller;

import com.example.lab7_2023_1.Entity.Pagos;
import com.example.lab7_2023_1.Entity.Solicitudes;
import com.example.lab7_2023_1.Entity.Usuarios;
import com.example.lab7_2023_1.Repository.PagosRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @PostMapping("/registrarPago")
    public ResponseEntity<HashMap<String, Object>> registrarPago(
            @RequestBody Pagos pagos,
            @RequestParam(value = "fetchId", required = false) boolean fetchId) {

        HashMap<String, Object> responseJson = new HashMap<>();

        pagosRepository.save(pagos);
        if(fetchId){
            responseJson.put("id",pagos.getId());
        }
        int id = pagos.getId();
        responseJson.put("id", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String,String>> gestionException(HttpServletRequest request){
        HashMap<String,String> responseMap = new HashMap<>();
        if(request.getMethod().equals("POST")|| request.getMethod().equals("PUT")){
            responseMap.put("estado","error");
            responseMap.put("msg","Debe enviar un pago");
        }
        return ResponseEntity.badRequest().body(responseMap);
    }
}
