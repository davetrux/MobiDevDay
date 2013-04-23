using System;
using System.Collections.Generic;
using System.ServiceModel;
using System.ServiceModel.Activation;
using System.ServiceModel.Web;
using PersonDotNetService.Models;
using System.Diagnostics;

namespace PersonDotNetService.Services
{
    [ServiceContract]
    [AspNetCompatibilityRequirements(RequirementsMode = AspNetCompatibilityRequirementsMode.Allowed)]
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.PerCall)]
    public class NameService
    {
        [WebInvoke(UriTemplate = "name/{gender}", Method = "GET", RequestFormat = WebMessageFormat.Json,
            ResponseFormat = WebMessageFormat.Json)]
        public Person GetSingleName(string gender)
        {
            var generator = new PersonGenerator();

            return generator.GetRandomName(gender);
        }

        [WebInvoke(UriTemplate = "names/{count}", Method = "GET", RequestFormat = WebMessageFormat.Json,
            ResponseFormat = WebMessageFormat.Json)]
        public List<Person> GetRandomPersons(string count)
        {
            var total = int.Parse(count);
            var result = new List<Person>();
            var generator = new PersonGenerator();

            for (var i = 0; i < total; i++)
            {
                var person = generator.GetRandomName();
                Debug.WriteLine("{0} {1}", person.firstName, person.lastName);
                result.Add(person);
            }

            return result;
        }
    }
}