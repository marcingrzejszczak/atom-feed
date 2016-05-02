package com.example.accurest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.Resource;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import com.example.DemoApplication;
import com.example.TransformerController;
import com.example.accurest.restdocs.AccurestSnippet;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

import io.codearte.accurest.messaging.AccurestMessaging;

import static org.springframework.restdocs.cli.CliDocumentation.curlRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebIntegrationTest
public class BaseClass {

	private static final String ACCUREST_PATH = "/accurest/";

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

	@Autowired TransformerController transformerController;
	@Inject AccurestMessaging messaging;

	@Value("classpath:/github-webhook-input/issue-created.json") Resource issueCreatedInput;
	@Value("classpath:/github-webhook-input/hook-created.json") Resource hookCreatedInput;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation).snippets()
				.withDefaults(curlRequest(), httpRequest(), httpResponse(),
						new AccurestSnippet(ACCUREST_PATH))).build();
		RestAssuredMockMvc.mockMvc(this.mockMvc);
		messaging.receiveMessage("output", 100, TimeUnit.MILLISECONDS);
	}

	public void createHook() throws IOException  {
		transformerController.transform(read(hookCreatedInput));
	}

	public void createHookV2() throws IOException  {
		createHook();
	}

	public void createIssue() throws IOException  {
		transformerController.transform(read(issueCreatedInput));
	}

	public void createIssueV2() throws IOException  {
		createIssue();
	}

	private String read(Resource resource) throws IOException {
		return FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
	}
}
