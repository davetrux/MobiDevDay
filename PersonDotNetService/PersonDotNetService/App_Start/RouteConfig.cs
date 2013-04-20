using System.ServiceModel.Activation;
using System.Web.Mvc;
using System.Web.Routing;
using PersonDotNetService.Services;

namespace PersonDotNetService
{
    public class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");

            routes.MapRoute(
                "Default", // Route name
                "{controller}/{action}/{id}", // URL with parameters
                new { controller = "Home", action = "Index", id = UrlParameter.Optional }, // Parameter defaults
                new { controller = "^(?!api).*" } //Allow for the WCF REST routes
            );

            routes.Add(new ServiceRoute("api", new WebServiceHostFactory(), typeof(NameService)));
        }
    }
}