package com.uvsq.cloud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CloudGenerator
 */
public class ServerContainerGenerator extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServerContainerGenerator() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String sqlFileUrl = request.getParameter("sqlFileUrl");

		String imageName = request.getParameter("imageName");

		StringBuilder sb = new StringBuilder();
		sb.append("FROM " + imageName + ":latest\n");
		sb.append("RUN apt-get update\n");
		sb.append("RUN apt-get -y upgrade\n");
		sb.append("ENV POSTGRES_USER dzTeam\n");
		sb.append("ENV POSTGRES_PASSWORD DZ123\n");
		sb.append("RUN apt-get -y install git\n");

		String[] paths = sqlFileUrl.split("/");

		sb.append("RUN git clone " + sqlFileUrl + ".git \n");
		sb.append("RUN cp " + paths[paths.length - 1] + "/*.sql sqlfile.sql\n");
		sb.append("ADD deploy.sh /docker-entrypoint-initdb.d/\n");
		sb.append("RUN chmod +x /docker-entrypoint-initdb.d/deploy.sh\n");

		String dockerFile = sb.toString();

		Process process = Runtime.getRuntime().exec("mkdir /home/fodil/Docker-Images/" + imageName + "/");

		try {
			String[] commande = { "cp", "-n", "/home/fodil/Docker-Images/deploy.sh",
					"/home/fodil/Docker-Images/" + imageName + "/deploy.sh" };

			Runtime.getRuntime().exec(commande).waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String appPwd;

		appPwd = "/home/fodil/Docker-Images/" + imageName + "/Dockerfile";

		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(appPwd), "utf-8"));

			writer.write(dockerFile);

			writer.close();

		} catch (IOException ex) {

		} finally {

		}

		process = Runtime.getRuntime()
				.exec("docker build -t fodilbenali/bdd_deploy2 /home/fodil/Docker-Images/" + imageName);

		StringBuilder output = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line;

		while ((line = reader.readLine()) != null) {

			output.append(line + "\n");
		}

		process = Runtime.getRuntime()
				.exec("sudo docker run --name \"postgs-bd2\" -p 25434:5432 -d fodilbenali/bdd_deploy2");

		StringBuilder output2 = new StringBuilder();

		BufferedReader reader2 = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line2;

		while ((line2 = reader2.readLine()) != null) {
			output2.append(line2 + "\n");
		}

		request.setAttribute("dockerOutput", output.toString());

		request.setAttribute("dockerFile", dockerFile);

		request.setAttribute("dockerOutputRun", output2.toString());

		request.getRequestDispatcher("/dockerGenerator.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
