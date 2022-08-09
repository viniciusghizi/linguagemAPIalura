/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ghizi.java.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import ghizi.java.model.ERole;
import ghizi.java.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(ERole name);
}
