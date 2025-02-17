package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassInstructorsResponse.Instructor;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Days;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Time;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Type;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/instructors")
@RequiredArgsConstructor
public class FindBoSwimmingClassInstructorsController {

  @Operation(summary = "클래스 데이터 관리 - 강사 리스트 조회")
  @GetMapping
  public FindBoSwimmingClassInstructorsResponse findBoInstructors() {
    return FindBoSwimmingClassInstructorsResponse.builder()
        .instructors(List.of(
            Instructor.builder()
                .instructorId(1L)
                .name("김민수")
                .build(),
            Instructor.builder()
                .instructorId(2L)
                .name("신수진")
                .build(),
            Instructor.builder()
                .instructorId(3L)
                .name("오동혁")
                .build()
        ))
        .build();
  }
}

