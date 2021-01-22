package com.xantrix.webapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xantrix.webapp.entity.Listini;

@Repository
public interface ListinoRepository extends CrudRepository<Listini, String> {

}
