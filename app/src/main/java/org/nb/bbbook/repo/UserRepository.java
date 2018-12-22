package org.nb.bbbook.repo;

import org.nb.bbbook.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

}