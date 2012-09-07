/*
 * Copyright 2012 Michael Krolikowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mkroli.maven.scrooge;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Generates classes from thrift-idl files using the scrooge compiler.
 * 
 * @goal scrooge
 * @phase generate-sources
 */
public class ScroogeMojo extends AbstractMojo {
	/**
	 * The default maven project object.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	public MavenProject getProject() {
		return project;
	}

	public void setProject(MavenProject project) {
		this.project = project;
	}

	/**
	 * Directory containing the thrift-idl files.
	 * 
	 * @parameter default-value="${project.basedir}/src/main/thrift"
	 * @required
	 */
	private File thriftDirectory;

	public File getThriftDirectory() {
		return thriftDirectory;
	}

	public void setThriftDirectory(File thriftDirectory) {
		this.thriftDirectory = thriftDirectory;
	}

	/**
	 * Directory where the generated source files are created.
	 * 
	 * @parameter 
	 *            default-value="${project.build.directory}/generated-sources/thrift"
	 * @required
	 */
	private File outputDirectory;

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/**
	 * The language of the generated source files (either "java" or "scala")
	 * 
	 * @parameter expression="${scrooge.language}" default-value="java"
	 * @required
	 */
	private String language;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Whether an Ostrich server should be created.
	 * 
	 * @parameter default-value="false"
	 * @required
	 */
	private boolean withOstrichServer;

	public boolean isWithOstrichServer() {
		return withOstrichServer;
	}

	public void setWithOstrichServer(boolean withOstrichServer) {
		this.withOstrichServer = withOstrichServer;
	}

	/**
	 * Whether an Finagle client should be created.
	 * 
	 * @parameter default-value="true"
	 * @required
	 */
	private boolean withFinagleClient;

	public boolean isWithFinagleClient() {
		return withFinagleClient;
	}

	public void setWithFinagleClient(boolean withFinagleClient) {
		this.withFinagleClient = withFinagleClient;
	}

	/**
	 * Whether an Finagle service should be created.
	 * 
	 * @parameter default-value="true"
	 * @required
	 */
	private boolean withFinagleService;

	public boolean isWithFinagleService() {
		return withFinagleService;
	}

	public void setWithFinagleService(boolean withFinagleService) {
		this.withFinagleService = withFinagleService;
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		Logger logger = new Logger() {
			public void info(String msg) {
				getLog().info(msg);
			}
		};
		ScroogeWrapper scrooge = new ScroogeWrapper(logger, thriftDirectory,
				outputDirectory, language, withOstrichServer,
				withFinagleClient, withFinagleService);
		try {
			scrooge.generate();
		} catch (Throwable t) {
			throw new MojoExecutionException(t.getMessage(), t);
		}
		project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
	}
}
