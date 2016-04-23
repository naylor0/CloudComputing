package ie.dit.naylor.mark;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class ViewServlet extends HttpServlet 
{
	// Populate arraylist from DB query then loop and print links
	public static ArrayList<DeleteServlet> files = DownloadServlet.files;
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		resp.getWriter().print("<html><body>");
		// Query datastore for all public images which will be shown for guest user
		FilterPredicate filter = new FilterPredicate("public", FilterOperator.EQUAL, "public");
		Query q = new Query("Image").setFilter(filter);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);
		
		// If there are no results from the query
		if(pq.countEntities() == 0)
		{
			resp.getWriter().println("<p><font size=\"4\">There are no public images on Picture Box</font></p>");
			resp.getWriter().println("<p><font size=\"4\"><a href=\"/login\">Sign in</a> to view private images</font></p>");
		}
		else
		{
			// print out the list of files and info gotten from the datastore in a table
			resp.getWriter().println("<font color=\"red\" size=\"6\">All Public Images On Flop Box</font>");
			resp.getWriter().println();
			resp.getWriter().print("<table align=\"left\" border=\"0\" style=\"width:60%\">");
			resp.getWriter().print("<tr><font size=\"4\"><th align=\"left\">Filename</th align=\"left\"><th align=\"left\">View</th align=\"left\"><th align=\"left\">Owner</th><th align=\"left\">Permissions</th></font></tr>");
			for (Entity result : pq.asIterable()) 
			{
				String fname = (String) result.getProperty("fname");
				String imagekey = (String) result.getProperty("imagekey");
				String owner = (String) result.getProperty("owner");
				String isPublic = (String) result.getProperty("public");

				//System.out.println(fname + ", " + imagekey + ", " + owner + ", " + isPublic);
				resp.getWriter().print("<tr>");
				resp.getWriter().print("<td><font size=\"4\">" + fname + "</font></td>");
				resp.getWriter().print("<td><font size=\"4\"><a href=\"serve?blob-key=" + imagekey + "\">   View</a></font></td>");
				resp.getWriter().print("<td><font size=\"4\">" + owner + "</font></td>");
				resp.getWriter().print("<td><font size=\"4\">" + isPublic + "</font></td>");
				resp.getWriter().print("</tr>");
			}
			resp.getWriter().println("<p><font size=\"5\"><a href=\""+ "index.html"+ "\">Go Back</a></p></font><br>");
		}
		resp.getWriter().print("</body></html>");
	}
}