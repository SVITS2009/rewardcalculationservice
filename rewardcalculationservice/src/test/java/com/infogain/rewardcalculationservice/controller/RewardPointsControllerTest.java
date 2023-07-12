package com.infogain.rewardcalculationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infogain.rewardcalculationservice.dto.CreateCustomerRequest;
import com.infogain.rewardcalculationservice.dto.UpdateCustomerRequest;
import com.infogain.rewardcalculationservice.exceptions.CustomerExistException;
import com.infogain.rewardcalculationservice.service.RewardPointsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Implements to perform Junit testing to cover code coverage
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
@Transactional
class RewardPointsControllerTest {
    private static final String PATH = "/api/v1/reward-points";

    private MockMvc mockMvc;

    @Autowired
    private RewardPointsController rewardPointsController;

    @Autowired
    RewardPointsService rewardPointsService;

    @BeforeEach
    public void setup() throws CustomerExistException {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setName("Manish");
        createCustomerRequest.setAmountList(List.of(50d, 100d, 30d, 20d));
        rewardPointsService.createCustomer(createCustomerRequest);
    }

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(rewardPointsController)
                .build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void testCreateCustomerWith201StatusCode() throws Exception {
        String uri = PATH + "/customers";
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setName("Rajkumar");
        createCustomerRequest.setAmountList(List.of(20d, 40d));

        String inputJson = mapToJson(createCustomerRequest);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
    }

    @Test
    void testCreateCustomerWith400ErrorMessage() throws Exception {
        String uri = PATH + "/customers";
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setName("Sanish");
        String inputJson = mapToJson(createCustomerRequest);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
    }

    @Test
    void testUpdateCustomerWith200StatusCode() throws Exception {

        String postUri = PATH + "/customers";
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setName("Peter");
        createCustomerRequest.setAmountList(List.of(120d, 30d, 40d));

        String postInputJson = mapToJson(createCustomerRequest);
        mockMvc.perform(MockMvcRequestBuilders.post(postUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(postInputJson)).andReturn();

        String uri = PATH + "/customers/6";
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        updateCustomerRequest.setAmountList(List.of(120d, 30d, 40d));

        String inputJson = mapToJson(updateCustomerRequest);
        MvcResult mvcResultUpdate = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResultUpdate.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResultUpdate.getResponse().getContentAsString();
        assertNotNull(content);
    }

    @Test
    void testUpdateCustomerWith404StatusCode() throws Exception {
        String uri = PATH + "/customers/100";
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        updateCustomerRequest.setAmountList(List.of(101d, 102d));

        String inputJson = mapToJson(updateCustomerRequest);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
    }

    @Test
    void testGetCustomerByIdWith200StatusCode() throws Exception {

        String postUri = PATH + "/customers";
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setName("XYZ");
        createCustomerRequest.setAmountList(List.of(10d, 60d));

        String postInputJson = mapToJson(createCustomerRequest);
        mockMvc.perform(MockMvcRequestBuilders.post(postUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(postInputJson)).andReturn();
        String getUri = PATH + "/customer/11";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(getUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
    }

    @Test
    void testGetCustomerByIdWith404StatusCode() throws Exception {
        String getUri = PATH + "/customer/100";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(getUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
    }

    @Test
    void testFindAllTransactionOfLast3MonthsForEachCustomerWith200StatusCode() throws Exception {
        String getUri = PATH + "/monthly-rewards-point";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(getUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
    }

    @Test
    void testFindAllTransactionOfLast3MonthsForEachCustomerWithEmptyResponse() throws Exception {
        String getUri = PATH + "/monthly-rewards-point";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(getUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
    }

    @Test
    @Transactional
    void testDeleteCustomerWith200StatusCode() throws Exception {

        String postUri = PATH + "/customers";
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setName("ABC");
        createCustomerRequest.setAmountList(List.of(120d));

        String postInputJson = mapToJson(createCustomerRequest);
        mockMvc.perform(MockMvcRequestBuilders.post(postUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(postInputJson)).andReturn();
        String uri = PATH + "/customers/3";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Customer id : 3 deleted successfully", content);
    }

    @Test
    void testDeleteCustomerWith404StatusCode() throws Exception {
        String uri = PATH + "/customers/120";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }
}