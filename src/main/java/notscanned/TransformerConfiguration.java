package notscanned;

import com.example.Pojo;
import com.jayway.jsonpath.DocumentContext;
import com.toomuchcoding.jsonassert.JsonPath;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.app.http.source.HttpSource;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Transformer;

/**
 * @author Marcin Grzejszczak
 */
@Configuration
@EnableAutoConfiguration(exclude = HttpSource.class)
@EnableBinding(Processor.class)
public class TransformerConfiguration {

	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Pojo transform(String message) {
		DocumentContext parsedJson = com.jayway.jsonpath.JsonPath.parse(message);
		String username = parsedJson.read(JsonPath.builder().field("sender").field("login").jsonPath());
		String repo = parsedJson.read(JsonPath.builder().field("repository").field("full_name").jsonPath());
		return new Pojo(username, repo);
	}

}
