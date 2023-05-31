package com.example.lab7_2023_1.Controller;

import com.example.lab7_2023_1.Entity.Acciones;
import com.example.lab7_2023_1.Entity.Pagos;
import com.example.lab7_2023_1.Repository.AccionesRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController //me añade el @ResponseBody

@RequestMapping("acciones")
public class AccionesController {
    final AccionesRepository accionesRepository;

    public AccionesController(AccionesRepository accionesRepository){this.accionesRepository = accionesRepository;}

    @PostMapping("/save")
    public ResponseEntity<HashMap<String, Object>> guardarAcciones(
            @RequestBody Acciones acciones,
            @RequestParam(value = "fetchId", required = false) boolean fetchId) {

        HashMap<String, Object> responseJson = new HashMap<>();

        accionesRepository.save(acciones);
        if(fetchId){
            responseJson.put("id",acciones.getId());
        }
        int id = acciones.getId();
        responseJson.put("idCreado", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String,String>> gestionException(HttpServletRequest request){
        HashMap<String,String> responseMap = new HashMap<>();
        if(request.getMethod().equals("POST")|| request.getMethod().equals("PUT")){
            responseMap.put("estado","error");
            responseMap.put("msg","Debe enviar una accion");
        }
        return ResponseEntity.badRequest().body(responseMap);
    }
}
