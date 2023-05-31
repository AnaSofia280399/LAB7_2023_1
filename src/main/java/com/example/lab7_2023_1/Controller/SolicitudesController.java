package com.example.lab7_2023_1.Controller;

import com.example.lab7_2023_1.Entity.Solicitudes;
import com.example.lab7_2023_1.Entity.Usuarios;
import com.example.lab7_2023_1.Repository.SolicitudesRepository;
import com.example.lab7_2023_1.Repository.SolicitudesRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@RestController //me añade el @ResponseBody

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
        if(request.getMethod().equals("POST")|| request.getMethod().equals("PUT")){
            responseMap.put("estado","error");
            responseMap.put("msg","Debe enviar una solicitud");
        }
        return ResponseEntity.badRequest().body(responseMap);
    }

    @PutMapping(value = "/aprobarSolicitud" )
    public ResponseEntity<HashMap<String,Object>> actualizarSolicitud(@RequestBody Solicitudes solicitudes) {

        HashMap<String, Object> responseMap = new HashMap<>();

        if (solicitudes.getId() != null && solicitudes.getId() > 0) {
            Optional<Solicitudes> opt = solicitudesRepository.findById(solicitudes.getId());
            if (opt.isPresent()) {
                if ("pendiente".equals(opt.get().getSolicitud_estado())) {
                    solicitudes.setSolicitud_fecha(opt.get().getSolicitud_fecha());
                    solicitudes.setSolicitud_monto(opt.get().getSolicitud_monto());
                    solicitudes.setSolicitud_producto(opt.get().getSolicitud_producto());
                    solicitudes.setUsuarios_id(opt.get().getUsuarios_id());
                    solicitudes.setSolicitud_estado("aprobado");
                    solicitudesRepository.save(solicitudes);
                    int id = solicitudes.getId();
                    responseMap.put("id solicitud", id);
                    return ResponseEntity.ok(responseMap);
                } else{

                    int id = solicitudes.getId();
                    responseMap.put("Solicitud ya atendida", id);
                    return ResponseEntity.badRequest().body(responseMap);
                }

            } else {
                responseMap.put("estado", "error");
                responseMap.put("msg", "La solicitud a actualizar no existe");
                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Debe enviar un ID");
            return ResponseEntity.badRequest().body(responseMap);
        }
    }

    @PutMapping(value = "/denegarSolicitud")
    public ResponseEntity<HashMap<String,Object>> denegarSolicitud(@RequestBody Solicitudes solicitudes) {

        HashMap<String, Object> responseMap = new HashMap<>();

        if (solicitudes.getId() != null && solicitudes.getId() > 0) {
            Optional<Solicitudes> opt = solicitudesRepository.findById(solicitudes.getId());
            if (opt.isPresent()) {
                if ("pendiente".equals(opt.get().getSolicitud_estado())) {
                    solicitudes.setSolicitud_fecha(opt.get().getSolicitud_fecha());
                    solicitudes.setSolicitud_monto(opt.get().getSolicitud_monto());
                    solicitudes.setSolicitud_producto(opt.get().getSolicitud_producto());
                    solicitudes.setUsuarios_id(opt.get().getUsuarios_id());
                    solicitudes.setSolicitud_estado("denegada");
                    solicitudesRepository.save(solicitudes);
                    int id = solicitudes.getId();
                    responseMap.put("id solicitud", id);
                    return ResponseEntity.ok(responseMap);
                } else{

                    int id = solicitudes.getId();
                    responseMap.put("Solicitud ya atendida", id);
                    return ResponseEntity.badRequest().body(responseMap);
                }

            } else {
                responseMap.put("estado", "error");
                responseMap.put("msg", "La solicitud a actualizar no existe");
                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Debe enviar un ID");
            return ResponseEntity.badRequest().body(responseMap);
        }
    }

    @DeleteMapping(value = "/borrarSolicitud/{id}")
    public ResponseEntity<HashMap<String, Object>> borrarSolicitud(@PathVariable("id") String idStr) {

        HashMap<String, Object> responseMap = new HashMap<>();

        try {
            int id = Integer.parseInt(idStr);

            if (solicitudesRepository.existsById(id)) {
                Optional<Solicitudes> opt = solicitudesRepository.findById(id);

                if ("denegada".equals(opt.get().getSolicitud_estado())){

                    solicitudesRepository.deleteById(id);
                    responseMap.put("id solicitada borrada", id);
                    return ResponseEntity.ok(responseMap);
                } else{

                responseMap.put("Solicitud ", "Debe ser una solicitud denegada");
                return ResponseEntity.badRequest().body(responseMap);
                }

            } else {
                responseMap.put("estado", "error");
                responseMap.put("msg", "no se encontró la solicitud con id: " + id);
                return ResponseEntity.badRequest().body(responseMap);
            }
        } catch (NumberFormatException ex) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "El ID debe ser un número");
            return ResponseEntity.badRequest().body(responseMap);
        }
    }



}
