package ie.dit.naylor.mark;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.api.server.spi.auth.common.User;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.images.Image;

public class UploadServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private com.google.appengine.api.blobstore.BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private BlobInfoFactory infoFactory = new BlobInfoFactory();
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    //blobstore.delete(blob_key);
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		// Set up userservice
		UserService userService = UserServiceFactory.getUserService();
		userService.getCurrentUser();
		String isPublic = req.getParameter("isPublic");
		@SuppressWarnings("deprecation")
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("myFile");
		if (blobKey == null) 
		{ 
			res.sendRedirect("/download");
		} 
		else 
		{
			BlobInfo info = infoFactory.loadBlobInfo(blobKey);
		    String fname = info.getFilename();
		    // create entity for each image which will store all attributes required
		    Entity image = new Entity("Image");
		    image.setProperty("fname", fname);
		    image.setProperty("owner", DownloadServlet.user);
		    image.setProperty("imagekey", blobKey.getKeyString());
		    image.setProperty("public", isPublic);
		    
		    // put entity in datastore
		    datastore.put(image);
		    res.sendRedirect("/download");
		}
	}
}