package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.math.BigDecimal;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {
    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
    config = @SqlConfig(encoding = "utf-8"))


class FetchJeepTest {

  
  @Autowired
  private TestRestTemplate restTemplate;
  
  @LocalServerPort
  private int serverPort;

  @Test

  void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
    JeepModel model = JeepModel.WRANGLER;
    String trim = "Sport";
    String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);
    ResponseEntity<List<Jeep>> response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Jeep>>() {});
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<Jeep> expected = buildExpected();
    assertThat(response.getBody()).isEqualTo(expected);
    System.out.println(response.getBody());
  }
  
  protected List<Jeep> buildExpected(){
    List<Jeep> list = new LinkedList<Jeep>();
    
    // @formatter:off
    list.add(Jeep.builder()
        .modelId(JeepModel.WRANGLER)
        .trimLevel("Sport")
        .numDoors(2)
        .wheelSize(17)
        .basePrice(new BigDecimal("28475.00"))
        .build());
    
    list.add(Jeep.builder()
        .modelId(JeepModel.WRANGLER)
        .trimLevel("Sport")
        .numDoors(4)
        .wheelSize(17)
        .basePrice(new BigDecimal("31975.00"))
        .build());
    
    //@formatter:on
    return list;    
    
  }
}