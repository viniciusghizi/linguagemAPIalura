/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ghizi.java.controller;

import ghizi.java.model.Linguagem;
import ghizi.java.repository.LinguagemRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ghizi
 */
@RestController
public class LinguagemController {

    @Autowired

    private LinguagemRepository repositorio;

    @ApiOperation(value = "Endpoint com o link para maiores informações")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retorna o Link do Github para maiores informações"),
        @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
        @ApiResponse(code = 500, message = "Foi gerada uma exceção"),})
    @RequestMapping(
            value = "/info",
            method = RequestMethod.GET,
            produces = "application/json")
    @GetMapping("/info")
    public String processaLinguagemPreferida() {
        return "Mais informações: \n https://github.com/viniciusghizi/linguagemAPIalura#readme";
    }

    //GET no /linguagens
    @ApiOperation(value = "Endpoint com todas as linguagens no Banco")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retorna todas as linguagens adicionadas no MongoDB"),
        @ApiResponse(code = 401, message = "Você não está logado com uma conta do tipo:"
                + "USER, MODERATOR ou ADMIN"),
        @ApiResponse(code = 500, message = "Foi gerada uma exceção"),})
    @RequestMapping(
            value = "/linguagens",
            method = RequestMethod.GET,
            produces = "application/json")
    @GetMapping("/linguagens")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Linguagem> obterLinguagens() {
        List<Linguagem> linguagens = repositorio.findAll();
        //Lista todas as linguagens no banco
        return linguagens;
    }

    //POST na URL /add-linguagem 
    @ApiOperation(value = "Endpoint para adicionar linguagem")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Adiciona a linguagem no banco "
                + "e retorna todas as linguagens"),
        @ApiResponse(code = 401, message = "Você não está logado com uma conta do tipo:"
                + "MODERATOR ou ADMIN"),
        @ApiResponse(code = 500, message = "Foi gerada uma exceção"),})
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(
            value = "/add-linguagem",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = "application/json")
    public Linguagem addLinguagem(@RequestBody Linguagem linguagem) {
        //Insere uma linguagem, com Title, Image e Ranking
        repositorio.insert(linguagem);
        return linguagem;
    }

    //DEL na URL /delete-linguagem-id 
    @ApiOperation(value = "Endpoint para deletar linguagem")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Deleta a linguagem no banco "
                + "e retorna mensagem de sucesso"),
        @ApiResponse(code = 401, message = "Você não está logado com uma conta do tipo:"
                + "MODERATOR ou ADMIN"),
        @ApiResponse(code = 500, message = "Foi gerada uma exceção"),})
    @DeleteMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(
            value = "/delete-linguagem-id",
            method = RequestMethod.DELETE,
            consumes = {"application/json"})
    public String removeLinguagem(@RequestBody Linguagem linguagem) throws CloneNotSupportedException {
        Optional<Linguagem> resultado = repositorio.findById(linguagem.getId());
        String respostaID = linguagem.getId();
        String body = "{\"message\":\"" + respostaID + " SUBSTITUIR\"}";
        if (!resultado.isEmpty()) { //Irá procurar se o resultado está vazio, senão estiver
            //fará o delete e imprimi a message
            repositorio.deleteById(linguagem.getId());
            //Retorna a variavel body, substituindo a string "SUBSTITUIR" pela mensagem de exito
            return body.replace("SUBSTITUIR", "removido com sucesso.");
        } else {

            //Retorna a variavel body, substituindo a string "SUBSTITUIR" pela mensagem de erro
            return body.replace("SUBSTITUIR", "não localizado em nossa fonte.");
        }

    }

    //Verifica se a linguagem existe pelo ID
    @ApiOperation(value = "Endpoint para verificar se existe a linguagem passando "
            + "ID pelo BODY da requisição")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Exibe: Editado com sucesso"),
        @ApiResponse(code = 401, message = "Você não está logado com uma conta do tipo:"
                + "MODERATOR ou ADMIN"),
        @ApiResponse(code = 500, message = "Foi gerada uma exceção"),})
    @GetMapping("/existe-linguagem-id")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(
            value = "/existe-linguagem-id",
            method = RequestMethod.GET,
            consumes = {"application/json"},
            produces = "application/json")
    public Optional<Linguagem> getLinguagemPorID(@RequestBody Linguagem linguagem) {
        Optional<Linguagem> resultado = repositorio.findById(linguagem.getId());
        //Vai retornar o json do ID se existir, caso não, retorna NULL
        return resultado;
    }

    @ApiOperation(value = "Endpoint para atualização parcial da linguagem passando "
            + "ID pelo BODY e pela URL da requisição")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Exibe a linguagem pelo ID"),
        @ApiResponse(code = 401, message = "Você não está logado com uma conta do tipo:"
                + "MODERATOR ou ADMIN"),
        @ApiResponse(code = 500, message = "Foi gerada uma exceção"),})
    @RequestMapping(
            value = "/atualizar/{id}",
            method = RequestMethod.PATCH,
            consumes = {"application/json"},
            produces = "application/json")
    @PatchMapping("/atualizar/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> update(@RequestBody Linguagem linguagem,
            @PathVariable("id") String id) {
        Optional<Linguagem> resultado = repositorio.findById(linguagem.getId());
        Linguagem linguagemEditada = resultado.get();
        if (linguagem.getTitle() != null) {
            linguagemEditada.setTitle(linguagem.getTitle());
        }
        if (linguagem.getImage() != null) {
            linguagemEditada.setImage(linguagem.getImage());
        }
        if (linguagem.getRanking() != 0) {
            linguagemEditada.setRanking(linguagem.getRanking());
        }
        repositorio.save(linguagemEditada);
        return ResponseEntity.ok("Editado com sucesso");
    }

}
