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

    @PutMapping(value = "/aprobarSolicitud")
    public ResponseEntity<HashMap<String, Object>> actualizarSolicitud(@RequestParam(value = "idSolicitud", required = false) String idParam) {
        HashMap<String, Object> responseMap = new HashMap<>();

        if (idParam == null || idParam.isEmpty()) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Debe enviar el parámetro 'idSolicitud'");
            return ResponseEntity.badRequest().body(responseMap);
        }

        try {
            int id = Integer.parseInt(idParam);

            if (id > 0) {
                Optional<Solicitudes> opt = solicitudesRepository.findById(id);
                if (opt.isPresent()) {
                    Solicitudes solicitudes = opt.get();
                    if ("pendiente".equals(solicitudes.getSolicitud_estado())) {
                        solicitudes.setSolicitud_estado("aprobado");
                        solicitudesRepository.save(solicitudes);
                        responseMap.put("id solicitud", id);
                        return ResponseEntity.ok(responseMap);
                    } else {
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
                responseMap.put("msg", "Debe enviar un ID válido");
                return ResponseEntity.badRequest().body(responseMap);
            }
        } catch (NumberFormatException e) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "El ID de solicitud debe ser un número válido");
            return ResponseEntity.badRequest().body(responseMap);
        }
    }

    @PutMapping(value = "/denegarSolicitud")
    public ResponseEntity<HashMap<String, Object>> denegarSolicitud(@RequestParam(value = "idSolicitud", required = false) String idParam) {
        HashMap<String, Object> responseMap = new HashMap<>();

        if (idParam == null || idParam.isEmpty()) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Debe enviar el parámetro 'idSolicitud'");
            return ResponseEntity.badRequest().body(responseMap);
        }

        try {
            int id = Integer.parseInt(idParam);

            if (id > 0) {
                Optional<Solicitudes> opt = solicitudesRepository.findById(id);
                if (opt.isPresent()) {
                    Solicitudes solicitudes = opt.get();
                    if ("pendiente".equals(solicitudes.getSolicitud_estado())) {
                        solicitudes.setSolicitud_estado("denegada");
                        solicitudesRepository.save(solicitudes);
                        responseMap.put("id solicitud", id);
                        return ResponseEntity.ok(responseMap);
                    } else {
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
                responseMap.put("msg", "Debe enviar un ID válido");
                return ResponseEntity.badRequest().body(responseMap);
            }
        } catch (NumberFormatException e) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "El ID de solicitud debe ser un número válido");
            return ResponseEntity.badRequest().body(responseMap);
        }
    }

    @DeleteMapping(value = "/borrarSolicitud")
    public ResponseEntity<HashMap<String, Object>> borrarSolicitud(@RequestParam(value = "idSolicitud", required = false) String idParam) {
        HashMap<String, Object> responseMap = new HashMap<>();


        if (idParam == null || idParam.isEmpty()) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Debe enviar el parámetro 'idSolicitud'");
            return ResponseEntity.badRequest().body(responseMap);
        }

        try {
            int id = Integer.parseInt(idParam);

            if (id > 0) {
                if (solicitudesRepository.existsById(id)) {
                    Optional<Solicitudes> opt = solicitudesRepository.findById(id);

                    if ("denegada".equals(opt.get().getSolicitud_estado())) {
                        solicitudesRepository.deleteById(id);
                        responseMap.put("id solicitud borrada", id);
                        return ResponseEntity.ok(responseMap);
                    } else {
                        responseMap.put("estado", "error");
                        responseMap.put("msg", "La solicitud debe estar denegada para poder ser borrada");
                        return ResponseEntity.badRequest().body(responseMap);
                    }
                } else {
                    responseMap.put("estado", "error");
                    responseMap.put("msg", "No se encontró la solicitud con ID: " + id);
                    return ResponseEntity.badRequest().body(responseMap);
                }
            } else {
                responseMap.put("estado", "error");
                responseMap.put("msg", "Debe enviar un ID válido");
                return ResponseEntity.badRequest().body(responseMap);
            }
        } catch (NumberFormatException ex) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "El ID de solicitud debe ser un número válido");
            return ResponseEntity.badRequest().body(responseMap);
        }
    }



}
