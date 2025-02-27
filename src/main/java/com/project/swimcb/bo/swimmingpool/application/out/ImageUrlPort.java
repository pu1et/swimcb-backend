package com.project.swimcb.bo.swimmingpool.application.out;

import lombok.NonNull;

public interface ImageUrlPort {

  String getImageUrl(@NonNull String imagePath);
}
