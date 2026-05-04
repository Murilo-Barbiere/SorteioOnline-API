package com.progWeb.SorteioOnline.controller;

import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.DTO.request.SorteioRequestDTO;
import com.progWeb.SorteioOnline.model.SorteioModel;
import com.progWeb.SorteioOnline.service.SorteioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sorteio")
public class SorteioController {

    private SorteioService sorteioService;

    public SorteioController(SorteioService sorteioService) {
        this.sorteioService = sorteioService;
    }

    @PostMapping
    public ResponseEntity<SorteioModel> cria(@AuthenticationPrincipal JWTUserData jwtUserData,
                                             @RequestBody SorteioRequestDTO dadosSorteio){
        SorteioModel novoSorteio = sorteioService.addSorteio(jwtUserData, dadosSorteio);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoSorteio);
    }

    @GetMapping("/lista_sorteios")
    public List<SorteioModel> mostrarSorteios(){
        return sorteioService.listSorteios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SorteioModel> mostraSorteio(@PathVariable("id") Long id){
        return ResponseEntity.of(sorteioService.getSorteio(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarSorteio(@PathVariable("id") Long idSorteio,
                                                 @AuthenticationPrincipal JWTUserData userLogadoJWT){
        boolean deletado = sorteioService.deleteSorteio(idSorteio, userLogadoJWT);

        if(! deletado){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User nao encontrado");
        }
        return ResponseEntity.ok("User deletado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> alteraDadosSorteio(@PathVariable Long id,
                                                     @RequestBody SorteioRequestDTO dados,
                                                     @AuthenticationPrincipal JWTUserData userLogadoJWT){
        boolean atualizado = sorteioService.atualiza(id, dados, userLogadoJWT);

        if(!atualizado) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nao autorizado");

        return ResponseEntity.ok("Sorteio atualizado com sucesso!");
    }

    @PatchMapping("encerrado/{id}")
    public ResponseEntity<String> encerrarSorteio(@PathVariable("id") Long id,
                                                      @AuthenticationPrincipal JWTUserData userData){
        sorteioService.encerrar(id, userData);
        return ResponseEntity.ok("sorteio encerrado");
    }
}