package com.maestria.gestion.hoja_de_vida.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maestria.gestion.hoja_de_vida.dto.request.CrearSesionDemoRequestDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.PerfilAccesoDemoDTO;
import com.maestria.gestion.hoja_de_vida.dto.response.SesionDemoResponseDTO;
import com.maestria.gestion.hoja_de_vida.security.demo.AccesoDemoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/demo/auth")
@RequiredArgsConstructor
@Profile("demo & !prod")
@ConditionalOnProperty(prefix = "app.demo-auth", name = "enabled", havingValue = "true")
@Tag(name = "Acceso de demostración", description = "Sesiones temporales sin el microservicio de autenticación.")
public class AccesoDemoController {

    private final AccesoDemoService accesoDemoService;

    @GetMapping("/perfiles")
    @Operation(
            summary = "Listar perfiles de demostración",
            description = "Retorna únicamente los perfiles temporales configurados para la sesión de pruebas.")
    @ApiResponse(responseCode = "200", description = "Perfiles disponibles.")
    public ResponseEntity<List<PerfilAccesoDemoDTO>> listarPerfiles() {
        return ResponseEntity.ok(accesoDemoService.listarPerfiles());
    }

    @PostMapping("/token")
    @Operation(
            summary = "Crear una sesión de demostración",
            description = "Emite un JWT temporal para uno de los perfiles permitidos. "
                    + "Solo existe cuando están activos el perfil demo y la propiedad de habilitación.")
    @ApiResponse(responseCode = "200", description = "Sesión temporal creada.")
    public ResponseEntity<SesionDemoResponseDTO> crearSesion(
            @Valid @RequestBody CrearSesionDemoRequestDTO request) {
        return ResponseEntity.ok(accesoDemoService.crearSesion(request.getPerfil()));
    }
}
