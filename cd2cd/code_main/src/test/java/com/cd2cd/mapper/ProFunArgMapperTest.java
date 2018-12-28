package com.cd2cd.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cd2cd.domain.ProFunArg;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProFunArgMapperTest {
	
	private static final Logger log = LoggerFactory.getLogger(ProFunArgMapperTest.class);
	
	@Autowired
    private ProFunArgMapper funArgMapper;
	
	@Test
	public void test() {
		List<ProFunArg> list = funArgMapper.fetchFunArgsByFunId(8l);
		log.info("list={}", list);
	}
	
}
