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
	 * @parameter property="project"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * Directory containing the thrift-idl files.
	 * 
	 * @parameter property="scrooge.thrift.directory"
	 *            default-value="${project.basedir}/src/main/thrift"
	 * @required
	 */
	private File thriftDirectory;

	/**
	 * Directory where the generated source files are created.
	 * 
	 * @parameter 
	 *            default-value="${project.build.directory}/generated-sources/thrift"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * The language of the generated source files (either "java" or "scala")
	 * 
	 * @parameter property="scrooge.language" default-value="java"
	 * @required
	 */
	private String language;

	/**
	 * Whether an Ostrich server should be created.
	 * 
	 * @parameter property="scrooge.ostrich" default-value="false"
	 * @required
	 */
	private boolean withOstrichServer;

	/**
	 * Whether an Finagle client should be created.
	 * 
	 * @parameter property="scrooge.finagle.client" default-value="true"
	 */
	private boolean withFinagleClient;

	/**
	 * Whether an Finagle service should be created.
	 * 
	 * @parameter property="scrooge.finagle.service" default-value="true"
	 */
	private boolean withFinagleService;

	public void execute() throws MojoExecutionException, MojoFailureException {
		ScroogeWrapper scrooge = new ScroogeWrapper(project, thriftDirectory,
				outputDirectory, language, withOstrichServer,
				withFinagleClient, withFinagleService);
		scrooge.generate();
	}
}
