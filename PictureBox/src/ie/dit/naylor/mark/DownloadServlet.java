package ie.dit.naylor.mark;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.datanucleus.store.types.sco.backed.Map;

@SuppressWarnings("serial")
public class DownloadServlet extends HttpServlet 
{
	private com.google.appengine.api.blobstore.BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	// set up global user varible to use in upload servlet because myPrincipal.getname gave errors in that servlet
	public static String user = new String();
	public static ArrayList<DeleteServlet> files = new ArrayList<DeleteServlet>();
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		resp.getWriter().print("<html><body>");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		// set up user service and check if user is logged in
		UserService userService = UserServiceFactory.getUserService();
		userService.getCurrentUser();
		String thisURL = req.getRequestURI();
		String logoutURL = userService.createLogoutURL("/index.html");
		Principal myPrincipal = req.getUserPrincipal();
		user = myPrincipal.getName();
		String loginURL = userService.createLoginURL(thisURL);
		// if user isnt logged in then give them option to redirect to login page or continue as a guest
		if(myPrincipal == null) 
		{
			resp.getWriter().println("<font color=\"red\" size=\"4\">You are not logged in</font>");
			resp.getWriter().println("<font size=\"4\"><a href=\""+ loginURL + "\">Sign In Here</a></font>");
			resp.getWriter().println("<font size=\"4\"><a href=\"/view\"Continue as guest</a></font>");

		}
		// show admin page if user is me or Mark Deegan
		// from this page any image can be deleted regardless of owner
		if (myPrincipal.getName().equals("marknaylor2006@gmail.com") || myPrincipal.getName().equals("mark.deegan@dit.ie"))
		{
			resp.getWriter().println("<p><font color=\"red\" size=\"6\">Picture Box</font></p>");
			resp.getWriter().println("<p><font size=\"4\">You are logged in as: " + user + "</font></p><br>");
			resp.getWriter().println("<p><font size=\"4\"><a href=\"" + logoutURL +"\">Sign Out</a></p></font>");
			resp.getWriter().println("<p><font size=\"4\"><a href=\"upload.jsp\">Upload A File</a></font></p><br>");
			// query all images from database
			FilterPredicate filter = new FilterPredicate("owner", FilterOperator.NOT_EQUAL, " ");
			Query q = new Query("Image").setFilter(filter);
			// Use PreparedQuery interface to retrieve results
			PreparedQuery pq = datastore.prepare(q);
			
			// if theres no images let user know
			if(pq.countEntities() == 0)
			{
				resp.getWriter().println("<font size=\"4\">There are no images on Picture Box</font><br>");
			}
			// otherwise print a table with the images and links to view or delete all of them
			else
			{
				resp.getWriter().print("<table align=\"left\" border=\"0\" style=\"width:75%\">");
				resp.getWriter().print("<tr><font size=\"4\"><th align=\"left\">Filename</th align=\"left\"><th align=\"left\">View</th align=\"left\"><th align=\"left\">Owner</th><th align=\"left\">Permissions</th><th align=\"left\">Delete</th></font></tr>");
				for (Entity result : pq.asIterable()) 
				{
					String fname = (String) result.getProperty("fname");
					String imagekey = (String) result.getProperty("imagekey");
					String owner = (String) result.getProperty("owner");
					String isPublic = (String) result.getProperty("public");

					resp.getWriter().print("<tr>");
					resp.getWriter().print("<td><font size=\"4\">" + fname + "</font></td>");
					resp.getWriter().print("<td><font size=\"4\"><a href=\"serve?blob-key=" + imagekey + "\">   View</a></font></td>");
					resp.getWriter().print("<td><font size=\"4\">" + owner + "</font></td>");
					resp.getWriter().print("<td><font size=\"4\">" + isPublic + "</font></td>");
					resp.getWriter().print("<td><font size=\"4\"><a href=\"delete?blob-key=" + imagekey + "\">   Delete</a></font></td>");
					resp.getWriter().print("</tr>");
				}
			}
		}
		// for all other users show them all images but only let them delete their own images
		else if (myPrincipal.getName() != null)
		{
			resp.getWriter().println("<p><font color=\"red\" size=\"6\">Picture Box</font></p>");
			resp.getWriter().println("<p><font size=\"4\">You are logged in as: " + user + "</font></p><br>");
			resp.getWriter().println("<p><font size=\"4\"><a href=\"" + logoutURL +"\">Sign out</a></font></p>");
			resp.getWriter().println("<p><font size=\"4\"><a href=\"upload.jsp\">Upload A Image</a></font></p><br>");
			// again, query all images from datastore
			FilterPredicate filter = new FilterPredicate("owner", FilterOperator.NOT_EQUAL, " ");
			Query q = new Query("Image").setFilter(filter);
			// Use PreparedQuery interface to retrieve results
			PreparedQuery pq = datastore.prepare(q);
			
			// if no results then let user know
			if(pq.countEntities() == 0)
			{
				resp.getWriter().println("<p><font size=\"4\">There are no images on Picture Box</font></p><br>");
			}
			else
			{
				// create table to show images
				resp.getWriter().print("<table align=\"left\" border=\"0\" style=\"width:75%\">");
				resp.getWriter().print("<tr><font size=\"4\"><th align=\"left\">Filename</th align=\"left\"><th align=\"left\">View</th align=\"left\"><th align=\"left\">Owner</th><th align=\"left\">Permissions</th><th align=\"left\">Delete</th></font></tr>");
				for (Entity result : pq.asIterable()) 
				{
					String fname = (String) result.getProperty("fname");
					String imagekey = (String) result.getProperty("imagekey");
					String owner = (String) result.getProperty("owner");
					String isPublic = (String) result.getProperty("public");

					resp.getWriter().print("<tr>");
					resp.getWriter().print("<td><font size=\"4\">" + fname + "</font></td>");
					resp.getWriter().print("<td><font size=\"4\"><a href=\"serve?blob-key=" + imagekey + "\">   View</a></font></td>");
					resp.getWriter().print("<td><font size=\"4\">" + owner + "</font></td>");
					resp.getWriter().print("<td><font size=\"4\">" + isPublic + "</font></td>");
					// this time only show option to delete if user is the owner of the image
					if(user.equals(owner))
					{
						resp.getWriter().print("<td><font size=\"4\"><b><a href=\"delete?blob-key=" + imagekey + "\">   Delete</a></b></font></td>");
					}
					resp.getWriter().print("</tr>");
				}
			}
			
		}
		resp.getWriter().print("</body></html>");
	}
}