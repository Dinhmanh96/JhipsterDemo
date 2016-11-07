package com.dinhmanh.project8.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dinhmanh.project8.domain.Cophong;

import com.dinhmanh.project8.repository.CophongRepository;
import com.dinhmanh.project8.repository.search.CophongSearchRepository;
import com.dinhmanh.project8.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Cophong.
 */
@RestController
@RequestMapping("/api")
public class CophongResource {

    private final Logger log = LoggerFactory.getLogger(CophongResource.class);
        
    @Inject
    private CophongRepository cophongRepository;

    @Inject
    private CophongSearchRepository cophongSearchRepository;

    /**
     * POST  /cophongs : Create a new cophong.
     *
     * @param cophong the cophong to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cophong, or with status 400 (Bad Request) if the cophong has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cophongs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cophong> createCophong(@Valid @RequestBody Cophong cophong) throws URISyntaxException {
        log.debug("REST request to save Cophong : {}", cophong);
        if (cophong.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cophong", "idexists", "A new cophong cannot already have an ID")).body(null);
        }
        Cophong result = cophongRepository.save(cophong);
        cophongSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cophongs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cophong", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cophongs : Updates an existing cophong.
     *
     * @param cophong the cophong to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cophong,
     * or with status 400 (Bad Request) if the cophong is not valid,
     * or with status 500 (Internal Server Error) if the cophong couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cophongs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cophong> updateCophong(@Valid @RequestBody Cophong cophong) throws URISyntaxException {
        log.debug("REST request to update Cophong : {}", cophong);
        if (cophong.getId() == null) {
            return createCophong(cophong);
        }
        Cophong result = cophongRepository.save(cophong);
        cophongSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cophong", cophong.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cophongs : get all the cophongs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cophongs in body
     */
    @RequestMapping(value = "/cophongs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cophong> getAllCophongs() {
        log.debug("REST request to get all Cophongs");
        List<Cophong> cophongs = cophongRepository.findAll();
        return cophongs;
    }

    /**
     * GET  /cophongs/:id : get the "id" cophong.
     *
     * @param id the id of the cophong to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cophong, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cophongs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cophong> getCophong(@PathVariable Long id) {
        log.debug("REST request to get Cophong : {}", id);
        Cophong cophong = cophongRepository.findOne(id);
        return Optional.ofNullable(cophong)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cophongs/:id : delete the "id" cophong.
     *
     * @param id the id of the cophong to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cophongs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCophong(@PathVariable Long id) {
        log.debug("REST request to delete Cophong : {}", id);
        cophongRepository.delete(id);
        cophongSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cophong", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cophongs?query=:query : search for the cophong corresponding
     * to the query.
     *
     * @param query the query of the cophong search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/cophongs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cophong> searchCophongs(@RequestParam String query) {
        log.debug("REST request to search Cophongs for query {}", query);
        return StreamSupport
            .stream(cophongSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
