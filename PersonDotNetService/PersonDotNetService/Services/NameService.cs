using System;
using System.Collections.Generic;
using System.ServiceModel;
using System.ServiceModel.Activation;
using System.ServiceModel.Web;
using PersonDotNetService.Models;

namespace PersonDotNetService.Services
{
    [ServiceContract]
    [AspNetCompatibilityRequirements(RequirementsMode = AspNetCompatibilityRequirementsMode.Allowed)]
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.PerCall)]
    public class NameService
    {
        public Person GetRandomName()
        {
            var result = new Person();
            var generator = new Random();

            var r = generator.Next(Constants.Surnames.Length);
            
            result.LastName = Constants.Surnames[r];

            if (r % 2 == 0)
            {
                r = generator.Next(Constants.Female.Length);
                result.FirstName=Constants.Female[r];
                result.Gender="f";
            }
            else
            {
                r = generator.Next(Constants.Male.Length);
                result.FirstName=Constants.Male[r];
                result.Gender="m";
            }

            return result;
        }

        [WebInvoke(UriTemplate = "name/{gender}", Method = "GET", RequestFormat = WebMessageFormat.Json,
            ResponseFormat = WebMessageFormat.Json)]
        public Person GetRandomName(string gender)
        {
            var result = new Person();
            var generator = new Random();

            var r = generator.Next(Constants.Surnames.Length);

            result.LastName=Constants.Surnames[r];

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

        [WebInvoke(UriTemplate = "names/{count}", Method = "GET", RequestFormat = WebMessageFormat.Json,
            ResponseFormat = WebMessageFormat.Json)]
        public List<Person> GetRandomPersons(string count)
        {
            var total = int.Parse(count);
            var result = new List<Person>();

            for (var i = 0; i < total; i++)
            {
                result.Add(GetRandomName());
            }

            return result;
        }
    }
}