package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.application.in.UpdateSwimmingPoolBasicInfoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수영장")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/basic-info")
@RequiredArgsConstructor
public class UpdateSwimmingPoolBasicInfoController {

   private final UpdateSwimmingPoolBasicInfoUseCase useCase;

   @Operation(summary = "수영장 기본 정보 수정")
   @PutMapping
   public void updateBasicInfo(
       @PathVariable("swimmingPoolId") Long swimmingPoolId,
       @Valid @RequestBody UpdateSwimmingPoolBasicInfoRequest request
   ) {
     useCase.updateBasicInfo(swimmingPoolId, request.toCommand());
   }
}
