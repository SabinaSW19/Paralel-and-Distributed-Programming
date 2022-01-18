using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace Lab4.Implementation
{
    class CallbackImpl
    {
        public CallbackImpl(List<string> hosts)
        { 
            foreach (string host in hosts)
            {
                this.Start(host);
                Thread.Sleep(1500);
            }
        }

        private void Start(string host)
        {
            var ipAddress = Dns.GetHostEntry(host.Split('/')[0]).AddressList[0];

            var endPoint = new IPEndPoint(ipAddress, 80);

            var clientSocket = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            MySocket mySocket = new MySocket(host, endPoint, clientSocket);

            clientSocket.BeginConnect(endPoint, this.Connect, mySocket);

        }

        private void Connect(IAsyncResult ar)
        {
            var socket = (MySocket)ar.AsyncState;
            socket.clientSocket.EndConnect(ar);
            var bytes = Encoding.ASCII.GetBytes(Utils.getRequest(socket.host));
            socket.clientSocket.BeginSend(bytes, 0, bytes.Length, 0, this.Send, socket);

        }

        private void Send(IAsyncResult ar)
        {
            var socket = (MySocket)ar.AsyncState;
            socket.clientSocket.EndSend(ar);

            socket.clientSocket.BeginReceive(socket.buffer, 0,socket.buffer.Length,0, this.Receive, socket);
        }

        private void Receive(IAsyncResult ar)
        {
            var socket = (MySocket)ar.AsyncState;
            socket.clientSocket.EndReceive(ar);
            socket.clientSocket.Shutdown(SocketShutdown.Both);
            socket.clientSocket.Close();
            //Console.WriteLine(Encoding.ASCII.GetString(socket.buffer),0,1024);
            Utils.parse(socket.buffer,socket.host);
        }
    }
}
