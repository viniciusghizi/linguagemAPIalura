/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ghizi.java.repository;

import model.Linguagem;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author ghizi
 */

public interface LinguagemRepository extends MongoRepository<Linguagem, String>{
    
}
