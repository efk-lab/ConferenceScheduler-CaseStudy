package com.n11.conference.scheduler.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.n11.conference.scheduler.document.Conference;

@Repository
public interface ConferenceRepository extends MongoRepository<Conference, ObjectId> {

}
