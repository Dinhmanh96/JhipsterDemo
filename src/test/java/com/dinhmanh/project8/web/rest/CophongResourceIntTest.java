package com.dinhmanh.project8.web.rest;

import com.dinhmanh.project8.Project8App;

import com.dinhmanh.project8.domain.Cophong;
import com.dinhmanh.project8.repository.CophongRepository;
import com.dinhmanh.project8.repository.search.CophongSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CophongResource REST controller.
 *
 * @see CophongResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Project8App.class)
public class CophongResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    @Inject
    private CophongRepository cophongRepository;

    @Inject
    private CophongSearchRepository cophongSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCophongMockMvc;

    private Cophong cophong;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CophongResource cophongResource = new CophongResource();
        ReflectionTestUtils.setField(cophongResource, "cophongSearchRepository", cophongSearchRepository);
        ReflectionTestUtils.setField(cophongResource, "cophongRepository", cophongRepository);
        this.restCophongMockMvc = MockMvcBuilders.standaloneSetup(cophongResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cophong createEntity(EntityManager em) {
        Cophong cophong = new Cophong()
                .name(DEFAULT_NAME)
                .age(DEFAULT_AGE)
                .phone(DEFAULT_PHONE)
                .address(DEFAULT_ADDRESS);
        return cophong;
    }

    @Before
    public void initTest() {
        cophongSearchRepository.deleteAll();
        cophong = createEntity(em);
    }

    @Test
    @Transactional
    public void createCophong() throws Exception {
        int databaseSizeBeforeCreate = cophongRepository.findAll().size();

        // Create the Cophong

        restCophongMockMvc.perform(post("/api/cophongs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cophong)))
                .andExpect(status().isCreated());

        // Validate the Cophong in the database
        List<Cophong> cophongs = cophongRepository.findAll();
        assertThat(cophongs).hasSize(databaseSizeBeforeCreate + 1);
        Cophong testCophong = cophongs.get(cophongs.size() - 1);
        assertThat(testCophong.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCophong.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testCophong.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCophong.getAddress()).isEqualTo(DEFAULT_ADDRESS);

        // Validate the Cophong in ElasticSearch
        Cophong cophongEs = cophongSearchRepository.findOne(testCophong.getId());
        assertThat(cophongEs).isEqualToComparingFieldByField(testCophong);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cophongRepository.findAll().size();
        // set the field null
        cophong.setName(null);

        // Create the Cophong, which fails.

        restCophongMockMvc.perform(post("/api/cophongs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cophong)))
                .andExpect(status().isBadRequest());

        List<Cophong> cophongs = cophongRepository.findAll();
        assertThat(cophongs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cophongRepository.findAll().size();
        // set the field null
        cophong.setAge(null);

        // Create the Cophong, which fails.

        restCophongMockMvc.perform(post("/api/cophongs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cophong)))
                .andExpect(status().isBadRequest());

        List<Cophong> cophongs = cophongRepository.findAll();
        assertThat(cophongs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = cophongRepository.findAll().size();
        // set the field null
        cophong.setPhone(null);

        // Create the Cophong, which fails.

        restCophongMockMvc.perform(post("/api/cophongs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cophong)))
                .andExpect(status().isBadRequest());

        List<Cophong> cophongs = cophongRepository.findAll();
        assertThat(cophongs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = cophongRepository.findAll().size();
        // set the field null
        cophong.setAddress(null);

        // Create the Cophong, which fails.

        restCophongMockMvc.perform(post("/api/cophongs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cophong)))
                .andExpect(status().isBadRequest());

        List<Cophong> cophongs = cophongRepository.findAll();
        assertThat(cophongs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCophongs() throws Exception {
        // Initialize the database
        cophongRepository.saveAndFlush(cophong);

        // Get all the cophongs
        restCophongMockMvc.perform(get("/api/cophongs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cophong.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getCophong() throws Exception {
        // Initialize the database
        cophongRepository.saveAndFlush(cophong);

        // Get the cophong
        restCophongMockMvc.perform(get("/api/cophongs/{id}", cophong.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cophong.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCophong() throws Exception {
        // Get the cophong
        restCophongMockMvc.perform(get("/api/cophongs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCophong() throws Exception {
        // Initialize the database
        cophongRepository.saveAndFlush(cophong);
        cophongSearchRepository.save(cophong);
        int databaseSizeBeforeUpdate = cophongRepository.findAll().size();

        // Update the cophong
        Cophong updatedCophong = cophongRepository.findOne(cophong.getId());
        updatedCophong
                .name(UPDATED_NAME)
                .age(UPDATED_AGE)
                .phone(UPDATED_PHONE)
                .address(UPDATED_ADDRESS);

        restCophongMockMvc.perform(put("/api/cophongs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCophong)))
                .andExpect(status().isOk());

        // Validate the Cophong in the database
        List<Cophong> cophongs = cophongRepository.findAll();
        assertThat(cophongs).hasSize(databaseSizeBeforeUpdate);
        Cophong testCophong = cophongs.get(cophongs.size() - 1);
        assertThat(testCophong.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCophong.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testCophong.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCophong.getAddress()).isEqualTo(UPDATED_ADDRESS);

        // Validate the Cophong in ElasticSearch
        Cophong cophongEs = cophongSearchRepository.findOne(testCophong.getId());
        assertThat(cophongEs).isEqualToComparingFieldByField(testCophong);
    }

    @Test
    @Transactional
    public void deleteCophong() throws Exception {
        // Initialize the database
        cophongRepository.saveAndFlush(cophong);
        cophongSearchRepository.save(cophong);
        int databaseSizeBeforeDelete = cophongRepository.findAll().size();

        // Get the cophong
        restCophongMockMvc.perform(delete("/api/cophongs/{id}", cophong.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean cophongExistsInEs = cophongSearchRepository.exists(cophong.getId());
        assertThat(cophongExistsInEs).isFalse();

        // Validate the database is empty
        List<Cophong> cophongs = cophongRepository.findAll();
        assertThat(cophongs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCophong() throws Exception {
        // Initialize the database
        cophongRepository.saveAndFlush(cophong);
        cophongSearchRepository.save(cophong);

        // Search the cophong
        restCophongMockMvc.perform(get("/api/_search/cophongs?query=id:" + cophong.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cophong.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }
}
