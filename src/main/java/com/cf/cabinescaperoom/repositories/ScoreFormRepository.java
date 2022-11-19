package com.cf.cabinescaperoom.repositories;

import com.cf.cabinescaperoom.models.ScoreForm;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScoreFormRepository extends CrudRepository<ScoreForm, Long> {

    List<ScoreForm> findAll(Sort stars);
}
