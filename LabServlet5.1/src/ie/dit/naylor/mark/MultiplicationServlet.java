package ie.dit.naylor.mark;

import java.io.IOException;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class MultiplicationServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, I'm the multiplication servlet");
		String AString = req.getParameter("A");
		String BString = req.getParameter("B");
		if (AString == null || BString == null)
		{
			resp.getWriter().println("You didn't enter 2 numbers");
			// get the default A parameter from the Deployment Descriptor
			AString = getServletConfig().getInitParameter("A");
			// get the B parameter from the url which called the servlet
			// get the default B parameter from the Deployment Descriptor
			BString = getServletConfig().getInitParameter("B");
			int a = Integer.parseInt(AString);
			int b = Integer.parseInt(BString);
			int c = a * b;
			resp.getWriter().println("A:" + a + ", B: " + b + "   A + B: " + c);
		}
		else
		{
			boolean checka = isNumeric(AString);
			boolean checkb = isNumeric(BString);
			if (checka && checkb)
			{
				// get the default A parameter from the Deployment Descriptor
				AString = getServletConfig().getInitParameter("A");
				// get the B parameter from the url which called the servlet
				// get the default B parameter from the Deployment Descriptor
				BString = getServletConfig().getInitParameter("B");
				AString = req.getParameter("A");
				BString = req.getParameter("B");
				int a = Integer.parseInt(AString);
				int b = Integer.parseInt(BString);
				int c = a * b;
				resp.getWriter().println("A:" + a + ", B: " + b + "   A + B: " + c);
			}
			else
			{
				resp.getWriter().println("You didn't enter 2 numbers");
			}
		}
	}
	
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
}
