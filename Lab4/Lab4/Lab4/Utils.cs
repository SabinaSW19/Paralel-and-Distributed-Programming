using System;
using System.Collections.Generic;
using System.Text;

namespace Lab4
{
    class Utils
    {
        public static string getRequest(string host)
        {
            string request = "GET ";
            if (host.Contains("/"))
                request += host.Substring(host.IndexOf("/"));
            else
                request += "/";
            request += " HTTP/1.1\r\nHost: "+host.Split("/")[0]+"\r\n";
            request += "Content-Length: 0\r\n\r\n";      
            return request;
        }

        public static void parse(byte[] buffer,String host)
        {
            string parsingResult=Encoding.ASCII.GetString(buffer, 0, buffer.Length);
            string[] lines = parsingResult.Split('\n');
            string header = "";
            foreach(string line in lines)
            {
                if (line != "\r")
                {
                    if (line.Contains("Content-Length"))
                    {

                        header += "Content-Length = " + Int32.Parse(line.Split(':')[1]) +" bytes\n";
                    }
                    else
                    {
                        header += line;
                        header += "\n";
                    }
                }
                else
                {
                    break;
                }
            }
            Console.WriteLine(header);
            Console.WriteLine(parsingResult);
            Console.WriteLine("Website is: " + host);
        }
    }
}
