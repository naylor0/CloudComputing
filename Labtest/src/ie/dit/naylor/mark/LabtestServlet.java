package ie.dit.naylor.mark;

import java.io.IOException;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class LabtestServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//	display welcome message
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, I'm the labtest servlet");
		resp.getWriter().println("");
		
		//	create strings
		String Astring = new String();
		String Bstring = new String();
		String Cstring = new String();
		String Dstring = new String();
		
		//	check if strings are null or empty
		boolean notemptyornull = checkempltyornull(req.getParameter("A"), req.getParameter("A"), req.getParameter("A"), req.getParameter("A")); 
		
		if(!notemptyornull)
		{
			//	read parameters into variables
			Astring = req.getParameter("A");
			Bstring = req.getParameter("B");
			Cstring = req.getParameter("C");
			Dstring = req.getParameter("D");
			//check if they are numeric
			boolean arenumeric = isNumeric(Astring, Bstring, Cstring, Dstring);
			if(arenumeric)
			{
				//change them to integers
				int a = Integer.parseInt(Astring);
				int b = Integer.parseInt(Bstring);
				int c = Integer.parseInt(Cstring);
				int d = Integer.parseInt(Dstring);
				int res = (a-b) * (c-d);
				//	print results
				resp.getWriter().println("A:" + a + ", B: " + b + ", C: " + c + ", D:" + d);
				resp.getWriter().println("(A-B) * (C-D) = (" + a + "-" + b + ") * (" + c + "-" + d + ") = " + res);
			}
			else
			{
				resp.getWriter().println("One of the values wasn't a number");
			}
		}
		else
		{
			resp.getWriter().println("One of the values was null or empty");
		}
	}
	
	public boolean isNumeric(String str0, String str1, String str2, String str3)
	{
		boolean result = true;
		char[] char0 = str0.toCharArray();
		char[] char1 = str1.toCharArray();
		char[] char2 = str2.toCharArray();
		char[] char3 = str3.toCharArray();
	    for (int i = 1; i < char0.length; i++)
	    {
	        if (!Character.isDigit(char0[i]))
	        {
	        	if( i == 0 && char0[i] == '-')
	        	{
	        		result = true;
	        	}
	        	else
	        	{
	        		result = false;
	        	}
	        }
	    }
	    for (int i = 1; i < char1.length; i++)
	    {
	        if (!Character.isDigit(char1[i]))
	        {
	        	if( i == 0 && char1[i] == '-')
	        	{
	        		result = true;
	        	}
	        	else
	        	{
	        		result = false;
	        	}
	        }
	    }
	    for (int i = 1; i < char2.length; i++)
	    {
	        if (!Character.isDigit(char2[i]))
	        {
	        	if( i == 0 && char2[i] == '-')
	        	{
	        		result = true;
	        	}
	        	else
	        	{
	        		result = false;
	        	}
	        }
	    }
	    for (int i = 1; i < char3.length; i++)
	    {
	        if (!Character.isDigit(char3[i]))
	        {
	        	if( i == 0 && char3[i] == '-')
	        	{
	        		result = true;
	        	}
	        	else
	        	{
	        		result = false;
	        	}
	        }
	    }
	    return result;
	}
	public boolean checkempltyornull(String Astring, String Bstring, String Cstring, String Dstring)
	{
		if(Astring == null || Astring == "" || Bstring == null || Bstring == "" || Cstring == null || Cstring == "" || Dstring == "" || Dstring == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
