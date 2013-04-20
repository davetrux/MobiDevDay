using System.Web.Mvc;
using System.Web.Security;
using PersonDotNetService.Models;

namespace PersonDotNetService.Controllers
{
    public class HomeController : Controller
    {
        //
        // GET: /Home/

        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Login(string returnUrl)
        {
            ViewBag.ReturnUrl = returnUrl;
            return View(new LoginModel());
        }

        [HttpPost]
        [AllowAnonymous]
        public ActionResult Login(LoginModel model, string returnUrl)
        {
            if (ModelState.IsValid)
            {
                if (FormsAuthentication.Authenticate(model.UserName, model.Password))
                {
                    FormsAuthentication.SetAuthCookie(model.UserName, false);
                    FormsAuthentication.RedirectFromLoginPage(model.UserName, false);
                }
                else
                {
                    
                }
            }
            return View();
        }
    }
}
