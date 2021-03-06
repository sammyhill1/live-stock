package servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import general.CustomerAccount;
import general.Order;
import general.UserAccount;
import utils.MyUtils;

@WebServlet(urlPatterns = { "/doexport" })
public class DoExportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DoExportServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		// Check User has logged on
		UserAccount loginedUser = MyUtils.getLoginedUser(session);
		System.out.println("Logged in user is " + loginedUser);

		// Not logged in
		if (loginedUser == null) {

			// Redirect to login page.
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		Connection conn = MyUtils.getStoredConnection(request);

		Process p = null;
		try {
			File f = new File(".");;
			System.out.println(f.getAbsolutePath());
			// Change this accordingly
			String path = "..\\Documents\\programming\\cse305dumps\\";
			Runtime runtime = Runtime.getRuntime();
			p = runtime
					.exec("..\\..\\..\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump -uroot -ptoor --add-drop-database -B live-stock -r " + path + "dump" + ".sql");
			// change the dbpass and dbname with your dbpass and dbname
			int processComplete = p.waitFor();

			if (processComplete == 0) {

				System.out.println("Backup created successfully!");

			} else {
				System.out.println("Could not create the backup");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/manutilities.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public boolean backupDB(String dbName, String dbUserName, String dbPassword, String path) {

		String executeCmd = "mysqldump" + " -u " + dbUserName + " -p" + " --add-drop-database -B " + dbName + " -r "
				+ path;
		System.out.println(executeCmd);
		try {
			Process runtimeProcess = Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", executeCmd });

			int processComplete = runtimeProcess.waitFor();

			System.out.println(processComplete);

			if (processComplete == 0) {
				System.out.println("Backup Created Successfully !");
			} else {
				System.out.println("Couldn't Create the backup !");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}
}
