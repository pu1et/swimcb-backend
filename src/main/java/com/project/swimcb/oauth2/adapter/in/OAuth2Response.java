package com.project.swimcb.oauth2.adapter.in;

import lombok.NonNull;
import org.springframework.web.servlet.view.RedirectView;

public record OAuth2Response(@NonNull RedirectView redirectView) {

}
