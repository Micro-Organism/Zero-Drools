package com.zero.drools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.drools.domain.entity.SystemUserEntity;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//方法二：使用 @AutoConfigureMockMvc 注解，这样就可以使用 @Autowired 即可注入 MockMvc 对象
//需要添加 addFilters = false 否则可能会导致走 AntBuservice 过滤器，导致需要登录，从而集成测试失败
//@AutoConfigureMockMvc(addFilters = false)
class ZeroDroolsBootApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KieSession kieSession;

    ObjectMapper objectMapper = new ObjectMapper();

    public static final String VISA = "VISA";
    public static final String MASTER_CARD = "MASTERCARD";
    public static final String ICICI = "ICICI";

    @Test
    public void shouldFireRuleWithVisa() {
        // given
        SystemUserEntity systemUserEntity = new SystemUserEntity();
        systemUserEntity.setCardType("VISA");
        systemUserEntity.setPrice(14000);
        kieSession.insert(systemUserEntity);

        // when
        kieSession.fireAllRules();

        // then
        assertEquals(14, systemUserEntity.getDiscount());
    }

    @Test
    public void shouldFireRuleWithMasterCard() {
        // given
        SystemUserEntity systemUserEntity = new SystemUserEntity();
        systemUserEntity.setCardType("MASTERCARD");
        systemUserEntity.setPrice(14000);
        kieSession.insert(systemUserEntity);

        // when
        kieSession.fireAllRules();

        // then
        assertEquals(10, systemUserEntity.getDiscount());
    }

    @Test
    public void shouldFireRuleWithICICI() {
        // given
        SystemUserEntity systemUserEntity = new SystemUserEntity();
        systemUserEntity.setCardType("ICICI");
        systemUserEntity.setPrice(3001);
        kieSession.insert(systemUserEntity);

        // when
        kieSession.fireAllRules();

        // then
        assertEquals(20, systemUserEntity.getDiscount());
    }

    @Test
    public void shouldApplyVISARule() throws Exception {
        //given
        SystemUserEntity systemUserEntity = new SystemUserEntity();
        systemUserEntity.setCardType(VISA);
        systemUserEntity.setPrice(11000);

        //when
        MockHttpServletRequestBuilder request = createPostOrder(systemUserEntity);
        String contentAsString = performAction(request);
        SystemUserEntity resultSystemUserEntity = objectMapper.readValue(contentAsString, SystemUserEntity.class);

        //then
        assertEquals(systemUserEntity.getCardType(), resultSystemUserEntity.getCardType());
        assertEquals(systemUserEntity.getPrice(), resultSystemUserEntity.getPrice());
        assertEquals(14, resultSystemUserEntity.getDiscount());
    }

    @Test
    public void shouldApplyMASTERCARDRule() throws Exception {
        //given
        SystemUserEntity systemUserEntity = new SystemUserEntity();
        systemUserEntity.setCardType(MASTER_CARD);
        systemUserEntity.setPrice(11000);

        //when
        MockHttpServletRequestBuilder request = createPostOrder(systemUserEntity);
        String contentAsString = performAction(request);
        SystemUserEntity resultSystemUserEntity = objectMapper.readValue(contentAsString, SystemUserEntity.class);

        //then
        assertEquals(systemUserEntity.getCardType(), resultSystemUserEntity.getCardType());
        assertEquals(systemUserEntity.getPrice(), resultSystemUserEntity.getPrice());
        assertEquals(10, resultSystemUserEntity.getDiscount());
    }

    @Test
    public void shouldApplyICICIRule() throws Exception {
        //given
        SystemUserEntity systemUserEntity = new SystemUserEntity();
        systemUserEntity.setCardType(ICICI);
        systemUserEntity.setPrice(11000);

        //when
        MockHttpServletRequestBuilder request = createPostOrder(systemUserEntity);
        String contentAsString = performAction(request);
        SystemUserEntity resultSystemUserEntity = objectMapper.readValue(contentAsString, SystemUserEntity.class);

        //then
        assertEquals(systemUserEntity.getCardType(), resultSystemUserEntity.getCardType());
        assertEquals(systemUserEntity.getPrice(), resultSystemUserEntity.getPrice());
        assertEquals(20, resultSystemUserEntity.getDiscount());
    }

    private MockHttpServletRequestBuilder createPostOrder(SystemUserEntity systemUserEntity) throws JsonProcessingException {
        return post("/rule")
                .content(objectMapper.writeValueAsBytes(systemUserEntity))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);

    }

    private String performAction(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


}
