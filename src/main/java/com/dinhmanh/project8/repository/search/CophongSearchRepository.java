package com.dinhmanh.project8.repository.search;

import com.dinhmanh.project8.domain.Cophong;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Cophong entity.
 */
public interface CophongSearchRepository extends ElasticsearchRepository<Cophong, Long> {
}
