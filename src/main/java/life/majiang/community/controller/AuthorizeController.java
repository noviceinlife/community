package life.majiang.community.controller;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientsecret;
    @Value("${github.redirect.uri}")
    private String redirecturi;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                            HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("clientId");
        accessTokenDTO.setClient_secret("clientsecret");
        accessTokenDTO.setCode(code);
            accessTokenDTO.setRedirect_uri("redirecturi");
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.GetAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        if( user !=null){
            request.getSession().setAttribute("user",user);
            return "redirect:index";
            //登陆成功，写cookie和session
        }else{
            //登录失败，重新登录
            return "redirect:index";
        }
    }
}
