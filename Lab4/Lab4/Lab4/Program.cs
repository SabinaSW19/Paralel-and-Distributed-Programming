using Lab4.Implementation;
using System;
using System.Collections.Generic;

namespace Lab4
{
    class Program
    {
        static void Main(string[] args)
        {
            List<string> hosts = new List<string> {
                "www.cs.ubbcluj.ro/~forest/",
                "www.cs.ubbcluj.ro/~motogna/LFTC/",
                "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/",
                "www.cs.ubbcluj.ro/~ilazar/ma/",
            };
            //CallbackImpl impl = new CallbackImpl(hosts);
            //TaskImpl impl1 = new TaskImpl(hosts);
            AsyncTaskImpl impl2 = new AsyncTaskImpl(hosts);

        }
    }
}
