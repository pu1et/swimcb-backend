package com.project.swimcb.oauth2.adapter.out;

import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.application.port.out.OAuth2Presenter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

@Service
class OAuth2Formatter implements OAuth2Presenter {

  @Override
  public OAuth2Response signup() {
    return new OAuth2Response(new RedirectView("https://cb-user.vercel.app"));
  }

  @Override
  public OAuth2Response login() {
    return new OAuth2Response(new RedirectView("https://cb-user.vercel.app"));
  }

}
