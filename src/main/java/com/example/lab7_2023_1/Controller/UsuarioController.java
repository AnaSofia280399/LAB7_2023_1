package com.example.lab7_2023_1.Controller;


import com.example.lab7_2023_1.Entity.Usuarios;
import com.example.lab7_2023_1.Repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController //me añade el @ResponseBody
@CrossOrigin // para evitar bloqueos
@RequestMapping("/users")

public class UsuarioController {
    final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/listar")  //como no hay vista
    public List<Usuarios> listar(){
        List<Usuarios> list = usuarioRepository.findAll();
        return list;
    }

    @PostMapping("/crear")
    public ResponseEntity<HashMap<String, Object>> guardarUsuario(
            @RequestBody Usuarios usuarios,
            @RequestParam(value = "fetchId", required = false) boolean fetchId) {

        HashMap<String, Object> responseJson = new HashMap<>();

        usuarioRepository.save(usuarios);
        if(fetchId){
            responseJson.put("id",usuarios.getId());
        }
        int id = usuarios.getId();
        responseJson.put("id creado", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String,String>> gestionException(HttpServletRequest request){
        HashMap<String,String> responseMap = new HashMap<>();
        if(request.getMethod().equals("POST")){
            responseMap.put("estado","error");
            responseMap.put("msg","Debe enviar un usuario");
        }
        return ResponseEntity.badRequest().body(responseMap);
    }



}
