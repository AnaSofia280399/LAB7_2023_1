package com.example.lab7_2023_1.Controller;

import com.example.lab7_2023_1.Entity.Solicitudes;
import com.example.lab7_2023_1.Entity.Usuarios;
import com.example.lab7_2023_1.Repository.SolicitudesRepository;
import com.example.lab7_2023_1.Repository.SolicitudesRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController //me añade el @ResponseBody
@CrossOrigin // para evitar bloqueos
@RequestMapping("solicitudes")
public class SolicitudesController {

    final SolicitudesRepository solicitudesRepository;
    public SolicitudesController(SolicitudesRepository solicitudesRepository){ this.solicitudesRepository = solicitudesRepository;}


    @PostMapping("/registro")
    public ResponseEntity<HashMap<String, Object>> guardaSolicitud(
            @RequestBody Solicitudes solicitudes,
            @RequestParam(value = "fetchId", required = false) boolean fetchId) {

        HashMap<String, Object> responseJson = new HashMap<>();

        // Set the pre-filled field value
        solicitudes.setSolicitud_estado("pendiente");

        solicitudesRepository.save(solicitudes);
        if(fetchId){
            responseJson.put("id",solicitudes.getId());
        }
        Double monto = solicitudes.getSolicitud_monto();
        int id = solicitudes.getId();

        responseJson.put("Monto Solicitado", monto);
        responseJson.put("id", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String,String>> gestionException(HttpServletRequest request){
        HashMap<String,String> responseMap = new HashMap<>();
        if(request.getMethod().equals("POST")){
            responseMap.put("estado","error");
            responseMap.put("msg","Debe enviar una solicitud");
        }
        return ResponseEntity.badRequest().body(responseMap);
    }
}
