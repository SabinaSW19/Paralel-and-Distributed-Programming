using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Lab4.Implementation
{
    class AsyncTaskImpl
    {
        public AsyncTaskImpl(List<string> hosts)
        {
            List<Task> tasks = new List<Task>();
            foreach (string host in hosts)
            {
                tasks.Add(Task.Factory.StartNew(this.Start, host));
            }
            Task.WaitAll(tasks.ToArray());
            Thread.Sleep(1500);
        }

        private async void Start(object host)
        {
            string hostString = (string)host;

            var ipAddress = Dns.GetHostEntry(hostString.Split('/')[0]).AddressList[0];

            var endPoint = new IPEndPoint(ipAddress, 80);

            var clientSocket = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            await this.Connect(clientSocket, endPoint);

            await this.Send(clientSocket, hostString);

            byte[] buffer = new byte[1024];

            await this.Receive(clientSocket, buffer);

            clientSocket.Shutdown(SocketShutdown.Both);
            clientSocket.Close();
            //Console.WriteLine(Encoding.ASCII.GetString(buffer),0,1024);
            Utils.parse(buffer,hostString);
        }

        private async Task Connect(Socket clientSocket, IPEndPoint endpoint)
        {
            var promise = new TaskCompletionSource<int>();
            clientSocket.BeginConnect(endpoint, (IAsyncResult asyncResult) => promise.SetResult(this.EndConnect(clientSocket, asyncResult)), null);
            await promise.Task;
        }

        private int EndConnect(Socket clientSocket, IAsyncResult asyncResult)
        {
            clientSocket.EndConnect(asyncResult);
            return 0;
        }

        private async Task Send(Socket clientSocket, string host)
        {
            var promise = new TaskCompletionSource<int>();
            var bytes = Encoding.ASCII.GetBytes(Utils.getRequest(host));
            clientSocket.BeginSend(bytes, 0, bytes.Length, 0, (IAsyncResult asyncResult) => promise.SetResult(clientSocket.EndSend(asyncResult)), null);
            await promise.Task;
        }

        private async Task Receive(Socket clientSocket, byte[] buffer)
        {
            var promise = new TaskCompletionSource<int>();
            clientSocket.BeginReceive(buffer, 0, buffer.Length, 0, (IAsyncResult asyncResult) => promise.SetResult(clientSocket.EndReceive(asyncResult)), null);
            await promise.Task;
        }
    }
}
