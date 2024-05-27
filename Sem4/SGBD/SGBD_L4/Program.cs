using Microsoft.Data.SqlClient;
using System.Runtime.InteropServices;

namespace SGBD_L4
{
    internal class Program
    {
        static int maxTries = 3;
        static string connectionString = "Data Source=DESKTOP-FHMI43Q\\SQLEXPRESS;" +
            "Initial Catalog=GradinaZoo;Integrated Security=true;TrustServerCertificate=true";


        static void Main(string[] args)
        {
            Thread t1 = new Thread(new ThreadStart(TransactionOne));
            Thread t2 = new Thread(new ThreadStart(TransactionTwo));

            t1.Start();
            t2.Start();

            t1.Join();
            t2.Join();

            Console.WriteLine("All transactions are done.");
            Console.ReadKey();
        }

        public static void TransactionOne()
        {
            int noOfTries = 1;

            Console.WriteLine("TransactionOne, try 1");

            while (!TransactionOne_Run())
            {
                noOfTries++;

                Console.WriteLine("TransactionOne, try " + noOfTries);

                if (noOfTries == maxTries)
                {
                    Console.WriteLine("TransactionOne aborted.");
                    return;
                }
            }
            Console.WriteLine("TransactionOne done.");
        }

        public static bool TransactionOne_Run()
        {
            bool success = false;

            using (SqlConnection con = new SqlConnection(connectionString))
            {
                try
                {
                    con.Open();

                    SqlCommand cmd = con.CreateCommand();

                    cmd.Connection = con;
                    cmd.CommandText = "EXEC deadlock_t1;";

                    cmd.ExecuteNonQuery();
                    success = true;
                } 
                catch (SqlException e)
                {
                    if (e.Number == 1205)
                    {
                        Console.WriteLine("TransactionOne failed.");
                    }
                }

                return success;
            }
        }

        public static void TransactionTwo()
        {
            int noOfTries = 1;

            Console.WriteLine("TransactionTwo, try 1");

            while (!TransactionTwo_Run())
            {
                noOfTries++;

                Console.WriteLine("TransactionTwo, try " + noOfTries);

                if (noOfTries == maxTries)
                {
                    Console.WriteLine("TransactionTwo aborted.");
                    return;
                }
            }

            Console.WriteLine("TransactionTwo done.");
        }

        public static bool TransactionTwo_Run()
        {
            bool success = false;

            using (SqlConnection con = new SqlConnection(connectionString))
            {
                try
                {
                    con.Open();

                    SqlCommand cmd = con.CreateCommand();

                    cmd.Connection = con;
                    cmd.CommandText = "EXEC deadlock_t2;";

                    cmd.ExecuteNonQuery();
                    success = true;
                }
                catch (SqlException e)
                {
                    if (e.Number == 1205)
                    {
                        Console.WriteLine("TransactionTwo failed.");
                    }
                }

                return success;
            }
        }
    }
}
