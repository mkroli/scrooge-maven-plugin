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

	/**
	 * Directory containing the thrift-idl files.
	 * 
	 * @parameter expression="${scrooge.thrift.directory}"
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
	 * @parameter expression="${scrooge.language}" default-value="java"
	 * @required
	 */
	private String language;

	/**
	 * Whether an Ostrich server should be created.
	 * 
	 * @parameter expression="${scrooge.ostrich}" default-value="false"
	 * @required
	 */
	private boolean withOstrichServer;

	public void execute() throws MojoExecutionException, MojoFailureException {
		ScroogeWrapper scrooge = new ScroogeWrapper(project, thriftDirectory,
				outputDirectory, language, withOstrichServer);
		scrooge.generate();
	}
}
