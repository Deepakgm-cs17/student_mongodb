package com.trustrace.springmongo.reporistory;

import com.trustrace.springmongo.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserModel,Long> {
    Optional<UserModel> findById(Integer id);
}
