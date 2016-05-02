package com.example.accurest.restdocs;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.codearte.accurest.dsl.GroovyDsl;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * @author Marcin Grzejszczak
 */
public class AccurestSnippet extends TemplatedSnippet {

	private static final Log log = LogFactory.getLog(AccurestSnippet.class);
	private static final String DEFAULT_ACCUREST_PATH = "/src/test/accurest/";

	private static final Map<File, Collection<GroovyDsl>> GROOVY_DSLS = new ConcurrentHashMap<>();
	private final File dirWithStubs;

	public AccurestSnippet() {
		this(DEFAULT_ACCUREST_PATH);
	}

	public AccurestSnippet(String dirWithStubs) {
		this(null, new File(AccurestSnippet.class.getResource(dirWithStubs).getFile()));
	}

	public AccurestSnippet(Map<String, Object> attributes, File dirWithStubs) {
		super("accurest-dsls", attributes);
		this.dirWithStubs = dirWithStubs;
		if (!GROOVY_DSLS.containsKey(dirWithStubs)) {
			GROOVY_DSLS.put(dirWithStubs, collectDsls(dirWithStubs));
		}
	}

	@Override
	protected Map<String, Object> createModel(Operation operation) {
		Map<String, Object> requestModel = new HashMap<>();

		return requestModel;
	}

	private Collection<GroovyDsl> collectDsls(File dirWithStubs) {
		try {
			AccurestFinder accurestFinder = new AccurestFinder();
			Files.walkFileTree(dirWithStubs.toPath(), accurestFinder);
			return accurestFinder.getAccurestContracts();
		}
		catch (IOException e) {
			log.error("Exception occurred while trying to parse the stubs", e);
			return Collections.emptyList();
		}
	}

	private static class AccurestFinder extends SimpleFileVisitor<Path> {

		private final PathMatcher matcher;
		private final Collection<GroovyDsl> accurestContracts = new ArrayList<>();

		AccurestFinder() {
			matcher = FileSystems.getDefault().getPathMatcher("glob:*.groovy");
		}

		void find(Path file) {
			Path name = file.getFileName();
			if (name != null && matcher.matches(name)) {
				try {
					// afterwards use: AccurestDslConverter
					CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
					compilerConfiguration.setSourceEncoding("UTF-8");
					GroovyDsl groovyDsl = (GroovyDsl) new GroovyShell(getClass().getClassLoader(), new Binding(), compilerConfiguration).evaluate(file.toFile());
					accurestContracts.add(groovyDsl);
				}
				catch (Exception e) {
					log.warn("Exception occurred while trying to parse the DSL", e);
				}
			}
		}

		Collection<GroovyDsl> getAccurestContracts() {
			return accurestContracts;
		}

		@Override
		public FileVisitResult visitFile(Path file,
				BasicFileAttributes attrs) {
			find(file);
			return CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
			find(dir);
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			log.warn("Exception occurred while traversing the directory for file [" + file + "]", exc);
			return CONTINUE;
		}
	}
}
