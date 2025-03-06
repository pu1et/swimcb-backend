package com.project.swimcb.bo.swimmingclass.domain.enums;

import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassType;

public enum CreateBoSwimmingClassSwimmingClassType {
  GROUP, KIDS_SWIMMING, AQUA_AEROBICS, SPECIAL_CLASS, PRIVATE_LESSON;

  public SwimmingClassType toSwimmingClassType() {
    return switch (this) {
      case GROUP -> SwimmingClassType.GROUP;
      case KIDS_SWIMMING -> SwimmingClassType.KIDS_SWIMMING;
      case AQUA_AEROBICS -> SwimmingClassType.AQUA_AEROBICS;
      case SPECIAL_CLASS -> SwimmingClassType.SPECIAL_CLASS;
      case PRIVATE_LESSON -> SwimmingClassType.PRIVATE_LESSON;
    };
  }
}
