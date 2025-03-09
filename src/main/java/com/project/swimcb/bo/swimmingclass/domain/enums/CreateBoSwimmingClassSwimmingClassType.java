package com.project.swimcb.bo.swimmingclass.domain.enums;

import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;

public enum CreateBoSwimmingClassSwimmingClassType {
  GROUP, KIDS_SWIMMING, AQUA_AEROBICS, SPECIAL_CLASS, PRIVATE_LESSON;

  public SwimmingClassTypeName toSwimmingClassType() {
    return switch (this) {
      case GROUP -> SwimmingClassTypeName.GROUP;
      case KIDS_SWIMMING -> SwimmingClassTypeName.KIDS_SWIMMING;
      case AQUA_AEROBICS -> SwimmingClassTypeName.AQUA_AEROBICS;
      case SPECIAL_CLASS -> SwimmingClassTypeName.SPECIAL_CLASS;
      case PRIVATE_LESSON -> SwimmingClassTypeName.PRIVATE_LESSON;
    };
  }
}
