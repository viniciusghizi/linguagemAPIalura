/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ghizi.java.controller;

import ghizi.java.model.Linguagem;
import ghizi.java.repository.LinguagemRepository;
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

    @GetMapping("/")
    public String processaLinguagemPreferida() {
        return "Mais informações: \n https://github.com/viniciusghizi/linguagemAPIalura#readme";
    }

    //GET no /linguagens
    @GetMapping("/linguagens")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Linguagem> obterLinguagens() {
        List<Linguagem> linguagens = repositorio.findAll();
        //Lista todas as linguagens no banco
        return linguagens;
    }

    //POST na URL /add-linguagem 
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @RequestMapping(
            value = "/add-linguagem",
            method = RequestMethod.POST,
            consumes = {"application/json"})
    public Linguagem addLinguagem(@RequestBody Linguagem linguagem) {
        //Insere uma linguagem, com Title, Image e Ranking
        repositorio.insert(linguagem);
        return linguagem;
    }

    //DEL na URL /delete-linguagem-id 
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
    @GetMapping("/existe-linguagem-id")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public Optional<Linguagem> getLinguagemPorID(@RequestBody Linguagem linguagem) {
        Optional<Linguagem> resultado = repositorio.findById(linguagem.getId());
        //Vai retornar o json do ID se existir, caso não, retorna NULL
        return resultado;
    }

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
