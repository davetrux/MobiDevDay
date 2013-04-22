using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace PersonDotNetService.Models
{
    public class PersonGenerator
    {
        public static int lastId = 44;

        public Person GetRandomName()
        {
            var result = new Person();
            var generator = new Random(lastId);

            var r = generator.Next(Constants.Surnames.Length);

            result.LastName = Constants.Surnames[r];

            if (r % 2 == 0)
            {
                r = generator.Next(Constants.Female.Length);
                result.FirstName = Constants.Female[r];
                result.Gender = "f";
            }
            else
            {
                r = generator.Next(Constants.Male.Length);
                result.FirstName = Constants.Male[r];
                result.Gender = "m";
            }
            lastId++;
            return result;
        }

        public Person GetRandomName(string gender)
        {
            var result = new Person();
            var generator = new Random();

            var r = generator.Next(Constants.Surnames.Length);

            result.LastName = Constants.Surnames[r];

            if (string.Equals(gender, "f", StringComparison.OrdinalIgnoreCase))
            {
                r = generator.Next(Constants.Female.Length);
                result.FirstName = Constants.Female[r];
                result.Gender = "f";
            }
            else
            {
                r = generator.Next(Constants.Male.Length);
                result.FirstName = Constants.Male[r];
                result.Gender = "m";
            }

            return result;
        }
    }


}