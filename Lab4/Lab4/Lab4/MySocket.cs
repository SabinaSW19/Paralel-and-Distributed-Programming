using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;

namespace Lab4
{
    class MySocket
    {
        public string host;
        public IPEndPoint endpoint;
        public Socket clientSocket;
        public byte[] buffer = new byte[1024];

        public MySocket(string host, IPEndPoint endpoint, Socket clientSocket)
        {
            this.host = host;
            this.endpoint = endpoint;
            this.clientSocket = clientSocket;
        }

    }
}
