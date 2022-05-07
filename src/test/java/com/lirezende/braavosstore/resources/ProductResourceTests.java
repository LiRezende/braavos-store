package com.lirezende.braavosstore.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lirezende.braavosstore.dto.ProductDTO;
import com.lirezende.braavosstore.factory.Factory;
import com.lirezende.braavosstore.services.ProductService;
import com.lirezende.braavosstore.services.exceptions.DatabaseException;
import com.lirezende.braavosstore.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourcesTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long validId;
    private Long invalidId;
    private Long relatedId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() throws Exception {

        validId = 1L;
        invalidId = 2L;
        relatedId = 3L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        doThrow(DatabaseException.class).when(productService).delete(any());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {

        when(productService.findAllPaged(any())).thenReturn(page);
        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenValidId() throws Exception {

        when(productService.findById(any())).thenReturn(productDTO);
        mockMvc.perform(get("/products/{id}", validId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenInvalidId() throws Exception {

        when(productService.findById(any())).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/products/{id}", invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductWhenValidId() throws Exception {

        when(productService.update(any(), any())).thenReturn(productDTO);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", validId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").exists())
                        .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void updateShouldReturnResourceNotFoundExceptionWhenInvalidId() throws Exception {

        when(productService.update(any(), any())).thenThrow(ResourceNotFoundException.class);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", invalidId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception {

        when(productService.insert(any())).thenReturn(productDTO);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void deleteShouldReturnNoContentWhenValidId() throws Exception {

        doNothing().when(productService).delete(any());

        mockMvc.perform(delete("/products/{id}", validId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNoFoundWhenInvalidId() throws Exception {

        doThrow(ResourceNotFoundException.class).when(productService).delete(any());

        mockMvc.perform(delete("/products/{id}", invalidId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
