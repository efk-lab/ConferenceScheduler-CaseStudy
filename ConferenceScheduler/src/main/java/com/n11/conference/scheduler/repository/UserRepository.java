package com.n11.conference.scheduler.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.n11.conference.scheduler.document.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
	
	Optional<User> findByEmail(String email);
	
}