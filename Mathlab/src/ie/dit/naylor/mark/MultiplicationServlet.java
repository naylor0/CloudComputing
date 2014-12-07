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
		boolean checka = isNumeric(AString);
		boolean checkb = isNumeric(BString);
		if(checka && checkb)
		{
			int a = Integer.parseInt(AString);
			int b = Integer.parseInt(BString);
			int c = a * b;
			resp.getWriter().println("A:" + a + ", B: " + b + "   A + B: " + c);
		}
		else
		{
			resp.getWriter().println("You must enter 2 numbers to multiply");
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
